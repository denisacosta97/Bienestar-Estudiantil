package com.unse.bienestar.estudiantil.Herramientas;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.model.LatLng;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.unse.bienestar.estudiantil.Databases.AlumnoViewModel;
import com.unse.bienestar.estudiantil.Databases.BDGestor;
import com.unse.bienestar.estudiantil.Databases.DBManager;
import com.unse.bienestar.estudiantil.Databases.EgresadoViewModel;
import com.unse.bienestar.estudiantil.Databases.ProfesorViewModel;
import com.unse.bienestar.estudiantil.Databases.RolViewModel;
import com.unse.bienestar.estudiantil.Databases.UsuarioViewModel;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.FileStorageManager;
import com.unse.bienestar.estudiantil.Modelos.Archivo;
import com.unse.bienestar.estudiantil.Modelos.Noticia;
import com.unse.bienestar.estudiantil.R;
import com.unse.bienestar.estudiantil.Vistas.Activities.Becas.SubirDocumentacionFamiliarActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.DrawableCompat;

public class Utils {

    //Constantes para las BD
    public static final String STRING_TYPE = "text";
    public static final String INT_TYPE = "integer";
    public static final String FLOAT_TYPE = "float";
    public static final String NULL_TYPE = "not null";
    public static final String NULL_YES_TYPE = "null";
    public static final String AUTO_INCREMENT = "auto_increment";
    //Constantes de preferencias
    public static final String IS_FIRST_TIME_LAUNCH = "is_first";
    public static final String IS_LOGIN = "login_yes";
    public static final String MY_ID = "my_id_user";
    public static final String FECHA_PASS = "fecha_pass";
    public static final String TOKEN = "my_token";
    public static final String IS_VISIT = "visit";
    //Constantes para activities
    public static final String IS_EDIT_MODE = "edit_mode";
    public static final String USER_INFO = "user_info";
    public static final String DEPORTE_NAME = "dato_deporte";
    public static final String DEPORTE_TEMPORADA = "temporada_deporte";
    public static final String DEPORTE_ID = "id_deporte";
    public static final String TIPO_CREDENCIAL = "tipo_cred";
    public static final String TIPO_CREDENCIAL_DATO = "tipo_cred_dato";
    public static final String CREDENCIAL = "credencial";
    public static final String URI_IMAGE = "uri_image";
    public static final String DEPORTE_NAME_PROF = "dato_deporte_prof";
    public static final String IS_ADMIN_MODE = "is_admin_mode";
    public static final String NAME_GENERAL = "name_general";
    public static final String ANIO = "anio_temp";
    public static final String ROLES = "roles_all";
    public static final String ROLES_USER = "roles_user";
    public static final String ASISTENCIA = "asistencia";
    public static final String FECHA = "fecha";
    public static final String LINEA_NAME = "linea_name";
    public static final String TIPO_REGISTRO = "tipo_acdeso";
    public static final String LIST_REGULARIDAD = "lista_regularidad";
    public static final String ARCHIVO_NAME = "archivo_name";
    public static final String LIST_HIJOS = "list_hijos";
    public static final String LIST_SUSCROP = "list_sus";
    public static final String LIST_CRED = "list_cred";
    public static final String RECORRIDO = "recorrido";
    public static final String SERVICIO = "servicio_info";
    public static final String PUNTO = "punto_info";
    public static final String COLECTIVO = "cole_info";
    public static final String NOTICIA_INFO = "noticia_info";
    public static final String PASAJERO = "pasajeros";
    public static final String DATA_MEDICAMENTO = "medicamento";
    public static final String DATA_OPCION = "opcion";
    public static final String DATA_FAMILIAR = "data_familiar";

    public static final String IMPRESION = "impresion";
    public static final String CANCHA = "cancha";
    //Constantes para activities
    public static final long UPDATE_INTERVAL = 12000;
    public static final int PICK_IMAGE = 9090;
    public static final int EDIT_IMAGE = 9091;
    public static final int PERMISSION_ALL = 9092;
    public static final int GET_FROM_DNI = 9093;
    public static final int SELECT_FILE = 9094;
    public static final int REQUEST_GROUP_PERMISSIONS_LOCATION = 9095;
    public static final int REQUEST_LOCATION = 9096;
    public static final int REQUEST_CHECK_SETTINGS = 9097;
    public static final int GET_FROM_GALLERY = 9096;
    public static final int UPLOAD_DOC = 9098;
    public static final int UPDATE_POST = 9099;
    //Constantes para tipos de usuario
    public static final int TIPO_USUARIO = 1;
    public static final int TIPO_ESTUDIANTE = 2;
    public static final int TIPO_ROLES = 10;
    public static final int TIPO_SOCIO = 11;
    public static final int TIPO_CHOFER = 12;
    //Constantes para busqueda
    public static final String PATRON_LEGAJO = "[0-9]{1,5}(-|/)[0-9]{2,4}";
    public static final String PATRON_DNI = "([0-9]){5,8}";
    public static final String PATRON_NOMBRES = "[a-zA-Z_ ]+";
    public static final String PATRON_NUMEROS = "[0-9]+";
    //Constante para tipo de usuario
    public static final int TIPO_ALUMNO = 1;
    public static final int TIPO_PROFESOR = 2;
    public static final int TIPO_NODOCENTE = 3;
    public static final int TIPO_EGRESADO = 4;
    public static final int TIPO_PARTICULAR = 5;
    //Constante para Volley
    public static final int MY_DEFAULT_TIMEOUT = 15000;
    //Constante de nombres de archivos
    public static final String PROFILE_PIC = "%s.jpg";
    public static final String FILE_EXT = "%s";
    //Constantes de permisos
    public static final int[] LIST_PERMISOS = new int[]{999, 998};

    public static final int NOTICIA_NORMAL = 3030;
    public static final int NOTICIA_BUTTON_WEB = 3131;
    public static final int NOTICIA_BUTTON_TIENDA = 3132;
    public static final int NOTICIA_BUTTON_APP = 3133;

    public static final int TIPO_COMEDOR = 1;
    public static final int TIPO_DEPORTE = 2;
    public static final int TIPO_TRANSPORTE = 3;
    public static final int TIPO_BECA = 4;
    public static final int TIPO_RESIDENCIA = 5;
    public static final int TIPO_CYBER = 6;
    public static final int TIPO_UPA = 7;

    public static final int TIPO_CANCHA = 1010;
    public static final int TIPO_QUINCHO = 1011;

    public static final String TYPE_RANGE = "type_range";

    public static final String MONSERRAT = "Montserrat-Regular.ttf";
    public static final String MONTSERRAT_BOLD = "Montserrat-Black.ttf";

