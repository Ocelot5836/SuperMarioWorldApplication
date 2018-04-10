package com.ocelot.mod;

import com.ocelot.api.utils.GuiUtils;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * Contains some useful usernames of players from their UUID.
 * 
 * @author Ocelot5836
 */
public class Usernames {

	/** Ocelot5836's UUID */
	public static final String OCELOT5836_UUID = "86dc8a9f238e450280211d488095fd8a";
	/** MrCrayfish's UUID */
	public static final String MR_CRAYFISH_UUID = "62d17f0b524841f4befc2daa457fb266";

	/** Ocelot5836's Username from UUID */
	public static final String OCELOT5836 = GuiUtils.getPlayerName(OCELOT5836_UUID);
	/** MrCrayfish's Username from UUID */
	public static final String MR_CRAYFISH = GuiUtils.getPlayerName(MR_CRAYFISH_UUID);

}