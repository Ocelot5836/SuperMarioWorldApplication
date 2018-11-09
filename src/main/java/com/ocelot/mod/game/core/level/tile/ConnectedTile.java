package com.ocelot.mod.game.core.level.tile;

import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.TileMap;
import com.ocelot.mod.game.core.level.tile.property.PropertyString;
import com.ocelot.mod.game.core.level.tile.property.TileStateContainer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * Allows a tile's sprite to link with those around them that are the same tile.
 * 
 * @author Ocelot5836
 */
public class ConnectedTile extends BasicTile {

	/** The tile connections */
	public static final PropertyString CONNECTIONS = PropertyString.create("connections", "000000000");

	protected Sprite[] sprites;

	protected ConnectedTile(String unlocalizedName) {
		super(unlocalizedName);
	}

	public ConnectedTile(String unlocalizedName, ResourceLocation sheet, int u, int v, int textureWidth, int textureHeight) {
		this(unlocalizedName, sheet, u, v, textureWidth, textureHeight, 16);
	}

	public ConnectedTile(String unlocalizedName, ResourceLocation sheet, int u, int v, int textureWidth, int textureHeight, int tileSize) {
		super(unlocalizedName);
		this.sprites = new Sprite[13];
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				sprites[x + y * 3] = new Sprite(sheet, u + x * tileSize, v + y * tileSize, tileSize, tileSize, textureWidth, textureHeight);
			}
		}
		for (int i = 0; i < 4; i++) {
			sprites[i + 9] = new Sprite(sheet, u + (i % 2) * tileSize, v + (i / 2) * tileSize + tileSize * 3, tileSize, tileSize, textureWidth, textureHeight);
		}
	}

	@Override
	public TileStateContainer modifyContainer(int x, int y, TileMap tileMap, TileStateContainer container) {
		String value = container.getValue(CONNECTIONS);

		if (value != null && value.length() == 9) {
			StringBuilder connections = new StringBuilder();
			for (int i = 0; i < 9; i++) {
				int xx = i % 3;
				int yy = i / 3;
				int tileX = x + xx - 1;
				int tileY = y + yy - 1;
				connections.append(this.canConnectTo(tileX, tileY, tileMap) ? 1 : 0);
			}
			container.setValue(CONNECTIONS, connections.toString());
		}

		return container;
	}

	public boolean canConnectTo(int x, int y, TileMap tileMap) {
		Tile tile = tileMap.getTile(x, y);
		return tile == this || tile == Tile.VOID;
	}

	@Override
	public void render(double x, double y, TileMap tileMap, Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		// if (getValue(UP) != null && getValue(DOWN) != null && getValue(LEFT) != null && getValue(RIGHT) != null) {
		// boolean up = (boolean) getValue(UP);
		// boolean down = (boolean) getValue(DOWN);
		// boolean left = (boolean) getValue(LEFT);
		// boolean right = (boolean) getValue(RIGHT);
		//
		// if (up && down) {
		// if (left && right) {
		// sprites[4].render(x, y, 16, 16);
		// } else if (left) {
		// sprites[5].render(x, y, 16, 16);
		// } else if (right) {
		// sprites[3].render(x, y, 16, 16);
		// }
		// } else if (up) {
		// if (left && right) {
		// sprites[7].render(x, y, 16, 16);
		// } else if (left) {
		// sprites[8].render(x, y, 16, 16);
		// } else if (right) {
		// sprites[6].render(x, y, 16, 16);
		// }
		// } else if (down) {
		// if (left && right) {
		// sprites[1].render(x, y, 16, 16);
		// } else if (left) {
		// sprites[2].render(x, y, 16, 16);
		// } else if (right) {
		// sprites[0].render(x, y, 16, 16);
		// }
		// } else {
		// sprites[1].render(x, y, 16, 16);
		// }
		// } else {
		// sprites[1].render(x, y, 16, 16);
		// }

		String value = getValue(CONNECTIONS);
		if (value != null && value.length() == 9) {
			boolean upLeft = (value.charAt(0) + "").equalsIgnoreCase("1");
			boolean up = (value.charAt(1) + "").equalsIgnoreCase("1");
			boolean upRight = (value.charAt(2) + "").equalsIgnoreCase("1");
			boolean left = (value.charAt(3) + "").equalsIgnoreCase("1");
			boolean right = (value.charAt(5) + "").equalsIgnoreCase("1");
			boolean downLeft = (value.charAt(6) + "").equalsIgnoreCase("1");
			boolean down = (value.charAt(7) + "").equalsIgnoreCase("1");
			boolean downRight = (value.charAt(8) + "").equalsIgnoreCase("1");

			if (up && down) {
				if (left && right) {
					if (upLeft && upRight) {
						if (downLeft && downRight) {
							sprites[4].render(x, y, 16, 16);
						} else if (downLeft) {
							sprites[9].render(x, y, 16, 16);
						} else if (downRight) {
							sprites[10].render(x, y, 16, 16);
						} else {
							sprites[4].render(x, y, 16, 16);
						}
					} else if (upLeft) {
						sprites[11].render(x, y, 16, 16);
					} else if (upRight) {
						sprites[12].render(x, y, 16, 16);
					} else {
						sprites[4].render(x, y, 16, 16);
					}
				} else if (left) {
					sprites[5].render(x, y, 16, 16);
				} else if (right) {
					sprites[3].render(x, y, 16, 16);
				} else {
					sprites[4].render(x, y, 16, 16);
				}
			} else if (up) {
				if (left && right) {
					if (upLeft && upRight) {
						sprites[7].render(x, y, 16, 16);
					} else if (upLeft) {
						sprites[11].render(x, y, 16, 16);
					} else if (upRight) {
						sprites[12].render(x, y, 16, 16);
					} else {
						sprites[7].render(x, y, 16, 16);
					}
				} else if (left) {
					sprites[8].render(x, y, 16, 16);
				} else if (right) {
					sprites[6].render(x, y, 16, 16);
				} else {
					sprites[7].render(x, y, 16, 16);
				}
			} else if (down) {
				if (left && right) {
					sprites[1].render(x, y, 16, 16);
				} else if (left) {
					sprites[2].render(x, y, 16, 16);
				} else if (right) {
					sprites[0].render(x, y, 16, 16);
				} else {
					sprites[1].render(x, y, 16, 16);
				}
			} else {
				sprites[1].render(x, y, 16, 16);
			}
		} else {
			sprites[1].render(x, y, 16, 16);
		}
	}

	@Override
	public TileStateContainer createContainer() {
		return new TileStateContainer(this, CONNECTIONS);
	}
}