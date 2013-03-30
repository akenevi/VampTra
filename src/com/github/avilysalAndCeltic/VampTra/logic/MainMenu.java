package com.github.avilysalAndCeltic.VampTra.logic;

public class MainMenu {
	private static String state = "New Game";
	private static byte s = 1;
	private static float shakingMagnitudeMultiplyer = 2;
	private static float posX = 25;
	private static float posY = 25;
	
	public static String getState(){
		return state;
	}
	
	public static void changeState(byte n){
		s += n;
		if(s<1) s=4;
		if(s>4) s=1;
		if(s==1) {state = "New Game";}
		if(s==2) {state = "Load Game";}
		if(s==3) {state = "Options";}
		if(s==4) {state = "Exit Game";}
		System.out.println(state);
	}
	
	public static void render(){
		if(s == 1)
			GamePlay.render.drawShakingString("New Game", posX, posY+51, shakingMagnitudeMultiplyer);
		else
			GamePlay.render.drawString("New Game", posX, posY+51);
		if(s == 2)
			GamePlay.render.drawShakingString("Load Game", posX, posY+34, shakingMagnitudeMultiplyer);
		else
			GamePlay.render.drawString("Load Game", posX, posY+34);
		if(s == 3)
			GamePlay.render.drawShakingString("Options", posX, posY+17, shakingMagnitudeMultiplyer);
		else
			GamePlay.render.drawString("Options", posX, posY+17);
		if(s == 4)
			GamePlay.render.drawShakingString("Exit Game", posX, posY, shakingMagnitudeMultiplyer);
		else
			GamePlay.render.drawString("Exit Game", posX, posY);
	}
}
