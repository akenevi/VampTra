package com.github.avilysalAndCeltic.VampTra.entities;

public abstract class Item extends Entity{

	 protected String name;
	 protected float durability;
	 
	 public String getName(){
		 return name;
	 }
	 
	 public float getDurability(){
		 return durability;
	 }
}
