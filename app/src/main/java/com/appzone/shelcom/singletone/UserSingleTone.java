package com.appzone.shelcom.singletone;

import android.content.Context;

import com.appzone.shelcom.models.UserModel;
import com.appzone.shelcom.preferences.Preferences;

public class UserSingleTone {

    private static UserSingleTone instance = null;
    private UserModel userModel;
    private UserSingleTone() {
    }

    public static UserSingleTone getInstance()
    {
        if (instance ==null)
        {
            instance = new UserSingleTone();
        }
        return instance;
    }

    public static void setInstance(UserSingleTone instance) {
        UserSingleTone.instance = instance;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }
    public void clear(Context context)
    {
        this.userModel=null;
        Preferences preferences = Preferences.getInstance();
        preferences.Clear(context);
    }

}
