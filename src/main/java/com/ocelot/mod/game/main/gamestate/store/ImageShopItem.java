package com.ocelot.mod.game.main.gamestate.store;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import com.mrcrayfish.device.Reference;
import com.mrcrayfish.device.api.app.Component;
import com.mrcrayfish.device.api.app.Dialog;
import com.mrcrayfish.device.api.io.File;
import com.mrcrayfish.device.api.utils.BankUtil;
import com.mrcrayfish.device.api.utils.RenderUtil;
import com.mrcrayfish.device.object.Picture;
import com.mrcrayfish.device.object.Picture.Size;
import com.ocelot.api.utils.SoundUtils;
import com.ocelot.api.utils.TextureUtils;
import com.ocelot.mod.SuperMarioWorld;
import com.ocelot.mod.application.ApplicationGame;
import com.ocelot.mod.lib.Lib;
import com.ocelot.mod.lib.RenderHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class ImageShopItem extends ShopItem {

	public static final BufferedImage SHOP_IMAGES = Lib.loadImage(new ResourceLocation(SuperMarioWorld.MOD_ID, "textures/shop_images.png"));

	private ResourceLocation texture;
	private BufferedImage image;
	private String label;
	private boolean canBuy;

	private double x, y, width, height;

	public ImageShopItem(BufferedImage image, String label, int price) {
		super(price);
		this.texture = TextureUtils.createBufferedImageTexture(image);
		this.image = image;
		this.label = label;
		BankUtil.getBalance((nbt, success) -> {
			this.canBuy = success ? nbt.getInteger("balance") >= price : false;
		});
	}

	@Override
	public void render(double x, double y, double width, double height, Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		RenderHelper.fillRect(x, y, x + width, y + height, Color.DARK_GRAY.getRGB());
		RenderHelper.fillRect(x + 1, y + 1, x + width - 1, y + height - 1, Color.GRAY.getRGB());

		GlStateManager.pushMatrix();
		GlStateManager.translate(x + width / 2 - Minecraft.getMinecraft().fontRenderer.getStringWidth(label) / 2, y + 6, 0);
		Minecraft.getMinecraft().fontRenderer.drawString(label, 0, 0, 0xffffffff, true);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.translate(x + width / 2 - Minecraft.getMinecraft().fontRenderer.getStringWidth(price + "Emeralds") / 2, y + 18, 0);
		Minecraft.getMinecraft().fontRenderer.drawString(price + " Emeralds", 0, 0, 0xffffffff, true);
		GlStateManager.popMatrix();

		TextureUtils.bindTexture(this.texture);
		RenderHelper.drawScaledCustomSizeModalRect(x + width / 2 - 8, y + 32, 0, 0, image.getWidth(), image.getHeight(), 16, 16, image.getWidth(), image.getHeight());
		this.drawButton("Buy", x + width / 2 - (width - 5) / 2, y + height - 16.5, (int) width - 5, 14, mouseX, mouseY, this.canBuy);
	}

	@Override
	public void onMousePressed(int mouseButton, int mouseX, int mouseY) {
		this.onButtonPressed(x + width / 2 - (width - 5) / 2, y + height - 16.5, (int) width - 5, 14, mouseX, mouseY, this.canBuy);
	}

	private void onButtonPressed(double x, double y, int width, int height, int mouseX, int mouseY, boolean enabled) {
		if (enabled) {
			if (mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height) {
				SoundUtils.playButtonClick();

				BankUtil.getBalance((nbt, success) -> {
					int balance = nbt.getInteger("balance");
					if (balance >= price) {
						BankUtil.remove(price, (nbt1, success1) -> {
							if (success1) {
								Picture picture = new Picture(this.label, "Ocelot5836", Size.X32);
								picture.pixels = this.getImageData();
								NBTTagCompound pictureNBT = new NBTTagCompound();
								picture.writeToNBT(pictureNBT);
								File file = new File(this.label, Reference.MOD_ID + ".pixel_painter", pictureNBT);
								Dialog.SaveFile dialog = new Dialog.SaveFile(ApplicationGame.getApp(), file);
								ApplicationGame.getApp().openDialog(dialog);
							} else {
								ApplicationGame.getApp().openDialog(new Dialog.Message("Bank transaction failed"));
							}
						});
					}
				});
			}
		}
	}

	public BufferedImage getImage() {
		return image;
	}

	public int[] getImageData() {
		try {
			return ((DataBufferInt) image.getData().getDataBuffer()).getData();
		} catch (Exception e) {
			BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
			for (int y = 0; y < copy.getHeight(); y++) {
				for (int x = 0; x < copy.getWidth(); x++) {
					copy.setRGB(x, y, image.getRGB(x, y));
				}
			}
			return ((DataBufferInt) copy.getData().getDataBuffer()).getData();
		}
	}

	private static void drawButton(String text, double x, double y, int width, int height, int mouseX, int mouseY, boolean enabled) {
		int i = 1;

		if (!enabled) {
			i = 0;
		} else if (mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height) {
			i = 2;
		}

		TextureUtils.bindTexture(Component.COMPONENTS_GUI);
		RenderUtil.drawRectWithTexture(x, y, 96 + i * 5, 12, 2, 2, 2, 2);
		RenderUtil.drawRectWithTexture(x + width - 2, y, 99 + i * 5, 12, 2, 2, 2, 2);
		RenderUtil.drawRectWithTexture(x + width - 2, y + height - 2, 99 + i * 5, 15, 2, 2, 2, 2);
		RenderUtil.drawRectWithTexture(x, y + height - 2, 96 + i * 5, 15, 2, 2, 2, 2);

		RenderUtil.drawRectWithTexture(x + 2, y, 98 + i * 5, 12, width - 4, 2, 1, 2);
		RenderUtil.drawRectWithTexture(x + width - 2, y + 2, 99 + i * 5, 14, 2, height - 4, 2, 1);
		RenderUtil.drawRectWithTexture(x + 2, y + height - 2, 98 + i * 5, 15, width - 4, 2, 1, 2);
		RenderUtil.drawRectWithTexture(x, y + 2, 96 + i * 5, 14, 2, height - 4, 2, 1);

		RenderUtil.drawRectWithTexture(x + 2, y + 2, 98 + i * 5, 14, width - 4, height - 4, 1, 1);

		String label = RenderUtil.clipStringToWidth(text, width - 4);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + width / 2 - Minecraft.getMinecraft().fontRenderer.getStringWidth(label) / 2, y + height / 2 - Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT / 2, 0);
		Minecraft.getMinecraft().fontRenderer.drawString(label, 0, 0, 0xffffffff, true);
		GlStateManager.popMatrix();
	}
}
