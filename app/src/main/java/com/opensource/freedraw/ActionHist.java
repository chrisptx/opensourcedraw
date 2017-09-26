package com.opensource.freedraw;

import android.graphics.Paint.Style;

//  This class holds a paint action, with all the relevant information to re-create the event
//  when re-drawing the action history after an undo-event
//
//  sequenceNum holds the value of Globals.gActionSequenceNum at the time the action was originally drawn

public class ActionHist
{
	Boolean isDeleted = false;
	int sequenceNum;		//  Sequence number is incremented on an up touch event
	ActionType actionType;
	Style strokeStyle;
	float strokeWidth;
	Shapes actionShape;
	int xPos;
	int yPos;
	int color;
	int fillColor;
	int brushWidth;
	int alphaVal;
}
