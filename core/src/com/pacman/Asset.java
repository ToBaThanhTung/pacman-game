package com.pacman;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pacman.Astar.PathFinding;

public class Asset {
	
	public static Animation<TextureRegion> ghostScare;

	public static Texture items;
	
	public static Animation<TextureRegion> pacmanStand;
	public static Animation<TextureRegion> pacmanMoveLeft;
	public static Animation<TextureRegion> pacmanMoveRight;
	public static Animation<TextureRegion> pacmanMoveUp;
	public static Animation<TextureRegion> pacmanMoveDown;
	public static Animation<TextureRegion>	pacmanDie;
	
	public static Animation<TextureRegion> ghostRedMoveLeft;
	public static Animation<TextureRegion> ghostRedMoveRight;
	public static Animation<TextureRegion> ghostRedMoveUp;
	public static Animation<TextureRegion> ghostRedMoveDown;
	public static Animation<?>	ghostGaspOut;
	
	
	
	public static Texture eyeGhostLeft;
	public static Texture eyeGhostRight;
	public static Texture eyeGhostUp;
	public static Texture eyeGhostDown;
	
	public static TextureRegion pill;
	
	public static PathFinding finding;

	public static TextureRegion bigPill;

	public static Animation ghostDie;
	
	
	
	public static void load() {
		items = new Texture("animation1.png");
		
		pacmanStand = new Animation<TextureRegion>(0.1f, new TextureRegion(items, 642, 3, 32, 32));
		
		pacmanMoveRight = new Animation<TextureRegion>(0.1f, new TextureRegion(items, 642, 3, 32, 32), new TextureRegion(items, 642, 43, 32, 32), new TextureRegion(items, 642, 77, 32, 32));
		pacmanMoveDown = new Animation<TextureRegion>(0.1f, new TextureRegion(items, 642, 116, 32, 32), new TextureRegion(items, 642, 156, 32, 32), new TextureRegion(items, 642, 190, 32, 32));
		pacmanMoveLeft = new Animation<TextureRegion>(0.1f, new TextureRegion(items, 642, 228, 32, 32), new TextureRegion(items, 642, 268, 32, 32), new TextureRegion(items, 642, 302, 32, 32));
		pacmanMoveUp = new Animation<TextureRegion>(0.1f, new TextureRegion(items, 642, 340, 32, 32), new TextureRegion(items, 642, 380, 32, 32), new TextureRegion(items, 642, 422, 32, 32));
		
		pacmanDie = new Animation<TextureRegion>(0.1f,new TextureRegion(items, 264, 3, 32, 32), new TextureRegion(items, 264, 43, 32, 32), new TextureRegion(items, 264, 77, 32, 32), 
						new TextureRegion(items, 264, 116, 32, 32), new TextureRegion(items, 264, 156, 32, 32), new TextureRegion(items, 264, 190, 32, 32), 
						new TextureRegion(items, 264, 228, 32, 32), new TextureRegion(items, 264, 268, 32, 32), new TextureRegion(items, 264, 302, 32, 32),
						new TextureRegion(items, 264, 340, 32, 32), new TextureRegion(items, 264, 380, 32, 32));
		
		ghostRedMoveRight = new Animation<TextureRegion>(0.1f, new TextureRegion(items, 490 , 3, 32, 32), new TextureRegion(items, 490, 43, 32, 32));
		ghostRedMoveLeft = new Animation<TextureRegion>(0.1f, new TextureRegion(items, 490 , 156, 32, 32), new TextureRegion(items, 490, 190, 32, 32));
		ghostRedMoveUp = new Animation<TextureRegion>(0.1f, new TextureRegion(items, 490 , 77, 32, 32), new TextureRegion(items, 490, 116, 32, 32));
		ghostRedMoveDown = new Animation<TextureRegion>(0.1f, new TextureRegion(items, 490 , 228, 32, 32), new TextureRegion(items, 490, 268, 32, 32));
		ghostScare = new  Animation<TextureRegion>(0.1f, new TextureRegion(items, 3 , 416, 32, 32), new TextureRegion(items, 40 , 416, 32, 32));
		ghostDie = new Animation<TextureRegion>(0.1f, new TextureRegion(items, 78 , 416, 32, 32));
		ghostRedMoveDown.setPlayMode(PlayMode.LOOP_PINGPONG);
		ghostRedMoveUp.setPlayMode(PlayMode.LOOP_PINGPONG);
		ghostRedMoveLeft.setPlayMode(PlayMode.LOOP_PINGPONG);
		ghostRedMoveRight.setPlayMode(PlayMode.LOOP_PINGPONG);
		ghostScare.setPlayMode(PlayMode.LOOP_PINGPONG);
		
		bigPill = new TextureRegion(items, 453, 76, 32, 32);
		pill = new TextureRegion(items, 305, 195, 32, 32);
		pacmanMoveRight.setPlayMode(PlayMode.LOOP_PINGPONG);
		pacmanMoveLeft.setPlayMode(PlayMode.LOOP_PINGPONG);
		pacmanMoveDown.setPlayMode(PlayMode.LOOP_PINGPONG);
		pacmanMoveUp.setPlayMode(PlayMode.LOOP_PINGPONG);		
		pacmanDie.setPlayMode(PlayMode.LOOP_PINGPONG);
		
	}
	
}
