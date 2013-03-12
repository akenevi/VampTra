package com.github.avilysalAndCeltic.VampTra.entities;

public abstract class Item {

	 protected float damage;
	 protected float durability;
	 protected String type;
	 protected String name;
	 
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
