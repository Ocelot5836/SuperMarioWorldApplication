package com.ocelot.mod.game.main.entity.fx.particle;

import java.awt.image.BufferedImage;

import com.ocelot.mod.SuperMarioWorld;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.fx.EntityFX;
import com.ocelot.mod.game.core.gfx.BufferedAnimation;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.lib.Lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class DustFX extends EntityFX {

	public static final BufferedImage DUST_SHEET = Lib.loadImage(new ResourceLocation(SuperMarioWorld.MOD_ID, "textures/effect/dust.png"));

	private Sprite sprite;
	private BufferedAnimation animation;

	private static BufferedImage[] sprites;

	public DustFX(GameTemplate game) {
		this(game, 0, 0);
	}

	public DustFX(GameTemplate game, double x, double y) {
		super(game);
		this.setPosition(x, y);
		this.setLastPosition(x, y);

		this.sprite = new Sprite();
		this.animation = new BufferedAnimation();
		if (sprites == null) {
			loadSprites();
		}
		this.animation.setDelay(50);
		this.animation.setFrames(sprites);
		loadSprites();
	}

	private void loadSprites() {
		sprites = new BufferedImage[3];
		for (int i = 0; i < sprites.length; i++) {
			sprites[i] = DUST_SHEET.getSubimage(i * 8, 0, 8, 8);
		}
	}

	@Override
	public void update() {
		super.update();

		animation.update();
		if (animation.hasPlayedOnce()) {
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