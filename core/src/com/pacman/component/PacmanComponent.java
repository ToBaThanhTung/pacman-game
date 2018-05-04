package com.pacman.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;

public class PacmanComponent implements Component{
	public static final int STAY = 0;
	public static final int MOVE_UP = 1;
	public static final int MOVE_DOWN = 2;
	public static final int MOVE_LEFT = 3;
	public static final int MOVE_RIGHT= 4;
	public static final int DIE = 5;
	
	public Body body;
	public float pacmanDieTime;
	public int curState;
	public int hp;
	public boolean isDie;
	public float inviTime;
	public PacmanComponent(Body b) {
		body = b;
		curState = STAY;
		isDie = false;
		pacmanDieTime = 0;
		inviTime = 0;
	}
	
	
	public Body getBody() {
		return body;
	}
}
