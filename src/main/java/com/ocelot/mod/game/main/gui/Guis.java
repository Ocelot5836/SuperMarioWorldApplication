package com.ocelot.mod.game.main.gui;

import com.ocelot.mod.game.core.gfx.gui.GuiHandler;
import com.ocelot.mod.game.core.gfx.gui.MarioGui;
import com.ocelot.mod.game.core.level.tile.Tile;
import com.ocelot.mod.game.main.entity.player.Player;

public class Guis implements GuiHandler {

	public static final Guis INSTANCE = new Guis();

	public static final int YOSHI_TEXT_BUBBLE = 0;
	public static final int PLAYER_HELLO = 1;

	@SuppressWarnings("unused")
	@Override
	public MarioGui openGui(int id, Player player) {
		double x = player.getX();
		double y = player.getY();
		Tile tile = player.getLevel().getMap().getTile((int) x / player.getLevel().getMap().getTileSize(), (int) y / player.getLevel().getMap().getTileSize());
		
		if (id == YOSHI_TEXT_BUBBLE)
			return new MarioGuiYoshiTextBubble();
		if (id == PLAYER_HELLO)
			return new MarioGuiPlayerHello();

		return null;
	}
}