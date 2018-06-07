package com.ocelot.mod.game.main.entity.powerup;

import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.EntityItem;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.main.entity.item.EntityPowerup;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class PowerupMissing extends Powerup {

	private Sprite sprite;

	public PowerupMissing() {
		super("null", "null");
		this.sprite = new Sprite();
	}

	@Override
	public void update() {
	}

	@Override
	public void render(Minecraft mc, Gui gui, double x, double y, int mouseX, int mouseY, float partialTicks) {
		sprite.render(x, y, 16, 16);
	}

	@Override
	public EntityItem createInstance(GameTemplate game, double x, double y) {
		return (EntityItem) new EntityPowerup(game, 0, 0, 16, 16, this).setPosition(x, y).setLastPosition(x, y);
	}
}