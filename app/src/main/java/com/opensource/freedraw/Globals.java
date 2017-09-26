package com.opensource.freedraw;

//import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

//  This class holds values for global variables used by other classes in the app

public class Globals
{
	public static String gAppName = "freedraw";
	public static String gConfigFile = gAppName + ".cfg";
	public static String gImageFile = "tempfile.png";
	
	public static Context gMainContext = null;
	public static Activity gMainActivity = null;
	public static MainEngine gMainEngine = null;
	
	public static Boolean gDebug = false;
	public static Boolean gAreRecording = false;
	public static Boolean gStopRecordingOnUpFlag = true;
	
	public static Bitmap gBitmap = null;      
	
	public static int gCurColor = 0x0000ff00;
	public static int gCurAlpha = 255;
	public static int gCursorColor = Color.BLACK;
	
	public static ArrayList<ActionHist> gActionList = new ArrayList();
	public static int gActionSequenceNum = 0;
	
	public static int gRed = 0;
	public static int gBlue = 0;
	public static int gGreen = 0;
	
	public static int gLastXpos = 0;
	public static int gLastYpos = 0;
	public static int gLastUpXpos = 0;
	public static int gLastUpYpos = 0;
	
	public static int gStrokeWidth = 1;
	public static int gBrushWidth = 20;
	public static Boolean gBrushSolid = true;
	public static Boolean gShowCursorPos = true;
	
	public static Shapes gBrushShape = Shapes.circle;
	public static ActionType gActionType = ActionType.circle;
	public static CursorInfo gCursorInfo = CursorInfo.black;
	public static int gAppCount = 0;
	
	public static CursorOffset gCursorOffset;
	
	public static int gOffsetPixels = 0;
	public static int gOffsetLeft = 0;
	public static int gOffsetRight = 0;
	public static int gOffsetUp = 0;
	public static int gOffsetDown = 0;
	public static int gScreenWidth = 0;
	
	public static String gCurFilename = null;
	public static String gFileDirectory = null;
	public static String gFileBaseName = null;
	public static String gImageFileDir = null;
	public static String gTmpFileDir = null;
	
	public static String gLastImageFile = "tempfile.png";
	public static float gLastCursorXpos = 0;
	public static float gLastCursorYpos = 0;
	
    
}
