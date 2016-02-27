package com.hxgwx.www.view;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

public class CustomListViewForNoDataPage extends LinearLayout {

	public CustomListViewForNoDataPage(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CustomListViewForNoDataPage(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CustomListViewForNoDataPage(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	/**
	 * The listener that receives notifications when an item is clicked.
	 */
	OnItemClickListener mOnItemClickListener;

	/**
	 * Register a callback to be invoked when an item in this AdapterView has
	 * been clicked.
	 * 
	 * @param listener
	 *            The callback that will be invoked.
	 */
	public void setOnItemClickListener(OnItemClickListener listener) {
		mOnItemClickListener = listener;
	}

	/**
	 * Interface definition for a callback to be invoked when an item in this
	 * AdapterView has been clicked.
	 */
	public interface OnItemClickListener {

		/**
		 * Callback method to be invoked when an item in this AdapterView has
		 * been clicked.
		 * <p>
		 * Implementers can call getItemAtPosition(position) if they need to
		 * access the data associated with the selected item.
		 * 
		 * @param parent
		 *            The AdapterView where the click happened.
		 * @param view
		 *            The view within the AdapterView that was clicked (this
		 *            will be a view provided by the adapter)
		 * @param position
		 *            The position of the view in the adapter.
		 * @param id
		 *            The row id of the item that was clicked.
		 */
		void onItemClick(AdapterView<?> parent, View view, int position, long id);
	}

	ListAdapter adapter;
	/**
	 * @return the adapter
	 */
	public ListAdapter getAdapter() {
		return adapter;
	}
	View convertView;
	DataSetObserver observer;
	LinearLayout chilrdView;
	public synchronized void setAdapter(final ListAdapter adapter) {
		this.adapter=adapter;
		chilrdView = new LinearLayout(getContext());
		chilrdView.setOrientation(LinearLayout.HORIZONTAL);
		android.view.ViewGroup.LayoutParams params = new LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		chilrdView.setGravity(Gravity.CENTER);

		chilrdView.setLayoutParams(params);
		if (getChildCount()<1) {
			addView(chilrdView);
		}
		observer = new DataSetObserver() {
			@Override
			public void onChanged() {
				// TODO Auto-generated method stub
				super.onChanged();

				for (int i = 0; i < adapter.getCount(); i++) {

					View v = adapter.getView(i, convertView, null);
					if (mOnItemClickListener != null) {
						final int p = i;
						v.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								mOnItemClickListener.onItemClick(null, v, p,
										adapter.getItemId(p));
							}
						});
					}
					chilrdView.addView(v);
				}
			}
		};
		adapter.registerDataSetObserver(observer);

	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
		if (scrollListenear != null)
			scrollListenear.onScrollChanged2();
	}

	private ScrollChangeListenear scrollListenear;

	public interface ScrollChangeListenear {
		public void onScrollChanged2();
	}

	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		if (adapter != null && observer != null)
			adapter.unregisterDataSetObserver(observer);
		super.onDetachedFromWindow();
	}

	/**
	 * @param scrollListenear
	 *            the scrollListenear to set
	 */
	public void setScrollListenear(ScrollChangeListenear scrollListenear) {
		this.scrollListenear = scrollListenear;
	}

	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		if (sizeChange != null)
			sizeChange.onSizeChanged(w, h, oldw, oldh);
	}

	private SizeChange sizeChange;

	/**
	 * @param sizeChange
	 *            the sizeChange to set
	 */
	public void setSizeChange(SizeChange sizeChange) {
		this.sizeChange = sizeChange;
	}

	public interface SizeChange {
		void onSizeChanged(int w, int h, int oldw, int oldh);
	}
}
