package com.pacman.System;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.pacman.component.GhostComponent;
import com.pacman.component.MovementComponent;
import com.pacman.component.StateComponent;

public class GhostSystem extends IteratingSystem{


	private final ComponentMapper<GhostComponent> ghostM = ComponentMapper.getFor(GhostComponent.class);
	private final ComponentMapper<MovementComponent> mvM = ComponentMapper.getFor(MovementComponent.class);
	private final ComponentMapper<StateComponent> stateM = ComponentMapper.getFor(StateComponent.class);
	
	private final Vector2 velocity = new Vector2(); 
	public GhostSystem() {
		super(Family.all(GhostComponent.class, MovementComponent.class, StateComponent.class).get());
	}
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		GhostComponent ghost = ghostM.get(entity);
		StateComponent stateComponent = stateM.get(entity);
		MovementComponent movementComponent = mvM.get(entity);
		Body body = movementComponent.body;
		if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
			body.setLinearVelocity(movementComponent.velocity, 0);
			stateComponent.setState(ghost.MOVE_RIGHT);
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
			body.setLinearVelocity(-movementComponent.velocity, 0);
			stateComponent.setState(ghost.MOVE_LEFT);
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
			body.setLinearVelocity(0, movementComponent.velocity);
			stateComponent.setState(ghost.MOVE_UP);
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
			body.setLinearVelocity(0, -movementComponent.velocity);
			stateComponent.setState(ghost.MOVE_DOWN);
		}
	}

}
