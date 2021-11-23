package com.example.gestionvuelos.util;

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

public class Util {
    public static boolean isEmail(EditText text){
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
    public static boolean isPass(EditText text) {
        String pass = text.getText().toString();
        return (!TextUtils.isEmpty(pass));
    }
}
