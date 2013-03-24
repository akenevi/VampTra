package com.github.avilysalAndCeltic.VampTra.map;

public class Room{
	private Node[][] nodes;
	private boolean onBorder = false;
	private boolean[] border = {false,false,false,false};
	private boolean[] expanded = {false,false,false,false};
	private String type = "";
	
	public Room(int rows, int columns){
		nodes = new Node[rows][columns];
	}
	
	public void updateBorders(){
		for(Node[] row : nodes){
			for(Node n : row){
				if(n.isOnBorder()) onBorder = true;
			}
		}
		if(onBorder){
			if(nodes[0][1].isOnBorder()) border[0] = true;  //north
			if(nodes[1][nodes[1].length-1].isOnBorder()) border[1] = true;  //east
			if(nodes[nodes.length-1][1].isOnBorder()) border[2] = true;  // south
			if(nodes[1][0].isOnBorder()) border[3] = true;  //west
		}
	}
	
	public void addNode(Node n, int row, int column){
		nodes[row][column] = n;
	}
	
	public void setExpanded(byte direction, boolean state){
		expanded[direction]=state;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public boolean makeDoor(Room r, byte direction){
		if(direction == 0){ // expand north
			if(nodes[0][nodes[0].length/2].getName()=='w'){
				nodes[0][nodes[0].length/2-1].setName('d');
				nodes[0][nodes[0].length/2].setName('d');
				nodes[0][nodes[0].length/2+1].setName('d');
				r.getNodes()[r.getNodes().length-1][r.getNodes()[r.getNodes().length-1].length/2-1].setName('d');
				r.getNodes()[r.getNodes().length-1][r.getNodes()[r.getNodes().length-1].length/2].setName('d');
				r.getNodes()[r.getNodes().length-1][r.getNodes()[r.getNodes().length-1].length/2+1].setName('d');
				r.setExpanded((byte) 2, true); //set target room expansion to south
				expanded[direction] = true;
				return true;
			}
		}
		else if(direction == 1){ // expand east
			if(nodes[nodes.length/2][nodes[nodes.length/2].length-1].getName()=='w'){
				nodes[nodes.length/2-1][nodes[nodes.length/2].length-1].setName('d');
				nodes[nodes.length/2][nodes[nodes.length/2].length-1].setName('d');
				nodes[nodes.length/2+1][nodes[nodes.length/2].length-1].setName('d');
				r.getNodes()[r.getNodes().length/2-1][0].setName('d');
				r.getNodes()[r.getNodes().length/2][0].setName('d');
				r.getNodes()[r.getNodes().length/2+1][0].setName('d');
				r.setExpanded((byte) 3, true); //set target room expansion to west
				expanded[direction] = true;
				return true;
			}
		}
		else if(direction == 2){ // expand south
			if(nodes[nodes.length-1][nodes[nodes.length-1].length/2].getName()=='w'){
				nodes[nodes.length-1][nodes[nodes.length-1].length/2-1].setName('d');
				nodes[nodes.length-1][nodes[nodes.length-1].length/2].setName('d');
				nodes[nodes.length-1][nodes[nodes.length-1].length/2+1].setName('d');
				r.getNodes()[0][r.getNodes()[0].length/2-1].setName('d');
				r.getNodes()[0][r.getNodes()[0].length/2].setName('d');
				r.getNodes()[0][r.getNodes()[0].length/2+1].setName('d');
				r.setExpanded((byte) 0, true); //set target room expansion to north
				expanded[direction] = true;
				return true;
			}
		}
		else if(direction == 3){ // expand west
			if(nodes[nodes.length/2][0].getName()=='w'){
				nodes[nodes.length/2-1][0].setName('d');
				nodes[nodes.length/2][0].setName('d');
				nodes[nodes.length/2+1][0].setName('d');
				r.getNodes()[r.getNodes().length/2-1][r.getNodes()[r.getNodes().length/2].length-1].setName('d');
				r.getNodes()[r.getNodes().length/2][r.getNodes()[r.getNodes().length/2].length-1].setName('d');
				r.getNodes()[r.getNodes().length/2+1][r.getNodes()[r.getNodes().length/2].length-1].setName('d');
				r.setExpanded((byte) 1, true); //set target room expansion to east
				expanded[direction] = true;
				return true;
			}
		}
		return false;
	}
	
	public boolean expand(Room r, byte direction){ //delete walls between this and given room and mark expansions
		
		/*
		 * If we want to combine multiple rooms into single room... 
		 * Have to think up a way to express complex shapes, T shape for example.
		 * Suddenly complexity of this jumped way to high...
		 * Just thinking out loud.. polygons, vectors, lines, what else ?
		 * Would be nice to have complex shapes, but they complicate soooo many things
		 * 
		 * Screw combining into one room... going to just remove walls
		 */
		
		if(direction == 0){ // expand north
			for(int i=1; i<nodes[0].length-1; i++)
				nodes[0][i].setName(' ');
			for(int i=1; i<r.getNodes()[r.getNodes().length-1].length-1; i++)
				r.getNodes()[r.getNodes().length-1][i].setName(' ');
			r.setExpanded((byte) 2, true); //set target room expansion to south
			r.setType(type);
			expanded[direction] = true;
			return true;
		}
		else if(direction == 1){ // expand east
			for(int i=1; i<nodes.length-1; i++)
				nodes[i][nodes[i].length-1].setName(' ');
			for(int i=1; i<r.getNodes().length-1; i++)
				r.getNodes()[i][0].setName(' ');
			r.setExpanded((byte) 3, true); //set target room expansion to west
			r.setType(type);
			expanded[direction] = true;
			return true;
		}
		else if(direction == 2){ // expand south
			for(int i=1; i<nodes[nodes.length-1].length-1; i++)
				nodes[nodes.length-1][i].setName(' ');
			for(int i=1; i<r.getNodes()[0].length-1; i++)
				r.getNodes()[0][i].setName(' ');
			r.setExpanded((byte) 0, true); //set target room expansion to north
			r.setType(type);
			expanded[direction] = true;
			return true;
		}
		else if(direction == 3){ // expand west
			for(int i=1; i<nodes.length-1; i++)
				nodes[i][0].setName(' ');
			for(int i=1; i<r.getNodes().length-1; i++)
				r.getNodes()[i][r.getNodes()[i].length-1].setName(' ');
			r.setExpanded((byte) 1, true); //set target room expansion to east
			r.setType(type);
			expanded[direction] = true;
			return true;
		}
		else return false;
	}
	
	public String getType(){
		return type;
	}
	
	public Node[][] getNodes(){
		return nodes;
	}

	public boolean isOnBorder(){
		return onBorder;
	}
	
	public boolean[] getBorders(){
		return border;
	}
	
	public boolean[] getExpansions(){
		return expanded;
	}
}