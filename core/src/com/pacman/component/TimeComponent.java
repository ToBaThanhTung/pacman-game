package com.pacman.component;

import com.badlogic.ashley.core.Component;

public class TimeComponent implements Component{
	private float time;
	private int state;
	public TimeComponent() {
		
	}
	
	public TimeComponent(int state) {
		this.state = state;
		time  = 0;
	}
	
	public void increaseTime(float delta) {
		time +=  delta;
	}
	
	public int getState() {
		return state;
	}
	
	public void setState(int newState) {
		state = newState;
		time = 0;
	}
	
}
