package com.github.avilysalAndCeltic.VampTra.map;

public class Room{
	private Node[][] nodes;
	
	public Room(int rows, int columns){
		nodes = new Node[rows][columns];
	}
	
	public void addNode(Node n, int row, int column){
		nodes[row][column] = n;
	}
	
	public Node[][] getNodes(){
		return nodes;
	}
	
	public void expand(Room r, int direction){ //reconstruct this room to contain provided room and delete walls between 'em
		
		//needs more thinking to get it working properly... Ain't in working order yet
		
		Node[][] temp = new Node[0][0];
		if(direction == 0){ // expand north
			temp = new Node[r.getNodes().length+nodes.length][nodes[0].length];
			
		}
		if(direction == 1){ // expand east
			temp = new Node[nodes.length][r.getNodes()[0].length+nodes[0].length];
			
		}
		if(direction == 2){ // expand south
			temp = new Node[r.getNodes().length+nodes.length][nodes[0].length];
			
		}
		if(direction == 3){ // expand west
			temp = new Node[nodes.length][r.getNodes()[0].length+nodes[0].length];
			
		}
		
		nodes = temp;
	}
	
	public void makeEmpty(){
		nodes = new Node[0][0];
	}
}