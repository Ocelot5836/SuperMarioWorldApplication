package com.ocelot.mod.game.main.entity.fx;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.fx.EntityFX;
import com.ocelot.mod.game.core.level.tile.Tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class CoinFX extends EntityFX {

	private double weight;
	private double motion;

	private Stopwatch timer;

	public CoinFX(GameTemplate game) {
		this(game, 0, 0);
	}

	public CoinFX(GameTemplate game, double x, double y) {
		super(game);
		this.setSize(16, 16);
		this.setPosition(x, y);
		this.setLastPosition(x, y);
		this.motion = -10;
		this.weight = 2;

		this.timer = Stopwatch.createStarted();
	}

	@Override
	public void update() {
		super.update();
		Tile.COIN.update();
		if (this.timer.elapsed(TimeUnit.MILLISECONDS) >= 525) {
			this.setDead();
		}
		this.y += motion;
		this.motion += weight;
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		double posX = lastX + this.getPartialRenderX();
		double posY = lastY + this.getPartialRenderY();
		Tile.COIN.render(posX - this.getTileMapX() - cwidth / 2, posY - this.getTileMapY() - cheight / 2, tileMap, gui, mc, mouseX, mouseY, partialTicks);
	}
}