    public static final String BECA_NAME = "dato_deporte";
    public static final String INFO_EXTRA = "extra_uno";
    public static final String INFO_EXTRA_2 = "extra_dos";
    public static final String SERVICIO_NAME = "servicio_deporte";
    public static final String BARCODE = "dato_barcode";
    public static final String TORNEO = "dato_torneo";
    public static final String NOTICIA = "dato_noticias";
    public static final String RESERVA = "dato_reserva";
    public static final String DATA_RESERVA = "dato_reserva";
    public static final String DATA_TURNO = "dato_turno";
    public static final String DATA_FECHA = "dato_fecha";
    public static final String DATA_CONVOCATORIA = "dato_convocatoria";
    public static final String ALUMNO_NAME = "dato_alumno";
    public static final String USER_NAME = "dato_user";
    public static final String NUM_INST = "num_inst";
    public static final int LIST_RESET = 0;
    public static final int LIST_LEGAJO = 1;
    public static final int LIST_DNI = 2;
    public static final int LIST_NOMBRE = 3;
    public static final String INSCRIPCION_ID = "inscripcion_id";
    public static final String TIPO_ARCHIVOS = "tipo_archivos";
    public static final String TIPO_FAMILIA = "tipo_familia";
    public static final String INSCRIPCION_ANIO = "anio_id";
    public static final String TOKEN_FIREBASE = "token_firebase";
    public static final String DOCTOR = "doctor_info";
    public static final String DATA_INTEG = "dato_integ";
    public static final String DATA_DOCUM = "dato_docum";


    private static final String IP = "sis.bienestar.unse.edu.ar/api";
    //USUARIO
    public static final String URL_USUARIO_INSERTAR = "https://" + IP + "/usuario/insertar.php";
    public static final String URL_USUARIO_ACTUALIZAR = "https://" + IP + "/usuario/actualizar.php";
    public static final String URL_USUARIO_LOGIN = "https://" + IP + "/usuario/login.php";
    public static final String URL_USUARIO_LOGIN_DATA = "https://" + IP + "/usuario/loginInfo.php";
    public static final String URL_USUARIO_IMAGE = "https://" + IP + "/general/uploadImage.php";
    public static final String URL_USUARIO_IMAGE_LOAD = "https://" + IP + "/usuariosImg/";
    public static final String URL_REC_CONTRASENIA = "https://" + IP + "/mail/restablecer.php";
    public static final String URL_USUARIOS_LISTA = "https://" + IP + "/usuario/getUsuarios.php";
    public static final String URL_USUARIO_BY_ID = "https://" + IP + "/usuario/getUser.php";
    public static final String URL_USUARIO_ELIMINAR = "https://" + IP + "/usuario/eliminar.php";
    public static final String URL_USUARIO_BY_ID_REDUCE = "https://" + IP + "/usuario/getUserReduce.php";
    public static final String URL_USUARIO_DIRECCION = "https://" + IP + "/usuario/getDireccion.php";
    //ALUMNO
    public static final String URL_REGULARIDAD = "https://" + IP + "/usuario/insertarRegularidad.php";
    public static final String URL_REGULARIDAD_CAMBIAR = "https://" + IP + "/usuario/cambiarRegularidad.php";
    //SOCIO
    public static final String URL_SOCIO_CREDENCIAL = "https://" + IP + "/socio/getCredencial.php";
    public static final String URL_SOCIO_LISTA = "https://" + IP + "/socio/getSocios.php";
    public static final String URL_SOCIO_PARTIULAR = "https://" + IP + "/socio/getSocio.php";
    public static final String URL_SOCIO_FAMILIAR_AGREGAR = "https://" + IP + "/socio/insertarFamiliar.php";
    public static final String URL_SOCIO_FAMILIAR_ACTUALIZAR = "https://" + IP + "/socio/actualizarFamiliar.php";
    public static final String URL_CREDENCIAL_SOCIO_CAMBIAR = "https://" + IP + "/socio/cambiarCredencial.php";
    public static final String URL_SOCIO_SUSCRIPCION = "https://" + IP + "/socio/getSuscripcion.php";
    public static final String URL_SOCIO_SUSCRIPCION_AGREGAR = "https://" + IP + "/socio/insertarInscripcion.php";
    public static final String URL_SOCIO_SUSCRIPCION_CAMBIAR = "https://" + IP + "/socio/actualizarSuscripcion.php";
    //ROLES
    public static final String URL_ROLES_LISTA = "https://" + IP + "/general/getRoles.php";
    public static final String URL_ROLES_INSERTAR = "https://" + IP + "/general/insertarRol.php";
    public static final String URL_ROLES_USER_LISTA = "https://" + IP + "/general/getRolesByUsuario.php";
    //DEPORTES
    public static final String URL_DEPORTE_LISTA = "https://" + IP + "/deportes/getDeportes.php";
    public static final String URL_DEPORTE_TEMPORADA = "https://" + IP + "/deportes/inscripcion/getTemporada.php";
    public static final String URL_DEPORTE_INSCRIPCION = "https://" + IP + "/deportes/inscripcion/insertar.php";
    public static final String URL_INSCRIPCIONES_GENERALES = "https://" + IP + "/becas/inscripcion/getInscripciones.php";
    public static final String URL_INSCRIPCION_BY_ID = "https://" + IP + "/deportes/inscripcion/getInscripcion.php";
    public static final String URL_INSCRIPCION_ACTUALIZAR = "https://" + IP + "/deportes/inscripcion/actualizar.php";
    public static final String URL_INSCRIPCIONES_POR_DEPORTE = "https://" + IP + "/deportes/inscripcion/getInscriptosByDeporte.php";
    public static final String URL_INSCRIPCIONES_TEMPORADA_DEPORTE = "https://" + IP + "/deportes/inscripcion/getTemporadaByEntrenador.php";
    public static final String URL_INSCRIPCION_PARTICULAR_ELIMIAR = "https://" + IP + "/deportes/inscripcion/eliminar.php";
    public static final String URL_INSCRIPCION_AGREGAR_CREDENCIAL = "https://" + IP + "/deportes/credencial/insertar.php";
    public static final String URL_CREDENCIAL_CAMBIAR = "https://" + IP + "/deportes/credencial/eliminar.php";
    public static final String URL_CREDENCIAL_DEPORTE = "https://" + IP + "/deportes/credencial/getCredencialByUser.php";
    public static final String URL_DEPORTE_TEMPORADAS = "https://" + IP + "/deportes/getTemporadas.php";
    public static final String URL_DEPORTE_BAJA = "https://" + IP + "/deportes/actualizarDeporte.php";
    public static final String URL_PROFES = "https://" + IP + "/deportes/getAllProfesores.php";
    public static final String URL_BECADOS = "https://" + IP + "/beca/getAllBecados.php";
    public static final String URL_ASISTENCIA = "https://" + IP + "/deportes/asistencia.php";

    //POLIDEPORTIVO
    public static final String URL_INGRESO_POLI = "http://" + IP + "/bienestar/polideportivo/pileta/ingresoPoli.php";
    public static final String URL_INGRESO_PILE = "http://" + IP + "/bienestar/polideportivo/pileta/ingresoPileta.php";
    public static final String URL_INGRESO_TEMPORADA = "http://" + IP + "/bienestar/polideportivo/pileta/getIngresosByTemporada.php";
    public static final String URL_INGRESO_EMPLEADO = "http://" + IP + "/bienestar/polideportivo/pileta/getIngresosByEmpleado.php";
    public static final String URL_RESERVAS_ESPACIOS_FECHA = "http://" + IP + "/bienestar/polideportivo/espacio/getReservas.php";
    public static final String URL_RESERVA_ESPACIOS = "http://" + IP + "/bienestar/polideportivo/espacio/getReserva.php";
    public static final String URL_RESERVAS_ESPACIOS_ID = "http://" + IP + "/bienestar/polideportivo/espacio/getReservaByUser.php";


