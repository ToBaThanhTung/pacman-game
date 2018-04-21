package com.pacman.component;

import com.badlogic.ashley.core.Component;

public class PillComponent implements Component{
	public int smallPillScore = 100;
	public int bigPillScore = 200;
	public boolean isEat = false;
}
