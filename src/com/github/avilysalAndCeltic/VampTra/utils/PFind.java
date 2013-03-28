package com.github.avilysalAndCeltic.VampTra.utils;

import java.util.ArrayList;

import com.github.avilysalAndCeltic.VampTra.map.Node;

public class PFind {
	private ArrayList<Node> open, closed;
	private static Node[][] map;
	private float xInc, yInc;
	private int jumps;
	
	/*
	 *  A*
	 *  1. Find node closest to your position and declare it start node and put it on the open list. 
		2. While there are nodes in the open list:
   		3. Pick the node from the open list having the smallest F score. Put it on 
      			the closed list (you don't want to consider it again).
   		4. For each neighbor (adjacent cell) which isn't in the closed list:
      	5. Set its parent to current node.
      	6. Calculate G score (distance from starting node to this neighbor) and 
         		add it to the open list
      	7. Calculate F score by adding heuristics to the G value.
	 */
	
	public void findPath(Node[][] map, Node start, Node destination){
		this.map = map;
		open = new ArrayList<Node>();
		closed = new ArrayList<Node>();
		
		//resetting forced flag and parents on all the nodes;
		for(Node[] row : map){
			for(Node n : row){
				n.setForced(false);
				n.setParent(null);
				n.setF(manhattanDistance(n, destination));
			}
		}
		
		yInc = com.github.avilysalAndCeltic.VampTra.logic.GamePlay.DH / map.length;
		xInc = com.github.avilysalAndCeltic.VampTra.logic.GamePlay.DW / map[0].length;

		start.setG(0);
		start.setParent(start);
		open.add(start);
		while (!open.isEmpty()){
			Node lowestF = open.get(0);
			for(Node n : open){
				if(n.getF() < lowestF.getF()) lowestF = n;
			}
			if(lowestF == destination){
				System.out.println("Found path!!!");
				break;
			}
			closed.add(lowestF); 
			open.remove(lowestF);
			
			ArrayList<Node> succ = identifySuccessors(lowestF, start, destination);
			for(Node n : succ){
				if(!closed.contains(n)){
					n.setParent(lowestF);
					n.setG(Math.abs(n.getX()-lowestF.getX())/xInc + Math.abs(n.getY() - lowestF.getY()/yInc));
					open.add(n);
					n.setF(n.getG() + manhattanDistance(n, destination));
				}
			}
		}
	}

	
	private ArrayList<Node> identifySuccessors(Node c, Node s, Node g){
		ArrayList<Node> successors = new ArrayList<Node>();
		ArrayList<Node> neighbors = prune(c, 0, 0);
		
		for(Node n : neighbors){
			int dX = (int)n.getX() - (int)c.getX();
			int dY = (int)n.getY() - (int)c.getY();
			
			if(jump(c, dX, dY, s, g) != null)
				jumps = 0;
				n = jump(c, dX, dY, s, g);
			successors.add(n);
		}
		return successors;
	}
	
	private Node jump(Node initial, int dX, int dY, Node s, Node g){
		Node next = getNode(initial.getX()+dX, initial.getY()+dY);
		
		if(next.isTraversable() == false)
			return null;
		if(next == g)
			return next;
		if(hasForced(next, dX, dY))
			return next;
		if(dX != 0 && dY != 0){
			if(jump(next, dX, 0, s, g) != null || jump(next, 0, dY, s, g) != null) return next;
		}
		
		return jump(next, dX, dY, s, g);
	}

	private boolean hasForced(Node node, int dX, int dY){
		ArrayList<Node> prunedNeighbors = prune(node, dX, dY);
		boolean forced = false;
		for(Node n : prunedNeighbors){
			if (n.isForced()) forced = true;
		}
		return forced;
	}
	
