package com.pacman.System;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pacman.component.AnimationComponent;
import com.pacman.component.StateComponent;
import com.pacman.component.TextureComponent;

public class AnimationSystem extends IteratingSystem{
	private final ComponentMapper<TextureComponent> textureM = ComponentMapper.getFor(TextureComponent.class);
	private final ComponentMapper<AnimationComponent> aniM = ComponentMapper.getFor(AnimationComponent.class);
	private final ComponentMapper<StateComponent> stateM = ComponentMapper.getFor(StateComponent.class);
	
	public AnimationSystem() {
		super(Family.all(TextureComponent.class, AnimationComponent.class, StateComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		TextureComponent textureComponent = textureM.get(entity);
		AnimationComponent animationComponent = aniM.get(entity);
		StateComponent stateComponent = stateM.get(entity);
		
		Animation animation = animationComponent.ani.get(stateComponent.getState());
		
		if (animation != null) {
			textureComponent.region = (TextureRegion) animation.getKeyFrame(stateComponent.getStateTime()); 
		}
		
		stateComponent.increaseTime(deltaTime);
	}
}
