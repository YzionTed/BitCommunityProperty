package com.bit.communityProperty.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.bit.communityProperty.R;


public abstract class BaseDialog extends Dialog {

	protected final static float ALPHA = 1f; // 完全不透明

	public BaseDialog(Context context) {
		super(context, R.style.Dialog_bocop);
	}

	public BaseDialog(Context context, int style) {
		super(context, style);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public interface ButtonListener {
		// public void OnNegativeButtonClickListener();

		void OnPositiveButtonClickListener(String data);

	}

	public interface OnDialogItemClickListener {
		void onDiaogItemClick(int position);
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}

}
