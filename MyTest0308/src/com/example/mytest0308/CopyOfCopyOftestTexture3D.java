package com.example.mytest0308;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

/**
 * Description:
 * <br/>site: <a href="http://www.crazyit.org">crazyit.org</a> 
 * <br/>Copyright (C), 2001-2012, Yeeku.H.Lee
 * <br/>This program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @author  Yeeku.H.Lee kongyeeku@163.com
 * @version  1.0
 */
public class CopyOfCopyOftestTexture3D extends Activity
	implements OnGestureListener
{
	// 定义旋转角度
	private float anglex = 0f;
	private float angley = 0f;
	static final float ROTATE_FACTOR = 60;
	private float actionX, actionY;
	private SensorEventListener MyListener;
	private float accelerationX, accelerationY, accelDiffX, accelDiffY;
	
	private float currentAngle;
	
	private boolean beerReady = true;
	private Beer beer;
	
	Sensor localSensor;
	SensorManager localSensorManager;
	private boolean downHand = false;
	private float acceLastX, acceLastY;
	private float tempRange = 0;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// 创建一个GLSurfaceView，用于显示OpenGL绘制的图形
		GLSurfaceView glView = new GLSurfaceView(this);
		// 创建GLSurfaceView的内容绘制器
		MyRenderer myRender = new MyRenderer(this);
		glView.getHolder().setFormat(PixelFormat.TRANSLUCENT); 
		glView.setEGLConfigChooser(8, 8, 8, 8, 16, 0); 
		// 为GLSurfaceView设置绘制器
		glView.setRenderer(myRender);

		setContentView(glView);
		
		beer = new Beer();

		localSensorManager = (SensorManager)getSystemService("sensor");
	    localSensor = (Sensor)localSensorManager.getSensorList(1).get(0);
	    MyListener = new SensorEventListener() {
	    	long curTime, lastTime;
	        public void onAccuracyChanged(Sensor paramSensor, int paramInt) {
	        	
	        }

	        public void onSensorChanged(SensorEvent paramSensorEvent)
	        {
	          
	        	accelerationX = (0.05F * paramSensorEvent.values[0] + 0.95F * accelerationX);
	        	accelerationY = (0.05F * paramSensorEvent.values[1] + 0.95F * accelerationY);
	        	curTime = System.currentTimeMillis();
	        	if(Math.abs(accelerationX - acceLastX) > 0.1f) {
	        		float temp = Math.abs(accelerationX - acceLastX) / (curTime - lastTime) * 50f;
	        		if(temp > 1) {
	        			temp = 1;
	        		}
	        		downHand = true;
	        		tempRange = temp;
	        		Log.i("jjj", temp + "");
	        		
	        		lastTime = System.currentTimeMillis();
	        		acceLastX = accelerationX;
	        	}
	        	if(curTime - lastTime > 20L) {
	        		lastTime = System.currentTimeMillis();
	        		acceLastX = accelerationX;
	        	}
	        	accelDiffX = (paramSensorEvent.values[0] - (0.1F * paramSensorEvent.values[0] + 0.9F * accelDiffX));
	          
	        	accelDiffY = (paramSensorEvent.values[1] - (0.1F * paramSensorEvent.values[1] + 0.9F * accelDiffY));
	        	boolean bool = false;
	        	if (accelerationY * accelerationY - paramSensorEvent.values[1] * paramSensorEvent.values[1] > 10.550000000000001D)
	        		bool = true;
	         
	        	if (beerReady)
	        	{
	        		beer.setAccelX(accelerationX);
	        		beer.setAccelY(accelerationY);
	        		beer.setAccelerationX(accelDiffX);
	        		beer.setAccelerationY(accelDiffY);
	        		beer.setShakeFlag(bool);
	        	}
	        	currentAngle = (3.5F + (float)(57.295780000000001D * StrictMath.atan2(beer.getAccelY(), beer.getAccelX()) - 90.0D));
//	        	Log.i("test", "acceleX=" + accelerationX + "  acceleY=" + accelerationY + "   DiffX=" + accelDiffX + "  DiffY=" + accelDiffY);
//	        	Log.i("test", "accelDiffX=" + accelDiffX + "  accelDiffY=" + accelDiffY);
	        	Log.i("test", "" + currentAngle);
	        }
	    };
	      
	    if (localSensor != null) {
	        localSensorManager.registerListener(MyListener, localSensor, 0);
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		localSensorManager.unregisterListener(MyListener, localSensor);
	}
	@Override
	public boolean onTouchEvent(MotionEvent me)
	{
		// 将该Activity上的触碰事件交给GestureDetector处理
		switch (me.getAction()) {
		case MotionEvent.ACTION_DOWN:
			actionX = me.getX();
			actionY = me.getY();
			downHand = true;
			break;
		case MotionEvent.ACTION_MOVE:
			angley += (me.getX() - actionX) ; 
			anglex += (me.getY() - actionY) ; 
			actionX = me.getX();
			actionY = me.getY();
			break;
		case MotionEvent.ACTION_UP:
			
			break;

		default:
			break;
		}
		return true;
		//return detector.onTouchEvent(me);
	}

	@Override
	public boolean onFling(MotionEvent event1, MotionEvent event2,
		float velocityX, float velocityY)
	{
		velocityX = velocityX > 4000 ? 4000 : velocityX;
		velocityX = velocityX < -4000 ? -4000 : velocityX;
		velocityY = velocityY > 4000 ? 4000 : velocityY;
		velocityY = velocityY < -4000 ? -4000 : velocityY;
		// 根据横向上的速度计算沿Y轴旋转的角度
		angley += velocityX * ROTATE_FACTOR / 4000;
		// 根据纵向上的速度计算沿X轴旋转的角度
		anglex += velocityY * ROTATE_FACTOR / 4000;
		return true;
	}

	@Override
	public boolean onDown(MotionEvent arg0)
	{
		return false;
	}

	@Override
	public void onLongPress(MotionEvent event)
	{
	}

	@Override
	public boolean onScroll(MotionEvent event1, MotionEvent event2,
		float distanceX, float distanceY)
	{
		return false;
	}

	@Override
	public void onShowPress(MotionEvent event)
	{
	}

	@Override
	public boolean onSingleTapUp(MotionEvent event)
	{
		return false;
	}

	public class MyRenderer implements Renderer
	{
		float ratio;
		private float[] cube1Vertices = { 
				1f, 1f, 0f,
				1f, -1f, 0f,
				-1f, 1f, 0f,
				-1f, -1f, 0f		
		};
		private float[] cube2Vertices = { 
				1f, 1f, 0f,
				1f, -1f, 0f,
				-1f, 1f, 0f,
				-1f, -1f, 0f		
		};
		private float[] foamVertices = new float[45 * 2 * 3];
		private float[] foamTextures = new float[45 * 2 * 2];
		private float[] slimeVertices = new float[45 * 2 * 3];
		private float[] slimeTextures = new float[45 * 2 * 2];
		private float[] bubbleVertices = { 
				0.1f, 0.1f, 0f,
				0.1f, -0.1f, 0f,
				-0.1f, 0.1f, 0f,
				-0.1f, -0.1f, 0f		
		};
		private byte[] cube1Facets = {
				
		};

		private float[] cube1Textures = {
				0f, 0f, 0f, 1f, 1f, 0f, 1f, 1f
				
		};
		private float[] cube2Textures = {
				0f, 0f, 0f, 1f, 1f, 0f, 1f, 1f
				
		};
		private float[] bubbleTextures = {
				0f, 0f, 0f, 1f, 1f, 0f, 1f, 1f
				
		};
		private Context context;
		private FloatBuffer cubeVerticesBuffer, cube2VerticesBuffer, foamVerticesBuffer, slimeVerticesBuffer,
			bubbleVerticesBuffer;
		private ByteBuffer cubeFacetsBuffer;
		private FloatBuffer cubeTexturesBuffer, cube2TexturesBuffer, foamTexturesBuffer, slimeTexturesBuffer,
			bubbleTexturesBuffer;
		// 定义本程序所使用的纹理
		private int texture;
		private int[] angleInt = new int[45];
		private float[] range = new float[45];
		private boolean[] canMove = new boolean[45];
		private int inx = 0;
		private ArrayList<PowerWave> powerWavesList = new ArrayList<PowerWave>();
		
		public MyRenderer(Context main)
		{
			this.context = main;
			// 将立方体的顶点位置数据数组包装成FloatBuffer;
			cubeVerticesBuffer = BufferIntUtil.getToFloatBuffer(cube1Vertices);
			// 将立方体的6个面（12个三角形）的数组包装成ByteBuffer
//			cubeFacetsBuffer = BufferIntUtil.getToByteBuffer(cubeFacets);
			// 将立方体的纹理贴图的座标数据包装成FloatBuffer
			cubeTexturesBuffer = BufferIntUtil.getToFloatBuffer(cube1Textures);
			
			//前景波浪
			foamVerticesBuffer = BufferIntUtil.getToFloatBuffer(foamVertices);
			foamTexturesBuffer = BufferIntUtil.getToFloatBuffer(foamTextures);
			//背景泡沫
			slimeVerticesBuffer = BufferIntUtil.getToFloatBuffer(slimeVertices);
			slimeTexturesBuffer = BufferIntUtil.getToFloatBuffer(slimeTextures);
			
			for(int i = 0;i < 45;i++) {
				foamVerticesBuffer.put(3 * 2 * i, i * 2f / (45 - 1) - 1f);
				foamVerticesBuffer.put(3 * 2 * i + 1, 1f);
				foamVerticesBuffer.put(3 * 2 * i + 2, 0f);
				
				foamTexturesBuffer.put(2 * 2 * i, i * 1f / (45 - 1));
				foamTexturesBuffer.put(2 * 2 * i + 1, 0f);
				
				foamVerticesBuffer.put(3 * (2 * i + 1), i * 2f / (45 - 1) - 1f);
				foamVerticesBuffer.put(3 * (2 * i + 1) + 1, -1f);
				foamVerticesBuffer.put(3 * (2 * i + 1) + 2, 0f);
				
				foamTexturesBuffer.put(2 * (2 * i + 1), i * 1f / (45 - 1));
				foamTexturesBuffer.put(2 * (2 * i + 1) + 1, 1f);
			}
			for(int i = 0;i < 45;i++) {
				slimeVerticesBuffer.put(3 * 2 * i, i * 2f / (45 - 1) - 1f); //x
				slimeVerticesBuffer.put(3 * 2 * i + 1, 1f);					//y
				slimeVerticesBuffer.put(3 * 2 * i + 2, 0f);					//z
				
				slimeTexturesBuffer.put(2 * 2 * i, i * 1f / (45 - 1));		//x
				slimeTexturesBuffer.put(2 * 2 * i + 1, 1 - 0f);				//y
				
				slimeVerticesBuffer.put(3 * (2 * i + 1), i * 2f / (45 - 1) - 1f);
				slimeVerticesBuffer.put(3 * (2 * i + 1) + 1, -1f);
				slimeVerticesBuffer.put(3 * (2 * i + 1) + 2, 0f);
				
				slimeTexturesBuffer.put(2 * (2 * i + 1), i * 1f / (45 - 1));
				slimeTexturesBuffer.put(2 * (2 * i + 1) + 1, 1 - 1f);
			}
			for(int i = 0;i < 45;i++){
				angleInt[i] = 0;
				canMove[i] = false;
			}
			
			cube2VerticesBuffer = BufferIntUtil.getToFloatBuffer(cube2Vertices);
			cube2TexturesBuffer = BufferIntUtil.getToFloatBuffer(cube2Textures);
			//泡泡
			bubbleVerticesBuffer = BufferIntUtil.getToFloatBuffer(bubbleVertices);
			bubbleTexturesBuffer = BufferIntUtil.getToFloatBuffer(bubbleTextures);
		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config)
		{
			// 关闭抗抖动
			gl.glDisable(GL10.GL_DITHER);
			// 设置系统对透视进行修正
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			gl.glClearColor(0, 0, 0, 0);
			// 设置阴影平滑模式
			gl.glShadeModel(GL10.GL_SMOOTH);
			// 启用深度测试
			gl.glEnable(GL10.GL_DEPTH_TEST);
			// 设置深度测试的类型
			gl.glDepthFunc(GL10.GL_LEQUAL);
			// 启用2D纹理贴图
			gl.glEnable(GL10.GL_TEXTURE_2D);
			// 装载纹理
			loadTexture(gl);
		    gl.glEnable(GL10.GL_BLEND);  
		    gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);  
		  
		    gl.glEnable(GL10.GL_DEPTH_TEST);  
		    gl.glEnable(GL10.GL_ALPHA_TEST);  // Enable Alpha Testing (To Make BlackTansparent)   
		  
		    gl.glAlphaFunc(GL10.GL_GREATER,0.1f);  // Set Alpha Testing (To Make Black Transparent)   
		  
		    // Smooth shading   
		    gl.glShadeModel(GL10.GL_SMOOTH);  


		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height)
		{
			// 设置3D视窗的大小及位置
			gl.glViewport(0, 0, width, height);
			// 将当前矩阵模式设为投影矩阵
			gl.glMatrixMode(GL10.GL_PROJECTION);
			// 初始化单位矩阵
			gl.glLoadIdentity();
			// 计算透视视窗的宽度、高度比
			ratio = (float) width / height;
			// 调用此方法设置透视视窗的空间大小。
			gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
		}
		private float xx = 1.0f;
		private float inc = 0.01f;
		private float incP = 0.25f;
		private float p = 3.0f;
		public void onDrawFrame(GL10 gl)
		{
			// 清除屏幕缓存和深度缓存
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			

			// 启用顶点座标数据
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			// 启用贴图座标数组数据
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			// 设置当前矩阵模式为模型视图。
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glEnable(GL10.GL_BLEND); 
//			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);  
	        
			// --------------------绘制前先把视角往屏幕里推---------------------
			gl.glLoadIdentity();
			// 把绘图中心移入屏幕2个单位
			gl.glTranslatef(0f, 0.0f, -3.0f);
			//------------------------啤酒背景---------------------------------------
			gl.glPushMatrix();
			gl.glTranslatef(0f, 0f, 0f);
//			gl.glColor4f(0f, 0f, 1f, 1f);
			// 设置顶点的位置数据
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, cube2VerticesBuffer);
			// 设置贴图的的座标数据
//			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, cube2TexturesBuffer);
			gl.glScalef(ratio * 3f, 3f, 1f);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, cubeTexturesBuffer);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[1]);
			// 执行纹理贴图
