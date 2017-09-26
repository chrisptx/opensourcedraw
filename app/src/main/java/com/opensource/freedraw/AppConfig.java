package com.opensource.freedraw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import android.util.Log;

//  This class handles the configuration file

public class AppConfig extends Thread
{
	public AppConfig()
	{
	}
	
    public static boolean LoadConfig()
    {
    	File fPath = new File(Globals.gMainContext.getExternalCacheDir(), Globals.gConfigFile);

    	if (Globals.gDebug)
    		Log.d("AppConfig", "Trying to open config file " + fPath.getAbsolutePath());
		
		try
		{
		    InputStream instream = new FileInputStream(fPath.getAbsolutePath());		    
	    	InputStreamReader inputreader = new InputStreamReader(instream);
	    	BufferedReader buffreader = new BufferedReader(inputreader);
			LoadData(buffreader);
			instream.close();
		}
	    catch (FileNotFoundException e)
    	{
    		ConfigError();
    		return false;
    	}
    	catch (IOException e)
    	{
    		ConfigError();
    		return false;
    	}
	    	
    	return true;
	
    }

    public String GetConfigFilePath()
    {
    	File fPath = new File(Globals.gMainContext.getExternalCacheDir(), Globals.gConfigFile);
    	return fPath.getAbsolutePath();
    }
    
    //  Load config data one line at a time
    
	public static boolean LoadData(BufferedReader buffreader)
	{
    	String line;
    	int swVal, brushType, offset, color, cVal;

    	try
    	{
    		line = buffreader.readLine();		//  load last working image filename
    		Globals.gLastImageFile = line;
    		
    		line = buffreader.readLine();		//  load stroke width
    		swVal = GetInt(line);
    		if (swVal >= 0)
    			Globals.gBrushWidth = swVal;
    		else
    			Globals.gBrushWidth = 10;

    		line = buffreader.readLine();		//  load brush shape
    		brushType = GetInt(line);
    		if (brushType < 0) brushType = 0;
    		if (brushType == 0)
    			Globals.gBrushShape = Shapes.circle;
    		else if (brushType == 1)
    			Globals.gBrushShape = Shapes.square;
    		else if (brushType == 2)
    			Globals.gBrushShape = Shapes.triangle;

    		line = buffreader.readLine();		//  load left offset
    		offset = GetInt(line);
    		if (offset < 0) offset = 0;
    		Globals.gOffsetLeft = offset;
    		
    		line = buffreader.readLine();		//  load right offset
    		offset = GetInt(line);
    		if (offset < 0) offset = 0;
    		Globals.gOffsetRight = offset;

    		line = buffreader.readLine();		//  load red color
    		color = GetInt(line);
    		if (color < 0) color = 0;
    		Globals.gRed = color;

    		line = buffreader.readLine();		//  load green color
    		color = GetInt(line);
    		if (color < 0) color = 0;
    		Globals.gGreen = color;

    		line = buffreader.readLine();		//  load blue color
    		color = GetInt(line);
    		if (color < 0) color = 0;
    		Globals.gBlue = color;

    	    Globals.gCurColor = 0xff000000 + Globals.gRed * 0x10000  + Globals.gGreen * 0x100  + Globals.gBlue;
    		
    		line = buffreader.readLine();		//  load cursor up x offset
    		cVal = GetInt(line);
    		if (cVal < 0) cVal = 0;
    		Globals.gLastUpXpos = cVal;
    		
    		line = buffreader.readLine();		//  load cursor up y offset
    		cVal = GetInt(line);
    		if (cVal < 0) cVal = 0;
    		Globals.gLastUpYpos = cVal;

    		line = buffreader.readLine();		//  load cursor x offset
    		cVal = GetInt(line);
    		if (cVal < 0) cVal = 0;
    		Globals.gLastXpos = cVal;

    		line = buffreader.readLine();		//  load cursor y offset
    		cVal = GetInt(line);
    		if (cVal < 0) cVal = 0;
    		Globals.gLastYpos = cVal;

    		line = buffreader.readLine();		//  load cursor type
    		cVal = GetInt(line);
    		if (cVal < 0) cVal = 0;
   		
    		if (cVal == 0)
    			Globals.gCursorInfo = CursorInfo.black;
    		else if (cVal == 1)
    			Globals.gCursorInfo = CursorInfo.white;

    		line = buffreader.readLine();		//  load up offset
    		offset = GetInt(line);
    		if (offset < 0) offset = 0;
    		Globals.gOffsetUp = offset;
    		
    		line = buffreader.readLine();		//  load down offset
    		offset = GetInt(line);
    		if (offset < 0) offset = 0;
    		Globals.gOffsetDown = offset;
    			
    		line = buffreader.readLine();		//  load pixel offset
    		offset = GetInt(line);
    		if (offset < 0) offset = 0;
    		Globals.gOffsetPixels = offset;

    		line = buffreader.readLine();		//  load cursor offset type
    		offset = GetInt(line);
    		if (offset < 0) offset = 0;
    		if (offset == 0)
    			Globals.gCursorOffset = CursorOffset.left;
    		else if (offset == 1)
    			Globals.gCursorOffset = CursorOffset.right;
    		else if (offset == 2)
    			Globals.gCursorOffset = CursorOffset.up;
    		else if (offset == 3)
    			Globals.gCursorOffset = CursorOffset.down;
    		
    		line = buffreader.readLine();		//  load brush solid/outline
    		cVal = GetInt(line);
    		if (cVal < 0) cVal = 0;
    		if (cVal == 0)
    			Globals.gBrushSolid = false;
    		else
    			Globals.gBrushSolid = true;

    		line = buffreader.readLine();		//  load alpha value
    		cVal = GetInt(line);
    		if (cVal < 0) cVal = 255;
   			Globals.gCurAlpha = cVal;

   			line = buffreader.readLine();		//  load record off on up touch flag
    		cVal = GetInt(line);
    		if (cVal < 0) cVal = 0;
    		if (cVal == 0)
    			Globals.gStopRecordingOnUpFlag = false;
    		else
    			Globals.gStopRecordingOnUpFlag = true;

    		line = buffreader.readLine();		//  load stroke width value
    		cVal = GetInt(line);
    		if (cVal < 0) cVal = 1;
   			Globals.gStrokeWidth = cVal;
    	
   			line = buffreader.readLine();		//  load cursor position display flag
    		cVal = GetInt(line);
    		if (cVal < 0) cVal = 0;
    		if (cVal == 0)
    			Globals.gShowCursorPos = false;
    		else
    			Globals.gShowCursorPos = true;
    	}
    	
	    //  Trap any errors
    	catch (Exception ex)
    	{
    		return false;
    	}

    	return true;
	}
    
