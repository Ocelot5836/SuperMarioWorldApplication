package com.ocelot.mod.game.main.entity.fx;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.ocelot.mod.SuperMarioWorld;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.Entity;
import com.ocelot.mod.game.core.entity.fx.EntityFX;
import com.ocelot.mod.game.core.gfx.Animation;
import com.ocelot.mod.game.core.gfx.Sprite;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class CoinShimmerFX extends EntityFX {

	public static final ResourceLocation SHIMMER_IMAGE = new ResourceLocation(SuperMarioWorld.MOD_ID, "textures/effect/shimmer.png");

	private static BufferedImage[] sprites;
	
	private Stopwatch timer;
	private Animation<Sprite> animation;

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
		this.animation = new Animation<Sprite>();
		
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
		double posX = lastX + this.getPartialRenderX();
		double posY = lastY + this.getPartialRenderY();
		this.animation.get().render(posX - this.getTileMapX() - this.animation.get().getWidth() / 2, posY - this.getTileMapY() + cheight / 2 - this.animation.get().getHeight() / 2);
	}
}