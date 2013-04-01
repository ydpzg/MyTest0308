package com.example.mytest0308;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class BufferIntUtil {

	public static FloatBuffer getToFloatBuffer(float[] a) {
		// 先初始化buffer,数组的长度*4,因为一个int 占4个字节
		FloatBuffer floatBuffer;
		ByteBuffer mbb = ByteBuffer.allocateDirect(a.length * 4);
		// 数组排列用nativeOrder
		mbb.order(ByteOrder.nativeOrder());
		floatBuffer = mbb.asFloatBuffer();
		floatBuffer.put(a);
		floatBuffer.position(0);
		return floatBuffer;
	}
	public static IntBuffer getToIntBuffer(int[] a) {
		// 先初始化buffer,数组的长度*4,因为一个int 占4个字节
		IntBuffer floatBuffer;
		ByteBuffer mbb = ByteBuffer.allocateDirect(a.length * 4);
		// 数组排列用nativeOrder
		mbb.order(ByteOrder.nativeOrder());
		floatBuffer = mbb.asIntBuffer();
		floatBuffer.put(a);
		floatBuffer.position(0);
		return floatBuffer;
	}
	public static ByteBuffer getToByteBuffer(byte[] a) {
		ByteBuffer mbb = ByteBuffer.allocateDirect(a.length);
		// 数组排列用nativeOrder
		mbb.put(a);
		mbb.position(0);
		return mbb;
	}
}