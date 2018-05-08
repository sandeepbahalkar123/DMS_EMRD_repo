package com.scorg.dms.interfaces;


import com.scorg.dms.network.ConnectRequest;

/**
 * @author Sandeep Bahalkar
 */

public interface ConnectionListener extends ResponseResults {
    /**
     * @param responseResult There is two possible value of customResponse AUTH_FAILURE_ERROR & RESPONSE_OK
     *                       if request not reached at server we return AUTH_FAILURE_ERROR otherwise return RESPONSE_OK.
     * @param customResponse This is the customResponse object we get it from Server side.
     * @param mOldDataTag
     */
    public void onResponse(int responseResult, CustomResponse customResponse, String mOldDataTag);

    public void onTimeout(ConnectRequest request);
}
