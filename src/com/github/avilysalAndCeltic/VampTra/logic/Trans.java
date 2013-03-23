package com.github.avilysalAndCeltic.VampTra.logic;

public class Trans {
	private static boolean finished;
	private static boolean mid;
	private static float h,w, alpha, incSign;
	private static String nextState; 
	
	public static void transite(String state){
		//transition animation(simple fade into black, emerge from black)
		if(nextState != state)
			nextState = state;
		if(finished)
			com.github.avilysalAndCeltic.VampTra.logic.GamePlay.changeState(state);
	}
	
	public static void reset(){
		finished = false;
		mid = false;
		incSign = 1;
		alpha = 0f;
		h = com.github.avilysalAndCeltic.VampTra.logic.GamePlay.DH;
		w = com.github.avilysalAndCeltic.VampTra.logic.GamePlay.DW;
	}
	
	public static void render(){
		if(!finished){
			alpha += 0.05*2*incSign;
			if(alpha >= 1f){
				mid = true;
				incSign = -1;
			}
			if(alpha <= 0) finished = true;
			com.github.avilysalAndCeltic.VampTra.logic.GamePlay.rend.createQuad(w/2, h/2, w, h, 0f, 0f, 0f, alpha);
		}
		transite(nextState);
	}
	
	public static boolean getMid(){
		return mid;
	}
}