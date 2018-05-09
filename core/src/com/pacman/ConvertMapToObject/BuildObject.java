package com.pacman.ConvertMapToObject;


import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.pacman.Asset;
import com.pacman.Astar.Graph;
import com.pacman.Astar.PathFinding;
import com.pacman.component.AnimationComponent;
import com.pacman.component.GhostComponent;
import com.pacman.component.MovementComponent;
import com.pacman.component.PacmanComponent;
import com.pacman.component.PillComponent;
import com.pacman.component.TextureComponent;
import com.pacman.component.StateComponent;
import com.pacman.component.TransformComponent;
import com.pacman.manager.Manager;

public class BuildObject {
	
	
	private World world;
	private TiledMap tiledMap;
	private PooledEngine engine;
	public boolean wall;
	public PathFinding pathFinding;
	
	public BuildObject(TiledMap tiledMap, World world, PooledEngine engine){
		this.tiledMap = tiledMap;
        this.engine = engine;
        this.world = world;
	}
	
	
	public void build() {
		
			
				
				// Wall which ID = 1 in title map
				for(MapObject object : tiledMap.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)) {
					
					Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
					
					Body body;
					BodyDef bDef = new BodyDef();
					FixtureDef fDef = new FixtureDef();
					PolygonShape shape = new PolygonShape();
					bDef.type = BodyDef.BodyType.StaticBody;
					bDef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / Manager.PPM, (rectangle.getY() + rectangle.getHeight() / 2) / Manager.PPM);
					body = world.createBody(bDef);
					fDef.filter.categoryBits = Manager.wallBit;
					fDef.filter.maskBits = Manager.pacmanBit | Manager.ghostBit;
					shape.setAsBox(rectangle.getWidth() / 2 / Manager.PPM,  rectangle.getHeight() / 2 / Manager.PPM);
					fDef.shape = shape;
					body.createFixture(fDef);
					shape.dispose();
					//body.setUserData();
				}
				
	
				// Graph
				MapLayers mapLayer = tiledMap.getLayers();
				int mapH = ((TiledMapTileLayer) mapLayer.get(0)).getWidth();
				int mapW = ((TiledMapTileLayer) mapLayer.get(0)).getHeight();
				Graph aStarMap = new Graph(mapW, mapH);
				QueryCallback queryCallback = new QueryCallback() {
					
					@Override
					public boolean reportFixture(Fixture fixture) {
						wall = fixture.getFilterData().categoryBits == Manager.wallBit;
						return false;
					}
				};
				
				for(int y = 0; y < mapW; y++) {
					for(int x = 0; x < mapH; x++) {
						wall = false;
						world.QueryAABB(queryCallback, x + 0.2f, y + 0.2f, x + 0.8f, y + 0.8f);
						
						if(wall) {
							aStarMap.getNode(x, y).isWall = true;
						}
					}
				}
				Gdx.app.log("map", aStarMap.toString());
				Asset.finding = new PathFinding(aStarMap);
				//pathFinding = new PathFinding(aStarMap);
				/*for(int y = 0; y < mapH; y++) {
					for(int x = 0; x < mapW; x++) {
						System.out.println(aStarMap.getNode(x, y).toString());
					}
					System.out.println();
				}*/
				// Pill layer
				for(MapObject object : tiledMap.getLayers().get("Pill").getObjects().getByType(RectangleMapObject.class)) {
					
					Entity entity = engine.createEntity(); 
					
					Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
					checkPosition(rectangle);
					
					
					
					
					Body body;
					FixtureDef fixtureDef = new FixtureDef();
					CircleShape circleShape = new CircleShape();
					BodyDef bDef = new BodyDef();
					TextureComponent textureComponent = engine.createComponent(TextureComponent.class);
					PillComponent pillComponent = engine.createComponent(PillComponent.class);
					
					// 1  = big pill 
					if(object.getProperties().containsKey("1")) {
						textureComponent.region = Asset.bigPill;
						pillComponent.isBig = true;
						circleShape.setRadius(0.4f);
					}
					else {
						textureComponent.region = Asset.pill;
						pillComponent.isBig = false;
						
						circleShape.setRadius(0.2f);
					}
					
					bDef.type = BodyType.DynamicBody;
					bDef.position.set((rectangle.getX() + rectangle.getWidth() / 2), (rectangle.getY() + rectangle.getHeight() / 2));
					Manager.manager.ghostSpawPos.set((rectangle.getX() + rectangle.getWidth() / 2), (rectangle.getY() + rectangle.getHeight() / 2));
					body = world.createBody(bDef);
					fixtureDef.isSensor = true;
					fixtureDef.shape = circleShape;
					fixtureDef.filter.categoryBits = Manager.PillBit ;
					fixtureDef.filter.maskBits = Manager.pacmanBit;
					body.createFixture(fixtureDef);
					body.setUserData(entity);
					circleShape.dispose();
					
					//body.setUserData("Pill");
					MovementComponent movementComponent = new MovementComponent(body);
				
					
					
					
					TransformComponent transformComponent = new TransformComponent((rectangle.getX() + rectangle.getWidth() / 2) , 
																	(rectangle.getY() + rectangle.getHeight() / 2));
					
					
					
					System.out.println(object.getName());
					
						
					
					entity.add(movementComponent);
					entity.add(pillComponent);
					
					entity.add(textureComponent);
					entity.add(transformComponent);
					engine.addEntity(entity);
				
					
				}
				
				
				// Pacman layer
				
				for(MapObject object : tiledMap.getLayers().get("Pacman").getObjects().getByType(RectangleMapObject.class)) {
					
					Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
					
					// fix position
					checkPosition(rectangle);
					
					
					Manager.manager.pacmanSpawPos.set(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
					
					createPacman(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
					
				}
				
				// Ghost layer
				for(MapObject object : tiledMap.getLayers().get("Ghost").getObjects().getByType(RectangleMapObject.class)) {
					Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
					checkPosition(rectangle);
					Manager.manager.ghostSpawPos.set(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
					// create 4 ghost
					for(int i = 0; i < 5; i++)
					createGhost(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
				}
				
	}
	
	private void createGhost(float x, float y){
		
		Entity entity = engine.createEntity();
		Body pBody;
		
		BodyDef  def = new BodyDef();
		def.type = BodyDef.BodyType.DynamicBody;
		def.position.set(x, y);
		Manager.manager.ghostSpawPos.set(x,y);
		def.fixedRotation = true;
		
		pBody = world.createBody(def);
		CircleShape shape = new CircleShape();
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.filter.categoryBits = Manager.ghostBit;
		fixtureDef.filter.maskBits = Manager.pacmanBit | Manager.wallBit;
		shape.setRadius(0.4f);
		fixtureDef.shape = shape;
		
		pBody.createFixture(fixtureDef);
		shape.dispose();
		
		
		
		AnimationComponent animation = engine.createComponent(AnimationComponent.class);
		StateComponent state = new StateComponent(GhostComponent.MOVE_DOWN);
		GhostComponent ghost = new GhostComponent(pBody);
		TransformComponent transform = new TransformComponent(x, y, 0);
		
		TextureComponent texture = engine.createComponent(TextureComponent.class);
		
		MovementComponent movement = new MovementComponent(pBody);
		
		
		animation.ani.put(GhostComponent.MOVE_LEFT,  Asset.ghostRedMoveLeft);
		animation.ani.put(GhostComponent.MOVE_RIGHT,  Asset.ghostRedMoveRight);
		animation.ani.put(GhostComponent.MOVE_UP,  Asset.ghostRedMoveUp);
		animation.ani.put(GhostComponent.MOVE_DOWN,  Asset.ghostRedMoveDown);
		animation.ani.put(GhostComponent.SCARE, Asset.ghostScare);
		animation.ani.put(GhostComponent.DIE, Asset.ghostDie);
		entity.add(ghost);
		entity.add(movement);
		entity.add(transform);
		entity.add(state);
		entity.add(texture);
		entity.add(animation);
		
		engine.addEntity(entity);
		pBody.setUserData(entity);
		
	}
	
	
	private void createPacman(float x, float y) {
		
		Entity entity = engine.createEntity();
		
		Body pBody;
		
		BodyDef  def = new BodyDef();
		def.type = BodyDef.BodyType.DynamicBody;
		def.position.set(x, y);
		def.fixedRotation = true;
		
		pBody = world.createBody(def);
		CircleShape shape = new CircleShape();
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.filter.categoryBits = Manager.pacmanBit;
		fixtureDef.filter.maskBits = Manager.PillBit | Manager.wallBit | Manager.ghostBit;
		shape.setRadius(0.43f);
		fixtureDef.shape = shape;
		
		pBody.createFixture(fixtureDef);
		shape.dispose();
		
		
		
		AnimationComponent animation = engine.createComponent(AnimationComponent.class);
		
		PacmanComponent pacman = new PacmanComponent(pBody);
		StateComponent state = new StateComponent(pacman.curState);
		TransformComponent transform = new TransformComponent(x, y, 1);
		
		TextureComponent texture = engine.createComponent(TextureComponent.class);
		
		MovementComponent movement = new MovementComponent(pBody);
		Manager.manager.pacmanLocation = pacman.getBody().getPosition();
		
		animation.ani.put(PacmanComponent.DIE,  Asset.pacmanDie);
		animation.ani.put(PacmanComponent.MOVE_UP,  Asset.pacmanMoveUp);
		animation.ani.put(PacmanComponent.MOVE_DOWN,  Asset.pacmanMoveDown);
		animation.ani.put(PacmanComponent.MOVE_LEFT,  Asset.pacmanMoveLeft);	
		animation.ani.put(PacmanComponent.MOVE_RIGHT,  Asset.pacmanMoveRight);
		animation.ani.put(PacmanComponent.STAY,  Asset.pacmanStand);
	
		
		entity.add(pacman);
		entity.add(movement);
		entity.add(transform);
		entity.add(state);
		entity.add(texture);
		entity.add(animation);
		
		engine.addEntity(entity);
		pBody.setUserData(entity);
	}
	
	private void checkPosition(Rectangle rectangle) {
		rectangle.x /= Manager.PPM;
		rectangle.y /= Manager.PPM;
		rectangle.width /= Manager.PPM;
		rectangle.height /= Manager.PPM;
	}
	
}
