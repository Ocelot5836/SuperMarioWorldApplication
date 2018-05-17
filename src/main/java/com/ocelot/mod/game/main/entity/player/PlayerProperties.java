package com.ocelot.mod.game.main.entity.player;

import com.ocelot.mod.audio.Sounds;
import com.ocelot.mod.game.core.GameTemplate;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class PlayerProperties implements INBTSerializable<NBTTagCompound> {

	private GameTemplate game;

	private boolean enableKeyboardInput;
	private byte powerupId;
	private boolean dead;
	private boolean running;
	private boolean holding;
	private int enemyJumpCount;
	private byte lives;
	private byte coins;
	private byte dragonCoins;
	private byte bonus;
	private int score;

	public PlayerProperties(GameTemplate game) {
		this(game, true, 0);
	}

	public PlayerProperties(GameTemplate game, boolean enableKeyboardInput, int powerupId) {
		this.game = game;
		this.enableKeyboardInput = enableKeyboardInput;
		this.powerupId = (byte) powerupId;
		this.dead = false;
		this.running = false;
		this.holding = false;
		this.enemyJumpCount = 0;
		this.lives = 4;
		this.coins = 0;
		this.dragonCoins = 0;
		this.bonus = 0;
		this.score = 0;
	}

	public boolean isKeyboardInputEnabled() {
		return enableKeyboardInput;
	}

	public boolean isDead() {
		return dead;
	}

	public boolean isRunning() {
		return running;
	}

	public boolean isSmall() {
		return powerupId == 0;
	}

	public boolean isBig() {
		return powerupId == 1;
	}

	public boolean isFire() {
		return powerupId == 2;
	}

	public boolean isCape() {
		return powerupId == 3;
	}

	public boolean isHolding() {
		return holding;
	}

	public int getEnemyJumpCount() {
		return enemyJumpCount;
	}

	public byte getLives() {
		return lives;
	}

	public byte getCoins() {
		return coins;
	}

	public byte getDragonCoins() {
		return dragonCoins;
	}

	public byte getBonus() {
		return bonus;
	}

	public int getScore() {
		return score;
	}

	public void setKeyboardInputEnabled(boolean enableKeyboardInput) {
		this.enableKeyboardInput = enableKeyboardInput;
	}

	public void setDead() {
		this.dead = true;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public void setSmall() {
		this.powerupId = 0;
	}

	public void setBig() {
		this.powerupId = 1;
	}

	public void setFire() {
		this.powerupId = 2;
	}

	public void setCape() {
		this.powerupId = 3;
	}

	public void setHolding(boolean holding) {
		this.holding = holding;
	}

	public void setEnemyJumpCount(int enemyJumpCount) {
		this.enemyJumpCount = enemyJumpCount;
		if (this.enemyJumpCount < 0) {
			this.enemyJumpCount = 0;
		}
	}

	public void setLives(int lives) {
		if (lives < 0) {
			lives = 0;
		}
		if (lives > 99) {
			lives = 99;
		}
		this.lives = (byte) lives;
	}

	public void setCoins(int coins) {
		if (coins < 0) {
			coins = 0;
		}
		if (coins > 99) {
			coins = 0;
			increaseLives();
			game.playSound(Sounds.COLLECT_ONE_UP, 1.0F);
		}
		this.coins = (byte) coins;
	}

	public void setDragonCoins(int coins) {
		if (coins < 0) {
			coins = 0;
		}
		if (coins > 4) {
			coins = 4;
		}
		this.dragonCoins = (byte) coins;
	}

	public void setBonus(int bonus) {
		if (bonus < 0) {
			bonus = 0;
		}
		if (bonus > 100) {
			coins = 100;
		}
		this.bonus = (byte) bonus;
	}

	public void setScore(int score) {
		if (score < 0) {
			score = 0;
		}
		if (score > 9999990) {
			score = 9999990;
		}
		this.score = score;
	}

	public void increaseEnemyJumpCounter() {
		this.enemyJumpCount++;
	}

	public void decreaseEnemyJumpCounter() {
		this.enemyJumpCount--;
		if (this.enemyJumpCount < 0)
			this.enemyJumpCount = 0;
	}

	public void increaseLives() {
		this.lives++;
		if (this.lives > 99)
			this.lives = 99;
	}

	public void decreaseLives() {
		this.lives--;
		if (this.lives < 0) {
			this.lives = 0;
		}
	}

	public void increaseCoins() {
		setCoins(this.coins + 1);
	}

	public void decreaseCoins() {
		setCoins(this.coins - 1);
	}

	public void increaseDragonCoins() {
		setDragonCoins(this.dragonCoins + 1);
	}

	public void decreaseDragonCoins() {
		setDragonCoins(this.dragonCoins - 1);
	}

	public void increaseBonus() {
		setBonus(this.bonus + 1);
	}

	public void decreaseBonus() {
		setBonus(this.bonus - 1);
	}

	public void increaseScore(int amount) {
		setScore(this.score + amount);
	}

	public void decreaseScore(int amount) {
		setScore(this.score - amount);
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setByte("powerupId", this.powerupId);
		nbt.setByte("lives", this.lives);
		nbt.setByte("coins", this.coins);
		nbt.setByte("dragonCoins", this.dragonCoins);
		nbt.setByte("bonus", this.bonus);
		nbt.setInteger("score", this.score);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.powerupId = nbt.getByte("powerupId");
		this.lives = nbt.getByte("lives");
		this.coins = nbt.getByte("coins");
		this.dragonCoins = nbt.getByte("dragonCoins");
		this.bonus = nbt.getByte("bonus");
		this.score = nbt.getInteger("score");
	}
}