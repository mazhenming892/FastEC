package com.cby.orange.net;

import android.content.Context;

import com.cby.orange.net.callback.IError;
import com.cby.orange.net.callback.IFailure;
import com.cby.orange.net.callback.IRequest;
import com.cby.orange.net.callback.ISuccess;
import com.cby.orange.ui.LoaderStyle;

import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Ma on 2017/11/28.
 */

public class RestClientBuilder {
    private String mUrl = null;
    private static final WeakHashMap<String,Object> PARAMS = RestCreator.getParams();
    private IRequest mIRequest = null;
    private ISuccess mISuccess = null;
    private IFailure mIFailure = null;
    private IError mIError = null;
    private RequestBody mBody = null;
    private Context mContext = null;
    private LoaderStyle mloaderStyle = null;

    RestClientBuilder() {

    }

    public final RestClientBuilder url(String url){
        this.mUrl = url;
        return this;
    }

    public final RestClientBuilder params(WeakHashMap<String,Object> params){
        PARAMS.putAll(params);
        return this;
    }

    public final RestClientBuilder params(String key, Object value){
        PARAMS.put(key,value);
        return this;
    }

    public final RestClientBuilder row(String row){
        this.mBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),row);
        return this;
    }

    public final RestClientBuilder onRequest(IRequest iRequest){
        this.mIRequest = iRequest;
        return this;
    }

    public final RestClientBuilder success(ISuccess iSuccess){
        this.mISuccess = iSuccess;
        return this;
    }

    public final RestClientBuilder failure(IFailure iFailure){
        this.mIFailure = iFailure;
        return this;
    }

    public final RestClientBuilder error(IError iError){
        this.mIError = iError;
        return this;
    }

    public final RestClientBuilder loader(Context context,LoaderStyle loaderStyle){
        this.mContext = context;
        this.mloaderStyle = loaderStyle;
        return this;
    }

    public final RestClientBuilder loader(Context context){
        this.mContext = context;
        this.mloaderStyle = LoaderStyle.BallGridPulseIndicator;
        return this;
    }
    public final RestClient build(){
        return new RestClient(mUrl,PARAMS,mIRequest,mISuccess,mIFailure,mIError,mBody,mloaderStyle,mContext);
    }

}