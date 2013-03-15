package com.github.avilysalAndCeltic.VampTra.map;

import java.util.ArrayList;

public class RoomTemplates {
	private ArrayList<char[]> rooms;
	// 'w' == wall; 'd' == door; 'f' == floor; 'c'== center; 's' == possible spawn 
	private char[] entrance = {'7','7',
			'w', 'w', 'w', 'd', 'w', 'w', 'w',
			'w', 'f', 'f', 'f', 'f', 'f', 'w',
			'w', 'f', 'f', 'f', 'f', 'f', 'w',
			'd', 'f', 'f', 'c', 'f', 'f', 'd',
			'w', 'f', 'f', 'f', 'f', 'f', 'w',
			'w', 'f', 'f', 'f', 'f', 'f', 'w',
			'w', 'w', 'w', 'd', 'w', 'w', 'w'};
	
	public RoomTemplates(){
		rooms = new ArrayList<char[]>();
		rooms.add(entrance);
	}
	
	public char[] genRoom(String name){
		if(name=="entrance") 
			return entrance;
		else
			return null;
	}
	
	public char[] genRoom(int w, int h, int random){
		ArrayList<char[]> results = new ArrayList<char[]>();
		for(char[] ca : rooms){
			if(Character.toString(ca[0])==Integer.toString(w)){
				if(Character.toString(ca[1])==Integer.toString(h)){
					results.add(ca);
				}
			}
		}
		if(!results.isEmpty()){
			while(random > results.size()-1){
				random -= results.size()-1;
			}
			return results.get(random);
		}
		return null;
	}
}
