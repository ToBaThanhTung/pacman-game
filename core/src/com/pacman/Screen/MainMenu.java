package com.pacman.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pacman.PacMan;
public class MainMenu implements Screen {

	SpriteBatch batch;
	BitmapFont font;
	Texture texture;
	String str;
	PacMan game;
	Texture label = new Texture("capture.png");
	public MainMenu(PacMan game)
	{
		this.game = game;
		this.batch = game.batch;
	    texture = new Texture("images.jpg");
	}
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float arg0) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
      game.batch.draw(texture, 0, 0);
 //     batch.draw(label,(float) (Gdx.graphics.getWidth()/3),(float) (Gdx.graphics.getHeight()/1.22));
      if(Gdx.input.isTouched())
      {
    	 game.setScreen(new PlayScreen(game));
      }
      batch.end();
	}

	@Override
	public void resize(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}
	

}
