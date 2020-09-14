package com.unse.bienestar.estudiantil.Vistas.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.unse.bienestar.estudiantil.BuildConfig;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imgIcono;
    TextView txtVersion;
    CardView cardInsta, cardFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        loadViews();

        loadListener();

        loadData();

        setToolbar();

    }

    private void loadData() {
        txtVersion.setText(String.valueOf(BuildConfig.VERSION_NAME));
    }

    private void loadListener() {
        imgIcono.setOnClickListener(this);
        cardInsta.setOnClickListener(this);
        cardFace.setOnClickListener(this);
    }

    private void setToolbar() {
        ((TextView) findViewById(R.id.txtTitulo)).setText("Sobre nosotros");
        Utils.changeColorDrawable(((ImageView) findViewById(R.id.imgFlecha)), getApplicationContext(), R.color.colorAccent);
    }

    private void loadViews() {
        txtVersion = findViewById(R.id.txtVersion);
        imgIcono = findViewById(R.id.imgFlecha);
        cardInsta = findViewById(R.id.cardInsta);
        cardFace = findViewById(R.id.cardFace);
    }

    public static Intent newInstagramProfileIntent(PackageManager pm, String url) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        //final String username = url.substring(url.lastIndexOf("/") + 1);
        intent.setData(Uri.parse(url));
        //intent.setPackage("com.instagram.android");
        /*try {
            if (pm.getPackageInfo("com.instagram.android", 0) != null) {
                if (url.endsWith("/")) {
                    url = url.substring(0, url.length() - 1);
                }

            }
        } catch (PackageManager.NameNotFoundException ignored) {

        }*/
        //intent.setData(Uri.parse(url));
        return intent;
    }

    public static Intent newFacebookProfileIntent(PackageManager pm, String url) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        //final String username = url.substring(url.lastIndexOf("/") + 1);
        intent.setData(Uri.parse(url));
        //intent.setPackage("com.instagram.android");
        /*try {
            if (pm.getPackageInfo("com.instagram.android", 0) != null) {
                if (url.endsWith("/")) {
                    url = url.substring(0, url.length() - 1);
                }

            }
        } catch (PackageManager.NameNotFoundException ignored) {

        }*/
        //intent.setData(Uri.parse(url));
        return intent;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgFlecha:
                onBackPressed();
                break;
            case R.id.cardInsta:
                String url = "https://www.instagram.com/bienestarestudiantilunse/";
                Intent openInsta = newInstagramProfileIntent(getPackageManager(), url);
                startActivity(openInsta);
                break;
            case R.id.cardFace:
                String url1 = "https://www.facebook.com/bienestarunse";
                Intent open = newFacebookProfileIntent(getPackageManager(), url1);
                startActivity(open);
                break;

        }
    }


}
