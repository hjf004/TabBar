package com.pupa.common.widget;

import com.pupa.common.util.StringHelper;
import com.pupa.TabBarDemo.R;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TabBar extends LinearLayout {
	private static final String TAG = "TabBar";
	private final TabBar mTabBar; // 自己
	private final Context mContext;
	private final AttributeSet mAttrs;
	private int mDefStyle;

	private Paint mPaint;
	private int mCurrentTabMaskColor;
	private int mCurrentTabMaskAlpha;

	private String mResPackageName;
	private CharSequence[] mTitles;
	private CharSequence[] mIconNames;
	private Drawable[] mIcons;
	private Drawable mSeperator;
	private int mSeperatorResId;
	private int mSeperatorWidth;

	private boolean mIconAboveTitle;

	private int mCurrentTabIndex;
	private int mTabCount;
	private int mTabWidth;
	private int mPosition;
	private OnCurrentTabChangedListener mTabChangedListener;

	public static final int POSITION_TOP = 1; // 位于顶部或者底部
	public static final int POSITION_BOTTOM = 2;

	public TabBar(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public TabBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		setWillNotDraw(false); // 重要!!!
		mContext = context;
		mAttrs = attrs;
		mCurrentTabIndex = -1;
		mTabCount = 0;
		mSeperatorWidth = 0;
		mPosition = POSITION_TOP;
		mCurrentTabMaskColor = Color.BLACK;
		mCurrentTabMaskAlpha = 0x5f;
		mPaint = new Paint();
		mTabBar = this;
		init();
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("deprecation")
	public void init() {
		getResourcesFromXml();
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.setPadding(0, 0, 0, 0);

		WindowManager wm = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		Display dp = wm.getDefaultDisplay();
		mTabWidth = dp.getWidth();

		mTabCount = mTitles.length;

		if (mTabCount > 0) {
			if (mSeperator != null) {
				Bitmap bmp = BitmapFactory.decodeResource(getResources(),
						mSeperatorResId);
				mSeperatorWidth = bmp.getWidth();
				mTabWidth = mTabWidth - (mTabCount - 1) * mSeperatorWidth;
				bmp.recycle();
				bmp = null;
			}
			mTabWidth = mTabWidth / mTabCount; // 计算每个tab的宽度
			mCurrentTabIndex = 0;
		}

		LayoutParams inParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		LayoutParams outParams = new LayoutParams(mTabWidth,
				LayoutParams.WRAP_CONTENT);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		View.OnClickListener clkListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int index = (Integer) v.getTag();
				if (index != mCurrentTabIndex) {
					if (mTabChangedListener != null)
						mTabChangedListener.onCurrentTabChanged(index);
					mCurrentTabIndex = index;
					mTabBar.invalidate();
				}
			}
		};
		
		//逐个添加Tab
		for (int i = 0; i < mTabCount; ++i) {
			LinearLayout tab = new LinearLayout(mContext);
			tab.setOrientation(LinearLayout.VERTICAL);
			tab.setPadding(0, 0, 0, 0);
			tab.setTag(i); // 设置内部标号
			tab.setClickable(true);
			ImageView imv = new ImageView(mContext);
			imv.setScaleType(ScaleType.CENTER);
			if (i < mIcons.length)
				imv.setImageDrawable(mIcons[i]);
			TextView tv = new TextView(mContext);
			tv.setGravity(Gravity.CENTER_HORIZONTAL);
			tv.setText(mTitles[i]);
			if (mIconAboveTitle) { // 图标在标题之上
				tab.addView(imv, inParams);
				tab.addView(tv, inParams);
			} else { // 标题在图标之上
				tab.addView(tv, inParams);
				tab.addView(imv, inParams);
			}
			tab.setOnClickListener(clkListener);
			this.addView(tab, outParams);
			if (mSeperator != null && i < mTabCount - 1) {
				ImageView sep = new ImageView(mContext);
				sep.setImageDrawable(mSeperator);
				this.addView(sep, params);
			}
		}
	}

	/**
	 * 设置当前Tab的序号
	 * 
	 * @param index
	 *            你想指定的Tab的序号
	 */
	public void setCurrentTab(int index) {
		if (index > -1 && index < mTabCount&&index!=mCurrentTabIndex) {
			mCurrentTabIndex = index;
			this.invalidate();
			if (mTabChangedListener != null)
				mTabChangedListener.onCurrentTabChanged(mCurrentTabIndex);
		}
	}

	public void setOnCurrentTabChangedListener(
			OnCurrentTabChangedListener listener) {
		mTabChangedListener = listener;
	}

	/**
	 * 设置TabBar在顶端还是底端.真实位置由你的Activity的布局文件决定，这里仅仅是作一个标识， 根据这个信息可以增加一些自定义的效果
	 * 
	 * @param i
	 *            顶端TabBar.POSITION_TOP或底端TabBar.POSITION_BOTTOM
	 */
	public void setTabBarPosition(int i) {
		mPosition = i;
	}

	/**
	 * 设定工程中R.java文件的包名，因为在解析出各个Tab的icon时要用到。如果是默认值则无需指定
	 * 
	 * @param name
	 *            R.java文件的包名
	 */
	public void setResourcesPackageName(String name) {
		mResPackageName = name;
	}

	/**
	 * 设置Tab选中后的颜色，默认alpha为0x5f
	 * 
	 * @param c
	 *            rgb颜色值
	 */
	public void setCurrentTabMaskColor(int rgb) {
		mCurrentTabMaskColor = rgb;
	}

	/**
	 * 设置Tab选中后的颜色.为什么要重载这个方法呢？因为我总是记不住Alpha值0和255谁是全透明， 于是宁愿把ARGB颜色中A跟RGB分开设置。。
	 * 
	 * @param rgb
	 *            rgb颜色值
	 * @param a
	 *            alpha值
	 */
	public void setCurrentTabMaskColor(int rgb, int a) {
		mCurrentTabMaskColor = rgb;
		mCurrentTabMaskAlpha = a;
	}

	/**
	 * 获取Tab个数
	 * 
	 * @return Tab个数
	 */
	public int getTabCount() {
		return mTabCount;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int h = this.getHeight();
		if (mCurrentTabIndex > -1 && mCurrentTabIndex < mTabCount) {
			int startX = (mTabWidth + mSeperatorWidth) * mCurrentTabIndex;
			mPaint.setColor(mCurrentTabMaskColor);
			mPaint.setAlpha(mCurrentTabMaskAlpha);
			mPaint.setStyle(Paint.Style.FILL);
			canvas.drawRect(new Rect(startX, 0, startX + mTabWidth, h), mPaint);
		}
	}

	/**
	 * 从布局文件的属性值中解析出各个资源
	 */
	private void getResourcesFromXml() {
		TypedArray ta = mContext.obtainStyledAttributes(mAttrs,
				R.styleable.TabBar, 0, 0);
		mIconNames = ta.getTextArray(R.styleable.TabBar_icons);
		mTitles = ta.getTextArray(R.styleable.TabBar_titles);
		mIconAboveTitle = ta
				.getBoolean(R.styleable.TabBar_IconAboveTitle, true);
		mSeperator = ta.getDrawable(R.styleable.TabBar_seperator);
		mSeperatorResId = ta.getResourceId(R.styleable.TabBar_seperator, -1);

		if (!StringHelper.notNullAndNotEmpty(mResPackageName))
			mResPackageName = mContext.getPackageName();

		if (mTitles == null) {
			mTitles = new CharSequence[0]; // 避免为null
		}

		if (mIconNames == null) {
			mIconNames = new CharSequence[0]; // 避免为null
		}

		Resources res = mContext.getResources();
		mIcons = new Drawable[mIconNames.length];
		for (int i = 0; i < mIconNames.length; ++i) {
			int id = res.getIdentifier(mIconNames[i].toString(), "drawable",
					mResPackageName);
			if (id != 0)
				mIcons[i] = res.getDrawable(id);
		}

		ta.recycle();
	}

	public interface OnCurrentTabChangedListener {
		public void onCurrentTabChanged(int index);
	}
}
