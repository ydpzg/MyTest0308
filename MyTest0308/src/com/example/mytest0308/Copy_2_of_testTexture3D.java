package com.example.mytest0308;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.R.array;
import android.R.integer;
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
public class Copy_2_of_testTexture3D extends Activity
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
	
	private static final int XX = 0;
	private static final int YY = 1;
	
	private boolean jj = true;
	Sensor localSensor;
	SensorManager localSensorManager;
	private float[] waveAddEach = new float[128];
	
	
	private float[] pathMaxX = new float[128];
	private float[] pathCurX = new float[128];
	private float[] speedV = new float[128];
	private float[] theWay = new float[128];
	
	private ArrayList<PointPower> pointPowers;
	private float lastX, lastY;
	
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
	        public void onAccuracyChanged(Sensor paramSensor, int paramInt) {
	        	
	        }

	        public void onSensorChanged(SensorEvent paramSensorEvent)
	        {
	          
	        	accelerationX = (0.05F * paramSensorEvent.values[0] + 0.95F * accelerationX);
	        	accelerationY = (0.05F * paramSensorEvent.values[1] + 0.95F * accelerationY);
	        	accelDiffX = (paramSensorEvent.values[0] - (0.1F * paramSensorEvent.values[0] + 0.9F * accelDiffX));
	          
	        	accelDiffY = (paramSensorEvent.values[1] - (0.1F * paramSensorEvent.values[1] + 0.9F * accelDiffY));
	        	boolean bool = false;
	        	if (accelerationY * accelerationY - paramSensorEvent.values[1] * paramSensorEvent.values[1] > 10.550000000000001D)
	        		bool = true;
//	            Log.i("olp", accelDiffX + "    " + accelDiffY);
	        	if(Math.abs(lastX - accelerationX) > 0.1) {
	        		putAWave(70, 0.25f);
	        	}
	        	lastX = accelerationX; 
	        	lastY = accelerationY; 
	        	if (beerReady)
	        	{
	        		beer.setAccelX(accelerationX);
	        		beer.setAccelY(accelerationY);
	        		beer.setAccelerationX(accelDiffX);
	        		beer.setAccelerationY(accelDiffY);
	        		beer.setShakeFlag(bool);
	        	}
	        	currentAngle = (3.5F + (float)(57.295780000000001D * StrictMath.atan2(beer.getAccelY(), beer.getAccelX()) - 90.0D));
//	        	Log.i("test", "accelerationX=" + accelerationX + "  accelerationY=" + accelerationY);
//	        	Log.i("test", "accelDiffX=" + accelDiffX + "  accelDiffY=" + accelDiffY);
//	        	Log.i("test", "" + currentAngle);
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
//			putAWave(45, 0.3f);
//			putAWave(78, 0.3f);
			Log.i("ccc", "11111");
			actionX = me.getX();
			actionY = me.getY();
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
	public void putAWave(int j, float power) {
		if(theWay[j] == -1) {
			if(power > pathMaxX[j]) {
				theWay[j] = 1;
				pathMaxX[j] = power - pathMaxX[j];
			} else {
				pathMaxX[j] -= power; 
			}
		} else {
			pathMaxX[j] += power;
		}
		if(pathMaxX[j] > 0.3f){
			pathMaxX[j] = 0.3f;
		}
		if(theWay[j] == 0) {
			theWay[j] = 1;
		}
		if(theWay[j] == -1) {
			speedV[j] = pathMaxX[j] / 5.0f; 
		} else {
			speedV[j] = pathMaxX[j] / 4.0f; 
		}
		pointPowers.add(new PointPower(1, j + 1, power));
		pointPowers.add(new PointPower(-1, j - 1, power));
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
		//背景黑泡沫
		private float[] cubeVertices = { 
				1f, -1f, 0f,
				1f, 0f, 0f,
				1f, 0f, 0f,
				-1f, 0f, 0f,
				-1f, 0f, 0f,
				-1f, -1f, 0f		
		};
		private float[] cubeTextures = {
				1f, 1f, 1f, 0.5f, 1f, 0.5f, 
				0f, 0.5f, 0f, 0.5f, 0f, 1f
				
		};
		//前景泡沫
		private float[] cube2Vertices = { 
				1f, 1f, 0f,
				1f, -1f, 0f,
				-1f, 1f, 0f,
				-1f, -1f, 0f		
		};
		private float[] cube2Textures = {
				0f, 0f, 0f, 1f, 1f, 0f, 1f, 1f
				
		};
		private float[] cube3Vertices = {
			1f, 1f, 0f,
			-1f, 1f, 0f,
			-1f, 0f, 0f,
			
			1f, 1f, 0f,
			1f, 0f, 0f,
			-1f, 0f, 0f,
			
				
			-1f, 0f, 0f,
			1f, 0f, 0f,
			1f, -1f, 0f,
			
			-1f, 0f, 0f,
			-1f, -1f, 0f,
			1f, -1f, 0f,
		};
		private float[] cube3Textures = {
				1f, 1f, 
				0f, 1f, 
				0f, 0.5f,
				
				1f, 1f,
				1f, 0.5f,
				0f, 0.5f,
				
				0f, 0.5f,
				1f, 0.5f, 
				1f, 0f, 
				
				0f, 0.5f,
				0f, 0f,
				1f, 0f
				
		};
		//背景啤酒
		private float[] cube4Vertices = { 
				1f, -1f, 0f,
				1f, 1f, 0f,
				-1f, 1f, 0f,
				-1f, -1f, 0f		
		}; 
		private float[] cube4Textures = {
				1f, 1f, 1f, 0f, 0f, 0f, 0f, 1f
				
		};
		private float[] waveVertices = new float[32 * 128 * 6 * 3];
		private float[] waveTextures = new float[32 * 128 * 6 * 2];
		private float[] waveVerticesCopy = new float[32 * 128 * 6 * 3];
		//泡泡
		private float[] bubbleVertices = { 
				0.1f, 0.1f, 0f,
				0.1f, -0.1f, 0f,
				-0.1f, 0.1f, 0f,
				-0.1f, -0.1f, 0f		
		}; 
		private float[] bubbleTextures = {
				0f, 0f, 0f, 1f, 1f, 0f, 1f, 1f
				
		};

		
		private Context context;
		private FloatBuffer cubeVerticesBuffer, cube2VerticesBuffer, cube3VerticesBuffer, 
			cube4VerticesBuffer, bubbleVerticesBuffer, waveVerticesBuffer;
		private ByteBuffer cubeFacetsBuffer;
		private FloatBuffer cubeTexturesBuffer, cube2TexturesBuffer, cube3TexturesBuffer, 
			cube4TexturesBuffer, bubbleTexturesBuffer, waveTexturesBuffer;
		// 定义本程序所使用的纹理
		private int texture;
		private float[] waveAddTemp = new float[128];
		
		private float[] waveMax = new float[128];
		

		public MyRenderer(Context main)
		{
			this.context = main;
			// 将立方体的顶点位置数据数组包装成FloatBuffer;
			cubeVerticesBuffer = BufferIntUtil.getToFloatBuffer(cubeVertices);
			// 将立方体的6个面（12个三角形）的数组包装成ByteBuffer
//			cubeFacetsBuffer = BufferIntUtil.getToByteBuffer(cubeFacets);
			// 将立方体的纹理贴图的座标数据包装成FloatBuffer
			cubeTexturesBuffer = BufferIntUtil.getToFloatBuffer(cubeTextures);
			
			
			cube2VerticesBuffer = BufferIntUtil.getToFloatBuffer(cube2Vertices);
			cube2TexturesBuffer = BufferIntUtil.getToFloatBuffer(cube2Textures);
			
			cube3VerticesBuffer = BufferIntUtil.getToFloatBuffer(cube3Vertices);
			cube3TexturesBuffer = BufferIntUtil.getToFloatBuffer(cube3Textures);
			//背景啤酒
			cube4VerticesBuffer = BufferIntUtil.getToFloatBuffer(cube4Vertices);
			cube4TexturesBuffer = BufferIntUtil.getToFloatBuffer(cube4Textures);
			//泡泡
			bubbleVerticesBuffer = BufferIntUtil.getToFloatBuffer(bubbleVertices);
			bubbleTexturesBuffer = BufferIntUtil.getToFloatBuffer(bubbleTextures);
			
			//波浪
			int k = 0;
			float tempF1 = 8f / 128f;
			float tempF2 = 8f / 32f;
			//方块排序，从左到右放在数组里
			for(int j = 0;j < 32;j++) {
				for(int i = 0;i < 128;i++) {
					waveVertices[k++] = tempF1 * i - 4;
					waveVertices[k++] = 4 -  tempF2 * j;
					waveVertices[k++] = 0;
					waveVertices[k++] = waveVertices[k - 4];
					waveVertices[k++] = waveVertices[k - 4] - tempF2;
					waveVertices[k++] = 0;
					waveVertices[k++] = waveVertices[k - 7] + tempF1;
					waveVertices[k++] = waveVertices[k - 4];
					waveVertices[k++] = 0;
					
					waveVertices[k++] = waveVertices[k - 10];
					waveVertices[k++] = waveVertices[k - 10];
					waveVertices[k++] = 0;
					waveVertices[k++] = waveVertices[k - 7];
					waveVertices[k++] = waveVertices[k - 4];
					waveVertices[k++] = 0;
					waveVertices[k++] = waveVertices[k - 10];
					waveVertices[k++] = waveVertices[k - 10];
					waveVertices[k++] = 0;
				}
			}
			k = 0;
			float tempF3 = 1f / 128f;
			float tempF4 = 1f / 32f;
			for(int j = 0;j < 32;j++) {
				for(int i = 0;i < 128;i++) {
					waveTextures[k++] = tempF3 * i;
					waveTextures[k++] = tempF4 * j;
					waveTextures[k++] = waveTextures[k - 3];
					waveTextures[k++] = waveTextures[k - 3] + tempF4;
					waveTextures[k++] = waveTextures[k - 5] + tempF3;
					waveTextures[k++] = waveTextures[k - 3];
					
					waveTextures[k++] = waveTextures[k - 7];
					waveTextures[k++] = waveTextures[k - 7];
					waveTextures[k++] = waveTextures[k - 5];
					waveTextures[k++] = waveTextures[k - 3];
					waveTextures[k++] = waveTextures[k - 7];
					waveTextures[k++] = waveTextures[k - 7];
				}
			}
//			for(int i = 0;i < waveVertices.length;i++){
//				waveVerticesCopy[i] = waveVertices[i];
//			}
			waveVerticesCopy = (float[])waveVertices.clone();
			for(int i = 0; i < 64;i++)
			waveVerticesBuffer = BufferIntUtil.getToFloatBuffer(waveVertices);
			waveTexturesBuffer = BufferIntUtil.getToFloatBuffer(waveTextures);
			
			for(int i = 0;i < 128;i++) {
				pathCurX[i] = 0;
				theWay[i] = 0;
			}
			
			float[] waveVertices1 = {
					-4f, 4f, 0f,
					-4f, -4f, 0f,
					4f, -4f, 0f,
					-4f, 4f, 0f,
					4f, 4f, 0f,
					4f, -4f, 0f
			};
			float[] waveTextures1= {
					0f, 0f, 
					0f, 1f,
					1f, 1f,
					0f, 0f,
					1f, 0f,
					1f, 1f
			};
			pointPowers = new ArrayList<PointPower>();
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
			float ratio = (float) width / height;
			// 调用此方法设置透视视窗的空间大小。
			gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
		}
		private float xx = 1.0f;
		private float inc = 0.01f;
		private float incP = 0.25f;
		private float p = 2.0f;
		int ii = 0;
		int jj = 1;
		private int tempInx = 128;
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
	        
			// --------------------绘制第一个图形---------------------
			gl.glLoadIdentity();
			// 把绘图中心移入屏幕2个单位
			gl.glTranslatef(0f, 0.0f, -3.0f);
			//---------------------------------------------------------------------
			gl.glPushMatrix();
			gl.glTranslatef(0f, 0f, 0f);
			gl.glScalef(3f, 3f, 1f);
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, cube4VerticesBuffer);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, cube4TexturesBuffer);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[69]);
//			gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
			gl.glPopMatrix();
			//--------------------------------------------------------------------
			gl.glPushMatrix();
			gl.glTranslatef(0f, 0f, -0.0f);
