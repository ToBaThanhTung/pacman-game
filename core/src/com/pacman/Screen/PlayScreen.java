package com.pacman.Screen;


import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.profiling.GL20Interceptor;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntIntMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pacman.PacMan;
import com.pacman.ConvertMapToObject.BuildObject;

public class PlayScreen implements Screen{
	
	private final float WIDTH = 35.0f;
	private final float HEIGHT = 15.0f;
	
	private PacMan game;
	private SpriteBatch batch;
	private Engine engine;
	private OrthographicCamera camera;
	private TiledMap tiledMap;
	private OrthoCachedTiledMapRenderer tiledMapRenderer;
	
	private FitViewport viewport;
	
	
	// just for demo
	
	private TextureAtlas atlas; 
	private Texture pacmanTexture;
	private Vector2 position;
	private float velocity = 1f;
	private MapObjects wallCollisionObj;
	private Array<Rectangle> collisionRect ;
	private World world;
	private Body player;
	private Box2DDebugRenderer b2Renderer;
	private Animation<TextureRegion> PacmanMoveLeft;
	private Texture animationSheet;
	private float stateTime;
	public PlayScreen(PacMan game) {
		this.game = game;
		this.batch = game.batch;
	}

	@Override
	public void show() {
		camera = new OrthographicCamera();
		viewport = new FitViewport(WIDTH, HEIGHT, camera);
		camera.translate(WIDTH / 2, HEIGHT / 2);
		//camera.setToOrtho(false, WIDTH / 2f, HEIGHT / 2f);
		camera.update();
		batch = new SpriteBatch();
		
		//
		
		
		engine = new Engine();
		// load map
		tiledMap = new TmxMapLoader().load("map/map2.tmx");
		// load the map, set the unit scale to 1/16 (1 unit == 16 pixels)
		tiledMapRenderer = new OrthoCachedTiledMapRenderer(tiledMap, 1/32f);
		world = new World(new Vector2(0, 0), true);
		new BuildObject(tiledMap, world, engine).build();
		
	}

	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.1f, 0.2f, 0.2f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		world.step(1/60f, 8, 3);
		//update(delta);
		engine.update(delta);
		
		// test animation
		
		
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();
		
		// render the world of physic
		//b2Renderer.render(world, camera.combined);
		stateTime += Gdx.graphics.getDeltaTime();
	
		
	}
	
	void update(float deltatime) {
		


		
	//	System.out.println(Gdx.input.getX() + " " + Gdx.input.getY());
		System.out.println(position);
		if(deltatime == 0) return;
		if(deltatime > 1f) deltatime = 1f;
		
		// check input
			
			int hozForce = 0;
			int verForce = 0;
			if(Gdx.input.isKeyPressed(Keys.D) ) {
				position.x += velocity;
				hozForce += 1;
				
			}
			if(Gdx.input.isKeyPressed(Keys.A)) {
				position.x -= velocity;
				hozForce -= 1;
			}
			if(Gdx.input.isKeyPressed(Keys.W)) {
				position.y += velocity;
				verForce += 1;
			}
			if(Gdx.input.isKeyPressed(Keys.S)) {
				position.y -= velocity;
				verForce -= 1;
			}
			
			player.setLinearVelocity(hozForce * 3, verForce * 3);
			System.out.println("hoz force: " + hozForce);
			System.out.println(player.getPosition());
	}
	
	public void createAnimation() {
		
	}
	
	
	public Body createBox(int x, int y, int w, int h, boolean isStatic) {
		Body pBody;
		BodyDef  def = new BodyDef();
		if(isStatic)
			
			def.type = BodyDef.BodyType.StaticBody;
		else 
			def.type = BodyDef.BodyType.DynamicBody;
		
		def.position.set(x / 16f, y / 16f);
		def.fixedRotation = true;
		pBody = world.createBody(def);
		CircleShape shape = new CircleShape();
		shape.setRadius((float) 0.4);
		pBody.createFixture(shape, 2.0f);
		return pBody;
	}
	
	
	
	public void B2dWorldCreator() {
		BodyDef bDef = new BodyDef();
		PolygonShape shape = new PolygonShape();
		FixtureDef fDef = new FixtureDef();
		Body body;
		CircleShape circleShape = new CircleShape();
		
		// where 2 is the id of object in tmx file which is "Wall"
		for(MapObject object : tiledMap.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
			
			bDef.type = BodyDef.BodyType.StaticBody;
			bDef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / 32f, (rectangle.getY() + rectangle.getHeight() / 2) / 32f);
			body = world.createBody(bDef);
			shape.setAsBox(rectangle.getWidth() / 2 / 32f,  rectangle.getHeight() / 2 / 32f);
			fDef.shape = shape;
			body.createFixture(fDef);
		}
		
		  // pills
       /* MapLayer pillLayer = tiledMap.getLayers().get("Pill"); // pill layer
        for (MapObject mapObject : pillLayer.getObjects()) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();

            float radius = 0.1f;
          

            bDef.type = BodyDef.BodyType.DynamicBody;
            bDef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / 16f, (rectangle.getY() + rectangle.getHeight() / 2) / 16f);

            circleShape.setRadius(radius);
            fDef.shape = circleShape;
            fDef.isSensor = true;
            body = world.createBody(bDef);
            body.createFixture(fDef);*/

           // circleShape.dispose();

        //}
        
        
		
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width , height);
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {
		tiledMap.dispose();
		tiledMapRenderer.dispose();
		world.dispose();
		b2Renderer.dispose();
		batch.dispose();
		animationSheet.dispose();
		
	}
	
}
