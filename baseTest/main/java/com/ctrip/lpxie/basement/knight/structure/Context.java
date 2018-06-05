package com.ctrip.lpxie.basement.knight.structure;

/**
 * Created by lpxie on 2016/7/19.
 */
public interface Context extends Container{
    public static final String ADD_WELCOME_FILE_EVENT = "addWelcomeFile";
    public static final String REMOVE_WELCOME_FILE_EVENT = "removeWelcomeFile";

    public static final String CHANGE_SESSION_ID_EVENT = "changeSessionId";


    public String getPath();

    public void setPath(String path);

    public String getWebappVersion();

    public String[] findWelcomeFiles();
}
