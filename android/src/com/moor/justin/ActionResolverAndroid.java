package com.moor.justin;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.moor.justin.AndroidSpecific.ActionResolver;

/**
 * Created by justi on 15-11-2017.
 */

//Daadwerkelijke Android klasse voor Android specific code, in dit geval dus de toast.
public class ActionResolverAndroid implements ActionResolver{
    private Handler handler;
    private Context context;

    //Context wordt meegegeven (dus het spel)
    public ActionResolverAndroid(Context context) {
        handler = new Handler();
        this.context = context;
    }

    // Laat het toast bericht zien met gegeven tekst
    public void showToast(final CharSequence text) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
