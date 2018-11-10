package com.ocelot.mod.game.main.entity.ai;

import com.ocelot.mod.game.core.entity.ai.AIBase;
import com.ocelot.mod.game.main.entity.enemy.Koopa.KoopaType;
import com.ocelot.mod.game.main.entity.player.Player;

public class AIKoopa extends AIBase {

	private KoopaType type;
	private boolean movingRight;

	public AIKoopa(KoopaType type) {
		this.type = type;
	}

	@Override
	public void initAI() {
		this.movingRight = mob.isFacingRight();
	}

	@Override
	public void update() {
		if (type != KoopaType.KAMIKAZE) {
			double newX = movingRight ? mob.getMoveSpeed() : -mob.getMoveSpeed();
			mob.calculateCorners(mob.getX() + newX, mob.getY());
			if ((mob.topLeft && mob.bottomLeft) || (mob.topRight && mob.bottomRight)) {
				if (mob instanceof BasicWalkListener) {
					((BasicWalkListener) mob).basicWalkTrigger(movingRight);
				}
				movingRight = !movingRight;
			}
		} else {
			Player nearestPlayer = level.getNearestPlayer(mob);
			if (nearestPlayer != null) {
				if (nearestPlayer.getX() <= mob.getX()) {
					movingRight = false;
				} else {
					movingRight = true;
				}
			}

			mob.calculateCorners(mob.getX() + (mob.isLeft() ? -1 : 1), mob.getY());
			if ((mob.topLeft && mob.bottomLeft) || (mob.topRight && mob.bottomRight)) {
				movingRight = !movingRight;
			}
		}
		mob.setRight(movingRight);
		mob.setLeft(!movingRight);
	}

	@Override
	public String getName() {
		return "Koopa";
	}
}