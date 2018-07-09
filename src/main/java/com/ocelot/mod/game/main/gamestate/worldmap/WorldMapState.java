package com.ocelot.mod.game.main.gamestate.worldmap;

import java.awt.image.BufferedImage;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import com.ocelot.mod.Mod;
import com.ocelot.mod.game.GameStateManager;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.gameState.GameState;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.main.gamestate.DebugSelectStateLevel;
import com.ocelot.mod.lib.Lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

@DebugSelectStateLevel
public class WorldMapState extends GameState {

	public static final BufferedImage WORLD_MAP_ICONS = Lib.loadImage(new ResourceLocation(Mod.MOD_ID, "textures/map.png"));
	public static final BufferedImage WORLD_MAP_PATHS = Lib.loadImage(new ResourceLocation(Mod.MOD_ID, "textures/path.png"));

	private WorldMap map;

	public WorldMapState(GameStateManager gsm, GameTemplate game) {
		super(gsm, game);
		this.map = new WorldMap(game);
	}

	@Override
	public void load() {
		this.map.clear();
		this.map.mapPath(new Sprite(WORLD_MAP_PATHS.getSubimage(0, 16, 145, 128)), new Vector2f(4 * 16, 4 * 16), new Vector2f(5 * 16, 4 * 16), new Vector2f(8 * 16, 1 * 16), new Vector2f(8 * 16, 8 * 16), new Vector2f(5 * 16, 8 * 16));
		this.map.mapPath(new Sprite(WORLD_MAP_PATHS.getSubimage(0, 0, 16, 16)), new Vector2f(2 * 16, 2 * 16), new Vector2f(3 * 16, 3 * 16));
	}

	@Override
	public void update() {
		this.map.update();
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		this.map.render(gui, mc, mouseX, mouseY, partialTicks);
	}

	@Override
	public void onKeyPressed(int keyCode, char typedChar) {
		this.map.onKeyPressed(keyCode, typedChar);

		if (keyCode == Keyboard.KEY_Z) {
			this.gsm.setState(GameStateManager.DEBUG_SELECT_LEVEL);
		}

		if (keyCode == Keyboard.KEY_T) {
			load();
		}
	}

	@Override
	public void onKeyReleased(int keyCode, char typedChar) {
		this.map.onKeyReleased(keyCode, typedChar);
	}

	@Override
	public void onMousePressed(int mouseButton, int mouseX, int mouseY) {
		this.map.onMousePressed(mouseButton, mouseX, mouseY);
	}

	@Override
	public void onMouseReleased(int mouseButton, int mouseX, int mouseY) {
		this.map.onMouseReleased(mouseButton, mouseX, mouseY);
	}

	@Override
	public void onMouseScrolled(boolean direction, int mouseX, int mouseY) {
		this.map.onMouseScrolled(direction, mouseX, mouseY);
	}
}