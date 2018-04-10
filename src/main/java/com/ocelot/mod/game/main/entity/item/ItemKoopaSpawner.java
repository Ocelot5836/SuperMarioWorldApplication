package com.ocelot.mod.game.main.entity.item;

import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.EntityItem;
import com.ocelot.mod.game.core.entity.IFileSummonable;
import com.ocelot.mod.game.core.entity.IItemCarriable;
import com.ocelot.mod.game.core.entity.ISpawnerEntity;
import com.ocelot.mod.game.core.entity.Spawner;
import com.ocelot.mod.game.core.entity.SummonException;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.Level;
import com.ocelot.mod.game.main.entity.enemy.Koopa;
import com.ocelot.mod.game.main.entity.enemy.Koopa.KoopaType;
import com.ocelot.mod.game.main.entity.player.Player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class ItemKoopaSpawner extends EntityItem implements IItemCarriable {

	private Sprite sprite = new Sprite(ItemCrayfish.SHEET.getSubimage(16, 0, 16, 16));

	public ItemKoopaSpawner(GameTemplate game) {
		this(game, 100, 50);
	}

	public ItemKoopaSpawner(GameTemplate game, double x, double y) {
		super(game, 0, 0, 0.015);
		this.setPosition(x, y);
		this.setSize(sprite.getWidth(), sprite.getHeight());
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		double posX = lastX + this.getPartialRenderX();
		double posY = lastY + this.getPartialRenderY();
		double tileMapX = tileMap.getLastX() + tileMap.getPartialRenderX();
		double tileMapY = tileMap.getLastY() + tileMap.getPartialRenderY();
		sprite.render(posX - tileMapX - cwidth / 2, posY - tileMapY - cheight / 2);
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
		level.add(new Koopa(game, KoopaType.BLUE, x, y - 6));
	}

	@Override
	public void onDrop(Player player) {
		setDefaultPlacing(player);
		level.add(new Koopa(game, KoopaType.RED, x, y - 6));
	}

	@Override
	public boolean canPickup(Player player) {
		return true;
	}

	public static class Spawnable implements ISpawnerEntity {
		@Override
		public void create(GameTemplate game, Level level, Spawner spawner, double x, double y, Object... args) throws SummonException {
			level.add(new ItemKoopaSpawner(game, x, y));
		}
	}

	public static class Summonable implements IFileSummonable {
		@Override
		public void summonFromFile(GameTemplate game, Level level, String[] args) throws SummonException {
			if (args.length > 1) {
				try {
					level.add(new ItemKoopaSpawner(game, Double.parseDouble(args[0]), Double.parseDouble(args[1])));
				} catch (Exception e) {
					throwSummonException("Can not summon a Koopa Spawner Item at non-numerical coords!");
				}
			} else {
				level.add(new ItemKoopaSpawner(game));
			}
		}
	}
}