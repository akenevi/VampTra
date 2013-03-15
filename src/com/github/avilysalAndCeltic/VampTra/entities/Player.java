package com.github.avilysalAndCeltic.VampTra.entities;

public class Player extends Creature{
	private long score;
	
	public Player(String perk, String name){
		initProf();
		addProf("magick", 5);
			addProf("heal", 5);
			addProf("fireball", 5);
		addProf("melee", 5);
			addProf("unarmed", 60);
			addProf("dagger", 20);
			addProf("1h", 15);
			addProf("2h", 10);
			addProf("staff", 10);
		addProf("block", 10);
			addProf("shield", 15);
		addProf("dodge", 20);
		if (perk == "acrobat") { addProf("dodge", 15); addProf("dagger", 30); }
		if (perk == "brawl") { addProf("melee", 10); addProf("block", 10); addProf("unarmed", 10); }
		if (perk == "scholar") { addProf("magick", 15); addProf("heal", 10); addProf("fireball", 10); }
		this.type = "undead";
		this.name = name;
		this.x = com.github.avilysalAndCeltic.VampTra.logic.GamePlay.DW/2;
		this.y = com.github.avilysalAndCeltic.VampTra.logic.GamePlay.DH/2;
	}
	
	public void addSkill(String skill){
		skills.add(skill);
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
		com.github.avilysalAndCeltic.VampTra.logic.GamePlay.rend.createQuad(x, y, 16f);
	}

	public long getScore(){
		return score;
	}
}