//			gl.glColor4f(0f, 0f, 1f, 1f);
			// 设置顶点的位置数据
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, cubeVerticesBuffer);
			// 设置贴图的的座标数据
//			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, cube2TexturesBuffer);
			gl.glEnable(GL10.GL_DITHER);
			float temp1 = -beer.getAccelX() * 9f;
			float temp2 = Math.abs(temp1);
			float temp3 = (temp2 * temp2 / 180f + temp2 / 2.0f);
			if(temp1 < 0) temp3 *= -1; 
			double tempD = Math.tan(temp3 * Math.PI / 180.0);
//			temp3 = (3.5F + (float)(57.295780000000001D * StrictMath.atan2(beer.getAccelY(), beer.getAccelX()) - 90.0D));
			Log.i("aaa", temp3 + "   " + tempD);
			if(temp3 < -45) {
				if(tempD != 0) {
					changeCubeDatas(3, 1 / (float)(tempD), 1f);
					changeCubeDatas(1, 1 / -(float)(tempD), -1f);
					changeCubeDatas(2, 1 / -(float)(tempD), -1f);
					changeCubeDatas(0, 1 / -(float)(tempD), -1f);
//					changeCubeDatas(5, 1 / -1f, -1f);
					changeCubeDatas(4, 1 / -1f, 1f);
				}
			} else if(temp3 <= 45) {
				changeCubeDatas(3, -1f, -(float)(tempD));
				changeCubeDatas(4, -1f, -(float)(tempD));
				changeCubeDatas(1, 1f, (float)(tempD));
				changeCubeDatas(2, 1f, (float)(tempD));
				changeCubeDatas(0, 1 / 1f, -1f);
				changeCubeDatas(5, 1 / -1f, -1f);
			} else if (temp3 > 45) {
				if(tempD != 0) {
					changeCubeDatas(2, 1 / (float)(tempD), 1f);
					changeCubeDatas(3, 1 / -(float)(tempD), -1f);
					changeCubeDatas(4, 1 / -(float)(tempD), -1f);
					changeCubeDatas(5, 1 / -(float)(tempD), -1f);
//					changeCubeDatas(0, 1 / 1f, -1f);
					changeCubeDatas(1, 1 / 1f, 1f);
				}
			}
