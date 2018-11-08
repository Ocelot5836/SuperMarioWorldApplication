package com.ocelot.mod.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.ocelot.mod.SuperMarioWorld;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.ConfigGuiType;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.DummyConfigElement.DummyCategoryElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.GuiConfigEntries.CategoryEntry;
import net.minecraftforge.fml.client.config.GuiConfigEntries.NumberSliderEntry;
import net.minecraftforge.fml.client.config.IConfigElement;

public class ModConfigGuiFactory implements IModGuiFactory {

	@Override
	public void initialize(Minecraft mc) {
	}

	@Override
	public boolean hasConfigGui() {
		return true;
	}

	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen) {
		return new ModConfigGui(parentScreen);
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

	private static class ModConfigGui extends GuiConfig {
		public ModConfigGui(GuiScreen parentScreen) {
			super(parentScreen, getConfigElements(), SuperMarioWorld.MOD_ID, false, false, I18n.format("gui." + SuperMarioWorld.MOD_ID + ".config.title"));
		}
	}

	private static List<IConfigElement> getConfigElements() {
		List<IConfigElement> list = new ArrayList<IConfigElement>();
		for (ConfigCategory category : ConfigCategory.configs) {
			list.add(new DummyCategoryElement(I18n.format("gui." + SuperMarioWorld.MOD_ID + ".config.category." + category.getName()), "gui." + SuperMarioWorld.MOD_ID + ".config.category." + category.getName(), category.getClazz()));
		}
		return list;
	}

	public static class CategoryEntryDefault extends CategoryEntry {

		protected ConfigCategory category;

		public CategoryEntryDefault(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
			super(owningScreen, owningEntryList, configElement);
		}

		@Override
		protected GuiScreen buildChildScreen() {
			this.category = ModConfig.CATEGORY_SOUNDS;
			for (ConfigCategory cat : ConfigCategory.configs) {
				String catName = I18n.format("gui." + SuperMarioWorld.MOD_ID + ".config.category." + cat.getName());
				if (catName.equalsIgnoreCase(configElement.getName())) {
					this.category = cat;
					break;
				}
			}

			Configuration config = ModConfig.getConfig();
			ConfigElement category = new ConfigElement(config.getCategory(this.category.getName()));
			List<IConfigElement> propertiesOnScreen = category.getChildElements();
			String windowTitle = I18n.format("gui." + SuperMarioWorld.MOD_ID + ".config.category." + category.getName());
			return new GuiConfig(owningScreen, propertiesOnScreen, owningScreen.modID, this.configElement.requiresWorldRestart() || this.owningScreen.allRequireWorldRestart, this.configElement.requiresMcRestart() || this.owningScreen.allRequireMcRestart, windowTitle);
		}
	}

	public static class CategoryEntrySounds extends CategoryEntryDefault {
		public CategoryEntrySounds(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
			super(owningScreen, owningEntryList, configElement);
		}

		@Override
		protected GuiScreen buildChildScreen() {
			Configuration config = ModConfig.getConfig();
			ConfigElement category = new ConfigElement(config.getCategory(this.category.getName()));
			List<IConfigElement> propertiesOnScreen = category.getChildElements();
			String windowTitle = I18n.format("gui." + SuperMarioWorld.MOD_ID + ".config.category." + category.getName());

			for (int i = 0; i < propertiesOnScreen.size(); i++) {
				IConfigElement element = propertiesOnScreen.get(i);
				if (element.getName().equals("mario_music_volume") || element.getName().equals("mario_sfx_volume")) {
					propertiesOnScreen.set(i, new DummyConfigElement(element.getName(), Integer.parseInt((String) element.getDefault()), ConfigGuiType.INTEGER, element.getLanguageKey(), element.getMinValue(), element.getMaxValue()).setCustomListEntryClass(NumberSliderEntry.class));
				}
			}

			return new GuiConfig(owningScreen, propertiesOnScreen, owningScreen.modID, this.configElement.requiresWorldRestart() || this.owningScreen.allRequireWorldRestart, this.configElement.requiresMcRestart() || this.owningScreen.allRequireMcRestart, windowTitle);
		}
	}
}