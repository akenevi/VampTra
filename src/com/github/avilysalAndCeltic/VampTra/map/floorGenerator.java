package com.github.avilysalAndCeltic.VampTra.map;

public class floorGenerator {
	//chance to generate, percentage
	private static int corridor = 21;
	private static int crypt = 5;
	private static int stairs = 2;
	
	
	public static Node[][] generateFloor(Node[][] floor){
		//moved code to separate function for easier testing, will be moved back later
	//	Node[][] temp = generation(floor); 
	//	return temp;
		return floor;
	}
	
	/*	PSEUDO
	 * 	crypt and room with access to the next floor should be made from template
	 * 
	 * 	generate room (corridor is also a room)
	 * 	find door
	 * 	set (x,y) based of door's (x,y)
	 * 	increase floor array and add the room
	 * 	repeat until no more doors && there's access to the next floor
	 */
	
	private static int getRan(){
		int ran = com.github.avilysalAndCeltic.VampTra.logic.GamePlay.rend.random.nextInt(100);
		return ran;
	}
	
	private static Node[][] generation(Node[][] floor){
		int ran = getRan();
		if(ran < corridor) { //generate corridor
			/*
			 * chance to fork off
			 * chance to create a door
			 */
		}
		else { //generate room
			int r = getRan();
			if(r < stairs){ //generate stairs
				
			}
			else if(r > stairs && r < crypt){ //generate crypt
				
			}
			else{ //generate normal room
				
			}
		}
		//find door
		for(Node[] row : floor)
			for(Node n : row){
				if(n.name == 'd'){
					if(n.direction == 'n'){} // start adding from row above
					if(n.direction == 's'){} // start adding from row below
					if(n.direction == 'w'){} // start adding from column to the left
					if(n.direction == 'e'){} // start adding from column to the right
				}
			}
		return floor;
	}
}
