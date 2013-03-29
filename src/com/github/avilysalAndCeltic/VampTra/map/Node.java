package com.github.avilysalAndCeltic.VampTra.map;

public class Node {
	private float x, y;
	private char name;
	private boolean traversable = true;
	private boolean onBorder = false;
	private String type = "";
	
	//path finding stuff
	private Node parent;
	private boolean forced = false;
	private double g, f;
	
	//path finding visual representstion/debugging stuff
	public boolean open = false;
	public boolean closed = false;
	
	public Node(float x, float y, char name){
		this.x = x;
		this.y = y;
		this.name = name;
		this.parent = null;
		this.g = 0;
		this.f = 1;
	}
	
	public void setName(char c){
		name = c;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public void setTraversable(boolean pass){
		traversable = pass;
	}
	
	public void setOnBorder(boolean bord){
		onBorder = bord;
	}
	
	public void setParent(Node n){
		parent = n;
	}
	
	
	public void setForced(boolean set){
		forced = set;
	}
	
	public void setG(double g){
		this.g = g;
	}
	
	public void setF(double d){
		this.f = d;
	}
	
	public boolean isForced(){
		return forced;
	}
	
	public double getG (){
		return g;
	}
	
	public double getF(){
		return f;
	}
	
	public Node getParent(){
		return parent;
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
	
	public String getType(){
		return type;
	}
	
	public boolean isTraversable(){
		return traversable;
	}
	
	public boolean isOnBorder(){
		return onBorder;
	}
}
