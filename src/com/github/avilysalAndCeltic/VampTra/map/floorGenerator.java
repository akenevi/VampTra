package com.github.avilysalAndCeltic.VampTra.map;

import java.util.ArrayList;

public class floorGenerator {
	private static int roomSize = 5; // this is width or length of room including 2 wall tiles
	private static int mapSize = 5; // this make a map of mapSize x mapSize rooms
	private static int roomsTotal = 5; // will combine rooms until their total number equals this
	
	public static Node[][] generateFloor(){
		Node[][] generated = new Node[roomSize*mapSize][roomSize*mapSize];
		ArrayList<Room> roomNum = new ArrayList<Room>();
		
		//generate walls at regular interval, fill the rest of the map with floor tiles
		for(int i=0; i<generated.length; i++){
			for(int j=0; j<generated[i].length; j++){
				if((i+roomSize)%roomSize==0 || i%(roomSize)==roomSize-1 || (j+roomSize)%roomSize==0 || j%(roomSize)==roomSize-1)
					generated[i][j] = new Node(i*16, j*16, 'w');
				else{
					char name = 'f';
					if(giveChance()<5) name = 's';
					generated[i][j] = new Node(i*16, j*16, name);
				}
			}
		}
		
		//initialize rooms
		for(int i=0; i<mapSize*mapSize*20; i++)
			roomNum.add(i,new Room(roomSize, roomSize));
		
		//mark rooms, including walls
		
		//go through each room in generated array
		for(int i=0; i<generated.length; i++){
			for(int j=0; j<generated[i].length; j++){
				//calculate which room the node belongs to
				int row = i/roomSize;
				int column = j/roomSize;
				int room = (mapSize * row) + column;
				//add the node to the room
				roomNum.get(room).addNode(generated[i][j], (i+roomSize)%roomSize, (j+roomSize)%roomSize);
			}
		}
		//make crypt in the center of the map
		
/*		
		while(roomNum.size()>roomsTotal){
			//expand room && remove unneeded walls
			int r = giveRandom(roomNum.size());
			int direction = giveRandom(4);
			//calculate what room the expansion goes into
			roomNum.get(r).expand(roomNum.get(), direction);
			
			//decrease roomList.size()
		//	roomList.trimToSize();
		}
		
*/		//reconstruct map into Node[][] mode, based on rooms;
		//return reconstructed map;
		return generated;
	}
	
	private static int giveRandom(int upTo){
		return	com.github.avilysalAndCeltic.VampTra.logic.GamePlay.rend.random.nextInt(upTo);
	}
	
	private static int giveChance(){
		return com.github.avilysalAndCeltic.VampTra.logic.GamePlay.rend.random.nextInt(101);
	}
}