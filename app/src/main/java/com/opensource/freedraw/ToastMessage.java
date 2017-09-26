package com.opensource.freedraw;

import android.widget.Toast;

//  Class to handle popup messages that don't require any user input

public class ToastMessage
{
    public static void ShortToast(String str)
    {
   		CharSequence text = str;
   		int duration = Toast.LENGTH_SHORT;
   		Toast toast = Toast.makeText(Globals.gMainContext, text, duration);
   		toast.show();
     }

    public static void LongToast(String str)
    {
   		CharSequence text = str;
   		int duration = Toast.LENGTH_LONG;
   		Toast toast = Toast.makeText(Globals.gMainContext, text, duration);
   		toast.show();
    }
}