    //  Can't load config file so show an error message 
    
    public static void ConfigError()
    {
    	if (Globals.gDebug)
    	{
    		ToastMessage.ShortToast("Setting defaults...");
    		Log.e("AppConfig", "Config file error - loading defaults...");
    	}
    	
		SetDefaults();
    }
    
    //  Try to save  config data to internal memory for the 
    
    public static void SaveConfig()
    {
    	String dataStr = GetDataStr();

    	File fPath = new File(Globals.gMainContext.getExternalCacheDir(),  Globals.gConfigFile);

    	if (Globals.gDebug)
    		Log.d("SaveConfig", "Saving config to " + fPath.getAbsolutePath());
    	
   		try		//  Try to create/update the config file
    	{
   			OutputStream out = new FileOutputStream(fPath.getAbsolutePath());
    		OutputStreamWriter outstream = new OutputStreamWriter(out);
    		outstream.write(dataStr);
    		outstream.flush();
    		outstream.close();
    	}
    	catch (FileNotFoundException e)
    	{
    		Log.e("AppConfig", e.getMessage());
    	}
    	catch (IOException e)
    	{
    		Log.e("AppConfig", e.getMessage());
    	}
    }

    //  Create a data string with all of the config information
    
    private static String GetDataStr()
    {
    	String dataStr = "";
    	String eol = "\n";
   	
    	dataStr += Globals.gImageFile + eol;

    	dataStr += Globals.gBrushWidth + eol;
    	
    	if (Globals.gBrushShape == Shapes.circle)
    		dataStr += "0" + eol;
    	else if (Globals.gBrushShape == Shapes.square)
    		dataStr += "1" + eol;
    	else if (Globals.gBrushShape == Shapes.triangle)
    		dataStr += "2" + eol;
    	
    	dataStr += Globals.gOffsetLeft + eol;
    	
    	dataStr += Globals.gOffsetRight + eol;

    	dataStr += Globals.gRed + eol;
    	
    	dataStr += Globals.gGreen + eol;

    	dataStr += Globals.gBlue + eol;
    	
    	dataStr += Globals.gLastUpXpos + eol;
    	
    	dataStr += Globals.gLastUpYpos + eol;

    	dataStr += Globals.gLastXpos + eol;
    	
    	dataStr += Globals.gLastYpos + eol;

    	if (Globals.gCursorInfo == CursorInfo.black)
    		dataStr += "0" + eol;
    	else if (Globals.gCursorInfo == CursorInfo.white)
    		dataStr += "1" + eol;
    	else
    		dataStr += "2" + eol;
    		
    	dataStr += Globals.gOffsetUp + eol;

    	dataStr += Globals.gOffsetDown + eol;
    	
    	dataStr += Globals.gOffsetPixels + eol;
    	
    	if (Globals.gCursorOffset == CursorOffset.left)
    		dataStr += "0" + eol;
    	else if (Globals.gCursorOffset == CursorOffset.right)
    		dataStr += "1" + eol;
    	else if (Globals.gCursorOffset == CursorOffset.up)
    		dataStr += "2" + eol;
    	else if (Globals.gCursorOffset == CursorOffset.down)
    		dataStr += "3" + eol;

    	if (Globals.gBrushSolid)
    		dataStr += "1" + eol;
    	else
    		dataStr += "0" + eol;
    		
    	dataStr += Globals.gCurAlpha + eol;

    	if (Globals.gStopRecordingOnUpFlag)
    		dataStr += "1" + eol;
    	else
    		dataStr += "0" + eol;
    	
    	dataStr += Globals.gStrokeWidth + eol;

    	if (Globals.gShowCursorPos)
    		dataStr += "1" + eol;
    	else
    		dataStr += "0" + eol;

    	return dataStr;
    }

