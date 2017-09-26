package com.opensource.freedraw;

import java.io.File;
import java.io.FileOutputStream;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MainEngine extends View
{
	private Canvas mCanvas = null;				//  current canvas available to all methods
	public static Canvas cursorCanvas = null;	//  cursor canvas available to all methods

	private Paint pBrush = new Paint();			//  current paintbrush available to all methods
	private Paint pCursorBrush = new Paint();	//  current paintbrush for the cursor
	
	private int xPos = 0, yPos = 0;

	private int screenWidth = 0;
	private int screenHeight = 0;
	private int xOffset = 0;
	private int yOffset = 0;
	
	private Bitmap mBitmap;
	private static Bitmap cursorBitmap;
	
	private Shapes drawingShape = Shapes.circle;

	private Resources res;		//  resource file handle

	public MainEngine(Context context)
	{
		super(context);
	
        res = this.getResources();		//  Load the resources
		
		cursorBitmap =  BitmapFactory.decodeResource(res, R.drawable.cursor1);
	}	
	
	//  Called when the screen size is changed or established for the first time
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
	    super.onSizeChanged(w, h, oldw, oldh);
	    
	    //  Save the screen width and height
	    screenWidth = w;
	    screenHeight = h;

		if (Globals.gLastImageFile != null && Globals.gLastImageFile.trim().length() != 0)
		{
			if (LoadFile())
			{
				return;
			}
		}

		Globals.gMainActivity.invalidateOptionsMenu();
		
		NewBitmap();
	    invalidate();
    }

    //  Create a new blank bitmap to draw on
    
    private void NewBitmap()
    {
    	Bitmap.Config conf = Bitmap.Config.ARGB_8888;
    	mBitmap = Bitmap.createBitmap(screenWidth, screenHeight, conf);

    	//  Create a new canvas to use to draw onto the new bitmap
    	mCanvas = new Canvas();
    
    	try
    	{
    		mCanvas.setBitmap(mBitmap);
    	}
    	catch (Exception ex)
    	{
    		ToastMessage.ShortToast("bitmap assign error...");
    	}

    	//  Initialize paint surface to white background
    	pBrush.setColor(Color.WHITE);
    	pBrush.setStyle(Style.FILL_AND_STROKE);
    	mCanvas.drawRect(0, 0, screenWidth, screenHeight, pBrush);
    	Globals.gLastImageFile = "";
    	Globals.gMainActivity.setTitle("FreeDraw");
    }

    //  Clear the canvas and erase the current drawing actions
    
    public void ClearCanvas()
    {
    	NewBitmap();
    	ClearHistory();
    	invalidate();
    }
    
	//  Redraw the screen
	
    @Override
	protected void onDraw(Canvas c)
   	{
	    super.onDraw(c);
	
	    cursorCanvas = c;
	    
    	c.drawBitmap(mBitmap, 0, 0, pBrush);
    	
    	// Globals.gBitmap = mBitmap;
    	
    	DrawTitleBar();

    	DrawCursor(c);
   	}

    //  Draw cursor outlines around drawing points
    
    public void DrawCursor(Canvas c)
    {
		pCursorBrush.setAlpha(255);
    	
    	c.drawBitmap(cursorBitmap, Globals.gLastXpos, Globals.gLastYpos, pCursorBrush);
    	
    	if (Globals.gCursorInfo == CursorInfo.black)
    		pCursorBrush.setColor(Color.BLACK);
    	else if (Globals.gCursorInfo == CursorInfo.white)
    		pCursorBrush.setColor(Color.WHITE);

    	if (Globals.gOffsetPixels != 0)
    	{
    		int saveAlpha = pCursorBrush.getAlpha();
    		pCursorBrush.setAlpha(150);
    		c.drawCircle(Globals.gLastUpXpos, Globals.gLastUpYpos, 45, pCursorBrush);
    		pCursorBrush.setAlpha(saveAlpha);
    	}
    }
    
    //  Draw the title bar depending on debug settings
    
    private void DrawTitleBar()
    {
    	if (Globals.gDebug)
    		ShowDebug();
    	else
    		ShowFileInfo();
    }

    //  Use this for general debugging when needed
    
	private void ShowDebug()
	{
		//  Globals.gMainActivity.setTitle("offset left is " + Globals.gOffsetLeft);
	}
	
	//  Show current filename and other information
	
	public void ShowFileInfo()
	{
		String title = "";
		int xpos, ypos;
		
		title += "FreeDraw";

		if (Globals.gShowCursorPos)
		{
			if ((xpos = Globals.gLastXpos) > screenWidth)
				xpos = screenWidth;
			else if (xpos < 0)
				xpos = 0;
			
			if ((ypos = Globals.gLastYpos) > screenHeight)
				ypos = screenHeight;
			else if (ypos < 0)
				ypos = 0;
			
			title += " (" + xpos + ", " + ypos + ")";
		}

		// title += " ( seqnum is " + Globals.gActionSequenceNum + ")";
		
		if (Globals.gAreRecording && Globals.gStopRecordingOnUpFlag)
		{
			title += " (Recording) ";
		}

		Globals.gMainActivity.setTitle(title);
	}
	
    
    //  Handle Touch Events
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
    	xPos = (int)event.getX();		//  Get Horizontal screen position of touch event
    	yPos = (int)event.getY();		//  Get Vertical screen position of touch event

    	Globals.gLastUpXpos = xPos;				//  Save position for finger touch circle 
    	Globals.gLastUpYpos = yPos;
    	
    	// Globals.gMainActivity.setTitle("xPos: " + xPos + ", yPos " + yPos);
    	
    	int radius = Globals.gBrushWidth;
    	
    	drawingShape = Globals.gBrushShape;

    	xOffset = 0;
    	yOffset = 0;

    	if (Globals.gCursorOffset == CursorOffset.left)
    		xOffset = 0 - Globals.gOffsetPixels;
    	else if (Globals.gCursorOffset == CursorOffset.right)
    		xOffset = Globals.gOffsetPixels;
    	else if (Globals.gCursorOffset == CursorOffset.up)
    		yOffset = 0 - Globals.gOffsetPixels;
    	else if (Globals.gCursorOffset == CursorOffset.down)
    		yOffset = Globals.gOffsetPixels;
    	
    	if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE)
    	{
    	   
    	    //  Set the last horizontal and vertical positions of the screen press
    	    Globals.gLastXpos = xPos + xOffset;
    		Globals.gLastYpos = yPos + yOffset;

    		if (!Globals.gAreRecording)		//  If we're not recording touches, then just return
    		{
    			invalidate();
    			return true;
    		}

    		pBrush.setStrokeWidth(Globals.gStrokeWidth);
    	    pBrush.setColor(Globals.gCurColor);
    	    pBrush.setAlpha(Globals.gCurAlpha);
    		
    	    if (Globals.gBrushSolid)
    	    	pBrush.setStyle(Style.FILL_AND_STROKE);
    	    else
    	    	pBrush.setStyle(Style.STROKE);
    	    
    	    ActionHist ah = new ActionHist();
    	    ah.sequenceNum = Globals.gActionSequenceNum;
    	    ah.actionShape = drawingShape;
    	    ah.xPos = Globals.gLastXpos;
    	    ah.yPos = Globals.gLastYpos;
    	    ah.color = Globals.gCurColor;
    	    ah.brushWidth = Globals.gBrushWidth;
    	    ah.alphaVal = Globals.gCurAlpha;
    	    ah.strokeStyle = pBrush.getStyle();
    	    ah.strokeWidth = pBrush.getStrokeWidth();
    	    
    	    if (drawingShape == Shapes.circle)
    	    {
    	    	try
    	    	{
    	    		mCanvas.drawCircle(Globals.gLastXpos, Globals.gLastYpos, radius, pBrush);
    	    		ah.actionType = ActionType.circle;
    	    	}
    		    catch (Exception ex)
    		    {
    		    	ToastMessage.LongToast("error while drawing circle");
    		    }
    	    }
    	    else if (drawingShape == Shapes.square)
    	    {
    	    	try
    	    	{
    	    		mCanvas.drawRect(Globals.gLastXpos, Globals.gLastYpos, Globals.gLastXpos + radius, Globals.gLastYpos + radius, pBrush);
    	    		ah.actionType = ActionType.square;
    	    	}
    	    	catch (Exception ex)
    	    	{
    	    		ToastMessage.LongToast("error while drawing square");
    	    	}
   	    	}
    	    else if (drawingShape == Shapes.triangle)
    	    {
   	    		DrawTriangle(Globals.gLastXpos, Globals.gLastYpos, mCanvas, pBrush);
   	    		ah.actionType = ActionType.triangle;
   	    	}

    	    Globals.gActionList.add(ah);
    	    
    	    //reset the transparency back to solid
    	    pBrush.setAlpha(255);

    	    //  force a redraw
    	    invalidate();
    	    
    		return true;
    	}
    	else if (event.getAction() == MotionEvent.ACTION_UP)
    	{
    		if (Globals.gAreRecording)		//  Increment action sequence number for use with undo feature, if we are currently recording
    		{
    			Globals.gActionSequenceNum++;
    			if (Globals.gStopRecordingOnUpFlag)
    			{
    				Globals.gAreRecording = false;
    				Globals.gMainActivity.invalidateOptionsMenu();
    			}
    		}
    		ShowFileInfo();
    	}
    	else		//  Aren't interested in any other events for now, so just return
    	{
    		return false;
    	}

    	return true;
    }

    //  Apply all actions in the action buffer to the current canvas
    
    private void ApplyActions()
    {
    	Canvas tCanvas = new Canvas();
    	tCanvas.setBitmap(mBitmap);
    	Paint p = new Paint();
    	ActionHist ah;
    	
    	//  Loop through the action array and apply the actions to the canvas
    	
    	for (int x=0; x < Globals.gActionList.size(); x++)
    	{
    		ah = Globals.gActionList.get(x);
    		if (ah.isDeleted)
    		{
    			continue;
    		}
    		p.setColor(ah.color);
    		p.setAlpha(ah.alphaVal);
    		p.setStyle(ah.strokeStyle);
    		p.setStrokeWidth(ah.strokeWidth);
    		
    		if (ah.actionType == ActionType.circle)
	    		tCanvas.drawCircle(ah.xPos, ah.yPos, ah.brushWidth, p);
    		else if (ah.actionType == ActionType.square)
    			tCanvas.drawRect(ah.xPos, ah.yPos, ah.xPos + ah.brushWidth, ah.yPos + ah.brushWidth, p);
    		else if (ah.actionType == ActionType.triangle)
    			DrawTriangle(ah.xPos, ah.yPos, tCanvas, p);
    		else if (ah.actionType == ActionType.floodfill)
    			FillArea(ah);
    	}
    }

    private void DrawTriangle(int x, int y, Canvas canvas, Paint paint)
    {
    	paint.setAntiAlias(true);

        Path path = new Path();
        path.moveTo(x, y);
        path.lineTo(x+Globals.gBrushWidth, y);
        path.moveTo(x, y);
        path.lineTo(x, y+Globals.gBrushWidth);
        path.moveTo(x, y+Globals.gBrushWidth);
        path.lineTo(x+Globals.gBrushWidth, y);
        path.close();

        canvas.drawPath(path, paint);    	
    }
    
    public void SetDrawingShape(Shapes shape)
    {
    	drawingShape = shape;
    	Globals.gBrushShape = shape;
    }
    
    public void ClearHistory()
    {
    	Globals.gActionList.clear();
    	Globals.gActionSequenceNum = 0;
    }

    public String GetImagePath()
    {
    	File fPath = new File(Globals.gMainContext.getExternalCacheDir(), "tempfile.png");
    	return fPath.getAbsolutePath();
    	
    }
    
    public Boolean SaveFile()
    {
    	String fileName = "tempfile.png";
    	
    	// File fPath = new File(Globals.gMainContext.getFilesDir(), fileName);
    	File fPath = new File(Globals.gMainContext.getExternalCacheDir(), fileName);

    	Log.d("SaveFile", "Saving File to " + fPath.getAbsolutePath());
    	
        try
    	{
        	FileOutputStream out = new FileOutputStream(fPath.getAbsolutePath());
        	mBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
        	out.close();
        	Log.d("SaveFile", "Image saved successfully...");
        	return true;
    	}
        catch (Exception e)
        {
    		// ToastMessage.ShortToast(e.getMessage());
        	Log.e("SaveFile", e.getMessage());
        	return false;
        }
    }
    
	public void Refresh()
	{
		invalidate();
	}

	public Boolean LoadFile()
	{
    	String fileName = Globals.gImageFile;
    	
    	File fPath = new File(Globals.gMainContext.getExternalCacheDir(), fileName);
    	
    	if (Globals.gDebug) Log.d("MainEngine", "Trying to load image " + fPath.getAbsolutePath());
    	
		if (!fPath.exists())
		{
			Globals.gLastImageFile = "";
			return false;
		}
		
	    BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inSampleSize = 1;			
	    options.inMutable = true;
	    options.inScaled = true;
	    options.outHeight = screenHeight;
	    options.outWidth = screenWidth;

	    mCanvas = new Canvas();

	    try		//  try to load/decode the image from storage
	    {
	    	mBitmap = BitmapFactory.decodeFile(fPath.getAbsolutePath(), options);
	    	mCanvas.setBitmap(mBitmap);
	    	Globals.gLastImageFile = fPath.getAbsolutePath();
	    	invalidate();
	    	if (Globals.gDebug) Log.d("MainEngine", "Image loaded from file: " + fPath.getAbsolutePath());
	    	return true;
	    }
	    catch (Exception ex)
	    {
	    	Globals.gLastImageFile = ""; 
	    	Log.e("MainEngine", "Error " + ex.getMessage());
	    	return false;
	    }
	}

	//  Return the current version number
	
	public String GetPackageVersion()
    {
    	try
    	{
        	PackageInfo pInfo;
    		pInfo = Globals.gMainActivity.getPackageManager().getPackageInfo(Globals.gMainActivity.getPackageName(), 0);
    		return pInfo.versionName;
    	}
    	catch (NameNotFoundException e)
    	{
    		return "";
    	}
    }

	//  Grab the current color from the last x and y coordinates
	
	public void GrabCurrentColor()
	{
		int pixel = mBitmap.getPixel(Globals.gLastXpos, Globals.gLastYpos);
        
		Globals.gRed = Color.red(pixel);
        Globals.gBlue = Color.blue(pixel);
        Globals.gGreen = Color.green(pixel);
	    Globals.gCurColor = 0xff000000 + Globals.gRed * 0x10000  + Globals.gGreen * 0x100  + Globals.gBlue;
	}
	
    public String GetGameVersion()
    {
    	try
    	{
        	PackageInfo pInfo;
    		pInfo = Globals.gMainContext.getPackageManager().getPackageInfo(Globals.gMainContext.getPackageName(), 0);
    		return pInfo.versionName;
    	}
    	catch (NameNotFoundException e)
    	{
    		return "";
    	}
    }

    public void UndoLastAction()
    {
    	if (Globals.gLastImageFile.isEmpty())	
    	{
    		NewBitmap();
    		ApplyActions();
    	}
    	else
    		LoadFile();

    	DeleteSequence();
    	ApplyActions();
    	invalidate();
    }
    
    private void DeleteSequence()
    {
    	int seqNum = Globals.gActionSequenceNum - 1;

    	if (seqNum < 0)
    	{
    		ClearHistory();
    		return;
    	}

    	int gsize = Globals.gActionList.size();

    	if (gsize == 0)
    	{
    		Globals.gActionSequenceNum = 0;
    		return;
    	}

    	//  Mark all the actions in this sequence as deleted
    	
    	for (int x=0; x < gsize; x++)
    	{
    		if (!Globals.gActionList.get(x).isDeleted)
    		{
    			if (Globals.gActionList.get(x).sequenceNum == seqNum)
    			{
    				Globals.gActionList.get(x).isDeleted = true;
    			}
    		}
    	}

   		Globals.gActionSequenceNum--;
    	
    	ShowFileInfo();

    }

    public void FillArea()
    {
    	Point p1 = new Point();
    	
    	p1.set(Globals.gLastXpos, Globals.gLastYpos);
    	
    	ActionHist ah = new ActionHist();
    	ah.sequenceNum = Globals.gActionSequenceNum;
    	ah.actionType = ActionType.floodfill;
    	ah.xPos = Globals.gLastXpos;
    	ah.yPos = Globals.gLastYpos;
    	ah.color = Globals.gCurColor;
    	ah.strokeStyle = Style.FILL_AND_STROKE;
    	ah.fillColor = mBitmap.getPixel(Globals.gLastXpos, Globals.gLastYpos);
    	new FillAreaTask(mBitmap, p1, ah.fillColor, ah.color).execute();
    	Globals.gActionList.add(ah);
    	Globals.gActionSequenceNum++;
    }

    public void FillArea(ActionHist ah)
    {
    	Point p1 = new Point();
    	
    	p1.set(ah.xPos, ah.yPos);
    	
    	new FillAreaTask(mBitmap, p1, ah.fillColor, ah.color).execute();
    }
    
}
