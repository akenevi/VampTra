package com.github.avilysalAndCeltic.VampTra.utils;

import java.util.ArrayList;

import com.github.avilysalAndCeltic.VampTra.map.Node;

public class PFind {
	private static ArrayList<Node> open, closed;
	private static Node[][] map;
	private static float xInc, yInc;
	
	public static boolean canBeFound(Node[][] cMap, Node from, Node node){
		if(findPath(cMap, from, node) != null) return true;
		else return false;
	}
	
	public static Node[] findPath(Node[][] cMap, Node start, Node destination){
		map = cMap;
		open = new ArrayList<Node>();
		closed = new ArrayList<Node>();
		
		//resetting forced flag and parents on all the nodes;
		for(Node[] row : map){
			for(Node n : row){
				n.setForced(false);
				n.setParent(null);
				n.setF(heuristics(n, destination));
			}
		}
		
		yInc = 16; //com.github.avilysalAndCeltic.VampTra.logic.GamePlay.DH / map.length;
		xInc = 16; //com.github.avilysalAndCeltic.VampTra.logic.GamePlay.DW / map[0].length;

		start.setG(0);
		start.setParent(start);
		open.add(start);
		while(!open.isEmpty()){
			Node lowestF = open.get(0);
			for(Node n : open){
				if(n.getF() < lowestF.getF()) lowestF = n;
			}
			if(lowestF == destination){
				System.out.println("Found path!!!"); 
            	return reconstructPath(lowestF);
			}
			closed.add(lowestF); 
			open.remove(lowestF);
			
			ArrayList<Node> succ = identifySuccessors(lowestF, start, destination);
			for(Node n : succ){
				if(!closed.contains(n)){
					if(!open.contains(n)){
						n.setParent(lowestF);
						n.setG(n.getG() + Math.sqrt(Math.pow((lowestF.getX() - n.getX()), 2) + Math.pow((lowestF.getY() - n.getY()), 2)));
						n.setF(n.getG() + heuristics(n, destination));
						open.add(n);
					}
				}
			}
		}
		return null;
	}

	
	private static ArrayList<Node> identifySuccessors(Node c, Node s, Node g){
		ArrayList<Node> successors = new ArrayList<Node>();
		ArrayList<Node> neighbors = prune(c, 0, 0);
		
		for(Node n : neighbors){
			int dX = (int)n.getX() - (int)c.getX();
			int dY = (int)n.getY() - (int)c.getY();
			
			if(jump(c, dX, dY, s, g) != null)
				n = jump(c, dX, dY, s, g);
			successors.add(n);
		}
		return successors;
	}
	
