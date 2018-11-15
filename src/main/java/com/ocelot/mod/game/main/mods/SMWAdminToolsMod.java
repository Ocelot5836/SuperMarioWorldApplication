package com.ocelot.mod.game.main.mods;

import com.ocelot.api.mod.GameTemplateListener;
import com.ocelot.api.mod.MarioPlugin;
import com.ocelot.api.mod.SMWMod;
import com.ocelot.mod.game.GameStateManager;
import com.ocelot.mod.game.core.GameTemplate;

@SMWMod
public class SMWAdminToolsMod implements MarioPlugin, GameTemplateListener {

	@Override
	public void register() {
	}
	
	@Override
	public void registerListeners(GameTemplate game) {
		game.addListener(this);
	}

	@Override
	public void registerGameStates(GameStateManager gsm) {

	}
}