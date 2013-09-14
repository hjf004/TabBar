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
	private final TabBar mTabBar; // �Լ�
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

	public static final int POSITION_TOP = 1; // λ�ڶ������ߵײ�
	public static final int POSITION_BOTTOM = 2;

	public TabBar(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public TabBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		setWillNotDraw(false); // ��Ҫ!!!
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
			mTabWidth = mTabWidth / mTabCount; // ����ÿ��tab�Ŀ��
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
		
		//������Tab
		for (int i = 0; i < mTabCount; ++i) {
			LinearLayout tab = new LinearLayout(mContext);
			tab.setOrientation(LinearLayout.VERTICAL);
			tab.setPadding(0, 0, 0, 0);
			tab.setTag(i); // �����ڲ����
			tab.setClickable(true);
			ImageView imv = new ImageView(mContext);
			imv.setScaleType(ScaleType.CENTER);
			if (i < mIcons.length)
				imv.setImageDrawable(mIcons[i]);
			TextView tv = new TextView(mContext);
			tv.setGravity(Gravity.CENTER_HORIZONTAL);
			tv.setText(mTitles[i]);
			if (mIconAboveTitle) { // ͼ���ڱ���֮��
				tab.addView(imv, inParams);
				tab.addView(tv, inParams);
			} else { // ������ͼ��֮��
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
	 * ���õ�ǰTab�����
	 * 
	 * @param index
	 *            ����ָ����Tab�����
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
	 * ����TabBar�ڶ��˻��ǵ׶�.��ʵλ�������Activity�Ĳ����ļ������������������һ����ʶ�� ���������Ϣ��������һЩ�Զ����Ч��
	 * 
	 * @param i
	 *            ����TabBar.POSITION_TOP��׶�TabBar.POSITION_BOTTOM
	 */
	public void setTabBarPosition(int i) {
		mPosition = i;
	}

	/**
	 * �趨������R.java�ļ��İ�������Ϊ�ڽ���������Tab��iconʱҪ�õ��������Ĭ��ֵ������ָ��
	 * 
	 * @param name
	 *            R.java�ļ��İ���
	 */
	public void setResourcesPackageName(String name) {
		mResPackageName = name;
	}

	/**
	 * ����Tabѡ�к����ɫ��Ĭ��alphaΪ0x5f
	 * 
	 * @param c
	 *            rgb��ɫֵ
	 */
	public void setCurrentTabMaskColor(int rgb) {
		mCurrentTabMaskColor = rgb;
	}

	/**
	 * ����Tabѡ�к����ɫ.ΪʲôҪ������������أ���Ϊ�����Ǽǲ�סAlphaֵ0��255˭��ȫ͸���� ������Ը��ARGB��ɫ��A��RGB�ֿ����á���
	 * 
	 * @param rgb
	 *            rgb��ɫֵ
	 * @param a
	 *            alphaֵ
	 */
	public void setCurrentTabMaskColor(int rgb, int a) {
		mCurrentTabMaskColor = rgb;
		mCurrentTabMaskAlpha = a;
	}

	/**
	 * ��ȡTab����
	 * 
	 * @return Tab����
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
	 * �Ӳ����ļ�������ֵ�н�����������Դ
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
			mTitles = new CharSequence[0]; // ����Ϊnull
		}

		if (mIconNames == null) {
			mIconNames = new CharSequence[0]; // ����Ϊnull
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
