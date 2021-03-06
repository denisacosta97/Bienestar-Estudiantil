package com.unse.bienestar.estudiantil.Herramientas.PDF;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.unse.bienestar.estudiantil.Databases.UsuarioViewModel;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.FileStorageManager;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.Credencial.CustomPictureButton;
import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Interfaces.YesNoDialogListener;
import com.unse.bienestar.estudiantil.Modelos.Archivo;
import com.unse.bienestar.estudiantil.Modelos.Turno;
import com.unse.bienestar.estudiantil.Modelos.Usuario;
import com.unse.bienestar.estudiantil.Vistas.Dialogos.DialogoProcesamiento;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Map;

import androidx.fragment.app.FragmentManager;

public class LoadInfoPDF extends AsyncTask<String, Integer, String> {

    private Context mContext;
    private String directory_path;
    private File origen, destino;
    private Archivo mArchivo;
    private PdfDocument mPdfDocument;
    private boolean isYes = true;
    private YesNoDialogListener mListener;
    private DialogoProcesamiento mDialogoProcesamiento;
    private FragmentManager mFragmentManager;
    private Turno mTurno;

    public LoadInfoPDF(Context context, Archivo archivo, YesNoDialogListener listener, FragmentManager manager) {
        this.mContext = context;
        this.mArchivo = archivo;
        this.directory_path = Utils.getDirectoryPath(true, context);
        this.mListener = listener;
        this.mFragmentManager = manager;
    }

    @Override
    protected void onPreExecute() {
        mDialogoProcesamiento = new DialogoProcesamiento();
        mDialogoProcesamiento.show(mFragmentManager, "dialog_proces");
        origen = new File(directory_path + mArchivo.getNombreArchivo(true));
        destino = new File(directory_path + "old_" + mArchivo.getNombreArchivo(true));
        try {
            FileInputStream inputStream = new FileInputStream(origen);
            FileOutputStream outputStream = new FileOutputStream(destino);
            FileChannel inChanel = inputStream.getChannel();
            FileChannel outChanel = outputStream.getChannel();
            inChanel.transferTo(0, inChanel.size(), outChanel);
            inputStream.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            isYes = false;
        } catch (IOException e) {
            e.printStackTrace();
            isYes = false;
        }
        origen = new File(directory_path + "old_" + mArchivo.getNombreArchivo(true));
        destino = new File(directory_path + mArchivo.getNombreArchivo(true));
        try {
            mPdfDocument = new PdfDocument(new PdfReader(origen), new PdfWriter(destino));
        } catch (IOException e) {
            e.printStackTrace();
            isYes = false;
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        if (mPdfDocument != null) {
            PdfAcroForm form = PdfAcroForm.getAcroForm(mPdfDocument, true);

            if (form.getFormFields().size() > 0) {


                completeValues(form, form.getFormFields());


            } else {
                return "EMP";
            }


            return "OK";
        } else {
            return null;
        }

    }

    private void completeValues(PdfAcroForm form, Map<String, PdfFormField> formFields) {
        int id = new PreferenceManager(mContext).getValueInt(Utils.MY_ID);
        //Info user
        UsuarioViewModel usuarioViewModel = new UsuarioViewModel(mContext);
        Usuario usuario = usuarioViewModel.getById(id);
        if (usuario != null) {
            if (formFields.containsKey("dni"))
                formFields.get("dni").setValue(String.valueOf(usuario.getIdUsuario()));
            if (formFields.containsKey("nombreApe"))
                formFields.get("nombreApe").setValue(String.format("%s %s", usuario.getNombre(), usuario.getApellido()));
            if (formFields.containsKey("nombre"))
                formFields.get("nombre").setValue(usuario.getNombre());
            if (formFields.containsKey("apellido"))
                formFields.get("apellido").setValue(usuario.getApellido());
            if (formFields.containsKey("fechaNac"))
                formFields.get("fechaNac").setValue(usuario.getFechaNac());
            if (formFields.containsKey("pais"))
                formFields.get("pais").setValue(usuario.getPais());
            if (formFields.containsKey("foto")) {

                Bitmap bitmap = FileStorageManager.getBitmap(mContext, Utils.FOLDER,
                        String.format(Utils.PROFILE_PIC, id), false);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
               /* Image signatura;
                signatura = new Image(ImageDataFactory.create(stream.toByteArray()));
                signatura.setAutoScale(true);
                signatura.setFixedPosition(300,200);*/

                //PdfButtonFormField ad = (PdfButtonFormField) form.copyField("foto");
                Document document = new Document(mPdfDocument);


                //PdfFormField ad = form.copyField("foto");

                CustomPictureButton ad = new CustomPictureButton((PdfButtonFormField) formFields.get("foto"));
                ad.setImage(ImageDataFactory.create(stream.toByteArray()));
                formFields.remove("foto");
                document.add(new Paragraph().add(ad));
            }

        }
        if (mTurno != null){
            if (formFields.containsKey("receptor"))
                formFields.get("receptor").setValue(mTurno.getReceptorString());
            if (formFields.containsKey("fecha"))
                formFields.get("fecha").setValue(mTurno.getFecha());
            if (formFields.containsKey("hora"))
                formFields.get("hora").setValue(mTurno.getFechaInicio());
            if (formFields.containsKey("estado"))
                formFields.get("estado").setValue(mTurno.getEstado());
        }

    }

    public void setTurno(Turno turno) {
        mTurno = turno;
    }

    @Override
    protected void onPostExecute(String s) {
        mDialogoProcesamiento.dismiss();
        if (origen != null)
            origen.delete();
        if (mPdfDocument != null)
            mPdfDocument.close();
        if (s != null)
            if (isYes && (s.equals("OK") || s.equals("EMP")))
                mListener.yes();
            else
                mListener.no();
        else
            mListener.no();

    }
}
