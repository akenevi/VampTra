package com.github.avilysalAndCeltic.VampTra.map;

public class Node {
	float x, y;
	char name;
	char direction;
	boolean passable = true;
	
	public Node(float x, float y, char name, char dir){
		this.direction = dir;
		this.x = x;
		this.y = y;
		this.name = name;
		if (name == 'w') passable = false;
	}
	
	public void setDirection(char dir){
		direction = dir;
	}
}
