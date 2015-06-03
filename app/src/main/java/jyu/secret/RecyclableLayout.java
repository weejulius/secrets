package jyu.secret;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by jyu on 15-5-28.
 */
public class RecyclableLayout extends RecyclerView {



    public RecyclableLayout(Context context) {
        super(context);
    }

    public RecyclableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    float dis = 0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
//
        return super.dispatchTouchEvent(event);
    }


}
