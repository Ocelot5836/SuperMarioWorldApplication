package com.ocelot.mod.game.main.entity.enemy;

import java.util.List;

import com.ocelot.mod.game.core.EnumDirection;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.MarioDamageSource;
import com.ocelot.mod.game.core.entity.Entity;
import com.ocelot.mod.game.core.entity.IDamagable;
import com.ocelot.mod.game.core.entity.Mob;
import com.ocelot.mod.game.main.entity.player.Player;
import com.ocelot.mod.lib.AxisAlignedBB;

public class Thwomp extends Enemy {
	
	private int detectRange;

	public Thwomp(GameTemplate game) {
		this(game, 0, 0);
	}

	public Thwomp(GameTemplate game,double x, double y) {
		super(game);
		this.setSize(14, 18);
		this.setPosition(x, y);
		this.setLastPosition(x, y);
		
		
	}
	
	@SuppressWarnings("unused")
	private boolean isPlayerCloseEnough() {
		List<Player>players=level.getPlayers();
		for(int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			
			AxisAlignedBB box = player.getEntityBox();
			if(box.getXMax() >= this.x - detectRange && box.getX() < this.x + detectRange) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean dealDamage(Entity entity, EnumDirection sideHit) {
		if (entity instanceof Mob) {
			Mob mob = (Mob) entity;
			if (!mob.invulnerable) {
				if (mob instanceof IDamagable) {
					((IDamagable) mob).takeDamage(this, MarioDamageSource.THWOMP, sideHit, false);
				}
			}
		}
		return false;
	}
}