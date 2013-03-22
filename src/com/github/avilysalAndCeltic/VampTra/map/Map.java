package com.github.avilysalAndCeltic.VampTra.map;

/*	This should have check, to see if it fits on the whole map,
 * 	passing all the needed info to renderer, passing values to
 * 	pathfinding algorithm(if needed)..?
 */

public class Map{
	private Node[][] map;
	private float offsX, offsY;
	
	public Map(){
		offsX = com.github.avilysalAndCeltic.VampTra.logic.GamePlay.DW/2 - 3*16;
		offsY = com.github.avilysalAndCeltic.VampTra.logic.GamePlay.DH/2 - 3*16;
	}
	
	public boolean checkCollision(){
		return false;
	}
	
	public void makeEntrance(){
		map = new Node[7][7];
		char name;
		for(int i=0; i<map.length; i++)
			for(int j=0; j<map[i].length; j++){
				if(i==0 || i==6) name = 'w';
				else if (j==0 || j==6) name = 'w';
				else name = 'f';
				if ((i==0 || i==6) && j==3) name = 'd';
				if (i==3 && (j==0 || j==6)) name = 'd';
				map[i][j] = new Node(i*16,j*16,name);
			}
	}
	
	public boolean changeOffset(String axis, float amount){
		float px = com.github.avilysalAndCeltic.VampTra.logic.GamePlay.player.getX(); //400  offs initially 352
		float py = com.github.avilysalAndCeltic.VampTra.logic.GamePlay.player.getY(); //300  offs initially 252
		for(Node[] nArr : map){
			for(Node n : nArr){
				if(axis == "x" && n.y+offsY==py){
					if(n.x+offsX==px-amount && n.passable){
						offsX+=amount;
						return true;
					}
				}
				if(axis == "y" && n.x+offsX==px){
					if(n.y+offsY==py-amount && n.passable){
						offsY+=amount;
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void render(){
		for(Node[] nArr : map){
			for(Node n : nArr){
				if(	offsX+n.x<com.github.avilysalAndCeltic.VampTra.logic.GamePlay.DW && 
					offsY+n.y<com.github.avilysalAndCeltic.VampTra.logic.GamePlay.DH &&
					offsX+n.x>0 && 
					offsY+n.y>0)
				{
					com.github.avilysalAndCeltic.VampTra.logic.GamePlay.text.drawChar(n.name, n.x+offsX, n.y+offsY);
					if(!n.passable)
						com.github.avilysalAndCeltic.VampTra.logic.GamePlay.text.createQuad(n.x+offsX, n.y+offsY, 16f, 1f, .1f, .1f, .4f);
				}
			}
		}
		com.github.avilysalAndCeltic.VampTra.logic.GamePlay.text.drawString("offsX: "+offsX, 8, com.github.avilysalAndCeltic.VampTra.logic.GamePlay.DH-8);
		com.github.avilysalAndCeltic.VampTra.logic.GamePlay.text.drawString("offsY: "+offsY, 8, com.github.avilysalAndCeltic.VampTra.logic.GamePlay.DH-24);
		com.github.avilysalAndCeltic.VampTra.logic.GamePlay.text.drawString("P x:" + com.github.avilysalAndCeltic.VampTra.logic.GamePlay.player.getX() +
				" y:" + com.github.avilysalAndCeltic.VampTra.logic.GamePlay.player.getY(), 8, com.github.avilysalAndCeltic.VampTra.logic.GamePlay.DH-40);
		for(Node[] nArr : map)
			for(Node n : nArr){
				if(n.x+offsX==com.github.avilysalAndCeltic.VampTra.logic.GamePlay.player.getX() && n.y+offsY==com.github.avilysalAndCeltic.VampTra.logic.GamePlay.player.getY())
					com.github.avilysalAndCeltic.VampTra.logic.GamePlay.text.drawString("standing on node x:"+(n.x+offsX)+" y:"+(n.y+offsY)+" n:"+n.name, 8, com.github.avilysalAndCeltic.VampTra.logic.GamePlay.DH-56);
			}
	}
}

class Node{
	float x, y;
	float cx1, cx2, cy1, cy2; //corner markings; (cx1,cy1) upper left; (cx2, cy2) lower right;
	char name;
	boolean passable = true;
	
	public Node(float x, float y, char name){
		this.x = x;
		this.y = y;
		this.name = name;
		if (name == 'w') passable = false;
		cx1 = x-8;
		cx2 = x+8;
		cy1 = y+8;
		cy2 = y-8;
	}
}
