package com.moor.justin;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.moor.justin.AndroidSpecific.ActionResolver;

/**
 * Created by justi on 15-11-2017.
 */

public class ActionResolverAndroid implements ActionResolver{
    private Handler handler;
    private Context context;

    public ActionResolverAndroid(Context context) {
        handler = new Handler();
        this.context = context;
    }

    public void showToast(final CharSequence text) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                Gdx.app.log("DEBUG", " WORKS");
            }
        });
    }

}
