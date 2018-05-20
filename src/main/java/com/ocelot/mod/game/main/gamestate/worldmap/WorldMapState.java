package com.ocelot.mod.game.main.gamestate.worldmap;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.ocelot.mod.Mod;
import com.ocelot.mod.game.Game;
import com.ocelot.mod.game.GameStateManager;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.gameState.GameState;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.Level;
import com.ocelot.mod.game.main.entity.player.PlayerMap;
import com.ocelot.mod.game.main.gamestate.DebugSelectStateLevel;
import com.ocelot.mod.lib.Lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

@DebugSelectStateLevel
public class WorldMapState extends GameState {

	public static final BufferedImage WORLD_MAP_ICONS = Lib.loadImage(new ResourceLocation(Mod.MOD_ID, "textures/map.png"));

	private ResourceLocation mapLocation;
	private Level level;
	private List<WorldMapIcon> icons;
	private PlayerMap player;

	public WorldMapState(GameStateManager gsm, GameTemplate game, ResourceLocation mapLocation) {
		super(gsm, game);
		this.mapLocation = mapLocation;
	}

	@Override
	public void init() {
		icons = new ArrayList<WorldMapIcon>();
		icons.add(new WorldMapIcon(this, new Sprite(WORLD_MAP_ICONS.getSubimage(0, 0, 16, 16)), Game.WIDTH / 2, Game.HEIGHT / 2, () -> {

		}).addChildren(new WorldMapIcon(this, new Sprite(WORLD_MAP_ICONS.getSubimage(16, 0, 16, 16)), Game.WIDTH / 4, Game.HEIGHT / 4, () -> {

		})));

		level = new Level(16, new ResourceLocation(Mod.MOD_ID, "maps/empty.map"));
		level.add(player = new PlayerMap(game, this, Game.WIDTH / 2, Game.HEIGHT / 2));
	}

	@Override
	public void update() {
		level.update();
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		for (int i = 0; i < icons.size(); i++) {
			icons.get(i).render();
		}
		level.render(gui, mc, mouseX, mouseY, partialTicks);
	}

	@Override
	public void onKeyPressed(int keyCode, char typedChar) {
		if (keyCode == Keyboard.KEY_Y) {
			init();
		}

		level.onKeyPressed(keyCode, typedChar);
	}

	@Override
	public void onKeyReleased(int keyCode, char typedChar) {
		level.onKeyReleased(keyCode, typedChar);
	}

	@Override
	public void save(NBTTagCompound nbtTag) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbtTag.setDouble("playerX", this.player.getX());
		nbtTag.setDouble("playerY", this.player.getY());
		nbtTag.setTag("WorldMapState", nbt);
	}

	@Override
	public void load(NBTTagCompound nbtTag) {
		NBTTagCompound nbt = nbtTag.getCompoundTag("WorldMapState");
		if (nbt != null) {
			this.player.setPosition(nbt.getDouble("playerX"), nbt.getDouble("playerY"));
		}
	}

	public WorldMapState addIcons(WorldMapIcon... icons) {
		for (int i = 0; i < icons.length; i++) {
			this.icons.add(icons[i]);
		}
		return this;
	}

	@Nullable
	public WorldMapIcon getIcon(double x, double y) {
		for (WorldMapIcon icon : icons) {
			if (icon.getX() >= x && icon.getX() < x + icon.getWidth() && icon.getY() >= y && icon.getY() < y + icon.getHeight()) {
				return icon;
			}
		}
		return null;
	}
}