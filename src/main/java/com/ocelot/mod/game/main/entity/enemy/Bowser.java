package com.ocelot.mod.game.main.entity.enemy;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.ocelot.mod.SuperMarioWorld;
import com.ocelot.mod.game.core.EnumDirection;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.MarioDamageSource;
import com.ocelot.mod.game.core.entity.Entity;
import com.ocelot.mod.game.core.entity.IDamagable;
import com.ocelot.mod.game.core.gfx.Animation;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.main.entity.ai.AIBowser;
import com.ocelot.mod.lib.Lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class Bowser extends Enemy {

	public static final BufferedImage BOWSER_SHEET = Lib.loadImage(new ResourceLocation(SuperMarioWorld.MOD_ID, "textures/entity/enemy/bowser.png"));

	private static List<BufferedImage[]> sprites;

	private Animation<Sprite> animation;
	private Sprite sprite;

	public Bowser(GameTemplate game) {
		this(game, 0, 0);
	}

	public Bowser(GameTemplate game, double x, double y) {
		super(game, 100);
		this.setPosition(x, y);
		this.setSize(32, 32);

		this.animation = new Animation<Sprite>();
		this.sprite = new Sprite();

		if (sprites == null) {
			this.sprites = new ArrayList<BufferedImage[]>();
			this.loadSprites();
		}
	}

	private void loadSprites() {

	}

	@Override
	public void initAI() {
		this.registerAI(new AIBowser());
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		double posX = lastX + this.getPartialRenderX();
		double posY = lastY + this.getPartialRenderY();
		sprite.render(posX - this.getTileMapX() - cwidth / 2 - 2, posY - this.getTileMapY() + cheight / 2 - sprite.getHeight());
	}

	@Override
	public boolean dealDamage(Entity entity, EnumDirection sideHit) {
		if (entity instanceof IDamagable) {
			((IDamagable) entity).takeDamage(this, MarioDamageSource.BOWSER, sideHit, false);
		}
		return false;
	}
}