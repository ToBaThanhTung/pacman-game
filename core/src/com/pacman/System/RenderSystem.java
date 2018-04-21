package com.pacman.System;

import java.util.Comparator;

import javax.xml.soap.Text;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.pacman.component.TextureComponent;
import com.pacman.component.TransformComponent;
import com.pacman.manager.Manager;

public class RenderSystem extends IteratingSystem{
	ComponentMapper<TextureComponent> texMapper = ComponentMapper.getFor(TextureComponent.class);
	ComponentMapper<TransformComponent> tranMapper = ComponentMapper.getFor(TransformComponent.class);
	private final  Comparator<Entity> comparator;
	private final SpriteBatch batch;
	private final Array<Entity> renderQueue;
	
	
	public RenderSystem(SpriteBatch batch) {
		super(Family.all(TextureComponent.class, TransformComponent.class).get());
		
		this.batch = batch;
		renderQueue = new Array<Entity>();
		comparator = new Comparator<Entity>() {

			@Override
			public int compare(Entity o1, Entity o2) {
				return (int) Math.signum(tranMapper.get(o2).zIndex - tranMapper.get(o1).zIndex);
			}
			
		};
		
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		renderQueue.sort(comparator);
		batch.begin();
		for(Entity entity : renderQueue) {
			TransformComponent transformComponent  = tranMapper.get(entity);
			TextureComponent textureComponent = texMapper.get(entity);
			
			float w = textureComponent.region.getRegionWidth() / Manager.PPM;
			float h = textureComponent.region.getRegionHeight() / Manager.PPM;
			float ox = w * 0.5f;
			float oy = h * 0.5f;
			
			batch.draw(textureComponent.region, 
					transformComponent.pos.x - ox , transformComponent.pos.y - oy, ox, oy, w, h, 
					transformComponent.scale.x, transformComponent.scale.y, 
					transformComponent.rotation * MathUtils.radiansToDegrees);
		}
		batch.end();
		renderQueue.clear();
	}
	

	@Override
	protected void processEntity(Entity entity, float deltaTime) {

		
		renderQueue.add(entity);
		
	}
	
}
