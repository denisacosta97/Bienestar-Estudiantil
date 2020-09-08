package com.unse.bienestar.estudiantil.Vistas.Activities.Inicio;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.R;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

public class LoginWelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    Button mLogin, mInvitado;
    TextView txtWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
        boolean isLogin = preferenceManager.getValue(Utils.IS_LOGIN);
        if(isLogin){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }else {

            setContentView(R.layout.activity_login_welcome);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            loadViews();

            loadListener();

            changeTextWelcome();
        }

    }

    private void loadListener() {
        mLogin.setOnClickListener(this);
        mInvitado.setOnClickListener(this);
    }

    private void loadViews() {
        mLogin = findViewById(R.id.inisesion);
        mInvitado = findViewById(R.id.visit);
        txtWelcome = findViewById(R.id.txtWelcome);
    }

    public void changeTextWelcome() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 6 && timeOfDay <= 12) {
            txtWelcome.setText("¡Buen día!");
        } else if (timeOfDay >= 13 && timeOfDay <= 19) {
            txtWelcome.setText("¡Buenas tardes!");
        } else if (timeOfDay >= 20)
            txtWelcome.setText("¡Buenas noches!");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.visit:
                PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
                preferenceManager.setValue(Utils.IS_VISIT, true);
                startActivity(new Intent(LoginWelcomeActivity.this, MainActivity.class));
                break;
            case R.id.inisesion:
                startActivity(new Intent(LoginWelcomeActivity.this, LoginActivity.class));
                break;
        }

    }
}