	private ArrayList<Node> prune(Node node, int dX, int dY){
		ArrayList<Node> prunedNeighbors = new ArrayList<Node>();
		float nX=node.getX();
		float nY=node.getY();
		
		//nn == natural neighbors
		Node[] nn ={getNode(nX-xInc, nY+yInc), getNode(nX, nY+yInc), getNode(nX+xInc, nY+yInc),
					getNode(nX-xInc, nY),							 getNode(nX+xInc, nY),
					getNode(nX-xInc, nY-yInc), getNode(nX, nY-yInc), getNode(nX+xInc, nY-yInc)};
		
		if(dX == 0 && dY == 0){ //1st node expansion
			for(Node n : nn){
				if(n.isTraversable())
					prunedNeighbors.add(n);
			}
		}
		
		if(dY == 0 && dX != 0){ //horizontal checks
			if(dX > 0){
				if(nn[5].isTraversable()) prunedNeighbors.add(nn[5]);
				if(!nn[2].isTraversable()){
					if(nn[3].isTraversable()){
						nn[3].setForced(true);
						prunedNeighbors.add(nn[3]);}
				}
				if(!nn[7].isTraversable()){
					if(nn[8].isTraversable()){
						nn[8].setForced(true);
						prunedNeighbors.add(nn[8]);}
				}
			}
			if (dX < 0){
				if(nn[4].isTraversable()) prunedNeighbors.add(nn[4]);
				if(!nn[2].isTraversable()){
					if(nn[1].isTraversable()){
						nn[1].setForced(true);
						prunedNeighbors.add(nn[1]);}
				}
				if(!nn[7].isTraversable()){
					if(nn[6].isTraversable()){
						nn[6].setForced(true);
						prunedNeighbors.add(nn[6]);}
				}
			}
		}
		if(dX == 0 && dY != 0){ //vetical checks
			if(dY > 0){
				if(nn[2].isTraversable()) prunedNeighbors.add(nn[2]);
				if(!nn[4].isTraversable()){
					if(nn[1].isTraversable()){
						nn[1].setForced(true);
						prunedNeighbors.add(nn[1]);}
				}
				if(!nn[5].isTraversable()){
					if(nn[3].isTraversable()){
						nn[3].setForced(true);
						prunedNeighbors.add(nn[3]);}
				}
			}
			if (dX < 0){
				if(nn[7].isTraversable()) prunedNeighbors.add(nn[7]);
				if(!nn[4].isTraversable()){
					if(nn[6].isTraversable()){
						nn[6].setForced(true);
						prunedNeighbors.add(nn[6]);}
				}
				if(!nn[5].isTraversable()){
					if(nn[8].isTraversable()){
						nn[8].setForced(true);
						prunedNeighbors.add(nn[8]);}
				}
			}
		}
		if(dX != 0 && dY != 0){ //diagonal checks
			if(dX > 0 && dY > 0){
				if(nn[2].isTraversable()) prunedNeighbors.add(nn[2]);
				if(nn[3].isTraversable()) prunedNeighbors.add(nn[3]);
				if(nn[5].isTraversable()) prunedNeighbors.add(nn[5]);
				if(!nn[4].isTraversable()){
					if(nn[1].isTraversable()){
						nn[1].setForced(true);
						prunedNeighbors.add(nn[1]);}
				}
				if(!nn[7].isTraversable()){
					if(nn[8].isTraversable()){
						nn[8].setForced(true);
						prunedNeighbors.add(nn[8]);}
				}
			}
			if(dX > 0 && dY < 0){
				if(nn[5].isTraversable()) prunedNeighbors.add(nn[5]);
				if(nn[8].isTraversable()) prunedNeighbors.add(nn[8]);
				if(nn[7].isTraversable()) prunedNeighbors.add(nn[7]);
				if(!nn[2].isTraversable()){
					if(nn[3].isTraversable()){
						nn[3].setForced(true);
						prunedNeighbors.add(nn[3]);}
				}
				if(!nn[4].isTraversable()){
					if(nn[6].isTraversable()){
						nn[6].setForced(true);
						prunedNeighbors.add(nn[6]);}
				}
			}
			if(dX < 0 && dY < 0){
				if(nn[4].isTraversable()) prunedNeighbors.add(nn[4]);
				if(nn[6].isTraversable()) prunedNeighbors.add(nn[6]);
				if(nn[7].isTraversable()) prunedNeighbors.add(nn[7]);
				if(!nn[2].isTraversable()){
					if(nn[1].isTraversable()){
						nn[1].setForced(true);
						prunedNeighbors.add(nn[1]);}
				}
				if(!nn[5].isTraversable()){
					if(nn[8].isTraversable()){
						nn[8].setForced(true);
						prunedNeighbors.add(nn[8]);}
				}
			}
			if(dX < 0 && dY > 0){
				if(nn[4].isTraversable()) prunedNeighbors.add(nn[4]);
				if(nn[1].isTraversable()) prunedNeighbors.add(nn[1]);
				if(nn[2].isTraversable()) prunedNeighbors.add(nn[2]);
				if(!nn[7].isTraversable()){
					if(nn[6].isTraversable()){
						nn[6].setForced(true);
						prunedNeighbors.add(nn[6]);}
				}
				if(!nn[5].isTraversable()){
					if(nn[3].isTraversable()){
						nn[3].setForced(true);
						prunedNeighbors.add(nn[3]);}
				}
			}
		}
		return prunedNeighbors;
	}
	
	
	/*  OLD CODE... Got too complicated, but is to the letter
	 * 
	// Prune rules: (if true, prune neighbor) (len = length of path; parent(x) = parent of node x; 
	// 		neighbor = neighbor of node x, that is checked to be pruned)
	// StraigtMove len(parent(x),...,neighbor without x) ² len(parent(x), x, neighbor)
	// DiagonalMov len(parent(x),...,neighbor without x) < len(parent(x), x, neighbor)
	// forced neighbor check:
	// len(parent(x), x, n) < len(parent(x),...,n without x)
	
	private ArrayList<Node> prune(Node node, int dX, int dY){
		ArrayList<Node> prunedNeighbors = new ArrayList<Node>();
		float nX=node.getX();
		float nY=node.getY();
		
		Node parent = node.getParent();
		Node[] naturalNeighbors = { getNode(nX-xInc, nY+yInc), getNode(nX, nY+yInc), getNode(nX+xInc, nY+yInc),
									getNode(nX-xInc, nY),							 getNode(nX+xInc, nY),
									getNode(nX-xInc, nY-yInc), getNode(nX, nY-yInc), getNode(nX+xInc, nY-yInc)};
		
		for(Node n : naturalNeighbors){
			if(n.isTraversable()){
				Node[] checkAgainst = {parent, node, n};
				if((dY == 0 && (dX == xInc || dX == -xInc)) || dX == 0 && (dY == yInc || dY == -yInc) ){ // Straight movement (1 node)
					Node[] check = {parent, n};
					if(!(checkLength(check) <= checkLength(checkAgainst))){
						prunedNeighbors.add(n);
					}
				} else if((dX == xInc || dX == -xInc) && (dY == yInc || dY == -yInc)){ //Diagonal movement (1 node)
					Node[] check = {parent, n};
					if(!(checkLength(check) < checkLength(checkAgainst))){
						prunedNeighbors.add(n);
					}
				}
			}  // if not traversable
		}
		return prunedNeighbors;
	}
	
	private double checkLength(Node [] nodes){
		double length = 0;
		for(int i=0; i<nodes.length-1; i++){
			if(nodes[i+1].getX()-nodes[i].getX() != 0 && nodes[i+1].getY() - nodes[i].getY() != 0){ //if it's diagonal move
				length += Math.sqrt(2);
			} else length += 1;  //else it's straight move
		}
		return length;
	}
	*/
	private double manhattanDistance(Node start, Node end) {
	    return Math.abs(end.getX() - start.getX()) + Math.abs(end.getY() - start.getY());
	}
	
	private Node getNode(float x, float y){
		for(int i=0; i<map.length; i++){
			if(map[i][0].getY() == y){
				for(Node n : map[i]){
					if(n.getX() == x)
						return n;
				}
			}
		}
		return null;
	}
}