package com.ocelot.mod.application;

import java.awt.Color;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.mrcrayfish.device.api.app.Dialog;
import com.mrcrayfish.device.api.app.Layout.Background;
import com.mrcrayfish.device.api.app.component.Button;
import com.mrcrayfish.device.api.app.component.Text;
import com.mrcrayfish.device.api.app.listener.ClickListener;
import com.ocelot.mod.Mod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

/**
 * An alternate, fixed version of the default {@link Message}.
 * 
 * @author Ocelot5836
 * @author MrCrayfish
 */
public class DialogMessage extends Dialog {

	private String messageText = "";

	private ClickListener positiveListener;
	private Button buttonPositive;
	private Button buttonSaveStackTrace;

	public DialogMessage(String messageText) {
		this.messageText = messageText;
		defaultLayout.width = 250;
	}

	@Override
	public void init() {
		super.init();

		int lines = Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(messageText, getWidth() - 10).size();
		defaultLayout.height += (lines - 1) * 10;

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

		buttonSaveStackTrace = new Button(5, getHeight() - 20, "Open Pastebin");
		buttonSaveStackTrace.setToolTip("Why Open Pastebin?", "You can input the error and send it to the developer to help fix bugs.");
		buttonSaveStackTrace.setClickListener((mouseX, mouseY, mouseButton) -> {
			try {
				Desktop.getDesktop().browse(new URI("https://pastebin.com/"));
			} catch (Exception e) {
				Mod.logger().catching(e);
			}
		});
		this.addComponent(buttonSaveStackTrace);
	}

	public void setPositiveListener(ClickListener positiveListener) {
		this.positiveListener = positiveListener;
	}
}