//			if(ii < 99) {
//				changeCubeDatas(5, (ii / 100f - 1f), -1f);
//			} else {
//				ii = 0;
//				changeCubeDatas(5, -1f, -1f);
//			}
			ii++;
			gl.glScalef(3f, 3f, 1f); 
			gl.glRotatef(180f, 0, 0, 1);
//			gl.glMatrixMode(GL10.GL_TEXTURE);
//			gl.glPushMatrix();
//			gl.glLoadIdentity();
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, cubeTexturesBuffer);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[1]);
//			
//			gl.glPopMatrix();
//			gl.glMatrixMode(GL10.GL_MODELVIEW);
			
			
//			gl.glMatrixMode(GL10.GL_TEXTURE);
//			gl.glPushMatrix();
//			gl.glLoadIdentity();
//		    gl.glTranslatef(10.0f, 0.0f, 0.0f);
//		    gl.glTranslatef(0.0f, 10.0f, 0.0f);
////		    glTranslatef(0.0f, 0.0f, offset);
//			gl.glPopMatrix();
//			gl.glMatrixMode(GL10.GL_MODELVIEW);
			// 执行纹理贴图
//			gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
			// 按cubeFacetsBuffer指定的面绘制三角形
//						gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4,
//							GL10.GL_UNSIGNED_BYTE, cubeFacetsBuffer);
//			gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 6);
			gl.glFlush();
			
			//----------------------------------------------------------------------
			gl.glPopMatrix();
