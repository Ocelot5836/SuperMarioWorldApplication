package com.ocelot.mod.game.main.entity;

import java.util.List;

import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.EntityItem;
import com.ocelot.mod.game.main.entity.player.Player;
import com.ocelot.mod.game.main.entity.powerup.Powerup;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class EntityPowerup extends EntityItem {

	private Powerup powerup;

	public EntityPowerup(GameTemplate game, double xSpeed, double ySpeed, int width, int height, Powerup powerup) {
		this(game, xSpeed, ySpeed, 0, 0, width, height, powerup);
	}

	public EntityPowerup(GameTemplate game, double xSpeed, double ySpeed, double x, double y, int width, int height, Powerup powerup) {
		super(game, xSpeed, ySpeed, 0.015);
		this.setSize(width, height);
		this.setPosition(x, y);
		this.setLastPosition(x, y);
		this.powerup = powerup;
	}

	@Override
	public void update() {
		super.update();
		this.powerup.update();

		List<Player> players = level.getPlayers();
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);

			if (!this.intersects(player))
				continue;

			this.powerup.onPickup(this, player);
		}
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		double posX = lastX + this.getPartialRenderX();
		double posY = lastY + this.getPartialRenderY();
		this.powerup.render(posX - this.getTileMapX() - cwidth / 2, posY - this.getTileMapY() - cheight / 2, mc, gui, mouseX, mouseY, partialTicks);
	}
}