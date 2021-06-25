package com.vb.helperlibrary.jsevaluator;

import android.os.Handler;

import com.vb.helperlibrary.interfaces.HandlerWrapperInterface;


public class HandlerWrapper implements HandlerWrapperInterface {
	private final Handler mHandler;

	public HandlerWrapper() {
		mHandler = new Handler();
	}

	@Override
	public void post(Runnable r) {
		mHandler.post(r);
	}
}
