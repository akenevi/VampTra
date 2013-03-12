package com.github.avilysalAndCeltic.VampTra.utils;

import static org.lwjgl.opengl.ARBTextureRectangle.GL_TEXTURE_RECTANGLE_ARB;
import static org.lwjgl.opengl.GL11.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;

import org.lwjgl.BufferUtils;

public class Renderer {
	private static HashMap<String, Sprite> spriteMap;
	private static final String location="res/spriteSheet.png"; //all sprites are in the same image
	private static Sprite curSprite;
	private static int tiles;
	
	public Renderer(){
	//	initSprites();
	}
	
	public void createQuad(float x, float y, float s){
		createQuad(x,y,s,0f,0f,0f,0);
	}
	
	public void createQuad(float x, float y, float s, float r, float g, float b){
		createQuad(x,y,s,r,g,b,0);
	}
	
	public void createQuad(float x, float y, float s, float r, float g, float b, float rot){
		glPushMatrix();
		{
			glTranslatef(x, y, 0);
			glRotatef(rot,0,0,1);
			glColor3f(r,g,b);
			
			glBegin(GL_QUADS);
			{
				glColor3f(1,1,1);
				glVertex2f(-s,-s);
				glVertex2f(s,-s);
				glVertex2f(s,s);
				glVertex2f(-s,s);
			}
			glEnd();
		}
		glPopMatrix();
	}
	
	public static void drawSprite(String name, float x, float y, int hs, int rotate){
		drawSprite(name, x, y, hs, rotate, false);
	}

	public static void drawSprite(String name, float x, float y, int s, int rotate, boolean reverseMap) {
		curSprite = spriteMap.get(name);
		int u = curSprite.x;
		int v = curSprite.y2;
		int u2 = curSprite.x2;
		int v2 = curSprite.y;
		int hs = s/2;
		if(reverseMap){
			u=curSprite.x2;
			u2=curSprite.x;
		}
		glPushMatrix();
		
		glTranslatef(x,y,0);
		glRotatef(rotate,0,0,1);
		
		glBindTexture(GL_TEXTURE_RECTANGLE_ARB, tiles);
		{
			glBegin(GL_QUADS);
			{
				glTexCoord2f(u, v);		glVertex2f(-hs, -hs);
				glTexCoord2f(u2, v);	glVertex2f(hs, -hs);
				glTexCoord2f(u2, v2);	glVertex2f(hs, hs);
				glTexCoord2f(u, v2);	glVertex2f(-hs, hs);
			}
			glEnd();
		}
		glBindTexture(GL_TEXTURE_RECTANGLE_ARB, 0);
		glPopMatrix();
	}
	
	
	public static void initSprites(){
		spriteMap = new HashMap<String, Sprite>();
		int texture = glGenTextures();
		tiles = texture;
		glBindTexture(GL_TEXTURE_RECTANGLE_ARB, texture);
		InputStream in = null;
		try {
			in = new FileInputStream(location);
			PNGDecoder decoder = new PNGDecoder(in);
			ByteBuffer buffer = BufferUtils.createByteBuffer(4 * decoder.getWidth() * decoder.getHeight());
			decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
			buffer.flip();
			in.close();
			glTexParameteri(GL_TEXTURE_RECTANGLE_ARB, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_RECTANGLE_ARB, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexImage2D(GL_TEXTURE_RECTANGLE_ARB, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		} catch (FileNotFoundException e) {
			System.err.println("File couldn't be found at "+ location);
		} catch (IOException e) {
			System.err.println("File couldn't be loaded from "+ location);
		}
		glBindTexture(GL_TEXTURE_RECTANGLE_ARB, 0);
		// create sprite map usind method below
		// upper left corner of SpriteSheet == (0,0)
		// spriteMap.put("key name", new Sprite("sprite name", x, y, size x, size y));
	}
}

class Sprite {
    String name;
    public int x, y, x2, y2, w, h;

    public Sprite(String name, int x, int y, int w, int h) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        x2 = x + w;
        y2 = y + h;
    }
}