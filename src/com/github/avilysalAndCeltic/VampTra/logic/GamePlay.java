package com.github.avilysalAndCeltic.VampTra.logic;

import org.lwjgl.util.Timer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import com.github.avilysalAndCeltic.VampTra.entities.*;
import com.github.avilysalAndCeltic.VampTra.map.Map;
import com.github.avilysalAndCeltic.VampTra.utils.PathFinder;
import com.github.avilysalAndCeltic.VampTra.utils.Renderer;

public class GamePlay {
	//state related stuff
	private enum State {INTRO, TRANSITION, PAUSED, MAIN_MENU, START_GAME, GENERATE_FLOOR, TURN, GAME_OVER, HIGHSCORES, EXIT_GAME};
	private static State currentState, prevState, nState;
	private static String nextState = "";
	
	//for updating entities ? dunno yet
	public static Timer clock;
	private static float pSpeed = 5.34f;
	
	//display opt
	public static final int DW = 800;
	public static final int DH = 600;
	private static boolean isResizable = false;
	private static boolean vSync = true;
	private static int fps = 60;
	
	private static boolean gameExit = false;
	
	public static Renderer render = new Renderer();
	
	public static Player player;
	public static Map map;
	
	public static PathFinder pathFind = new PathFinder();
	public static boolean generated = false;
	
	public static void main(String args[]){
		Thread renderer = new Thread(render);
		renderer.setDaemon(true);
		renderer.setName("Graphics Thread");
		renderer.start();
		if(render.startUp(DW, DH, fps, isResizable, vSync))
			startGame();
	}
	
	public static void gameLoop(){
		clock = new Timer();
		Thread path = new Thread(pathFind);
		path.setDaemon(true);
		path.setName("Pathfinding Thread");
		
		while(!gameExit){
			if(Display.isCloseRequested()) currentState = State.EXIT_GAME;
			Timer.tick();
			//updates
			render();
			switch (currentState){
				case INTRO:
					Intro.InitIntro();
					path.start();
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
				case GENERATE_FLOOR:
					if(generated){
						transiteState("TURN");
					}
					break;
				case TURN:
					getInput();
					player.update();
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
		//	System.out.println("update!");
		}
		render.CleanUp();
	}
	
	public static void getInput(){
		int key = 0;
		if(currentState != State.TURN){
			while(Keyboard.next()){
				if(Keyboard.getEventKeyState()) //if the key is down, updates key variable to that key, non-repetitive
					key = Keyboard.getEventKey();
			}
		} else {
			Keyboard.next();
			if(Keyboard.getEventKeyState()) //if the key is down, updates key variable to that key, repetitive
				key = Keyboard.getEventKey();
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
				if(opt == "New Game") {transiteState("START_GAME"); clock.reset();}
				//		if(opt == "Load Game")
				//		if(opt == "Options")
				if(opt == "Exit Game") transiteState("EXIT_GAME");
				break;
			case START_GAME:
				player = new Player(GameStart.getState());
				map = new Map();
				clock.reset();
				transiteState("GENERATE_FLOOR");
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
				map.changeOffset(player.getFloor(), (byte) 0, pSpeed);
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
				map.changeOffset(player.getFloor(), (byte) 2, pSpeed);
				break;
			default:break;
			}
			break;
		case Keyboard.KEY_LEFT:
			switch(currentState){
			case TURN:
				map.changeOffset(player.getFloor(), (byte) 3, pSpeed);
				break;
			default:break;
			}
			break;
		case Keyboard.KEY_RIGHT:
			switch(currentState){
			case TURN:
				map.changeOffset(player.getFloor(), (byte) 1, pSpeed);
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
	
	private static void render(){
		render.preRender();
		
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
			case GENERATE_FLOOR:
				render.drawString("Generating Floor", (float)DW/2-(float)17/2*12, (float)DH/2+(float)2*16);
				render.drawShakingString("!loading screen will be added later!", (float)DW/2-(float)36/2*12, (float)DH/2-(float)3*16, 0.7f);
				break;
			case PAUSED:
				render.drawString("Press ESQ to resume", (float)DW/2-(float)19/2*12, (float)DH/2+(float)2*16);
				render.drawString("Press Enter to exit to Main Menu", (float)DW/2-(float)32/2*12, (float)DH/2+(float)0*16);
				render.drawShakingString("!actual menu will be added later!", (float)DW/2-(float)33/2*12, (float)DH/2-(float)3*16, 0.7f);
				break;
			case TURN:
				map.render(player.getFloor());
				player.render();
				break;
			default:break;
		}
		render.afterRender();
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
				render.drawString("Press ESQ to resume", (float)DW/2-(float)19/2*12, (float)DH/2+(float)2*16);
				render.drawString("Press Enter to exit to Main Menu", (float)DW/2-(float)32/2*12, (float)DH/2+(float)0*16);
				render.drawShakingString("!actual menu will be added later!", (float)DW/2-(float)33/2*12, (float)DH/2-(float)3*16, 0.7f);
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
		else if (newState=="GENERATE_FLOOR") nState = State.GENERATE_FLOOR;
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
		if (newState=="GENERATE_FLOOR") currentState = State.GENERATE_FLOOR;
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
}