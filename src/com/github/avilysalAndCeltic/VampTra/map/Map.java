package com.github.avilysalAndCeltic.VampTra.map;

import java.util.ArrayList;

/*	This should have check, to see if it fits on the whole map,
 * 	passing all the needed info to renderer, passing values to
 * 	pathfinding algorithm(if needed)..?
 */

public class Map{
	private RoomTemplates templates;
	private ArrayList<char[]> generated;
	
	public Map(){
		templates = new RoomTemplates();
		generated = new ArrayList<char[]>();
	}
	
	public void makeEntrance(){
		generated.add(templates.genRoom("entrance"));
	}
	
	public void render(){
		float hdw = com.github.avilysalAndCeltic.VampTra.logic.GamePlay.DW/2;
		float hdh = com.github.avilysalAndCeltic.VampTra.logic.GamePlay.DH/2;
		float x, y, xs, ys;
		for(char[] ca : generated){
			int w = ca[0]-'0';
			int h = ca[1]-'0';
			xs = hdw-w/2*16;
			ys = hdh+h/2*16;
			for(int i=2; i < ca.length; i++){
				x = xs+(i-2)%w*16;
				y = ys-(i-2)/h*16;
				com.github.avilysalAndCeltic.VampTra.logic.GamePlay.text.drawChar(ca[i], x, y);
			}
		}
	}
}
