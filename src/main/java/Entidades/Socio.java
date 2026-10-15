package Entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "SOCIO")
public class Socio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_socio")
    private int idSocio;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "DNI")
    private String dni;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "fecha_alta")
    private String fechaAlta; // Se maneja como texto (formato yyyy-MM-dd)

    public Socio() {
    }

    public Socio(String nombre, String dni, String telefono, String fechaAlta) {
        this.nombre = nombre;
        this.dni = dni;
        this.telefono = telefono;
        this.fechaAlta = fechaAlta;
    }

    public int getIdSocio() {
        return idSocio;
    }

    public void setIdSocio(int idSocio) {
        this.idSocio = idSocio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(String fechaAlta) {
        this.fechaAlta = fechaAlta;
    }
}
