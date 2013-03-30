package com.github.avilysalAndCeltic.VampTra.logic;

public class GameStart {
	private static float shakingMagnitude = 1.5f;
	private static String message ="While alive your ambition was ...";
	private static String opt1 ="to be the strongest.";
	private static String opt2 ="to be the most agile.";
	private static String opt3 ="to have a lot of knowledge.";
	private static String ret1 ="brawl";
	private static String ret2 ="acrobat";
	private static String ret3 ="scholar";
	private static float x = GamePlay.DW/2 - message.lastIndexOf('.')*12/2;
	private static float y = GamePlay.DH*0.75f;
	private static byte state = 1;
			
	public static void render(){
		GamePlay.render.drawString(message, x, y);
		if (state == 1) GamePlay.render.drawShakingString(opt1, x, y-40, shakingMagnitude);
		else GamePlay.render.drawString(opt1, x, y-40);
		if (state == 2) GamePlay.render.drawShakingString(opt2, x, y-60, shakingMagnitude);
		else GamePlay.render.drawString(opt2, x, y-60);
		if (state == 3) GamePlay.render.drawShakingString(opt3, x, y-80, shakingMagnitude);
		else GamePlay.render.drawString(opt3, x, y-80);
	}
	
	public static void changeState(byte n){
		state += n;
		if(state < 1) state = 3;
		if(state > 3) state = 1;
	}
	
	public static String getState(){
		if(state == 1) return ret1;
		else if(state == 2) return ret2;
		else if(state == 3) return ret3;
		else return null;
	}
}
