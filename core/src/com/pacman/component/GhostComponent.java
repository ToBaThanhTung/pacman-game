package com.pacman.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.pacman.System.GhostEntity;
import com.pacman.System.GhostState;

public class GhostComponent implements Component{
	public static final int STAY = 4;
	public static final int MOVE_UP = 0;
	public static final int MOVE_DOWN = 1;
	public static final int MOVE_LEFT = 2;
	public static final int MOVE_RIGHT= 3;
	public static final int SCARE = 6;
	public static final int DIE = 5;
	
	public Body body;
	
	public int curState;
	public int hp;
	public float time;
	public float scareTime = 0;
	public GhostEntity ghostEntity;
	public boolean isScareMode;
	public boolean ghostDie;
	
	public GhostComponent(Body b) {
		body = b;
		ghostEntity = new GhostEntity(this);
		ghostEntity.state.setInitialState(GhostState.MOVE_LEFT);
		curState = STAY;
		time = 0;
		isScareMode = false;
		scareTime = 0;
		ghostDie = false;
	}
	
	public Body getBody() {
		return body;
	}
}
