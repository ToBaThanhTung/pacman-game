package com.pacman.System;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.pacman.component.MovementComponent;
import com.pacman.component.PacmanComponent;
import com.pacman.component.StateComponent;
import com.pacman.manager.Manager;

public class PacmanSystem extends IteratingSystem {
	
	private final ComponentMapper<PacmanComponent> pacM = ComponentMapper.getFor(PacmanComponent.class);
	private final ComponentMapper<MovementComponent> mvM = ComponentMapper.getFor(MovementComponent.class);
	private final ComponentMapper<StateComponent> stateM = ComponentMapper.getFor(StateComponent.class);
	
	@SuppressWarnings("unchecked")
	public PacmanSystem() {
		super(Family.all(PacmanComponent.class, MovementComponent.class, StateComponent.class).get());
		
	}



	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		PacmanComponent pacman = pacM.get(entity);
		StateComponent stateComponent = stateM.get(entity);
		MovementComponent movementComponent = mvM.get(entity);
		Body body = movementComponent.body;
		
		if(pacman.isDie == true) {
			stateComponent.setState(PacmanComponent.DIE);
			pacman.pacmanDieTime += deltaTime;
			Manager.manager.isInvi = true;                                                                                                            
		}
		if(pacman.pacmanDieTime > 1.1f) {
			pacman.isDie = false; 
			stateComponent.setState(PacmanComponent.STAY);
			pacman.body.setTransform(Manager.manager.pacmanSpawPos, 0);
			pacman.pacmanDieTime = 0;
			pacman.body.setLinearVelocity(0, 0);
		}
		
		// input
		if(Gdx.input.isKeyJustPressed(Input.Keys.D)) {
			body.setLinearVelocity(movementComponent.velocity, 0);
			stateComponent.setState(PacmanComponent.MOVE_RIGHT);
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.A)) {
			body.setLinearVelocity(-movementComponent.velocity, 0);
			stateComponent.setState(PacmanComponent.MOVE_LEFT);
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.W)) {
			body.setLinearVelocity(0, movementComponent.velocity);
			stateComponent.setState(PacmanComponent.MOVE_UP);
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.S)) {
			body.setLinearVelocity(0, -movementComponent.velocity);
			stateComponent.setState(PacmanComponent.MOVE_DOWN);
		}
		
		// invi count
		if(Manager.manager.isInvi ) {
			pacman.inviTime +=deltaTime;
			pacman.body.setActive(false);
			// set non body active
			if(pacman.inviTime >= 1.1f)
				pacman.body.setActive(true);
			if (pacman.inviTime >= 5f) {
				Manager.manager.isInvi = false;
				pacman.inviTime = 0;
				
			}
		}
	}
	
	/*private void checkMove(Body body) {
		canMove = true;
		World world = body.getWorld();
		
		RayCastCallback rayCastCallback = new RayCastCallback() {
			
			@Override
			public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
				if(fixture.getFilterData().categoryBits == Manager.wallBit) {
					canMove = false;
				}
				return 0;
			}
		};
		
		world.rayCast(rayCastCallback, , point2);
		
	}*/
	
	
}
