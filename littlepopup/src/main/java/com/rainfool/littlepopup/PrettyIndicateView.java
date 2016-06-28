package com.rainfool.littlepopup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by rainfool on 16/6/28.
 */
public class PrettyIndicateView extends LinearLayout {

    Context mContext;

    View mAttachView;

    /**
     * 坐标
     */
    private int[] mLocation;

    TextView mTextView;

    ImageView mImageView;

    public PrettyIndicateView(Context context) {
        this(context, null);
    }

    public PrettyIndicateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PrettyIndicateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        inflater.inflate(R.layout.pretty_indicate_layout, this);

        mTextView = (TextView) findViewById(R.id.contentText);
        mImageView = (ImageView) findViewById(R.id.triangle);

        mLocation = new int[2];
        ViewTreeObserver viewTreeObserver = this.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //当View可以获取宽高的时候，设置view的位置
                relocation(mLocation);

            }
        });
    }

    private void relocation(int[] location) {
        float offX = getWidth() / 2;
        float offY = getHeight() / 2;

        mLocation[0] -= offX;
        mLocation[1] -= offY;


    }

    public PrettyIndicateView setText(String text) {
        mTextView.setText(text);
        return this;
    }

    public PrettyIndicateView setLocationByTargetView(View v) {
        v.getLocationInWindow(mLocation);
        return this;
    }

    public void show() {
        this.setLeft(mLocation[0]);
        this.setRight(mLocation[1]);

    }

}
