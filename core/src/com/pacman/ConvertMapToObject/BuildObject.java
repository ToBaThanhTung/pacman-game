package com.pacman.ConvertMapToObject;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.pacman.Asset;
import com.pacman.Sprites.Pacman;
import com.pacman.component.AnimationComponent;
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
	private Texture im = new Texture("animation1.png");
	
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
					shape.setAsBox(rectangle.getWidth() / 2 / Manager.PPM,  rectangle.getHeight() / 2 / Manager.PPM);
					fDef.shape = shape;
					body.createFixture(fDef);
					shape.dispose();
				}
				
				
				// Pill layer
				for(MapObject object : tiledMap.getLayers().get("Pill").getObjects().getByType(RectangleMapObject.class)) {
					
					Entity entity = engine.createEntity(); 
					
					Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
					checkPosition(rectangle);
					
					Body body;
					FixtureDef fixtureDef = new FixtureDef();
					CircleShape circleShape = new CircleShape();
					BodyDef bDef = new BodyDef();
					
					bDef.type = BodyType.DynamicBody;
					
					bDef.position.set((rectangle.getX() + rectangle.getWidth() / 2), (rectangle.getY() + rectangle.getHeight() / 2));
					body = world.createBody(bDef);
					circleShape.setRadius(0.2f);
					fixtureDef.isSensor = true;
					fixtureDef.shape = circleShape;
					body.createFixture(fixtureDef);
					circleShape.dispose();
					
					
					PillComponent pillComponent = engine.createComponent(PillComponent.class);
					TextureComponent textureComponent = engine.createComponent(TextureComponent.class);
					
					
					TransformComponent transformComponent = new TransformComponent((rectangle.getX() + rectangle.getWidth() / 2) , 
																	(rectangle.getY() + rectangle.getHeight() / 2) );
					
					textureComponent.region = Asset.pill;
					
					
					entity.add(pillComponent);
					entity.add(textureComponent);
					entity.add(transformComponent);
					engine.addEntity(entity);
					body.setUserData(body);
				}
				
				
				// Pacman layer
				
				for(MapObject object : tiledMap.getLayers().get("Pacman").getObjects().getByType(RectangleMapObject.class)) {
					
					Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
					
					// fix position
					checkPosition(rectangle);
					
					
					Manager.manager.pacmanSpawPos.set(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
					
					createPacman(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
					
				}
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
		shape.setRadius((float) 0.43f);
		pBody.createFixture(shape, 2.0f);
		shape.dispose();
		
		
		AnimationComponent animation = engine.createComponent(AnimationComponent.class);
		StateComponent state = new StateComponent(PacmanComponent.MOVE_RIGHT);
		PacmanComponent pacman = new PacmanComponent(pBody);
		TransformComponent transform = new TransformComponent(x, y, 1);
		
		TextureComponent texture = engine.createComponent(TextureComponent.class);
		
		MovementComponent movement = new MovementComponent(pBody);
		
		
		animation.ani.put(PacmanComponent.STAY,  Asset.pacmanStand);
		animation.ani.put(PacmanComponent.MOVE_DOWN,  Asset.pacmanMoveDown);
		animation.ani.put(PacmanComponent.MOVE_LEFT,  Asset.pacmanMoveLeft);	
		animation.ani.put(PacmanComponent.MOVE_RIGHT,  Asset.pacmanMoveRight);
		
		
		
		entity.add(pacman);
		entity.add(movement);
		entity.add(transform);
		entity.add(state);
		entity.add(texture);
		entity.add(animation);
		
		engine.addEntity(entity);
	}
	
	private void checkPosition(Rectangle rectangle) {
		rectangle.x /= Manager.PPM;
		rectangle.y /= Manager.PPM;
		rectangle.width /= Manager.PPM;
		rectangle.height /= Manager.PPM;
	}
	
}