//			画泡泡
//			for(int i = 0;i < 8;i ++) {
//				bubbles[i].draw(gl);
//				bubbles[i].move();
//			}
//			
			//-------------------------------------------------------------------------
			gl.glTranslatef(0f, 0f, -0.0f);
			gl.glColor4f(1f, 1f, 1f, 0f);
			
			if(beer.getAccelY() < 10.0f) {
				gl.glRotatef(temp3, 0, 0, 1);
//				Log.i("aaa", beer.getAccelX() + "");
			} else {
				gl.glRotatef(0, 0, 0, 1);
			}
			// 设置顶点的位置数据
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, waveVerticesBuffer);
			// 设置贴图的的座标数据
			gl.glScalef(1f, 0.25f, 1f);
			// 旋转图形
			
			
			//波浪效果
//			if(tempInx != 0) {
//				tempInx--;
//				func(tempInx);
//			}
			changeWave();
			solveMoving();
			waveVerticesBuffer = BufferIntUtil.getToFloatBuffer(waveVertices);
			// 执行纹理贴图
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, waveTexturesBuffer);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[(int)(p + 0.5f)]);
			
			gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 6 * 32 * 128);
			
			
			
			// 绘制结束
			gl.glFinish();
			// 禁用顶点、纹理座标数组
			gl.glDisable(GL10.GL_BLEND);
			gl.glDisableClientState(GL10.GL_TEXTURE_2D);
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
		public void func(int p) {
//			if(waveAddTemp[p] == -999) {
//				waveAddTemp[p] = 0;
//				waveAddEach[p] = 1;
//			}
		}
		public void changeWave() {
			int time;
			for(int j = 0;j < 128;j++) {
				if(pathMaxX[j] == 0) {
					continue;
				}
				if(pathCurX[j] == 0) {
					speedV[j] = pathMaxX[j] / 5.0f;
				} else if(pathCurX[j] >= pathMaxX[j] && theWay[j] > 0) {
					theWay[j] *= -1;
					speedV[j] = pathMaxX[j] / 6.0f;
					pathMaxX[j] *= 0.6f;
				} else if(pathCurX[j] <= -pathMaxX[j] && theWay[j] < 0) {
					theWay[j] *= -1;
					speedV[j] = pathMaxX[j] / 6.0f;
					pathMaxX[j] *= 0.6f;
				} 
					
				if(Math.abs(pathMaxX[j]) < 0.03) {
					pathMaxX[j] = 0; 
					speedV[j] = 0;
					pathCurX[j] = 0;
					theWay[j] = 0;
					resetWaveVertivesColumn(j);
				} else {
					pathCurX[j] += speedV[j] * theWay[j];
					changeWaveVerticesColumn(j, speedV[j] * theWay[j]);
				}
			}
		}
		public void solveMoving() {
			for(int i = pointPowers.size() - 1;i >= 0;i--) {
				if(pointPowers.get(i).power < 0.03f || pointPowers.get(i).inx < 0 
						|| pointPowers.get(i).inx > 127) {
					Log.i("ggg", "i=" + i + "  " + pointPowers.get(i).inx);
					pointPowers.remove(i);
				}
			}
			boolean[] flag = new boolean[128];
			boolean[] flag2 = new boolean[128];
			
			int[] wayly = new int[128];
			float[] powerly = new float[128];
			for(int i = 0;i < 128;i++) {
				flag[i] = false;
				flag2[i] = false;
				wayly[i] = 0;
				powerly[i] = 0f;
			}
			for(int i = pointPowers.size() - 1;i >= 0;i--) {
				if(wayly[pointPowers.get(i).inx] == 0) {
					wayly[pointPowers.get(i).inx] = pointPowers.get(i).way;
					powerly[pointPowers.get(i).inx] = pointPowers.get(i).power;
				} else {
					flag[pointPowers.get(i).inx] = true;
					if(wayly[pointPowers.get(i).inx] * pointPowers.get(i).way > 0) {
						powerly[pointPowers.get(i).inx] += pointPowers.get(i).power;
					} else {
						if(powerly[pointPowers.get(i).inx] > pointPowers.get(i).power) {
							powerly[pointPowers.get(i).inx] -= pointPowers.get(i).power;
						} else {
							powerly[pointPowers.get(i).inx] = pointPowers.get(i).power - powerly[pointPowers.get(i).inx];
							wayly[pointPowers.get(i).inx] = pointPowers.get(i).way;
						}
					}
				}
				
			}
			for(int i = 0;i < pointPowers.size();i++){
//				if(flag[pointPowers.get(i).inx]) {
					if(!flag2[pointPowers.get(i).inx]) {
						flag2[pointPowers.get(i).inx] = true;
						if(wayly[pointPowers.get(i).inx] == 0) {
						} else {
							if(theWay[pointPowers.get(i).inx] == 0) {
								theWay[pointPowers.get(i).inx] = 1;
								pathMaxX[pointPowers.get(i).inx] += powerly[pointPowers.get(i).inx];
							} else {
								if(theWay[pointPowers.get(i).inx] == 1) {
									pathMaxX[pointPowers.get(i).inx] += powerly[pointPowers.get(i).inx];
								} else {
									if(powerly[pointPowers.get(i).inx] < pathMaxX[pointPowers.get(i).inx]) {
										pathMaxX[pointPowers.get(i).inx] -= powerly[pointPowers.get(i).inx];
									} else {
										pathMaxX[pointPowers.get(i).inx] = powerly[pointPowers.get(i).inx] - pathMaxX[pointPowers.get(i).inx];
										theWay[pointPowers.get(i).inx] = 1;
									}
								}
							}
						}
					}
					Log.i("hhh", theWay[pointPowers.get(i).inx] + "  " + pathMaxX[pointPowers.get(i).inx]);
//				} else {
//					pathMaxX[pointPowers.get(i).inx] += powerly[pointPowers.get(i).inx];
//				}
				
//				if(pointPowers.get(i).power > 0.03f && theWay[pointPowers.get(i).inx] == 0) {
//					theWay[pointPowers.get(i).inx] = 1;
//				}
				if(pathMaxX[pointPowers.get(i).inx] > 0.3f){
					pathMaxX[pointPowers.get(i).inx] = 0.3f;
				}
				if(theWay[pointPowers.get(i).inx] == -1) {
					speedV[pointPowers.get(i).inx] = pathMaxX[pointPowers.get(i).inx] / 5.0f; 
				} else {
					speedV[pointPowers.get(i).inx] = pathMaxX[pointPowers.get(i).inx] / 4.0f; 
				}
				pointPowers.get(i).power *= 0.98f;
				pointPowers.get(i).inx += pointPowers.get(i).way;
				Log.i("ggg", "i=" + i + "  " + pointPowers.get(i).inx);
			}
		}
