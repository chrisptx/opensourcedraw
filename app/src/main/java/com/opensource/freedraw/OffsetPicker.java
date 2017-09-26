package com.opensource.freedraw;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class OffsetPicker extends Dialog
{
    private int offsetPixels;
    SeekBar offsetBar;
    LinearLayout mScreen;

    public OffsetPicker(Context context)
    {
    	super(context);
    	
    	setContentView(R.layout.offsetpicker);

    	setCancelable(false);
    	
    	mScreen = (LinearLayout) findViewById(R.id.myScreen);
    	
    	offsetBar = (SeekBar) findViewById(R.id.offset_pixels);

    	offsetBar.setOnSeekBarChangeListener(offsetBarChangeListener);

    	setTitle("Select offset from finger press (0 - 300)");
    	
    	Button saveButton = (Button) findViewById(R.id.button_save);
    	saveButton.setOnClickListener
    	(
    		new View.OnClickListener()
    		{
    			@Override
    			public void onClick(View v)
    			{
   					Globals.gOffsetPixels = offsetPixels;
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

    	initOffsets();
    	
    }
    
    
    private SeekBar.OnSeekBarChangeListener offsetBarChangeListener = new SeekBar.OnSeekBarChangeListener()
    {
       @Override
       public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
       {
          changeOffset();
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

	private void initOffsets()
	{
		offsetPixels = Globals.gOffsetPixels;
		offsetBar.setProgress(offsetPixels);
	}
	
	//  Change the current left right touch offset
	
	private void changeOffset()
	{
	    offsetPixels = offsetBar.getProgress();
	    
	    if (offsetPixels != Globals.gOffsetPixels)
	    {
	    	offsetBar.setProgress(offsetPixels);
	    	Globals.gOffsetPixels = offsetPixels;
    	}
	    TextView tv = (TextView) findViewById(R.id.pixOffset);
	    tv.setText("Current Offset: " + offsetPixels);	    
    }
    
}
    
