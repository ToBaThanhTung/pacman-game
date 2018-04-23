package com.pacman.System;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.pacman.component.MovementComponent;
import com.pacman.component.PacmanComponent;
import com.pacman.component.StateComponent;

public class PacmanSystem extends IteratingSystem{
	
	private final ComponentMapper<PacmanComponent> pacM = ComponentMapper.getFor(PacmanComponent.class);
	private final ComponentMapper<MovementComponent> mvM = ComponentMapper.getFor(MovementComponent.class);
	private final ComponentMapper<StateComponent> stateM = ComponentMapper.getFor(StateComponent.class);
	
	private final Vector2 velocity = new Vector2(); 
	
	public PacmanSystem() {
		super(Family.all(PacmanComponent.class, MovementComponent.class, StateComponent.class).get());
		
	}



	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		PacmanComponent pacman = pacM.get(entity);
		StateComponent stateComponent = stateM.get(entity);
		MovementComponent movementComponent = mvM.get(entity);
		Body body = movementComponent.body;
		
		// input
		if(Gdx.input.isKeyJustPressed(Input.Keys.D)) {
			body.setLinearVelocity(movementComponent.velocity, 0);
			stateComponent.setState(pacman.MOVE_RIGHT);
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.A)) {
			body.setLinearVelocity(-movementComponent.velocity, 0);
			stateComponent.setState(pacman.MOVE_LEFT);
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.W)) {
			body.setLinearVelocity(0, movementComponent.velocity);
			stateComponent.setState(pacman.MOVE_UP);
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.S)) {
			body.setLinearVelocity(0, -movementComponent.velocity);
			stateComponent.setState(pacman.MOVE_DOWN);
		}
	}
	
	
}
