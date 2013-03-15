package com.github.avilysalAndCeltic.VampTra.entities;

public abstract class Item extends Entity{

	 protected float damage;
	 protected float durability;
	 
	 public float getDamage(){
		 return damage;
	 }
	 
	 public float getDurability(){
		 return durability;
	 }
	 
	 public String getType(){
		 return type;
	 }
	 
	 public String getName(){
		 return name;
	 }
}
