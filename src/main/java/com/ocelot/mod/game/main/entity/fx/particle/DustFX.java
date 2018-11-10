package com.ocelot.mod.game.main.entity.fx.particle;

import com.ocelot.mod.SuperMarioWorld;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.fx.EntityFX;
import com.ocelot.mod.game.core.gfx.Animation;
import com.ocelot.mod.game.core.gfx.Sprite;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class DustFX extends EntityFX {

	public static final ResourceLocation DUST_SHEET = new ResourceLocation(SuperMarioWorld.MOD_ID, "textures/effect/dust.png");

	private Animation<Sprite> animation;

	private static Sprite[] sprites;

	public DustFX(GameTemplate game) {
		this(game, 0, 0);
	}

	public DustFX(GameTemplate game, double x, double y) {
		super(game);
		this.setPosition(x, y);
		this.setLastPosition(x, y);

		this.animation = new Animation<Sprite>();
		if (sprites == null) {
			loadSprites();
		}
		this.animation.setDelay(50);
		this.animation.setFrames(sprites);
		loadSprites();
	}

	private static void loadSprites() {
		sprites = new Sprite[3];
		for (int i = 0; i < sprites.length; i++) {
			sprites[i] = new Sprite(DUST_SHEET, i * 8, 0, 8, 8, 24, 8);
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
		double posX = lastX + this.getPartialRenderX();
		double posY = lastY + this.getPartialRenderY();
		this.animation.get().render(posX - this.getTileMapX() - this.animation.get().getWidth() / 2, posY - this.getTileMapY() + cheight / 2 - this.animation.get().getHeight() / 2);
	}
}