package com.pacman.System;

import java.sql.Time;

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
import com.pacman.Astar.Graph;
import com.pacman.Astar.Node;
import com.pacman.component.GhostComponent;
import com.pacman.manager.Manager;

public enum GhostState implements State<GhostEntity> {
	MOVE_UP() {
		@Override
		public void update(GhostEntity entity) {
			entity.ghostComponent.curState = GhostComponent.MOVE_UP;
			Body tempBody = entity.ghostComponent.getBody();
			if(canMove(entity))
				tempBody.setLinearVelocity(0, entity.velocity);

			if (isHitWall(entity, GhostComponent.MOVE_UP)) {
				System.out.println("hit wall");
				changeDirectionState(entity, GhostComponent.MOVE_UP);
			}
			
			int random = MathUtils.random(20);
			if(random == 17) {
				changeDirectionState(entity, entity.ghostComponent.curState);
			}
			
			if (nearPlayer(entity, 6f) && !Manager.manager.isInvi) {
				if (entity.ghostComponent.isScareMode)
					entity.state.changeState(GhostState.SCARE);
				else
					entity.state.changeState(GhostState.HUNTER);
			}
			if (entity.ghostComponent.isScareMode) {
				entity.ghostComponent.curState = GhostComponent.SCARE;
				
			}
		}
	},
	MOVE_DOWN() {
		@Override
		public void update(GhostEntity entity) {
			entity.ghostComponent.curState = GhostComponent.MOVE_DOWN;
			Body tempBody = entity.ghostComponent.getBody();
			
			if(canMove(entity))
				tempBody.setLinearVelocity(0, -entity.velocity);
			if (isHitWall(entity, GhostComponent.MOVE_DOWN)) {
				System.out.println("hit wall");
				changeDirectionState(entity, GhostComponent.MOVE_DOWN);
			}
			if (nearPlayer(entity, 6f) && !Manager.manager.isInvi) {
				if (entity.ghostComponent.isScareMode)
					entity.state.changeState(GhostState.SCARE);
				else
					entity.state.changeState(GhostState.HUNTER);
			}
			if (entity.ghostComponent.isScareMode) {
				entity.ghostComponent.curState = GhostComponent.SCARE;
				
			}
			int random = MathUtils.random(50);
			if(random == 7) {
				changeDirectionState(entity, entity.ghostComponent.curState);
			}
		}
	},
	MOVE_LEFT() {
		@Override
		public void update(GhostEntity entity) {
			entity.ghostComponent.curState = GhostComponent.MOVE_LEFT;
			Body tempBody = entity.ghostComponent.getBody();
			int random = MathUtils.random(50);
		
			if(canMove(entity))
				tempBody.setLinearVelocity(-entity.velocity, 0);
			// System.out.println(tempBody.getWorldCenter());
			if (isHitWall(entity, GhostComponent.MOVE_LEFT)) {
				System.out.println("hit wall");
				changeDirectionState(entity, GhostComponent.MOVE_LEFT);
			}
			if (nearPlayer(entity, 6f) && !Manager.manager.isInvi) {
				if (entity.ghostComponent.isScareMode)
					entity.state.changeState(GhostState.SCARE);
				else
					entity.state.changeState(GhostState.HUNTER);
			}
			if (entity.ghostComponent.isScareMode) {
				entity.ghostComponent.curState = GhostComponent.SCARE;
				
			}
			if(random == 6) {
				changeDirectionState(entity, entity.ghostComponent.curState);
			}
		}
	},
	MOVE_RIGHT() {
		@Override
		public void update(GhostEntity entity) {
			entity.ghostComponent.curState = GhostComponent.MOVE_RIGHT;
			Body tempBody = entity.ghostComponent.getBody();
		
			if(canMove(entity))	
				tempBody.setLinearVelocity(entity.velocity, 0);
			if (isHitWall(entity, GhostComponent.MOVE_RIGHT)) {
				System.out.println("hit wall");
				changeDirectionState(entity, GhostComponent.MOVE_RIGHT);
			}
			if (nearPlayer(entity, 6f) && !Manager.manager.isInvi) {
				if (entity.ghostComponent.isScareMode)
					entity.state.changeState(GhostState.SCARE);
				else
					entity.state.changeState(GhostState.HUNTER);
			}
			if (entity.ghostComponent.isScareMode) {
				entity.ghostComponent.curState = GhostComponent.SCARE;
				
			}
			int random = MathUtils.random(50);
			if(random == 9) {
				changeDirectionState(entity, entity.ghostComponent.curState);
			}
			

		}
	},
	HUNTER() {
		@Override
		public void update(GhostEntity entity) {
			// System.out.println("hunter mod");

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

					if (((node.x - curPos.x) * entity.velocity > 0) && canMove(entity)) {
						System.out.println("go R");
						entity.ghostComponent.getBody().setLinearVelocity(entity.velocity, 0);
						entity.ghostComponent.curState = GhostComponent.MOVE_RIGHT;

					}

					else if (((node.x - curPos.x) * entity.velocity < 0) && canMove(entity)) {
						System.out.println("go L");
						entity.ghostComponent.getBody().setLinearVelocity(-entity.velocity, 0);
						entity.ghostComponent.curState = GhostComponent.MOVE_LEFT;

					} else if (((node.y - curPos.y) * entity.velocity > 0) && canMove(entity)) {
						System.out.println("go Down");
						entity.ghostComponent.getBody().setLinearVelocity(0, entity.velocity);
						entity.ghostComponent.curState = GhostComponent.MOVE_DOWN;

					} else if (((node.y - curPos.y) * entity.velocity < 0) && canMove(entity)) {
						System.out.println("go Up");
						entity.ghostComponent.getBody().setLinearVelocity(0, -entity.velocity);
						entity.ghostComponent.curState = GhostComponent.MOVE_UP;

					}

				}
				entity.time = 0;

			}
			if (!nearPlayer(entity, 6f)) {
				changeDirectionState(entity, 5);
				return;
			}
			if (entity.ghostComponent.isScareMode) {
				entity.state.changeState(GhostState.SCARE);
			}
		}
	},
	SCARE() {
		@Override
		public void update(GhostEntity entity) {
			System.out.println("SCARE MOD!");
			entity.ghostComponent.curState = GhostComponent.SCARE;
			Vector2 curPos = new Vector2();
			Vector2 target = new Vector2();

			if (entity.time > 0.2f && nearPlayer(entity, 6f)) {
				curPos = entity.getPos();

				int curPosX = MathUtils.floor(entity.getPos().x);
				int curPosY = MathUtils.floor(entity.getPos().y);
				Node node;

				curPos.set(curPosX, curPosY);
				Gdx.app.log("cur ghost", curPos.toString());
				Graph map = Asset.finding.map;

				float x = (Manager.manager.pacmanLocation.x + map.getWidth() / 2);
				float y = (Manager.manager.pacmanLocation.y + map.getHeight() / 2);

				do {
					x += 1;
					y += 1;
					x = x > map.getWidth() ? x - map.getWidth() : x;
					y = y > map.getHeight() ? y - map.getHeight() : y;
				} while (map.getNode(MathUtils.floor(x), MathUtils.floor(y)).isWall);

				target.set(MathUtils.floor(x), MathUtils.floor(y));

				node = Asset.finding.findNextNode(curPos, target);

				Gdx.app.log("ghost target", target.toString());

				if (node != null) {
					Gdx.app.log("ghost move node", node.toString());

					if (((node.x - curPos.x) * entity.velocity > 0) && canMove(entity)) {
						System.out.println("go R");
						entity.ghostComponent.getBody().setLinearVelocity(entity.velocity, 0);	
					}
					else if (((node.x - curPos.x) * entity.velocity < 0) && canMove(entity)) {
						System.out.println("go L");
						entity.ghostComponent.getBody().setLinearVelocity(-entity.velocity, 0);
					
					} else if (((node.y - curPos.y) * entity.velocity > 0) && canMove(entity)) {
						System.out.println("go Down");
						entity.ghostComponent.getBody().setLinearVelocity(0, entity.velocity);
					
					} else if (((node.y - curPos.y) * entity.velocity < 0) && canMove(entity)) {
						System.out.println("go Up");
						entity.ghostComponent.getBody().setLinearVelocity(0, -entity.velocity);
						
						
					}

				}
				entity.time = 0;

			}
			if(!nearPlayer(entity, 6f)) {
				changeDirectionState(entity, MathUtils.floor(3));
			}
			

			

			if(!entity.ghostComponent.isScareMode) {
				changeDirectionState(entity, MathUtils.floor(3));
			}
		}

	},
	SCARE_UP(){

		@Override
		public void update(GhostEntity entity) {
			entity.ghostComponent.curState = GhostComponent.SCARE;
			entity.ghostComponent.body.setLinearVelocity(0, entity.velocity);
			if(nearPlayer(entity, 6f)) {
				entity.state.changeState(SCARE);
			}
			if(!entity.ghostComponent.isScareMode) {
				changeDirectionState(entity, MathUtils.floor(3));
			}
		}
		
	},
	SCARE_DOWN(){

		@Override
		public void update(GhostEntity entity) {
			entity.ghostComponent.curState = GhostComponent.SCARE;
			entity.ghostComponent.body.setLinearVelocity(0, -entity.velocity);
			if(nearPlayer(entity, 6f)) {
				entity.state.changeState(SCARE);
			}
			if(!entity.ghostComponent.isScareMode) {
				changeDirectionState(entity, MathUtils.floor(3));
			}
		}
		
	},
	SCARE_LEFT(){

		@Override
		public void update(GhostEntity entity) {
			entity.ghostComponent.curState = GhostComponent.SCARE;
			entity.ghostComponent.body.setLinearVelocity(-entity.velocity, 0);
			if(nearPlayer(entity, 6f)) {
				entity.state.changeState(SCARE);
			}
			if(!entity.ghostComponent.isScareMode) {
				changeDirectionState(entity, MathUtils.floor(3));
			}
		}
		
	},
	SCARE_RIGHT(){

		@Override
		public void update(GhostEntity entity) {
			entity.ghostComponent.curState = GhostComponent.SCARE;
			entity.ghostComponent.body.setLinearVelocity(entity.velocity, 0);
			if(nearPlayer(entity, 6f)) {
				entity.state.changeState(SCARE);
			}
			if(!entity.ghostComponent.isScareMode) {
				changeDirectionState(entity, MathUtils.floor(3));
			}
		}
		
	};

	private boolean isWall;
	private Vector2 V1 = new Vector2();
	private Vector2 V2 = new Vector2();
	private java.util.Random random;

	protected boolean nearPlayer(GhostEntity entity, float distance) {
		if (Manager.manager.pacmanLocation == null) {
			return false;
		}
		Vector2 ghostCurPos = entity.getPos();
		Vector2 playerPos = Manager.manager.pacmanLocation;

		return ghostCurPos.dst2(playerPos) < distance * distance;
	}

	// private
	protected int randDirection(int preDirection) {
		random = new java.util.Random();
		int rand = random.nextInt(4);
		while (rand == preDirection) {
			rand = random.nextInt(4);
		}
		return rand;
	}
	protected boolean canMove(GhostEntity entity) {
		float x = entity.getPos().x;
		float y = entity.getPos().y;
		
		float xMinimun = MathUtils.floor(x) + 0.4f;
		float xMax = MathUtils.floor(x) + 0.6f;
		float yMinimun = MathUtils.floor(y) + 0.4f;
		float yMax = MathUtils.floor(y) + 0.6f;
		return (x > xMinimun && x < xMax && y > yMinimun && y < yMax);
		
	}
	
	protected void changeDirection(GhostEntity entity, int state) {

		int newState = randDirection(state);
		switch (newState) {
		case GhostComponent.MOVE_UP:
			entity.state.changeState(GhostState.SCARE_UP);
			break;
		case GhostComponent.MOVE_DOWN:
			entity.state.changeState(GhostState.SCARE_DOWN);
			break;
		case GhostComponent.MOVE_LEFT:
			entity.state.changeState(GhostState.SCARE_LEFT);
			break;
		case GhostComponent.MOVE_RIGHT:
			entity.state.changeState(GhostState.SCARE_RIGHT);
			break;
		default:
			break;
		}
	}

	protected void changeDirectionState(GhostEntity entity, int state) {
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
		// entity.state.changeState(GhostState.MOVE_UP);

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
			if (fixture.getFilterData().categoryBits == Manager.wallBit) {
				isWall = true;
				return 0;
			}
			return 0;
		}
	};

	@Override
	public void enter(GhostEntity entity) {
		// entity.ghostComponent.getBody().setLinearVelocity(0, 0);

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
