package com.pacman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pacman.Screen.PlayScreen;

public class PacMan extends Game {
	public SpriteBatch batch;
	@Override
	public void create() {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}
	@Override
	public void render() {
		super.render();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
	}
}
