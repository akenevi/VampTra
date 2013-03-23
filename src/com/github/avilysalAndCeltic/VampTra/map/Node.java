package com.github.avilysalAndCeltic.VampTra.map;

public class Node {
	private float x, y;
	private char name;
	private boolean passable = true;
	private boolean onBorder = false;
	
	public Node(float x, float y, char name){
		this.x = x;
		this.y = y;
		this.name = name;
	}
	
	public void setName(char c){
		name = c;
	}
	
	public void setPassable(boolean pass){
		passable = pass;
	}
	
	public void setOnBorder(boolean bord){
		onBorder = bord;
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public char getName(){
		return name;
	}
	
	public boolean isPassable(){
		return passable;
	}
	
	public boolean isOnBorder(){
		return onBorder;
	}
}
