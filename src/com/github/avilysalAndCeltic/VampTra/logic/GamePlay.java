package com.github.avilysalAndCeltic.VampTra.logic;

import static org.lwjgl.opengl.ARBTextureRectangle.GL_TEXTURE_RECTANGLE_ARB;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.Timer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.github.avilysalAndCeltic.VampTra.entities.*;
import com.github.avilysalAndCeltic.VampTra.map.Map;
import com.github.avilysalAndCeltic.VampTra.utils.Renderer;

public class GamePlay {
	//state related stuff
	private enum State {INTRO, TRANSITION, PAUSED, MAIN_MENU, START_GAME, TURN, GAME_OVER, HIGHSCORES, EXIT_GAME};
	private static State currentState, prevState, nState;
	private static String nextState = "";
	
	//for updating entities ? dunno yet
	private static Timer clock;
	
	//display opt
	public static final int DW = 800;
	public static final int DH = 600;
	private static boolean isResizable = false;
	private static boolean vSync = true;
	private static int fps = 60;
	
	private static boolean gameExit = false;
	
	public static Renderer rend;
	public static Renderer text;
	public static Map map;
	public static Player player;
	
	public static void main(String args[]){
		InitGLandDisplay();
		startGame();
	}
	
	public static void gameLoop(){
		text = new Renderer("font");
		rend = new Renderer("");
		clock = new Timer();
		while(!gameExit){
			if(Display.isCloseRequested()) currentState = State.EXIT_GAME;
			Timer.tick();
			//updates
			switch (currentState){
				case INTRO:
					Intro.InitIntro();
					transiteState("MAIN_MENU");
					break;
				case TRANSITION:
					Trans.transite(nextState);
					break;
				case PAUSED:
					getInput();
					break;
				case MAIN_MENU:
					getInput();
					break;
				case START_GAME:
					//player creation.. choosing perk, naming, ingame intro.
					getInput();
					break;
				case TURN:
					getInput();
					// update stuff
					break;
				case GAME_OVER:
					break;
				case HIGHSCORES:
					break;
				case EXIT_GAME:
					gameExit = true;
					//time to save things that need saving
					System.out.println("Exiting game");
					break;
			}
			render();
		//	System.out.println("update!");
		}
		CleanUp();
	}
	
