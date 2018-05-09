package com.pacman;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pacman.Screen.MainMenu;
import com.pacman.Screen.PlayScreen;

public class PacMan extends Game {
	public SpriteBatch batch;
	@Override
	public void create() {
		batch = new SpriteBatch();
		setScreen(new MainMenu(this));
		
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
