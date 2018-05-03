package ng.max.slideview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class Slider extends android.support.v7.widget.AppCompatSeekBar {

    private Drawable thumb;
    private SlideView.OnSlideCompleteListener listener;
    private SlideView.OnActionUpListener onActionUpListener;
    private SlideView.OnActionDownListener onActionDownListener;
    private SlideView slideView;

    public Slider(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setThumb(Drawable thumb) {
        this.thumb = thumb;
        super.setThumb(thumb);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (onActionDownListener != null) onActionDownListener.OnActionDown(slideView);
            if (thumb.getBounds().contains((int) event.getX(), (int) event.getY())) {
                // This fixes an issue where the parent view (e.g ScrollView) receives
                // touch events along with the SlideView
                getParent().requestDisallowInterceptTouchEvent(true);
                super.onTouchEvent(event);
            } else {
                return false;
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getProgress() > 50) {
                if (listener != null) listener.onSlideComplete(slideView);
            }else {
                if (onActionUpListener != null) onActionUpListener.OnActionUp(slideView);
            }
            getParent().requestDisallowInterceptTouchEvent(false);
            setProgress(0);
        } else
            super.onTouchEvent(event);

        return true;
    }

    void setOnSlideCompleteListenerInternal(SlideView.OnSlideCompleteListener listener, SlideView slideView) {
        this.listener = listener;
        this.slideView = slideView;
    }

    public void setOnActionUpListener(SlideView.OnActionUpListener listener, SlideView slideView) {
        this.onActionUpListener = listener;
        this.slideView = slideView;
    }

    public void setOnActionDownListener(SlideView.OnActionDownListener listener, SlideView slideView) {
        this.onActionDownListener = listener;
        this.slideView = slideView;
    }

    @Override
    public Drawable getThumb() {
        // getThumb method was added in SDK16 but our minSDK is 14
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return super.getThumb();
        } else {
            return thumb;
        }
    }
}
