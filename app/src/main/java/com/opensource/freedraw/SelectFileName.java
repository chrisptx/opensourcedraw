package com.opensource.freedraw;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;

public class SelectFileName extends Dialog
{
	private Context mContext;
	private int imageId = 100;
	private Boolean saveFlag;
	
	public SelectFileName(Context context, Boolean saveMode)
	{
		super(context);
	
		Log.d("SelectFileName", "In select filename");
		
		mContext = context;
		
		saveFlag = saveMode;

		if (saveFlag)
			setContentView(R.layout.loadfile);	
			// setContentView(R.layout.savefile);
		else
			setContentView(R.layout.loadfile);	

		if (saveFlag)
		{
			EditText et = (EditText) findViewById(R.id.curfilename);
			String tstr = Globals.gLastImageFile;
			if (tstr != null && tstr.length() > 0)
				et.setText(tstr);
			else
				et.setText("imagex");
			et.setSelection(et.getText().length());			
			setTitle("Select Filename to save as");
		}
		else
		{
			setTitle("Select File to load");
		}
		
		Button saveButton = (Button) findViewById(R.id.button_save);
		saveButton.setOnClickListener
		(
			new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					TextView tv = (TextView) findViewById(R.id.curfilename);
					String fname = tv.getText().toString();
					if (saveFlag)
						Globals.gMainEngine.SaveFile();
					else
						Globals.gMainEngine.LoadFile();
					dismiss();
				}
			}
		);
		
		if (saveFlag)
			saveButton.setText("Save");
		else
			saveButton.setText("Load");
		
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

		Log.d("SelectFileName", "Listing current files");

		ListCurrentFiles();
	}
	
	private void ListCurrentFiles()
	{
		TableRow tr;
		TextView tv;
		
		String mFileDirectory = Globals.gImageFileDir;
		
		File sd = new File(mFileDirectory);

		//gets a list of the files
        File[] sdDirList = sd.listFiles(); 

        if (sdDirList.length == 0)
        	return;

        TableLayout tl = (TableLayout)findViewById(R.id.filetable);
		
		LinearLayout.LayoutParams layoutParams =
			new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		TableRow.LayoutParams layoutParams2 =
			new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	
		for (File ff: sdDirList)
		{
			tr = (TableRow) new TableRow(mContext);
			tr.setPadding(5, 5, 5, 5);
			tv = new TextView(mContext);
			tv.setTextSize(20);
			tv.setHeight(50);
			tv.setText(GetShortFilename(ff.getName()));
			tv.setId(imageId++);

			tv.setOnTouchListener(new OnTouchListener()
			{
				@Override
				public boolean onTouch(View v, MotionEvent event)
				{
					if (event.getAction() == MotionEvent.ACTION_UP)
					{
						int sid = v.getId();
						TextView fv = (TextView) findViewById(sid);
						TextView tfn = (TextView) findViewById(R.id.curfilename);
						if (saveFlag)
							tfn.setText(fv.getText());
						else
						{
							Globals.gMainEngine.LoadFile();
							dismiss();
						}
					}
					return false;
				}
			});

			tv.setOnClickListener(new Button.OnClickListener()
			{
	            @Override
	            public void onClick(View v)
	            {
					ToastMessage.LongToast("press received");
	            }
	        });
			
			tr.addView(tv, layoutParams2);

			tv = new TextView(mContext);
			tv.setHeight(50);
			tv.setTextSize(20);
			tv.setPadding(40, 0, 0, 0);
			tv.setText(getDate(ff.lastModified(), "MM/dd/yyyy hh:mm"));
			
			tr.addView(tv, layoutParams2);
			tl.addView(tr, layoutParams);
			
		}
	}
	
	private String GetShortFilename(String s)
	{
		String name = s;

		final int lastPeriodPos = name.lastIndexOf('.');
		if (lastPeriodPos <= 0)
		{
			return name;
		}
		else
		{
			return(name.substring(0, lastPeriodPos));
		}
	}
	
	public static String getDate(long milliSeconds, String dateFormat)
	{
	    // Create a DateFormatter object for displaying date in specified format.
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

		// Create a calendar object that will convert the date and time value in milliseconds to date. 
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return formatter.format(calendar.getTime());
	}	

}
