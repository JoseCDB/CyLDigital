package es.jcyl.cee.cylford.app.cyldigital.parser.dto;

import java.io.Serializable;

/**
 * Created by josecarlos.delbarrio on 14/10/2015.
 */
public class CyLDFormacion extends CyLDDTO implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * Atributos Comunes
     */

    /*Devuelve un booleano indicando si el mensaje es correcto o no.*/
    public int correcto; //boolean en su origen

    /*Devuelve un String indicando un mensaje sobre el error que ha
    dado en caso de que el campo anterior sea false o un mensaje de
    éxito en el caso de que se haya devuelto correctamente la respuesta.*/
    public String mensaje;

    public int id;

    public String tipoFormación;

    /*Devuelve un String indicando la provincia de la actividad*/
    public String provincia;

    /*Devuelve un String indicando el nombre de la actividad*/
    public String nombre;

    /*Devuelve un String indicando la descripción de la actividad*/
    public String descripcion;

    /*Devuelve un String indicando la fecha de inicio de la actividad en formato yyyy-MM-dd*/
    public String fechaInicio;

    /*Devuelve un String indicando la hora de inicio de la actividad en formato yyyy-MM-dd*/
    public String fechaFin;

    /*Devuelve un float indicando el número de horas que dura la actividad*/
    public float numeroHoras;

    /*Devuelve un int indicando el número de plazas de la actividad*/
    public int numeroPlazas;

    /*Devuelve un int indicando el número de solicitudes de la actividad. Corresponde con el
    resultado de la suma de la lista de usuarios matriculados en la actividad (confirmados o no)
    mas los usuarios que están en lista de espera*/
    public int numeroSolicitudes;

    /*Devuelve un int indicando el número de plazas para la lista de espera de la actividad*/
    public int plazasEnListaEspera;

    /*Devuelve un String indicando los requisitos de la actividad,*/
    public String requisitos;

    /*Devuelve un String indicando el campo aviso de la actividad*/
    public String aviso;

    /*Devuelve un String indicando la temática de la actividad*/
    public String tematica;

    /**
     * Atributos Online
     */

    /*booleano que indica si el curso online pertenece a una agrupación o no.
    En CyL Digital hay algunos cursos que forman parte de una agrupación*/
    public int agrupacion;//En origen boolean pero wn SQLite int 1 0

    /*En el caso de que pertenezca a una agrupación, devuelve el nombre de la agrupación online*/
    public String nombreAgrupacion;

    /*Devuelve un String indicando la url de la ficha web de la actividad, desde la cual el usuario podrá solicitar matrícula.
    Para ello, es necesario que el usuario haya iniciado sesión previamente en la página web www.cyldigital.es.*/
    public String url;

    /**
     * Atributos Presencial
     */

    /*Devuelve un String indicando de que tipo es la actividad (curso, taller, charla)*/
    public String tipo;

    /*Devuelve un String indicando la hora de inicio de la actividad en formato HH:MM*/
    public String horaInicio;

    /*Devuelve un String indicando la hora de fin de la actividad en formato HH:MM*/
    public String horaFin;

    /*Devuelve un String indicando la fecha de inicio para matricularse en la actividad en formato yyyy-MM-dd*/
    public String fechaInicioMatriculacion;

    /*Devuelve un String indicando la fecha de fin para matricularse en la actividad en formato yyyy-MM-dd*/
    public String fechaFinMatriculacion;

    /*Devuelve un String indicando el nombre del centro en el que se imparte la actividad*/
    public String centro;

    /*Devuelve un String indicando de que nivel de la actividad (básico, medio,avanzado)*/
    public String nivel;


    /**
     * Getters y Setters
     */
    /*
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nom) {
        nombre = nom;
    }
    public String getDescriocion() {
        return descripcion;
    }
    public void setDescripcion(String des) {
        descripcion = des;
    }
    public String getFechaInicio() {
        return fechaInicio;
    }
    public void setFechaInicio(String fecIni) {
        fechaInicio = fecIni;
    }
    public String getFechaFin() {
        return fechaFin;
    }
    public void setFechaFin(String fecFin) {
        fechaInicio = fecFin;
    }
    public float getNumeroHoras() {
        return numeroHoras;
    }
    public void setNumeroHoras(float numHor) {
        numeroHoras = numHor;
    }
    public int getNumeroPlazas() {
        return numeroPlazas;
    }
    public void setNumeroPlazas(int numPla) {
        numeroPlazas = numPla;
    }
    public int getNumeroSolicitudes() {
        return numeroSolicitudes;
    }
    public void setNumeroSolicitudes(int numSol) {
        numeroSolicitudes = numSol;
    }
    public int getPlazasListaEspera() {
        return plazasEnListaEspera;
    }
    public void setPlazasListaEspera(int numPlaLista) {
        plazasEnListaEspera = numPlaLista;
    }
    public String getRequisitos() {
        return requisitos;
    }
    public void setRequisitos(String req) {
        requisitos = req;
    }
    public String getAviso() {
        return aviso;
    }
    public void setAviso(String avs) {
        aviso = avs;
    }
    public String getTematica() {
        return tematica;
    }
    public void setTematica(String tema) {
        tematica = tema;
    }
    public int getAgrupacion() {
        return agrupacion;
    }
    public void setAgrupacion(int tema) {
        agrupacion = tema;
    }
    public String getNombreAgrupacion() {
        return nombreAgrupacion;
    }
    public void setNombreAgrupacion(String agr) {
        nombreAgrupacion = agr;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getHoraInicio() {
        return horaInicio;
    }
    public void setHoraInicio(String horaIni) {
        this.horaInicio = horaIni;
    }
    public String getHoraFin() {
        return horaFin;
    }
    public void setHoraFin(String horaF) {
        this.horaFin = horaF;
    }
    public String getFechaInicioMatriculacion() {
        return fechaInicioMatriculacion;
    }
    public void setFechaInicioMatriculacion(String fecIniMat) {
        this.fechaInicioMatriculacion = fecIniMat;
    }
    public String getFechaFinMatriculacion() {
        return fechaFinMatriculacion;
    }
    public void setFechaFinMatriculacion(String fecFinMat) {
        this.fechaFinMatriculacion = fecFinMat;
    }
    public String getCentro() {
        return centro;
    }
    public void setCentro(String cen) {
        this.centro = cen;
    }
    public String getNivel() {
        return nivel;
    }
    public void setNivel(String niv) {
        this.centro = niv;
    }
    */
}


