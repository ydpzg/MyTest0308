//package com.example.mytest0308;
//
//import java.lang.reflect.Array;
//import java.nio.ByteBuffer;
//import java.nio.ByteOrder;
//import java.nio.FloatBuffer;
//import java.nio.IntBuffer;
//import java.util.Random;
//import java.util.Timer;
//
//import javax.microedition.khronos.egl.EGLConfig;
//import javax.microedition.khronos.opengles.GL10;
//
//import com.example.mytest0308.Texture3D.MyRenderer;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.hardware.Sensor;
//import android.hardware.SensorEventListener;
//import android.opengl.GLSurfaceView;
//import android.opengl.GLUtils;
//import android.opengl.GLSurfaceView.Renderer;
//import android.os.Bundle;
//import android.provider.Settings.Global;
//import android.view.GestureDetector;
//import android.view.GestureDetector.OnGestureListener;
//import android.view.MotionEvent;
//import android.view.SurfaceHolder;
//
///**
// * Description: <br/>
// * site: <a href="http://www.crazyit.org">crazyit.org</a> <br/>
// * Copyright (C), 2001-2012, Yeeku.H.Lee <br/>
// * This program is protected by copyright laws. <br/>
// * Program Name: <br/>
// * Date:
// * 
// * @author Yeeku.H.Lee kongyeeku@163.com
// * @version 1.0
// */
//public class iBree extends Activity {
//	private static final int MENU_ID = 10798;
//	public static final float PI = 3.141593F;
//	public static final String PREFS_NAME = "preferences";
//	protected static final float kFastFilteringFactor = 0.1F;
//	protected static final float kFilteringFactor = 0.05F;
//	public static final int rSOUND_Burp = 2131034114;
//	public static final int rSOUND_Button = 2131034122;
//	public static final int rSOUND_Fill = 2131034116;
//	public static final int rSOUND_Fizz = 2131034115;
//	public static final int rSOUND_FizzFade = 2131034120;
//	public static final int rSOUND_Shake = 2131034123;
//	public static final int rSOUND_Smash = 2131034113;
//	public boolean FadeFizzFlag;
//	public boolean FizzLoaded;
//	float[] I;
//	public int LoopSoundFile;
//	SensorEventListener MyListener;
//	float[] Rx;
//	public int SOUND_Burp;
//	public int SOUND_Button;
//	public int SOUND_Fill;
//	public int SOUND_Fizz;
//	public int SOUND_Shake;
//	boolean SounderSoundsLoaded;
//	public boolean TimerValidFlag = false;
//	public Context XYcontext;
//	public int XYsoundFile;
//	float accelDiffX;
//	float accelDiffY;
//	float accelerationX;
//	float accelerationY;
//	float[] accels;
//	float azimuth;
//	FluidView beer = new FluidView();
//	boolean beerReady;
//	boolean burpSound;
//	boolean buttonSound;
//	int displayHeight;
//	int displayWidth;
//	String eggString;
//	int eggtimer;
//	boolean fillSound;
//	public boolean first_run_flag;
//	boolean fizzSound;
//	private Timer helloTimer;
//	public boolean hitflag;
//	boolean isReady;
//	BasicGLSurfaceView mAndroidSurface;
//	SharedPreferences mPrefs;
//	float[] mags;
//	public long millisOpened;
//	Sensor myAcc;
//	float[] orients;
//	float[] outR;
//	float pitch;
//	float roll;
//	public boolean smashEnabled;
//	public boolean smashed;
//	private Timer startTimer;
//	float[] values = new float[3];
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		BasicGLSurfaceView glView = new BasicGLSurfaceView(this);
//		setContentView(glView);
//	}
//
//	private class BasicGLSurfaceView extends GLSurfaceView implements
//			SurfaceHolder.Callback {
//		iBree.BeerRenderer mRenderer = new iBree.BeerRenderer();
//
//		BasicGLSurfaceView(Context arg2) {
//			super(arg2);
//			setRenderer(this.mRenderer);
//		}
//
//		public boolean onTouchEvent(MotionEvent paramMotionEvent) {
//			return FadeFizzFlag;
//			// int i;
//			// int j;
//			// if (paramMotionEvent.getEventTime()
//			// - paramMotionEvent.getDownTime() > 2000L) {
//			// i = 1;
//			// if (!iBree.this.beer.mButtonView)
//			// break label54;
//			// j = 0;
//			// label33: if ((i & j) == 0)
//			// break label59;
//			// iBree.this.smashed = true;
//			// }
//			// while (true) {
//			// return true;
//			// i = 0;
//			// break;
//			// label54: j = 1;
//			// break label33;
//			// label59: fluid.this.smashed = false;
//			// fluid.this.beer.smashsoundhit = false;
//			// if (paramMotionEvent.getAction() != 1)
//			// continue;
//			// fluid.this.beer.ScreenHitFlag = true;
//			// }
//		}
//	}
//
//	class BeerRenderer implements GLSurfaceView.Renderer {
//		boolean InitOnceFlag;
//		float ratioFix;
//
//		BeerRenderer() {
//		}
//
//		public void onDrawFrame(GL10 paramGL10) {
//			if (!iBree.this.beer.DoneLoaded) {
//				iBree.this.beer.LoadPieces(paramGL10);
//			}
//			iBree.this.beer.draw(paramGL10);
//		}
//
//		public void onSurfaceChanged(GL10 paramGL10, int paramInt1,
//				int paramInt2) {
//			iBree.this.displayWidth = paramInt1;
//			iBree.this.displayHeight = paramInt2;
//			this.ratioFix = (240.0F * paramInt1 / paramInt2);
//			paramGL10.glViewport(0, 0, paramInt1, paramInt2);
//			paramGL10.glMatrixMode(GL10.GL_PROJECTION);
//			paramGL10.glLoadIdentity();
//			paramGL10.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
//			paramGL10.glOrthof(-this.ratioFix, this.ratioFix, -240.0F, 240.0F,
//					-8.5F, 8.5F);
//			paramGL10.glEnableClientState(32884);
//			paramGL10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
//			paramGL10.glShadeModel(7425);
//			paramGL10.glEnable(GL10.GL_TEXTURE_2D);
//			iBree.this.beer.mButtonView = true;
//		}
//
//		public void onSurfaceCreated(GL10 paramGL10, EGLConfig paramEGLConfig) {
//			iBree.this.beer.oglsetup(paramGL10);
//			iBree.this.beer.BeerDisplayMode = 0;
//		}
//
//		public void setHit(boolean paramBoolean) {
//			iBree.this.beer.ScreenHitFlag = true;
//		}
//
//		public void setSmashed(boolean paramBoolean) {
//			iBree.this.smashed = paramBoolean;
//		}
//	}
//
//	private class FluidView {
//		public float AccelX;
//		public float AccelY;
//		public float AccelerationX;
//		public float AccelerationY;
//		float[] AniVertices;
//		public int Ani_count;
//		public boolean BackFoamFlag;
//		public int BeerDisplayMode;
//		public float BlueShade;
//		public int BubGeneratorCount;
//		public int[] BubbleListAge;
//		public boolean[] BubbleListDoneZoom;
//		public boolean[] BubbleListEnabled;
//		public float[] BubbleListXaccl;
//		public float[] BubbleListXpos;
//		public float[] BubbleListYaccl;
//		public float[] BubbleListYpos;
//		float[] ButtonVertices;
//		int ButxCount;
//		public int CurrentFoam;
//		public boolean DoneLoaded;
//		// public boolean FadeSlimeFlag;
//		public int FoamCount;
//		public float FoamFade;
//		public int FoamSkip;
//		public int FoamSkipCount;
//		public int[] Foamlist_EndFrame;
//		public boolean[] Foamlist_LoopEnabled;
//		public int[] Foamlist_NextFoam;
//		public int[] Foamlist_StartFrame;
//		public float GreenShade;
//		public int HeadZoomCount;
//		public float Hmult;
//		public float Hoffset;
//		public int MaxAgeStill;
//		public int MaxAgeZoom;
//		public int MaxBubbles;
//		public int MaxFoamCount;
//		int MaxHeadZoomCount;
//		public boolean PourFlip;
//		public double Rangle;
//		public float RedShader;
//		boolean ScreenHitFlag;
//		public boolean ShakeFlag;
//		public boolean ShakeXitionFlag;
//		public int SlimeDropPos;
//		public boolean SlimeReady;
//		public float[][] Slimepoints;
//		public float WaveOffSetVal;
//		public float[] XCoords;
//		public float[] Xpoints;
//		public float[] YCoords;
//		public float[] Ypoints;
//		public int blubcount;
//		float[] bubbleVertices;
//		public float[] ctemp;
//		public float currentAngle;
//		public int currentFluidHeight;
//		float dripXpar;
//		public float dt;
//		float[] elementVertices;
//		public int endcount;
//		public boolean endflag;
//		public boolean extraBounce;
//		public float fadeFactor;
//		public float ffx;
//		public float ffy;
//		public boolean firstflag;
//		public float fluidHeight;
//		float[] foamVertices;
//		public boolean foamflag;
//		public GL10 gl;
//		boolean glassFadedin;
//		public boolean glassPoured;
//		public float[] h;
//		public int i;
//		public int iButtonCount;
//		public int initBubCount;
//		public int loadPlace;
//		public IntBuffer mAniBuffer;
//		public FloatBuffer mAniVerticesBuffer;
//		public FloatBuffer mBubbleVertexBuffer;
//		public boolean mButtonDone;
//		public boolean mButtonExit;
//		public FloatBuffer mButtonVertices;
//		public boolean mButtonView;
//		public boolean mDone;
//		public FloatBuffer mElementVertexBuffer;
//		public IntBuffer mFoamTextureBuffer;
//		public FloatBuffer mFoamVertexBuffer;
//		public FloatBuffer mStandTextCoordsBuffer;
//		public FloatBuffer mTexCoordFoamBuffer;
//		public FloatBuffer mTexCoordSlimeBuffer;
//		public IntBuffer mTextureBuffer;
//		public FloatBuffer mVertexFoamBuffer;
//		public FloatBuffer mVertexSlimeBuffer;
//		float maxFill;
//		public float minusone;
//		public float newX;
//		public float newY;
//		public boolean newsmallfoamflag;
//		boolean playBurp;
//		boolean playFill;
//		public float plusone;
//		public int posOff;
//		public Random randomizer;
//		public boolean residueSetting;
//		public float sA;
//		public double sAd;
//		public float[] sXpoints;
//		public float[] sYpoints;
//		public boolean secondLoaded;
//		public boolean secondflag;
//		public int shakecount;
//		public int shaketime;
//		public int smallfoamcount;
//		public boolean smallfoamflag;
//		public boolean smashsoundhit;
//		float[] standardTextureCoordinates;
//		public int tempcount;
//		public float[] texcoord_foam;
//		public float[] texcoord_slime;
//		public int[] textures;
//		public int tpoint;
//		public float[] v;
//		public float[] vertex_foam;
//		public float[] vertex_slime;
//		public int x;
//		public float xFl;
//		public float xx;
//		public int y;
//		public float yoff;
//		public float zeropos;
//
//		private FluidView() {
//			int[] arrayOfInt = new int[2];
//			arrayOfInt[0] = 50;
//			arrayOfInt[1] = 50;
//			this.Slimepoints = ((float[][]) Array.newInstance(Float.TYPE,
//					arrayOfInt));
//			this.BubbleListAge = new int[1000];
//			this.BubbleListXpos = new float[1000];
//			this.BubbleListYpos = new float[1000];
//			this.BubbleListXaccl = new float[1000];
//			this.BubbleListYaccl = new float[1000];
//			this.BubbleListEnabled = new boolean[1000];
//			this.BubbleListDoneZoom = new boolean[1000];
//			this.randomizer = new Random(System.currentTimeMillis());
//			this.h = new float[100];
//			this.v = new float[100];
//			this.ctemp = new float[3];
//			this.MaxBubbles = 200;
//			this.MaxAgeZoom = 6;
//			this.MaxAgeStill = 7;
//			this.mDone = false;
//			this.mButtonView = false;
//			this.mButtonDone = false;
//			this.mButtonExit = false;
//			this.iButtonCount = 0;
//			this.BeerDisplayMode = 0;
//			this.maxFill = 0.95F;
//			this.dripXpar = 0.87F;
//			this.MaxHeadZoomCount = 25;
//			this.Xpoints = new float[50];
//			this.sXpoints = new float[50];
//			this.XCoords = new float[50];
//			this.Ypoints = new float[50];
//			this.sYpoints = new float[50];
//			this.YCoords = new float[50];
//			this.texcoord_foam = new float[2160];
//			float[] arrayOfFloat1 = new float[12];
//			arrayOfFloat1[0] = -160.0F;
//			arrayOfFloat1[1] = 240.0F;
//			arrayOfFloat1[2] = 0.0F;
//			arrayOfFloat1[3] = -160.0F;
//			arrayOfFloat1[4] = -240.0F;
//			arrayOfFloat1[5] = 0.0F;
//			arrayOfFloat1[6] = 160.0F;
//			arrayOfFloat1[7] = -240.0F;
//			arrayOfFloat1[8] = 0.0F;
//			arrayOfFloat1[9] = 160.0F;
//			arrayOfFloat1[10] = 240.0F;
//			arrayOfFloat1[11] = 0.0F;
//			this.elementVertices = arrayOfFloat1;
//			float[] arrayOfFloat2 = new float[12];
//			arrayOfFloat2[0] = -120.0F;
//			arrayOfFloat2[1] = 120.0F;
//			arrayOfFloat2[2] = 0.0F;
//			arrayOfFloat2[3] = -120.0F;
//			arrayOfFloat2[4] = -120.0F;
//			arrayOfFloat2[5] = 0.0F;
//			arrayOfFloat2[6] = 120.0F;
//			arrayOfFloat2[7] = -120.0F;
//			arrayOfFloat2[8] = 0.0F;
//			arrayOfFloat2[9] = 120.0F;
//			arrayOfFloat2[10] = 120.0F;
//			arrayOfFloat2[11] = 0.0F;
//			this.ButtonVertices = arrayOfFloat2;
//			float[] arrayOfFloat3 = new float[12];
//			arrayOfFloat3[0] = -40.0F;
//			arrayOfFloat3[1] = 60.0F;
//			arrayOfFloat3[2] = 0.0F;
//			arrayOfFloat3[3] = -40.0F;
//			arrayOfFloat3[4] = -60.0F;
//			arrayOfFloat3[5] = 0.0F;
//			arrayOfFloat3[6] = 40.0F;
//			arrayOfFloat3[7] = -60.0F;
//			arrayOfFloat3[8] = 0.0F;
//			arrayOfFloat3[9] = 40.0F;
//			arrayOfFloat3[10] = 60.0F;
//			arrayOfFloat3[11] = 0.0F;
//			this.AniVertices = arrayOfFloat3;
//			float[] arrayOfFloat4 = new float[12];
//			arrayOfFloat4[0] = -160.0F;
//			arrayOfFloat4[1] = 31.0F;
//			arrayOfFloat4[2] = 0.0F;
//			arrayOfFloat4[3] = -160.0F;
//			arrayOfFloat4[4] = -41.0F;
//			arrayOfFloat4[5] = 0.0F;
//			arrayOfFloat4[6] = 160.0F;
//			arrayOfFloat4[7] = -41.0F;
//			arrayOfFloat4[8] = 0.0F;
//			arrayOfFloat4[9] = 160.0F;
//			arrayOfFloat4[10] = 31.0F;
//			arrayOfFloat4[11] = 0.0F;
//			this.foamVertices = arrayOfFloat4;
//			float[] arrayOfFloat5 = new float[8];
//			arrayOfFloat5[0] = 0.0F;
//			arrayOfFloat5[1] = 0.0F;
//			arrayOfFloat5[2] = 0.0F;
//			arrayOfFloat5[3] = 1.0F;
//			arrayOfFloat5[4] = 1.0F;
//			arrayOfFloat5[5] = 1.0F;
//			arrayOfFloat5[6] = 1.0F;
//			arrayOfFloat5[7] = 0.0F;
//			this.standardTextureCoordinates = arrayOfFloat5;
//			float[] arrayOfFloat6 = new float[12];
//			arrayOfFloat6[0] = -5.0F;
//			arrayOfFloat6[1] = 5.0F;
//			arrayOfFloat6[2] = 0.0F;
//			arrayOfFloat6[3] = -5.0F;
//			arrayOfFloat6[4] = -5.0F;
//			arrayOfFloat6[5] = 0.0F;
//			arrayOfFloat6[6] = 5.0F;
//			arrayOfFloat6[7] = -5.0F;
//			arrayOfFloat6[8] = 0.0F;
//			arrayOfFloat6[9] = 5.0F;
//			arrayOfFloat6[10] = 5.0F;
//			arrayOfFloat6[11] = 0.0F;
//			this.bubbleVertices = arrayOfFloat6;
//			this.textures = new int[50];
//			this.Foamlist_StartFrame = new int[8];
//			this.Foamlist_EndFrame = new int[8];
//			this.Foamlist_LoopEnabled = new boolean[8];
//			this.Foamlist_NextFoam = new int[8];
//			this.vertex_foam = new float[3240];
//			this.vertex_slime = new float[3240];
//			this.texcoord_slime = new float[2160];
//			this.Ani_count = 0;
//			this.DoneLoaded = false;
//			this.loadPlace = 0;
//			this.playFill = true;
//			this.playBurp = true;
//		}
//
//		void Beersetup(GL10 paramGL10) {
//			this.Foamlist_StartFrame[0] = 0;
//			this.Foamlist_EndFrame[0] = 45;
//			this.Foamlist_LoopEnabled[0] = false;
//			this.Foamlist_NextFoam[0] = 7;
//			this.Foamlist_StartFrame[1] = 10;
//			this.Foamlist_EndFrame[1] = 45;
//			this.Foamlist_LoopEnabled[1] = false;
//			this.Foamlist_NextFoam[1] = 4;
//			this.Foamlist_StartFrame[2] = 10;
//			this.Foamlist_EndFrame[2] = 45;
//			this.Foamlist_LoopEnabled[2] = false;
//			this.Foamlist_NextFoam[2] = 5;
//			this.Foamlist_StartFrame[3] = 0;
//			this.Foamlist_EndFrame[3] = 13;
//			this.Foamlist_LoopEnabled[3] = false;
//			this.Foamlist_NextFoam[3] = 6;
//			this.Foamlist_StartFrame[4] = 46;
//			this.Foamlist_EndFrame[4] = 64;
//			this.Foamlist_LoopEnabled[4] = false;
//			this.Foamlist_NextFoam[4] = 1;
//			this.Foamlist_StartFrame[5] = 46;
//			this.Foamlist_EndFrame[5] = 64;
//			this.Foamlist_LoopEnabled[5] = true;
//			this.Foamlist_NextFoam[5] = 2;
//			this.Foamlist_StartFrame[6] = 14;
//			this.Foamlist_EndFrame[6] = 33;
//			this.Foamlist_LoopEnabled[6] = true;
//			this.Foamlist_NextFoam[6] = 3;
//			this.Foamlist_StartFrame[7] = 46;
//			this.Foamlist_EndFrame[7] = 64;
//			this.Foamlist_LoopEnabled[7] = true;
//			this.Foamlist_NextFoam[7] = 0;
//			this.FoamCount = this.Foamlist_StartFrame[0];
//			this.RedShader = 1.0F;
//			this.GreenShade = 1.0F;
//			this.BlueShade = 1.0F;
//			initRipple();
//			ResetFluid();
//			this.RedShader = 1.0F;
//			this.fadeFactor = 1.0F;
//			this.GreenShade = 1.0F;
//			this.BlueShade = 1.0F;
//			// this.FadeSlimeFlag = false;
//			this.currentFluidHeight = 0;
//			this.mBubbleVertexBuffer = iBree.this
//					.getFloatBufferFromFloatArray(this.bubbleVertices);
//			this.mFoamVertexBuffer = iBree.this
//					.getFloatBufferFromFloatArray(this.foamVertices);
//			this.glassPoured = false;
//			this.tpoint = 0;
//			this.y = 0;
//			if (this.y > 44) {
//				this.mVertexSlimeBuffer = iBree.this
//						.getFloatBufferFromFloatArray(this.vertex_slime);
//				this.mTexCoordSlimeBuffer = iBree.this
//						.getFloatBufferFromFloatArray(this.texcoord_slime);
//				this.mVertexFoamBuffer = iBree.this
//						.getFloatBufferFromFloatArray(this.vertex_foam);
//				this.mTexCoordFoamBuffer = iBree.this
//						.getFloatBufferFromFloatArray(this.texcoord_foam);
//				this.secondLoaded = true;
//				return;
//			}
//			for (this.x = 0;; this.x = (1 + this.x)) {
//				if (this.x >= 44) {
//					this.y = (22 + this.y);
//					break;
//				}
//				this.vertex_foam[(3 * this.tpoint)] = this.Xpoints[this.x];
//				this.vertex_foam[(1 + 3 * this.tpoint)] = 0.0F;
//				this.vertex_slime[(3 * this.tpoint)] = this.Xpoints[this.x];
//				this.vertex_slime[(1 + 3 * this.tpoint)] = 0.0F;
//				this.vertex_foam[(2 + 3 * this.tpoint)] = 0.0F;
//				this.vertex_slime[(2 + 3 * this.tpoint)] = 0.0F;
//				this.ffx = this.XCoords[this.x];
//				this.ffy = this.YCoords[this.y];
//				this.texcoord_foam[(2 * this.tpoint)] = this.ffx;
//				this.texcoord_foam[(1 + 2 * this.tpoint)] = (1.0F - this.ffy);
//				this.texcoord_slime[(2 * this.tpoint)] = (this.ffx + this.newX
//						/ iBree.this.displayHeight);
//				this.texcoord_slime[(1 + 2 * this.tpoint)] = 0.0F;
//				this.tpoint = (1 + this.tpoint);
//				this.vertex_foam[(3 * this.tpoint)] = this.Xpoints[this.x];
//				this.vertex_slime[(3 * this.tpoint)] = this.Xpoints[this.x];
//				this.vertex_foam[(2 + 3 * this.tpoint)] = 0.0F;
//				this.vertex_slime[(2 + 3 * this.tpoint)] = 0.0F;
//				this.ffx = this.XCoords[this.x];
//				this.ffy = this.YCoords[(1 + this.y)];
//				this.texcoord_foam[(2 * this.tpoint)] = this.ffx;
//				this.texcoord_foam[(1 + 2 * this.tpoint)] = (1.0F - this.ffy);
//				this.texcoord_slime[(2 * this.tpoint)] = (this.ffx + this.newX
//						/ iBree.this.displayHeight);
//				this.texcoord_slime[(1 + 2 * this.tpoint)] = 0.0F;
//				this.tpoint = (1 + this.tpoint);
//			}
//		}
//
//		void FadeSlimeDrips() {
//			if (this.endcount > 60)
//				return;
//			float f = (60 - this.endcount) / 60.0F;
//			int j = 0;
//			label25: if (j < 45)
//				;
//			for (int k = 0;; k++) {
//				if (k >= 45) {
//					j++;
//					// break label25;
//					break;
//				}
//				float[] arrayOfFloat = this.Slimepoints[j];
//				arrayOfFloat[k] = (f * arrayOfFloat[k]);
//			}
//		}
//
//		void LoadPieces(GL10 paramGL10) {
//			if (this.loadPlace < 6)
//				switch (this.loadPlace) {
//				default:
//				case 2:
//				case 4:
//				case 3:
//				case 5:
//				case 1:
//				case 0:
//				}
//			this.loadPlace = (1 + this.loadPlace);
//			if (this.loadPlace > 73) {
//				this.DoneLoaded = true;
//				return;
//			}
//			loadTexturenew(2130837578, this.mTextureBuffer.get(0));//layer
//			loadTexturenew(2130837601, this.mTextureBuffer.get(1));//slime
//			loadTexturenew(2130837504, this.mTextureBuffer.get(3));
//			loadTexturenew(2130837505, this.mTextureBuffer.get(4));
//			loadTextureBig(2130837579, this.mTextureBuffer.get(5));
//			loadTexturenew(2130837506, this.mTextureBuffer.get(7));
//			if (this.loadPlace < 72) {
//				loadTextureFoam(2130837508 + (this.loadPlace - 6),
//						this.mFoamTextureBuffer.get(this.loadPlace - 6));
//			}
//			Beersetup(this.gl);
//		}
//
//		public void ResetFluid() {
//			this.playFill = true;
//			this.playBurp = true;
//			this.shaketime = 0;
//			this.shakecount = 0;
//			this.currentFluidHeight = 1;
//			this.currentAngle = 0.0F;
//			this.glassPoured = false;
//			this.CurrentFoam = 0;
//			this.BackFoamFlag = false;
//			this.ShakeXitionFlag = false;
//			this.currentAngle = 0.0F;
//			this.FoamCount = 0;
//			this.MaxFoamCount = 0;
//			this.FoamSkipCount = 1;
//			this.SlimeReady = false;
//			// this.FadeSlimeFlag = false;
//			this.ShakeFlag = false;
//		}
//
//		void draw(GL10 paramGL10) {
//			boolean bool11;
//			int i34;
//			int i35;
//	     	if (this.mButtonView) {
//	     		paramGL10.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//	     		// 清除屏幕缓存
//	     		paramGL10.glClear(GL10.GL_COLOR_BUFFER_BIT);
//	     		boolean bool10 = this.DoneLoaded;
////	     		if (this.BeerDisplayMode != 0)
////	          break label2372;
//	     		bool11 = true;
//	     		if ((bool10 & bool11)) {
//	     			this.BeerDisplayMode = 2;
//	     			this.ScreenHitFlag = false;
//	     			iBree.this.beerReady = true;
//	     		}
//	     		if (this.ScreenHitFlag)
//	     		{
//	     			this.ScreenHitFlag = false;
////	     			if (this.BeerDisplayMode != 2)
////	     				break label2378;
//	     			i34 = 1;
//	     			if (!this.mButtonExit)
//	     				break label2384;
//	     			i35 = 0;
////	          label125: if ((i34 & i35) != 0)
////	          {
////	            this.mDone = false;
////	            this.mButtonView = true;
////	            this.mButtonDone = false;
////	            this.mButtonExit = true;
////	          }
//	     		}
//	     	}
////	      label290: int i32;
////	      label304: int i33;
////	      label314: int j;
////	      label534: label823: label1222: int i26;
////	      label1301: label1725: int i27;
////	      label1737: int i30;
////	      label1814: int i31;
////	      label1824: label1850: boolean bool9;
////	      label2018: int i24;
////	      label2047: int i25;
//	     	switch (this.BeerDisplayMode)
//	     	{
//	     	default:
//	     		//当前角度
//	     		this.currentAngle = (3.5F + (float)(57.295780000000001D * StrictMath.atan2(this.AccelY, this.AccelX) - 90.0D));
//	     		if (this.glassPoured) {
////	     			if (!this.FadeSlimeFlag)
////	     				break;
//	     			FadeSlimeDrips();
//	     			this.FoamFade -= 5.0F;
//	     			if (this.FoamFade < 0.0F)
//	        	    {
//	     				this.FoamFade = 0.0F;
//	     				iBree.this.smashed = false;
//	        	    }
////	     			if (this.FoamFade < 60.0F)
////	     				break label2967;
////	     			i32 = 1;
////	     			if (!this.FadeSlimeFlag)
////	     				break label2973;
////	          i33 = 0;
////	     			if ((i32 & i33) != 0)
////	     				this.FoamFade = 60.0F;
//	     			this.fadeFactor = (this.FoamFade / 60.0F);
//	     		}
//	     		else
//	     		{
//	     			this.Rangle = (3.141593F * this.currentAngle / 180.0F);
//	     			if (this.Rangle >= 3.141592741012573D)
//	     				this.Rangle = (3.141592741012573D - this.Rangle);
//	     			if (this.Rangle <= -3.141592741012573D)
//	     				this.Rangle = (3.141592741012573D + this.Rangle);
//	     			this.sAd = Math.sin(this.Rangle);
//	     			this.sA = (float)this.sAd;
//	     			this.xFl = (this.currentFluidHeight / 250.0F * this.maxFill - 0.35F);
//	     			this.newX = 0.0F;
//	     			this.newY = (480.0F * this.xFl - 240.0F - Math.abs(20.0F * this.sA));
//	     			//清除屏幕缓存
//	     			paramGL10.glClear(GL10.GL_COLOR_BUFFER_BIT);
//	     			//绘制顶点
//	     			paramGL10.glVertexPointer(3, GL10.GL_FLOAT, 0, this.mBubbleVertexBuffer);
//	     			paramGL10.glTexCoordPointer(2, GL10.GL_FLOAT, 0, this.mStandTextCoordsBuffer);
//	     			paramGL10.glBindTexture(GL10.GL_TEXTURE_2D, this.textures[4]);
////	     			j = 0;
////	     			if (j < this.BubGeneratorCount)
////	     				break label2979;
//	     			this.BubbleListXpos[this.initBubCount] = this.randomizer.nextInt(320);
//	     			this.BubbleListYpos[this.initBubCount] = this.randomizer.nextInt(480);
//	     			this.BubbleListEnabled[this.initBubCount] = true;
//	     			this.BubbleListAge[this.initBubCount] = 1;
//	     			this.BubbleListXaccl[this.initBubCount] = 0.0F;
//	     			this.BubbleListYaccl[this.initBubCount] = 0.0F;
//	     			this.BubbleListDoneZoom[this.initBubCount] = false;
//	     			this.initBubCount = (1 + this.initBubCount);
//	     			this.BubGeneratorCount = (1 + this.BubGeneratorCount);
//	     			if (this.initBubCount >= this.MaxBubbles)
//	     				this.initBubCount = 0;
//	     			if (this.BubGeneratorCount > this.MaxBubbles)
//	     				this.BubGeneratorCount = this.MaxBubbles;
//	     			paramGL10.glPushMatrix();
//	     			paramGL10.glVertexPointer(3, GL10.GL_FLOAT, 0, this.mElementVertexBuffer);
//	     			paramGL10.glTexCoordPointer(2, GL10.GL_FLOAT, 0, this.mStandTextCoordsBuffer);
//	     			paramGL10.glBindTexture(GL10.GL_TEXTURE_2D, this.textures[0]);
//	     			paramGL10.glTranslatef(0.0F, 0.0F, -1.1F);
//	     			paramGL10.glEnable(GL10.GL_BLEND);
//	     			paramGL10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
//	     			paramGL10.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
//	     			paramGL10.glPopMatrix();
//	     			this.dt = 0.96F;
//	     			this.zeropos = this.h[0];
//	     			this.WaveOffSetVal = 0.0F;
//	     			this.plusone = this.h[1];
//	          		this.i = 1;
//	          		if (this.i < 45)
//	          			break label3357;
//	          		this.h[0] = this.h[1];
//	          		this.WaveOffSetVal += this.h[1];
//	          		this.WaveOffSetVal /= 45.0F;
//	          		this.h[44] = this.h[43];
//	          		this.posOff = (int)(0.2075F * Math.abs(90.0F - Math.abs(180.0F - this.currentAngle)));
//	          		if (this.posOff < 1)
//	          			this.posOff = 1;
//	          		if (this.posOff > 43)
//	          			this.posOff = 43;
//	          		this.xx = (Math.abs(0.1F * this.AccelerationX) + Math.abs(0.1F * this.AccelerationY));
//	          		float[] arrayOfFloat6 = this.v;
//	          		int i5 = this.posOff - 1;
//	          		arrayOfFloat6[i5] = (float)(arrayOfFloat6[i5] + 0.5D * this.xx);
//	          		float[] arrayOfFloat7 = this.v;
//	          		int i6 = this.posOff;
//	          		arrayOfFloat7[i6] = (float)(arrayOfFloat7[i6] + 1.0D * this.xx);
//	          		float[] arrayOfFloat8 = this.v;
//	          		int i7 = 1 + this.posOff;
//	          		arrayOfFloat8[i7] = (float)(arrayOfFloat8[i7] + 0.5D * this.xx);
//	          		float[] arrayOfFloat9 = this.v;
//	          		int i8 = 45 - this.posOff;
//	          		arrayOfFloat9[i8] = (float)(arrayOfFloat9[i8] - 0.5D * this.xx);
//	          		float[] arrayOfFloat10 = this.v;
//	          		int i9 = 44 - this.posOff;
//	          		arrayOfFloat10[i9] = (float)(arrayOfFloat10[i9] - 1.0D * this.xx);
//	          		float[] arrayOfFloat11 = this.v;
//	          		int i10 = 43 - this.posOff;
//	          		arrayOfFloat11[i10] = (float)(arrayOfFloat11[i10] - 0.5D * this.xx);
//	          		this.Hoffset = 0.0F;
//	          		this.Hmult = 1.0F;
//	          		if (this.HeadZoomCount >= this.MaxHeadZoomCount)
//	          			break label3513;
//	          		this.Hmult = (1.0F + 0.25F * (this.HeadZoomCount / this.MaxHeadZoomCount));
//	          		this.Hoffset = (2.5F * (this.Hmult - 1.0F));
//	          		this.tpoint = 0;
//	          		this.y = 0;
//	          		if (this.y <= 44)
//	          			break label3566;
//	          		paramGL10.glVertexPointer(3, GL10.GL_FLOAT, 0, this.mVertexSlimeBuffer);
//	          		paramGL10.glEnable(GL10.GL_TEXTURE_2D);
//	          		if (!this.glassPoured)
//	          			break label3899;
//	          		paramGL10.glBindTexture(GL10.GL_TEXTURE_2D, this.textures[1]);
//	          		paramGL10.glDisable(GL10.GL_BLEND);
//	          		paramGL10.glTexCoordPointer(2, GL10.GL_FLOAT, 0, this.mTexCoordSlimeBuffer);
//	          		paramGL10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
//	          		paramGL10.glMatrixMode(GL10.GL_PROJECTION);
//	          		paramGL10.glPushMatrix();
//	          		paramGL10.glMatrixMode(GL10.GL_PROJECTION);
//	          		paramGL10.glRotatef(this.currentAngle, 0.0F, 0.0F, 1.0F);
//	          		paramGL10.glScalef(3.0F, 3.0F, 1.0F);
//	          		paramGL10.glMatrixMode(GL10.GL_TEXTURE);
//	          		paramGL10.glPushMatrix();
//	          		paramGL10.glTranslatef(0.5F, -0.55F, 0.0F);
//	          		paramGL10.glScalef(1.9F, 1.9F, 1.0F);
//	          		paramGL10.glTranslatef(0.5F, -0.5F, 0.0F);
//	          		paramGL10.glRotatef(this.currentAngle, 0.0F, 0.0F, 1.0F);
//	          		paramGL10.glTranslatef(-0.5F, 0.0F, 0.0F);
//	          		paramGL10.glTranslatef(0.0F, (480.0F * this.xFl - 240.0F) / 960.0F, 0.0F);
//	          		this.ctemp[0] = (this.RedShader * this.fadeFactor);
//	          		this.ctemp[1] = (this.GreenShade * this.fadeFactor);
//	          		this.ctemp[2] = (this.BlueShade * this.fadeFactor);
//	          		paramGL10.glColor4f(this.ctemp[0], this.ctemp[1], this.ctemp[2], 1.0F);
//	          		paramGL10.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 264);
//	          		paramGL10.glMatrixMode(GL10.GL_TEXTURE);
//	          		paramGL10.glPopMatrix();
//	          		paramGL10.glMatrixMode(GL10.GL_PROJECTION);
//	          		paramGL10.glDisableClientState(32886);
//	          		paramGL10.glPopMatrix();
//	          		if (this.currentFluidHeight > 0)
//	          		{
//	          			paramGL10.glPushMatrix();
//	          			paramGL10.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//	          			paramGL10.glVertexPointer(3, GL10.GL_FLOAT, 0, this.mVertexFoamBuffer);
//	          			paramGL10.glEnableClientState(32884);
//	          			paramGL10.glTexCoordPointer(2, GL10.GL_FLOAT, 0, this.mTexCoordFoamBuffer);
//	          			paramGL10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
//	          			paramGL10.glBindTexture(GL10.GL_TEXTURE_2D, this.mFoamTextureBuffer.get(this.FoamCount));
//	          			paramGL10.glEnable(GL10.GL_TEXTURE_2D);
//	          			this.FoamSkip = (1 + this.FoamSkip);
//	          			if (this.CurrentFoam >= 0)
//	          				break label3922;
////	  	          	i26 = 1;
//	          			if (this.CurrentFoam <= 7)
//	          				break label3928;
////	           	 i27 = 1;
////	           	 if ((i26 | i27) != 0)
////	           	 	  this.CurrentFoam = 0;
//	          			if (this.FoamSkip > this.FoamSkipCount)
//	          			{
//	          				if (!this.BackFoamFlag)
//	          					break label3946;
//	          				if (this.ShakeXitionFlag)
//	          					this.FoamCount -= 1;
//	          				this.FoamCount -= 1;
//	          				if (this.FoamCount > this.Foamlist_StartFrame[this.CurrentFoam])
//	        	   			  break label3934;
////	             	 i30 = 1;
//	          				if (this.FoamCount >= 0)
//	          					break label3940;
////	             	 i31 = 1;
////	            	 if ((i30 | i31) != 0)
////	            	{
////	            		this.BackFoamFlag = false;
////	            		this.FoamCount = this.Foamlist_StartFrame[this.CurrentFoam];
////	            	}
//	          				this.FoamSkip = 0;
//	          			}
//	          			paramGL10.glEnable(GL10.GL_BLEND);
//	          			paramGL10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
//	          			paramGL10.glScalef(3.0F * this.Hmult, 3.0F * this.Hmult, 1.0F);
//	          			paramGL10.glRotatef(this.currentAngle, 0.0F, 0.0F, 1.0F);
//	          			paramGL10.glTranslatef(0.0F, -this.Hoffset - 0.25F, 0.0F);
//	          			paramGL10.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 264);
//	          			paramGL10.glPopMatrix();
//	          		}
//	          		if (this.glassPoured)
//	          			break label4216;
//	          		if (this.playFill)
//	          		{
//	          			if (!this.playFill)
//	          				break label4158;
////	        	  bool9 = false;
////	        	  this.playFill = iBree;
//	          		}
//	          		this.currentFluidHeight = (3 + this.currentFluidHeight);
//	          		if (this.currentFluidHeight <= 180)
//	          			break label4164;
////	          i24 = 1;
//	          		if (!this.ShakeXitionFlag)
//	          			break label4170;
////	          i25 = 0;
////	          label2057: if ((i24 & i25) != 0)
////	          {
////	            this.ShakeXitionFlag = true;
////	            this.FoamSkipCount = 0;
////	            this.BackFoamFlag = true;
////	          }
//	          		if (this.currentFluidHeight > 220)
//	          			this.HeadZoomCount = (1 + this.HeadZoomCount);
//	          		if (this.currentFluidHeight <= 250)
//	          			break label4176;
//	          		this.firstflag = true;
//	          		this.glassPoured = true;
//	          		label2120: if ((!iBree.this.smashed) || (!iBree.this.smashEnabled))
//	          			break label5203;
//	          		paramGL10.glPushMatrix();
//	          		paramGL10.glVertexPointer(3, GL10.GL_FLOAT, 0, this.mElementVertexBuffer);
//	          		paramGL10.glEnableClientState(32884);
//	          		paramGL10.glTexCoordPointer(2, GL10.GL_FLOAT, 0, this.mStandTextCoordsBuffer);
//	          		paramGL10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
//	          		paramGL10.glBindTexture(GL10.GL_TEXTURE_2D, this.textures[7]);
//	          		paramGL10.glEnable(GL10.GL_TEXTURE_2D);
////	          if (this.FadeSlimeFlag)
////	          {
////	        	  this.ctemp[0] = (this.RedShader * this.fadeFactor);
////	        	  this.ctemp[1] = (this.GreenShade * this.fadeFactor);
////	        	  this.ctemp[2] = (this.BlueShade * this.fadeFactor);
////	           	  paramGL10.glColor4f(this.ctemp[0], this.ctemp[1], this.ctemp[2], this.ctemp[2]);
////	          }
//	          
//	          		paramGL10.glTranslatef(0.0F, 0.0F, -1.1F);
//	          		paramGL10.glEnable(GL10.GL_BLEND);
//	          		paramGL10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
//	          		paramGL10.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
//	          		paramGL10.glPopMatrix();
//	     		}
//	     	case 0:
//	     	case 1:
//	     	case 2:
//	      
//	     	}
//	     
//	     	while (true)
//	     	{
//	     		return;
//	     		label2372: bool11 = false;
//	     		break;
//	     		label2378: i34 = 0;
//	     		break label115;
//	     		label2384: i35 = 1;
//	     		break label125;
//	     		paramGL10.glPushMatrix();
//	     		paramGL10.glVertexPointer(3, GL10.GL_FLOAT, 0, this.mElementVertexBuffer);
//	     		paramGL10.glTexCoordPointer(2, GL10.GL_FLOAT, 0, this.mStandTextCoordsBuffer);
//	     		paramGL10.glBindTexture(GL10.GL_TEXTURE_2D, this.textures[6]);
//	     		paramGL10.glTranslatef(0.0F, 0.0F, -1.1F);
//	     		paramGL10.glEnable(GL10.GL_BLEND);
//	     		paramGL10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
//	     		paramGL10.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
//	     		paramGL10.glPopMatrix();
//	     		paramGL10.glPushMatrix();
//	     		paramGL10.glVertexPointer(3, GL10.GL_FLOAT, 0, this.mAniVerticesBuffer);
//	     		paramGL10.glTexCoordPointer(2, GL10.GL_FLOAT, 0, this.mStandTextCoordsBuffer);
//	     		paramGL10.glBindTexture(GL10.GL_TEXTURE_2D, this.mAniBuffer.get(this.Ani_count));
//	     		paramGL10.glTranslatef(80.0F, -106.0F, -1.1F);
//	     		//使用OpenGL的混合功能
//	     		paramGL10.glEnable(GL10.GL_BLEND);
//	     		paramGL10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
//	     		paramGL10.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
//	     		paramGL10.glPopMatrix();
//	     		this.Ani_count = (1 + this.Ani_count);
//	     		if (this.Ani_count <= 17)
//	     			continue;
//	     		this.Ani_count = 0;
//	     		continue;
//	     		paramGL10.glPushMatrix();
//	     		paramGL10.glVertexPointer(3, GL10.GL_FLOAT, 0, this.mButtonVertices);
//	     		paramGL10.glTexCoordPointer(2, GL10.GL_FLOAT, 0, this.mStandTextCoordsBuffer);
//	     		paramGL10.glBindTexture(GL10.GL_TEXTURE_2D, this.textures[5]);
//	     		paramGL10.glEnable(GL10.GL_BLEND);
//	     		paramGL10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
//	     		paramGL10.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
//	     		paramGL10.glPopMatrix();
//	     		continue;
//	     		if (this.mButtonExit)
//	     		{
//	     			this.iButtonCount -= 1;
//	     			if (this.iButtonCount < -60)
//	     			{
//	     				this.iButtonCount = 0;
//	     				this.mButtonDone = true;
//	     				this.mButtonView = false;
//	     			}
//	     		}
//	     		while (true)
//	     		{
//	     			this.ButxCount = this.iButtonCount;
//	     			if (this.ButxCount < 0)
//	     				this.ButxCount = 0;
//	     			if (this.ButxCount > 30)
//	     				this.ButxCount = 30;
//	     			paramGL10.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
//	     			paramGL10.glClear(GL10.GL_COLOR_BUFFER_BIT);
//	     			paramGL10.glPushMatrix();
//	     			paramGL10.glVertexPointer(3, GL10.GL_FLOAT, 0, this.mButtonVertices);
//	     			paramGL10.glTexCoordPointer(2, GL10.GL_FLOAT, 0, this.mStandTextCoordsBuffer);
//	     			paramGL10.glBindTexture(GL10.GL_TEXTURE_2D, this.textures[5]);
//	     			paramGL10.glRotatef(90 - 3 * this.ButxCount, 0.0F, 1.0F, 0.0F);
//	     			paramGL10.glEnable(GL10.GL_BLEND);
//	     			paramGL10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
//	     			paramGL10.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
//	     			paramGL10.glPopMatrix();
//	     			break;
//	     			this.iButtonCount = (1 + this.iButtonCount);
//	     			if (this.iButtonCount <= 29)
//	     				continue;
//	     			this.mButtonDone = true;
//	     			this.iButtonCount = 30;
//	     		}
//	     		this.FoamFade = (1.0F + this.FoamFade);
////	     		break label290;
////	     		label2967: i32 = 0;
////	     		break label304;
////	     		label2973: i33 = 1;
////	     		break label314;
////	     		label2979: if (this.BubbleListEnabled[j] != 0)
////	     		{
////	     			paramGL10.glPushMatrix();
////	     			if (this.BubbleListDoneZoom[j] == 0)
////	     			{
////	     				paramGL10.glTranslatef(this.BubbleListXpos[j] - 160.0F, this.BubbleListYpos[j] - 240.0F, 0.1F);
////	     				float f = this.BubbleListAge[j] / this.MaxAgeZoom;
////	     				paramGL10.glScalef(f, f, 1.0F);
////	     				int[] arrayOfInt = this.BubbleListAge;
////	     				arrayOfInt[j] = (1 + arrayOfInt[j]);
////	     				if (this.BubbleListAge[j] > this.MaxAgeStill)
////	     					this.BubbleListDoneZoom[j] = true;
////	     				paramGL10.glEnable(GL10.GL_BLEND);
////	     				paramGL10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
////	     				paramGL10.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
////	     				paramGL10.glPopMatrix();
////	     			}
////	     		}
////	     		else
////	     		{
////	      		    j++;
////	      		    break label534;
////	     		}
////	     		float[] arrayOfFloat1 = this.BubbleListXaccl;
////	     		arrayOfFloat1[j] = (0.1F + arrayOfFloat1[j]);
////	     		float[] arrayOfFloat2 = this.BubbleListYaccl;
////	     		arrayOfFloat2[j] -= 0.75F;
////	     		float[] arrayOfFloat3 = this.BubbleListXpos;
////	     		arrayOfFloat3[j] -= this.BubbleListXaccl[j];
////	     		float[] arrayOfFloat4 = this.BubbleListYpos;
////	     		arrayOfFloat4[j] -= this.BubbleListYaccl[j];
////	     		int k;
////	     		label3226: int m;
////	     		label3242: int i1;
////	    	    label3265: int i2;
////	     		if (this.BubbleListXpos[j] < -50.0F)
////	     		{
////	     			k = 1;
////	     			if (this.BubbleListXpos[j] <= 400.0F)
////	     				break label3339;
////	     			m = 1;
////	     			int n = k | m;
////	     			if (this.BubbleListYpos[j] >= -50.0F)
////	     				break label3345;
////	     			i1 = 1;
////	     			i2 = n | i1;
////	     			if (this.BubbleListYpos[j] <= 550.0F)
////	     				break label3351;
////	     		}
////	      		
////	     		label3339: label3345: label3351: for (int i3 = 1; ; i3 = 0)
////	     		{
////	     			if ((i2 | i3) != 0)
////	     				this.BubbleListEnabled[j] = false;
////	     			paramGL10.glTranslatef(this.BubbleListXpos[j] - 160.0F, this.BubbleListYpos[j] - 240.0F, 0.1F);
////	     			break;
////	     			k = 0;
////	     			break label3226;
////	     			m = 0;
////	     			break label3242;
////	     			i1 = 0;
////	     			break label3265;
////	     		}
//	     		label3357: this.minusone = this.zeropos;
//	     		this.zeropos = this.plusone;
//	     		this.plusone = this.h[(1 + this.i)];
//	     		this.v[this.i] = (this.v[this.i] + (this.minusone + this.plusone) / 2.85F - this.zeropos);
//	     		float[] arrayOfFloat5 = this.v;
//	     		int i4 = this.i;
//	     		arrayOfFloat5[i4] = (0.73F * arrayOfFloat5[i4]);
//	     		this.zeropos += this.v[this.i] * this.dt;
//	     		this.h[this.i] = this.zeropos;
//	     		this.WaveOffSetVal += this.zeropos;
//	     		this.i = (1 + this.i);
//	       		 
//	     		break label823;
//	       		 
//	     		label3513: if (this.HeadZoomCount < 2 * this.MaxHeadZoomCount)
//	       		
//	     		{
//	       			 this.Hmult = (1.0F + 0.25F * ((2.0F * this.MaxHeadZoomCount - this.HeadZoomCount) / this.MaxHeadZoomCount));
//	       			 break label1222;
//	     		}
//	     		this.Hmult = 1.0F;
//	     		break label1222;
//	     		label3566: for (this.x = 0; ; this.x = (1 + this.x))
//	     		{
//	     			if (this.x >= 44)
//	     			{
//	     				this.y = (22 + this.y);
//	     				break;
//	     			}
//	     			this.yoff = (2.0F * this.h[this.x] + this.newY / 3.0F);
//	     			this.mVertexFoamBuffer.put(1 + 3 * this.tpoint, 0.15F * this.Ypoints[this.y] + this.yoff - 12.0F);
//	     			this.mVertexSlimeBuffer.put(1 + 3 * this.tpoint, 0.75F * this.Ypoints[this.y] + this.yoff);
//	     			this.ffy = this.YCoords[this.y];
//	     			this.mTexCoordSlimeBuffer.put(1 + 2 * this.tpoint, this.ffy + 2.0F * this.h[this.x] / 360.0F);
//	     			this.tpoint = (1 + this.tpoint);
//	     			this.mVertexFoamBuffer.put(1 + 3 * this.tpoint, 0.15F * this.Ypoints[(1 + this.y)] + this.yoff - 12.0F);
//	     			this.mVertexSlimeBuffer.put(1 + 3 * this.tpoint, 0.75F * this.Ypoints[(1 + this.y)] + this.yoff);
//	     			this.ffy = this.YCoords[(1 + this.y)];
//	          		this.mTexCoordSlimeBuffer.put(1 + 2 * this.tpoint, this.ffy + 2.0F * this.h[this.x] / 360.0F);
//	         	    this.tpoint = (1 + this.tpoint);
//	     		}
//	     		label3899: paramGL10.glBindTexture(GL10.GL_TEXTURE_2D, this.textures[3]);
//	     		this.FoamFade = 0.0F;
//	     		break label1301;
//	     		label3922: i26 = 0;
//	     		break label1725;
//	     		label3928: i27 = 0;
//	     		break label1737;
//	     		label3934: i30 = 0;
//	     		break label1814;
//	     		label3940: i31 = 0;
//	     		break label1824;
//	       		 
//	     		label3946: if (this.ShakeXitionFlag)
//	       		{
//	     			if (this.Foamlist_EndFrame[this.CurrentFoam] - this.FoamCount < 2)
//	     				this.ShakeXitionFlag = false;
//	     			if (this.FoamCount < this.Foamlist_StartFrame[this.CurrentFoam])
//	     				this.FoamCount = (this.Foamlist_StartFrame[this.CurrentFoam] - 1);
//	       		}
//	     		this.FoamCount = (1 + this.FoamCount);
//	     		if (this.FoamCount < this.Foamlist_EndFrame[this.CurrentFoam])
//	     			break label1850;
//	     		if (this.Foamlist_LoopEnabled[this.CurrentFoam] != 0)
//	     		{
//	     			this.FoamCount -= 1;
//	     			this.BackFoamFlag = true;
//	     			break label1850;
//	     		}
//	     		this.CurrentFoam = this.Foamlist_NextFoam[this.CurrentFoam];
//	     		int i28;
//	     		if (this.CurrentFoam < 0)
//	     		{
//	     			i28 = 1;
//	     			label4086: if (this.CurrentFoam <= 7)
//	            break label4152;
//	        }
//	        label4152: for (int i29 = 1; ; i29 = 0)
//	        {
//	        	if ((i28 | i29) != 0)
//	        		this.CurrentFoam = 0;
//	        	this.FoamSkipCount = 1;
//	        	if (Math.abs(this.Foamlist_StartFrame[this.CurrentFoam] - this.FoamCount) <= 10)
//	        		break;
//	        	this.ShakeXitionFlag = true;
//	        	break;
//	        	i28 = 0;
//	        	break label4086;
//	        }
//	        label4158: bool9 = true;
//	        break label2018;
//	        label4164: i24 = 0;
//	        break label2047;
//	        label4170: i25 = 1;
//	        break label2057;
//	        label4176: boolean bool7 = this.PourFlip;
//	        if (this.currentFluidHeight > 100);
//	        for (boolean bool8 = true; ; bool8 = false)
//	        {
//	          if (!(bool7 & bool8))
//	            break label4214;
//	          this.PourFlip = false;
//	          break;
//	        }
//	        label4214: break label2120;
//	        label4216: this.HeadZoomCount = (1 + this.HeadZoomCount);
//	        int i18;
//	        label4265: int i19;
//	        label4277: int i20;
//	        label4295: int i21;
//	        label4305: int i22;
//	        label4349: int i23;
//	        label4361: label4411: int i11;
//	        label4436: boolean bool2;
//	        label4457: boolean bool6;
//	        label4525: int i16;
//	        label4607: int i17;
//	        if (this.ShakeFlag)
//	        {
//	          this.ShakeFlag = false;
//	          this.shaketime = 0;
//	          this.shakecount = (1 + this.shakecount);
//	          if (this.shakecount > 7)
//	          {
//	            i18 = 1;
//	            if (this.currentFluidHeight <= 120)
//	              break label4825;
//	            i19 = 1;
//	            if ((i18 & i19) != 0)
//	            {
//	              if (!this.ShakeXitionFlag)
//	                break label4831;
//	              i20 = 0;
//	              if (!this.BackFoamFlag)
//	                break label4837;
//	              i21 = 0;
//	              if ((i20 | i21) != 0)
//	                this.ShakeXitionFlag = true;
//	              if (this.CurrentFoam > 3)
//	                this.CurrentFoam = this.Foamlist_NextFoam[this.CurrentFoam];
//	              if (this.CurrentFoam >= 0)
//	                break label4843;
//	              i22 = 1;
//	              if (this.CurrentFoam <= 7)
//	                break label4849;
//	              i23 = 1;
//	              if ((i22 | i23) != 0)
//	                this.CurrentFoam = 0;
//	              this.FoamSkipCount = 0;
//	              this.BackFoamFlag = true;
//	              this.shaketime = 0;
//	            }
//	            if (this.shakecount > 45)
//	            {
//	            	iBree.this.smashed = true;
//	              this.shakecount = 0;
//	            }
//	            if (Math.abs(this.currentAngle) <= 90 - this.currentFluidHeight / 6)
//	              break label4882;
//	            i11 = 1;
////	            boolean bool1 = i11 & this.glassPoured;
//	            if (this.currentFluidHeight <= 10)
//	              break label4888;
//	            bool2 = true;
////	            if (!(bool1 & bool2))
////	              break label4912;
//	            if (!this.PourFlip)
//	            {
//	              this.extraBounce = false;
//	              this.PourFlip = true;
//	            }
//	            boolean bool5 = this.smallfoamflag;
//	            if (this.currentFluidHeight >= 199)
//	              break label4894;
//	            bool6 = true;
//	            if ((bool5 & bool6))
//	            {
//	              this.firstflag = false;
//	              this.smallfoamflag = false;
//	              this.foamflag = false;
//	              this.smallfoamcount = 0;
//	              this.secondflag = true;
//	              this.newsmallfoamflag = true;
//	            }
//	            this.currentFluidHeight = (int)(this.currentFluidHeight - 1.5D);
//	            if (this.currentFluidHeight < 140)
//	            	iBree.this.FadeFizzFlag = true;
//	            if (this.currentFluidHeight >= 60)
//	              break label4900;
//	            i16 = 1;
//	            if (!this.endflag)
//	              break label4906;
//	            i17 = 0;
//	            label4617: if ((i16 & i17) != 0)
//	            {
//	              this.endflag = true;
//	              this.endcount = 0;
//	              iBree.this.FadeFizzFlag = true;
//	            }
//	            if (this.currentFluidHeight < 0)
//	              this.currentFluidHeight = 0;
//	          }
//	        }
//	        label4655: label4825: label4831: label4837: label4843: label4849: 
//	        do
//	        {
//	          if (!this.endflag)
//	            break label5171;
//	          this.endcount = (1 + this.endcount);
////	          if (this.endcount == 25)
////	            this.FadeSlimeFlag = true;
//	          if (this.endcount != 130)
//	            break;
//	          this.playFill = true;
//	          this.endcount = 0;
//	          this.endflag = false;
//	          this.mDone = true;
//	          this.BeerDisplayMode = 2;
//	          this.mButtonView = true;
//	          this.mButtonDone = false;
//	          this.mButtonExit = false;
//	          this.ScreenHitFlag = false;
////	          iBree.this.RestartAudio();
//	          ResetFluid();
//	          break;
//	          i18 = 0;
//	          break label4265;
//	          i19 = 0;
//	          break label4277;
//	          i20 = 1;
//	          break label4295;
//	          i21 = 1;
//	          break label4305;
//	          i22 = 0;
//	          break label4349;
//	          i23 = 0;
//	          break label4361;
//	          this.shaketime = (1 + this.shaketime);
//	          if (this.shaketime <= 30)
//	            break label4411;
//	          this.shakecount = 0;
//	          break label4411;
//	          i11 = 0;
//	          break label4436;
//	          bool2 = false;
//	          break label4457;
//	          bool6 = false;
//	          break label4525;
//	          i16 = 0;
//	          break label4607;
//	          i17 = 1;
//	          break label4617;
//	          if (!this.PourFlip)
//	            continue;
//	          this.PourFlip = false;
//	        }
//	        while ((!iBree.this.smashed) || (!iBree.this.smashEnabled));
//	        label4882: label4888: label4894: label4900: label4906: label4912: this.currentFluidHeight = (int)(this.currentFluidHeight - 0.5D);
//	        this.blubcount = (1 + this.blubcount);
//	        int i12;
////	        label4979: int i13;
////	        label4991: boolean bool4;
////	        label5054: int i14;
//	        if (this.blubcount == 5)
//	        {
//	          i12 = 1;
//	          if (this.currentFluidHeight <= 60)
//	            break label5179;
//	          i13 = 1;
//	          if (this.blubcount > 40)
//	            this.blubcount = 0;
//	          boolean bool3 = this.smallfoamflag;
//	          if (this.currentFluidHeight >= 199)
//	            break label5185;
//	          bool4 = true;
//	          if ((bool3 & bool4))
//	          {
//	            this.firstflag = false;
//	            this.smallfoamflag = false;
//	            this.foamflag = false;
//	            this.smallfoamcount = 0;
//	            this.secondflag = true;
//	            this.newsmallfoamflag = true;
//	          }
//	          if (this.currentFluidHeight < 140)
//	        	  iBree.this.FadeFizzFlag = true;
//	          if (this.currentFluidHeight >= 60)
//	            break label5191;
//	          i14 = 1;
//	          label5122: if (!this.endflag)
//	            break label5197;
//	        }
//	        label5171: label5179: label5185: label5191: label5197: for (int i15 = 0; ; i15 = 1)
//	        {
//	          if ((i14 & i15) != 0)
//	          {
//	            this.endflag = true;
//	            this.endcount = 0;
//	            iBree.this.FadeFizzFlag = true;
//	          }
//	          if (this.currentFluidHeight >= 0)
//	            break label4655;
//	          this.currentFluidHeight = 0;
//	          break label4655;
//	          break;
//	          i12 = 0;
//	          break label4979;
//	          i13 = 0;
//	          break label4991;
//	          bool4 = false;
//	          break label5054;
//	          i14 = 0;
//	          break label5122;
//	        }
//	        label5203: this.smashsoundhit = false;
//	      }
//	    }
//
//		void initRipple() {
//			// int j = 0;
//			// if (j >= 45)
//			// ;
//			// for (int k = 0;; k++) {
//			// if (k >= 45) {
//			// return;
//			// this.Xpoints[j] = (j * 320 / 44.0F - 160.0F);
//			// this.sXpoints[j] = (float) (0.25D * (j * 320 / 44.0F - 160.0F));
//			// this.XCoords[j] = (j / 44.0F);
//			// j++;
//			// break;
//			// }
//			// this.Ypoints[k] = (k * 480 / 44.0F);
//			// this.sYpoints[k] = (float) (0.25D * (k * 480 / 44.0F));
//			// this.YCoords[k] = (k / 44.0F);
//			// }
//		}
//
//		void loadTextureAni(int paramInt1, int paramInt2) {
//			Bitmap localBitmap1 = BitmapFactory.decodeResource(
//					iBree.this.getResources(), paramInt1);
//			Bitmap localBitmap2 = Bitmap.createScaledBitmap(localBitmap1, 64,
//					64, false);
//			this.gl.glBindTexture(GL10.GL_TEXTURE_2D, paramInt2);
//			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, localBitmap2, 0);
//			localBitmap1.recycle();
//			localBitmap2.recycle();
//			this.gl.glTexParameterx(GL10.GL_TEXTURE_2D,
//					GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
//			this.gl.glTexParameterx(GL10.GL_TEXTURE_2D,
//					GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
//		}
//
//		void loadTextureBig(int paramInt1, int paramInt2) {
//			Bitmap localBitmap1 = BitmapFactory.decodeResource(
//					iBree.this.getResources(), paramInt1);
//			Bitmap localBitmap2 = Bitmap.createScaledBitmap(localBitmap1, 512,
//					512, false);
//			this.gl.glBindTexture(GL10.GL_TEXTURE_2D, paramInt2);
//			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, localBitmap2, 0);
//			localBitmap1.recycle();
//			localBitmap2.recycle();
//			this.gl.glTexParameterx(GL10.GL_TEXTURE_2D,
//					GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
//			this.gl.glTexParameterx(GL10.GL_TEXTURE_2D,
//					GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
//		}
//
//		void loadTextureFoam(int paramInt1, int paramInt2) {
//			Bitmap localBitmap1 = BitmapFactory.decodeResource(
//					iBree.this.getResources(), paramInt1);
//			Bitmap localBitmap2 = Bitmap.createScaledBitmap(localBitmap1, 256,
//					128, false);
//			this.gl.glBindTexture(GL10.GL_TEXTURE_2D, paramInt2);
//			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, localBitmap2, 0);
//			localBitmap1.recycle();
//			localBitmap2.recycle();
//			this.gl.glTexParameterx(GL10.GL_TEXTURE_2D,
//					GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
//			this.gl.glTexParameterx(GL10.GL_TEXTURE_2D,
//					GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
//		}
//
//		void loadTexturenew(int paramInt1, int paramInt2) {
//			Bitmap localBitmap1 = BitmapFactory.decodeResource(
//					iBree.this.getResources(), paramInt1);
//			Bitmap localBitmap2 = Bitmap.createScaledBitmap(localBitmap1, 256,
//					256, false);
//			this.gl.glBindTexture(GL10.GL_TEXTURE_2D, paramInt2);
//			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, localBitmap2, 0);
//			localBitmap1.recycle();
//			localBitmap2.recycle();
//			this.gl.glTexParameterx(GL10.GL_TEXTURE_2D,
//					GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
//			this.gl.glTexParameterx(GL10.GL_TEXTURE_2D,
//					GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
//		}
//
//		void oglsetup(GL10 paramGL10) {
//			this.CurrentFoam = 200;
//			this.BackFoamFlag = false;
//			this.ShakeXitionFlag = false;
//			this.fluidHeight = 0.0F;
//			this.currentAngle = 0.0F;
//			this.FoamCount = 0;
//			this.MaxFoamCount = 0;
//			this.FoamSkipCount = 1;
//			this.SlimeDropPos = 0;
//			this.gl = paramGL10;
//			this.mTextureBuffer = IntBuffer.allocate(13);
//			this.mFoamTextureBuffer = IntBuffer.allocate(70);
//			this.mAniBuffer = IntBuffer.allocate(20);
//			paramGL10.glGenTextures(12, this.mTextureBuffer);
//			paramGL10.glGenTextures(69, this.mFoamTextureBuffer);
//			paramGL10.glGenTextures(19, this.mAniBuffer);
//			this.textures[0] = this.mTextureBuffer.get(0);
//			this.textures[1] = this.mTextureBuffer.get(1);
//			this.textures[2] = this.mTextureBuffer.get(2);
//			this.textures[3] = this.mTextureBuffer.get(3);
//			this.textures[4] = this.mTextureBuffer.get(4);
//			this.textures[5] = this.mTextureBuffer.get(5);
//			this.textures[6] = this.mTextureBuffer.get(6);
//			this.textures[7] = this.mTextureBuffer.get(7);
//			loadTextureBig(2130837603, this.mTextureBuffer.get(6));
//			this.mElementVertexBuffer = iBree.this
//					.getFloatBufferFromFloatArray(this.elementVertices);
//			this.mStandTextCoordsBuffer = iBree.this
//					.getFloatBufferFromFloatArray(this.standardTextureCoordinates);
//			this.mAniVerticesBuffer = iBree.this
//					.getFloatBufferFromFloatArray(this.AniVertices);
//			this.mButtonVertices = iBree.this
//					.getFloatBufferFromFloatArray(this.ButtonVertices);
//			for (int j = 0;; j++) {
//				if (j >= 18)
//					return;
//				loadTextureAni(2130837583 + j, this.mAniBuffer.get(j));
//			}
//		}
//	}
//
//	ByteBuffer getByteBufferFromByteArray(byte[] paramArrayOfByte) {
//		ByteBuffer localByteBuffer = ByteBuffer
//				.allocateDirect(paramArrayOfByte.length);
//		localByteBuffer.put(paramArrayOfByte);
//		localByteBuffer.position(0);
//		return localByteBuffer;
//	}
//
//	FloatBuffer getFloatBufferFromFloatArray(float[] paramArrayOfFloat) {
//		ByteBuffer localByteBuffer = ByteBuffer
//				.allocateDirect(4 * paramArrayOfFloat.length);
//		localByteBuffer.order(ByteOrder.nativeOrder());
//		FloatBuffer localFloatBuffer = localByteBuffer.asFloatBuffer();
//		localFloatBuffer.put(paramArrayOfFloat);
//		localFloatBuffer.position(0);
//		return localFloatBuffer;
//	}
//
//	IntBuffer getIntBufferFromIntArray(int[] paramArrayOfInt) {
//		ByteBuffer localByteBuffer = ByteBuffer
//				.allocateDirect(4 * paramArrayOfInt.length);
//		localByteBuffer.order(ByteOrder.nativeOrder());
//		IntBuffer localIntBuffer = localByteBuffer.asIntBuffer();
//		localIntBuffer.put(paramArrayOfInt);
//		localIntBuffer.position(0);
//		return localIntBuffer;
//	}
//}