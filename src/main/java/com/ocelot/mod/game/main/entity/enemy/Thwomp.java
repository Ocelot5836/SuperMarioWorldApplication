package com.ocelot.mod.game.main.entity.enemy;

import com.ocelot.mod.game.core.EnumDirection;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.Entity;
import com.ocelot.mod.game.core.entity.IDamagable;
import com.ocelot.mod.game.core.entity.Mob;

public class Thwomp extends Enemy {

	public Thwomp(GameTemplate game) {
		super(game);
	}

	@Override
	public boolean dealDamage(Entity entity, EnumDirection sideHit) {
		if (entity instanceof Mob) {
			Mob mob = (Mob) entity;
			if (!mob.invulnerable) {
				if(mob instanceof IDamagable) {
					((IDamagable)mob).takeDamage(this, MarioDamageSource.THWOMP, sideHit, false);
				}
			}
		}
		return false;
	}
}