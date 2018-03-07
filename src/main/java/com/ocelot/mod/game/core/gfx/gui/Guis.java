package com.ocelot.mod.game.core.gfx.gui;

import com.ocelot.mod.game.core.level.tile.Tile;
import com.ocelot.mod.game.entity.Player;
import com.ocelot.mod.game.gui.MarioGuiYoshiTextBubble;

public class Guis implements IGuiHandler {

	public static final Guis INSTANCE = new Guis();

	public static final int YOSHI_TEXT_BUBBLE = 0;
	
	@Override
	public MarioGui openClient(int id, Player player) {
		double x = player.getX();
		double y = player.getY();
		Tile tile = player.getLevel().getMap().getTile((int) x / player.getLevel().getMap().getTileSize(), (int) y / player.getLevel().getMap().getTileSize());

		if(id == YOSHI_TEXT_BUBBLE)
			return new MarioGuiYoshiTextBubble();
		
		return null;
	}

	@Override
	public MarioGui openServer(int id, Player player) {
		double x = player.getX();
		double y = player.getY();
		Tile tile = player.getLevel().getMap().getTile((int) x / player.getLevel().getMap().getTileSize(), (int) y / player.getLevel().getMap().getTileSize());

		return null;
	}
}