package com.github.avilysalAndCeltic.VampTra.logic;

public class MainMenu {
	private static String state = "";
	private static byte s = 1;
	
	public static String getState(){
		return state;
	}
	
	public static void changeState(byte n){
		s += n;
		if(s<1) s=1;
		if(s>4) s=4;
		if(s==1) {state = "New Game";}
		if(s==2) {state = "Load Game";}
		if(s==3) {state = "Options";}
		if(s==4) {state = "Exit Game";}
		System.out.println(state);
	}
}
