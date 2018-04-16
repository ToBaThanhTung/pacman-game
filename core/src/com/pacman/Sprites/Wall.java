package com.pacman.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.pacman.FinalStaticVar.PacFinal;

public class Wall extends InteractiveTileObject{
	
	private BodyDef bodyDef;
	private FixtureDef fDef;
	PolygonShape shape;
	
	public Wall(World world, TiledMap tiledMap, Rectangle rectangle) {
		
	}

}
