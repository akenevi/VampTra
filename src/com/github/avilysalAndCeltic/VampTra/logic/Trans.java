package com.github.avilysalAndCeltic.VampTra.logic;

public class Trans {
//	private static boolean finished;
	
	public static void transite(String state){
		//transition animation(simple fade into black, emerge from black)
//		finished = false;
//		while(!finished){
//			animation	
//			if(transperancy < 0) finished = true;
//		}
		com.github.avilysalAndCeltic.VampTra.logic.GamePlay.changeState(state);
	}
}
