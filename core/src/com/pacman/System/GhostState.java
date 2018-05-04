package com.pacman.System;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.pacman.Asset;
import com.pacman.Astar.Node;
import com.pacman.component.GhostComponent;
import com.pacman.manager.Manager;

public enum GhostState implements State<GhostEntity>{
	MOVE_UP(){
		@Override
		public void update(GhostEntity entity) {
			entity.ghostComponent.curState = GhostComponent.MOVE_UP;
			Body tempBody = entity.ghostComponent.getBody();
			tempBody.setLinearVelocity(0, entity.velocity);
			
			if(isHitWall(entity, GhostComponent.MOVE_UP)) {
				System.out.println("hit wall");
				changeDirection(entity, GhostComponent.MOVE_UP);
			}
			if(nearPlayer(entity, 6f)) {
				entity.state.changeState(GhostState.HUNTER);
			}
		}		
	},
	MOVE_DOWN(){
		@Override
		public void update(GhostEntity entity) {
			entity.ghostComponent.curState = GhostComponent.MOVE_DOWN;
			Body tempBody = entity.ghostComponent.getBody();
			tempBody.setLinearVelocity(0, -entity.velocity);
			if(isHitWall(entity, GhostComponent.MOVE_DOWN)) {
				System.out.println("hit wall");
				changeDirection(entity, GhostComponent.MOVE_DOWN);
			}
			if(nearPlayer(entity, 6f)) {
				entity.state.changeState(GhostState.HUNTER);
			}
		}		
	},
	MOVE_LEFT(){
		@Override
		public void update(GhostEntity entity) {
			entity.ghostComponent.curState = GhostComponent.MOVE_LEFT;
			Body tempBody = entity.ghostComponent.getBody();
			tempBody.setLinearVelocity(-entity.velocity, 0);
			//System.out.println(tempBody.getWorldCenter());
			if(isHitWall(entity, GhostComponent.MOVE_LEFT)) {
				System.out.println("hit wall");
				changeDirection(entity, GhostComponent.MOVE_LEFT);
			}
			if(nearPlayer(entity, 6f)) {
				entity.state.changeState(GhostState.HUNTER);
			}
		}		
	},
	MOVE_RIGHT(){
		@Override
		public void update(GhostEntity entity) {
			entity.ghostComponent.curState = GhostComponent.MOVE_RIGHT;
			Body tempBody = entity.ghostComponent.getBody();
			tempBody.setLinearVelocity(entity.velocity, 0);
			if(isHitWall(entity, GhostComponent.MOVE_RIGHT)) {
				System.out.println("hit wall");
				changeDirection(entity, GhostComponent.MOVE_RIGHT);
			}
			if(nearPlayer(entity, 6f)) {
				entity.state.changeState(GhostState.HUNTER);
			}
			
		}		
	},
	HUNTER(){
		@Override
		public void update(GhostEntity entity) {
			System.out.println("hunter mod");
			
			
			Vector2 curPos = new Vector2();
			Vector2 target = new Vector2();

			if (entity.time > 0.1f) {
				curPos = entity.getPos();
				int targetX = MathUtils.floor(Manager.manager.pacmanLocation.x);
				int targetY = MathUtils.floor(Manager.manager.pacmanLocation.y);
				int curPosX = MathUtils.floor(entity.getPos().x);
				int curPosY = MathUtils.floor(entity.getPos().y);
				Node node;
				target.set(targetX, targetY);
				curPos.set(curPosX, curPosY);

				node = Asset.finding.findNextNode(curPos, target);

				Gdx.app.log("ghost cur pos", curPos.toString());

				if (node != null) {
					Gdx.app.log("ghost move node", node.toString());

					if ((node.x - curPos.x) * entity.velocity > 0) {
						System.out.println("go R");
						entity.ghostComponent.getBody().setLinearVelocity(entity.velocity, 0);
						entity.ghostComponent.curState = GhostComponent.MOVE_RIGHT;
					}

					else if ((node.x - curPos.x) * entity.velocity < 0) {
						System.out.println("go L");
						entity.ghostComponent.getBody().setLinearVelocity(-entity.velocity, 0);
						entity.ghostComponent.curState = GhostComponent.MOVE_LEFT;
					} else if ((node.y - curPos.y) * entity.velocity > 0) {
						System.out.println("go Down");
						entity.ghostComponent.getBody().setLinearVelocity(0, entity.velocity);
						entity.ghostComponent.curState = GhostComponent.MOVE_DOWN;
					} else if ((node.y - curPos.y) * entity.velocity < 0) {
						System.out.println("go Up");
						entity.ghostComponent.getBody().setLinearVelocity(0, -entity.velocity);
						entity.ghostComponent.curState = GhostComponent.MOVE_UP;
					}
					entity.time = 0;
				}
				
				if(!nearPlayer(entity, 6f)) {
					changeDirection(entity, 0);
				}
			}	
		}
	};
	
