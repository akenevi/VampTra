package com.github.avilysalAndCeltic.VampTra.logic;

import static org.lwjgl.opengl.ARBTextureRectangle.GL_TEXTURE_RECTANGLE_ARB;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.Timer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.github.avilysalAndCeltic.VampTra.utils.*;
import com.github.avilysalAndCeltic.VampTra.entities.*;

public class GamePlay {
	//Class that starts everything up and is containing and handling all the game states
	
	private enum State {INTRO, TRANSITION, PAUSED, MAIN_MENU, START_GAME, GENERATE_ROOM, TURN, GAME_OVER, HIGHSCORES, EXIT_GAME};
	private static State currentState;
	private static String nextState = "";
	private static Timer clock;
	
	//display opt
	private static final int DW = 160;
	private static final int DH = 120;
	private static boolean isResizable = false;
	private static boolean vSync = false;
	private static int fps = 60;
	
	private static boolean gameExit = false;
	
	private static Player player;
	
	public static void main(String args[]){
		InitGLandDisplay();
		startGame();
	}
	
	public static void gameLoop(){
		while(!gameExit){
			if(Display.isCloseRequested()) currentState = State.EXIT_GAME;
			
			//for debugging purposes
			State prevState = State.EXIT_GAME;
			if (currentState == prevState)
				System.out.println("Current State: "+currentState);
			prevState = currentState;
			
			//updates
			switch (currentState){
				case INTRO:
					clock = new Timer();
					clock.pause();
					clock.set(0.0f);
					com.github.avilysalAndCeltic.VampTra.logic.Intro.InitIntro();
					transiteState("MAIN_MENU");
					break;
				case TRANSITION:
					com.github.avilysalAndCeltic.VampTra.logic.Trans.transite(nextState);
					break;
				case PAUSED:
					clock.pause();
					getInput();
					break;
				case MAIN_MENU:
					getInput();
					break;
				case START_GAME:
					if(clock.isPaused()) clock.resume();
					
					player = new Player("brawl");
					//generate starting room
					changeState("TURN");
					break;
				case GENERATE_ROOM:
					changeState("TURN");
					break;
				case TURN:
					Timer.tick();
					getInput();
					player.update();
					// update stuff
					changeState("TURN");
					break;
				case GAME_OVER:
					break;
				case HIGHSCORES:
					break;
				case EXIT_GAME:
					gameExit = true;
					//time to save thing that need saving
					System.out.println("Exiting game");
					break;
			}
			//clearing old stuff
			glClear(GL_COLOR_BUFFER_BIT);
			glLoadIdentity();
			
			//render updated stuff
			
			//swap buffers, poll new input and finally autotune (Display.sync(fps)) Thread sleep to meet stated fps
			Display.update();
			Display.sync(fps);
		}
		CleanUp();
	}
	
	public static void getInput(){
		// for debugging purposes
		// System.out.println("Waiting for keyboard event");
		
		while(Keyboard.next()){
			int key = 0;
			if(Keyboard.getEventKeyState())
				key = Keyboard.getEventKey();
		
			//for debugging purposes
			if (key != 0){ 
				System.out.println("Key pressed: "+Keyboard.getKeyName(key));
			}
			
			switch(key){
				case Keyboard.KEY_ESCAPE:
					switch(currentState){
						case PAUSED:
							transiteState("TURN");
							break;
						case MAIN_MENU:
							transiteState("EXIT_GAME");
							break;
						case TURN:
							transiteState("PAUSED");
							break;
						case GENERATE_ROOM:
							break;
						case GAME_OVER:
							transiteState("HIGHSCORES");
							break;
						case HIGHSCORES:
							transiteState("MAIN_MENU");
							break;
						default:break;
					}
					break;
				case Keyboard.KEY_RETURN:
					switch(currentState){
						case MAIN_MENU:
							String opt = com.github.avilysalAndCeltic.VampTra.logic.MainMenu.getState();
							if(opt == "New Game") transiteState("START_GAME");
					//		if(opt == "Load Game")
					//		if(opt == "Options")
							if(opt == "Exit Game") transiteState("EXIT_GAME");
							break;
						default:break;
					}
					break;
				case Keyboard.KEY_UP:
					switch(currentState){
						case MAIN_MENU:
							com.github.avilysalAndCeltic.VampTra.logic.MainMenu.changeState((byte) -1);
							break;
						default:break;
					}
					break;
				case Keyboard.KEY_DOWN:
					switch(currentState){
					case MAIN_MENU:
						com.github.avilysalAndCeltic.VampTra.logic.MainMenu.changeState((byte) 1);
						break;
					default:break;
				}
					break;
				case Keyboard.KEY_LEFT:
					break;
				case Keyboard.KEY_RIGHT:
					break;
				case Keyboard.KEY_SPACE:
					break;
				default:break;
			}
		}
	}
	
	public static void transiteState(String newState){
		currentState = State.TRANSITION;
		nextState = newState;
	}
	
	public static void changeState(String newState){
		if (newState=="TRANSITION") currentState = State.TRANSITION;
		if (newState=="PAUSED") currentState = State.PAUSED;
		if (newState=="MAIN_MENU") currentState = State.MAIN_MENU;
		if (newState=="START_GAME") currentState = State.START_GAME;
		if (newState=="GENERATE_ROOM") currentState = State.GENERATE_ROOM;
		if (newState=="TURN") currentState = State.TURN;
		if (newState=="GAME_OVER") currentState = State.GAME_OVER;
		if (newState=="HIGHSCORES") currentState = State.HIGHSCORES;
		if (newState=="EXIT_GAME") currentState = State.EXIT_GAME;
	}
	
	private static void startGame(){
		gameExit = false;
		currentState = State.INTRO;
		gameLoop();
	}
	
	private static void InitGLandDisplay(){
		try{
			Display.setDisplayMode(new DisplayMode(DW,DH));
			Display.create();
			Display.setResizable(isResizable);
			Display.setVSyncEnabled(vSync);
			Keyboard.create();
		} catch (Exception e) {
			System.err.println("Failed to create window");
		}
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0,DW,0,DH,-1,1);
		
		glEnable(GL_TEXTURE_RECTANGLE_ARB);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glClearColor(0,0,0,1);
		glDisable(GL_DEPTH_TEST);
		
		glMatrixMode(GL_MODELVIEW);
	}
	
	private static void CleanUp(){
		Display.sync(0);
		Display.destroy();
		Keyboard.destroy();
	}
}
