package com.ocelot.mod.game.main.entity.powerup;

import java.util.Map;

import com.google.common.collect.Maps;
import com.ocelot.mod.SuperMarioWorld;
import com.ocelot.mod.game.main.entity.EntityPowerup;
import com.ocelot.mod.game.main.entity.player.Player;
import com.ocelot.mod.game.main.tile.IQuestionBlockItem;
import com.ocelot.mod.game.main.tile.TileQuestionBlock;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public abstract class Powerup implements IQuestionBlockItem {

	public static final ResourceLocation POWERUP_SHEET = new ResourceLocation(SuperMarioWorld.MOD_ID, "textures/powerups.png");

	private static final Map<String, Powerup> POWERUPS = Maps.<String, Powerup>newHashMap();

	public static final Powerup NULL = new PowerupNull();
	public static final Powerup MUSHROOM = new PowerupMushroom();
	public static final Powerup FEATHER = new PowerupFeather();

	private String registryName;
	private String unlocalizedName;

	public Powerup(String registryName, String unlocalizedName) {
		this.registryName = registryName;
		this.unlocalizedName = unlocalizedName;
		if (!POWERUPS.containsKey(registryName)) {
			POWERUPS.put(registryName, this);
		} else {
			throw new RuntimeException("Attempted to register a powerup over another. OLD: " + POWERUPS.get(registryName).getLocalizedName() + ", NEW: " + this.getLocalizedName());
		}
		TileQuestionBlock.registerQuestionBlockItem(this);
	}

	public void onPickup(EntityPowerup powerup, Player player) {
		powerup.setDead();
		player.collectPowerup(this);
	}

	/**
	 * Called when the player can use this effect.
	 * 
	 * @param player
	 *            The player that will have the effect applied
	 */
	public void apply(Player player) {
	}

	public String getRegistryName() {
		return registryName;
	}

	public String getUnlocalizedName() {
		return unlocalizedName;
	}

	public String getLocalizedName() {
		return I18n.format("powerup." + SuperMarioWorld.MOD_ID + "." + this.getUnlocalizedName() + ".name");
	}

	@Override
	public String getName() {
		return this.getRegistryName();
	}

	@Override
	public String toString() {
		return "Powerup[" + this.getLocalizedName() + "/" + this.getUnlocalizedName() + ":" + this.getRegistryName() + "]";
	}

	public static Powerup byName(String registryName) {
		return POWERUPS.containsKey(registryName) ? POWERUPS.get(registryName) : NULL;
	}
}