package com.ocelot.mod.application;

import java.awt.Color;
import java.awt.Desktop;

import com.mrcrayfish.device.api.app.Dialog;
import com.mrcrayfish.device.api.app.Layout.Background;
import com.mrcrayfish.device.api.app.component.Button;
import com.mrcrayfish.device.api.app.component.Text;
import com.mrcrayfish.device.api.app.listener.ClickListener;
import com.ocelot.mod.Mod;
import com.ocelot.mod.game.Game;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

/**
 * An alternate, fixed version of the default {@link Message}.
 * 
 * @author Ocelot5836
 * @author MrCrayfish
 */
public class DialogCrashLog extends Dialog {

	private String messageText = "";

	private ClickListener positiveListener;
	private Button buttonPositive;
	private Button buttonSaveStackTrace;

	public DialogCrashLog(String messageText) {
		this.setTitle("Crash Report");
		this.messageText = messageText;
		defaultLayout.width = 250;
	}

	@Override
	public void init() {
		super.init();

		int lines = Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(messageText, getWidth() - 10).size();
		defaultLayout.height += (lines - 1) * 10 + 20;

		super.init();

		defaultLayout.setBackground(new Background() {
			@Override
			public void render(Gui gui, Minecraft mc, int x, int y, int width, int height, int mouseX, int mouseY, boolean windowActive) {
				Gui.drawRect(x, y, x + width, y + height, Color.LIGHT_GRAY.getRGB());
			}
		});

		Text message = new Text(messageText, 5, 5, getWidth() - 10);
		this.addComponent(message);

		buttonPositive = new Button(getWidth() - 41, getHeight() - 20, 36, 16, "Close");
		buttonPositive.setClickListener((mouseX, mouseY, mouseButton) -> {
			if (positiveListener != null) {
				positiveListener.onClick(mouseX, mouseY, mouseButton);
			}
			close();
		});
		this.addComponent(buttonPositive);

		buttonSaveStackTrace = new Button(5, getHeight() - 20, "Log");
		buttonSaveStackTrace.setToolTip("Open the log file", "Tells you the issue that happened to the developer can fix it.");
		buttonSaveStackTrace.setClickListener((mouseX, mouseY, mouseButton) -> {
			try {
				Desktop.getDesktop().open(Game.getErrorFile());
			} catch (Exception e) {
				openDialog(new Dialog.Message("Could not open error log"));
				Mod.logger().catching(e);
			}
		});
		this.addComponent(buttonSaveStackTrace);
	}

	public void setPositiveListener(ClickListener positiveListener) {
		this.positiveListener = positiveListener;
	}
}