package System;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.pacman.component.StateComponent;

public class StateSystem extends IteratingSystem{
	
	ComponentMapper<StateComponent> scMapper = ComponentMapper.getFor(StateComponent.class);
	
	public StateSystem() {
		super(Family.all(StateComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		StateComponent stateComponent = scMapper.get(entity);
		//stateComponent.increaseTime(deltaTime);
	}
	
	
}
