package com.opensource.freedraw;

import java.io.File;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
//import android.graphics.Point;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
//import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity
{
	private MainEngine engine;
	private Context mContext;
	private static Bitmap bgBitmap;
	private static Paint pBrush = new Paint();
	private Resources res;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mContext = this;
        Globals.gMainActivity = this;
        Globals.gMainContext = mContext;
        res = getResources();
        
        engine = new MainEngine(this);		//  Start a new drawing engine
        
        Globals.gMainEngine = engine;

        setTitle("FreeDraw");
        
		setContentView(engine);				//	Set the view to the drawing engine

        AppConfig.LoadConfig();
		
		engine.LoadFile();

    }

    @Override
    protected void onStop()
    {
    	// engine.SaveBitMap();
    	super.onStop();
    }
    
    @Override
    protected void onPause()
    {
    	// engine.SaveBitMap();
    	super.onPause();
    }

    @Override
    protected void onResume()
    {
    	AppConfig.LoadConfig();
    	engine.Refresh();
    	super.onPause();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	getMenuInflater().inflate(R.menu.activity_main_menu, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
    	SetCurColorBox(menu);

    	if (Globals.gStopRecordingOnUpFlag)
    		menu.findItem(R.id.menu_recordoffonup).setChecked(true);
    	else
    		menu.findItem(R.id.menu_recordoffonup).setChecked(false);

    	if (Globals.gBrushShape == Shapes.circle)
    		menu.findItem(R.id.menu_circles).setChecked(true);
    	else if (Globals.gBrushShape == Shapes.square)
    		menu.findItem(R.id.menu_squares).setChecked(true);
    	else if (Globals.gBrushShape == Shapes.triangle)
    		menu.findItem(R.id.menu_triangles).setChecked(true);
    		
    	if (Globals.gBrushSolid)
    		menu.findItem(R.id.menu_brushsolid).setChecked(true);
    	else
    		menu.findItem(R.id.menu_brushoutline).setChecked(true);

    	if (Globals.gShowCursorPos)
    		menu.findItem(R.id.menu_showcursorpos).setChecked(true);
    	else
    		menu.findItem(R.id.menu_showcursorpos).setChecked(false);

    	if (Globals.gCursorInfo == CursorInfo.black)
    		menu.findItem(R.id.menu_setcursorblack).setChecked(true);
    	else
    		menu.findItem(R.id.menu_setcursorwhite).setChecked(true);

    	return true;
    }
    
	//  Handle the menu requests
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.menu_colorbox:
    			PickColor();
				return true;

			case R.id.menu_record:
				ToggleRecord(item);
				return true;

			case R.id.menu_clear:
    			ClearCanvas();
    			return true;

			case R.id.menu_save:
    			SaveImage();
    			return true;

			case R.id.menu_transparency:
    			SetTransparency();
    			return true;

			case R.id.menu_revert:
    			engine.UndoLastAction();
    			return true;

    		case R.id.menu_send:
    			ShareImage();
    			return true;

    		case R.id.menu_pickcolor:
    			PickColor();
    			return true;

    		case R.id.menu_setcursoroffset:
    			SelectCursorOffset();
    			return true;

    		case R.id.menu_pickbgcolor:
    			PickBackgroundColor(item);
    			return true;

    		case R.id.menu_floodfill:
    			engine.FillArea();
    			return true;

    		case R.id.menu_setcursorblack:
    			Globals.gCursorInfo = CursorInfo.black;
    			engine.invalidate();
    			return true;

    		case R.id.menu_setcursorwhite:
    			Globals.gCursorInfo = CursorInfo.white;
    			engine.invalidate();
    			return true;

    		case R.id.menu_rotateoffset:
    			AppConfig.SetOffsetValue();
    			engine.invalidate();
    			return true;

    		case R.id.menu_circles:
    			engine.SetDrawingShape(Shapes.circle);
    			return true;

    		case R.id.menu_squares:
    			engine.SetDrawingShape(Shapes.square);
    			return true;

    		case R.id.menu_triangles:
    			engine.SetDrawingShape(Shapes.triangle);
    			return true;

    		case R.id.menu_brushwidth:
    			ChangeBrushWidth();
    			return true;
    			
    		case R.id.menu_strokewidth:
    			ChangeStrokeWidth();
    			return true;
    			
    		case R.id.menu_brushsolid:
    			Globals.gBrushSolid = true;
    			invalidateOptionsMenu();
    			return true;

    		case R.id.menu_brushoutline:
    			Globals.gBrushSolid = false;
    			invalidateOptionsMenu();
    			return true;

    		case R.id.menu_recordoffonup:
    			ToggleRecordUpFlag(item);
    			return true;

    		case R.id.menu_showcursorpos:
    			ToggleCursorPosFlag(item);
    			return true;

    		case R.id.menu_exit:
    			SaveImage();
    			AppConfig.SaveConfig();
    			ExitProg();
    			return true;
    			
    		case R.id.menu_about:
    			AboutApp();
    			return true;

    		default:
	            return super.onOptionsItemSelected(item);				
		}
	}

	private void ExitProg()
	{
		System.exit(0);
	}

	private void ClearCanvas()
	{
		engine.ClearCanvas();
	}

	//  Share image through email or bluetooth
	
	private void ShareImage()
	{
		engine.SaveFile();
		
		String imagePath = engine.GetImagePath();

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/html");
		intent.putExtra(Intent.EXTRA_SUBJECT, "FreeDraw Image");
		intent.putExtra(Intent.EXTRA_TEXT, "My latest doodle");
		
		Uri uri = Uri.fromFile(new File(imagePath));
		intent.putExtra(Intent.EXTRA_STREAM, uri);		

		startActivity(Intent.createChooser(intent, "Send Email"));
	}

	//  Open the color picker dialog
	
	private void PickColor()
	{
		ColorPicker cp = new ColorPicker(this);
		cp.show();
	}

	//  Draw the current color box filled with the current color
	
	private void SetCurColorBox(Menu menu)
	{
    	Bitmap.Config conf = Bitmap.Config.ARGB_8888;
    	bgBitmap = Bitmap.createBitmap(48, 48, conf);
    	Canvas mCanvas = new Canvas();
    
    	try
    	{
    		mCanvas.setBitmap(bgBitmap);
    	}
    	catch (Exception ex)
    	{
    		ToastMessage.ShortToast("bitmap assign error...");
    	}

    	pBrush.setColor(Globals.gCurColor);
    	pBrush.setStyle(Style.FILL_AND_STROKE);
    	mCanvas.drawRect(0, 0, 47, 47, pBrush);
    	
    	MenuItem item = menu.findItem(R.id.menu_colorbox);
    	Drawable bmd = new BitmapDrawable(res, bgBitmap);
    	item.setIcon(bmd);
	}
	
	//  Pick the brush color from the current cursor location
	
	private void PickBackgroundColor(MenuItem item)
	{
		engine.GrabCurrentColor();
		invalidateOptionsMenu();
	}
	
	//  Save the current image and update the globals class with the current filename
	
	private void SaveImage()
	{
		engine.SaveFile();
	}

	//  Save the image with a potential new name