    //TORNEOS
    public static final String URL_TORNEO_CREDENCIAL = "https://" + IP + "/deportes/torneo/getCredencial.php";
    public static final String URL_TORNEOS_LISTA = "https://" + IP + "/deportes/torneo/getAllTorneos.php";
    public static final String URL_TORNEOS_ACTUALIZAR = "https://" + IP + "/deportes/torneo/actualizar.php";
    public static final String URL_TORNEOS_BAJA = "https://" + IP + "/deportes/torneo/borrar.php";
    public static final String URL_TORNEOS_INSERTAR = "https://" + IP + "/deportes/torneo/insertar.php";

    //BECAS
    public static final String URL_BECAS_CREDENCIAL = "https://" + IP + "/becas/beca/getCredencial.php";
    public static final String URL_BECAS_CONVOCATORIA = "https://" + IP + "/becas/convocatoria/getConvocatoriasTurnos.php";
    public static final String URL_BECAS_DISPONIBILIDAD = "https://" + IP + "/becas/convocatoria/getConvocatoria.php";
    public static final String URL_BECAS_INSCRIPCION = "https://" + IP + "/becas/inscripcion/getInscripcion.php";
    public static final String URL_BECAS_HORARIO = "https://" + IP + "/becas/turno/horarios.json";
    public static final String URL_TURNO_HORARIO = "https://" + IP + "/becas/turno/getTurnoHorarios.php";
    public static final String URL_TURNO_NUEVO = "https://" + IP + "/becas/turno/insertar.php";
    public static final String URL_TURNO_POR_USUARIO = "https://" + IP + "/becas/turno/getTurnoByUsuario.php";
    public static final String URL_TURNO_CANCELAR = "https://" + IP + "/becas/turno/cancelar.php";
    public static final String URL_ARCHIVO_TURNO = "https://" + IP + "/becas/turno/archivos/";
    public static final String URL_BECAS_INSCRIPCION_DOC = "https://" + IP + "/becas/inscripcion/insertar.php";
    public static final String URL_BECAS_INSCRIPCION_ACTUALIZAR = "https://" + IP + "/becas/inscripcion/actualizar.php";
    public static final String URL_BECAS_DOCUMENTACION = "https://" + IP + "/becas/inscripcion/documentacion/uploadPDF.php";
    public static final String URL_BECAS_DOCUMENTACION_ELIMINAR = "https://" + IP + "/becas/inscripcion/documentacion/deletePDF.php";
    public static final String URL_INSERT_FAMILIAR = "https://" + IP + "/becas/inscripcion/insertarFamiliar.php";

    //CIBER
    public static final String URL_REGISTRAR_INGRESO = "https://" + IP + "/ciber/maquina/insertar.php";
    //GENERALES
    public static final String URL_ARCHIVOS_LISTA = "https://" + IP + "/general/getArchivos.php";
    public static final String URL_ARCHIVOS = "https://" + IP + "/archivos/";
    public static final String URL_UPLOAD_FILE = "https://" + IP + "/general/uploadFile.php";
    public static final String URL_GENERATE_PDF = "https://" + IP + "/archivos/generate.php";
    public static final String URL_FECHAS_VALIDA = "https://" + IP + "/becas/fecha/getFechaInvalidate.php";
    public static final String URL_PDF_TURNO = "https://" + IP + "/archivos/";
    public static final String URL_CONFIG = "https://" + IP + "/general/config/getValue.php";


    //TRANSPORTE
    public static final String URL_LINEAS = "https://" + IP + "/transporte/getAllLineas.php";
    public static final String URL_LINEAS_UPDATE = "https://" + IP + "/trasporte/actualizar.php";
    public static final String URL_LINEAS_ADD = "https://" + IP + "/transporte/insertar.php";

    public static final String URL_ACTUALIZAR_CHOFER = "https://" + IP + "/transporte/chofer/actualizarChofer.php";
    public static final String URL_ELIMINAR_CHOFER = "https://" + IP + "/transporte/chofer/eliminarChofer.php";
    //public static final String URL_ACTUALIZAR_TEMPORADA = "https://" + IP + "/transporte/chofer/actualizarTemporada.php";
    public static final String URL_GET_CHOFER = "https://" + IP + "/transporte/chofer/getChofer.php";
    public static final String URL_GET_CHOFERES = "https://" + IP + "/transporte/chofer/getChoferes.php";
    public static final String URL_SERVICIOS_CHOFER = "https://" + IP + "/transporte/chofer/getServiciosByChofer.php";
    public static final String URL_INSERTAR_CHOFER = "https://" + IP + "/transporte/chofer/insertarChofer.php";
    //public static final String URL_TEMPORADA_CHOFER = "https://" + IP + "/transporte/chofer/registrarTemporada.php";

    public static final String URL_ACTUALIZAR_COLECTIVO = "https://" + IP + "/transporte/colectivo/actualizarColectivo.php";
    public static final String URL_ELIMINAR_COLECTIVO = "https://" + IP + "/transporte/colectivo/eliminarColectivo.php";
    public static final String URL_GET_COLECTIVO = "https://" + IP + "/transporte/colectivo/getColectivo.php";
    public static final String URL_GET_COLECTIVOS = "https://" + IP + "/transporte/colectivocolectivo/getColectivos.php";
    public static final String URL_INSERTAR_COLECTIVO = "https://" + IP + "/transporte/colectivo/insertarColectivo.php";

    public static final String URL_ELIMINAR_RECORRIDO = "https://" + IP + "/transporte/recorrido/eliminarRecorrido.php";
    public static final String URL_GET_RECORRIDO = "https://" + IP + "/transporte/recorrido/getRecorrido.php";
    public static final String URL_GET_RECORRIDOS = "https://bienestar.unse.edu.ar/transporte/recorrido/getRecorridos.php";
    public static final String URL_RECORRIDOS = "https://" + IP + "/transporte/recorrido/getRecorridos.php";
    public static final String URL_RECORRIDOS_DATOS = "https://" + IP + "/transporte/recorrido/recorridos.json";

    public static final String URL_FINALIZAR_SERVICIO = "https://" + IP + "/transporte/servicio/finalizarServicio.php";
    public static final String URL_ULTIMO_SERVICIO = "https://" + IP + "/transporte/servicio/getLastPoint.php";
    public static final String URL_GET_SERVICIO = "https://" + IP + "/transporte/servicio/getServicio.php";
    public static final String URL_SERVICIO_ALUMNOS = "https://" + IP + "/transporte/servicio/getServiciosAlumno.php";
    public static final String URL_SERVICIO_CHOFER = "https://" + IP + "/transporte/servicio/getServiciosChofer.php";
    public static final String URL_INICIAR_SERVICIO = "https://" + IP + "/transporte/servicio/insertarServicio.php";
    public static final String URL_PASAJERO_REGISTRAR_SERVICIO = "https://" + IP + "/transporte/servicio/registrarPasajero.php";
    public static final String URL_ACTUALIZAR_SERVICIO = "https://" + IP + "/transporte/servicio/updatePosition.php";
    public static final String URL_PASAJERO_INFO_SERVICIO = "https://" + IP + "/transporte/servicio/getPasajeroServicio.php";
    public static final String URL_SERVICIOS_BY_FECHA = "https://" + IP + "/transporte/servicio/getServicios.php";

