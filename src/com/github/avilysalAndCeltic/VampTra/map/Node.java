package com.github.avilysalAndCeltic.VampTra.map;

public class Node {
	float x, y;
	char name;
	boolean passable = true;
	
	public Node(float x, float y, char name){
		this.x = x;
		this.y = y;
		this.name = name;
	}
	
	public void setPassable(boolean pass){
		passable = pass;
	}
}
