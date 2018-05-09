package com.ocelot.mod.game.main.entity.fx.particle;

import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.ISpawnerEntity;
import com.ocelot.mod.game.core.entity.Spawner;
import com.ocelot.mod.game.core.entity.fx.Particle;
import com.ocelot.mod.game.core.level.Level;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class SnowParticle extends Particle {

	private int rotation;

	public SnowParticle(GameTemplate game, double x, double y, int life) {
		super(game, x, y, life, life / 2, 2.0, 0.1, 0.0001, 0.25, DEFAULT_SPRITE);
		this.rotation = random.nextInt(360);
		this.ya = this.ya < 0 ? this.ya * -1 : this.ya;
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		double posX = lastX + this.getPartialRenderX();
		double posY = lastY + this.getPartialRenderY();

		GlStateManager.pushMatrix();
		GlStateManager.translate(posX - this.getTileMapX() - cwidth / 2, posY - this.getTileMapX() - cheight / 2, 0);
		GlStateManager.rotate(rotation, 0, 0, 1);
		sprite.render(0, 0);
		GlStateManager.popMatrix();
	}

	public static class Spawnable implements ISpawnerEntity {
		@Override
		public void create(GameTemplate game, Level level, Spawner spawner, double x, double y, Object... args) {
			if (args.length > 0) {
				level.add(new SnowParticle(game, x, y, parseInt(args[0])));
			}
		}
	}
}