package com.opensource.freedraw;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class ColorPicker extends Dialog
{
    private int seekR, seekG, seekB;
    SeekBar redSeekBar, greenSeekBar, blueSeekBar;
    LinearLayout mScreen;
  	int newColor;

    public ColorPicker(Context context)
    {
    	super(context);
    	
    	setContentView(R.layout.colorpicker);

    	mScreen = (LinearLayout) findViewById(R.id.myScreen);
    	redSeekBar = (SeekBar) findViewById(R.id.mySeekingBar_R);
    	greenSeekBar = (SeekBar) findViewById(R.id.mySeekingBar_G);
    	blueSeekBar = (SeekBar) findViewById(R.id.mySeekingBar_B);

    	redSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
    	greenSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
    	blueSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);

    	setTitle("Set Brush Color");

    	setCancelable(false);
    	
    	Button saveButton = (Button) findViewById(R.id.button_save);
    	saveButton.setOnClickListener
    	(
    		new View.OnClickListener()
    		{
    			@Override
    			public void onClick(View v)
    			{
    				Globals.gCurColor = newColor;
    			    Globals.gRed = redSeekBar.getProgress();
    			    Globals.gGreen = greenSeekBar.getProgress();
    			    Globals.gBlue = blueSeekBar.getProgress();
    			    Globals.gMainActivity.invalidateOptionsMenu();
    				dismiss();
    			}
    		}
    	);
    	
    	Button cancelButton = (Button) findViewById(R.id.button_cancel);
    	cancelButton.setOnClickListener
    	(
    		new View.OnClickListener()
    		{
    			@Override
    			public void onClick(View v)
    			{
    				dismiss();
    			}
    		}
    	);

    	initColor();
    }
    
    
    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener()
    {
       @Override
       public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
       {
          changeColor();
       }

       @Override
	   public void onStartTrackingTouch(SeekBar seekBar)
	   {
	   }

	   @Override
	   public void onStopTrackingTouch(SeekBar seekBar)
	   {
	   }

	};

	private void initColor()
	{
	    redSeekBar.setProgress(Globals.gRed);
	    greenSeekBar.setProgress(Globals.gGreen);
	    blueSeekBar.setProgress(Globals.gBlue);
	}
	
	//  Change the current color based on the slider bar values
	
	private void changeColor()
	{
	    seekR = redSeekBar.getProgress();
	    seekG = greenSeekBar.getProgress();
	    seekB = blueSeekBar.getProgress();
	    
	    newColor = 0xff000000 + seekR * 0x10000  + seekG * 0x100  + seekB;

	    Globals.gCurColor = newColor;
	    
        TextView tv = (TextView) findViewById(R.id.colorbar);
        tv.setBackgroundColor(newColor);
    }
	
}
    