//			gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
			// 按cubeFacetsBuffer指定的面绘制三角形
//						gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4,
//							GL10.GL_UNSIGNED_BYTE, cubeFacetsBuffer);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			
			gl.glPopMatrix();
			//--------------------------背景泡沫-----------------------------------------------
			gl.glPushMatrix();
			gl.glTranslatef(0f, 0f, -0.0f);
			gl.glColor4f(1f, 1f, 1f, 0f);
			gl.glScalef(ratio * 3, 3f, 1f);
//			gl.glScalef(ratio, 1f, 1f);
			changeSlimeVertives();
			// 设置顶点的位置数据
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, slimeVerticesBuffer);
			// 执行纹理贴图
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, slimeTexturesBuffer);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
			
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 45 * 2);
			gl.glPopMatrix();
			//----------------------------泡泡----------------------------------------
			gl.glPushMatrix();
//			for(int i = 0;i < 8;i ++) {
//				bubbles[i].draw(gl);
//				bubbles[i].move();
//			}
			gl.glPopMatrix();
			//--------------------------前景波浪-----------------------------------------------
			gl.glPushMatrix();
			gl.glTranslatef(0f, 0f, -0.0f);
			gl.glColor4f(1f, 1f, 1f, 0f);
			gl.glRotatef(currentAngle, 0f, 0f, 1f);
			gl.glScalef(5f, 1f, 1f);

			freshWave();
			if(downHand) {
				downHand = false;
				putAWaveSor(44, tempRange);
			}
			// 设置顶点的位置数据
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, foamVerticesBuffer);
			// 执行纹理贴图
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, foamTexturesBuffer);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[(int)(p + 0.5f)]);
			
			// 按cubeFacetsBuffer指定的面绘制三角形
