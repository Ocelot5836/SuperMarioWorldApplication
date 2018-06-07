package com.ocelot.mod.game.main.entity.powerup;

import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.Entity;
import com.ocelot.mod.game.core.entity.EntityItem;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.main.entity.item.EntityPowerup;
import com.ocelot.mod.game.main.entity.player.Player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class PowerupMushroom extends Powerup {

	private Sprite sprite;
	
	public PowerupMushroom(String registryName, String unlocalizedName) {
		super(registryName, unlocalizedName);
		this.sprite = new Sprite(POWERUP_SHEET.getSubimage(0, 0, 16, 16));
	}

	@Override
	public void update() {
	}

	@Override
	public void render(Minecraft mc, Gui gui, double x, double y, int mouseX, int mouseY, float partialTicks) {
		sprite.render(x, y);
	}

	@Override
	public EntityItem createInstance(GameTemplate game, double x, double y) {
		return (EntityItem) new EntityPowerup(game, 1, 0, 16, 16, this).setPosition(x, y).setLastPosition(x, y);
	}
	
	@Override
	public void apply(Entity entity) {
		if(entity instanceof Player) {
			Player player = (Player)entity;
			if(player.getProperties().isSmall()) {
				player.getProperties().setBig();
			}
			player.getProperties().increaseScore(500);
		}
	}
}