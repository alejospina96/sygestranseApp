package com.sistemasivhorsnet.tutransporte.classes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.sistemasivhorsnet.tutransporte.MainActivity;
import com.sistemasivhorsnet.tutransporte.R;


/**
 * Created by Pedro on 19/05/2017.
 */

public class Session {
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public Session(Context context){
        this.context = context;
        sharedPreferences = this.context.getSharedPreferences(this.context.getString(R.string.archiveSP), Context.MODE_PRIVATE);
    }
    public void logout(){
        editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.usernameSP), "");
        editor.putString(context.getString(R.string.rolSP), "");
        editor.putString(context.getString(R.string.asignacionSP),"");
        editor.commit();
        context.startActivity(new Intent(context, MainActivity.class));
    }
    public void changeSchool(){
        editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.schoolSP), "");
        editor.putString(context.getString(R.string.hostnameSP), "");
        editor.commit();
        logout();
    }

    public boolean started(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.loginSP), Context.MODE_PRIVATE);
        boolean has = sharedPreferences.getString(context.getString(R.string.rolSP),"") !="";
        return has;
    }
}
