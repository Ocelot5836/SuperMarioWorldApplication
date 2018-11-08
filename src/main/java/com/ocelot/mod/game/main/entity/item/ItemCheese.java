package com.ocelot.mod.game.main.entity.item;

import com.ocelot.mod.SuperMarioWorld;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.EntityItem;
import com.ocelot.mod.game.core.entity.IItemCarriable;
import com.ocelot.mod.game.core.entity.ISpawnerEntity;
import com.ocelot.mod.game.core.entity.Spawner;
import com.ocelot.mod.game.core.entity.SummonException;
import com.ocelot.mod.game.core.entity.summonable.FileSummonableEntity;
import com.ocelot.mod.game.core.entity.summonable.IFileSummonable;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.Level;
import com.ocelot.mod.game.main.entity.player.Player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;

@FileSummonableEntity(ItemCheese.Summonable.class)
public class ItemCheese extends EntityItem implements IItemCarriable {

	private Sprite sprite = new Sprite(ItemCrayfish.SHEET.getSubimage(16, 0, 16, 16));

	public ItemCheese(GameTemplate game) {
		this(game, 100, 50);
	}

	public ItemCheese(GameTemplate game, double x, double y) {
		super(game, 0, 0);
		this.setPosition(x, y);
		this.setSize(sprite.getWidth(), sprite.getHeight());
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		double posX = lastX + this.getPartialRenderX();
		double posY = lastY + this.getPartialRenderY();
		sprite.render(posX - this.getTileMapX() - cwidth / 2, posY - this.getTileMapY() - cheight / 2);
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
		return true;
	}

	public static class Spawnable implements ISpawnerEntity {
		@Override
		public void create(GameTemplate game, Level level, Spawner spawner, double x, double y, Object... args) throws SummonException {
			level.add(new ItemCheese(game, x, y));
		}
	}

	public static class Summonable implements IFileSummonable {
		@Override
		public void summonFromFile(GameTemplate game, Level level, String[] args) throws SummonException {
			if (args.length > 1) {
				try {
					level.add(new ItemCheese(game, Double.parseDouble(args[0]), Double.parseDouble(args[1])));
				} catch (Exception e) {
					throwSummonException(I18n.format("exception." + SuperMarioWorld.MOD_ID + ".item.summon.numerical", this.getRegistryName()));
				}
			} else {
				level.add(new ItemCheese(game));
			}
		}

		@Override
		public String getRegistryName() {
			return "ItemCheese";
		}
	}
}