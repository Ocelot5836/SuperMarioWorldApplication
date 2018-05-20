package com.ocelot.mod.lib.opengl3d;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.BufferUtils;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * Allows the ability to easily store data in the buffers for the default data types.
 * 
 * @author Ocelot5836
 */
public class Buffers {

	/**
	 * Stores the data in a new byte buffer.
	 * 
	 * @param data
	 *            The data to put in a new buffer
	 * @return The buffer with the data inside
	 */
	public static ByteBuffer storeDataInByteBuffer(byte[] data) {
		ByteBuffer buffer = BufferUtils.createByteBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	/**
	 * Stores the data in a new short buffer.
	 * 
	 * @param data
	 *            The data to put in a new buffer
	 * @return The buffer with the data inside
	 */
	public static ShortBuffer storeDataInShortBuffer(short[] data) {
		ShortBuffer buffer = BufferUtils.createShortBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	/**
	 * Stores the data in a new char buffer.
	 * 
	 * @param data
	 *            The data to put in a new buffer
	 * @return The buffer with the data inside
	 */
	public static CharBuffer storeDataInCharBuffer(char[] data) {
		CharBuffer buffer = BufferUtils.createCharBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	/**
	 * Stores the data in a new int buffer.
	 * 
	 * @param data
	 *            The data to put in a new buffer
	 * @return The buffer with the data inside
	 */
	public static IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	/**
	 * Stores the data in a new long buffer.
	 * 
	 * @param data
	 *            The data to put in a new buffer
	 * @return The buffer with the data inside
	 */
	public static LongBuffer storeDataInLongBuffer(long[] data) {
		LongBuffer buffer = BufferUtils.createLongBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	/**
	 * Stores the data in a new float buffer.
	 * 
	 * @param data
	 *            The data to put in a new buffer
	 * @return The buffer with the data inside
	 */
	public static FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	/**
	 * Stores the data in a new double buffer.
	 * 
	 * @param data
	 *            The data to put in a new buffer
	 * @return The buffer with the data inside
	 */
	public static DoubleBuffer storeDataInDoubleBuffer(double[] data) {
		DoubleBuffer buffer = BufferUtils.createDoubleBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
}