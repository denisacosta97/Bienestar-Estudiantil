package com.unse.bienestar.estudiantil.Vistas.Fragmentos;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Activities.Becas.InfoBecasActivity;
import com.unse.bienestar.estudiantil.Vistas.Activities.Becas.TipoTurnosActivity;
import com.unse.bienestar.estudiantil.Vistas.Activities.Perfil.InscripcionesActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import static android.content.Context.DOWNLOAD_SERVICE;

public class BecasFragment extends Fragment implements View.OnClickListener {

    View view;
    CardView cardInscr, cardInfo, cardWsp, cardWsp2, cardReglamento;
    Context mContext;

    public BecasFragment() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    CardView cardInsta;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_becas, container, false);

        loadViews();

        loadListener();

        return view;
    }

    private void loadListener() {
        cardInscr.setOnClickListener(this);
        cardInfo.setOnClickListener(this);
        cardInsta.setOnClickListener(this);
        cardWsp.setOnClickListener(this);
        cardWsp2.setOnClickListener(this);
        cardReglamento.setOnClickListener(this);
    }

    private void loadViews() {
        cardWsp = view.findViewById(R.id.btnWhats);
        cardWsp2 = view.findViewById(R.id.btnWhats2);
        cardInscr = view.findViewById(R.id.card_inscrip);
        cardInfo = view.findViewById(R.id.card_infoBecas);
        cardInsta = view.findViewById(R.id.btnInsta);
        cardReglamento = view.findViewById(R.id.cardReglamento);
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

    @Override
    public void onClick(View v) {
        PreferenceManager manager = new PreferenceManager(mContext);
        boolean isLogin = manager.getValue(Utils.IS_LOGIN);
        switch (v.getId()) {
            case R.id.btnWhats:
                openWhatsAppConversationUsingUri(mContext, "+5493855198292", "¡Hola Área Becas! Tengo una consulta: ");
                break;
            case R.id.btnWhats2:
                openWhatsAppConversationUsingUri(mContext, "+5493855198402", "¡Hola Área Becas! Tengo una consulta: ");
                break;
            case R.id.card_inscrip:
                startActivity(new Intent(getContext(), InscripcionesActivity.class));
                break;
            case R.id.card_infoBecas:
                startActivity(new Intent(getContext(), InfoBecasActivity.class));
                break;
            case R.id.btnInsta:
                String url = "https://www.instagram.com/becas_unse/";
                Intent openInsta = newInstagramProfileIntent(getActivity().getPackageManager(), url);
                startActivity(openInsta);
                break;
            case R.id.cardReglamento:
                downloadFile();
                break;
        }
    }

    private void downloadFile() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://bienestar.unse.edu.ar/becas/REGLAMENTO_BECAS_UNSE.pdf")));
    }

    public static void openWhatsAppConversationUsingUri(Context context, String numberWithCountryCode,
                                                        String message) {
        Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=" + numberWithCountryCode + "&text=" + message);
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(sendIntent);
    }

}
