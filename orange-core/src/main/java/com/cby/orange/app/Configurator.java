package com.cby.orange.app;

import android.app.Activity;

import com.joanzapata.iconify.IconFontDescriptor;
import com.joanzapata.iconify.Iconify;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Interceptor;

/**
 *
 * Created by Ma on 2017/11/22.
 */

public class Configurator {

    private static final HashMap<Object,Object> ORANGE_CONFIGS = new HashMap<>();
    private static final ArrayList<IconFontDescriptor> ICONS = new ArrayList<>();
    private static final ArrayList<Interceptor> INTERCEPTORS = new ArrayList<>();

    public Configurator() {

        ORANGE_CONFIGS.put(ConfigKeys.CONFIG_READY.name(),false);
    }

    public static Configurator getInstance(){
        return Holder.INSTANCE;
    }

    public final HashMap<Object,Object> getOrangeConfigs(){
        return ORANGE_CONFIGS;
    }

    private static class Holder{
        private static final Configurator INSTANCE = new Configurator();
    }

    public final void configure(){
        initIcons();
        initLogger();
        ORANGE_CONFIGS.put(ConfigKeys.CONFIG_READY.name(),true);
    }

    public final Configurator withApiHost(String host){
        ORANGE_CONFIGS.put(ConfigKeys.API_HOST.name(),host);
        return this;
    }

    private void checkConfiguration(){
        final boolean isReady = (boolean) ORANGE_CONFIGS.get(ConfigKeys.CONFIG_READY.name());
        if (!isReady){
            throw new RuntimeException("Configuration is not ready,call configure");
        }
    }


    @SuppressWarnings("unchecked")
    final <T> T getConfiguration(Object key){
        checkConfiguration();
        final Object value=ORANGE_CONFIGS.get(key);
        if(value==null){
            throw new NullPointerException(key.toString()+"IS NULL");
        }
        return (T) ORANGE_CONFIGS.get(key);
    }


    private void initIcons(){
        if (ICONS.size() > 0){
            final Iconify.IconifyInitializer initializer = Iconify.with(ICONS.get(0));
            for (int i = 1; i < ICONS.size(); i++) {
                initializer.with(ICONS.get(i));
            }
        }
    }

    private void initLogger(){
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public Configurator withIcon(IconFontDescriptor descriptor){
        ICONS.add(descriptor);
        return this;
    }

    public Configurator withInterceptor(Interceptor interceptor){
        INTERCEPTORS.add(interceptor);
        ORANGE_CONFIGS.put(ConfigKeys.INTERCEPTOR,INTERCEPTORS);
        return this;
    }

    public Configurator withInterceptors(ArrayList<Interceptor> interceptors){
        INTERCEPTORS.addAll(interceptors);
        ORANGE_CONFIGS.put(ConfigKeys.INTERCEPTOR,INTERCEPTORS);
        return this;
    }

    public Configurator withWeChatAppId(String weChatAppId){
        ORANGE_CONFIGS.put(ConfigKeys.WECHAT_APP_ID,weChatAppId);
        return this;
    }

    public Configurator withWeChatAppSecret(String weChatAppSecret){
        ORANGE_CONFIGS.put(ConfigKeys.WECHAT_APP_SECRET,weChatAppSecret);
        return this;
    }

    public Configurator withActivity(Activity activity){
        ORANGE_CONFIGS.put(ConfigKeys.ACTIVITY,activity);
        return this;
    }
}
