package com.pacman.System;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.pacman.Asset;
import com.pacman.Astar.Graph;
import com.pacman.Astar.Node;
import com.pacman.component.GhostComponent;
import com.pacman.component.MovementComponent;
import com.pacman.component.StateComponent;
import com.pacman.manager.Manager;

public class GhostSystem extends IteratingSystem {

	private final ComponentMapper<GhostComponent> ghostM = ComponentMapper.getFor(GhostComponent.class);
	private final ComponentMapper<MovementComponent> mvM = ComponentMapper.getFor(MovementComponent.class);
	private final ComponentMapper<StateComponent> stateM = ComponentMapper.getFor(StateComponent.class);

	private Vector2 curPos = new Vector2();
	private Vector2 target = new Vector2();

	@SuppressWarnings("unchecked")
	public GhostSystem() {

		super(Family.all(GhostComponent.class, MovementComponent.class, StateComponent.class).get());

	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		GhostComponent ghost = ghostM.get(entity);
		StateComponent stateComponent = stateM.get(entity);
		MovementComponent movementComponent = mvM.get(entity);
		Body body = movementComponent.body;

		
		
		ghost.ghostEntity.update(deltaTime);
		stateComponent.setState(ghost.curState);
		if(Manager.manager.isGhostScare) {
			ghost.isScareMode = true;
		}
		
		if(ghost.isScareMode) {
			//System.out.println("ghost scare state!!!!!");
			ghost.scareTime += deltaTime;
			if(ghost.scareTime >= 10f) {
				ghost.scareTime = 0;
				ghost.isScareMode = false;
			}
		}
		
		if(ghost.ghostDie) {
			ghost.time += deltaTime;
			stateComponent.setState(GhostComponent.DIE);
			ghost.body.setActive(false);
			if(ghost.time >= 0.5f) {
				ghost.ghostDie = false;
				ghost.body.setTransform(Manager.manager.ghostSpawPos, 0);
				stateComponent.setState(GhostComponent.MOVE_UP);
				body.setActive(true);
				ghost.time = 0;
			}
			
		}
		
		/*ghost.time += deltaTime;

		if (ghost.time > 0.1f) {
			curPos = ghost.getBody().getPosition();
			int targetX = MathUtils.floor(Manager.manager.pacmanLocation.x);
			int targetY = MathUtils.floor(Manager.manager.pacmanLocation.y);
			int curPosX = MathUtils.floor(ghost.getBody().getPosition().x);
			int curPosY = MathUtils.floor(ghost.getBody().getPosition().y);
			Node node;
			target.set(targetX, targetY);
			curPos.set(curPosX, curPosY);

			node = Asset.finding.findNextNode(curPos, target);

			Gdx.app.log("ghost cur pos", curPos.toString());

			if (node != null) {
				Gdx.app.log("ghost move node", node.toString());

				if ((node.x - curPos.x) * movementComponent.velocity > 0) {
					System.out.println("go R");
					body.setLinearVelocity(movementComponent.velocity, 0);
					stateComponent.setState(ghost.MOVE_RIGHT);
				}

				else if ((node.x - curPos.x) * movementComponent.velocity < 0) {
					System.out.println("go L");
					body.setLinearVelocity(-movementComponent.velocity, 0);
					stateComponent.setState(ghost.MOVE_LEFT);
				} else if ((node.y - curPos.y) * movementComponent.velocity > 0) {
					System.out.println("go Down");
					body.setLinearVelocity(0, movementComponent.velocity);
					stateComponent.setState(ghost.MOVE_UP);
				} else if ((node.y - curPos.y) * movementComponent.velocity < 0) {
					System.out.println("go Up");
					body.setLinearVelocity(0, -movementComponent.velocity);
					stateComponent.setState(ghost.MOVE_DOWN);
				}
				ghost.time = 0;
			}

		}
*/
		// just test movement
		if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {

			stateComponent.setState(ghost.MOVE_RIGHT);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
			body.setLinearVelocity(-movementComponent.velocity, 0);
			stateComponent.setState(ghost.MOVE_LEFT);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
			body.setLinearVelocity(0, movementComponent.velocity);
			stateComponent.setState(ghost.MOVE_UP);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
			body.setLinearVelocity(0, -movementComponent.velocity);
			stateComponent.setState(ghost.MOVE_DOWN);
		}
	}
	
	

}
