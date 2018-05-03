package com.pacman.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;

public class MovementComponent implements Component {
	public float velocity = 3.5f;
	public Body body;
	
	public MovementComponent(Body b) {
		body = b;
	}
}

