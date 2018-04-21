package com.pacman.System;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.pacman.component.MovementComponent;
import com.pacman.component.PillComponent;
import com.pacman.component.TransformComponent;

public class MovementSystem extends IteratingSystem{
	
	private ComponentMapper<MovementComponent> mv = ComponentMapper.getFor(MovementComponent.class);
	private ComponentMapper<TransformComponent> tr = ComponentMapper.getFor(TransformComponent.class);
	//private ComponentMapper<PillComponent> pi = ComponentMapper.getFor(PillComponent.class);
	public MovementSystem() {
		super(Family.all(MovementComponent.class, TransformComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		//System.out.println("cacac");
		MovementComponent mvComponent = mv.get(entity);
		TransformComponent trComponent = tr.get(entity);
		//PillComponent pillComponent = pi.get(entity);
		trComponent.pos.set(mvComponent.body.getPosition());
		
	}

}
