package com.pacman.System;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.pacman.component.MovementComponent;
import com.pacman.component.PillComponent;
import com.pacman.manager.Manager;

public class PillSystem extends IteratingSystem{
	
	
	ComponentMapper<PillComponent> pillMapper = ComponentMapper.getFor(PillComponent.class);
	//ComponentMapper<TransformComponent> trMapper = ComponentMapper.getFor(TransformComponent.class);
	ComponentMapper<MovementComponent> movMapper = ComponentMapper.getFor(MovementComponent.class);
	
	public PillSystem() {
		super(Family.all(PillComponent.class, MovementComponent.class).get());
	}
	@Override
    protected void processEntity(Entity entity, float deltaTime) {
        PillComponent pill = pillMapper.get(entity);
        MovementComponent movement = movMapper.get(entity);

        Body body = movement.body;
		
		if(pill.isEat == true) {
			body.getWorld().destroyBody(body);
			Manager.score += pill.smallPillScore;
			getEngine().removeEntity(entity);
			System.out.println("pacman eat pill at: " + body.getPosition());
			System.out.println("Score: " + Manager.score);
		}
	}
}
	
	
