package com.github.avilysalAndCeltic.VampTra.map;

import java.util.ArrayList;

public class floorGenerator {
	private static int roomSize = 7; // this is width or length of room including 2 wall tiles
	private static int mapSize = 26*2+1; //26 rooms 'till border // this make a map of mapSize x mapSize rooms, keep this an odd number for player to spawn in a room, not wall
	private static int roomsTotal = 5; // will combine rooms until their total number equals this.... Why you don't work, roomsTotal...? Why?
	private static int doorChance = 15; //chance to create a door between rooms
	private static int spawnChance = 1; //chance the floor(' ') tile will change into spawn('s') tile
	
	public static Node[][] generateFloor(){
		Node[][] generated = new Node[roomSize*mapSize][roomSize*mapSize];
		Node[][] completeFloor = new Node[roomSize*mapSize][roomSize*mapSize];
		ArrayList<Room> roomList = new ArrayList<Room>();
		int rooms = 0;
		
		//generate walls('w') at regular interval, fill the rest of the map with floor tiles(' ') and spawn tiles('s')
		for(int i=0; i<generated.length; i++){
			for(int j=0; j<generated[i].length; j++){
				if((i+roomSize)%roomSize==0 || i%(roomSize)==roomSize-1 || (j+roomSize)%roomSize==0 || j%(roomSize)==roomSize-1)
					generated[i][j] = new Node(i*16, j*16, 'w');
				else
					generated[i][j] = new Node(i*16, j*16, ' ');
				
				// mark border nodes
				if(i==0 || j==0 || i==generated.length-1 || j==generated.length-1)
					generated[i][j].setOnBorder(true);
			}
		}
		
		//initialize rooms
		for(int i=0; i<mapSize*mapSize; i++)
			roomList.add(i,new Room(roomSize, roomSize));
		
		//mark rooms, including walls
		//go through each room in generated array
		for(int i=0; i<generated.length; i++){
			for(int j=0; j<generated[i].length; j++){
				//calculate which room the node belongs to
				int row = i/roomSize;
				int column = j/roomSize;
				int room = (mapSize * row) + column;
				//add the node to the room
				roomList.get(room).addNode(generated[i][j], (i+roomSize)%roomSize, (j+roomSize)%roomSize);
			}
		}
		
		//update bordering in rooms
		for(Room r : roomList)
			r.updateBorders();

		rooms = roomList.size();
		
		//constructing predesigned rooms
//		constructObelisk(roomList); 
//		constructStairs(roomList);
		constructCrypt(roomList);
		rooms -= 21;
		
		//expand room && remove unneeded walls
		while(rooms>roomsTotal){
			int expandInto;
			int ran = giveRandom(roomList.size());
			byte direction = (byte)giveRandom(4);
			
			//check if the room is suitable to expand in said direction, if not, pick another room, another direction.
			while(roomList.get(ran).getBorders()[direction] || roomList.get(ran).getExpansions()[direction]){ 
				//if border in said direction or has expanded there already, will return true
				ran = giveRandom(roomList.size());
				direction = (byte)giveRandom(4);
			}
			
			//calculate what room the expansion goes into  (needs redoing to take into consideration borders of the map)
			if(direction == 0)
				expandInto = ran-mapSize;
			else if(direction == 1)
				expandInto = ran+1;
			else if(direction == 2)
				expandInto = ran+mapSize;
			else
				expandInto = ran-1;
			
			if(roomList.get(ran).expand(roomList.get(expandInto), direction))
				rooms -= 1;
		}
		
		cleanUp(roomList); //moved cleaning into it's own function;
		
		//connect nearby two rooms that are not expanded into each other, doors
		for(Room r : roomList){
			if(giveChance() <= doorChance-1){
				byte direction = (byte)giveRandom(4);
				if(!r.getBorders()[direction] && !r.getExpansions()[direction]){
					int expandInto;
					if(direction == 0)
						expandInto = roomList.indexOf(r)-mapSize;
					else if(direction == 1)
						expandInto = roomList.indexOf(r)+1;
					else if(direction == 2)
						expandInto = roomList.indexOf(r)+mapSize;
					else
						expandInto = roomList.indexOf(r)-1;
					r.makeDoor(roomList.get(expandInto), direction);
				}
			}
		}
		
		//reconstruct map into Node[][] mode, based on rooms;
		for(int i=0; i<completeFloor.length; i++){
			for(int j=0; j<completeFloor[0].length; j++){
				int row = i/roomSize;
				int column = j/roomSize;
				int room = (mapSize * row) + column;
				completeFloor[i][j]=roomList.get(room).getNodes()[(i+roomSize)%roomSize][(j+roomSize)%roomSize];
				completeFloor[i][j].setType(roomList.get(room).getType());
			}
		}
		
		//"solidify" walls
		for(Node[] row : completeFloor)
			for(Node n : row){
				if(n.getName() == 'w') n.setTraversable(false);
				if(giveChance()<spawnChance && giveChance()<20 && n.getName() == ' ') n.setName('s');
			}
		
		//check if obelisk(if there is one) & stairs can be found from crypt, if not, redo.
		
		//test all tiles that can be accessed from crypt, assign blanks to the ones that can't
		// from map[floor][1][1] to map[floor][map[floor].length-2][map[floor][map[floor].length-2].length-2]
		
		//return reconstructed map;
		return completeFloor;
	}
	
