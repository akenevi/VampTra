package com.github.avilysalAndCeltic.VampTra.map;

/*	This should have check, to see if it fits on the whole map,
 * 	passing all the needed info to renderer, passing values to
 * 	pathfinding algorithm(if needed)..?
 */

public class Map{
	private Node[][][] map; // map[floor][row][column]; floor starting on 10 (map[9]);
	private float[] offsX, offsY;
	
	public Map(){
		offsX = new float[10];
		offsY = new float[10];
		for(int i=0; i < 10; i++){
			offsX[i] = com.github.avilysalAndCeltic.VampTra.logic.GamePlay.DW/2 - 3*16;
			offsY[i] = com.github.avilysalAndCeltic.VampTra.logic.GamePlay.DH/2 - 3*16;
		}
		makeEntrance();
		map[0] = floorGenerator.generateFloor(map[0]); //generate first floor
	}
	
	public void makeEntrance(){
		map = new Node[10][7][7];
		char name, dir;
		for(int i=0; i<map[0].length; i++)
			for(int j=0; j<map[0][i].length; j++){
				name = 'f'; dir = ' ';
				if(i==0 || i==6) name = 'w';
				if (j==0 || j==6) name = 'w';
				if (i==0 && j==3) {name = 'd'; dir = 'n';}
				if (i==6 && j==3) {name = 'd'; dir = 's';}
				if (i==3 && j==0) {name = 'd'; dir = 'w';}
				if (i==3 && j==6) {name = 'd'; dir = 'e';}
				map[0][i][j] = new Node(i*16,j*16,name,dir);
			}
	}
	
	public boolean changeOffset(int floor, String axis, float amount){
		float px = com.github.avilysalAndCeltic.VampTra.logic.GamePlay.player.getX(); //400  offs initially 352
		float py = com.github.avilysalAndCeltic.VampTra.logic.GamePlay.player.getY(); //300  offs initially 252
		for(Node[] row : map[floor]){
			for(Node n : row){
				if(axis == "x" && n.y+offsY[floor]==py){
					if(n.x+offsX[floor]==px-amount && n.passable){
						offsX[floor]+=amount;
						return true;
					}
				}
				if(axis == "y" && n.x+offsX[floor]==px){
					if(n.y+offsY[floor]==py-amount && n.passable){
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
				if(	offsX[floor]+n.x<com.github.avilysalAndCeltic.VampTra.logic.GamePlay.DW && 
					offsY[floor]+n.y<com.github.avilysalAndCeltic.VampTra.logic.GamePlay.DH &&
					offsX[floor]+n.x>0 && 
					offsY[floor]+n.y>0)
				{
					com.github.avilysalAndCeltic.VampTra.logic.GamePlay.text.drawChar(n.name, n.x+offsX[floor], n.y+offsY[floor]);
					if(!n.passable)
						com.github.avilysalAndCeltic.VampTra.logic.GamePlay.text.createQuad(n.x+offsX[floor], n.y+offsY[floor], 16f, 1f, .1f, .1f, .4f);
				}
			}
		}
	}
}