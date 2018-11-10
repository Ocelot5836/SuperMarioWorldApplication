package com.ocelot.mod.game.main.entity.ai;

import com.ocelot.mod.game.core.entity.Mob;
import com.ocelot.mod.game.core.entity.ai.AIBase;

public class AIBasicWalk extends AIBase {

	private boolean movingRight;

	@Override
	public void initAI() {
		this.movingRight = this.getMob().isFacingRight();
	}

	@Override
	public void update() {
		Mob mob = this.getMob();
		double newX = movingRight ? mob.getMoveSpeed() : -mob.getMoveSpeed();

		mob.calculateCorners(mob.getX() + (newX == 0 ? 0 : newX < 0 ? -1 : 1), mob.getY());
		if ((mob.topLeft && mob.bottomLeft) || (mob.topRight && mob.bottomRight)) {
			if (mob instanceof BasicWalkListener) {
				((BasicWalkListener) mob).basicWalkTrigger(movingRight);
			}
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