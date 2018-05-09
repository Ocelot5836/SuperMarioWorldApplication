package com.ocelot.mod.game.core.entity.ai;

import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.Mob;
import com.ocelot.mod.game.core.level.Level;

import scala.util.Random;

public abstract class AIBase implements IAI {

	protected GameTemplate game;
	protected Level level;
	protected Mob mob;
	protected Random random;
	
	public AIBase() {
		this.random = new Random();
	}

	@Override
	public void setMob(Mob mob) {
		this.mob = mob;
		this.game = mob.getGame();
		this.level = mob.getLevel();
	}
}