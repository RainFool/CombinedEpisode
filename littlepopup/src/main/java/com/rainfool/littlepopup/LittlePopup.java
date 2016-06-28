package com.rainfool.littlepopup;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Created by rainfool on 16/6/27.
 */
public class LittlePopup {

    private static final int POPUP_WINDOW_OFFSET_Y = 80;
    private static final int POPUP_WINDOW_PADDING_LEFT_RIGHT = 8 * 2;
    private static final int POPUP_TRIANGLE_WIDTH_HEIGHT = 24;

    private static int mScreenWidth = 1080;

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

    ImageView triangle;

    View popupView;

    float mWidth;

    public LittlePopup(Context context, View attachView) {
        this.mContext = context;
        this.mAttachView = attachView;
        init(context);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point screenPoint = new Point();
        windowManager.getDefaultDisplay().getSize(screenPoint);
        mScreenWidth = screenPoint.x;
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        popupView = inflater.inflate(R.layout.popup_layout, null);
        content = (TextView) popupView.findViewById(R.id.text);
        triangle = (ImageView) popupView.findViewById(R.id.indicate);
        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(popupView);
        this.setLocation(new int[]{0, 0});
    }

    public LittlePopup setLocation(int[] mLocation) {
        this.mLocation = mLocation;
        return this;
    }

    public LittlePopup setLocationByTargetView(View v) {
        int[] location = new int[2];
        v.getLocationInWindow(location);

        location[0] -= (mWidth / 2 + POPUP_WINDOW_PADDING_LEFT_RIGHT - v.getWidth() / 2);
        location[1] -= POPUP_WINDOW_OFFSET_Y;

        this.mLocation = location;
        return this;
    }

    public LittlePopup setText(String text) {
        content.setText(text);
        mWidth = content.getPaint().measureText(text);
        return this;
    }

    public LittlePopup show() {
        if (popupWindow != null) {
            Log.d("test", "x:" + mLocation[0] + "|y:" + mLocation[1]);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, POPUP_TRIANGLE_WIDTH_HEIGHT);
            if (mLocation[0] < 0) {
                Log.d("test", "left:" + ((mWidth + POPUP_WINDOW_PADDING_LEFT_RIGHT) / 2 + mLocation[0]));
                params.setMargins(
                        (int) ((mWidth + POPUP_WINDOW_PADDING_LEFT_RIGHT - POPUP_TRIANGLE_WIDTH_HEIGHT) / 2 + mLocation[0])
                        , 0, 0, 0);
            } else if (mLocation[0] + mWidth + POPUP_WINDOW_PADDING_LEFT_RIGHT > mScreenWidth) {
                Log.d("test", "screen width:" + mScreenWidth + "|left:" + ((mWidth + POPUP_WINDOW_PADDING_LEFT_RIGHT) / 2 - mScreenWidth));
                params.setMargins(
                        (int) ((mLocation[0] + mWidth + POPUP_WINDOW_PADDING_LEFT_RIGHT - mScreenWidth)
                                + (mWidth + POPUP_WINDOW_PADDING_LEFT_RIGHT - POPUP_TRIANGLE_WIDTH_HEIGHT) / 2)
                        , 0, 0, 0);
            } else {
                params.gravity = Gravity.CENTER;
            }
            triangle.setLayoutParams(params);
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