    //NOTICIAS
    public static final String URL_IMAGE_NOTICIA = "https://sis.bienestar.unse.edu.ar/api/general/noticia/img/%s";
    public static final String URL_NOTICIA_BY_ID = "https://sis.bienestar.unse.edu.ar/api/general/noticia/get.php";
    public static final String URL_LISTA_NOTICIA = "https://sis.bienestar.unse.edu.ar/api/general/noticia/getNoticias.php";
    public static final String URL_AGREGAR_NOTICIA = "https://sis.bienestar.unse.edu.ar/api/general/noticia/insertar.php";
    public static final String URL_ELIMINAR_NOTICIA = "https://sis.bienestar.unse.edu.ar/api/general/noticia/eliminar.php";
    public static final String URL_ACTUALIZAR_NOTICIA = "https://sis.bienestar.unse.edu.ar/api/general/noticia/actualizar.php";
    public static final String URL_NOTICIA_IMAGE = "https://sis.bienestar.unse.edu.ar/api/general/noticia/uploadImage.php";


    //UAPU
    public static final String URL_MEDICAM_INSERT = "https://" + IP + "/uapu/medicamento/insertar.php";
    public static final String URL_MEDICAM_DAY = "https://" + IP + "/uapu/medicamento/getByDay.php";
    public static final String URL_MEDICAM_USER = "https://" + IP + "/uapu/medicamento/getByUser.php";
    public static final String URL_MEDICAM_CANCELAR = "https://" + IP + "/uapu/medicamento/actualizar.php";
    public static final String URL_TURNO_UAPU_HORARIO = "https://" + IP + "/uapu/turno/horarios.json";
    public static final String URL_UAPU_HORARIO = "https://" + IP + "/uapu/turno/getTurnoHorarios.php";
    public static final String URL_TURNO_UAPU_NUEVO = "https://" + IP + "/uapu/turno/insertar.php";
    public static final String URL_SERVICIOS = "https://" + IP + "/uapu/servicio/getServicios.php";
    public static final String URL_HORARIOS_MEDICAM = "https://" + IP + "/uapu/medicamento/horarios.json";
    public static final String URL_TUNO_HORARIO_MEDICAM = "https://" + IP + "/uapu/medicamento/getTurnoHorarios.php";
    public static final String URL_TURNO_UPA_CANCELAR = "https://" + IP + "/uapu/turno/cancelar.php";
    public static final long SECONS_TIMER = 15000;

    //PUNTOS DE CONECTIVIDAD
    public static final String URL_PC_TURNO = "https://" + IP + "/general/conectividad/insertar.php";
    public static final String URL_PC_HORARIOS = "https://" + IP + "/general/conectividad/horarios.json";
    public static final String URL_PC_TURNOSHORARIOS = "https://" + IP + "/general/conectividad/getTurnoHorarios.php";
    public static final String URL_PC_CANCELAR = "https://" + IP + "/general/conectividad/cancelar.php";
    public static final String URL_PC_ZONAS = "https://" + IP + "/general/conectividad/getLugares.php";

    //IMAGENES
    public static final String URL_IMAGEN_INICIO = "https://" + IP + "/deportes/img_unse2.jpg";
    public static final String URL_IMAGEN_CANCHA = "https://" + IP + "/deportes/cancha.jpg";
    public static final String URL_IMAGEN_SUM = "https://" + IP + "/deportes/sum.jpg";
    public static final String URL_IMAGEN_REC_SI = "https://" + IP + "/deportes/rec_si.jpg";
    public static final String URL_IMAGEN_REC_NO = "https://" + IP + "/deportes/rec_no.jpg";

    //MARATÓN
    public static final String URL_INSCRIPCION_MARATON = "https://" + IP + "/deportes/carrera/insertar.php";
    public static final String URL_INSCRIPCION_CHECK = "https://" + IP + "/deportes/carrera/check.php";
    public static final String URL_IMAGEN_LOGO = "https://" + IP + "/deportes/carrera/logo.png";

    //CARPETAS
    public static final String FOLDER = "BIENESTAR_ESTUDIANTIL/";
    public static final String FOLDER_CREDENCIALES = FOLDER + "CREDENCIALES/";

    public static String[] med = {"Clínica Médica", "Salud Sexual y Reproductiva"};
    public static String[] puntosConect = {"Ciber UNSE", "Parque Industrial", "Residencia (El Zanjón)"};
    public static String[] catMaraton = {"3", "10"};
    public static String[] rangoEdad = {"Menor de 18 años", "19 a 24", "25 a 29", "30 a 34", "35 a 39", "40 a 44", "45 a 49", "50 a 54", "55 a 59", "60 o mas"};
    public static String[] facultad = {"FAyA", "FCEyT", "FCF", "FCM", "FHCSyS"};
    public static String[] faya = {"Ingeniería Agronómica", "Ingeniería en Alimentos", "Licenciatura en Biotecnología",
            "Licenciatura en Química", "Profesorado en Química", "Tecnicatura en Apicultura"};
    public static String[] fceyt = {"Ingeniería Civil", "Ingeniería Electromecánica", "Ingeniería Electrónica",
            "Ingeniería Eléctrica", "Ingeniería en Agrimensura", "Ingeniería Hidráulica",
            "Ingeniería Industrial", "Ingeniería Vial", "Licenciatura en Hidrología Subterránea",
            "Licenciatura en Matemática", "Licenciatura en Sistemas de Información",
            "Profesorado en Física", "Profesorado en Informática", "Profesorado en Matemática",
            "Programador Universitario en Informática", "Tecnicatura Universitaria Vial",
            "Tecnicatura Universitaria en Construcciones",
            "Tecnicatura Universitaria en Hidrología Subterránea",
            "Tecnicatura Universitaria en Organización y Control de la Producción"};
    public static String[] fcf = {"Ingeniería Forestal", "Ingeniería en Industrias Forestales",
            "Licenciatura en Ecología y Conservación del Ambiente",
            "Licenciatura en Biología",
            "Tecnicatura Universitaria Fitosanitarista",
            "Tecnicatura Universitaria en Viveros y Plantaciones Forestales",
            "Tecnicatura Universitaria en Aserraderos y Carpintería Industrial"};

    public static String[] fcm = {"Medicina"};

    public static String[] fhcys = {"Licenciatura en Administración", "Contador Público Nacional",
            "Licenciatura en Letras", "Licenciatura en Sociología", "Licenciatura en Enfermería",
            "Licenciatura en Educación para la Salud", "Obstetricia", "Licenciatura en Obstetricia",
            "Licenciatura en Filosofía", "Licenciatura en Trabajo Social",
            "Licenciatura en Periodismo", "Profesorado en Educación para la Salud",
            "Tecnicatura Sup. Adm. y Gestión Universitaria",
            "Tecnicatura en Educación Intercultural Bilingue"};

