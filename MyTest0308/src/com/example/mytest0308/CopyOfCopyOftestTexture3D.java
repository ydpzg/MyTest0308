package com.example.mytest0308;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

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
	
	private float currentBeerHeight = -2f;
	private boolean foamShakeUp = false;
	private boolean bottonBroken = false;
	
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
	        		//超过一定程度波浪往上变大
	        		if(Math.abs(accelerationX - acceLastX) > 1f) {
	        			bottonBroken = true;
	        		}
	        		if(Math.abs(accelerationX - acceLastX) > 0.5f) {
	        			foamShakeUp = true;
	        		}
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
	        	if (accelerationY * accelerationY - paramSensorEvent.values[1] * paramSensorEvent.values[1] > 10.550000000000001D) {
	        		bool = true;
	        		Log.i("ping", accelerationY * accelerationY - paramSensorEvent.values[1] * paramSensorEvent.values[1] + "");
	        	}
	         
	        	if (beerReady)
	        	{
	        		beer.setAccelX(accelerationX);
	        		beer.setAccelY(accelerationY);
	        		beer.setAccelerationX(accelDiffX);
	        		beer.setAccelerationY(accelDiffY);
	        		beer.setShakeFlag(bool);
	        	}
	        	currentAngle = (3.5F + (float)(57.295780000000001D * StrictMath.atan2(beer.getAccelY(), beer.getAccelX()) - 90.0D));
	        	if(currentAngle > 70) {
	        		currentAngle = 70f;
	        	} else if(currentAngle < -200) {
	        		currentAngle = 70f;
	        	} else if(currentAngle < -70) {
	        		currentAngle = -70f;
	        	}
	        	
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
		private float[] mBubbleListX = new float[200];
		private float[] mBubbleListY = new float[200];
		private float[] mBubbleListAccX = new float[200];
		private float[] mBubbleListAccY = new float[200];
		private float[] mBubbleListAge = new float[200];
		private int mBubbleMaxAge = 8;
		private Random bubbleRandom = new Random();
		
		private boolean firstDraw = true;
		private int state = 0;
		
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
			
			for(int i = 0;i < 20;i++) {
				mBubbleListAccX[i] = 0.02f;
				mBubbleListAccY[i] = 0.12f;
				mBubbleListAge[i] = 0;
			}
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
		  
		    gl.glAlphaFunc(GL10.GL_GREATER, 0.1f);  // Set Alpha Testing (To Make Black Transparent)   
		  
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
//			ratio = 240 * (float) width / height;
			
			// 调用此方法设置透视视窗的空间大小。
			gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
//			gl.glOrthof(-this.ratio, this.ratio, -240.0F, 240.0F,
//					-8.5F, 8.5F);
		}
		private float xx = 1.0f;
		private float inc = 0.01f;
		private float incCurrTime = 0.25f;
		private float currTime = 3.0f;
		private boolean foamUpChange = false;
		private boolean foamDownChange = false;
		private int upScaleCount = 0;
		private float currTimeMax = 67f;
		private float currTimeMin = 58f;
		
		public void onDrawFrame(GL10 gl)
		{
			if(bottonBroken) {
				state = 2;
			}
			if(state == 0) {
				if(2f - currentBeerHeight < 0.01f) {
					state = 1;
				}
				currentBeerHeight += 0.05f;
			} else if(state == 1) {
				//倒水
				if(Math.abs(currentAngle) >= 60) {
					currentBeerHeight -= 0.05f;
				}
			} else if(state == 2) {
				currentBeerHeight -= 0.05f;
			}
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
			//-----------------------------泡泡-------------------------------------
			gl.glPushMatrix();
			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_DST_COLOR);
			gl.glColor4f(1f, 1f, 1f, 0.5f);   // 全亮度， 50% Alpha 混合
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, bubbleVerticesBuffer);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, bubbleTexturesBuffer);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[68]);
			float tempScal = 1f;
			
			for(int i = 0;i < 30;i++) {
				gl.glPushMatrix();
				gl.glScalef(0.6f, 0.6f, 1f);
				tempScal = (mBubbleListAge[i] / (float)mBubbleMaxAge);
				if(mBubbleListAge[i] == 0) {
					mBubbleListAge[i]++;
					mBubbleListX[i] = bubbleRandom.nextInt(100) / 20f - 2.5f;
					mBubbleListY[i] = bubbleRandom.nextInt(200) / 20f - 5f;
					mBubbleListAccX[i] = 0f;
					mBubbleListAccY[i] = 0f;
				} else if(mBubbleListAge[i] < mBubbleMaxAge - 1) {
					mBubbleListAge[i]++;
				} else if(mBubbleListAge[i] == mBubbleMaxAge - 1) {
					mBubbleListAge[i]++;
					tempScal = 1f;
				} else if(mBubbleListAge[i] == mBubbleMaxAge) {
				
					mBubbleListX[i] += mBubbleListAccX[i];
					mBubbleListY[i] += mBubbleListAccY[i];
					mBubbleListAccY[i] += 0.006f;
					if(mBubbleListY[i] > 5) {
						mBubbleListAge[i] = 0;
					}
				}
				gl.glTranslatef(mBubbleListX[i], mBubbleListY[i], 0f);
				gl.glScalef(tempScal, tempScal, 1f);
				gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
				gl.glPopMatrix();
			}
			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA); 
			gl.glPopMatrix();
			//--------------------------背景泡沫-----------------------------------------------
			gl.glPushMatrix();
			gl.glTranslatef(0f, 0f, -0.0f);
			gl.glColor4f(1f, 1f, 1f, 0f);
			gl.glTranslatef(0f, -Math.abs(currentAngle) / 150f, 0F);
			gl.glScalef(ratio * 5, 5f, 1f);
			gl.glRotatef(currentAngle / 30f, 0.0F, 0.0F, 1.0F);