	private boolean isWall;
	private Vector2 V1 = new Vector2();
	private Vector2 V2 = new Vector2();
	private java.util.Random random ;
	
	 protected boolean nearPlayer(GhostEntity entity, float distance) {
	        if (Manager.manager.pacmanLocation == null) {
	            return false;
	        }
	        Vector2 ghostCurPos = entity.getPos();
	        Vector2 playerPos = Manager.manager.pacmanLocation;

	        return ghostCurPos.dst2(playerPos) < distance * distance;
	    }
	//private 
	protected int randDirection(int preDirection) {
		random = new java.util.Random();
		int rand = random.nextInt(4);
		//System.out.println("without: " + rand);
		while(rand == preDirection) {
			rand = random.nextInt(4);
		}
		return rand;
	}
	
	
	
	protected void changeDirection(GhostEntity entity, int state) {
		System.out.println(randDirection(state));
		int newState = randDirection(state);
		switch (newState) {
		case GhostComponent.MOVE_UP:
			entity.state.changeState(GhostState.MOVE_UP);
			break;
		case GhostComponent.MOVE_DOWN:
			entity.state.changeState(GhostState.MOVE_DOWN);
			break;
		case GhostComponent.MOVE_LEFT:
			entity.state.changeState(GhostState.MOVE_LEFT);
			break;
		case GhostComponent.MOVE_RIGHT:
			entity.state.changeState(GhostState.MOVE_RIGHT);
			break;
		default:
			break;
		}
		//entity.state.changeState(GhostState.MOVE_UP);
		
	}
	
	
	protected boolean isHitWall(GhostEntity entity, int state) {
		isWall = false;
		Body body = entity.ghostComponent.getBody();
		World world = body.getWorld();
		V1 = body.getWorldCenter();
		switch (state) {
		case GhostComponent.MOVE_UP:
			V2.set(V1).add(0, 0.55f);
			break;
		case GhostComponent.MOVE_DOWN:
			V2.set(V1).add(0, -0.55f);
			break;
		case GhostComponent.MOVE_LEFT:
			V2.set(V1).add(-0.55f, 0);
			break;
		case GhostComponent.MOVE_RIGHT:
			V2.set(V1).add(0.55f, 0);
			break;
		default:
			break;
		}
		world.rayCast(raycast, V1, V2);
		
		return isWall;
	}
	
	protected RayCastCallback raycast = new RayCastCallback() {
		
		@Override
		public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
			if(fixture.getFilterData().categoryBits == Manager.wallBit) {
				isWall = true;
				return 0;
			}
			return 0;
		}
	};
	
	@Override
	public void enter(GhostEntity entity) {
		entity.ghostComponent.getBody().setLinearVelocity(0, 0);
		
	}
	@Override
	public void exit(GhostEntity entity) {
		entity.node = null;
	}
	@Override
	public boolean onMessage(GhostEntity entity, Telegram telegram) {
		return false;
	}

}

