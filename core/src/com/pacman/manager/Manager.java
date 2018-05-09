package com.pacman.manager;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class Manager implements Disposable{
	
	public static final Manager manager = new Manager();
	
	public static final short PillBit = 1 << 2 ;
	public static final short pacmanBit = 1 << 3;
	public static final short wallBit = 1 << 4;
	public static final float PPM = 32f;
	
	
	public boolean gameOver = false;
	public static Integer score = 1000;

	public static final short ghostBit = 1 << 5;
	public int highScore = 0;
	
	public int playAlive = 3;
	public float invi = 5f;
	public boolean isInvi;
	public Vector2 pacmanLocation;
	public boolean isGhostScare;
	
	public Vector2 pacmanSpawPos;
	public Vector2 ghostSpawPos;
	public Manager() {
		pacmanSpawPos = new Vector2();
		ghostSpawPos = new Vector2();
		isInvi = false;
	}
	
	public void makeGameOver() {
		gameOver = true;
		
	}

	@Override
	public void dispose() {
		
		
	}

}
