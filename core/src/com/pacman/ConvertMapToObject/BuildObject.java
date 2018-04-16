package com.pacman.ConvertMapToObject;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
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
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.pacman.component.AnimationComponent;
import com.pacman.component.MovementComponent;
import com.pacman.component.PacmanComponent;
import com.pacman.component.TextureComponent;
import com.pacman.component.TimeComponent;
import com.pacman.component.TransformComponent;
import com.pacman.manager.Manager;

public class BuildObject {
	
	// for physic world
	
	private World world;
	private TiledMap tiledMap;
	private Engine engine;
	
	
	// for animation
	private Animation<TextureRegion> PacmanMoveLeft;
	private Texture animationSheet;
	private TextureRegion[][] tmp;
	
	public BuildObject(TiledMap tiledMap, World world, Engine engine){
		this.tiledMap = tiledMap;
        this.engine = engine;
        this.world = world;
	}
	
	
	public void build() {
		// load animation sheet as Texture
				animationSheet = new Texture("animation.png");
				
				// temp 
				tmp = TextureRegion.split(animationSheet, animationSheet.getWidth() / 19, animationSheet.getHeight() / 20);
				
				
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
				
				
				// Pacman layer
				
				for(MapObject object : tiledMap.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
					
					Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
					
					// fix position
					checkPosition(rectangle);
					
					
					Manager.manager.pacmanSpawPos.set(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
					
					createPacman(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
					
				}
	}
	
	private void createPacman(float x, float y) {
		
		// create physic body for pacman  (Dynamic, radius = 0.45)
		Body pBody;
		
		BodyDef  def = new BodyDef();
		def.type = BodyDef.BodyType.DynamicBody;
		def.position.set(x, y);
		def.fixedRotation = true;
		pBody = world.createBody(def);
		CircleShape shape = new CircleShape();
		shape.setRadius((float) 0.45f);
		pBody.createFixture(shape, 2.0f);
		shape.dispose();
		
		
		
		
		// animation
		AnimationComponent animationComponent = new AnimationComponent();
		Array<TextureRegion> frames = new Array<TextureRegion>();
		Animation animation;
		
		
		//
		PacmanComponent pacman = new PacmanComponent(pBody);
		Entity entity = new Entity();
		entity.add(pacman);
		entity.add(new TransformComponent(x, y, 1));
		entity.add(new MovementComponent(pBody));
		entity.add(new TimeComponent(PacmanComponent.STAY));
		entity.add(new TextureComponent(tmp[0][18]));
		
		// when stand 
		frames.add(tmp[0][18]);
		animation = new Animation(0.1f, frames);
		animationComponent.ani.put(PacmanComponent.STAY, animation);
		frames.clear();
		
		// when stand right
		frames.add(tmp[1][16]);
		animation = new Animation(0.1f, frames);
		animationComponent.ani.put(PacmanComponent.STAY_RIGHT, animation);
		frames.clear();	
		
		// when stand down
		frames.add(tmp[4][16]);
		animation = new Animation(0.1f, frames);
		animationComponent.ani.put(PacmanComponent.STAY_LEFT, animation);
		frames.clear();		
		

		// when stand up
		frames.add(tmp[10][16]);
		animation = new Animation(0.1f, frames);
		animationComponent.ani.put(PacmanComponent.STAY_UP, animation);
		frames.clear();		
		

		// when stand left
		frames.add(tmp[7][16]);
		animation = new Animation(0.1f, frames);
		animationComponent.ani.put(PacmanComponent.STAY_LEFT, animation);
		frames.clear();		
		
		// when move right
		for(int i = 0; i < 3; i++) {
			frames.add(tmp[i][16]);
		}
		animation = new Animation(0.1f, frames);
		animationComponent.ani.put(PacmanComponent.MOVE_RIGHT, animation);
		frames.clear();

		// when move down
		for(int i = 3; i < 6; i++) {
			frames.add(tmp[i][16]);
		}
		animation = new Animation(0.1f, frames);
		animationComponent.ani.put(PacmanComponent.MOVE_DOWN, animation);
		frames.clear();
		
		
		// when move left
		for(int i = 6; i < 9; i++) {
			frames.add(tmp[i][16]);
		}
		animation = new Animation(0.1f, frames);
		animationComponent.ani.put(PacmanComponent.MOVE_LEFT, animation);
		frames.clear();
		
		
		// when move up
		for(int i = 9; i < 12; i++) {
			frames.add(tmp[i][16]);
		}
		animation = new Animation(0.1f, frames);
		animationComponent.ani.put(PacmanComponent.MOVE_UP, animation);
		frames.clear();
		
		
		entity.add(animationComponent);
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
