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
		if(getState() == "New Game")
			com.github.avilysalAndCeltic.VampTra.logic.GamePlay.text.drawShakingString("New Game", posX, posY+48, shakingMagnitudeMultiplyer);
		else
			com.github.avilysalAndCeltic.VampTra.logic.GamePlay.text.drawString("New Game", posX, posY+48);
		if(getState() == "Load Game")
			com.github.avilysalAndCeltic.VampTra.logic.GamePlay.text.drawShakingString("Load Game", posX, posY+32, shakingMagnitudeMultiplyer);
		else
			com.github.avilysalAndCeltic.VampTra.logic.GamePlay.text.drawString("Load Game", posX, posY+32);
		if(getState() == "Options")
			com.github.avilysalAndCeltic.VampTra.logic.GamePlay.text.drawShakingString("Options", posX, posY+16, shakingMagnitudeMultiplyer);
		else
			com.github.avilysalAndCeltic.VampTra.logic.GamePlay.text.drawString("Options", posX, posY+16);
		if(getState() == "Exit Game")
			com.github.avilysalAndCeltic.VampTra.logic.GamePlay.text.drawShakingString("Exit Game", posX, posY, shakingMagnitudeMultiplyer);
		else
			com.github.avilysalAndCeltic.VampTra.logic.GamePlay.text.drawString("Exit Game", posX, posY);
	}
}
