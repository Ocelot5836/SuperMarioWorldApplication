package com.ocelot.mod.game.main.gamestate.level;

import java.lang.annotation.Annotation;

import org.lwjgl.input.Keyboard;

import com.ocelot.mod.Mod;
import com.ocelot.mod.audio.Jukebox;
import com.ocelot.mod.game.Game;
import com.ocelot.mod.game.GameStateManager;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.gameState.BasicLevel;
import com.ocelot.mod.game.core.level.LevelTemplate;
import com.ocelot.mod.game.core.level.tile.Tile;
import com.ocelot.mod.game.main.entity.enemy.Galoomba;
import com.ocelot.mod.game.main.entity.enemy.Koopa;
import com.ocelot.mod.game.main.entity.enemy.Koopa.KoopaType;
import com.ocelot.mod.game.main.entity.item.ItemCheese;
import com.ocelot.mod.game.main.entity.item.ItemCracker;
import com.ocelot.mod.game.main.entity.item.ItemCrayfish;
import com.ocelot.mod.game.main.entity.item.ItemPSwitch;
import com.ocelot.mod.game.main.entity.powerup.Powerup;
import com.ocelot.mod.game.main.gamestate.DebugSelectStateLevel;
import com.ocelot.mod.game.main.tile.TileInfoBox;
import com.ocelot.mod.game.main.tile.TileQuestionBlock;

import net.minecraft.util.ResourceLocation;

@DebugSelectStateLevel
public class DemoLevelState extends BasicLevel {

	public DemoLevelState(GameStateManager gsm, GameTemplate game) {
		super(gsm, game);
	}

	@Override
	public void load() {
		super.init(new LevelTemplate(game, new ResourceLocation(Mod.MOD_ID, "levels/demo")));
		level.getLevel().getMap().setTween(0.25);

		for (int i = 0; i < 10; i++) {
			level.getLevel().add(new Koopa(game, KoopaType.values()[i % 4], 140 + i * 18, 100));
		}
		
		level.getLevel().add(new Galoomba(game, 425, 50));

		level.getLevel().add(new ItemCrayfish(game, 400, 50));
		level.getLevel().add(new ItemCheese(game, 420, 50));
		level.getLevel().add(new ItemCracker(game, 440, 50));
		level.getLevel().add(new ItemPSwitch(game, 460, 50));

		if (level.getLevel().getMap().getTile(35, 2) == Tile.INFO_BOX) {
			level.getLevel().getMap().setValue(35, 2, TileInfoBox.TEXT, TileInfoBox.TextType.MR_CRAYFISH_HELLO);
		}
		TileQuestionBlock.setItem(level.getLevel().getMap(), 36, 2, Powerup.FEATHER);

		setPlayer(level.getLevel().getPlayers().get(0));
	}

	@Override
	public void update() {
		super.update();

		this.level.getLevel().getMap().setPosition(player.getX() - Game.WIDTH / 2, 0);
	}

	@Override
	public void onKeyPressed(int keyCode, char typedChar) {
		super.onKeyPressed(keyCode, typedChar);

		if (keyCode == Keyboard.KEY_T) {
			load();
		}
		
		if (keyCode == Keyboard.KEY_Z) {
			gsm.setState(GameStateManager.DEBUG_SELECT_LEVEL);
		}
	}
}