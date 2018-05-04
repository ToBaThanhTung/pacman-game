package com.pacman.component;

import com.badlogic.ashley.core.Component;

public class StateComponent implements Component{
	private float time;
	private int state;
	
	public StateComponent() {
		
	}
	
	public StateComponent(int state) {
		this.state = state;
		time  = 0;
	}
	
	public float getStateTime() {
		return time;
	}
	
	public void increaseTime(float delta) {
		time +=  delta;
	}
	
	public int getState() {
		return state;
	}
	
	public void setState(int newState) {
		state = newState;
		//time = 0;
	}
	
}
