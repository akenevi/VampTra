package com.github.avilysalAndCeltic.VampTra.map;

/*	This should have check, to see if it fits on the whole map,
 * 	passing all the needed info to renderer, passing values to
 * 	pathfinding algorithm(if needed)..?
 */

public class Map{
	private Node[][][] map; // map[floor][row][column]; floor starting on 10 (map[9]);
	private float[] offsX, offsY;
	
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
	
	public boolean changeOffset(int floor, String axis, float amount){
		float px = com.github.avilysalAndCeltic.VampTra.logic.GamePlay.player.getX(); //400  offs initially 352
		float py = com.github.avilysalAndCeltic.VampTra.logic.GamePlay.player.getY(); //300  offs initially 252
		for(Node[] row : map[floor]){
			for(Node n : row){
				if(axis == "x" && n.getY()+offsY[floor]==py){
					if(n.getX()+offsX[floor]==px-amount && n.isPassable()){
						offsX[floor]+=amount;
						return true;
					}
				}
				if(axis == "y" && n.getX()+offsX[floor]==px){
					if(n.getY()+offsY[floor]==py-amount && n.isPassable()){
						offsY[floor]+=amount;
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
				if(	offsX[floor]+n.getX()<com.github.avilysalAndCeltic.VampTra.logic.GamePlay.DW && 
					offsY[floor]+n.getY()<com.github.avilysalAndCeltic.VampTra.logic.GamePlay.DH &&
					offsX[floor]+n.getX()>0 && 
					offsY[floor]+n.getY()>0)
				{
					com.github.avilysalAndCeltic.VampTra.logic.GamePlay.text.drawChar(n.getName(), n.getX()+offsX[floor], n.getY()+offsY[floor]);
					if(n.getName()=='w')
						com.github.avilysalAndCeltic.VampTra.logic.GamePlay.text.createQuad(n.getX()+offsX[floor], n.getY()+offsY[floor], 16f, 1f, .1f, .1f, .4f);
				}
			}
		}
	}
}