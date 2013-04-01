package com.example.mytest0308;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.view.MotionEvent;

public class ClearActivity extends Activity {
	private GLSurfaceView mGLView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGLView = new ClearGLSurfaceView(this);
		setContentView(mGLView);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mGLView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mGLView.onResume();
	}
}

class ClearGLSurfaceView extends GLSurfaceView {
//	CubeRenderer mRenderer;
	GLRender mRenderer;

	public ClearGLSurfaceView(Context context) {
		super(context);
		mRenderer = new GLRender();
//		mRenderer = new CubeRenderer(false);
		setRenderer(mRenderer);
	}

//	public boolean onTouchEvent(final MotionEvent event) {
//		queueEvent(new Runnable() {
//			public void run() {
//				mRenderer.setColor(event.getX() / getWidth(), event.getY()
//						/ getHeight(), 1.0f);
//			}
//		});
//		return true;
//	}
}

class GLRender implements Renderer {

	float rotateTri, rotateQuad;
	int one = 0x10000;

	// 三角形的一个顶点
	private IntBuffer triggerBuffer = BufferIntUtil.getToIntBuffer(new int[] { 0, one, 0, // 上顶点
			-one, -one, 0, // 左顶点
			one, -one, 0 // 右下点
			});

	// 正方形的四个顶点
	private IntBuffer quateBuffer = BufferIntUtil.getToIntBuffer(new int[] { one, one, 0,
			-one, -one, 0, one, -one, 0, -one, -one, 0 });

	private IntBuffer colorBuffer = BufferIntUtil.getToIntBuffer(new int[] { one, 0, 0, one,
			one, 0, 0, one,
			one, 0, 0, one });

	private IntBuffer mVertexBuffer = BufferIntUtil.getToIntBuffer(new int[] { 
			-one, -one, -one,
            one, -one, -one,
            one,  one, -one,
            -one,  one, -one,
            -one, -one,  one,
            one, -one,  one,
            one,  one,  one,
            -one,  one,  one, });
	private IntBuffer mColorBuffer = BufferIntUtil.getToIntBuffer(new int[] {
			0,    0,    0,  one,
            one,    0,    0,  one,
            one,  one,    0,  one,
            0,  one,    0,  one,
            0,    0,  one,  one,
            one,    0,  one,  one,
            one,  one,  one,  one,
            0,  one,  one,  one, });
	private ByteBuffer mIndexBuffer = BufferIntUtil.getToByteBuffer(new byte[] { 
			0, 4, 5,    0, 5, 1,
            1, 5, 6,    1, 6, 2,
            2, 6, 7,    2, 7, 3,
            3, 7, 4,    3, 4, 0,
            4, 7, 6,    4, 6, 5,
            3, 0, 1,    3, 1, 2 });
	float translateX;
	@Override
	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub



		// 清除屏幕和深度缓存
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		// 重置当前的模型观察矩阵
		gl.glLoadIdentity();

		// 设置旋转   x,y,z表示以哪一个为轴
		gl.glTranslatef(0, 0f, -6f);
		gl.glRotatef(rotateTri, 1.0f, 1.0f, 1.0f);
		// 左移 1.5 单位，并移入屏幕 6.0
		gl.glTranslatef(1, 0f, 0f);
		// 设置定点数组
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		// 设置颜色数组
//		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

		
		gl.glFrontFace(GL10.GL_CW);
        gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer);
//        gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffer);
        gl.glColor4f(1, 0, 0, 0.3f);
        gl.glDrawElements(GL10.GL_TRIANGLES, 36, GL10.GL_UNSIGNED_BYTE, mIndexBuffer);

//        gl.glTranslatef(0.5f, 0.5f, 0.5f);
//		gl.glColorPointer(4, GL10.GL_FIXED, 0, colorBuffer);
//		// 设置三角形顶点
//		gl.glVertexPointer(3, GL10.GL_FIXED, 0, triggerBuffer);
//		// 绘制三角形
//		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
//
//		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

		// 绘制三角形结束
//		gl.glFinish();
//
//		/***********************/
//		/* 渲染正方形 */
//		// 重置当前的模型观察矩阵
//		gl.glLoadIdentity();
//
//		// 左移 1.5 单位，并移入屏幕 6.0
//		gl.glTranslatef(1.5f, 0.0f, 12.0f);
//
//		// 设置当前色为蓝色
//		gl.glColor4f(0.5f, 0.5f, 1.0f, 1.0f);
//		// 设置旋转
//		gl.glRotatef(rotateQuad, 1.0f, 0.0f, 0.0f);
//
//		// 设置和绘制正方形
//		gl.glVertexPointer(3, GL10.GL_FIXED, 0, quateBuffer);
//		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
//
//		// 绘制正方形结束
//		gl.glFinish();

		// 取消顶点数组
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

		// 改变旋转的角度
		rotateTri += 0.5f;
		rotateQuad -= 0.5f;
		translateX += 0.1f;
		if(translateX > 5) {
			translateX = -5;
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub

		float ratio = (float) width / height;
		// 设置OpenGL场景的大小
		gl.glViewport(0, 0, width, height);
		// 设置投影矩阵
		gl.glMatrixMode(GL10.GL_PROJECTION);
		// 重置投影矩阵
		gl.glLoadIdentity();
		// 设置视口的大小
		gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
		// 选择模型观察矩阵
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		// 重置模型观察矩阵
		gl.glLoadIdentity();

	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub
		// 启用阴影平滑
//		gl.glShadeModel(GL10.GL_SMOOTH);
//
//		// 黑色背景
//		gl.glClearColor(0, 0, 0, 0);
//
//		// 设置深度缓存
//		gl.glClearDepthf(1.0f);
//		// 启用深度测试
//		gl.glEnable(GL10.GL_DEPTH_TEST);
//		// 所作深度测试的类型
//		gl.glDepthFunc(GL10.GL_LEQUAL);
//
//		// 告诉系统对透视进行修正
//		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		gl.glDisable(GL10.GL_DITHER);

        /*
         * Some one-time OpenGL initialization can be made here
         * probably based on features of this particular context
         */
         gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
                 GL10.GL_FASTEST);

             gl.glClearColor(0,0,0,0);
         gl.glEnable(GL10.GL_CULL_FACE);
         gl.glShadeModel(GL10.GL_SMOOTH);
         gl.glEnable(GL10.GL_DEPTH_TEST);
	}

}
