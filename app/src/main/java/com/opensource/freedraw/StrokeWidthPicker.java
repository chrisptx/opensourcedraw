package com.opensource.freedraw;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class StrokeWidthPicker extends Dialog
{
    SeekBar widthBar;
    LinearLayout mScreen;
  	int newWidth;

    public StrokeWidthPicker(Context context)
    {
    	super(context);
    	
    	setContentView(R.layout.strokewidthpicker);

    	setCancelable(false);
    	
    	mScreen = (LinearLayout) findViewById(R.id.myScreen);
    	widthBar = (SeekBar) findViewById(R.id.strokewidthbar);

    	widthBar.setOnSeekBarChangeListener(seekBarChangeListener);

    	setTitle("Use slider to adjust stroke width");
    	
    	Button saveButton = (Button) findViewById(R.id.button_save);
    	saveButton.setOnClickListener
    	(
    		new View.OnClickListener()
    		{
    			@Override
    			public void onClick(View v)
    			{
    				Globals.gStrokeWidth = newWidth;
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

    	newWidth = Globals.gStrokeWidth;
    	
    	initWidth();
    }
    
    
    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener()
    {
       @Override
       public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
       {
          changeWidth();
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

	private void initWidth()
	{
	    widthBar.setProgress(Globals.gStrokeWidth);
	}
	
	private void changeWidth()
	{
	    if ((newWidth = widthBar.getProgress()) < 1)
	    {
	    	newWidth = 1;
	    }

	    Globals.gStrokeWidth = newWidth;

	    TextView tv = (TextView) findViewById(R.id.widthtitle);
	    tv.setText("Current Stroke Width: " + newWidth);
    }
	
    
}
    
