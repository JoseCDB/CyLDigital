package es.jcyl.cee.cylford.app.cyldigital.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by josecarlos.delbarrio on 15/03/2016.
 */
public class Utils {
    public static void showSimpleDialog(Context ctx, String title, String text,
                                        DialogInterface.OnClickListener listener) {

        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setMessage(text).setTitle(title);
            builder.setPositiveButton(android.R.string.ok, listener);
            builder.setCancelable(false);
            builder.create().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String processHtml(String html) {
        String formattedHtml = "<html><head><style type=\"text/css\">a:link {color:#A10621;}a:visited {color:#A10621;}body {font-family: Helvetica, Arial; width:device-width; font-size:13px; margin:0px; color:#666666;}p {color:#666666;margin-bottom:20px;} ul{color:#666666} li {color:#666666;}</style></head><body>"
                + html + "</body></html>";
        return formattedHtml;
    }
}
