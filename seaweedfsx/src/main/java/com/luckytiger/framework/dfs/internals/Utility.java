package com.luckytiger.framework.dfs.internals;

import com.luckytiger.framework.dfs.internals.Exception.HttpException;
import org.asynchttpclient.Response;
import org.asynchttpclient.uri.Uri;

import java.net.URI;
import java.net.URISyntaxException;

public class Utility {
    public static void raiseForStatus(Response response) throws HttpException {
        String reason = String.format("statusText:%s,responseBody:%s", response.getStatusText(), response.getResponseBody());
        String errormsg;
        if (response.getStatusCode() >= 400 && response.getStatusCode() < 500) {
            errormsg = String.format("%s Client Error: %s for url: %s", response.getStatusCode(), reason, response.getUri().toString());
            throw new HttpException(errormsg);
        }
        if (response.getStatusCode() >= 500 && response.getStatusCode() < 600) {
            errormsg = String.format("%s Server Error: %s for url: %s", response.getStatusCode(), reason, response.getUri().toString());
            throw new HttpException(errormsg);
        }
    }
}
