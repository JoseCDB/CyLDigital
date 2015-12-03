package es.jcyl.cee.cylford.app.cyldigital.model;

/**
 * Created by josecarlos.delbarrio on 29/09/2015.
 */
public class Family {
    /** Web service code */
    String code;
    /** Name to show */
    String name;

    public Family(String c, String n) {
        code = c;
        name = n;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