    public static String[] categorias = {"Seleccione una opción...", "Alumno", "Profesor", "Nodocente", "Egresado",
            "Particular", "Afiliado", "Jubilado", "Otro"};

    public static String[] horaCanchasDía = {"10:00", "11:00", "12:00", "13:00", "14:00", "15:00",
            "16:00", "17:00", "18:00"};

    public static String[] horaCanchasNoche = {"19:00", "20:00", "21:00", "22:00", "23:00", "00:00",
            "01:00", "02:00"};

    public static String[] canchas = {"Fútbol 11", "Fútbol 8", "Fútbol 5", "Futsal"};

    public static String dataAlumno = "?idU=%s&nom=%s&ape=%s&fechan=%s&pais=%s&prov=%s&local=%s" +
            "&dom=%s&sex=%s&car=%s&fac=%s&anio=%s&leg=%s" +
            "&tipo=%s&mail=%s&tel=%s&barr=%s&fecham=%s&idReg=%s";

    public static String dataProfesor = "?idU=%s&nom=%s&ape=%s&fechan=%s&pais=%s&prov=%s&local=%s" +
            "&dom=%s&sex=%s&tipo=%s&mail=%s&tel=%s" +
            "&prof=%s&fechain=%s&barr=%s&fecham=%s";

    public static String dataEgresado = "?idU=%s&nom=%s&ape=%s&fechan=%s&pais=%s&prov=%s&local=%s" +
            "&dom=%s&sex=%s&tipo=%s&mail=%s&tel=%s" +
            "&prof=%s&fechaeg=%s&barr=%s&fecham=%s";

    public static String dataPartiNoDoc = "?idU=%s&nom=%s&ape=%s&fechan=%s&pais=%s&prov=%s&local=%s" +
            "&dom=%s&sex=%s&tipo=%s&mail=%s&tel=%s&barr=%s&fecham=%s";

    /**
     * Método que permite inicializar la BD local
     *
     * @param c Contexto actual de la actividad desde donde de lo convoca
     */
    public static void initBD(Context c) {
        BDGestor gestor = new BDGestor(c);
        DBManager.initializeInstance(gestor);
    }


    public static void changeColorDrawable(ImageView view, Context context, int color) {
        DrawableCompat.setTint(
                DrawableCompat.wrap(view.getDrawable()),
                ContextCompat.getColor(context, color));
    }

    public static String getStringCamel(String string) {
        Pattern pattern = Pattern.compile("[A-Za-z]+");
        Matcher matcher = pattern.matcher(string);
        StringBuilder resp = new StringBuilder();
        while (matcher.find()) {
            if (matcher.group(0).length() > 1)
                resp.append(matcher.group(0).charAt(0)).append(matcher.group(0).substring(1).toLowerCase()).append(" ");
            else
                resp.append(matcher.group(0));
        }
        return resp.toString();
    }


    public static void showToast(Context c, String msj) {
        Toast.makeText(c, msj, Toast.LENGTH_LONG).show();
    }

    public static void showLog(String title, String msj) {
        Log.e(title, msj);
    }

    public static Bitmap resize(Bitmap bitmapToScale, float newWidth, float newHeight) {
        if (bitmapToScale == null)
            return null;
        int width = bitmapToScale.getWidth();
        int height = bitmapToScale.getHeight();

        Matrix matrix = new Matrix();

        matrix.postScale(newWidth / width, newHeight / height);

        return Bitmap.createBitmap(bitmapToScale, 0, 0, bitmapToScale.getWidth(), bitmapToScale.getHeight(), matrix, true);
    }

