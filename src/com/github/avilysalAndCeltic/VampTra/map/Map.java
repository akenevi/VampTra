package com.github.avilysalAndCeltic.VampTra.map;

/*	This should have check, to see if it fits on the whole map,
 * 	passing all the needed info to renderer, passing values to
 * 	pathfinding algorithm(if needed)..?
 */

public class Map{
	private Node[][][] map; // map[floor][row][column]; floor starting on 10 (map[9]);
	private float[] offsX, offsY;
	private float pxSpeed = 0; 
	private float pySpeed = 0; 
	
	public Map(){
		map = new Node[10][0][0];
		map[0] = floorGenerator.generateFloor(); //generate first floor
		offsX = new float[10];
		offsY = new float[10];
		for(int i=0; i < 10; i++){ //set offset to the lower left corner of the dungeon
			offsX[i] = com.github.avilysalAndCeltic.VampTra.logic.GamePlay.DW/2;
			offsY[i] = com.github.avilysalAndCeltic.VampTra.logic.GamePlay.DH/2;
		}
		offsX[0] -= map[0].length/2*16; //set offset to the center of the floor 0
		offsY[0] -= map[0][0].length/2*16;
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
					else if (n.getName() == 'b'){ // blank
						com.github.avilysalAndCeltic.VampTra.logic.GamePlay.text.createQuad(n.getX()+offsX[floor]+pxSpeed, n.getY()+offsY[floor]+pySpeed, 16f, 1f, 1f, 1f, .6f);
					}
					else //render a character that represents anything except two mentioned above
						com.github.avilysalAndCeltic.VampTra.logic.GamePlay.text.drawChar(n.getName(), n.getX()+offsX[floor]+pxSpeed, n.getY()+offsY[floor]+pySpeed);
					
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