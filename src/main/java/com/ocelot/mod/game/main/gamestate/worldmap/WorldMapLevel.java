package com.ocelot.mod.game.main.gamestate.worldmap;

import java.awt.image.BufferedImage;

import com.ocelot.mod.game.core.gfx.BufferedAnimation;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.lib.Lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class WorldMapLevel implements IWorldMapPoint {

	private int levelState;

	private double x;
	private double y;

	private Sprite sprite;
	private BufferedAnimation animation;

	private WorldMapLevel(int levelState, double x, double y, BufferedImage sprite) {
		this(levelState, x, y, -1, Lib.asArray(sprite));
	}

	private WorldMapLevel(int levelState, double x, double y, long delay, BufferedImage... frames) {
		this.levelState = levelState;
		this.x = x;
		this.y = y;

		this.sprite = new Sprite();
		this.animation = new BufferedAnimation();
		this.animation.setDelay(delay);
		this.animation.setFrames(frames);
	}

	@Override
	public void update() {
		this.animation.update();
	}

	@Override
	public void render(double width, double height, Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		this.sprite.setData(this.animation.getImage());
		this.sprite.render(this.x, this.y, width, height);
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}
}