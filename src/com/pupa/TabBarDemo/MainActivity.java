package com.pupa.TabBarDemo;

import java.util.ArrayList;
import java.util.List;

import com.pupa.TabBarDemo.R;
import com.pupa.common.widget.TabBar;

import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.support.v4.view.ViewPager;
import android.support.v4.view.PagerAdapter;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";

	private TabBar tabBar;

	private ViewPager viewPager;
	private PagerAdapter pagerAdapter;
	private LayoutInflater layoutInflater;
	private List<View> pages;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		pages = new ArrayList<View>();
		initWidgets();
		initAdapters();
		initListeners();
	}

	private void initWidgets() {
		tabBar = (TabBar) findViewById(R.id.MainTabBar);
		viewPager = (ViewPager) findViewById(R.id.MainViewPager);

		layoutInflater = LayoutInflater.from(this);
		View tab1= layoutInflater.inflate(R.layout.tab1, null);
		View tab2 = layoutInflater.inflate(R.layout.tab2, null);
		View tab3=layoutInflater.inflate(R.layout.tab3, null);
		pages.add(tab1);
		pages.add(tab2);
		pages.add(tab3);
	}

	private void initAdapters() {
		pagerAdapter = new PagerAdapter() {

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return pages.size();
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				// TODO Auto-generated method stub
				return arg0 == arg1;
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				((ViewPager) container).addView(pages.get(position));
				return pages.get(position);
			}

			@Override
			public void destroyItem(ViewGroup container, int pos, Object obj) {
				((ViewPager) container).removeView(pages.get(pos));
			}
		};

		viewPager.setAdapter(pagerAdapter);
	}

	private void initListeners() {
		TabBar.OnCurrentTabChangedListener tabListener = new TabBar.OnCurrentTabChangedListener() {

			@Override
			public void onCurrentTabChanged(int index) {
				// TODO Auto-generated method stub
				if (index > -1 && index < viewPager.getAdapter().getCount()
						&& index != viewPager.getCurrentItem()) {
					viewPager.setCurrentItem(index, true);
				}
			}
		};

		ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				tabBar.setCurrentTab(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		};

		tabBar.setOnCurrentTabChangedListener(tabListener);
		viewPager.setOnPageChangeListener(pageListener);
	}
}
