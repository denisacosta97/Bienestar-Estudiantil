package com.unse.bienestar.estudiantil.Modelos;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Egresado extends Usuario implements Parcelable {

    public static final String TABLE = "egresado";
    public static final String KEY_ID_EGR = "idEgresado";
    public static final String KEY_PROFESION = "profesion";
    public static final String KEY_FECHA_EGRESO = "fechaegreso";

    private int idEgresado;
    private String profesion;
    private String fechaEgreso;

    public Egresado(int idUsuario, String nombre, String apellido,
                    String fechaNac, String pais, String provincia,
                    String localidad, String domicilio, String barrio,
                    String telefono, String sexo, String mail,
                    int tipoUsuario, String fechaRegistro, String fechaModificacion,
                    int validez, int idEgresado, String profesion, String fechaEgreso) {
        super(idUsuario, nombre, apellido, fechaNac, pais, provincia, localidad, domicilio,
                barrio, telefono, sexo, mail, tipoUsuario, fechaRegistro,
                fechaModificacion, validez);
        this.idEgresado = idEgresado;
        this.profesion = profesion;
        this.fechaEgreso = fechaEgreso;
    }

    public Egresado() {
    }

    protected Egresado(Parcel in) {
        setIdUsuario(in.readInt());
        setTipoUsuario(in.readInt());
        setValidez(in.readInt());
        setNombre(in.readString());
        setApellido(in.readString());
        setPais(in.readString());
        setProvincia(in.readString());
        setLocalidad(in.readString());
        setDomicilio(in.readString());
        setBarrio(in.readString());
        setTelefono(in.readString());
        setSexo(in.readString());
        setMail(in.readString());
        setFechaNac(in.readString());
        setFechaRegistro(in.readString());
        setFechaModificacion(in.readString());

        profesion = in.readString();
        idEgresado = in.readInt();
        fechaEgreso = in.readString();
    }

    public static final Creator<Egresado> CREATOR = new Creator<Egresado>() {
        @Override
        public Egresado createFromParcel(Parcel in) {
            return new Egresado(in);
        }

        @Override
        public Egresado[] newArray(int size) {
            return new Egresado[size];
        }
    };

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    public int getIdEgresado() {
        return idEgresado;
    }

    public void setIdEgresado(int idEgresado) {
        this.idEgresado = idEgresado;
    }

    public String getFechaEgreso() {
        return fechaEgreso;
    }

    public void setFechaEgreso(String fechaEgreso) {
        this.fechaEgreso = fechaEgreso;
    }

    public static Egresado mapper(JSONObject o, Usuario usuario) {
        Egresado egresado = new Egresado();
        try {
            if (o.has("datos")) {
                JSONObject object = o.getJSONObject("datos");
                String profesion = object.getString("profesion");
                String anioIng = object.getString("fechaegreso");
                egresado = new Egresado(usuario.getIdUsuario(), usuario.getNombre(), usuario.getApellido(),
                        usuario.getFechaNac(), usuario.getPais(), usuario.getProvincia(), usuario.getLocalidad(),
                        usuario.getDomicilio(), usuario.getBarrio(), usuario.getTelefono(), usuario.getSexo(),
                        usuario.getMail(), usuario.getTipoUsuario(), usuario.getFechaRegistro(), usuario.getFechaModificacion(),
                        usuario.getValidez(), usuario.getIdUsuario(), profesion, anioIng);
                egresado.setValidez(usuario.getValidez());
            }
            return egresado;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return egresado;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getIdUsuario());
        dest.writeInt(getTipoUsuario());
        dest.writeInt(getValidez());
        dest.writeString(getNombre());
        dest.writeString(getApellido());
        dest.writeString(getPais());
        dest.writeString(getProvincia());
        dest.writeString(getLocalidad());
        dest.writeString(getDomicilio());
        dest.writeString(getBarrio());
        dest.writeString(getTelefono());
        dest.writeString(getSexo());
        dest.writeString(getMail());
        dest.writeString(getFechaNac());
        dest.writeString(getFechaRegistro());
        dest.writeString(getFechaModificacion());

        dest.writeString(profesion);
        dest.writeInt(idEgresado);
        dest.writeString(fechaEgreso);
    }
}
