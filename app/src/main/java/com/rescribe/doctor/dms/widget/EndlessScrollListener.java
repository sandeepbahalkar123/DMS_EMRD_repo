package com.rescribe.doctor.dms.widget;

import android.widget.AbsListView;

public class EndlessScrollListener implements AbsListView.OnScrollListener {

	private int visibleThreshold = 5;
	private int currentOffset = 0;
	private int previousTotal = 0;
	private boolean loading = false;
	private ScrollListener listner;
	private int totalProducts = 0;
	private final int MAX_OFFSET = 10000;

	public boolean canLoad() {
		return loading;
	}

	public void setLoading(boolean loading) {
		this.loading = loading;
	}

	public EndlessScrollListener() {
	}

	public EndlessScrollListener(int visibleThreshold) {
		this.visibleThreshold = visibleThreshold;
	}

	public EndlessScrollListener(ScrollListener listener, int totalProducts) {
		this.listner = listener;
		this.totalProducts = totalProducts;
		loading = true;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

		if (loading) {
			if (totalItemCount > previousTotal) {
				loading = false;
				previousTotal = totalItemCount;

			}
		}

		if (currentOffset <= MAX_OFFSET && totalProducts > totalItemCount) {
			if (!loading && view.getLastVisiblePosition()+1==totalItemCount) {

				// I load the next page of gigs using a background task,
				// but you can call any function here.
				// new LoadGigsTask().execute(currentPage + 1);

				loading = true;
				if (listner != null) {
					listner.onScrollPositionChanged();
				}

			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE) {

		}
	}

	public int getCurrentOffset() {
		return currentOffset;
	}

	public void setCurrentOffset(int currentOffset) {
		this.currentOffset = currentOffset;
	}

	public static interface ScrollListener {
		public void onScrollPositionChanged();
	}
}