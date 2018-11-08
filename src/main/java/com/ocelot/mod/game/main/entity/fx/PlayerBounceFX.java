package com.ocelot.mod.game.main.entity.fx;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.ocelot.mod.SuperMarioWorld;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.Entity;
import com.ocelot.mod.game.core.entity.fx.EntityFX;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.lib.Lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class PlayerBounceFX extends EntityFX {

	public static final BufferedImage BOUNCE_IMAGE = Lib.loadImage(new ResourceLocation(SuperMarioWorld.MOD_ID, "textures/effect/bounce.png"));

	private Stopwatch timer;
	private Sprite sprite;

	public PlayerBounceFX(GameTemplate game) {
		this(game, 0, 0);
	}

	public PlayerBounceFX(GameTemplate game, EntityFX effect) {
		this(game, effect.getX() - effect.getWidth() / 2, effect.getY() - effect.getHeight() / 2);
	}

	public PlayerBounceFX(GameTemplate game, Entity entity) {
		this(game, entity.getX() - entity.getWidth() / 2, entity.getY() - entity.getHeight() / 2);
	}

	public PlayerBounceFX(GameTemplate game, double x, double y) {
		super(game);
		this.setPosition(x, y);
		this.lastX = x;
		this.lastY = y;
		this.sprite = new Sprite(BOUNCE_IMAGE);

		this.timer = Stopwatch.createStarted();
	}

	@Override
	public void update() {
		if (this.timer.elapsed(TimeUnit.MILLISECONDS) >= 100) {
			this.setDead();
		}
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		double posX = lastX + this.getPartialRenderX();
		double posY = lastY + this.getPartialRenderY();
		this.sprite.render(posX - this.getTileMapX() - sprite.getWidth() / 2, posY - this.getTileMapY() - sprite.getHeight() / 2);
	}
}