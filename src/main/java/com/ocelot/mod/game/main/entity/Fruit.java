package com.ocelot.mod.game.main.entity;

import com.ocelot.mod.SuperMarioWorld;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.Entity;
import com.ocelot.mod.game.core.entity.SummonException;
import com.ocelot.mod.game.core.entity.summonable.FileSummonableEntity;
import com.ocelot.mod.game.core.entity.summonable.IFileSummonable;
import com.ocelot.mod.game.core.gfx.Animation;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.Level;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

@FileSummonableEntity(Fruit.Summonable.class)
public class Fruit extends Entity {

	public static final ResourceLocation TEXTURE = new ResourceLocation(SuperMarioWorld.MOD_ID, "textures/entity/fruit.png");

	private Animation<Sprite> animation;

	public Fruit(GameTemplate game) {
		this(game, 0, 0);
	}

	public Fruit(GameTemplate game, double x, double y) {
		super(game);
		this.setSize(16, 16);
		this.setPosition(x, y);
		this.animation = new Animation();

		Sprite[] sprites = new Sprite[4];
		for (int i = 0; i < sprites.length; i++) {
			sprites[i] = new Sprite(TEXTURE, i * 16, 0, 16, 16, 64, 16);
		}
		animation.setFrames(sprites);
		animation.setDelay(100);
	}

	@Override
	public void update() {
		super.update();

		animation.update();
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		super.render(gui, mc, mouseX, mouseY, partialTicks);

		animation.get().render(this.x - this.getTileMapX() - this.cwidth / 2, this.y - this.getTileMapY() - this.cheight / 2);
	}

	public static class Summonable implements IFileSummonable {
		@Override
		public void summonFromFile(GameTemplate game, Level level, String[] args) throws SummonException {
			if (args.length > 1) {
				try {
					level.add(new Fruit(game, Double.parseDouble(args[0]), Double.parseDouble(args[1])));
				} catch (Exception e) {
					throwSummonException(I18n.format("exception." + SuperMarioWorld.MOD_ID + ".fruit.summon.numerical"));
				}
			} else {
				level.add(new Fruit(game));
			}
		}

		@Override
		public String getRegistryName() {
			return "Fruit";
		}
	}
}