package com.ocelot.mod.game.main.entity.powerup;

import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.EntityItem;
import com.ocelot.mod.game.main.entity.EntityPowerup;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class PowerupNull extends Powerup {

	public PowerupNull() {
		super("null", "null");
	}

	@Override
	public void update() {
	}

	@Override
	public void render(double x, double y, Minecraft mc, Gui gui, int mouseX, int mouseY, float partialTicks) {
	}

	@Override
	public EntityItem createInstance(GameTemplate game, double x, double y) {
		return (EntityItem) new EntityPowerup(game, 0, 0, 16, 16, this).setPosition(x, y).setLastPosition(x, y);
	}
}