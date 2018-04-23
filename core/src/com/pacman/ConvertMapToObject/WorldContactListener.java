package com.pacman.ConvertMapToObject;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.pacman.Sprites.Pill;
import com.pacman.component.PillComponent;
import com.pacman.manager.Manager;

public class WorldContactListener implements ContactListener{
	private ComponentMapper<PillComponent> pillM = ComponentMapper.getFor(PillComponent.class);
	
	
	@Override
	public void beginContact(Contact contact) {
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		//System.out.println(fa.getBody().getType()+" has hit "+ fb.getBody().getType());
		
		if(fa.getFilterData().categoryBits == Manager.PillBit || fb.getFilterData().categoryBits == Manager.PillBit) {
			if(fa.getFilterData().categoryBits == Manager.pacmanBit) {
				Entity entity = (Entity) fb.getBody().getUserData();
				PillComponent pillComponent = pillM.get(entity);
				pillComponent.isEat = true;
			}
			else if (fb.getFilterData().categoryBits == Manager.pacmanBit) {
                Body body = fa.getBody();
                Entity entity = (Entity) body.getUserData();
                PillComponent pill = pillM.get(entity);
                pill.isEat = true;
			}
		}
		
	}
	


	@Override
	public void endContact(Contact contact) {
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}

}
