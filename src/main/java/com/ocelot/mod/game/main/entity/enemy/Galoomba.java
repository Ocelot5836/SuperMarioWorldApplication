package com.ocelot.mod.game.main.entity.enemy;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.EntityItem;
import com.ocelot.mod.game.core.entity.IItemCarriable;
import com.ocelot.mod.game.core.gfx.BufferedAnimation;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.main.entity.ai.AIBasicWalker;
import com.ocelot.mod.game.main.entity.player.Player;
import com.ocelot.mod.lib.Lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class Galoomba extends Enemy {

	private int currentAction;
	private Sprite sprite;
	private BufferedAnimation animation;

	private static int[] delays = {};
	private static List<BufferedImage[]> sprites;

	public static final int TURNING_SIDE = 0;
	public static final int WALKING_SIDE = 1;
	public static final int WALKING_WINGED_SIDE = 2;
	public static final int JUMPING = 3;
	public static final int FALLING = 4;
	public static final int IDLE = 5;

	public Galoomba(GameTemplate game) {
		this(game, 0, 0);
	}

	public Galoomba(GameTemplate game, double x, double y) {
		super(game);
		this.setPosition(x, y);
		this.lastX = x;
		this.lastY = y;
		this.setSize(16, 16);
		this.sprite = new Sprite();
		this.animation = new BufferedAnimation();

		if (sprites == null) {
			this.sprites = new ArrayList<BufferedImage[]>();
			this.loadSprites();
		}
	}

	private void loadSprites() {
	}

	@Override
	public void initAI() {
		super.registerAI(new AIBasicWalker());
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (right)
			facingRight = true;
		if (left)
			facingRight = false;

		sprite.setData(animation.getImage());
		if (!facingRight) {
			sprite = Lib.flipHorizontal(sprite);
		}

		double posX = lastX + this.getPartialRenderX();
		double posY = lastY + this.getPartialRenderY();
		sprite.render(posX - this.getTileMapX() - cwidth / 2, posY - this.getTileMapY() + cheight / 2 - sprite.getHeight());
	}

	public static class Item extends EntityItem implements IItemCarriable {
		
		private int timer;

		public Item(GameTemplate game) {
			super(game);
		}

		@Override
		public void update() {
			super.update();

			timer++;
			if (timer >= 100) {
				this.setDead();
				level.add(new Galoomba(game));
			}
		}

		@Override
		public void onHeldUpdate(Player player) {
		}

		@Override
		public void onPickup(Player player) {
		}

		@Override
		public void onThrow(Player player, ThrowingType type) {
			setDefaultThrowing(player, type);
		}

		@Override
		public void onDrop(Player player) {
			setDefaultPlacing(player);
		}

		@Override
		public boolean canPickup(Player player) {
			return true;
		}

		@Override
		public boolean canHold(Player player) {
			return timer < 98;
		}
	}
}