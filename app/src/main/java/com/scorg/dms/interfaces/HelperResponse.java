package com.scorg.dms.interfaces;

/**
 * @author Sandeep Bahalkar
 */

public interface HelperResponse {

    public void onSuccess(String mOldDataTag, CustomResponse customResponse);

    public void onParseError(String mOldDataTag, String errorMessage);

    public void onServerError(String mOldDataTag, String serverErrorMessage);

    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage);

}
