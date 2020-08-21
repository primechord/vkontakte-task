package com.atdroid.atyurin.futuremoney.custom_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.viewpager.widget.PagerTabStrip;

import com.atdroid.atyurin.futuremoney.R;

/**
 * Created by atdroid on 11.11.2015.
 */
public class MyPagerTabStrip extends PagerTabStrip
{
    public MyPagerTabStrip(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        final TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.MyPagerTabStrip);
        setTabIndicatorColor(a.getColor(
                R.styleable.MyPagerTabStrip_indicatorColor, Color.BLUE));
        a.recycle();
    }

}
