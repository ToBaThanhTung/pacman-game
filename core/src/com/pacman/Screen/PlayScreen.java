package com.pacman.Screen;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pacman.Asset;
import com.pacman.PacMan;
import com.pacman.ConvertMapToObject.BuildObject;
import com.pacman.ConvertMapToObject.WorldContactListener;

import com.pacman.System.*;
import com.pacman.manager.Manager;

public class PlayScreen implements Screen {

	private final float WIDTH = 35.0f;
	private final float HEIGHT = 15.0f;

	private PacMan game;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private TiledMap tiledMap;
	private OrthoCachedTiledMapRenderer tiledMapRenderer;
	private FitViewport viewport;

	private PooledEngine engine;
	private PacmanSystem pacmanSystem;
	private MovementSystem movementSystem;
	private AnimationSystem animationSystem;
	private RenderSystem renderSystem;
	private PillSystem pillSystem;
	private StateSystem stateSystem;
	private GhostSystem ghostSystem;

	// draw text
	private BitmapFont font;
	public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"�`'<>";
	private Label label;
	// just for demo

	private TextureAtlas atlas;
	private TextureRegion pacmanTexture;
	private Texture texture;
	World world;
	private Box2DDebugRenderer b2Renderer;
	private Texture animationSheet;
	private Texture image = new Texture("animation1.png");
	private TextureRegion Health = new TextureRegion(image, 642, 43, 32, 32);

	public PlayScreen(PacMan game) {
		this.game = game;
		this.batch = game.batch;
	}

	@Override
	public void show() {
		camera = new OrthographicCamera();
		viewport = new FitViewport(WIDTH, HEIGHT, camera);
		camera.translate(WIDTH / 2, HEIGHT / 2);
		// camera.update();
		batch = new SpriteBatch();

		// load font nbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb.
		//FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("pacmanfont.ttf"));
		font = new BitmapFont();
		// font = generator.generateFont(12, FONT_CHARACTERS, false);
		 font.setColor(Color.BLUE);
		 font.getData().setScale(0.1f, 0.1f);
		// Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE) ;
		// load assets

		Asset.load();

		world = new World(new Vector2(0, 0), true);
		world.setContactListener(new WorldContactListener());
		b2Renderer = new Box2DDebugRenderer();

		engine = new PooledEngine();

		pacmanSystem = new PacmanSystem();
		pillSystem = new PillSystem();
		movementSystem = new MovementSystem();
		animationSystem = new AnimationSystem();
		stateSystem = new StateSystem();
		ghostSystem = new GhostSystem();
		renderSystem = new RenderSystem(batch);

		engine.addSystem(pillSystem);
		engine.addSystem(pacmanSystem);
		engine.addSystem(ghostSystem);
		engine.addSystem(movementSystem);
		engine.addSystem(animationSystem);
		engine.addSystem(stateSystem);
		engine.addSystem(renderSystem);

		// load map
		tiledMap = new TmxMapLoader().load("map/map2.tmx");
		// load the map, set the unit scale to 1/32 (1 unit == 32 pixels)
		tiledMapRenderer = new OrthoCachedTiledMapRenderer(tiledMap, 1 / 32f);

		new BuildObject(tiledMap, world, engine).build();

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		

		world.step(1 / 60f, 8, 3);
		batch.setProjectionMatrix(camera.combined);
		engine.update(delta);
		
		
		

		// draw alive
		batch.begin();
		for (int i = 1; i <= Manager.manager.playAlive; ++i) {
			batch.draw(Health, i, 14, 1, 1);
		}
		
		
		batch.end();
		
	
		// print score
		
		
		batch.begin();
		font.draw(batch,Manager.manager.score.toString(), 10, 15);
		batch.end();

		/*
		 * batch.begin(); font.setColor(1.0f, 1.0f, 1.0f, 1.0f); font.draw(batch,
		 * "some string", 25, 160); batch.end();
		 */

		// b2Renderer.render(world, camera.combined);

		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();
		 if(Manager.manager.playAlive == 0) {
			 Manager.manager.playAlive = 3;
			 Manager.manager.score = 1000;
	    	 game.setScreen(new MainMenu(game));
	      }
		

	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		camera.update();
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
		font.dispose();

	}

}
