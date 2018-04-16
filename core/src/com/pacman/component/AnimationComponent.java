package com.pacman.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.IntMap;

public class AnimationComponent implements Component{
	public IntMap<Animation> ani = new IntMap<Animation>();
}
