package com.github.avilysalAndCeltic.VampTra.entities;

public class Player extends Creature{
	private long score;
	
	public Player(String perk){
		if (perk == "acrobat") addProf("dodge", 15);
		if (perk == "brawl"){ addProf("melee", 10); addProf("block", 10);}
		if (perk == "scholar") addProf("magick", 15);
	}
	
	public long getScore(){
		return score;
	}
}
