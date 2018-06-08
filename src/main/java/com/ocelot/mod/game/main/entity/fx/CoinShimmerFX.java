package com.ocelot.mod.game.main.entity.fx;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.ocelot.mod.Mod;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.Entity;
import com.ocelot.mod.game.core.entity.fx.EntityFX;
import com.ocelot.mod.game.core.gfx.BufferedAnimation;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.lib.Lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class CoinShimmerFX extends EntityFX {

	public static final BufferedImage SHIMMER_IMAGE = Lib.loadImage(new ResourceLocation(Mod.MOD_ID, "textures/effect/shimmer.png"));

	private static BufferedImage[] sprites;
	
	private Stopwatch timer;
	private BufferedAnimation animation;
	private Sprite sprite;

	public CoinShimmerFX(GameTemplate game) {
		this(game, 0, 0);
	}

	public CoinShimmerFX(GameTemplate game, EntityFX effect) {
		this(game, effect.getX() - effect.getWidth() / 2, effect.getY() - effect.getHeight() / 2);
	}

	public CoinShimmerFX(GameTemplate game, Entity entity) {
		this(game, entity.getX() - entity.getWidth() / 2, entity.getY() - entity.getHeight() / 2);
	}

	public CoinShimmerFX(GameTemplate game, double x, double y) {
		super(game);
		this.setPosition(x, y);
		this.timer = Stopwatch.createStarted();
		this.animation = new BufferedAnimation();
		this.sprite = new Sprite(SHIMMER_IMAGE);
		
		if(sprites == null) {
			loadSprites();	
		}
	}
	
	private void loadSprites() {
		
	}

	@Override
	public void update() {
		if (this.timer.elapsed(TimeUnit.MILLISECONDS) >= 100) {
			this.setDead();
		}
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		this.sprite.setData(animation.getImage());
		
		double posX = lastX + this.getPartialRenderX();
		double posY = lastY + this.getPartialRenderY();
		sprite.render(posX - this.getTileMapX() - sprite.getWidth() / 2, posY - this.getTileMapY() + cheight / 2 - sprite.getHeight() / 2);
	}
}