package com.ocelot.mod.game.core.level.tile;

import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.TileMap;

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
public class AdvancedConnectedTile extends ConnectedTile {

	public AdvancedConnectedTile(String unlocalizedName, ResourceLocation sheet, int u, int v, int textureWidth, int textureHeight) {
		this(unlocalizedName, sheet, u, v, textureWidth, textureHeight, 16);
	}

	public AdvancedConnectedTile(String unlocalizedName, ResourceLocation sheet, int u, int v, int textureWidth, int textureHeight, int tileSize) {
		super(unlocalizedName);
		this.sprites = new Sprite[48];

		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 12; x++) {
				this.sprites[x + y * 12] = new Sprite(sheet, u + x * tileSize, v + y * tileSize, tileSize, tileSize, textureWidth, textureHeight);
			}
		}
	}

	@Override
	public void render(double x, double y, TileMap tileMap, Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
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
					if (upLeft && upRight && downLeft && downRight) {
						sprites[26].render(x, y, 16, 16);
					} else if (upLeft && upRight && downLeft && downRight) {
						sprites[37].render(x, y, 16, 16);
					} else if (upRight && downLeft && downRight) {
						sprites[45].render(x, y, 16, 16);
					} else if (upLeft && downLeft && downRight) {
						sprites[44].render(x, y, 16, 16);
					} else if (upLeft && upRight && downRight) {
						sprites[33].render(x, y, 16, 16);
					} else if (upLeft && upRight && downLeft) {
						sprites[32].render(x, y, 16, 16);
					} else if (upLeft && upRight) {
						sprites[11].render(x, y, 16, 16);
					} else if (upRight && downRight) {
						sprites[23].render(x, y, 16, 16);
					} else if (downLeft && downRight) {
						sprites[22].render(x, y, 16, 16);
					} else if (upLeft && downLeft) {
						sprites[10].render(x, y, 16, 16);
					} else if (upLeft && downRight) {
						sprites[35].render(x, y, 16, 16);
					} else if (downLeft && upRight) {
						sprites[34].render(x, y, 16, 16);
					} else if (upLeft) {
						sprites[20].render(x, y, 16, 16);
					} else if (upRight) {
						sprites[8].render(x, y, 16, 16);
					} else if (downLeft) {
						sprites[21].render(x, y, 16, 16);
					} else if (downRight) {
						sprites[9].render(x, y, 16, 16);
					} else {
						sprites[46].render(x, y, 16, 16);
					}
				} else if (left) {
					if (upLeft && downLeft) {
						sprites[27].render(x, y, 16, 16);
					} else if (upLeft) {
						sprites[41].render(x, y, 16, 16);
					} else if (downLeft) {
						sprites[43].render(x, y, 16, 16);
					} else {
						sprites[19].render(x, y, 16, 16);
					}
				} else if (right) {
					if (upRight && downRight) {
						sprites[25].render(x, y, 16, 16);
					} else if (upRight) {
						sprites[30].render(x, y, 16, 16);
					} else if (downRight) {
						sprites[28].render(x, y, 16, 16);
					} else {
						sprites[6].render(x, y, 16, 16);
					}
				} else {
					sprites[24].render(x, y, 16, 16);
				}
			} else if (up) {
				if (left && right) {
					if (upLeft && upRight) {
						sprites[38].render(x, y, 16, 16);
					} else if (upLeft) {
						sprites[42].render(x, y, 16, 16);
					} else if (upRight) {
						sprites[40].render(x, y, 16, 16);
					} else {
						sprites[18].render(x, y, 16, 16);
					}
				} else if (left) {
					if (upLeft) {
						sprites[39].render(x, y, 16, 16);
					} else {
						sprites[17].render(x, y, 16, 16);
					}
				} else if (right) {
					if (upRight) {
						sprites[37].render(x, y, 16, 16);
					} else {
						sprites[16].render(x, y, 16, 16);
					}
				} else {
					sprites[36].render(x, y, 16, 16);
				}
			} else if (down) {
				if (left && right) {
					if (downLeft && downRight) {
						sprites[14].render(x, y, 16, 16);
					} else if (downLeft) {
						sprites[29].render(x, y, 16, 16);
					} else if (downRight) {
						sprites[31].render(x, y, 16, 16);
					} else {
						sprites[7].render(x, y, 16, 16);
					}
				} else if (left) {
					if (downLeft) {
						sprites[15].render(x, y, 16, 16);
					} else {
						sprites[5].render(x, y, 16, 16);
					}
				} else if (right) {
					if (downRight) {
						sprites[13].render(x, y, 16, 16);
					} else {
						sprites[4].render(x, y, 16, 16);
					}
				} else {
					sprites[12].render(x, y, 16, 16);
				}
			} else {
				if (left && right) {
					sprites[2].render(x, y, 16, 16);
				} else if (left) {
					sprites[3].render(x, y, 16, 16);
				} else if (right) {
					sprites[1].render(x, y, 16, 16);
				} else {
					sprites[0].render(x, y, 16, 16);
				}
			}
		} else {
			sprites[0].render(x, y, 16, 16);
		}
	}
}