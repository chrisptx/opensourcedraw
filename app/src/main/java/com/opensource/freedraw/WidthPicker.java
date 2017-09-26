package com.opensource.freedraw;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class WidthPicker extends Dialog
{
    SeekBar widthBar;
    LinearLayout mScreen;
  	int newWidth;

    public WidthPicker(Context context)
    {
    	super(context);
    	
    	setContentView(R.layout.widthpicker);
    	
    	setCancelable(false);
    	
    	mScreen = (LinearLayout) findViewById(R.id.myScreen);
    	widthBar = (SeekBar) findViewById(R.id.strokewidthbar);

    	widthBar.setOnSeekBarChangeListener(seekBarChangeListener);

    	setTitle("Use slider to adjust brush width");
    	
    	Button saveButton = (Button) findViewById(R.id.button_save);
    	saveButton.setOnClickListener
    	(
    		new View.OnClickListener()
    		{
    			@Override
    			public void onClick(View v)
    			{
    				Globals.gBrushWidth = newWidth;
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

    	newWidth = Globals.gBrushWidth;
    	
    	initWidth();
    }
    
    
    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener()
    {
       @Override
       public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
       {
          // TODO Auto-generated method stub
          changeWidth();
       }

       @Override
	   public void onStartTrackingTouch(SeekBar seekBar)
	   {
	      // TODO Auto-generated method stub}
	   }

	   @Override
	   public void onStopTrackingTouch(SeekBar seekBar)
	   {
	      // TODO Auto-generated method stub}};
	   }

	};

	private void initWidth()
	{
	    widthBar.setProgress(Globals.gBrushWidth);
	}
	
	private void changeWidth()
	{
	    if ((newWidth = widthBar.getProgress()) < 1)
	    {
	    	newWidth = 1;
	    }
	    

	    Globals.gBrushWidth = newWidth;

	    TextView tv = (TextView) findViewById(R.id.widthtitle);
	    tv.setText("Current Brush Width: " + newWidth);
    }
    
}
    
