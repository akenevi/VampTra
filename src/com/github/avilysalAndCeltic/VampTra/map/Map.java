package com.github.avilysalAndCeltic.VampTra.map;

/*	This should have check, to see if it fits on the whole map,
 * 	passing all the needed info to renderer, passing values to
 * 	pathfinding algorithm(if needed)..?
 */
import com.github.avilysalAndCeltic.VampTra.map.floorGenerator;

public class Map{
	public volatile static Node[][][] map; // map[floor][row][column]; floor starting on 10 (map[9]);
	private int currentFloor = 0;
	private Node stairsUp = null;
	private Node stairsDown = null;
	private static float[] offsX;
	private static float[] offsY;
	private float pxSpeed = 0; 
	private float pySpeed = 0;

	public floorGenerator fGen = new floorGenerator();
	
	public Map(){
		Thread mapThread = new Thread(fGen);
		mapThread.setDaemon(true);
		mapThread.setName("mapGenerationThread");
		mapThread.start();
		
		map = new Node[10][0][0];
		offsX = new float[10];
		offsY = new float[10];
		for(int i=0; i < 10; i++){ //set offset to the lower left corner of the dungeon
			offsX[i] = com.github.avilysalAndCeltic.VampTra.logic.GamePlay.DW/2;
			offsY[i] = com.github.avilysalAndCeltic.VampTra.logic.GamePlay.DH/2;
		}
	}
	
	//public Node[][] getFloor(int floor){
	//	return map[floor];
	//}
	
	public static void adjustOffset(int floor){
		offsX[floor] -= map[floor].length/2*16; //set offset to the center of the floor
		offsY[floor] -= map[floor][0].length/2*16;
	}
		