	public static void getInput(){
		int key = 0;
		Keyboard.next();
		if(clock.getTime() >= 0.075){
			if(Keyboard.getEventKeyState()) //if the key is down, updates key variable to that key
				key = Keyboard.getEventKey();
			clock.reset();
		}
		System.out.println(clock.getTime());
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
			case START_GAME:
				transiteState("MAIN_MENU");
				break;
			case TURN:
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
			switch(currentState){
			case MAIN_MENU:
				String opt = MainMenu.getState();
				if(opt == "New Game") transiteState("START_GAME");
				//		if(opt == "Load Game")
				//		if(opt == "Options")
				if(opt == "Exit Game") transiteState("EXIT_GAME");
				break;
			case START_GAME:
				map = new Map();
				player = new Player(GameStart.getState());
				transiteState("TURN");
				break;
			case PAUSED:
				player = null;
				map = null;
				transiteState("MAIN_MENU");
				break;
			default:break;
			}
			break;
		case Keyboard.KEY_UP:
			switch(currentState){
			case MAIN_MENU:
				MainMenu.changeState((byte) -1);
				break;
			case START_GAME:
				GameStart.changeState((byte) -1);
				break;
			case TURN:
				map.changeOffset(player.getFloor(), "y", -16);
				break;
			default:break;
			}
			break;
		case Keyboard.KEY_DOWN:
			switch(currentState){
			case MAIN_MENU:
				MainMenu.changeState((byte) 1);
				break;
			case START_GAME:
				GameStart.changeState((byte) 1);
				break;
			case TURN:
				map.changeOffset(player.getFloor(), "y", 16);
				break;
			default:break;
			}
			break;
		case Keyboard.KEY_LEFT:
			switch(currentState){
			case TURN:
				map.changeOffset(player.getFloor(), "x", 16);
				break;
			default:break;
			}
			break;
		case Keyboard.KEY_RIGHT:
			switch(currentState){
			case TURN:
				map.changeOffset(player.getFloor(), "x", -16);
				break;
			default:break;
			}
			break;
		case Keyboard.KEY_SPACE:
			switch(currentState){
			default:break;
			}
			break;
		default:break;
		}
	}
	
	private static void preRender(){
		//clearing old stuff
		glClear(GL_COLOR_BUFFER_BIT);
		glLoadIdentity();
	}
	
	private static void render(){
		preRender();
		
		switch(currentState){
			case TRANSITION:
				transRender();
				break;
			case MAIN_MENU:
				MainMenu.render();
				break;
			case START_GAME:
				GameStart.render();
				break;
			case PAUSED:
				text.drawString("Press ESQ to resume", (float)DW/2-(float)19/2*12, (float)DH/2+(float)2*16);
				text.drawString("Press Enter to exit to Main Menu", (float)DW/2-(float)32/2*12, (float)DH/2+(float)0*16);
				text.drawShakingString("!actual menu will be added later!", (float)DW/2-(float)33/2*12, (float)DH/2-(float)3*16, 0.7f);
				break;
			case TURN:
				map.render(player.getFloor());
				player.render();
				break;
			default:break;
		}
		
		{	//swap buffers, poll new input and finally autotune (Display.sync(fps)) Thread sleep to meet stated fps
			Display.update();
			Display.sync(fps);
		}
	}
	
	private static void transRender(){
		State temp; 
		if (Trans.getMid()) temp = nState;
		else temp = prevState;
		
		switch(temp){
			case MAIN_MENU:
				MainMenu.render();
				break;
			case START_GAME:
				GameStart.render();
				break;
			case PAUSED:
				text.drawString("Press ESQ to resume", (float)DW/2-(float)19/2*12, (float)DH/2+(float)2*16);
				text.drawString("Press Enter to exit to Main Menu", (float)DW/2-(float)32/2*12, (float)DH/2+(float)0*16);
				text.drawShakingString("!actual menu will be added later!", (float)DW/2-(float)33/2*12, (float)DH/2-(float)3*16, 0.7f);
				break;
			case TURN:
				map.render(player.getFloor());
				player.render();
				break;
			default:break;
		}
		
		Trans.render();
	}
	
	public static void transiteState(String newState){
		prevState = currentState;
		currentState = State.TRANSITION;
		if (newState=="PAUSED") nState = State.PAUSED;
		else if (newState=="MAIN_MENU") nState = State.MAIN_MENU;
		else if (newState=="START_GAME") nState = State.START_GAME;
		else if (newState=="TURN") nState = State.TURN;
		else if (newState=="GAME_OVER") nState = State.GAME_OVER;
		else if (newState=="HIGHSCORES") nState = State.HIGHSCORES;
		else if (newState=="EXIT_GAME") nState = State.EXIT_GAME;
		nextState = newState;
		Trans.reset();
	}
	
	public static void changeState(String newState){
		//for debugging
		System.out.println("Now entering: "+ newState + " state");
		
		if (newState=="TRANSITION") currentState = State.TRANSITION;
		if (newState=="PAUSED") currentState = State.PAUSED;
		if (newState=="MAIN_MENU") currentState = State.MAIN_MENU;
		if (newState=="START_GAME") currentState = State.START_GAME;
		if (newState=="TURN") currentState = State.TURN;
		if (newState=="GAME_OVER") currentState = State.GAME_OVER;
		if (newState=="HIGHSCORES") currentState = State.HIGHSCORES;
		if (newState=="EXIT_GAME") currentState = State.EXIT_GAME;
	}
	
	private static void startGame(){
		gameExit = false;
		currentState = State.INTRO;
		prevState = State.INTRO;
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
		Keyboard.enableRepeatEvents(true);
	}
}
