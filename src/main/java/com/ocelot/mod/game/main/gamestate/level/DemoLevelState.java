package com.ocelot.mod.game.main.gamestate.level;

import org.lwjgl.input.Keyboard;

import com.ocelot.mod.SuperMarioWorld;
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
		super.init(new LevelTemplate(this.getGame(), new ResourceLocation(SuperMarioWorld.MOD_ID, "demo")));
		this.getLevelTemplate().getLevel().getMap().setTween(0.25);

		for (int i = 0; i < 10; i++) {
			this.getLevelTemplate().getLevel().add(new Koopa(this.getGame(), KoopaType.values()[i % 4], 140 + i * 18, 100));
		}

		this.getLevelTemplate().getLevel().add(new Galoomba(this.getGame(), 425, 50));

		this.getLevelTemplate().getLevel().add(new ItemCrayfish(this.getGame(), 400, 50));
		this.getLevelTemplate().getLevel().add(new ItemCheese(this.getGame(), 420, 50));
		this.getLevelTemplate().getLevel().add(new ItemCracker(this.getGame(), 440, 50));
		this.getLevelTemplate().getLevel().add(new ItemPSwitch(this.getGame(), 460, 50));

		if (this.getLevelTemplate().getLevel().getMap().getTile(35, 2) == Tile.INFO_BOX) {
			this.getLevelTemplate().getLevel().getMap().setValue(35, 2, TileInfoBox.TEXT, TileInfoBox.TextType.MR_CRAYFISH_HELLO);
		}
		TileQuestionBlock.setItem(this.getLevelTemplate().getLevel().getMap(), 36, 2, Powerup.FEATHER);

		setPlayer(this.getLevelTemplate().getLevel().getPlayers().get(0));
	}

	@Override
	public void onKeyPressed(int keyCode, char typedChar) {
		super.onKeyPressed(keyCode, typedChar);

		if (keyCode == Keyboard.KEY_T) {
			this.load();
		}

		if (keyCode == Keyboard.KEY_Z) {
			this.getGsm().setState(GameStateManager.DEBUG_SELECT_LEVEL);
		}
	}
}