/*
	private void SaveImageAs()
	{
		AppConfig.SaveConfig();
		SelectFileName sfn = new SelectFileName(this, true);
		sfn.show();
	}
	//  Open the load image file dialog
	
	private void LoadImage()
	{
		SelectFileName sfn = new SelectFileName(this, false);
		sfn.show();
	}

	//  Delete the current image
	
	private void DeleteImage()
	{
		if (Globals.gLastImageFile == null || Globals.gLastImageFile.isEmpty())
		{
			return;
		}
		else
		{
			engine.DeleteFile();
		}
	}
*/
	
	//  Display an alert message
	
/*
	private void Alert(String str)
	{
		MesgBox mb = new MesgBox(mContext, "Alert", str);
		mb.show();
	}
*/
	
	//  Open the change brush width dialog
	
	private void ChangeBrushWidth()
	{
		WidthPicker bwp = new WidthPicker(this);
		bwp.show();
	}

	//  Open the change stroke width dialog
	
	private void ChangeStrokeWidth()
	{
		StrokeWidthPicker swp = new StrokeWidthPicker(this);
		swp.show();
	}

	//  Toggle recording flag on/off
	
	private void ToggleRecord(MenuItem item)
	{
		if (Globals.gAreRecording)
		{
			Globals.gAreRecording = false;
			item.setIcon(R.drawable.button_start_record);
		}
		else
		{
			Globals.gAreRecording = true;
			item.setIcon(R.drawable.button_stop_record);
		}
		engine.ShowFileInfo();
	}

	//  Show the about app dialog
	
	private void AboutApp()
	{
		String version = engine.GetGameVersion();
		MesgBox mb = new MesgBox(this, "FreeDraw", "v" + version + "\r\n\r\nBy Chris Peterson");
		mb.show();
	}

	//  Open the cursor offset dialog
	
	private void SelectCursorOffset()
	{
		OffsetPicker ofp = new OffsetPicker(this);
		ofp.show();
	}
	
	//  Open the set transparancy dialog
	
	private void SetTransparency()
	{
		AlphaPicker afp = new AlphaPicker(this);
		afp.show();
	}
	
	//  Toggle recording on/off flag on press up action
	
	private void ToggleRecordUpFlag(MenuItem item)
	{
		if (Globals.gStopRecordingOnUpFlag)
		{
			Globals.gStopRecordingOnUpFlag = false;
		}
		else
		{
			Globals.gStopRecordingOnUpFlag = true;
			Globals.gAreRecording = false;
		}
		
		invalidateOptionsMenu();
	}

	//  Toggle the display of the cursor position in the title bar
	
	private void ToggleCursorPosFlag(MenuItem item)
	{
		if (Globals.gShowCursorPos)
		{
			Globals.gShowCursorPos = false;
		}
		else
		{
			Globals.gShowCursorPos = true;
		}
		
		engine.ShowFileInfo();
		
		invalidateOptionsMenu();
	}
}