//			gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4,
//				GL10.GL_UNSIGNED_BYTE, cubeFacetsBuffer);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 45 * 2);
			gl.glPopMatrix();
			//-----------------------------------------------------------------
			// 绘制结束
			gl.glFinish();
			// 禁用顶点、纹理座标数组
			gl.glDisable(GL10.GL_BLEND);

			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			// 递增角度值以便每次以不同角度绘制
			if(xx > 1.03f) {
				inc = -0.002f;
			} else if(xx < 0.97f) {
				inc = 0.002f;
			}
			xx += inc;
			if(p >= 8.0f) {
				incP = -0.2f;
			} else if(p <= 3.0f) {
				incP = 0.2f;
			}
			p += incP;
		}
		public void changeSlimeVertives() {
			float x, y, x2, y2;
			double tanAngle = Math.tan(currentAngle * Math.PI / 180f) * ratio;
			for(int i = 0;i < 45;i++) {
				x = i * 2f / (45 - 1) - 1f;
				slimeVerticesBuffer.put(3 * 2 * i, x); //x
				slimeVerticesBuffer.put(3 * 2 * i + 1, 1f);					//y
				slimeVerticesBuffer.put(3 * 2 * i + 2, 0f);					//z
				
				x2 = i * 1f / (45 - 1);
				slimeTexturesBuffer.put(2 * 2 * i, x2);		//x
				slimeTexturesBuffer.put(2 * 2 * i + 1, 1 - 0);				//y
				
				slimeVerticesBuffer.put(3 * (2 * i + 1), i * 2f / (45 - 1) - 1f);
				slimeVerticesBuffer.put(3 * (2 * i + 1) + 1, (float)(-1f + 1 - tanAngle + (x + 1) * tanAngle));
				slimeVerticesBuffer.put(3 * (2 * i + 1) + 2, 0f);
				
				slimeTexturesBuffer.put(2 * (2 * i + 1), i * 1f / (45 - 1));
				slimeTexturesBuffer.put(2 * (2 * i + 1) + 1, 1 - (1 - (float)((1 - tanAngle) / 2.0 + x2 * tanAngle)));
			}
		}
		
		
		void test(int inx, float j) {
//			cube3VerticesBuffer.put(inx * 2 * 3 + 1, 1f + j / 100f);
//			cube3VerticesBuffer.put((inx * 2 + 1) * 3 + 1, -1f + j / 100f);
			if(j == 0) {
				if(range[inx] < 0.2) {
					range[inx] = 0;
					canMove[inx] = false;
					return ;
				}
				range[inx] *= 0.7f;
			}
			if(j / 10 == 0) {
				foamVerticesBuffer.put(inx * 2 * 3 + 1, 1f + range[inx] * (float)Math.sin(j * 90 / 5.0 * Math.PI / 180.0) / 10f);
				foamVerticesBuffer.put((inx * 2 + 1) * 3 + 1, -1f + range[inx] * (float)Math.sin(j * 90 / 5.0 * Math.PI / 180.0) / 10f);
			} else {
				foamVerticesBuffer.put(inx * 2 * 3 + 1, 1f + range[inx] * (float)Math.sin(j * 90 / 5.0 * Math.PI / 180.0) / 10f);
				foamVerticesBuffer.put((inx * 2 + 1) * 3 + 1, -1f + range[inx] * (float)Math.sin(j * 90 / 5.0 * Math.PI / 180.0) / 10f);
			}
		}
		int[] textures;
		Bitmap[] bitmaps;
		Bubble[] bubbles;
		void putAWaveSor(int j, float range) {
			if(j >= 0 && j <= 44) {
				if(powerWavesList.size() < 10) {
					powerWavesList.add(new PowerWave(j, range));
				}
			}
		}
		void freshWave() {
			if(powerWavesList.size() > 0) {
				for(int i = powerWavesList.size() - 1;i >= 0;i--) {
					if(powerWavesList.get(i).inx > 0) {
						canMove[powerWavesList.get(i).inx] = true;
						if(angleInt[powerWavesList.get(i).inx] / 10 == 0) {
							range[powerWavesList.get(i).inx] += powerWavesList.get(i).range;
							if(range[powerWavesList.get(i).inx] > 1) {
								range[powerWavesList.get(i).inx] = 1;
							}
						} else {
							range[powerWavesList.get(i).inx] -= powerWavesList.get(i).range;
							if(range[powerWavesList.get(i).inx] < 0) {
								range[powerWavesList.get(i).inx] = -range[powerWavesList.get(i).inx];
							}
							if(range[powerWavesList.get(i).inx] < 0.6f) {
								range[powerWavesList.get(i).inx] = 0.6f;
							}
						}
						powerWavesList.get(i).inx--;
					} else {
						powerWavesList.remove(i);
					}
				}
			}
			
			for(int i = 0;i < 45;i++) {
				if(canMove[i]) {
					test(i, angleInt[i]);
					angleInt[i] = (angleInt[i] + 1) % 20;
				} else {
					test(i, 0);
				}
			}
		}
		private void loadTexture(GL10 gl)
		{
			bitmaps = new Bitmap[69];
			try
			{
				// 加载位图
				bitmaps[0] = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.slime);
				bitmaps[1] = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.lager);
				
				textures = new int[69];
				// 指定生成N个纹理（第一个参数指定生成1个纹理），
				// textures数组将负责存储所有纹理的代号。
				gl.glGenTextures(69, textures, 0);
				// 获取textures纹理数组中的第一个纹理
				texture = textures[0];
				// 通知OpenGL将texture纹理绑定到GL10.GL_TEXTURE_2D目标中
				
				for(int i = 0;i < 2;i ++) {
					// 加载位图生成纹理
					gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[i]);
					GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmaps[i], 0);
					gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,  
						GL10.GL_NEAREST);   
					gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,  
						GL10.GL_NEAREST); 
					// 设置纹理被缩小（距离视点很远时被缩小）时候的滤波方式
					gl.glTexParameterf(GL10.GL_TEXTURE_2D,
						GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
					// 设置纹理被放大（距离视点很近时被方法）时候的滤波方式
					gl.glTexParameterf(GL10.GL_TEXTURE_2D,
						GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
					// 设置在横向、纵向上都是平铺纹理
					gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
						GL10.GL_REPEAT);
					gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
						GL10.GL_REPEAT);
				}
				for(int i = 2;i < 68;i ++) {
					// 加载位图生成纹理
					bitmaps[i] = BitmapFactory.decodeResource(context.getResources(),
							2130837505 + i - 2);
					gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[i]);
					GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmaps[i], 0);
					gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,  
							GL10.GL_NEAREST);   
					gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,  
							GL10.GL_NEAREST); 
					// 设置纹理被缩小（距离视点很远时被缩小）时候的滤波方式
					gl.glTexParameterf(GL10.GL_TEXTURE_2D,
							GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
					// 设置纹理被放大（距离视点很近时被方法）时候的滤波方式
					gl.glTexParameterf(GL10.GL_TEXTURE_2D,
							GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
					// 设置在横向、纵向上都是平铺纹理
					gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
							GL10.GL_REPEAT);
					gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
							GL10.GL_REPEAT);
					gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,  
				                GL10.GL_REPLACE);  

					if (bitmaps[i] != null) {
						bitmaps[i].recycle();
					}
					
				}
				// 加载位图生成纹理
				bitmaps[68] = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.bubble32);
				gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[68]);
				GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmaps[68], 0);
				gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,  
						GL10.GL_NEAREST);   
				gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,  
						GL10.GL_NEAREST); 
				// 设置纹理被缩小（距离视点很远时被缩小）时候的滤波方式
				gl.glTexParameterf(GL10.GL_TEXTURE_2D,
						GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
				// 设置纹理被放大（距离视点很近时被方法）时候的滤波方式
				gl.glTexParameterf(GL10.GL_TEXTURE_2D,
						GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
				// 设置在横向、纵向上都是平铺纹理
				gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
						GL10.GL_REPEAT);
				gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
						GL10.GL_REPEAT);
				gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,  
			                GL10.GL_REPLACE); 
				if (bitmaps[68] != null) {
					bitmaps[68].recycle();
				}
				
				bubbles = new Bubble[8];
				for(int i = 0;i < 8;i ++) {
					float x = (random.nextInt(8) - 4) / 2.0f;
					float y = (random.nextInt(10) - 25) / 10.0f;
					bubbles[i] = new Bubble(0, 0, x, y);
				}
				
			}
			finally
			{
				// 生成纹理之后，回收位图
				for(int i = 0;i < 2;i ++) {
					if (bitmaps[i] != null) {
						bitmaps[i].recycle();
					}
				}
			}
		}
		Bubble bubble;
		Random random = new Random();


		class Bubble {
			float x0, y0, x, y;
			int temp;
			boolean canRunning = true;
			public Bubble(float x0, float y0, float x, float y) {
				// TODO Auto-generated constructor stub
				this.x0 = x0;
				this.y0 = y0;
				this.x = x;
				this.y = y;
			}
			void draw(GL10 gl) {
//				gl.glLoadIdentity();
//				// 把绘图中心移入屏幕2个单位
				gl.glPushMatrix();
				gl.glTranslatef(x, y, 0);
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, bubbleVerticesBuffer);
				// 执行纹理贴图
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, bubbleTexturesBuffer);
				gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[68]);
				
				gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
				gl.glPopMatrix();
			    
			}
			public void move() {
				if(canRunning) {
					y += 0.06f;
				} else {
					//让泡泡随机固定一段时间
					temp = random.nextInt(4);
					if(temp == 0) {
						canRunning = true;
					}
				}
				if(y > 0.5) {
					//y = random(一个范围)
//					y = -2.0f;
					temp = random.nextInt(3);
					
					if(temp == 0) {
						x = (random.nextInt(8) - 4) / 2.0f;
						y = (random.nextInt(10) - 25) / 10.0f;
						canRunning = false;
					} else {
						y = 100;
					}
				}
			}
			private void setX0Y0(float x0, float y0) {
				this.x0 = x0;
				this.y0 = y0;
			}
		}
		class PowerWave {
			int inx;
			float range;
			public PowerWave(int inx, float range) {
				this.inx = inx;
				this.range = range;
			}
		}
	}
	
}