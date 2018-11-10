package com.ocelot.mod.game.main.gamestate.level;

import org.lwjgl.input.Keyboard;

import com.ocelot.mod.SuperMarioWorld;
import com.ocelot.mod.game.GameStateManager;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.gameState.BasicLevel;
import com.ocelot.mod.game.core.level.LevelTemplate;
import com.ocelot.mod.game.main.entity.player.Player;
import com.ocelot.mod.game.main.gamestate.DebugSelectStateLevel;

import net.minecraft.util.ResourceLocation;

@DebugSelectStateLevel
public class TestLevelState extends BasicLevel {

	public TestLevelState(GameStateManager gsm, GameTemplate game) {
		super(gsm, game);
	}

	@Override
	public void load() {
		super.init(new LevelTemplate(this.game, new ResourceLocation(SuperMarioWorld.MOD_ID, "test")));
		this.level.getLevel().getMap().setTween(0.25);

		this.setPlayer(this.level.getLevel().getPlayers().get(0));
	}

	@Override
	public void onKeyPressed(int keyCode, char typedChar) {
		super.onKeyPressed(keyCode, typedChar);

		if (keyCode == Keyboard.KEY_T) {
			this.load();
		}

		if (keyCode == Keyboard.KEY_Z) {
			this.gsm.setState(GameStateManager.DEBUG_SELECT_LEVEL);
		}
	}
}