    //  Convert a string into an int
    
    public static int GetInt(String str)
    {
    	int intVal = 0;

    	try
    	{
    		intVal = Integer.parseInt(str);
    		return intVal;
    	}
    	catch(NumberFormatException nfe)
    	{
    		return -1;
    	} 
    }
   
    public static void SetupConfig()
    {
 	    
    }

    //  Set defaults if config file isn't found
    
    private static void SetDefaults()
    {
    	Globals.gLastImageFile =  "";
    	Globals.gBrushWidth = 10;
    	Globals.gStrokeWidth = 1;
    	Globals.gBrushShape = Shapes.circle;
    	Globals.gCursorOffset = CursorOffset.left;
    	Globals.gShowCursorPos = false;
    	Globals.gStopRecordingOnUpFlag = true;
    	Globals.gOffsetLeft = 0;
    	Globals.gOffsetRight = 200;
    	Globals.gOffsetUp = 0;
    	Globals.gOffsetDown = 200;
    	Globals.gOffsetPixels = 200;
    	Globals.gCursorOffset = CursorOffset.left;    	
    	Globals.gRed = 0;
    	Globals.gBlue = 0;
    	Globals.gGreen = 100;
    	Globals.gLastUpXpos = 0;
    	Globals.gLastUpYpos = 0;
    	Globals.gLastXpos = 0;
    	Globals.gLastYpos = 0;
    	Globals.gCurAlpha = 255;
	    Globals.gCurColor = 0xff000000 + Globals.gRed * 0x10000  + Globals.gGreen * 0x100  + Globals.gBlue;
    }
    
    public static void SetOffsetValue()
    {
    	if (Globals.gCursorOffset == CursorOffset.left)
    	{
    		Globals.gCursorOffset = CursorOffset.up;
    		Globals.gLastUpXpos = Globals.gLastXpos;
    		Globals.gLastUpYpos = Globals.gLastYpos + Globals.gOffsetPixels;
    		Globals.gOffsetLeft = 0;
    	}
    	else if (Globals.gCursorOffset == CursorOffset.up)
    	{
    		Globals.gCursorOffset = CursorOffset.right;
    		Globals.gLastUpXpos = Globals.gLastXpos - Globals.gOffsetPixels;
    		Globals.gLastUpYpos = Globals.gLastYpos;
    		Globals.gOffsetUp = 0;
    	}
    	else if (Globals.gCursorOffset == CursorOffset.right)
    	{
    		Globals.gCursorOffset = CursorOffset.down;
    		Globals.gLastUpXpos = Globals.gLastXpos;
    		Globals.gLastUpYpos = Globals.gLastYpos - Globals.gOffsetPixels;
    		Globals.gOffsetRight = 0;
    	}
    	else if (Globals.gCursorOffset == CursorOffset.down)
    	{
    		Globals.gCursorOffset = CursorOffset.left;
    		Globals.gLastUpXpos = Globals.gLastXpos + Globals.gOffsetPixels;
    		Globals.gLastUpYpos = Globals.gLastYpos;
    		Globals.gOffsetDown = 0;
    	}
    	
    }
    
}
