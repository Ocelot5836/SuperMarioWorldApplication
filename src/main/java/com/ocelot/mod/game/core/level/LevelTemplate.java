package com.ocelot.mod.game.core.level;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.ocelot.mod.Mod;
import com.ocelot.mod.game.Backgrounds;
import com.ocelot.mod.game.Game;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.Entity;
import com.ocelot.mod.game.core.entity.FileSummonException;
import com.ocelot.mod.game.core.entity.IFileSummonable;
import com.ocelot.mod.game.core.gfx.Background;
import com.ocelot.mod.game.core.gfx.Sprite;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * A basic level template.
 * 
 * @author Ocelot5836
 */
public class LevelTemplate {

	private static final Sprite[] BACKGROUND_BUILTIN_IMAGES = new Sprite[] { Backgrounds.GREEN_HILLS, Backgrounds.SNOW_HILLS, Backgrounds.JUNGLE_VINES, Backgrounds.MUSHROOM_MOUNTAINS, Backgrounds.GREEN_MOUNTAIN_TOPS, Backgrounds.WHITE_MOUNTAINS, Backgrounds.GREEN_MOUNTAINS, Backgrounds.CASTLE };
	private static final String[] BACKGROUND_BUILTIN_TYPES = new String[] { "GREEN_HILLS", "SNOW_HILLS", "JUNGLE_VINES", "MUSHROOM_MOUNTAINS", "GREEN_MOUNTAIN_TOPS", "WHITE_MOUNTAINS", "GREEN_MOUNTAINS", "CASTLE" };

	private GameTemplate game;
	private Level level;
	private ResourceLocation levelFolder;
	private List<Background> backgrounds;

	public LevelTemplate(GameTemplate game, ResourceLocation levelFolder) {
		this.game = game;
		this.levelFolder = levelFolder;
		this.backgrounds = new ArrayList<Background>();

		String mapFile = levelFolder + "/tiles.map";
		String backgroundsFile = levelFolder + "/backgrounds.info";
		String entitiesFile = levelFolder + "/entities.info";

		this.level = new Level(16, new ResourceLocation(mapFile));
		this.loadBackgrounds(new ResourceLocation(backgroundsFile));
		this.loadEntities(new ResourceLocation(entitiesFile), this.level);
	}

	private void loadBackgrounds(ResourceLocation backgroundsLocation) {
		try {
			InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(backgroundsLocation).getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			String line = br.readLine();
			while (line != null) {
				if (line.startsWith("#") || line.isEmpty()) {
					line = br.readLine();
					continue;
				}

				String[] data = line.split(";");
				if (data.length > 4) {
					String type = data[0];
					String[] types = type.split("_");
					if (types[0].equalsIgnoreCase("BuiltIn")) {
						if (types.length > 1) {
							types = type.split("_", 2);
							String backgroundType = types[1];
							for (int i = 0; i < BACKGROUND_BUILTIN_TYPES.length; i++) {
								if (backgroundType.equalsIgnoreCase(BACKGROUND_BUILTIN_TYPES[i])) {
									Sprite backgroundImage = BACKGROUND_BUILTIN_IMAGES[i];
									Background background = new Background(backgroundImage, Double.parseDouble(data[1]), Double.parseDouble(data[2]));
									background.setPosition(Double.parseDouble(data[3]), Double.parseDouble(data[4]));
									backgrounds.add(background);
									if (Mod.isDebug()) {
										Mod.logger().info("Loaded built-in background " + backgroundType + ". Data: " + data[1] + ", " + data[2] + ", " + data[3] + ", " + data[4]);
									}
								}
							}
						} else {
							Mod.logger().warn("Unknown type of parameters " + types);
							line = br.readLine();
							continue;
						}
					} else if (types[0].equalsIgnoreCase("ResourceLoc")) {
						if (types.length > 5) {
							String backgroundLoc = types[1];
							int u = Integer.parseInt(types[2]);
							int v = Integer.parseInt(types[3]);
							int width = Integer.parseInt(types[4]);
							int height = Integer.parseInt(types[5]);
							Sprite backgroundImage = new Sprite(new ResourceLocation(backgroundLoc), u, v, width, height);

							Background background = new Background(backgroundImage, Double.parseDouble(data[1]), Double.parseDouble(data[2]));
							background.setPosition(Double.parseDouble(data[3]), Double.parseDouble(data[4]));
							backgrounds.add(background);
							if (Mod.isDebug()) {
								Mod.logger().info("Loaded resource location background : ResourceLocation(" + backgroundLoc + ", " + u + ", " + v + ", " + width + ", " + height + "). Data: " + data[1] + ", " + data[2] + ", " + data[3] + ", " + data[4]);
							}
						} else {
							Mod.logger().warn("Unknown type of parameters " + types);
							line = br.readLine();
							continue;
						}
					} else {
						Mod.logger().warn("Unknown type of function " + types[0]);
						line = br.readLine();
						continue;
					}
				} else {
					Mod.logger().warn("Could not load background with data " + line + " -length- " + data.length);
					line = br.readLine();
					continue;
				}
				line = br.readLine();
				continue;
			}
		} catch (Exception e) {
			Game.stop(e, "Could not load backgrounds from " + backgroundsLocation + "!");
		}
	}

