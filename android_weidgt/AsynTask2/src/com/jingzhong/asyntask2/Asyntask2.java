package com.jingzhong.asyntask2;

import java.lang.Thread.State;

import android.os.Handler;
import android.os.Message;

import com.jingzhong.asyntask2.utils.ThreadService;
/**
 * 
 * @author dengzhijiang
 * 
 */
public abstract class Asyntask2<Params, Progress, Result> {

	private static final int POST_FINSH = 0x01;
	private static final int POST_PROGRESS = 0x02;
	private static final int POST_START=0x03;

	private Thread thread;

	private static final internalHandler sHandler = new internalHandler();

	public Asyntask2() {
		init();
	}

	private static void init() {
		sHandler.getLooper();
	}

	private boolean isCancel = false;

	protected abstract Result doInbackProgres(Params... params);
	
	protected void doStart() {
		
	}

	protected void doProgress(Progress... p) {

	}

	protected void doResult(Result res) {

	}

	public void cancel() {
		if (thread.getState() == State.RUNNABLE) {
			setCancel(true);
			thread.interrupt();
		}

	}

	/**
	 * @return the isCancel
	 */
	public boolean isCancel() {
		return isCancel;
	}

	/**
	 * @param isCancel
	 *            the isCancel to set
	 */
	public void setCancel(boolean isCancel) {
		this.isCancel = isCancel;
	}

	private void finsh(Result result) {
		doResult(result);
	}

	private void onPostProgress(Progress... progress) {
		doProgress(progress);
	}

	private static class AsynTaskResult<Data> {
		Data[] mData;
		@SuppressWarnings("rawtypes")
		Asyntask2 mTask;
		@SuppressWarnings("rawtypes")
		AsynTaskResult(Asyntask2 task, Data... data) {
			mTask = task;
			mData = data;
		}

	}

	protected void updateProgress(Progress... p) {
		if (!isCancel) {
			sHandler.obtainMessage(POST_PROGRESS,
					new AsynTaskResult<Progress>(this, p)).sendToTarget();
		}
	}

	@SuppressWarnings("unchecked")
	public final void execute(final Params... params) {
		sHandler.obtainMessage(POST_START,
				new AsynTaskResult<Result>(Asyntask2.this))
				.sendToTarget();
		Thread run = new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Result result = doInbackProgres(params);
				sHandler.obtainMessage(POST_FINSH,
						new AsynTaskResult<Result>(Asyntask2.this, result))
						.sendToTarget();
			}
		};
		thread = run;
		ThreadService.getInstance().executeThread(run);
	}

	private static class internalHandler extends Handler {
		@SuppressWarnings({"rawtypes", "unchecked"})
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			AsynTaskResult result = (AsynTaskResult) msg.obj;
			switch (msg.what) {
				case POST_FINSH :
					result.mTask.finsh(result.mData[0]);
					break;

				case POST_PROGRESS :
					result.mTask.onPostProgress(result.mData);
					break;
				case POST_START:
					result.mTask.doStart();
				default :
					break;
			}
		}
	}
}
