package com.ocelot.mod.game.main.entity.item;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;

import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.EntityItem;
import com.ocelot.mod.game.core.level.tile.Tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class ItemCoin extends EntityItem {

	private StopWatch timer;

	public ItemCoin(GameTemplate game) {
		this(game, 0, 0);
	}

	public ItemCoin(GameTemplate game, double x, double y) {
		super(game, 0.05, 0);
		this.setSize(16, 16);
		this.setPosition(x, y);
		this.setLastPosition(x, y);
		this.timer = StopWatch.createStarted();
	}

	@Override
	public void update() {
		super.update();
		Tile.COIN.update();

		if (this.timer.getTime(TimeUnit.MILLISECONDS) >= 10000) {
			this.setDead();
		}
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		double posX = lastX + this.getPartialRenderX();
		double posY = lastY + this.getPartialRenderY();
		Tile.COIN.render(posX - this.getTileMapX() - cwidth / 2, posY - this.getTileMapY() - cheight / 2, tileMap, gui, mc, mouseX, mouseY, partialTicks);
	}
}