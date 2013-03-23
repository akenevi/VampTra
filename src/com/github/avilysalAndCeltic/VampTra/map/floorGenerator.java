package com.github.avilysalAndCeltic.VampTra.map;

import java.util.ArrayList;

public class floorGenerator {
	private static int roomSize = 5; // this is width or length of room including 2 wall tiles
	private static int mapSize = 15; // this make a map of mapSize x mapSize rooms, keep this an odd number for player to spawn in a room, not wall
	private static int roomsTotal = 5; // will combine rooms until their total number equals this
	private static int doorChance = 5; //chance to create a door bewteen rooms
	
	public static Node[][] generateFloor(){
		Node[][] generated = new Node[roomSize*mapSize][roomSize*mapSize];
		Node[][] completeFloor = new Node[roomSize*mapSize][roomSize*mapSize];
		ArrayList<Room> roomList = new ArrayList<Room>();
		int rooms = 0;
		
		//generate walls('w') at regular interval, fill the rest of the map with floor tiles('f') and spawn tiles('s')
		for(int i=0; i<generated.length; i++){
			for(int j=0; j<generated[i].length; j++){
				if((i+roomSize)%roomSize==0 || i%(roomSize)==roomSize-1 || (j+roomSize)%roomSize==0 || j%(roomSize)==roomSize-1)
					generated[i][j] = new Node(i*16, j*16, 'w');
				else{
					char name = 'f';
					
					generated[i][j] = new Node(i*16, j*16, name);
				}
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
		//constructing crypt
		{
			roomList.get(roomList.size()/2).expand(roomList.get(roomList.size()/2-mapSize), (byte) 0);
			roomList.get(roomList.size()/2).expand(roomList.get(roomList.size()/2+1), (byte) 1);
			roomList.get(roomList.size()/2).expand(roomList.get(roomList.size()/2+mapSize), (byte) 2);
			roomList.get(roomList.size()/2).expand(roomList.get(roomList.size()/2-1), (byte) 3);
			rooms -= 4;
		}
		
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
			roomList.get(ran).expand(roomList.get(expandInto), direction);
			
			rooms -= 1;
		}
		//connect nearby two rooms that are not expanded into eachother
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
			}
		}
		
		//clean up 2x2 "pillars"
		for(int i=1; i<completeFloor.length-2; i++){
			for(int j=1; j<completeFloor[i].length-2; j++){
				if(	completeFloor[i][j].getName() == 'w' && //find possible pillar
					completeFloor[i][j+1].getName() == 'w' &&
					completeFloor[i+1][j].getName() == 'w' &&
					completeFloor[i+1][j+1].getName() == 'w' &&
					completeFloor[i-1][j].getName() == 'f' && //verify, check for floor up
					completeFloor[i-1][j+1].getName() == 'f' && //check for floor up
					completeFloor[i+2][j].getName() == 'f' && //check for floor down
					completeFloor[i+2][j+1].getName() == 'f' && //check for floor down
					completeFloor[i][j-1].getName() == 'f' && //check for floor to the left
					completeFloor[i+1][j-1].getName() == 'f' && //check for floor to the left
					completeFloor[i][j+2].getName() == 'f' && //check for floor to the right
					completeFloor[i+1][j+2].getName() == 'f'){ //check for floor to the right
						completeFloor[i][j].setName('f');
						completeFloor[i][j+1].setName('f');
						completeFloor[i+1][j].setName('f');
						completeFloor[i+1][j+1].setName('f');
				}
			}
		}
		
		
		
		//"solidify" walls
		for(Node[] row : completeFloor)
			for(Node n : row){
				if(n.getName() == 'w') n.setPassable(false);
				if(giveChance()<100 && n.getName() == 'f') n.setName('s');
			}
		//return reconstructed map;
		return completeFloor;
	}
	
	private static int giveRandom(int upTo){
		return	com.github.avilysalAndCeltic.VampTra.logic.GamePlay.rend.random.nextInt(upTo);
	}
	
	private static int giveChance(){
		return com.github.avilysalAndCeltic.VampTra.logic.GamePlay.rend.random.nextInt(100);
	}
}