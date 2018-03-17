package com.ocelot.mod.game.main.gamestate.level;

import org.lwjgl.input.Keyboard;

import com.ocelot.mod.Mod;
import com.ocelot.mod.game.Backgrounds;
import com.ocelot.mod.game.GameStateManager;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.gameState.GameState;
import com.ocelot.mod.game.core.gfx.Background;
import com.ocelot.mod.game.core.level.LevelTemplate;
import com.ocelot.mod.game.core.level.tile.Tile;
import com.ocelot.mod.game.main.entity.Player;
import com.ocelot.mod.game.main.level.tile.InfoBoxTile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class YoshiHouseState extends GameState {

	private Player player;
	private Background background;

	private LevelTemplate template;

	public YoshiHouseState(GameStateManager gsm, GameTemplate game) {
		super(gsm, game);
	}

	@Override
	public void init() {
		template = new LevelTemplate(game, new ResourceLocation(Mod.MOD_ID, "levels/yoshihouse"));

		template.getLevel().getMap().setTween(1).setPosition(0, 58);

//		template.getLevel().add(player = new Player(game, 50, 100));

		if (template.getLevel().getMap().getTile(8, 8) == Tile.INFO_BOX) {
			template.getLevel().getMap().setValue(8, 8, InfoBoxTile.TEXT, InfoBoxTile.TextType.YOSHI_HOUSE);
		}

		background = new Background(Backgrounds.MUSHROOM_MOUNTAINS, 1, 0.5);
		background.setPosition(-Backgrounds.MUSHROOM_MOUNTAINS.getWidth() / 2, -120);
	}

	@Override
	public void update() {
		template.update();
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.scale(2, 2, 0);
		background.render();
		GlStateManager.popMatrix();
		template.render(gui, mc, mouseX, mouseY, partialTicks);
	}

	@Override
	public void onKeyPressed(int keyCode, char typedChar) {
		template.onKeyPressed(keyCode, typedChar);

		if (keyCode == Keyboard.KEY_T) {
			gsm.setState(gsm.MENU);
		}
	}

	@Override
	public void onKeyReleased(int keyCode, char typedChar) {
		template.onKeyReleased(keyCode, typedChar);
	}

	@Override
	public void onLoseFocus() {
		template.getLevel().onLoseFocus();
	}
}