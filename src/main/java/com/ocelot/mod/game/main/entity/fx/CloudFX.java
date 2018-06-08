package com.ocelot.mod.game.main.entity.fx;

import java.awt.image.BufferedImage;

import com.ocelot.mod.Mod;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.fx.EntityFX;
import com.ocelot.mod.game.core.gfx.BufferedAnimation;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.lib.Lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class CloudFX extends EntityFX {

	public static final BufferedImage CLOUD_SHEET = Lib.loadImage(new ResourceLocation(Mod.MOD_ID, "textures/effect/cloud.png"));

	private Sprite sprite;
	private BufferedAnimation animation;

	private static BufferedImage[] sprites;

	public CloudFX(GameTemplate game) {
		super(game);
	}

	public CloudFX(GameTemplate game, double x, double y) {
		super(game);
		this.setPosition(x, y);
		this.setLastPosition(x, y);

		this.sprite = new Sprite();
		this.animation = new BufferedAnimation();
		if (sprites == null) {
			loadSprites();
		}
		this.animation.setDelay(5);
		this.animation.setFrames(sprites);
		loadSprites();
	}

	private void loadSprites() {
		sprites = new BufferedImage[7];

		for (int i = 0; i < 4; i++) {
			sprites[i] = CLOUD_SHEET.getSubimage(48 - 16 * i, 0, 16, 16);
		}

		sprites[4] = CLOUD_SHEET.getSubimage(0, 0, 16, 16);

		for (int i = 4; i < sprites.length; i++) {
			sprites[i] = CLOUD_SHEET.getSubimage(16 * (i - 4), 0, 16, 16);
		}
	}

	@Override
	public void update() {
		super.update();

		animation.update();
		
		if(animation.hasPlayedOnce()) {
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