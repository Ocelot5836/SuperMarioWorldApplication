package com.ocelot.mod.game.main.entity.powerup;

import java.awt.image.BufferedImage;
import java.util.Map;

import com.google.common.collect.Maps;
import com.ocelot.mod.Mod;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.EntityItem;
import com.ocelot.mod.game.main.entity.item.EntityPowerup;
import com.ocelot.mod.game.main.entity.player.Player;
import com.ocelot.mod.game.main.tile.IQuestionBlockItem;
import com.ocelot.mod.game.main.tile.TileQuestionBlock;
import com.ocelot.mod.lib.Lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public abstract class Powerup implements IQuestionBlockItem {

	public static final BufferedImage POWERUP_SHEET = Lib.loadImage(new ResourceLocation(Mod.MOD_ID, "textures/powerups.png"));

	private static final Map<String, Powerup> POWERUPS = Maps.<String, Powerup>newHashMap();

	public static final Powerup NULL = new PowerupNull();
	public static final Powerup MUSHROOM = new PowerupMushroom();

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

	@Override
	public abstract void update();

	@Override
	public abstract void render(Minecraft mc, Gui gui, double x, double y, int mouseX, int mouseY, float partialTicks);

	@Override
	public abstract EntityItem createInstance(GameTemplate game, double x, double y);

	public void onPickup(EntityPowerup powerup, Player entity) {
		powerup.setDead();
		this.apply(entity);
	}

	public void apply(Player entity) {
	}

	public String getRegistryName() {
		return registryName;
	}

	public String getUnlocalizedName() {
		return unlocalizedName;
	}

	public String getLocalizedName() {
		return I18n.format("powerup." + Mod.MOD_ID + "." + this.getUnlocalizedName() + ".name");
	}

	@Override
	public String getName() {
		return this.getRegistryName();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Powerup) {
			Powerup powerup = (Powerup) obj;
			return powerup.getRegistryName().equalsIgnoreCase(this.getRegistryName());
		}
		return super.equals(obj);
	}

	@Override
	public String toString() {
		return "Powerup[" + this.getLocalizedName() + "/" + this.getUnlocalizedName() + ":" + this.getRegistryName() + "]";
	}

	public static Powerup byName(String registryName) {
		return POWERUPS.get(registryName);
	}
}