package com.pacman.ConvertMapToObject;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.pacman.component.GhostComponent;
import com.pacman.component.PacmanComponent;
import com.pacman.component.PillComponent;
import com.pacman.component.StateComponent;
import com.pacman.manager.Manager;

public class WorldContactListener implements ContactListener{
	private ComponentMapper<PillComponent> pillM = ComponentMapper.getFor(PillComponent.class);
	private ComponentMapper<PacmanComponent> pacmanM = ComponentMapper.getFor(PacmanComponent.class);
	private ComponentMapper<GhostComponent> ghostM = ComponentMapper.getFor(GhostComponent.class);
	private ComponentMapper<StateComponent> stateM = ComponentMapper.getFor(StateComponent.class);
	
	
	@SuppressWarnings("static-access")
	@Override
	public void beginContact(Contact contact) {
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		//System.out.println(fa.getBody().getType()+" has hit "+ fb.getBody().getType());
		
		if(fa.getFilterData().categoryBits == Manager.PillBit || fb.getFilterData().categoryBits == Manager.PillBit) {
			if(fa.getFilterData().categoryBits == Manager.pacmanBit) {
				Entity entity = (Entity) fb.getBody().getUserData();
				PillComponent pillComponent =  pillM.get(entity);
				pillComponent.isEat = true;
				GhostComponent ghost = ghostM.get(entity);
				Manager.manager.isGhostScare = pillComponent.isBig;
			}
			else if (fb.getFilterData().categoryBits == Manager.pacmanBit) {
                Body body = fa.getBody();
                Entity entity = (Entity) body.getUserData();
                PillComponent pill = pillM.get(entity);
                pill.isEat = true;
                Manager.manager.isGhostScare = pill.isBig;
			}
		}
		
		if(fa.getFilterData().categoryBits == Manager.ghostBit || fb.getFilterData().categoryBits == Manager.ghostBit) {
			if(fa.getFilterData().categoryBits == Manager.pacmanBit) {
				//Entity entity = (Entity) fb.getBody().getUserData();
				PacmanComponent pacmanComponent = pacmanM.get((Entity) fa.getBody().getUserData());
				GhostComponent ghostComponent = ghostM.get((Entity) fb.getBody().getUserData());
				//StateComponent stateComponent = stateM.get(entity);
				
				if(ghostComponent.isScareMode == true) {
					Manager.score += 300;
					Gdx.app.log("contact ", "Ghost die!" + " + 500 score!");
					ghostComponent.ghostDie = true;
				}
				else {
					//pacmanComponent.hp --;
					Gdx.app.log("contact ", "Pacman die!");
					Manager.manager.playAlive --;
					//stateComponent.setState(pacmanComponent.DIE);
					pacmanComponent.isDie = true;
				}
			} 
			if	(fb.getFilterData().categoryBits == Manager.pacmanBit) {
					//Entity entity = (Entity) fa.getBody().getUserData();
					PacmanComponent pacmanComponent = pacmanM.get((Entity) fb.getBody().getUserData());
					GhostComponent ghostComponent = ghostM.get((Entity) fa.getBody().getUserData());
					//StateComponent stateComponent = stateM.get(entity);
					if(ghostComponent.isScareMode == true) {
						Manager.score += 300;
						Gdx.app.log("contact ", "Ghost die!" + " + 500 score!"  );
						ghostComponent.ghostDie = true;
					}
					else {
						//pacmanComponent.hp --;
						Gdx.app.log("contact ", "Pacman die!" );
						Manager.manager.playAlive --;
					//	stateComponent.setState(pacmanComponent.DIE);
						pacmanComponent.isDie = true; 
					}
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
