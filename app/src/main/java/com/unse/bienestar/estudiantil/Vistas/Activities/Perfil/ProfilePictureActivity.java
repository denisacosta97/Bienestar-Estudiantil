package com.unse.bienestar.estudiantil.Vistas.Activities.Perfil;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.FileStorageManager;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProfilePictureActivity extends AppCompatActivity {

    ImageView imgUser;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        loadViews();

        loadData();


    }

    private void loadData() {
        PreferenceManager manager = new PreferenceManager(getApplicationContext());
        final int id = manager.getValueInt(Utils.MY_ID);
        Bitmap bitmap = FileStorageManager.getBitmap(getApplicationContext(), Utils.FOLDER, String.format(Utils.PROFILE_PIC, id),
                false);
        if (bitmap != null) {
            Glide.with(imgUser.getContext()).load(bitmap).into(imgUser);
        }
        Utils.loadPicture(imgUser, id).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                mProgressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                mProgressBar.setVisibility(View.GONE);
                FileStorageManager.saveBitmap(getApplicationContext(), Utils.FOLDER,
                        String.format(Utils.PROFILE_PIC,
                                id),
                        ((BitmapDrawable) resource).getBitmap(), false);
                return false;
            }
        }).into(imgUser);

    }

    private void loadViews() {
        imgUser = findViewById(R.id.imgUser);
        mProgressBar = findViewById(R.id.progres);
    }

}
