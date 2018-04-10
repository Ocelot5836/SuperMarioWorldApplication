package com.ocelot.mod.game.main.entity.fx.particle;

import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.ISpawnerEntity;
import com.ocelot.mod.game.core.entity.Spawner;
import com.ocelot.mod.game.core.entity.fx.Particle;
import com.ocelot.mod.game.core.level.Level;

public class CheeseParticle extends Particle {

	public CheeseParticle(GameTemplate game, double x, double y, int life) {
		super(game, x, y, life, life / 2, 0.5, 0.75, 0.1, 0.25, CHEESE_SPRITE);
	}

	public static class Spawnable implements ISpawnerEntity {
		@Override
		public void create(GameTemplate game, Level level, Spawner spawner, double x, double y, Object... args) {
			if (args.length > 0) {
				level.add(new CheeseParticle(game, x, y, parseInt(args[0])));
			}
		}
	}
}