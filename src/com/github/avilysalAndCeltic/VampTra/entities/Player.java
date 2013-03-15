package com.github.avilysalAndCeltic.VampTra.entities;

public class Player extends Creature{
	private long score;
	
	public Player(String perk, String name){
		if (perk == "acrobat") addProf("dodge", 15);
		if (perk == "brawl") { addProf("melee", 10); addProf("block", 10);}
		if (perk == "scholar") addProf("magick", 15);
		this.type = "undead";
		this.name = name;
		this.x = 80f;
		this.y = 60f;
	}
	
	public long getScore(){
		return score;
	}
	
	public void update(){
		
	}
	public void move(String axis, int amount){
		move(axis, (float)amount);
	}
	
	public void move(String axis, float amount){
		if(axis=="x") x+=amount;
		if(axis=="y") y+=amount;
	}
	
	public void render(){
		com.github.avilysalAndCeltic.VampTra.logic.GamePlay.rend.createQuad(x, y, 10f);
	}
}
