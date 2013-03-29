package com.github.avilysalAndCeltic.VampTra.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Comparator;
import java.util.PriorityQueue;

import com.github.avilysalAndCeltic.VampTra.map.Node;

public class PathFinder implements Runnable {

	private PriorityQueue<Node> open;
	private HashSet<Node> closed;
	private Node[][] map;
	
	private int yInc = 16;
	private int xInc = 16;
	
	@Override
	public void run() {
		System.out.println("Path Finding is runnin");
	}
	
	public void setMap(Node[][] cMap){
		this.map = cMap;
	}
	
	public boolean canBeFound(Node start, Node goal){
		if(findPath(start, goal) != null) return true;
		else return false;
	}
	
	public Node[] findPath(Node start, Node goal){
		if(map == null){
			System.out.println("Could not find map, use setMap(Node[][]) before calling for path.");
		} else { //if map exists, reset parents;
			for(Node[] row : map){
				for(Node n : row){
					n.open=false;
					n.closed = false;
					n.setParent(null);
					n.setF(0);
					n.setG(0);
				}
			}
		}

		closed = new HashSet<Node>();
		open = new PriorityQueue<Node>(20,new Comparator<Node>() {
			public int compare(Node n1, Node n2){
				if(n1.getF() < n2.getF()){
		            return -1;
		        }else if(n1.getF() > n2.getF()){
		            return 1;
		        }else{
		            return 0;
		        }
			}
		});
		open.add(start);
		
		start.setG(0);
		start.setF(start.getG() + heuristics(start, goal));
		start.setParent(start);
		
		while(!open.isEmpty()){
			Node current = open.poll();
			
			current.open=false;
			
			if(current == goal){
				return reconstructPath(current);
			}
			
			closed.add(current);
			
			current.closed = true;
			
			Node [] neighbors = getNeighbors(current);
			for(Node n : neighbors){
				if(n.isTraversable() == false){
					closed.add(n);
					continue;
				}
				if(closed.contains(n))
					continue;
				
				double tenG = current.getG() + getDistance(current, n);
				
				if(!open.contains(n) || tenG < n.getG()){
					n.setParent(current);
					n.setG(tenG);
					n.setF(tenG + heuristics(n, goal));
					if(!open.contains(n)){
						open.add(n);
						n.open = true;
					}
				}
			}
		}
		System.out.println("No paht found.");
		return null;
	}
	
	private Node[] reconstructPath(Node current) {
		ArrayList<Node> path = new ArrayList<Node>();
		Node temp = current;
        while(temp.getParent() != temp){
            path.add(temp);
            temp = temp.getParent();
        }
        Node[] foundPath = new Node[path.size()]; // reverse the queue
        for(int i = 0; i<path.size(); i++)
        	foundPath[(path.size()-1)-i] = path.get(i);
        return foundPath;
    }
	
	private Node[] getNeighbors(Node node){
		float nX=node.getX();
		float nY=node.getY();
		
		Node[] nn ={getNode(nX-xInc, nY+yInc), getNode(nX, nY+yInc), getNode(nX+xInc, nY+yInc),
					getNode(nX-xInc, nY),							 getNode(nX+xInc, nY),
					getNode(nX-xInc, nY-yInc), getNode(nX, nY-yInc), getNode(nX+xInc, nY-yInc)};
		return nn;
	}
	
	private Node getNode (float x, float y){
		for(Node[] row : map){
			for(Node n : row){
				if(n.getX() == x && n.getY() == y) return n;
			}
		}
		System.out.println("Could not fing node at x:"+x+" y:"+y);
		return null;
	}
	
	private double getDistance(Node from, Node to){
		return Math.sqrt(Math.pow((from.getX() - to.getX()), 2) + Math.pow((from.getY() - to.getY()), 2));
	}
	
	private double heuristics(Node start, Node end) {
        return Math.abs(end.getX() - start.getX()) + Math.abs(end.getY() - start.getY());
	}
}
