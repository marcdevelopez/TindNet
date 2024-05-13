package com.marcdevelopez.tindnet.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.marcdevelopez.tindnet.IntroActivity;
import com.marcdevelopez.tindnet.LoginRegisterActivity;
import com.marcdevelopez.tindnet.R;

public class IntroHelper {
    public static void checkIntroShown(Context context) {
        // Obtener SharedPreferences
        SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.prefs_file), Context.MODE_PRIVATE);
        boolean introShown = prefs.getBoolean(context.getString(R.string.intro_shown), false);

        Intent intent;
        if (introShown) {
            // Si la pantalla de introducción ya se ha mostrado, ir a la actividad de login/registro
            intent = new Intent(context, LoginRegisterActivity.class);
        } else {
            // Si la pantalla de introducción no se ha mostrado, ir a la actividad de introducción
            intent = new Intent(context, IntroActivity.class);
        }

        // Iniciar la actividad correspondiente y finalizar la actividad actual
        context.startActivity(intent);
        ((Activity) context).finish();
    }
}
