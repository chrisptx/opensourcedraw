package com.opensource.freedraw;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class AlphaPicker extends Dialog
{
    private int alphaVal;
    SeekBar alphaBar;

    public AlphaPicker(Context context)
    {
    	super(context);
    	
    	setContentView(R.layout.alphapicker);

    	setCancelable(false);
    	
    	alphaBar = (SeekBar) findViewById(R.id.alphaValBar);
    	alphaBar.setOnSeekBarChangeListener(alphaBarChangeListener);

    	setTitle("Select transparancy value..");
    	
    	Button saveButton = (Button) findViewById(R.id.button_save);
    	saveButton.setOnClickListener
    	(
    		new View.OnClickListener()
    		{
    			@Override
    			public void onClick(View v)
    			{
   					Globals.gCurAlpha = alphaVal;
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

    	initAlpha();
    	
    }
    
    
    private SeekBar.OnSeekBarChangeListener alphaBarChangeListener = new SeekBar.OnSeekBarChangeListener()
    {
       @Override
       public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
       {
          changeAlpha();
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

	private void initAlpha()
	{
		alphaVal = Globals.gCurAlpha;
		alphaBar.setProgress(alphaVal);
	}
	
	//  Change the current left right touch offset
	
	private void changeAlpha()
	{
	    alphaVal = alphaBar.getProgress();
	    
	    if (alphaVal != Globals.gCurAlpha)
	    {
    		alphaBar.setProgress(alphaVal);
	    	Globals.gCurAlpha = alphaVal;
    	}

	    TextView tv = (TextView) findViewById(R.id.alphaTextVal);
	    tv.setText("Current Transparency: " + alphaVal);	    
	    
    }
    
}
    
