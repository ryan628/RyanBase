/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.handmark.pulltorefresh.extras.viewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import ryan.com.librarybase.R;

public class PullToRefreshMultiTouchViewPager extends PullToRefreshBase<MultiTouchViewPager> {

	public PullToRefreshMultiTouchViewPager(Context context) {
		super(context);
	}

	public PullToRefreshMultiTouchViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public final Orientation getPullToRefreshScrollDirection() {
		return Orientation.HORIZONTAL;
	}

	@Override
	protected MultiTouchViewPager createRefreshableView(Context context, AttributeSet attrs) {
		MultiTouchViewPager viewPager = new MultiTouchViewPager(context, attrs);
		viewPager.setId(R.id.viewpager);
		return viewPager;
	}

	@Override
	protected boolean isReadyForPullStart() {
		MultiTouchViewPager refreshableView = getRefreshableView();

		PagerAdapter adapter = refreshableView.getAdapter();
		if (null != adapter) {
			return refreshableView.getCurrentItem() == 0;
		}

		return false;
	}

	@Override
	protected boolean isReadyForPullEnd() {
		MultiTouchViewPager refreshableView = getRefreshableView();

		PagerAdapter adapter = refreshableView.getAdapter();
		if (null != adapter) {
			return refreshableView.getCurrentItem() == adapter.getCount() - 1;
		}

		return false;
	}
}