	private static void cleanUp(ArrayList<Room> rooms){
		// create temporary version of a map to work with
		Node[][]temp = new Node[roomSize*mapSize][roomSize*mapSize];
		int iterationUpon = 2;
		int verificationCount = 0;
		
		for(int i=0; i<temp.length; i++){
			for(int j=0; j<temp[0].length; j++){
				int row = i/roomSize;
				int column = j/roomSize;
				int room = (mapSize * row) + column;
				temp[i][j]=rooms.get(room).getNodes()[(i+roomSize)%roomSize][(j+roomSize)%roomSize];
			}
		}
		
		// start filtering process, will go through the whole map looking for 2x2, 2x7, 7x2, 2x12, 
		// 12x2, 2x17 and 17x2 blocks that are straight and are not connected to any wall
		while(iterationUpon <= 2+4*roomSize){
			// find possible pillar
			boolean found = false;
			boolean vertical = false;
			for(int i=1; i<temp.length-iterationUpon; i++){
				for(int j=1; j<temp[i].length-iterationUpon; j++){
					if(temp[i][j].getName()=='w' && temp[i-1][j].getName() == ' ' && temp[i][j-1].getName() == ' '){
					// found possible pillar, verify
						verificationCount = 0;
						for(int v=0; v<iterationUpon; v++){
							if(temp[i-1][j].getName()==' ' && temp[i-1][j+1].getName()==' '){ //check for the floor above
								if(temp[i+iterationUpon][j].getName()==' ' && temp[i+iterationUpon][j+1].getName()==' '){ //check for the floor below
									if(temp[i+v][j].getName()=='w' && temp[i+v][j+1].getName()=='w'){ //check if it's 2 x itereationUpon block
										if(temp[i+v][j-1].getName()==' ' && temp[i+v][j+2].getName()==' '){ //check for floor to the sides
											verificationCount++;
											if(verificationCount == iterationUpon){ //checked it wholly
												found = true;
												vertical = true;
											}
										}
									}
								}
							}
						}
						verificationCount = 0;
						for(int h=0; h<iterationUpon; h++){
							if(temp[i][j-1].getName()==' ' && temp[i+1][j-1].getName()==' '){ //check for the to the left
								if(temp[i][j+iterationUpon].getName()==' ' && temp[i+1][j+iterationUpon].getName()==' '){ //check for the floor to the right
									if(temp[i][j+h].getName()=='w' && temp[i+1][j+h].getName()=='w'){ //check if it's itereationUpon x 2 block
										if(temp[i-1][j+h].getName()==' ' && temp[i+2][j+h].getName()==' '){ //check for floor below and above
											verificationCount++;
											if(verificationCount == iterationUpon){ //checked it wholy
												found = true;
												vertical = false;
											}
										}
									}
								}
							}
						}
						if(found){ 
							// verified
							if(vertical){
								//corrections on vertical pillar
								for(int v=0; v<iterationUpon; v++){
									temp[i+v][j].setName(' ');
									temp[i+v][j+1].setName(' ');
								}
							} else {
								//corrections on horizontal pillar
								for(int h=0; h<iterationUpon; h++){
									temp[i][j+h].setName(' ');
									temp[i+1][j+h].setName(' ');
								}
							}
						}
					} 
					found = false; // rinse, repeat
				}
			}
			iterationUpon+=roomSize;
		}
	}
	