	private static Node jump(Node initial, int dX, int dY, Node s, Node g){
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

	private static boolean hasForced(Node node, int dX, int dY){
		ArrayList<Node> prunedNeighbors = prune(node, dX, dY);
		boolean forced = false;
		for(Node n : prunedNeighbors){
			if (n.isForced()) forced = true;
		}
		return forced;
	}
	
	private static ArrayList<Node> prune(Node node, int dX, int dY){
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
				if(nn[4].isTraversable()) prunedNeighbors.add(nn[4]);
				if(!nn[1].isTraversable()){
					if(nn[2].isTraversable()){
						nn[2].setForced(true);
						prunedNeighbors.add(nn[2]);}
				}
				if(!nn[6].isTraversable()){
					if(nn[7].isTraversable()){
						nn[7].setForced(true);
						prunedNeighbors.add(nn[7]);}
				}
			}
			if (dX < 0){
				if(nn[3].isTraversable()) prunedNeighbors.add(nn[3]);
				if(!nn[1].isTraversable()){
					if(nn[0].isTraversable()){
						nn[0].setForced(true);
						prunedNeighbors.add(nn[0]);}
				}
				if(!nn[6].isTraversable()){
					if(nn[5].isTraversable()){
						nn[5].setForced(true);
						prunedNeighbors.add(nn[5]);}
				}
			}
		}
		if(dX == 0 && dY != 0){ //vetical checks
			if(dY > 0){
				if(nn[1].isTraversable()) prunedNeighbors.add(nn[1]);
				if(!nn[3].isTraversable()){
					if(nn[0].isTraversable()){
						nn[0].setForced(true);
						prunedNeighbors.add(nn[0]);}
				}
				if(!nn[4].isTraversable()){
					if(nn[2].isTraversable()){
						nn[2].setForced(true);
						prunedNeighbors.add(nn[2]);}
				}
			}
			if (dX < 0){
				if(nn[6].isTraversable()) prunedNeighbors.add(nn[6]);
				if(!nn[3].isTraversable()){
					if(nn[5].isTraversable()){
						nn[5].setForced(true);
						prunedNeighbors.add(nn[5]);}
				}
				if(!nn[4].isTraversable()){
					if(nn[7].isTraversable()){
						nn[7].setForced(true);
						prunedNeighbors.add(nn[7]);}
				}
			}
		}
		if(dX != 0 && dY != 0){ //diagonal checks
			if(dX > 0 && dY > 0){
				if(nn[1].isTraversable()) prunedNeighbors.add(nn[1]);
				if(nn[2].isTraversable()) prunedNeighbors.add(nn[2]);
				if(nn[4].isTraversable()) prunedNeighbors.add(nn[4]);
				if(!nn[3].isTraversable()){
					if(nn[0].isTraversable()){
						nn[0].setForced(true);
						prunedNeighbors.add(nn[0]);}
				}
				if(!nn[6].isTraversable()){
					if(nn[7].isTraversable()){
						nn[7].setForced(true);
						prunedNeighbors.add(nn[7]);}
				}
			}
			if(dX > 0 && dY < 0){
				if(nn[4].isTraversable()) prunedNeighbors.add(nn[4]);
				if(nn[7].isTraversable()) prunedNeighbors.add(nn[7]);
				if(nn[6].isTraversable()) prunedNeighbors.add(nn[6]);
				if(!nn[1].isTraversable()){
					if(nn[2].isTraversable()){
						nn[2].setForced(true);
						prunedNeighbors.add(nn[2]);}
				}
				if(!nn[3].isTraversable()){
					if(nn[5].isTraversable()){
						nn[5].setForced(true);
						prunedNeighbors.add(nn[5]);}
				}
			}
			if(dX < 0 && dY < 0){
				if(nn[3].isTraversable()) prunedNeighbors.add(nn[3]);
				if(nn[5].isTraversable()) prunedNeighbors.add(nn[5]);
				if(nn[6].isTraversable()) prunedNeighbors.add(nn[6]);
				if(!nn[1].isTraversable()){
					if(nn[0].isTraversable()){
						nn[0].setForced(true);
						prunedNeighbors.add(nn[0]);}
				}
				if(!nn[4].isTraversable()){
					if(nn[7].isTraversable()){
						nn[7].setForced(true);
						prunedNeighbors.add(nn[7]);}
				}
			}
			if(dX < 0 && dY > 0){
				if(nn[3].isTraversable()) prunedNeighbors.add(nn[3]);
				if(nn[0].isTraversable()) prunedNeighbors.add(nn[0]);
				if(nn[1].isTraversable()) prunedNeighbors.add(nn[1]);
				if(!nn[6].isTraversable()){
					if(nn[5].isTraversable()){
						nn[5].setForced(true);
						prunedNeighbors.add(nn[5]);}
				}
				if(!nn[4].isTraversable()){
					if(nn[2].isTraversable()){
						nn[2].setForced(true);
						prunedNeighbors.add(nn[2]);}
				}
			}
		}
//		for(Node n : nn){
//			if(!prunedNeighbors.contains(n) && n != getNode(nX+dX, nY+dY)) closed.add(n);
//		}
		return prunedNeighbors;
	}
	
	/* OLD CODE follows algorithm to the letter, will complete later, just for fun...
	// Prune rules: (if true, prune neighbor) (len = length of path; parent(x) = parent of node x; 
	// 		neighbor = neighbor of node x, that is checked to be pruned)
	// StraigtMove len(parent(x),...,neighbor without x) ² len(parent(x), x, neighbor)
	// DiagonalMov len(parent(x),...,neighbor without x) < len(parent(x), x, neighbor)
	// forced neighbor check:
	// len(parent(x), x, n) < len(parent(x),...,n without x)
	
	private ArrayList<Node> prune(Node node){
		ArrayList<Node> prunedNeighbors = new ArrayList<Node>();
		float nX=node.getX();
		float nY=node.getY();
		Node parent = node.getParent();
		Node[] naturalNeighbors = { getNode(nX-xInc, nY+yInc), getNode(nX, nY+yInc), getNode(nX+xInc, nY+yInc),
									getNode(nX-xInc, nY),							 getNode(nX+xInc, nY),
									getNode(nX-xInc, nY-yInc), getNode(nX, nY-yInc), getNode(nX+xInc, nY-yInc)};
		
		for(Node n : naturalNeighbors){
			if(n.isTraversable()){
					if(!(calcLength(parent, node , n, false) <= calcLength(parent, node , n, true))){
						prunedNeighbors.add(n);
					}
			}  // if not traversable
		}
		return prunedNeighbors;
	}
	
	private double calcLength(Node pare, Node node, Node n, boolean checkAgainst){
		double length = 0;
		float px = pare.getX(); float py = pare.getY();
		float nx = n.getX(); float ny = n.getY();
		float dx = node.getX() - px; float dy = node.getY() - py;
		
		if(pare == n) return 0;
		
		if(nx - px == 0){
			if (ny - py == dy){
				
			}
			else if (ny - py == dy*2){
				
			}
		}
		else if(nx - px == dx){
			if(ny - py == 0){
				
			}
			else if (ny - py == dy){
				
			}
			else if (ny - py == dy*2){
				
			}
		}
		else if(nx - px == dx*2){
			if(ny - py == 0){
				
			}
			else if (ny - py == dy){
				
			}
			else if (ny - py == dy*2){
				
			}
		}
		
		return length;
	}
	*/
	private static Node[] reconstructPath(Node current) {
		ArrayList<Node> path = new ArrayList<Node>();
        Node curr = current;
        while(curr.getParent() != curr){
            path.add(curr);
            curr = curr.getParent();
        }
        Node[] foundPath = new Node[path.size()];
        for(int i = 0; i<path.size(); i++)
        	foundPath[(path.size()-1)-i] = path.get(i);
        return foundPath;
    }

	
	private static double heuristics(Node start, Node end) {
		// manhattanHeuristic
        return Math.abs(end.getX() - start.getX()) + Math.abs(end.getY() - start.getY());
   /*     //diagonal manhattan
	    double h_diagonal = Math.min(Math.abs(start.getX()-end.getX()), Math.abs(start.getY()-end.getY()));
	    double h_straight = Math.abs(start.getX()-end.getX()) + Math.abs(start.getY()-end.getY());
		return (Math.sqrt(2)*h_diagonal + 1*(h_straight - 2*h_diagonal));
	 */    
	}
	
	private static Node getNode(float x, float y){
		for(Node[] row : map)
			for(Node n : row)
				if(n.getX() == x && n.getY() == y)
					return n;
		return null;
	}
}