package com.ocelot.mod.game.main.entity.enemy;

import java.awt.image.BufferedImage;

import com.ocelot.mod.Mod;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.Mob;
import com.ocelot.mod.game.main.entity.player.Player;
import com.ocelot.mod.lib.Colorizer;
import com.ocelot.mod.lib.Lib;

import net.minecraft.util.ResourceLocation;

public abstract class Enemy extends Mob {

	/**
	 * @deprecated This is not a good way to get enemy sprites. Try using {@link Colorizer#replacePixels(BufferedImage, int, int)} or {@link Colorizer#colorize(BufferedImage, int, int, int, int, int, int, int, int)} instead and use a grayscale image.
	 */
	public static final BufferedImage ENEMY_SHEET = Lib.loadImage(new ResourceLocation(Mod.MOD_ID, "textures/entity/enemy/enemies.png"));

	/** The range that this enemy can detect mobs at */
	protected double detectRange;

	public Enemy(GameTemplate game) {
		this(game, 0);
	}

	/**
	 * @param detectRange
	 *            The range in pixels of how close a mob has to be to be detected by this enemy
	 */
	public Enemy(GameTemplate game, double detectRange) {
		super(game);
		this.detectRange = detectRange;
	}

	/**
	 * Checks whether or not the specified damage source can hurt this enemy.
	 * 
	 * @param source
	 *            The source of damage
	 * @return Whether or not the source can do any damage
	 */
	public boolean canDamage(MarioDamageSource source) {
		return source != MarioDamageSource.ENEMY;
	}

	/**
	 * Searches for the nearest player if it is within the enemy's range.
	 * 
	 * @return The player if it was found inside the range
	 */
	protected Player getNearestPlayer() {
		Player nearestPlayer = this.level.getNearestPlayer(this);
		double distance = Math.sqrt(Math.pow(x - nearestPlayer.getX(), 2) + Math.pow(y - nearestPlayer.getY(), 2));
		return distance <= this.detectRange ? nearestPlayer : null;
	}

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
		MARIO, SHELL, ENEMY
	}
}