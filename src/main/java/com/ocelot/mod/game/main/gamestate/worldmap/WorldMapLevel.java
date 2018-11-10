package com.ocelot.mod.game.main.gamestate.worldmap;

import com.ocelot.mod.game.core.gfx.Animation;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.lib.Lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class WorldMapLevel implements IWorldMapPoint {

	private int levelState;

	private double x;
	private double y;

	private Animation<Sprite> animation;

	private WorldMapLevel(int levelState, double x, double y, Sprite sprite) {
		this(levelState, x, y, -1, Lib.asArray(sprite));
	}

	private WorldMapLevel(int levelState, double x, double y, long delay, Sprite... frames) {
		this.levelState = levelState;
		this.x = x;
		this.y = y;

		this.animation = new Animation();
		this.animation.setDelay(delay);
		this.animation.setFrames(frames);
	}

	@Override
	public void update() {
		this.animation.update();
	}

	@Override
	public void render(double width, double height, Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		this.animation.get().render(this.x, this.y, width, height);
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