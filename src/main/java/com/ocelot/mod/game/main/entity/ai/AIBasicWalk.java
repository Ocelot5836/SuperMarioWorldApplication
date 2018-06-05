package com.ocelot.mod.game.main.entity.ai;

import com.ocelot.mod.game.core.entity.ai.AIBase;
import com.ocelot.mod.lib.Lib;

public class AIBasicWalk extends AIBase {

	private boolean movingRight;

	@Override
	public void initAI() {
		this.movingRight = mob.isFacingRight();
	}

	@Override
	public void update() {
		double newX = movingRight ? mob.getMoveSpeed() : -mob.getMoveSpeed();
		mob.calculateCorners(mob.getX() + (newX == 0 ? 0 : newX < 0 ? -1 : 1), mob.getY());
		if ((mob.topLeft && mob.bottomLeft) || (mob.topRight && mob.bottomRight)) {
			movingRight = !movingRight;
		}
		mob.setRight(movingRight);
		mob.setLeft(!movingRight);
	}

	@Override
	public String getName() {
		return "BasicWalk";
	}
}