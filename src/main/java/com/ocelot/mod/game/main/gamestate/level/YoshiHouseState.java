package com.ocelot.mod.game.main.gamestate.level;

import org.lwjgl.input.Keyboard;

import com.ocelot.mod.Mod;
import com.ocelot.mod.audio.Jukebox;
import com.ocelot.mod.game.Backgrounds;
import com.ocelot.mod.game.GameStateManager;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.gameState.GameState;
import com.ocelot.mod.game.core.gfx.Background;
import com.ocelot.mod.game.core.gfx.gui.MarioGui;
import com.ocelot.mod.game.core.level.LevelTemplate;
import com.ocelot.mod.game.core.level.tile.Tile;
import com.ocelot.mod.game.main.entity.enemy.Bowser;
import com.ocelot.mod.game.main.gamestate.DebugSelectStateLevel;
import com.ocelot.mod.game.main.gui.GuiOverlay;
import com.ocelot.mod.game.main.tile.InfoBoxTile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

@DebugSelectStateLevel
public class YoshiHouseState extends GameState {

	private LevelTemplate template;
	private MarioGui overlay;

	public YoshiHouseState(GameStateManager gsm, GameTemplate game) {
		super(gsm, game);
	}

	@Override
	public void load() {
		template = new LevelTemplate(game, new ResourceLocation(Mod.MOD_ID, "levels/yoshihouse"));

		template.getLevel().getMap().setTween(1).setPosition(0, 58);
		template.getLevel().add(new Bowser(game, 50, 50));

		if (template.getLevel().getMap().getTile(8, 8) == Tile.INFO_BOX) {
			template.getLevel().getMap().setValue(8, 8, InfoBoxTile.TEXT, InfoBoxTile.TextType.YOSHI_HOUSE);
		}

		overlay = new GuiOverlay(template).setSizeAndWorld(game, template.getLevel().getPlayers().get(0));

		template.getProperties().playMusic();
	}

	@Override
	public void update() {
		template.update();
		overlay.update();
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		template.render(gui, mc, mouseX, mouseY, partialTicks);
		overlay.render(gui, mouseX, mouseY, partialTicks);
	}

	@Override
	public void onKeyPressed(int keyCode, char typedChar) {
		template.onKeyPressed(keyCode, typedChar);
		overlay.onKeyPressed(keyCode, typedChar);

		if (keyCode == Keyboard.KEY_T) {
			load();
		}
	}

	@Override
	public void onKeyReleased(int keyCode, char typedChar) {
		template.onKeyReleased(keyCode, typedChar);
		overlay.onKeyReleased(keyCode, typedChar);
	}

	@Override
	public void onLoseFocus() {
		template.getLevel().onLoseFocus();
	}
}