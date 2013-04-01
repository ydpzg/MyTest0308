package com.example.mytest0308;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
import android.content.res.Resources;
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
public class testTexture3D extends Activity
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
		LoadImage.load(getResources());
		// 创建一个GLSurfaceView，用于显示OpenGL绘制的图形
		GLSurfaceView glView = new GLSurfaceView(this);
		// 创建GLSurfaceView的内容绘制器
		MyRenderer myRender = new MyRenderer();
		glView.getHolder().setFormat(PixelFormat.TRANSLUCENT); 
		glView.setEGLConfigChooser(8, 8, 8, 8, 16, 0); 
		// 为GLSurfaceView设置绘制器
		glView.setRenderer(myRender);

		setContentView(glView);
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	@Override
	public boolean onTouchEvent(MotionEvent me)
	{
		// 将该Activity上的触碰事件交给GestureDetector处理
		return true;
		//return detector.onTouchEvent(me);
	}

	public class MyRenderer implements Renderer {
	    // Points 网格顶点数组(45,45,3)
	    float vertex[][][] = new float[45][45][3];
	    // 指定旗形波浪的运动速度
	    int wiggle_count = 0;
	    // 临时变量
	    float hold, hold1;
	    // x,y,z转角
	    float xrot, yrot, zrot;
	    // vertex
	    private float[] texVertex = new float[12];
	    // vetexBuffer
	    private FloatBuffer vertexBuffer;
	    // coord
	    private float[] coord = new float[8];
	    // coordBuffer
	    private FloatBuffer coordBuffer;
	    // textures
	    private int[] textures = new int[1];

	    // init()
	    public void init() {
	        ByteBuffer texByteBuffer = ByteBuffer
	                .allocateDirect(texVertex.length * 4);
	        texByteBuffer.order(ByteOrder.nativeOrder());
	        vertexBuffer = texByteBuffer.asFloatBuffer();
	        vertexBuffer.put(texVertex);
	        vertexBuffer.position(0);

	        ByteBuffer coordByteBuffer = ByteBuffer
	                .allocateDirect(coord.length * 4);
	        coordByteBuffer.order(ByteOrder.nativeOrder());
	        coordBuffer = coordByteBuffer.asFloatBuffer();
	        coordBuffer.put(coord);
	        coordBuffer.position(0);
	    }

	    @Override
	    public void onDrawFrame(GL10 gl) {
	        init();
	        int x, y; // 循环变量
	        float _x, _y, _xb, _yb; // 用来将旗形的波浪分割成很小的四边形

	        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT); // 清除屏幕和深度缓冲
	        gl.glLoadIdentity(); // 重置当前的模型观察矩阵

	        gl.glTranslatef(0.0f, 0.0f, -12.0f); // 移入屏幕12个单位
	        gl.glScalef(2f, 1f, 1f);
//	        gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f); // 绕 X 轴旋转
//	        gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f); // 绕 Y 轴旋转
//	        gl.glRotatef(zrot, 0.0f, 0.0f, 1.0f); // 绕 Z 轴旋转

	        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

	        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
	        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, coordBuffer);

	        for (x = 0; x < 44; x++) {
	            for (y = 0; y < 44; y++) {
	                _x = (float) (x) / 44.0f; // 生成X浮点值
	                _y = (float) (y) / 44.0f; // 生成Y浮点值
	                _xb = (float) (x + 1) / 44.0f; // X浮点值+0.0227f
	                _yb = (float) (y + 1) / 44.0f; // Y浮点值+0.0227f

	                coordBuffer.clear();
	                // bottom left
	                coordBuffer.put(_x);
	                coordBuffer.put(_y);
	                // bottom right
	                coordBuffer.put(_x);
	                coordBuffer.put(_yb);
	                // top right
	                coordBuffer.put(_xb);
	                coordBuffer.put(_yb);
	                // top left
	                coordBuffer.put(_xb);
	                coordBuffer.put(_y);

	                vertexBuffer.clear();
	                // bottom left
	                vertexBuffer.put(vertex[x][y][0]);
	                vertexBuffer.put(vertex[x][y][1]);
	                vertexBuffer.put(vertex[x][y][2]);
	                // bottom right
	                vertexBuffer.put(vertex[x][y + 1][0]);
	                vertexBuffer.put(vertex[x][y + 1][1]);
	                vertexBuffer.put(vertex[x][y + 1][2]);
	                // top right
	                vertexBuffer.put(vertex[x + 1][y + 1][0]);
	                vertexBuffer.put(vertex[x + 1][y + 1][1]);
	                vertexBuffer.put(vertex[x + 1][y + 1][2]);
	                // top left
	                vertexBuffer.put(vertex[x + 1][y][0]);
	                vertexBuffer.put(vertex[x + 1][y][1]);
	                vertexBuffer.put(vertex[x + 1][y][2]);

	                gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
	            }
	        } 
	        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

	        if (wiggle_count == 1) { // 用来降低波浪速度(每隔2帧一次)
	            for (int j = 0; j < 45; j++) {
	                hold = vertex[0][j][1]; // 存储当前左侧波浪值
	                hold1 = vertex[1][j][1]; // 存储当前左侧波浪值
	                for (int i = 0; i < 43; i++) {
	                    // 当前波浪值等于其右侧的波浪值
	                    vertex[i][j][1] = vertex[i + 2][j][1];
	                }
	                vertex[43][j][1] = hold;
	                vertex[44][j][1] = hold1;
	            }
	            wiggle_count=0;
	        } 
	        wiggle_count++;
	        xrot += 0.3f; // X 轴旋转
	        yrot += 0.2f; // Y 轴旋转
	        zrot += 0.4f; // Z 轴旋转
	    }

	    @Override
	    public void onSurfaceChanged(GL10 gl, int width, int height) {
	        float ratio = (float) width / (float) height;
	        // 设置OpenGL场景的大小
	        gl.glViewport(0, 0, width, height);
	        // 设置投影矩阵
	        gl.glMatrixMode(GL10.GL_PROJECTION);
	        // 重置投影矩阵
	        gl.glLoadIdentity();
	        // 设置视口的大小
	        gl.glFrustumf(-ratio, ratio, -1, 1, 1, 15);
	        // 选择模型观察矩阵
	        gl.glMatrixMode(GL10.GL_MODELVIEW);
	        // 重置模型观察矩阵
	        gl.glLoadIdentity();
	    }

	    @Override
	    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
	        // 黑色背景
	        gl.glClearColor(0, 0, 0, 0);
	        // 启用阴影平滑
	        gl.glShadeModel(GL10.GL_SMOOTH);
	        // 启用深度测试
	        gl.glEnable(GL10.GL_DEPTH_TEST);
	        // 启用纹理映射
	        gl.glClearDepthf(1.0f);
	        // 深度测试的类型
	        gl.glDepthFunc(GL10.GL_LEQUAL);
	        // 精细的透视修正
	        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
	        // 允许2D贴图,纹理
	        gl.glEnable(GL10.GL_TEXTURE_2D);

	        gl.glEnable(GL10.GL_LEQUAL);

	        // 创建纹理
	        gl.glGenTextures(1, textures, 0);
	        // 设置要使用的纹理
	        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

	        // 生成纹理
	        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, LoadImage.bitmap, 0);
	        // 线形滤波
	        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
	                GL10.GL_LINEAR);
	        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
	                GL10.GL_LINEAR);

	        //NEW 
	        // 沿X平面循环
	        for (int x = 0; x < 45; x++) {
	            // 沿Y平面循环
	            for (int y = 0; y < 45; y++) {
	                // 向表面添加波浪效果
	                // -4.5f~4.5f
	                vertex[x][y][0] = ((float) x / 5.0f) - 4.5f;
	                vertex[x][y][2] = 0f;
	                // 0-2*pai
//	                vertex[x][y][1] = (float) (Math
//	                        .sin(((((float) x / 5.0f) * 40.0f) / 360.0f) * 3.141592654 * 2.0f));
	                vertex[x][y][1] = ((float) y / 5.0f) - 4.5f +  0.5f * (float) (Math
	                        .sin(((((float) x / 5.0f) * 40.0f * 3f) / 360.0f) * 3.141592654 * 2.0f));
	            }
	        } 
	    }
	}
}
class LoadImage {
	public static Bitmap bitmap;
	
	public static void load(Resources res) {
		bitmap = BitmapFactory.decodeResource(res, R.drawable.foam);
	}
}
class PointPower {
	int way;//方向1表示向右，-1表示向左
	int inx;//振中此时的位置0~127
	float power;//此时振中的力量
	public PointPower(int way, int inx, float power) {
		this.way = way;
		this.inx = inx;
		this.power = power;
	}
}