package com.github.avilysalAndCeltic.VampTra.entities;

public class Player extends Creature{
	private long score;
	private int floor;
	
	public Player(String perk){
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
		if (perk == "brawl") { addProf("melee", 10); addProf("block", 10); addProf("unarmed", 10); }
		if (perk == "acrobat") { addProf("dodge", 15); addProf("dagger", 30); }
		if (perk == "scholar") { addProf("magick", 15); addProf("heal", 10); addProf("fireball", 10); }
		this.x = com.github.avilysalAndCeltic.VampTra.logic.GamePlay.DW/2;
		this.y = com.github.avilysalAndCeltic.VampTra.logic.GamePlay.DH/2;
		floor = 0;
	}
	
	public int getFloor(){
		return floor;
	}
	
	public void addSkill(String skill){
		skills.add(skill);
	}
	
	public void update(){
		
	}
	
	public void render(){
		com.github.avilysalAndCeltic.VampTra.logic.GamePlay.rend.createQuad(x, y, 16f);
	}

	public long getScore(){
		return score;
	}
}
