package com.ocelot.mod.game.core.level;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ocelot.api.utils.JsonHelper;
import com.ocelot.mod.SuperMarioWorld;
import com.ocelot.mod.game.Backgrounds;
import com.ocelot.mod.game.Game;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.SummonException;
import com.ocelot.mod.game.core.entity.summonable.IFileSummonable;
import com.ocelot.mod.game.core.entity.summonable.SummonableEntityRegistry;
import com.ocelot.mod.game.core.gfx.Background;
import com.ocelot.mod.game.core.gfx.Sprite;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.TextureMap;
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

	private static final Sprite[][] BACKGROUND_BUILTIN_IMAGES = new Sprite[][] { { Backgrounds.GREEN_HILLS }, { Backgrounds.SNOW_HILLS }, { Backgrounds.JUNGLE_VINES }, { Backgrounds.MUSHROOM_MOUNTAINS }, { Backgrounds.GREEN_MOUNTAIN_TOPS }, { Backgrounds.WHITE_MOUNTAINS }, { Backgrounds.GREEN_MOUNTAINS }, { Backgrounds.CASTLE }, Backgrounds.CAVES, Backgrounds.ICY_CAVES, Backgrounds.UNDERWATER, Backgrounds.GHOST_HOUSE, Backgrounds.CASTLE_ANIMATED, Backgrounds.STARRY_NIGHT };
	private static final String[] BACKGROUND_BUILTIN_TYPES = new String[] { "GREEN_HILLS", "SNOW_HILLS", "JUNGLE_VINES", "MUSHROOM_MOUNTAINS", "GREEN_MOUNTAIN_TOPS", "WHITE_MOUNTAINS", "GREEN_MOUNTAINS", "CASTLE", "CAVES", "ICY_CAVES", "UNDERWATER", "GHOST_HOUSE", "CASTLE_ANIMATED", "STARRY_NIGHT" };

	private LevelProperties properties;

	private GameTemplate game;
	private Level level;
	private List<Background> backgrounds;

	public LevelTemplate(GameTemplate game, ResourceLocation levelFolder) {
		this.game = game;
		this.backgrounds = new ArrayList<Background>();

		String worldInfoFile = levelFolder + "/world.json";
		String mapFile = levelFolder + "/tiles.map";
		String backgroundsFile = levelFolder + "/backgrounds.json";
		String entitiesFile = levelFolder + "/entities.osmw";

		SuperMarioWorld.logger().info("Loading Level \'" + levelFolder + "\'");
		this.level = new Level(game, 16, new ResourceLocation(mapFile));
		this.loadWorldInfo(new ResourceLocation(worldInfoFile));
		this.loadBackgrounds(new ResourceLocation(backgroundsFile));
		this.loadEntities(new ResourceLocation(entitiesFile), this.level);
	}

	private static Sprite createSprite(JsonElement element) {
		if (element.isJsonObject()) {
			JsonObject obj = element.getAsJsonObject();

			ResourceLocation texture = TextureMap.LOCATION_MISSING_TEXTURE;
			double u = 0;
			double v = 0;
			double width = 256;
			double height = 256;
			double textureWidth = 256;
			double textureHeight = 256;

			if (obj.has("texture")) {
				try {
					texture = new ResourceLocation(JsonHelper.getString(obj, "texture"));
				} catch (Exception e) {
					SuperMarioWorld.logger().warn("\'texture\' value in JSON must be a string");
				}
			}

			if (obj.has("u")) {
				try {
					u = JsonHelper.getNumber(obj, "u").doubleValue();
				} catch (Exception e) {
					SuperMarioWorld.logger().warn("\'u\' value in JSON must be a number");
				}
			}

			if (obj.has("v")) {
				try {
					v = JsonHelper.getNumber(obj, "v").doubleValue();
				} catch (Exception e) {
					SuperMarioWorld.logger().warn("\'v\' value in JSON must be a number");
				}
			}

			if (obj.has("width")) {
				try {
					width = JsonHelper.getNumber(obj, "width").doubleValue();
				} catch (Exception e) {
					SuperMarioWorld.logger().warn("\'width\' value in JSON must be a number");
				}
			}

			if (obj.has("height")) {
				try {
					u = JsonHelper.getNumber(obj, "height").doubleValue();
				} catch (Exception e) {
					SuperMarioWorld.logger().warn("\'height\' value in JSON must be a number");
				}
			}

			if (obj.has("textureWidth")) {
				try {
					textureWidth = JsonHelper.getNumber(obj, "textureWidth").doubleValue();
				} catch (Exception e) {
					SuperMarioWorld.logger().warn("\'textureWidth\' value in JSON must be a number");
				}
			}

			if (obj.has("textureHeight")) {
				try {
					textureWidth = JsonHelper.getNumber(obj, "textureHeight").doubleValue();
				} catch (Exception e) {
					SuperMarioWorld.logger().warn("\'textureHeight\' value in JSON must be a number");
				}
			}

			return new Sprite(texture, u, v, width, height, textureWidth, textureHeight);
		}
		return null;
	}

	private void loadWorldInfo(ResourceLocation worldInfoLocation) {
		String jsonLocation = "/assets/" + worldInfoLocation.getResourceDomain() + "/levels/" + worldInfoLocation.getResourcePath();

		try {
			SuperMarioWorld.logger().info("Loading world info JSON from \'" + jsonLocation + "\'");
			JsonElement json = new JsonParser().parse(IOUtils.toString(LevelTemplate.class.getResourceAsStream(jsonLocation), Charset.defaultCharset()));

			if (!json.isJsonObject())
				throw new RuntimeException("World info JSON at \'" + jsonLocation + "\' must be a JSON object!");

			JsonObject worldInfoJson = json.getAsJsonObject();

			int time = 150;
			ResourceLocation music = null;
			ResourceLocation musicFast = null;
			int endLoop = 0;

			if (worldInfoJson.has("time")) {
				try {
					time = JsonHelper.getNumber(worldInfoJson, "time").intValue();
				} catch (Exception e) {
					SuperMarioWorld.logger().warn("\'time\' value in JSON must be a number");
				}
			}

			if (worldInfoJson.has("music")) {
				try {
					music = new ResourceLocation(JsonHelper.getString(worldInfoJson, "music"));
				} catch (Exception e) {
					SuperMarioWorld.logger().warn("\'music\' value in JSON must be a string");
				}
			}

			if (worldInfoJson.has("music_fast")) {
				try {
					musicFast = new ResourceLocation(JsonHelper.getString(worldInfoJson, "music_fast"));
				} catch (Exception e) {
					SuperMarioWorld.logger().warn("\'music_fast\' value in JSON must be a string");
				}
			}

			if (worldInfoJson.has("loop")) {
				try {
					endLoop = JsonHelper.getNumber(worldInfoJson, "loop").intValue();
				} catch (Exception e) {
					SuperMarioWorld.logger().warn("\'loop\' value in JSON must be a number");
				}
			}

			this.properties = new LevelProperties(time, music, musicFast == null ? music : musicFast, endLoop);
			SuperMarioWorld.logger().info("Loaded world info JSON from \'" + jsonLocation + "\' successfully");
		} catch (Exception e) {
			Game.stop(e, "Could not load world info JSON from \'" + jsonLocation + "\'");
		}
	}

	private void loadBackgrounds(ResourceLocation backgroundsLocation) {
		String jsonLocation = "/assets/" + backgroundsLocation.getResourceDomain() + "/levels/" + backgroundsLocation.getResourcePath();

		try {
			SuperMarioWorld.logger().info("Loading backgrounds JSON from \'" + jsonLocation + "\'");
			JsonElement json = new JsonParser().parse(IOUtils.toString(LevelTemplate.class.getResourceAsStream(jsonLocation), Charset.defaultCharset()));

			if (!json.isJsonArray())
				throw new RuntimeException("Backgrounds JSON at \'" + jsonLocation + "\' must be a JSON array!");

			JsonArray backgroundsJson = json.getAsJsonArray();

			for (JsonElement backgroundJson : backgroundsJson) {
				try {
					if (!backgroundJson.isJsonObject())
						throw new RuntimeException("Background must be a JSON object!");

					JsonObject backgroundJsonObject = backgroundJson.getAsJsonObject();
					JsonObject dataObj = null;
					String type = null;
					double moveScale = 1;
					double heightScale = 0.5;
					double startX = 0;
					double startY = 0;

					try {
						type = JsonHelper.getString(backgroundJsonObject, "type");
					} catch (Exception e) {
						throw new RuntimeException("Background must have a type string");
					}

					try {
						dataObj = JsonHelper.getObj(backgroundJsonObject, "data");
					} catch (Exception e) {
						throw new RuntimeException("Background must have a data type obj");
					}

					if (dataObj.has("moveScale")) {
						try {
							moveScale = JsonHelper.getNumber(dataObj, "moveScale").doubleValue();
						} catch (Exception e) {
							throw new RuntimeException("Background type \'moveScale\' must be a string");
						}
					}

					if (dataObj.has("heightScale")) {
						try {
							heightScale = JsonHelper.getNumber(dataObj, "heightScale").doubleValue();
						} catch (Exception e) {
							throw new RuntimeException("Background type \'heightScale\' must be a string");
						}
					}

					if (dataObj.has("startX")) {
						try {
							startX = JsonHelper.getNumber(dataObj, "startX").doubleValue();
						} catch (Exception e) {
							throw new RuntimeException("Background type \'startX\' must be a string");
						}
					}

					if (dataObj.has("startY")) {
						try {
							startY = JsonHelper.getNumber(dataObj, "startY").doubleValue();
						} catch (Exception e) {
							throw new RuntimeException("Background type \'startY\' must be a string");
						}
					}

					switch (type) {
					case "BuiltIn":
						String name = null;

						try {
							name = JsonHelper.getString(dataObj, "name");
						} catch (Exception e) {
							throw new RuntimeException("Background must have a name string");
						}

						for (int i = 0; i < BACKGROUND_BUILTIN_TYPES.length; i++) {
							if (name.equalsIgnoreCase(BACKGROUND_BUILTIN_TYPES[i])) {
								Sprite[] backgroundImage = BACKGROUND_BUILTIN_IMAGES[i];
								Background background = new Background(backgroundImage, 100, moveScale, heightScale);
								background.setStartingPosition(startX, startY);
								backgrounds.add(background);
							}
						}
						break;
					case "ResourceLocation":
						List<Sprite> sprites = new ArrayList<Sprite>();
						int delay = -2;

						if (dataObj.has("delay")) {
							try {
								delay = JsonHelper.getNumber(dataObj, "delay").intValue();
								if(delay < 0)
									delay = -1;
							} catch (Exception e) {
								throw new RuntimeException("Background type \'delay\' must be a number");
							}
						}

						if (dataObj.has("sprites")) {
							if (dataObj.get("sprites").isJsonArray()) {
								JsonArray spritesArray = dataObj.get("sprites").getAsJsonArray();
								for (JsonElement spriteElement : spritesArray) {
									Sprite sprite = createSprite(spriteElement);
									if (sprite != null) {
										sprites.add(sprite);
									} else {
										SuperMarioWorld.logger().warn("Could not load sprite background JSON: \'" + spriteElement.toString() + "\', SKIPPING");
									}
								}
							} else {
								throw new RuntimeException("Background type \'sprites\' must be an array");
							}
						} else {
							throw new RuntimeException("Background must have at least one sprite");
						}

						Background background = new Background(sprites.toArray(new Sprite[0]), delay == -2 ? sprites.size() > 1 ? 0 : -1 : delay, moveScale, heightScale);
						background.setStartingPosition(startX, startY);
						backgrounds.add(background);
						break;
					default:
						break;
					}
				} catch (Exception e) {
					SuperMarioWorld.logger().warn("Could not load background from JSON: \'" + backgroundJson.toString() + "\', SKIPPING", e);
				}
			}
			SuperMarioWorld.logger().info("Loaded backgrounds JSON from \'" + jsonLocation + "\' successfully");
		} catch (Exception e) {
			Game.stop(e, "Could not load backgrounds JSON from \'" + jsonLocation + "\'");
		}
	}

	private void loadEntities(ResourceLocation entitiesLocation, Level level) {
		String fileLocation = "/assets/" + entitiesLocation.getResourceDomain() + "/levels/" + entitiesLocation.getResourcePath();

		try {
			SuperMarioWorld.logger().info("Loading entities OSMW from \'" + fileLocation + "\'");
			InputStream is = LevelTemplate.class.getResourceAsStream(fileLocation);
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
					IFileSummonable summonable = SummonableEntityRegistry.getSummonable(data[0].trim());
					if (summonable != null) {
						try {
							summonable.summonFromFile(game, level, args);
						} catch (SummonException e) {
							SuperMarioWorld.logger().warn("Could not load entity " + data[0] + ". " + e.getMessage());
							line = br.readLine();
							continue;
						}
					} else {
						SuperMarioWorld.logger().warn("Could not load entity with key " + data[0]);
					}
				} else {
					SuperMarioWorld.logger().warn("Could not load entity with data " + line + " -length- " + data.length);
					line = br.readLine();
					continue;
				}
				line = br.readLine();
				continue;
			}
			br.close();
			SuperMarioWorld.logger().info("Loaded entities OSMW from \'" + fileLocation + "\' successfully");
		} catch (Exception e) {
			Game.stop(e, "Could not load entities from \'" + fileLocation + "\'!");
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

	/**
	 * Called when the mouse is pressed.
	 * 
	 * @param mouseButton
	 *            The button pressed
	 * @param mouseX
	 *            The x position of the mouse
	 * @param mouseY
	 *            The y position of the mouse
	 */
	public void onMousePressed(int mouseButton, int mouseX, int mouseY) {
		level.onMousePressed(mouseButton, mouseX, mouseY);
	}

	/**
	 * Called when the mouse is released.
	 * 
	 * @param mouseButton
	 *            The button released
	 * @param mouseX
	 *            The x position of the mouse
	 * @param mouseY
	 *            The y position of the mouse
	 */
	public void onMouseReleased(int mouseButton, int mouseX, int mouseY) {
		level.onMouseReleased(mouseButton, mouseX, mouseY);
	}

	/**
	 * Called when the mouse is scrolled.
	 * 
	 * @param direction
	 *            The direction the mouse was scrolled
	 * @param mouseX
	 *            The x position of the mouse
	 * @param mouseY
	 *            The y position of the mouse
	 */
	public void onMouseScrolled(boolean direction, int mouseX, int mouseY) {
		level.onMouseScrolled(direction, mouseX, mouseY);
	}

	public Level getLevel() {
		return level;
	}

	public LevelProperties getProperties() {
		return properties;
	}
}