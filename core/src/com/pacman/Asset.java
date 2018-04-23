package com.pacman;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Asset {
	
	public static Texture items;
	
	public static Animation pacmanStand;
	public static Animation pacmanMoveLeft;
	public static Animation pacmanMoveRight;
	public static Animation pacmanMoveUp;
	public static Animation pacmanMoveDown;
	public static Animation	pacmanDie;
	
	public static Animation ghostMoveLeft;
	public static Animation ghostMoveRight;
	public static Animation ghostMoveUp;
	public static Animation ghostMoveDown;
	public static Animation	ghostGaspOut;
	public static Texture eyeGhostLeft;
	public static Texture eyeGhostRight;
	public static Texture eyeGhostUp;
	public static Texture eyeGhostDown;
	
	public static TextureRegion pill;
	
	
	
	
	
	@SuppressWarnings("unchecked")
	public static void load() {
		items = new Texture("animation1.png");
		
		pacmanStand = new Animation(0.1f, new TextureRegion(items, 642, 3, 32, 32));
		
		pacmanMoveRight = new Animation(0.1f, new TextureRegion(items, 642, 3, 32, 32), new TextureRegion(items, 642, 43, 32, 32), new TextureRegion(items, 642, 77, 32, 32));
		pacmanMoveDown = new Animation(0.1f, new TextureRegion(items, 642, 116, 32, 32), new TextureRegion(items, 642, 156, 32, 32), new TextureRegion(items, 642, 190, 32, 32));
		pacmanMoveLeft = new Animation(0.1f, new TextureRegion(items, 642, 228, 32, 32), new TextureRegion(items, 642, 268, 32, 32), new TextureRegion(items, 642, 302, 32, 32));
		pacmanMoveUp = new Animation(0.1f, new TextureRegion(items, 642, 340, 32, 32), new TextureRegion(items, 642, 380, 32, 32), new TextureRegion(items, 642, 422, 32, 32));
		pill = new TextureRegion(items, 305, 195, 32, 32);
		pacmanMoveRight.setPlayMode(PlayMode.LOOP_PINGPONG);
		pacmanMoveLeft.setPlayMode(PlayMode.LOOP_PINGPONG);
		pacmanMoveDown.setPlayMode(PlayMode.LOOP_PINGPONG);
		pacmanMoveUp.setPlayMode(PlayMode.LOOP_PINGPONG);		
		
	}
	
}
