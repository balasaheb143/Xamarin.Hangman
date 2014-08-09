package com.fundora.hangman.util;

import com.fundora.hangman.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class SelectableTextView extends TextView {

	private boolean selected = false;

	public SelectableTextView(Context context) {
		super(context);
	}

	public SelectableTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SelectableTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		if (selected) {
			this.setBackgroundResource(R.color.blue);
		} else {
			this.setBackgroundResource(R.color.black);
		}
	}

	public boolean isSelected() {
		return selected;
	}
}
