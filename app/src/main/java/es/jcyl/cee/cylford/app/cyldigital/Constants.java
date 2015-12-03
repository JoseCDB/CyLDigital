package es.jcyl.cee.cylford.app.cyldigital;


import es.jcyl.cee.cylford.app.cyldigital.model.Family;
/**
 * Created by josecarlos.delbarrio on 29/09/2015.
 */
public class Constants {

    public static final String  TIPO_ONLINE = "ONLINE";

    public static final String  TIPO_PRESENCIAL = "PRESENCIAL";

    /** Provinces of Castilla y León by alphabetical order */
    public static final Family[] provincias = new Family[] {
            new Family("", "Seleccionar provincia..."),
            new Family("avila", "Ávila"),
            new Family("burgos", "Burgos"),
            new Family("leon", "León"),
            new Family("palencia", "Palencia"),
            new Family("salamanca", "Salamanca"),
            new Family("segovia", "Segovia"),
            new Family("soria", "Soria"),
            new Family("valladolid", "Valladolid"),
            new Family("zamora", " Zamora")
    };

    /** Filter by type options four activities section */
    public static final Family[] tiposActividad = new Family[] {
            new Family("", "Seleccionar tipo actividad..."),
            new Family("charla", "Charla"),
            new Family("curso", "Curso"),
            new Family("taller", "Taller")
    };
}
