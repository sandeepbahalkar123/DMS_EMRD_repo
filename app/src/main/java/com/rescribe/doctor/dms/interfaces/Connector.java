package com.rescribe.doctor.dms.interfaces;

/**
 * @author Sandeep Bahalkar
 *
 */

import java.util.Map;

public interface Connector {

	void setPostParams(CustomResponse customResponse);

	void setPostParams(Map<String, String> postParams);

	void setHeaderParams(Map<String, String> headerParams);

	void connect();

	void parseJson(String data, boolean isTokenExpired);

	void abort();

	void setUrl(String url);

}
