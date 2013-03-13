package com.github.avilysalAndCeltic.VampTra.entities;

public abstract class Creature {
	protected String name;
	protected String type;
	protected float x,y;
	protected String[] skills = new String[9];
	private byte[][] proficiency = new byte[4][5];
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public String[] getSkills(){
		return skills;
	}
	
	private byte profi(String prof){
		if (prof=="melee") return proficiency[0][0];
		//different types of weapons; staff == spear
			if (prof=="unarmed") return proficiency[0][1];
			if (prof=="1h") return proficiency[0][2];
			if (prof=="2h") return proficiency[0][3];
			if (prof=="staff") return proficiency[0][4];
			
		if (prof=="magick") return proficiency[1][0];
		//spells
			if (prof=="heal") return proficiency[1][1];
			if (prof=="fireball") return proficiency[1][2];
/*			if (prof=="magick") return proficiency[1][3];
			if (prof=="magick") return proficiency[1][4];
			
*/		if (prof=="dodge") return proficiency[2][0];
		if (prof=="block") return proficiency[3][0];
		
		//if not found, return -1
		return -1;
	}
	
	public byte getProf(String prof){
		return profi(prof);
	}
	
	protected void addProf(String prof, int i){
		addProf(prof, (byte) i);
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
