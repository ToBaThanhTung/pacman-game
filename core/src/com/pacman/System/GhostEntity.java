package com.pacman.System;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.math.Vector2;
import com.pacman.Astar.Node;
import com.pacman.component.GhostComponent;

public class GhostEntity implements Telegraph{
	GhostComponent ghostComponent;
	public StateMachine<GhostEntity, GhostState> state;
	public float velocity = 3f;
	
	public float time;
	public Node node;
	
	public GhostEntity(GhostComponent ghostComponent) {
		state = new DefaultStateMachine<GhostEntity, GhostState>(this);
		this.ghostComponent = ghostComponent;
		time = 0;
	}
	
	public Vector2 getPos() {
		return ghostComponent.getBody().getPosition();
	}
	
	public void update(float delta) {
		this.time += delta;
		state.update();
	}

	@Override
	public boolean handleMessage(Telegram msg) {
		// TODO Auto-generated method stub
		return false;
	}
}
