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
import com.ocelot.mod.game.core.entity.IFileSummonable;
import com.ocelot.mod.game.core.entity.SummonException;
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

	private LevelProperties properties;

	private GameTemplate game;
	private Level level;
	private ResourceLocation levelFolder;
	private List<Background> backgrounds;

	public LevelTemplate(GameTemplate game, ResourceLocation levelFolder) {
		this.game = game;
		this.levelFolder = levelFolder;
		this.backgrounds = new ArrayList<Background>();

		String worldInfoFile = levelFolder + "/world.osmw";
		String mapFile = levelFolder + "/tiles.map";
		String backgroundsFile = levelFolder + "/backgrounds.osmw";
		String entitiesFile = levelFolder + "/entities.osmw";

		this.level = new Level(16, new ResourceLocation(mapFile));
		this.loadWorldInfo(levelFolder, new ResourceLocation(worldInfoFile));
		this.loadBackgrounds(new ResourceLocation(backgroundsFile));
		this.loadEntities(new ResourceLocation(entitiesFile), this.level);
	}

	private void loadWorldInfo(ResourceLocation levelFolder, ResourceLocation worldInfoLocation) {
		try {
			InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(worldInfoLocation).getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			int time = 150;
			ResourceLocation music = null;
			ResourceLocation musicFast = null;
			int startLoop = 0;
			int endLoop = -1;

			String line = br.readLine();
			while (line != null) {
				if (line.startsWith("#") || line.isEmpty()) {
					line = br.readLine();
					continue;
				}

				String[] data = line.split(";");
				if (data.length > 1) {
					String type = data[0];
					if (type.equalsIgnoreCase("time")) {
						try {
							int value = Integer.parseInt(data[1]);
							if (value < 0) {
								Mod.logger().warn("Time value was less than zero. Time will instead use 150s.");
								value = 150;
							} else if (value > 999) {
								Mod.logger().warn("Time max value is 999. Any time above will be lowered down to 999.");
								value = 999;
							} else {
								time = value;
							}
						} catch (NumberFormatException e) {
							Mod.logger().warn("Could not load time since it it not a numerical value");
						}
					} else if (type.equalsIgnoreCase("music")) {
						try {
							music = new ResourceLocation(data[1]);
						} catch (Exception e) {
							Mod.logger().warn("Could not load music \'" + music + "\'");
						}
					} else if (type.equalsIgnoreCase("music_fast")) {
						try {
							musicFast = new ResourceLocation(data[1]);
						} catch (Exception e) {
							Mod.logger().warn("Could not load music \'" + musicFast + "-music_fast\'");
						}
					} else if (type.equalsIgnoreCase("loop")) {
						if (data.length > 2) {
							try {
								int value = Integer.parseInt(data[1]);
								int value1 = Integer.parseInt(data[2]);
								startLoop = value;
								endLoop = value1;
							} catch (NumberFormatException e) {
								Mod.logger().warn("Could not load " + type + " since it is not an integer");
								line = br.readLine();
								continue;
							}
						} else {
							Mod.logger().warn("Could not load " + type + " since it requires two integer parameters");
							line = br.readLine();
							continue;
						}
					} else {
						Mod.logger().warn("Unknown type of function " + type);
						line = br.readLine();
						continue;
					}
				} else {
					Mod.logger().warn("Could not load world info with data " + line + " -length- " + data.length);
					line = br.readLine();
					continue;
				}
				line = br.readLine();
				continue;
			}

			this.properties = new LevelProperties(time, music, musicFast == null ? music : musicFast, startLoop, endLoop);
		} catch (Exception e) {
			Game.stop(e, "Could not load world info from " + worldInfoLocation + "!");
		}
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
							summonable.summonFromFile(game, level, args);
						} catch (SummonException e) {
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
			bg.setPosition(level.getMap().getX(), level.getMap().getY());
		}
		level.update();
		properties.update();
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

	public LevelProperties getProperties() {
		return properties;
	}
}