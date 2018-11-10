package com.ocelot.mod.game.core;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * The type of damage that can be dealt to a mob.
 * 
 * @author Ocelot5836
 */
public enum MarioDamageSource {
	MARIO, SHELL, REX, THWOMP, BOWSER, GALOOMBA, GALOOMBA_ITEM, WRENCH;
	
	public boolean isEnemy() {
		return this != MARIO && this != SHELL;
	}

	public boolean isHeavy() {
		return this == MARIO || this == THWOMP;
	}

	public boolean canDamagePlayer() {
		return (this != MARIO && this != GALOOMBA_ITEM) || this.isEnemyProjectile();
	}
	
	public boolean isPlayerProjectile() {
		return this == GALOOMBA_ITEM;
	}
	
	public boolean isEnemyProjectile() {
		return this == WRENCH;
	}
}