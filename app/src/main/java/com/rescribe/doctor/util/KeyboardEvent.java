package com.rescribe.doctor.util;

import android.animation.LayoutTransition;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

import com.rescribe.doctor.R;

public class KeyboardEvent {

    public KeyboardEvent(final RelativeLayout mainRelativeLayout, final KeyboardListener keyboardListener) {
        mainRelativeLayout.setLayoutTransition(new LayoutTransition());
        mainRelativeLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, final int newBottom, int oldLeft, int oldTop, int oldRight, final int oldBottom) {

                int difference = getDifference(oldBottom, newBottom);
                CommonMethods.Log("LAYOUT_DIFFERENCE", String.valueOf(difference));

                if (difference > mainRelativeLayout.getContext().getResources().getDimension(R.dimen.dp210)) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (oldBottom < newBottom)
                                keyboardListener.onKeyboardClose();
                            else keyboardListener.onKeyboardOpen();
                        }
                    }, 1);
                }
            }
        });
    }

    private int getDifference(int oldBottom, int newBottom) {
        return oldBottom > newBottom ? oldBottom - newBottom : newBottom - oldBottom;
    }

    public interface KeyboardListener {
        void onKeyboardOpen();
        void onKeyboardClose();
    }
}
