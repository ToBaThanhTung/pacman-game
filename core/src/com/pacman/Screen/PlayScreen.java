package com.pacman.Screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.profiling.GL20Interceptor;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pacman.PacMan;

public class PlayScreen implements Screen{
	
	private final float WIDTH = 19.0f;
	private final float HEIGHT = 23.0f;
	
	private PacMan game;
	private SpriteBatch batch;
	private Engine engine;
	private OrthographicCamera camera;
	private TiledMap tiledMap;
	private OrthoCachedTiledMapRenderer tiledMapRenderer;
	
	private FitViewport viewport;
	
	public PlayScreen(PacMan game) {
		this.game = game;
		this.batch = game.batch;
	}

	@Override
	public void show() {
		camera = new OrthographicCamera();
		viewport = new FitViewport(WIDTH, HEIGHT, camera);
		camera.translate(WIDTH / 2, HEIGHT / 2);
		camera.update();
		
		batch = new SpriteBatch();
		
		
		// load map
		tiledMap = new TmxMapLoader().load("map/map.tmx");
		// load the map, set the unit scale to 1/16 (1 unit == 16 pixels)
		tiledMapRenderer = new OrthoCachedTiledMapRenderer(tiledMap, 1/16f);
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.1f, 0.2f, 0.2f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();
		
		batch.setProjectionMatrix(camera.combined);
		
		
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
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
	}
	
}
