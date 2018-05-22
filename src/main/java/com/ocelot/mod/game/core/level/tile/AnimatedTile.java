package com.ocelot.mod.game.core.level.tile;

import com.ocelot.mod.game.core.gfx.Animation;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.TileMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * A tile that has the basic capability to loop through an array of sprites to give it animation.
 * 
 * @author Ocelot5836
 */
public class AnimatedTile extends BasicTile {

	protected Animation animation;

	/**
	 * Creates a new animation and sets the sprites.
	 * 
	 * @param animationSwitchDelay
	 *            The time it takes in ticks to switch to the next frame
	 * @param sprites
	 *            The sprites in the order you want them to play
	 */
	public AnimatedTile(String unlocalizedName, int animationSwitchDelay, Sprite... sprites) {
		super(sprites[0], unlocalizedName);
		this.animation = new Animation();
		this.animation.setDelay(animationSwitchDelay);
		this.animation.setFrames(sprites);
	}

	@Override
	public void update() {
		this.animation.update();
	}

	@Override
	public void render(double x, double y, TileMap tileMap, Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		this.sprite = this.animation.getSprite();
		super.render(x, y, tileMap, gui, mc, mouseX, mouseY, partialTicks);
	}
}