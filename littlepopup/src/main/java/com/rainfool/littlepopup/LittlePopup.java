package com.rainfool.littlepopup;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Created by rainfool on 16/6/27.
 */
public class LittlePopup{

    private static final int POPUP_WINDOW_OFFSET_Y = 70;
    private static final int POPUP_WINDOW_PADDING_LEFT_RIGHT = 8 * 2;

    Context mContext;

    /**
     * popup附着的view
     */
    View mAttachView;

    /**
     * 坐标
     */
    private int[] mLocation;

    /*
     *指示条本身
     */
    private PopupWindow popupWindow;

    /**
     * 提示内容
     */
    TextView content;

    View popupView;

    float mWidth,mHeight = 66;

    public LittlePopup(Context context,View attachView) {
        this.mContext = context;
        this.mAttachView = attachView;
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        popupView = inflater.inflate(R.layout.popup_layout,null);
        content = (TextView) popupView.findViewById(R.id.text);
        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(popupView);
        this.setLocation(new int[]{0,0});
    }

    public LittlePopup setLocation(int[] mLocation) {
        this.mLocation = mLocation;
        return this;
    }

    public LittlePopup setLocationByTargetView(View v) {
        int[] location = new int[2];
        v.getLocationInWindow(location);

        location[0] -= (mWidth / 2 + 16 - v.getWidth() / 2);
        location[1] -= mHeight;
        this.mLocation = location;
        return this;
    }

    public LittlePopup setText(String text) {
        content.setText(text);
        mWidth = content.getPaint().measureText(text);
        return this;
    }

    public LittlePopup show(){
        if (popupWindow != null) {
            Log.d("test", "x:" + mLocation[0] + "|y:" + mLocation[1]);
            popupWindow.showAtLocation(mAttachView, Gravity.NO_GRAVITY, mLocation[0], mLocation[1]);
        }
        return this;
    }

    public void dismiss() {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }
}