    public static void showCustomToast(Activity activity, Context context, String text, int icon) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) activity.findViewById(R.id.toast_layout));

        ImageView image = layout.findViewById(R.id.image);
        image.setImageResource(icon);
        TextView text2 = layout.findViewById(R.id.text);
        text2.setText(text);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM, 0, 30);
        toast.setDuration(Toast.LENGTH_LONG + 4);
        toast.setView(layout);
        toast.show();
    }


    //Metodo para saber si un permiso esta autorizado o no
    public static boolean isPermissionGranted(Context ctx, String permision) {
        int permission = ActivityCompat.checkSelfPermission(
                ctx,
                permision);
        return permission == PackageManager.PERMISSION_GRANTED;

    }

    public static String getTwoDecimals(double value) {
        DecimalFormat df = new DecimalFormat("#.#");
        return df.format(value);
    }

    public static int getColorIMC(String imc) {
        double auxImc = Double.parseDouble(imc);
//        String aux = getTwoDecimals(iMC);
//        double auxImc = Double.parseDouble(aux);
        if (auxImc <= 15) {
            return R.color.colorBrown;
        } else if (auxImc > 15 && auxImc <= 15.9) {
            return R.color.colorLightBlue;
        } else if (auxImc >= 16 && auxImc <= 18.4) {
            return R.color.colorBlue;
        } else if (auxImc >= 18.5 && auxImc <= 24.9) {
            return R.color.colorGreen;
        } else if (auxImc >= 25 && auxImc <= 29.9) {
            return R.color.colorOrange;
        } else if (auxImc >= 30 && auxImc <= 34.9) {
            return R.color.colorRed;
        } else if (auxImc >= 35 && auxImc <= 39.9) {
            return R.color.colorPrimaryDark;
        } else if (auxImc >= 40) {
            return R.color.colorPrimaryDark;
        }

        return R.color.colorRed;
    }

    public static String obtainEstado(String imc) {
        double auxImc = Double.parseDouble(imc);
//        String aux = getTwoDecimals(iMC);
//        double auxImc = Double.parseDouble(aux);
        String estado = " ";
        if (auxImc <= 15) {
            estado = "Delgadez muy severa";
        } else if (auxImc > 15 && auxImc <= 15.9) {
            estado = "Delgadez severa";
        } else if (auxImc >= 16 && auxImc <= 18.4) {
            estado = "Delgadez";
        } else if (auxImc >= 18.5 && auxImc <= 24.9) {
            estado = "Peso saludable";
        } else if (auxImc >= 25 && auxImc <= 29.9) {
            estado = "Sobrepeso";
        } else if (auxImc >= 30 && auxImc <= 34.9) {
            estado = "Obesidad moderada";
        } else if (auxImc >= 35 && auxImc <= 39.9) {
            estado = "Obesidad severa";
        } else if (auxImc >= 40) {
            estado = "Obesidad mórbida";
        }

        return estado;
    }

    public static String obtainIMC(String peso, String altura) {
        String imc = "DESCONOCIDO", aux = "DESCONOCIDO";
        double auximc = 0;
        if (!peso.equals(" ") && !altura.equals(" ")) {
            double pso = Double.parseDouble(peso);
            double alt = Double.parseDouble(altura);
            alt = alt / 100;
            auximc = pso / (alt * alt);
            aux = getTwoDecimals(auximc);
        }
        return aux;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static String getAppName(Context context, ComponentName name) {
        try {
            ActivityInfo activityInfo = context.getPackageManager().getActivityInfo(
                    name, PackageManager.GET_META_DATA);
            return activityInfo.loadLabel(context.getPackageManager()).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setFont(Context context, ViewGroup view, String font) {
        FontChangeUtil fontChanger = new FontChangeUtil(context.getAssets(), font);
        fontChanger.replaceFonts(view);
    }

    private static void crearArchivoProvisorio(InputStream inputStream, File file) {

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            int read;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String getDirectoryPath(boolean ext, Context context) {
        String directory_path = (ext ? Environment.getExternalStorageDirectory().getPath() : context.getCacheDir()) + "/BIENESTAR_ESTUDIANTIL/";
        File directorio = new File(directory_path);
        if (!directorio.exists())
            directorio.mkdirs();
        return directory_path;
    }

    public static Object[] exist(Archivo archivo, Context context) {
        File file = new File(getDirectoryPath(true, context) + archivo.getNombreArchivo(true));
        Object[] a = new Object[2];
        a[0] = file.exists();
        a[1] = file.exists() ? file.lastModified() : 0;

        return a;
    }

    public static void createPDF(Context context) {
        File file = null;
        Document document = null;
        AssetManager assetManager = context.getAssets();
        InputStream in = null;
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/BIENESTAR/";
        try {
            File directorio = new File(directory_path);
            if (!directorio.exists())
                directorio.mkdirs();
            in = assetManager.open("ficha_deportes_label.pdf");
            file = new File(directory_path, "deportesProvisorio.pdf");
            crearArchivoProvisorio(in, file);

            File src = new File(directory_path, "deportesProvisorio.pdf");
            File des = new File(directory_path, "ficha_deportes.pdf");
            if (!src.exists()) {
                try {
                    InputStream is = context.getAssets().open("ficha_deportes_label.pdf");
                    byte[] buffer = new byte[1024];
                    is.read(buffer);
                    is.close();
                    FileOutputStream fos = new FileOutputStream(src);
                    fos.write(buffer);
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            PdfDocument pdfDocument = new PdfDocument(new PdfReader(src), new PdfWriter(des));
            PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDocument, true);
            form.getField("DNI").setValue("4000000");
            pdfDocument.close();
            src.delete();
            //document = new Document(pdfDocument);
            //document.close();
            /*document.add(getText("SYSTOCK", 15, true));
            document.add(getText("Sistema de Gestión de Mercadería, Facturación y Gestión de Clientes", 12, true));
            document.add(getText("----------------------------------------------------------------------------------------------------------------------", 11, true));
            document.add(getText("Datos del Pedido", 12, false));
            document.close();*/

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void createPDF(Context context, String name) {
        File file = null;
        Document document = null;
        AssetManager assetManager = context.getAssets();
        InputStream in = null;
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/BIENESTAR/";
        try {
            File directorio = new File(directory_path);
            if (!directorio.exists())
                directorio.mkdirs();
            in = assetManager.open(name);
            file = new File(directory_path, "prov_" + name);
            crearArchivoProvisorio(in, file);

            File src = new File(directory_path, "prov_" + name);
            String names = name.substring(0, name.length() - 4);
            names = names + "_" + getHoraWithSeconds(new Date(System.currentTimeMillis())) + ".pdf";
            File des = new File(directory_path, names);
            if (!src.exists()) {
                try {
                    InputStream is = context.getAssets().open(name);
                    byte[] buffer = new byte[1024];
                    is.read(buffer);
                    is.close();
                    FileOutputStream fos = new FileOutputStream(src);
                    fos.write(buffer);
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            PdfDocument pdfDocument = new PdfDocument(new PdfReader(src), new PdfWriter(des));
            PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDocument, true);
            form.getField("DNI").setValue("4000000");
            pdfDocument.close();
            src.delete();
            //document = new Document(pdfDocument);
            //document.close();
            /*document.add(getText("SYSTOCK", 15, true));
            document.add(getText("Sistema de Gestión de Mercadería, Facturación y Gestión de Clientes", 12, true));
            document.add(getText("----------------------------------------------------------------------------------------------------------------------", 11, true));
            document.add(getText("Datos del Pedido", 12, false));
            document.close();*/

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static Paragraph getText(String text, float size, boolean center) {
        return center ? new Paragraph(text).setFontSize(size).setTextAlignment(TextAlignment.CENTER)
                : new Paragraph(text).setFontSize(size);
    }

    public static String getText(String text) {
        StringBuilder stringBuilder = new StringBuilder();
        text = text.replaceAll("!=", "\n");
        text = text.replaceAll("=!", "\t");
        stringBuilder.append(text);
        return stringBuilder.toString();
    }

    public static String styleText(String text) {
        StringBuilder stringBuilder = new StringBuilder();
        text = text.replaceAll("\n", "!=");
        text = text.replaceAll("\t", "=!");
        stringBuilder.append(text);
        return stringBuilder.toString();
    }

    public static Date getFechaDate(String fecha) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaI = null;
        if (fecha != null)
            try {
                fechaI = simpleDateFormat.parse(fecha);

            } catch (ParseException e) {
                simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    fechaI = simpleDateFormat.parse(fecha);

                } catch (ParseException e2) {
                    e2.printStackTrace();
                }
            }

        return fechaI;

    }

    public static String parseDateString (String fecha) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaI = null;
        String fechaR = "";
        if (fecha != null)
            try {
                fechaI = simpleDateFormat.parse(fecha);
                fechaR = dateFormat.format(fechaI);
            } catch (ParseException e) {

            }

        return fechaR;

    }

    public static Date getFechaDateWithHour(String fecha) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fechaI = null;
        if (fecha != null)
            try {
                fechaI = simpleDateFormat.parse(fecha);

            } catch (ParseException e) {
                simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                try {
                    fechaI = simpleDateFormat.parse(fecha);

                } catch (ParseException e2) {
                    e2.printStackTrace();
                }
            }

        return fechaI;

    }

    public static String getHora(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String value = "";
        if (cal.get(Calendar.HOUR_OF_DAY) <= 9) {
            value = "0" + cal.get(Calendar.HOUR_OF_DAY);
        } else {
            value = "" + cal.get(Calendar.HOUR_OF_DAY);
        }
        if (cal.get(Calendar.MINUTE) <= 9) {
            value = value + "0" + cal.get(Calendar.MINUTE);
        } else {
            value = value + "" + cal.get(Calendar.MINUTE);
        }
        return value;


    }

    public static String getHoraWithSeconds(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String value = "";
        if (cal.get(Calendar.HOUR_OF_DAY) <= 9) {
            value = "0" + cal.get(Calendar.HOUR_OF_DAY);
        } else {
            value = "" + cal.get(Calendar.HOUR_OF_DAY);
        }
        if (cal.get(Calendar.MINUTE) <= 9) {
            value = value + "0" + cal.get(Calendar.MINUTE);
        } else {
            value = value + "" + cal.get(Calendar.MINUTE);
        }
        if (cal.get(Calendar.SECOND) <= 9) {
            value = value + "0" + cal.get(Calendar.SECOND);
        } else {
            value = value + "" + cal.get(Calendar.SECOND);
        }
        return value;


    }

    public static String getFechaName(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String mesS, diaS, minutosS, segS, horasS;
        int mes = cal.get(Calendar.MONTH) + 1;
        if (mes < 10) {
            mesS = "0" + mes;
        } else
            mesS = String.valueOf(mes);

        int dia = cal.get(Calendar.DAY_OF_MONTH);
        if (dia < 10) {
            diaS = "0" + dia;
        } else
            diaS = String.valueOf(dia);

        int minutos = cal.get(Calendar.MINUTE);
        if (minutos < 10) {
            minutosS = "0" + minutos;
        } else
            minutosS = String.valueOf(minutos);

        int seg = cal.get(Calendar.SECOND);
        if (seg < 10) {
            segS = "0" + seg;
        } else
            segS = String.valueOf(seg);

        int horas = cal.get(Calendar.HOUR_OF_DAY);
        if (horas < 10)
            horasS = "0" + horas;
        else
            horasS = String.valueOf(horas);


        String value = cal.get(Calendar.YEAR) + "-" + mesS + "-"
                + diaS + " " + horasS + ":" +
                minutosS + ":" + segS;

        return value;

    }


    public static String getBirthday(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        String mesS, diaS;
        int mes = cal.get(Calendar.MONTH) + 1;
        if (mes < 10) {
            mesS = "0" + mes;
        } else
            mesS = String.valueOf(mes);

        int dia = cal.get(Calendar.DAY_OF_MONTH);
        if (dia < 10) {
            diaS = "0" + dia;
        } else
            diaS = String.valueOf(dia);
        String value = diaS + "/" + mesS + "/" + cal.get(Calendar.YEAR);

        return value;

    }

    public static String getFechaNameWithinHour(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        String mesS, diaS;
        int mes = cal.get(Calendar.MONTH) + 1;
        if (mes < 10) {
            mesS = "0" + mes;
        } else
            mesS = String.valueOf(mes);

        int dia = cal.get(Calendar.DAY_OF_MONTH);
        if (dia < 10) {
            diaS = "0" + dia;
        } else
            diaS = String.valueOf(dia);
        String value = cal.get(Calendar.YEAR) + "-" + mesS + "-"
                + diaS;

        return value;

    }

    public static String getDayWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                return "Lunes";
            case Calendar.TUESDAY:
                return "Martes";
            case Calendar.WEDNESDAY:
                return "Miércoles";
            case Calendar.THURSDAY:
                return "Jueves";
            case Calendar.FRIDAY:
                return "Viernes";
            case Calendar.SATURDAY:
                return "Sábado";
            case Calendar.SUNDAY:
                return "Domingo";
        }
        return "";
    }

    public static String getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String mes = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        char in = mes.charAt(0);

        return in + mes.substring(1);
    }

    public static boolean isDateHabilited(Calendar calendar) {

        if (getDayWeek(calendar.getTime()).equals("Sábado") || getDayWeek(calendar.getTime()).equals("Domingo")) {
            return true;
        }
        return false;
    }

    public static int getEdad(Date fechaNac) {
        Date hoy = new Date(System.currentTimeMillis());
        long tiempo = hoy.getTime() - fechaNac.getTime();
        double years = tiempo / 3.15576e+10;
        int age = (int) Math.floor(years);
        return age;
    }

    public static void changeColor(Drawable drawable, Context mContext, int colorNo) {
        if (drawable instanceof ShapeDrawable)
            ((ShapeDrawable) drawable).getPaint().setColor(ContextCompat.getColor(mContext, colorNo));
        else if (drawable instanceof GradientDrawable)
            ((GradientDrawable) drawable).setColor(ContextCompat.getColor(mContext, colorNo));
        else if (drawable instanceof ColorDrawable)
            ((ColorDrawable) drawable).setColor(ContextCompat.getColor(mContext, colorNo));
    }


    public static String limpiarAcentos(String cadena) {
        String limpio = null;
        if (cadena != null) {
            String valor = cadena;
            valor = valor.toUpperCase();
            // Normalizar texto para eliminar acentos, dieresis, cedillas y tildes
            limpio = Normalizer.normalize(valor, Normalizer.Form.NFD);
            // Quitar caracteres no ASCII excepto la enie, interrogacion que abre, exclamacion que abre, grados, U con dieresis.
            limpio = limpio.replaceAll("[^\\p{ASCII}(N\u0303)(n\u0303)(\u00A1)(\u00BF)(\u00B0)(U\u0308)(u\u0308)]", "");
            // Regresar a la forma compuesta, para poder comparar la enie con la tabla de valores
            limpio = Normalizer.normalize(limpio, Normalizer.Form.NFC);
        }
        return limpio;
    }

    public static String convertImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] img = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(img, Base64.DEFAULT);

    }

    public static byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] getFileDataFromUri(Context context, Uri uri) {
        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            inputStream = context.getContentResolver().openInputStream(uri);
            byteArrayOutputStream = new ByteArrayOutputStream();
            int leng = 0;
            byte[] buf = new byte[1024];
            while (((leng = inputStream.read(buf)) != -1)) {
                byteArrayOutputStream.write(buf, 0, leng);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream != null ? byteArrayOutputStream.toByteArray() : null;

    }

    public static String getTipoUser(int i) {
        switch (i) {
            case 1:
                return "Alumno";
            case 2:
                return "Profesor";
            case 3:
                return "Nodocente";
            case 4:
                return "Egresado";
            case 5:
                return "Particular";
        }
        return "";
    }

    public static String getFechaFormat(String fechaRegistro) {
        Calendar calendar = new GregorianCalendar();
        Date date = getFechaDateWithHour(fechaRegistro);
        if (date != null) {
            calendar.setTime(date);
            String dia = getDayWeek(date);
            String mes = getMonth(date);
            String anio = String.valueOf(calendar.get(Calendar.YEAR));
            String minutosS, segS, horasS;
            int minutos = calendar.get(Calendar.MINUTE);
            if (minutos < 10) {
                minutosS = "0" + minutos;
            } else
                minutosS = String.valueOf(minutos);

            int seg = calendar.get(Calendar.SECOND);
            if (seg < 10) {
                segS = "0" + seg;
            } else
                segS = String.valueOf(seg);

            int horas = calendar.get(Calendar.HOUR_OF_DAY);
            if (horas < 10)
                horasS = "0" + horas;
            else {
                horasS = String.valueOf(horas);
            }
            String hora = String.format("%s:%s:%s", horasS, minutosS, segS);
            String fecha = String.format("%s, %s de %s de %s - %s", dia, calendar.get(Calendar.DAY_OF_MONTH), mes, anio, hora);
            return fecha;
        }
        return "NO FECHA";

    }

    public static void resetData(Context context) {
        new UsuarioViewModel(context).deleteAll();
        new EgresadoViewModel(context).deleteAll();
        new ProfesorViewModel(context).deleteAll();
        new AlumnoViewModel(context).deleteAll();
        new RolViewModel(context).deleteAll();
        FileStorageManager.deleteAll(0);
    }

    public static String getExtension(String nombreArchivo) {
        int index = nombreArchivo.lastIndexOf(".");
        return index != -1 ? nombreArchivo.substring(index + 1) : "";
    }

    public static int getColorExtension(String ext) {
        ext = ext.toUpperCase();
        switch (ext) {
            case "PNG":
            case "JPG":
            case "JPEG":
                return R.color.colorBrown;
            case "PDF":
                return R.color.colorRed;
            case "DOC":
            case "DOCX":
                return R.color.colorBlue;
            case "PPT":
                return R.color.colorOrange;
            case "ZIP":
            case "RAR":
                return R.color.colorYellow;
            case "XLS":
                return R.color.colorGreen;
            default:
                return R.color.colorPink;
        }
    }

    public static String getSizeFile(Long size) {
        Double sizeD = Double.parseDouble(String.valueOf(size));
        if (sizeD > 1024 * 1024) {
            return String.format("%s %s", new DecimalFormat("#.##").format(sizeD / (1024 * 1024)), "MB");
        } else if (sizeD > 1024) {
            return String.format("%s %s", new DecimalFormat("#.##").format(sizeD / (1024)), "KB");
        } else return String.format("%s %s", sizeD, "B");
    }

    public static RequestBuilder<Drawable> loadPicture(ImageView img, int id) {
        String URL = String.format("%s%s.jpg", Utils.URL_USUARIO_IMAGE_LOAD, id);
        return Glide.with(img.getContext()).load(URL)

                .apply(new RequestOptions().error(R.drawable.ic_user)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE));

    }

    public static String getFechaOrder(Date date) {

        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
            String mesS, diaS, minutosS, segS, horasS;
            int mes = cal.get(Calendar.MONTH) + 1;
            if (mes < 10) {
                mesS = "0" + mes;
            } else
                mesS = String.valueOf(mes);

            int dia = cal.get(Calendar.DAY_OF_MONTH);
            if (dia < 10) {
                diaS = "0" + dia;
            } else
                diaS = String.valueOf(dia);

            int minutos = cal.get(Calendar.MINUTE);
            if (minutos < 10) {
                minutosS = "0" + minutos;
            } else
                minutosS = String.valueOf(minutos);

            int seg = cal.get(Calendar.SECOND);
            if (seg < 10) {
                segS = "0" + seg;
            } else
                segS = String.valueOf(seg);

            int horas = cal.get(Calendar.HOUR_OF_DAY);
            if (horas < 10)
                horasS = "0" + horas;
            else
                horasS = String.valueOf(horas);

            return String.format("%s/%s/%s - %s:%s:%s", diaS, mesS, cal.get(Calendar.YEAR), horasS, minutosS, segS);
        } else return "NO FECHA";

    }

    public static String getFechaOrderOnly(Date date) {

        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
            String mesS, diaS;
            int mes = cal.get(Calendar.MONTH) + 1;
            if (mes < 10) {
                mesS = "0" + mes;
            } else
                mesS = String.valueOf(mes);

            int dia = cal.get(Calendar.DAY_OF_MONTH);
            if (dia < 10) {
                diaS = "0" + dia;
            } else
                diaS = String.valueOf(dia);


            return String.format("%s/%s/%s", diaS, mesS, cal.get(Calendar.YEAR));
        } else return "NO FECHA";

    }

    public static String getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int value = cal.get(Calendar.YEAR);

        return String.valueOf(value);
    }

    public static String getDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int value = cal.get(Calendar.DAY_OF_MONTH);

        return String.valueOf(value);
    }


    public static String getMes(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        switch (cal.get(Calendar.MONTH)) {
            case Calendar.JANUARY:
                return "Enero";
            case Calendar.FEBRUARY:
                return "Febrero";
            case Calendar.MARCH:
                return "Marzo";
            case Calendar.APRIL:
                return "Abril";
            case Calendar.MAY:
                return "Mayo";
            case Calendar.JUNE:
                return "Junio";
            case Calendar.JULY:
                return "Julio";
            case Calendar.AUGUST:
                return "Agosto";
            case Calendar.SEPTEMBER:
                return "Septiembre";
            case Calendar.OCTOBER:
                return "Octubre";
            case Calendar.NOVEMBER:
                return "Noviembre";
            case Calendar.DECEMBER:
                return "Diciembre";
        }

        return "";
    }

    public static String getFechaOnlyDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String value = cal.get(Calendar.DAY_OF_MONTH) + "/"
                + (cal.get(Calendar.MONTH) + 1) + "/" +
                cal.get(Calendar.YEAR);

        return value;
    }

    /**
     * Método que permite compartir una noticia a otras aplicaciones del usuario
     *
     * @param
     * @param activity  Contexto actual de la actividad desde donde de lo convoca
     * @param imageView imagen a compartir
     */
    public static void onShare(Noticia noticia, Activity activity, ImageView imageView) {
        File x = createFile(activity.getApplicationContext(), imageView);
        Uri image = FileProvider.getUriForFile(activity.getApplicationContext(),
                "com.unse.bienestar.estudiantil.provider", x);
        //Uri image = Uri.fromFile(x);
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        //share.setPackage("com.whatsapp");
        share.putExtra(Intent.EXTRA_TEXT,
                String.format(activity.getApplicationContext().getResources().getString(R.string.shareInfo), noticia.getTitulo()));
        share.putExtra(Intent.EXTRA_STREAM, image);
        share.setType("image/jpeg");
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            //context.startActivity(share);
            activity.startActivity(Intent.createChooser(share, "Compartir en..."));
        } catch (ActivityNotFoundException o) {
            showToast(activity.getApplicationContext(), "No es posible abrir");
        }

    }

    /**
     * Permite crear un archivo temporal en el almacenamiento local
     *
     * @param context   Contexto actual de la actividad desde donde de lo convoca
     * @param imageView imagen a guardar
     * @return fichero con los datos de la imagen almacenada
     */
    private static File createFile(Context context, ImageView imageView) {
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share.jpg");
        try {
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            FileOutputStream bos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);
            bos.close();
        } catch (IOException e) {
            showLog("Creator File", e.getMessage());
        }
        return file;
    }

    public static String getTime(String fecha) {
        Date fechaRegistro = getFechaDateWithHour(fecha);
        Date now = new Date(System.currentTimeMillis());
        long different = now.getTime() - fechaRegistro.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        if (elapsedDays > 0)
            return String.format("Hace %s días", elapsedDays);
        else if (elapsedHours > 0)
            return String.format("Hace %s horas", elapsedHours);
        else if (elapsedMinutes > 0)
            return String.format("Hace %s minutos", elapsedMinutes);
        else if (elapsedSeconds > 0)
            return String.format("Hace %s segundos", elapsedSeconds);
        else
            return "Hace un momento";

    }

    public static void openMap(Activity activity, LatLng latLng) {
        String uri = String.format(Locale.ENGLISH, "geo:%f,%f?z=%d&q=%f,%f (%s)", latLng.latitude, latLng.longitude,
                16, latLng.latitude, latLng.longitude, " ");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        activity.startActivity(intent);
    }

    public static String getFileName(Uri file, Activity context) {
        Cursor cursor = context.getContentResolver().query(file, null, null, null, null);
        assert cursor != null;
        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        return cursor.getString(nameIndex);
    }

    public static char encode(char charAt) {
        if (charAt % 2 == 0) {

            switch (charAt) {
                case '0':
                    return 'M';
                case '2':
                    return 'U';
                case '4':
                    return 'T';
                case '6':
                    return 'W';
                case '8':
                    return 'X';
                default:
                    return charAt;
            }

        } else return charAt;
    }

    public static void showPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSION_ALL);
    }
}

