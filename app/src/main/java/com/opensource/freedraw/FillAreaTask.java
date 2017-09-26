package com.opensource.freedraw;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;

public class FillAreaTask extends AsyncTask<Void, Integer, Void>
{
	Bitmap bmp;
	Point pt;
	int replacementColor, targetColor;

	public FillAreaTask(Bitmap bm, Point p, int sc, int tc)
	{
		this.bmp = bm;
		this.pt = p;
		this.replacementColor = tc;
		this.targetColor = sc;
	}
    
	@Override
	protected void onPreExecute()
	{
	}

	@Override
	protected void onProgressUpdate(Integer... values)
	{
	}

	@Override
	protected Void doInBackground(Void... params)
	{
		FloodFill f = new FloodFill();
		f.floodFill(bmp, pt, targetColor, replacementColor);
		return null;
	}

	@Override
	protected void onPostExecute(Void result)
	{
		Globals.gMainEngine.invalidate();
    }
}