	private static void constructCrypt(ArrayList<Room> roomList){
		// + shape with the center of the + at the center of the map
		final int CRI = (int)(roomList.size()/2); // Central Room Index double
		roomList.get(CRI).setType("crypt");
		
		roomList.get(CRI).expand(roomList.get(CRI-mapSize),(byte) 0);
		roomList.get(CRI).expand(roomList.get(CRI+1),(byte) 1);
		roomList.get(CRI).expand(roomList.get(CRI+mapSize),(byte) 2);
		roomList.get(CRI).expand(roomList.get(CRI-1),(byte) 3);
		// expanding that + to a #
		roomList.get(CRI-mapSize).expand(roomList.get(CRI-mapSize+1),(byte) 1);
		roomList.get(CRI-mapSize+1).expand(roomList.get(CRI-mapSize+2),(byte) 1);
		roomList.get(CRI-mapSize+1).expand(roomList.get(CRI-mapSize*2+1),(byte) 0);
		roomList.get(CRI-mapSize+1).expand(roomList.get(CRI+1),(byte) 2);
		roomList.get(CRI+1).expand(roomList.get(CRI+mapSize+1),(byte) 2);
		roomList.get(CRI+mapSize+1).expand(roomList.get(CRI+mapSize*2+1),(byte) 2);
		roomList.get(CRI+mapSize+1).expand(roomList.get(CRI+mapSize+2),(byte) 1);
		roomList.get(CRI+mapSize+1).expand(roomList.get(CRI+mapSize),(byte) 3);
		roomList.get(CRI+mapSize).expand(roomList.get(CRI+mapSize-1),(byte) 3);
		roomList.get(CRI+mapSize-1).expand(roomList.get(CRI+mapSize-2),(byte) 3);
		roomList.get(CRI+mapSize-1).expand(roomList.get(CRI+mapSize*2-1),(byte) 2);
		roomList.get(CRI+mapSize-1).expand(roomList.get(CRI-1),(byte) 0);
		roomList.get(CRI-1).expand(roomList.get(CRI-mapSize-1),(byte) 0);
		roomList.get(CRI-mapSize-1).expand(roomList.get(CRI-mapSize*2-1),(byte) 0);
		roomList.get(CRI-mapSize-1).expand(roomList.get(CRI-mapSize-2),(byte) 3);
		roomList.get(CRI-mapSize-1).expand(roomList.get(CRI-mapSize),(byte) 1);
		// finishing the design layout
		roomList.get(CRI-mapSize*2+1).expand(roomList.get(CRI-mapSize*2+2),(byte) 1);
		roomList.get(CRI-mapSize*2+2).expand(roomList.get(CRI-mapSize+2),(byte) 2);
		roomList.get(CRI+mapSize+2).expand(roomList.get(CRI+mapSize*2+2),(byte) 2);
		roomList.get(CRI+mapSize*2+2).expand(roomList.get(CRI+mapSize*2+1),(byte) 3);
		roomList.get(CRI+mapSize*2-1).expand(roomList.get(CRI+mapSize*2-2),(byte) 3);
		roomList.get(CRI+mapSize*2-2).expand(roomList.get(CRI+mapSize-2),(byte) 0);
		roomList.get(CRI-mapSize-2).expand(roomList.get(CRI-mapSize*2-2),(byte) 0);
		roomList.get(CRI-mapSize*2-2).expand(roomList.get(CRI-mapSize*2-1),(byte) 1);
		// finishing it all up, locking expansions
		roomList.get(CRI-mapSize*2+1).setExpanded((byte)3, true);
		roomList.get(CRI-mapSize*2+1).setExpanded((byte)0, true);
		roomList.get(CRI-mapSize*3+1).setExpanded((byte)2, true);
		roomList.get(CRI-mapSize*2).setExpanded((byte)1, true);
		roomList.get(CRI-mapSize*2+2).setExpanded((byte)0, true);
		roomList.get(CRI-mapSize*2+2).setExpanded((byte)1, true);
		roomList.get(CRI-mapSize*3+2).setExpanded((byte)2, true);
		roomList.get(CRI-mapSize*2+3).setExpanded((byte)3, true);
		roomList.get(CRI-mapSize+2).setExpanded((byte)1, true);
		roomList.get(CRI-mapSize+2).setExpanded((byte)2, true);
		roomList.get(CRI-mapSize+3).setExpanded((byte)3, true);
		roomList.get(CRI+2).setExpanded((byte)0, true);
		roomList.get(CRI+mapSize+2).setExpanded((byte)0, true);
		roomList.get(CRI+mapSize+2).setExpanded((byte)1, true);
		roomList.get(CRI+2).setExpanded((byte)2, true);
		roomList.get(CRI+mapSize+3).setExpanded((byte)3, true);
		roomList.get(CRI+mapSize*2+2).setExpanded((byte)1, true);
		roomList.get(CRI+mapSize*2+2).setExpanded((byte)2, true);
		roomList.get(CRI+mapSize*2+3).setExpanded((byte)3, true);
		roomList.get(CRI+mapSize*3+2).setExpanded((byte)0, true);
		roomList.get(CRI+mapSize*2+1).setExpanded((byte)2, true);
		roomList.get(CRI+mapSize*2+1).setExpanded((byte)3, true);
		roomList.get(CRI+mapSize*3+1).setExpanded((byte)0, true);
		roomList.get(CRI+mapSize*2).setExpanded((byte)1, true);
		roomList.get(CRI+mapSize*2-1).setExpanded((byte)1, true);
		roomList.get(CRI+mapSize*2-1).setExpanded((byte)2, true);
		roomList.get(CRI+mapSize*2).setExpanded((byte)3, true);
		roomList.get(CRI+mapSize*3-1).setExpanded((byte)0, true);
		roomList.get(CRI+mapSize*2-2).setExpanded((byte)2, true);
		roomList.get(CRI+mapSize*2-2).setExpanded((byte)3, true);
		roomList.get(CRI+mapSize*3-2).setExpanded((byte)0, true);
		roomList.get(CRI+mapSize*2-3).setExpanded((byte)1, true);
		roomList.get(CRI+mapSize-2).setExpanded((byte)3, true);
		roomList.get(CRI+mapSize-2).setExpanded((byte)0, true);
		roomList.get(CRI+mapSize-3).setExpanded((byte)1, true);
		roomList.get(CRI-2).setExpanded((byte)2, true);
		roomList.get(CRI-mapSize-2).setExpanded((byte)2, true);
		roomList.get(CRI-mapSize-2).setExpanded((byte)3, true);
		roomList.get(CRI-2).setExpanded((byte)0, true);
		roomList.get(CRI-mapSize-3).setExpanded((byte)1, true);
		roomList.get(CRI-mapSize*2-2).setExpanded((byte)3, true);
		roomList.get(CRI-mapSize*2-2).setExpanded((byte)0, true);
		roomList.get(CRI-mapSize*2-3).setExpanded((byte)1, true);
		roomList.get(CRI-mapSize*3-2).setExpanded((byte)2, true);
		roomList.get(CRI-mapSize*2-1).setExpanded((byte)0, true);
		roomList.get(CRI-mapSize*2-1).setExpanded((byte)1, true);
		roomList.get(CRI-mapSize*3-1).setExpanded((byte)2, true);
		roomList.get(CRI-mapSize*2).setExpanded((byte)3, true);
		//finally, make doors
		roomList.get(CRI-mapSize).makeDoor(roomList.get(CRI-mapSize*2),(byte) 0);
		roomList.get(CRI+1).makeDoor(roomList.get(CRI+2),(byte) 1);
		roomList.get(CRI+mapSize).makeDoor(roomList.get(CRI+mapSize*2),(byte) 2);
		roomList.get(CRI-1).makeDoor(roomList.get(CRI-2),(byte) 3);
	}
	
	private static int giveRandom(int upTo){
		return	com.github.avilysalAndCeltic.VampTra.logic.GamePlay.rend.random.nextInt(upTo);
	}
	
	private static int giveChance(){
		return com.github.avilysalAndCeltic.VampTra.logic.GamePlay.rend.random.nextInt(100);
	}
}