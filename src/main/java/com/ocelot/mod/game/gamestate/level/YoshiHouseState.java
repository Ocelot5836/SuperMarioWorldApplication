package com.ocelot.mod.game.gamestate.level;

import com.ocelot.mod.Mod;
import com.ocelot.mod.game.Backgrounds;
import com.ocelot.mod.game.GameStateManager;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.gameState.GameState;
import com.ocelot.mod.game.core.gfx.Background;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.Level;
import com.ocelot.mod.game.core.level.tile.Tile;
import com.ocelot.mod.game.entity.Player;
import com.ocelot.mod.game.entity.misc.Fruit;
import com.ocelot.mod.game.level.tile.InfoBoxTile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class YoshiHouseState extends GameState {

	private Level level;
	private Player player;
	private Background background;
	private Background background1;

	public YoshiHouseState(GameStateManager gsm, GameTemplate game) {
		super(gsm, game);
	}

	@Override
	public void init() {
		level = new Level(16, new ResourceLocation(Mod.MOD_ID, "maps/yoshi_house.map"));
		level.getMap().setPosition(0, 58);

		level.add(new Fruit(game, 48, 63));
		level.add(new Fruit(game, 32, 47));
		level.add(new Fruit(game, 97, 48));
		level.add(new Fruit(game, 176, 47));
		level.add(new Fruit(game, 208, 63));
		level.add(new Fruit(game, 144, 63));
		level.add(new Fruit(game, 112, 79));
		level.add(player = new Player(game, 50, 100));

		if (level.getMap().getTile(8, 8) == Tile.INFO_BOX) {
			level.getMap().setValue(8, 8, InfoBoxTile.TEXT, InfoBoxTile.TextType.YOSHI_HOUSE);
		}

		background = new Background(Backgrounds.MUSHROOM_MOUNTAINS, 1, 0.5);
		background.setPosition(-Backgrounds.MUSHROOM_MOUNTAINS.getWidth() / 2, -120);
		background1 = new Background(new Sprite(new ResourceLocation(Mod.MOD_ID, "textures/level/yoshi_house/background1.png"), 0, 0, 256, 161), 1, 1);
		background1.setPosition(0, -42);
	}

	@Override
	public void update() {
		level.update();
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.scale(2, 2, 0);
		background.render();
		GlStateManager.popMatrix();
		background1.render();
		level.render(gui, mc, mouseX, mouseY, partialTicks);
	}

	@Override
	public void onKeyPressed(int keyCode, char typedChar) {
		level.onKeyPressed(keyCode, typedChar);
	}

	@Override
	public void onKeyReleased(int keyCode, char typedChar) {
		level.onKeyReleased(keyCode, typedChar);
	}

	@Override
	public void onLoseFocus() {
		player.onLoseFocus();
	}
}