	private void loadEntities(ResourceLocation entitiesLocation, Level level) {
		try {
			InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(entitiesLocation).getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			String line = br.readLine();
			while (line != null) {
				if (line.startsWith("#") || line.isEmpty()) {
					line = br.readLine();
					continue;
				}

				String[] data = line.split(";");
				String[] args = new String[data.length - 1];

				for (int i = 0; i < args.length; i++) {
					args[i] = data[i + 1];
				}

				if (data.length > 0) {
					IFileSummonable summonable = Entity.getSummonable(data[0].trim());
					if (summonable != null) {
						try {
							summonable.summon(game, level, args);
						} catch (FileSummonException e) {
							Mod.logger().warn("Could not load entity " + data[0] + ". " + e.getMessage());
							line = br.readLine();
							continue;
						}
					} else {
						Mod.logger().warn("Could not load entity with key " + data[0]);
					}
				} else {
					Mod.logger().warn("Could not load entity with data " + line + " -length- " + data.length);
					line = br.readLine();
					continue;
				}
				line = br.readLine();
				continue;
			}
		} catch (Exception e) {
			Game.stop(e, "Could not load entities from " + entitiesLocation + "!");
		}
	}

	/**
	 * Updates the backgrounds and level.
	 */
	public void update() {
		for (Background bg : backgrounds) {
			bg.update();
		}
		level.update();
	}

	/**
	 * Renders the backgrounds and level to the screen.
	 * 
	 * @param gui
	 *            A gui instance
	 * @param mc
	 *            A minecraft instance
	 * @param mouseX
	 *            The x position of the mouse
	 * @param mouseY
	 *            The y position of the mouse
	 * @param partialTicks
	 *            The partial ticks
	 */
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		for (Background bg : backgrounds) {
			bg.render();
		}
		level.render(gui, mc, mouseX, mouseY, partialTicks);
	}

	/**
	 * Called when a key is pressed.
	 * 
	 * @param keyCode
	 *            The code pressed
	 * @param typedChar
	 *            The char typed
	 */
	public void onKeyPressed(int keyCode, char typedChar) {
		level.onKeyPressed(keyCode, typedChar);
	}

	/**
	 * Called when a key is released.
	 * 
	 * @param keyCode
	 *            The code pressed
	 * @param typedChar
	 *            The char typed
	 */
	public void onKeyReleased(int keyCode, char typedChar) {
		level.onKeyReleased(keyCode, typedChar);
	}

	public Level getLevel() {
		return level;
	}
}