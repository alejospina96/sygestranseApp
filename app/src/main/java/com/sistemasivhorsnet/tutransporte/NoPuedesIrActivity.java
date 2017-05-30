package com.sistemasivhorsnet.tutransporte;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NoPuedesIrActivity extends AppCompatActivity implements View.OnClickListener {

    private RequestQueue queue;
    private FloatingActionButton fabNoPuedesIrEnviar;
    private final Context context =this;
    private SharedPreferences sharedPreferences;
    private EditText etNoPuedesIrRazon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_puedes_ir);
        queue = Volley.newRequestQueue(this);
        sharedPreferences = getSharedPreferences(getString(R.string.archiveSP),Context.MODE_PRIVATE);
        fabNoPuedesIrEnviar = (FloatingActionButton) findViewById(R.id.fabNoPuedesIrEnviar);
        fabNoPuedesIrEnviar.setOnClickListener(this);
        etNoPuedesIrRazon = (EditText)findViewById(R.id.etNoPuedesIrRazon);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fabNoPuedesIrEnviar:
                saveCause();
                break;
        }
    }
    private void saveCause(){
        String url = context.getString(R.string.mainHostRD)+"/Transporte/requests/no_puedo_ir.php";
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(context);
                        }
                        if(!response.isEmpty()){
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if(jsonObject.getBoolean(getString(R.string.successRD))){
                                    builder.setTitle("Motivo registrado correctamente")
                                            .setMessage(response)
                                            .setPositiveButton(android.R.string.yes, null)
                                            .setIcon(android.R.drawable.ic_dialog_info)
                                            .show();
                                }else{
                                    builder.setTitle("Error")
                                            .setMessage(jsonObject.getString(getString(R.string.errorRD)))
                                            .setPositiveButton(android.R.string.yes, null)
                                            .setIcon(android.R.drawable.ic_dialog_info)
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{
                            builder.setTitle("Request vacio")
                                    .setPositiveButton(android.R.string.yes, null)
                                    .setIcon(android.R.drawable.ic_dialog_info)
                                    .show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(context);
                        }
                        builder.setTitle("No se pudo hacer el request")
                                .setMessage(error.getMessage())
                                .setPositiveButton(android.R.string.yes, null)
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .show();
                    }
                }
        ){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(getString(R.string.hostnameSP),sharedPreferences.getString(getString(R.string.hostnameSP),""));
                params.put(getString(R.string.asignacionSP),sharedPreferences.getString(getString(R.string.asignacionSP),""));
                DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                params.put(getString(R.string.horaSP), hourFormat.format(new Date()));
                params.put(getString(R.string.fechaSP),dateFormat.format(new Date()));
                params.put(getString(R.string.motivoSP),etNoPuedesIrRazon.getText().toString());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(stringRequest);
        restMonitor();
        //startActivity(new Intent(this, MainActivity.class));
    }

    private void restMonitor() {
        //TODO: implement rest monitor method
    }
}
