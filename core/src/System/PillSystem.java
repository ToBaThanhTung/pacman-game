package System;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.pacman.component.PillComponent;
import com.pacman.component.TextureComponent;
import com.pacman.component.TransformComponent;

public class PillSystem extends IteratingSystem{
	
	ComponentMapper<PillComponent> pillMapper = ComponentMapper.getFor(PillComponent.class);
	ComponentMapper<TransformComponent> trMapper = ComponentMapper.getFor(TransformComponent.class);
	
	public PillSystem() {
		super(Family.all(TextureComponent.class, PillComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		PillComponent pill = pillMapper.get(entity);
		
	}
	
	
}
