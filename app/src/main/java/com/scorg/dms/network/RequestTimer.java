package com.scorg.dms.network;
/**
 * @author Sandeep Bahalkar
 */

import android.os.Handler;
import android.os.HandlerThread;

public class RequestTimer {
    public static final long DEFAULT_TIMEOUT = 1000 * 40; //40 seconds for timeout
    private static final String TAG = "RequestTimer";
    /* Public attributes */
    private long timeoutMillis;
    private RequestTimerListener listener;
    private Handler handlerMain;
    /* Local attributes */
    private HandlerThread threadTimer;
    private Handler handlerTimer;
    private Runnable runnableTimeout;
    public RequestTimer(long timeoutMillis, Handler handlerMain) {
        this.setTimeoutMillis(timeoutMillis);
        threadTimer = new HandlerThread("timer");
        threadTimer.start();
        handlerTimer = new Handler(threadTimer.getLooper());
    }

    public RequestTimer() {
        this(DEFAULT_TIMEOUT, null);
    }

    public long getTimeoutMillis() {
        return timeoutMillis;
    }

    public void setTimeoutMillis(long timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    public RequestTimerListener getListener() {
        return listener;
    }

    public void setListener(RequestTimerListener listener) {
        this.listener = listener;
    }

    public Handler getCallbackHandler() {
        return handlerMain;
    }

    public void setCallbackHandlerMain(Handler handlerMain) {
        this.handlerMain = handlerMain;
    }

    public void start() {
        if (runnableTimeout == null) {

            runnableTimeout = new Runnable() {
                @Override
                public void run() {
                    if (handlerMain == null) {
                        getListener().onTimeout(RequestTimer.this);
                    } else {
                        Runnable runnablePostback = new Runnable() {
                            @Override
                            public void run() {
                                getListener().onTimeout(RequestTimer.this);
                            }
                        };

                        handlerMain.post(runnablePostback);
                    }
                    clean();
                }
            };

            handlerTimer.postDelayed(runnableTimeout, getTimeoutMillis());
        }
    }

    public void cancel() {
        handlerTimer.removeCallbacks(runnableTimeout);
        clean();
    }

    private void clean() {
        if (runnableTimeout != null) {
            handlerTimer.removeCallbacks(runnableTimeout);
            runnableTimeout = null;
        }
        threadTimer.quit();
    }

    public interface RequestTimerListener {
        public void onTimeout(RequestTimer requestTimer);
    }
}