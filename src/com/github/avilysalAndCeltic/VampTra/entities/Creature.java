package com.github.avilysalAndCeltic.VampTra.entities;

import java.util.ArrayList;

public abstract class Creature extends Entity{
	protected ArrayList <String> skills;
	//equipped = "r hand", "l hand", "armor"  for now..
	protected String[] equipped = {"none","none","none"};
	protected boolean hasShield = false;
	protected byte armor = 0;
	protected byte shield = 0;
	protected byte health = 100;
	private byte[][] proficiency = new byte[4][6];
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public ArrayList <String> getSkills(){
		return skills;
	}
	
	public void attackMelee(Creature c){
		byte damage = 0; //for now
		if(getProf("melee") - c.getProf("dodge") < 0){
			if(com.github.avilysalAndCeltic.VampTra.logic.GamePlay.rend.random.nextInt(100) > 100-5){
				c.changeHealth((byte)(-1*damage));
				// inform about successful hit
			} else {
				c.addProf("dodge", 1);
				// inform about happened miss
			}
		} else {
			int block = getProf("block");
			if(c.hasShield())
				block += c.getProf("shiled");
			if(com.github.avilysalAndCeltic.VampTra.logic.GamePlay.rend.random.nextInt(100) > 100-block){
				c.changeHealth((byte)(-1*damage));
				c.changeArmor((byte)(-1*damage/5));
				// inform about successful hit
			} else {
				if(c.hasShield())
					c.changeShield((byte)(-1*damage/5));
				else
					c.changeHealth((byte)(-1*damage/2));
				// inform about happened block
			}
		}
	}
	
	public void attackMagick(Creature c){
		
	}
	
	public void useSkill(){
		
	}
	
	public void changeHealth(byte amount){
		health+=amount;
	}
	
	public void changeArmor(byte amount){
		armor+=amount;
	}
	
	public void changeShield(byte amount){
		shield+=amount;
		if(shield <= 0){
			shield = 0;
			hasShield = false;
		}
		else
			hasShield = true;
	}
	
	public byte getArmor(){
		return armor;
	}
	
	public boolean hasShield(){
		return hasShield;
	}
	
	private byte profi(String prof){
		if (prof=="magick") return proficiency[0][0];
		//spells
			if (prof=="heal") return proficiency[0][1];
			if (prof=="fireball") return proficiency[0][2];
/*			if (prof=="magick") return proficiency[0][3];
			if (prof=="magick") return proficiency[0][4];
			
*/		if (prof=="melee") return proficiency[1][0];
		//different types of weapons; staff == spear
			if (prof=="unarmed") return proficiency[1][1];
			if (prof=="dagger") return proficiency[1][2];
			if (prof=="1h") return proficiency[1][3];
			if (prof=="2h") return proficiency[1][4];
			if (prof=="staff") return proficiency[1][5];
			

		if (prof=="block") return proficiency[2][0];
			if (prof=="shield") return proficiency[2][1];
		if (prof=="dodge") return proficiency[3][0];
		
		//if not found, return -1
		return -1;
	}
	
	public byte getProf(String prof){
		return profi(prof);
	}
	
	protected void addProf(String prof, int i){
		addProf(prof, (byte) i);
	}
	
	protected void initProf(){
		for(int j=0; j<proficiency.length; j++)
			for(int k=0; k<proficiency[j].length; k++)
				proficiency[j][k] = 0;
	}
	
	protected void addProf(String prof, byte i){
		if (profi(prof)+i <= 100) 
			for(int j=0; j<proficiency.length; j++)
				for(int k=0; k<proficiency[j].length; k++)
					if(proficiency[j][k]==profi(prof))
						proficiency[j][k] = (byte)(proficiency[j][k]+i);
		
		if (prof=="melee") if(proficiency[0][0] <= 100) proficiency[0][0] = (byte) (proficiency[0][0]+i);
		//different types of weapons; staff == spear
			if (prof=="unarmed") proficiency[0][1] = (byte) (proficiency[0][1]+i);
			if (prof=="1h") proficiency[0][2] = (byte) (proficiency[0][2]+i);
			if (prof=="2h") proficiency[0][3] = (byte) (proficiency[0][3]+i);
			if (prof=="staff") proficiency[0][4] = (byte) (proficiency[0][4]+i);
			
		if (prof=="magick") proficiency[1][0] = (byte) (proficiency[1][0]+i);
		//spells
			if (prof=="heal") proficiency[1][1] = (byte) (proficiency[1][1]+i);
			if (prof=="fireball") proficiency[1][2] = (byte) (proficiency[1][2]+i);
/*			if (prof=="magick") proficiency[1][3] = (byte) (proficiency[1][3]+i);
			if (prof=="magick") proficiency[1][4] = (byte) (proficiency[1][4]+i);
			
*/		if (prof=="dodge") proficiency[2][0] = (byte) (proficiency[2][0]+i);
		if (prof=="block") proficiency[3][0] = (byte) (proficiency[3][0]+i);
	}
}
