package com.ocelot.mod.game.core.entity;

import com.ocelot.mod.game.Game;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.level.Level;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * Allows the ability to add objects to the level.
 * 
 * @author Ocelot5836
 */
public class Spawner extends Entity {

	private ISpawnerEntity entity;
	private int amount;
	private Object[] params;

	public Spawner(GameTemplate game, double x, double y, ISpawnerEntity entity, int amount, Object... params) {
		super(game);
		this.setPosition(x, y);
		this.entity = entity;
		this.amount = Math.min(amount, 10000);
		this.params = params;
	}

	@Override
	public void init(Level level) {
		super.init(level);
		for (int i = 0; i < amount; i++) {
			try {
				entity.create(game, level, this, x, y, params);
			} catch (SummonException e) {
				Game.stop(e, "Could not summon an entity using an entity spawner");
			}
		}
		this.setDead();
	}
}