	public boolean changeOffset(int floor, byte direction, float amount){
		float px = com.github.avilysalAndCeltic.VampTra.logic.GamePlay.player.getX();
		float py = com.github.avilysalAndCeltic.VampTra.logic.GamePlay.player.getY();
		for(int i=1; i<map[floor].length-1; i++){
			for(int j=1; j<map[floor][i].length-1; j++){
				if(map[floor][i][j].getX()+offsX[floor]==px && map[floor][i][j].getY()+offsY[floor]==py){ //is this the node we're looking for ?
					if(direction == 3 && map[floor][i-1][j].isTraversable()){ //can we pass through the node we're going into, based on direction
						pxSpeed += amount;
						if(pxSpeed >= 16){
							offsX[floor]+=16;
							pxSpeed = 0;
						}
						return true;
					}
					if(direction == 1 && map[floor][i+1][j].isTraversable()){
						pxSpeed -= amount;
						if(pxSpeed <= -16){
							offsX[floor]-=16;
							pxSpeed = 0;
						}
						return true;
					}
					if(direction == 2 && map[floor][i][j-1].isTraversable()){
						pySpeed += amount;
						if(pySpeed >= 16){
							offsY[floor]+=16;
							pySpeed = 0;
						}
						return true;
					}
					if(direction == 0 && map[floor][i][j+1].isTraversable()){
						pySpeed -= amount;
						if(pySpeed <= -16){
							offsY[floor]-=16;
							pySpeed = 0;
						}
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void updateStairs(int floor){
		Node[][] thisFloor = map[floor];
		for(Node[] row : thisFloor)
			for(Node n : row){
				if(n.isStairsUp()) stairsUp = n;
				if(n.isStairsDown()) stairsDown = n;
			}
	}
	
	public boolean checkForStairs(int floor, float x, float y){
		if(stairsUp==null || currentFloor != floor) updateStairs(floor);
		currentFloor = floor;
		if(floor!=0 && stairsDown.getX()+offsX[floor]==x && stairsDown.getY()+offsY[floor]==y){
			System.out.println("You see stairs down and climb down");
			offsX[floor-1] -= 16;
			offsY[floor-1] -= 16;
			com.github.avilysalAndCeltic.VampTra.logic.GamePlay.player.setFloor(floor-1);
			return true;
		}
		if(floor!=9 && stairsUp.getX()+offsX[floor]==x && stairsUp.getY()+offsY[floor]==y){
			System.out.println("You see stairs up and climb up");
			offsX[floor+1] -= 16;
			offsY[floor+1] -= 16;
			com.github.avilysalAndCeltic.VampTra.logic.GamePlay.player.setFloor(floor+1);
			return true;
		}
		return false;
	}
	
	public void render(int floor){
		for(Node[] row : map[floor]){
			for(Node n : row){
				if(	offsX[floor]+n.getX()<com.github.avilysalAndCeltic.VampTra.logic.GamePlay.DW && //render only stuff that's on screen
					offsY[floor]+n.getY()<com.github.avilysalAndCeltic.VampTra.logic.GamePlay.DH &&
					offsX[floor]+n.getX()>0 && 
					offsY[floor]+n.getY()>0)
				{
					if(n.getName()=='w') //if wall, render a red-ish rectangle
						com.github.avilysalAndCeltic.VampTra.logic.GamePlay.text.createQuad(n.getX()+offsX[floor]+pxSpeed, n.getY()+offsY[floor]+pySpeed, 16f, .3f, 0f, .05f, 1f);
					else if (n.getName() == ' '){ //if floor... just sit there, doing nothing, looking pwetty
						
					} 
					else if (n.getName() == 'p'){ // path
						com.github.avilysalAndCeltic.VampTra.logic.GamePlay.text.createQuad(n.getX()+offsX[floor]+pxSpeed, n.getY()+offsY[floor]+pySpeed, 16f, 1f, 1f, 0f, .5f);
					}
					else //render a character that represents anything except two mentioned above
						com.github.avilysalAndCeltic.VampTra.logic.GamePlay.text.drawChar(n.getName(), n.getX()+offsX[floor]+pxSpeed, n.getY()+offsY[floor]+pySpeed);
					
					//path finding visual representation
//					if(n.open) //aqua
//						com.github.avilysalAndCeltic.VampTra.logic.GamePlay.text.createQuad(n.getX()+offsX[floor]+pxSpeed, n.getY()+offsY[floor]+pySpeed, 16f, .3f, .6f, .7f, .5f);
//					if(n.closed) //beige
//						com.github.avilysalAndCeltic.VampTra.logic.GamePlay.text.createQuad(n.getX()+offsX[floor]+pxSpeed, n.getY()+offsY[floor]+pySpeed, 16f, 1f, 1f, .7f, .2f);
					
					if(n.isStairsUp())
						com.github.avilysalAndCeltic.VampTra.logic.GamePlay.text.createQuad(n.getX()+offsX[floor]+pxSpeed, n.getY()+offsY[floor]+pySpeed, 16f, 0f, 1f, .1f, .5f);
					if(n.isStairsDown())
						com.github.avilysalAndCeltic.VampTra.logic.GamePlay.text.createQuad(n.getX()+offsX[floor]+pxSpeed, n.getY()+offsY[floor]+pySpeed, 16f, 0f, 1f, .1f, .5f);
					
					// below are renders for predesigned rooms, later, distinctive graphics will be used
					if(n.getType()=="crypt") //give a nice carpet to the crypt, including walls (manly to see the area, testing purposes)
						com.github.avilysalAndCeltic.VampTra.logic.GamePlay.text.createQuad(n.getX()+offsX[floor]+pxSpeed, n.getY()+offsY[floor]+pySpeed, 16f, .3f, 0f, .2f, .5f);
					if(n.getType()=="stairs") //yet to be implemented, color code = beige
						com.github.avilysalAndCeltic.VampTra.logic.GamePlay.text.createQuad(n.getX()+offsX[floor]+pxSpeed, n.getY()+offsY[floor]+pySpeed, 16f, 1f, 1f, .7f, .5f);
					if(n.getType()=="obelisk") //yet to be implemented, color code cyan/aqua (only means of new skill acquisition)
						com.github.avilysalAndCeltic.VampTra.logic.GamePlay.text.createQuad(n.getX()+offsX[floor]+pxSpeed, n.getY()+offsY[floor]+pySpeed, 16f, .3f, .6f, .7f, .5f);
				}
			}
		}
	}
}