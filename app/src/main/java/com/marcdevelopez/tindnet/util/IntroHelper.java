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
        SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.prefs_file),
                Context.MODE_PRIVATE);
        boolean introShown = prefs.getBoolean(context.getString(R.string.intro_shown), false);
        String email = prefs.getString(context.getString(R.string.prefs_email), null);
        String provider = prefs.getString(context.getString(R.string.prefs_provider), null);
        boolean soyCliente = prefs.getBoolean(context.getString(R.string.soy_cliente), false);

        Intent intent;
        if (introShown) {
            // hay sesión iniciada
            if (!(email == null) && !(provider == null)) {
                // es cliente y lo envía al home de cliente
                if (soyCliente) {
                    //intent = new Intent(context, HomeClientActivity.class);
                } else { // abre el home empresa
                    // intent = new Intent(context, HomeCompanyActivity.class);
                }
            } else {
                // no hay sesión iniciada envía a loginregistro
                intent = new Intent(context, LoginRegisterActivity.class);
                // TODO: luego en loginRegister si va a resgistro enviar bundle para no comprovar
                //  introShown y no entrar en bucle
            }
        } else {
            // Si la pantalla de introducción no se ha mostrado, ir a la actividad de introducción
            intent = new Intent(context, IntroActivity.class);
        }

        // descomentar cuando esten creadas las actividades
        // Iniciar la actividad correspondiente y finalizar la actividad actual
        // context.startActivity(intent);
        // ((Activity) context).finish();


    }
}
