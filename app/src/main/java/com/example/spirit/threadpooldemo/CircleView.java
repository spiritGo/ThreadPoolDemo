package com.example.spirit.threadpooldemo;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class CircleView extends LinearLayout {
    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).setTag(i);
            getChildAt(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int item = (int) v.getTag();
                    int i1 = childCount - item;
                    if (v.isSelected()) {
                        for (int i = childCount - 1; i >= childCount - i1; i--) {
                            getChildAt(i).setSelected(false);
                        }
                    } else {
                        for (int i = 0; i <= item; i++) {
                            getChildAt(i).setSelected(true);
                        }
                    }
                }
            });
        }
    }
}
