package com.github.avilysalAndCeltic.VampTra.logic;

import static org.lwjgl.opengl.ARBTextureRectangle.GL_TEXTURE_RECTANGLE_ARB;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.Timer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.github.avilysalAndCeltic.VampTra.utils.*;

public class GamePlay {
	//Class that starts everything up and is containing and handling all the game states
	
	private enum State {INTRO, TRANSITION, PAUSED, MAIN_MENU, START_GAME, GENERATE_ROOM, TURN, GAME_OVER, HIGHSCORES, EXIT_GAME};
	private static State currentState;
	private static String nextState = "";
	private static Timer clock;
	private static int fps = 60;
	private static Renderer rend;

	public static final int DW = 800;
	public static final int DH = 600;
	
	private static boolean gameExit = false;
	
	public static void main(String args[]){
		InitGLandDisplay();
		startGame();
	}
	
	public static void gameLoop(){
		while(!gameExit){
			System.out.println("Current State: "+currentState);
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
					getInput();
					break;
				case MAIN_MENU:
					getInput();
					break;
				case START_GAME:
					if(clock.isPaused()) clock.resume();
					break;
				case GENERATE_ROOM:
					break;
				case TURN:
					Timer.tick();
					getInput();
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
			glClear(GL_COLOR_BUFFER_BIT);
			glLoadIdentity();
			
			//render
			rend.createQuad(400, 300, 10, 0, 0, 0, 0.1f);
			
			System.out.println("update!");
			Display.sync(fps);
			Display.update();
		}
		CleanUp();
	}
	
	public static void getInput(){
		// for debugging purposes
		System.out.println("Waiting for keyboard event");
		int prevKey = 0;
		
		while(!Keyboard.next() || Keyboard.getEventKey() == 0){
			Display.update();
		
			//for debugging purposes
			int key = Keyboard.getEventKey();
			if (key != 0 && key != prevKey){ 
				System.out.println("Key pressed: "+Keyboard.getKeyName(key));
				prevKey = key;
			}
			
			switch(key){
				case Keyboard.KEY_ESCAPE:
					switch(currentState){
						case PAUSED:
							transiteState("MAIN_MENU");
							break;
						case MAIN_MENU:
							transiteState("EXIT_GAME");
							break;
						case TURN:
							transiteState("PAUSED");
							break;
						case GENERATE_ROOM:
							transiteState("PAUSED");
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
					break;
				case Keyboard.KEY_UP:
					break;
				case Keyboard.KEY_DOWN:
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
		rend = new Renderer();
		gameLoop();
	}
	
	private static void InitGLandDisplay(){
		try{
			Display.setDisplayMode(new DisplayMode(DW,DH));
			Display.create();
			Display.setResizable(false);
			Display.setVSyncEnabled(true);
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
