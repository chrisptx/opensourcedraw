package com.opensource.freedraw;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MesgBox extends Dialog
{
	public MesgBox(Context context, String title, String msg)
	{
		super(context);

		setTitle(title);
		setContentView(R.layout.mesgbox);
		TextView msgText = (TextView) findViewById(R.id.mesgbox_text); 
		msgText.setText(msg); 		

		this.setCancelable(false);
		this.setCanceledOnTouchOutside(false);
		
		Button closeButton = (Button) findViewById(R.id.button_okay);
		closeButton.setOnClickListener
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
	}
}
