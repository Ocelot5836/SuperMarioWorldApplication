package com.ocelot.api.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class JsonHelper {
	
	public static boolean getBoolean(JsonObject obj, String name) throws Exception {
		if (obj.has(name) && obj.get(name).isJsonPrimitive() && obj.get(name).getAsJsonPrimitive().isBoolean()) {
			return obj.get(name).getAsBoolean();
		}
		throw new Exception();
	}

	public static Number getNumber(JsonObject obj, String name) throws Exception {
		if (obj.has(name) && obj.get(name).isJsonPrimitive() && obj.get(name).getAsJsonPrimitive().isNumber()) {
			return obj.get(name).getAsNumber();
		}
		throw new Exception();
	}

	public static String getString(JsonObject obj, String name) throws Exception {
		if (obj.has(name) && obj.get(name).isJsonPrimitive() && obj.get(name).getAsJsonPrimitive().isString()) {
			return obj.get(name).getAsString();
		}
		throw new Exception();
	}

	public static JsonObject getObj(JsonObject obj, String name) throws Exception {
		if (obj.has(name) && obj.get(name).isJsonObject()) {
			return obj.get(name).getAsJsonObject();
		}
		throw new Exception();
	}

	public static JsonArray getArray(JsonObject obj, String name) throws Exception {
		if (obj.has(name) && obj.get(name).isJsonArray()) {
			return obj.get(name).getAsJsonArray();
		}
		throw new Exception();
	}
}