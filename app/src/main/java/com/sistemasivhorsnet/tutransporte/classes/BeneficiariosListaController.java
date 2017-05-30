package com.sistemasivhorsnet.tutransporte.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sistemasivhorsnet.tutransporte.AsistenciaActivity;
import com.sistemasivhorsnet.tutransporte.R;
import com.sistemasivhorsnet.tutransporte.entities.Beneficiario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class BeneficiariosListaController {
    private ArrayList<Beneficiario> beneficiarios;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private AsistenciaActivity context;
    public BeneficiariosListaController(AsistenciaActivity context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.archiveSP),Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        beneficiarios = checkSharedPreferences();
    }
    private ArrayList<Beneficiario> checkSharedPreferences() {
        beneficiarios = new ArrayList<>();
        String jsonBeneficiariosString = sharedPreferences.getString(context.getString(R.string.beneficiariosSP),"");
        if(!Objects.equals(jsonBeneficiariosString, "")){
            try {
                JSONArray jsonArray = new JSONArray(jsonBeneficiariosString);
                for (int i = 0; i<jsonArray.length();i++){
                    String documento = ((JSONObject)jsonArray.get(i)).getString(context.getString(R.string.documentoSP));
                    String primerNombre = ((JSONObject)jsonArray.get(i)).getString(context.getString(R.string.pNombreSP));
                    String segundoNombre = ((JSONObject)jsonArray.get(i)).getString(context.getString(R.string.sNombreSP));
                    String primerApellido = ((JSONObject)jsonArray.get(i)).getString(context.getString(R.string.pApellidoSP));
                    String segundoApellido = ((JSONObject)jsonArray.get(i)).getString(context.getString(R.string.sApellidoSP));
                    beneficiarios.add(new Beneficiario(documento,primerNombre,segundoNombre,primerApellido,segundoApellido));
                }
                return beneficiarios;
            } catch (JSONException e) {
                e.printStackTrace();
                return beneficiarios;
            }

        }
        return beneficiarios;
    }
    public ArrayList<Beneficiario> getBeneficiariosLoaded() {
        return beneficiarios;
    }
    public ArrayList<Beneficiario> loadBeneficiarios() {
        String url = context.getString(R.string.mainHostRD)+"/Transporte/requests/beneficiarios.php?" +
                "asignacion="+sharedPreferences.getString(context.getString(R.string.asignacionSP),"")
                +"&hostname="+sharedPreferences.getString(context.getString(R.string.hostnameSP),"");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(context);
                }
                /*builder.setTitle("RESPONSE")
                        .setMessage(response)
                        .setPositiveButton(android.R.string.yes, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();*/
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean(context.getString(R.string.successRD));
                    if(success){
                        beneficiarios.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray(context.getString(R.string.resultsRD));
                        for (int i = 0; i<jsonArray.length();i++){
                            String documento = ((JSONObject)jsonArray.get(i)).getString(context.getString(R.string.documentoSP));
                            String primerNombre = ((JSONObject)jsonArray.get(i)).getString(context.getString(R.string.pNombreSP));
                            String segundoNombre = ((JSONObject)jsonArray.get(i)).getString(context.getString(R.string.sNombreSP));
                            String primerApellido = ((JSONObject)jsonArray.get(i)).getString(context.getString(R.string.pApellidoSP));
                            String segundoApellido = ((JSONObject)jsonArray.get(i)).getString(context.getString(R.string.sApellidoSP));
                            beneficiarios.add(new Beneficiario(documento,primerNombre,segundoNombre,primerApellido,segundoApellido));
                            editor.putString(context.getString(R.string.beneficiariosSP),jsonArray.toString(1));
                            editor.apply();
                        }
                        context.setBeneficiarios(beneficiarios);
                    }else{
                        /*
                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(context);
                        }*/
                        builder.setTitle("ERROR")
                                .setMessage(jsonObject.getString(context.getString(R.string.errorRD)))
                                .setPositiveButton(android.R.string.yes, null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(context);
                }
                builder.setTitle("ERROR")
                        .setMessage(error.getMessage())
                        .setPositiveButton(android.R.string.yes, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);
        return beneficiarios;
    }
}