//		public void changeWave() {
//			for(int j = 0;j < 128;j++) {
//				if(waveAddEach[j] == 0) {
//					continue;
//				}
//				
//				if(waveAddTemp[j] + waveAddEach[j] > waveMax[j]) {
//					waveAddEach[j] = -waveAddEach[j];
//					waveAddEach[j] *= 0.9f;
//					waveMax[j] *= 0.8f;
////					waveAddTemp[j] = waveMax[j]; 
//				} else if(waveAddTemp[j] + waveAddEach[j] < -waveMax[j]) {
//					waveAddEach[j] = -waveAddEach[j];
//					waveAddEach[j] *= 0.9f;
//					waveMax[j] *= 0.8f;
////					waveAddTemp[j] = -waveMax[j]; 
//				}
//				if(waveAddEach[j] < 0.02f && waveAddEach[j] > -0.02f) {
//					if(waveAddTemp[j] > -0.04f && waveAddTemp[j] < 0.04f) {
//						waveAddTemp[j] = -waveAddEach[j];
//						Log.i("ddd", j + "");
//					}
//				}
//				waveAddTemp[j] += waveAddEach[j];
//				changeWaveVerticesColumn(j, waveAddEach[j]);
//				if(waveAddTemp[j] == 0) {
//					waveAddEach[j] = 0;
//					waveMax[j] = 1f; 
//					resetWaveVertivesColumn(j);
//				}
//			}
//		}
		//第i行，第j列的纵坐标增加val
		public void changeWaveVerticesSquare(int i, int j, float val) {
			int base = 18 * (i * 128 + j);//第几个方格的左上角索引
			for(int k = 0;k < 6;k ++) {
				waveVertices[base + 1 + k * 3] += val;
			}
		}
		public void changeWaveVerticesColumn(int j, float val) {
			for(int i = 0;i < 32;i++) {
				changeWaveVerticesSquare(i, j, val);
			}
		}
		public void resetWaveVertivesColumn(int j) {
			int base ;
		
			for(int i = 0;i < 32;i++) {
				base = 18 * (i * 128 + j);
				for(int k = 0;k < 18;k ++) {
					waveVertices[base + k] = waveVerticesCopy[base + k];
				}
			}
		}
		int[] textures;
		Bitmap[] bitmaps;
		Bubble[] bubbles;
		//inx从0开始
		public FloatBuffer changeVertice(float[] datas, int inx, float x, float y) {
			datas[inx * 3 + 0] = x;
			datas[inx * 3 + 1] = y;
			FloatBuffer dataBuffer = BufferIntUtil.getToFloatBuffer(datas);
			return dataBuffer;
		}
		//inx从0开始
		public FloatBuffer changeTexture(float[] datas, int inx, float x, float y) {
			datas[inx * 2 + 0] = x;
			datas[inx * 2 + 1] = y;
			FloatBuffer dataBuffer = BufferIntUtil.getToFloatBuffer(datas);
			return dataBuffer;
		}
		//传入图形位置值
		public void changeCubeDatas(int inx, float x, float y) {
			float temp;
			float newX = x / 2.0f;
			float newY = y / 2.0f;
			if(newX * newY != 0) {
				newX = newX;
				newY = -newY;
			} else {
				if(newX == 0) {
					temp = newX;
					newX = -newX;
					newY = -newY;
				}
			}
			newX += 0.5;
			newY += 0.5;
			
			cubeVerticesBuffer = changeVertice(cubeVertices, inx, x, y);
//			if(jj == 0) {
//				jj = 1;
				cubeTexturesBuffer = changeTexture(cubeTextures, inx, newX, newY);
//			} else jj = 0;
		}
		public void changeCubeDatas1(int inx, float x, float y) {
			float temp;
			float newX = x / 2.0f;
			float newY = y / 2.0f;
			if(newX * newY != 0) {
				newX = newX;
				newY = -newY;
			} else {
				if(newX == 0) {
					temp = newX;
					newX = -newX;
					newY = -newY;
				}
			}
			newX += 0.5;
			newY += 0.5;
			
			cubeVerticesBuffer = changeVertice(cubeVertices, inx, x, y);
//			cubeTexturesBuffer = changeTexture(cubeTextures, inx, newX, newY);
		}
		private void loadTexture(GL10 gl)
		{
			bitmaps = new Bitmap[71];
			try
			{
				// 加载位图
				bitmaps[0] = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.foam);
				bitmaps[1] = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.slime);
				
				textures = new int[71];
				// 指定生成N个纹理（第一个参数指定生成1个纹理），
				// textures数组将负责存储所有纹理的代号。
				gl.glGenTextures(71, textures, 0);
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
				bitmaps[69] = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.lager);
				for(int i = 68;i < 70;i ++) {
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
				if(y > 0) {
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
		
		
	}
	
}
//class PointPower {
//	int way;//方向1表示向右，-1表示向左
//	int inx;//振中此时的位置0~127
//	float power;//此时振中的力量
//	public PointPower(int way, int inx, float power) {
//		this.way = way;
//		this.inx = inx;
//		this.power = power;
//	}
//}