package com.ocelot.mod.game.main.entity.powerup;

import com.ocelot.mod.audio.Sounds;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.EntityItem;
import com.ocelot.mod.game.core.entity.fx.TextFX;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.main.entity.item.EntityPowerup;
import com.ocelot.mod.game.main.entity.player.Player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class PowerupMushroom extends Powerup {

	private Sprite sprite;

	public PowerupMushroom() {
		super("mushroom", "mushroom");
		this.sprite = new Sprite(POWERUP_SHEET.getSubimage(0, 0, 16, 16));
	}

	@Override
	public void update() {
	}

	@Override
	public void render(Minecraft mc, Gui gui, double x, double y, int mouseX, int mouseY, float partialTicks) {
		sprite.render(x, y);
	}

	@Override
	public EntityItem createInstance(GameTemplate game, double x, double y) {
		return (EntityItem) new EntityPowerup(game, 0.5, 0, 16, 16, this).setPosition(x, y).setLastPosition(x, y);
	}

	@Override
	public void apply(Player player) {
		if (player.getProperties().isSmall()) {
			player.getProperties().setBig();
			player.getGame().playSound(Sounds.PLAYER_POWERUP_GAIN, 1.0F);
		}else {
			player.getGame().playSound(Sounds.PLAYER_COLLECT_POWERUP, 1.0F);
		}
		player.addScore(500);
	}
}