package com.ocelot.mod.game.main.entity.enemy.bowser;

import com.ocelot.mod.game.core.EnumDirection;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.MarioDamageSource;
import com.ocelot.mod.game.core.entity.Entity;
import com.ocelot.mod.game.core.entity.IDamagable;
import com.ocelot.mod.game.core.gfx.Animation;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.main.entity.enemy.Enemy;

public class BowserFire extends Enemy {

	private static int delay = 20;
	private static Sprite[] sprites;

	private Animation<Sprite> animation;

	public BowserFire(GameTemplate game) {
		super(game);

		if (sprites == null) {
			sprites = new Sprite[4];
			loadSprites();
		}

		this.animation = new Animation<Sprite>();
		this.animation.setFrames(sprites);
		this.animation.setDelay(delay);
	}

	private static void loadSprites() {

	}

	@Override
	public boolean dealDamage(Entity entity, EnumDirection sideHit) {
		if (entity instanceof IDamagable) {
			return ((IDamagable) entity).takeDamage(this, MarioDamageSource.BOWSER, sideHit, false);
		}
		return false;
	}
}