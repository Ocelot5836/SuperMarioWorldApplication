package com.ocelot.mod.game.core.save;

import com.ocelot.mod.game.core.GameTemplate;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class SaveFileManager implements INBTSerializable<NBTTagCompound> {

	private GameTemplate game;
	private NBTTagCompound[] saves;
	private int saveFile;

	public SaveFileManager(GameTemplate game) {
		this.game = game;
		this.saveFile = 0;
		this.saves = new NBTTagCompound[4];
		for (int i = 0; i < saves.length; i++) {
			this.saves[i] = new NBTTagCompound();
		}
	}

	public void save(String name, NBTTagCompound nbt) {
		this.getSaveCompound().setTag(name, nbt);
	}

	public NBTTagCompound load(String name) {
		return this.getSaveCompound().getCompoundTag(name);
	}

	public NBTTagCompound getSaveCompound() {
		return saves[saveFile];
	}

	public int getSaveFile() {
		return saveFile;
	}

	public void setSaveFile(int saveFile) {
		if (saveFile < 0)
			saveFile = 0;
		if (saveFile > saves.length)
			saveFile = saves.length;
		this.saveFile = saveFile;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		for (int i = 0; i < this.saves.length; i++) {
			nbt.setTag("save_" + i, this.saves[i]);
		}
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		for (int i = 0; i < 4; i++) {
			if (nbt != null && nbt.hasKey("save_" + i, 10)) {
				this.saves[i] = nbt.getCompoundTag("save_" + i);
			} else {
				this.saves[i] = new NBTTagCompound();
			}
		}
	}
}