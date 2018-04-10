package com.ocelot.mod.game.core.entity.fx;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.ocelot.mod.game.core.GameTemplate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * Represents a textual effect that is rendered on the screen.
 * 
 * @author Ocelot5836
 */
public class TextFX extends EntityFX {

	private Stopwatch timer;
	private double xSpeed, ySpeed;

	private String text;
	private int color;
	private double life;

	public TextFX(GameTemplate game, double x, double y, double xSpeed, double ySpeed, String text, int color, double life) {
		super(game);
		this.setPosition(x, y);
		this.lastX = x;
		this.lastY = y;
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.text = text;
		this.color = color;
		this.life = life;

		this.timer = Stopwatch.createStarted();
	}

	@Override
	public void update() {
		super.update();

		if (timer.elapsed(TimeUnit.SECONDS) >= life) {
			setDead();
		}

		setPosition(x + xSpeed * 3, y + ySpeed * 3);
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		double posX = lastX + this.getPartialRenderX();
		double posY = lastY + this.getPartialRenderY();
		double tileMapX = tileMap.getLastX() + tileMap.getPartialRenderX();
		double tileMapY = tileMap.getLastY() + tileMap.getPartialRenderY();
		mc.fontRenderer.drawString(this.text, (int) (posX - tileMapX - mc.fontRenderer.getStringWidth(this.text) / 2), (int) (posY - tileMapY - 8), color, false);
		GlStateManager.color(1, 1, 1, 1);
	}
}