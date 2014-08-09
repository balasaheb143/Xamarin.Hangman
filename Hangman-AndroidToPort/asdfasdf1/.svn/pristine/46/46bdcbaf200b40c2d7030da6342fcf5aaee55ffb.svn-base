package com.fundora.hangman.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.fundora.hangman.R;

public class LetterButton extends ImageView {

	String letter;
	int clikedImageId;
	int unclickedImageId;

	public LetterButton(Context context) {
		super(context);
	}

	public LetterButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LetterButton);
		letter = a.getString(R.styleable.LetterButton_letter);
		clikedImageId = a.getResourceId(R.styleable.LetterButton_clickedImage, 0);
		unclickedImageId = a.getResourceId(R.styleable.LetterButton_unclickedImage, 0);

		if (unclickedImageId != 0){
			setImageResource(unclickedImageId);
		}
		a.recycle();
	}

	public String getLetter() {
		return letter;
	}

	public void setLetter(String l) {
		letter = l;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			setImageResource(clikedImageId);
			return super.onTouchEvent(event);
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			setImageResource(unclickedImageId);
			return super.onTouchEvent(event);
		}
		return super.onTouchEvent(event);
	}
}