//			gl.glScalef(ratio, 1f, 1f);
			changeSlimeVertives();
			// 设置顶点的位置数据
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, slimeVerticesBuffer);
			// 执行纹理贴图
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, slimeTexturesBuffer);
			if(state == 0) {
				gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[69]);
			} else if(state == 1 || state == 2) {
				gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
				
			}
			
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 45 * 2);
			gl.glPopMatrix();
			//--------------------------前景波浪-----------------------------------------------
			gl.glPushMatrix();
			gl.glTranslatef(0f, 0.5f + currentBeerHeight, -0.0f);
			gl.glColor4f(1f, 1f, 1f, 0f);
			gl.glRotatef(currentAngle, 0f, 0f, 1f);
			if(foamShakeUp && state == 1) {
				foamShakeUp = false;
				foamUpChange = true;
				currTimeMax = 67;
				currTimeMin = 4;
				currTime = 58;
				incCurrTime = -3;
				Log.i("iii", "foamShakeUp=" + foamShakeUp);
			}
			if(foamUpChange) {
				if(upScaleCount < 300) {
					upScaleCount += 15;
				} else {
					foamDownChange = true;
					foamUpChange = false;
				}
			} else if(foamDownChange) {
				if(upScaleCount > 0) {
					upScaleCount -= 2;
				} else {
					foamDownChange = false;
					currTimeMin = 58f;
					currTimeMax = 67f;
					incCurrTime = 0.4f;
				}
			}
			gl.glScalef(6f, 1.5f, 1f);

			freshWave();
			if(downHand) {
				downHand = false;
				putAWaveSor(44, tempRange);
			}
			// 设置顶点的位置数据
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, foamVerticesBuffer);
			// 执行纹理贴图
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, foamTexturesBuffer);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[(int)(currTime + 0.5f)]);
			if(currTime >= currTimeMax) {
				incCurrTime = -0.4f;
			} else if(currTime <= currTimeMin) {
				incCurrTime = 0.4f;
			}
			currTime += incCurrTime;
			// 按cubeFacetsBuffer指定的面绘制三角形
//			gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4,
//				GL10.GL_UNSIGNED_BYTE, cubeFacetsBuffer);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 45 * 2);
			gl.glPopMatrix();
			//------------------------瓶破背景---------------------------------------
			if(state == 2) {
				gl.glPushMatrix();
				// 设置顶点的位置数据
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, cube2VerticesBuffer);
				// 设置贴图的的座标数据
				gl.glScalef(ratio * 3f, 3f, 1f);
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, cubeTexturesBuffer);
				gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[70]);
				gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
				gl.glPopMatrix();
			}
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
				slimeVerticesBuffer.put(3 * (2 * i + 1) + 1, (float)(-1f + 1 - tanAngle + (x + 1) * tanAngle) + currentBeerHeight / 10f * 2f);
				slimeVerticesBuffer.put(3 * (2 * i + 1) + 2, 0f);
				
				slimeTexturesBuffer.put(2 * (2 * i + 1), i * 1f / (45 - 1));
				slimeTexturesBuffer.put(2 * (2 * i + 1) + 1, 1 - (1 - (float)((1 - tanAngle) / 2.0 + x2 * tanAngle)) + currentBeerHeight / 10f * 1f);
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
			try
			{
				textures = new int[71];
				// 指定生成N个纹理（第一个参数指定生成1个纹理），
				// textures数组将负责存储所有纹理的代号。
				gl.glGenTextures(71, textures, 0);
				// 获取textures纹理数组中的第一个纹理
				
				loadBitmap(gl, R.drawable.slime, textures[0]);
				loadBitmap(gl, R.drawable.lager, textures[1]);
				for(int i = 2;i < 68;i ++) {
					// 加载位图生成纹理
					loadBitmap(gl, 2130837507 + i - 2, textures[i]);
				}
				// 加载位图生成纹理
				loadBitmap(gl, R.drawable.bubble32, textures[68]);
				loadBitmap(gl, R.drawable.black_tex, textures[69]);
				loadBitmap(gl, R.drawable.egg, textures[70]);
			}
			finally
			{
			}
		}
		void loadBitmap(GL10 gl, int resId, int textureId) {
			Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
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
			if (bitmap != null) {
				bitmap.recycle();
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