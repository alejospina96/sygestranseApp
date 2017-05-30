package com.sistemasivhorsnet.tutransporte;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.*;
import com.android.volley.toolbox.*;
import com.sistemasivhorsnet.tutransporte.classes.Session;

import org.json.*;

import java.util.*;

public class MainActivity extends AppCompatActivity {
    private Session session;
    private Spinner spinner;
    private Context context;
    private TextView tvHostname;
    private ArrayList<String> hostnames;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //new Session(this).logout();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        tvHostname = (TextView) findViewById(R.id.tvHostname);
        hostnames = new ArrayList<String>();
        sharedPreferences = getSharedPreferences(getString(R.string.archiveSP), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.commit();
        initSpinner();
        if (sharedPreferences.getString(getString(R.string.hostnameSP), "") != "") {
            tvHostname.setText(sharedPreferences.getString(getString(R.string.hostnameSP), getString(R.string.prompt_no_iet_selected)));
        }
        if (schoolSelected()) {
            nextActivity();
        }
    }

    private void nextActivity() {
        if (!sessionStarted()) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            activityTree();
        }

    }

    private int getRol() {
        if (sharedPreferences.getString(getString(R.string.rolSP), "").equals(getString(R.string.ROL_MONITOR))) {
            return R.string.ROL_MONITOR;
        } else if (sharedPreferences.getString(getString(R.string.rolSP), "").equals(getString(R.string.ROL_SUPLENTE))) {
            return R.string.ROL_SUPLENTE;
        }
        return -1;
    }

    private void activityTree() {
        int rol = getRol();
        if (rol == R.string.ROL_MONITOR) {
            if (!restingMonitor())
                startActivity(new Intent(this, MonitorActivity.class));
            else
                startActivity(new Intent(this, NoFueMonitorActivity.class));
        } else if (rol == R.string.ROL_SUPLENTE) {
            startActivity(new Intent(this, SuplenteActivity.class));
        }
    }

    public void initSpinner() {
        spinner = (Spinner) findViewById(R.id.spInstituciones);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.mainHostRD) + "/Transporte/requests/institucion_educativa.php?mode=all";
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean(getString(R.string.successRD));
                            if (success) {
                                List<String> list = new ArrayList<String>();
                                list.add(getString(R.string.prompt_select_one));
                                hostnames.add("");
                                JSONArray jsonArray = jsonObject.getJSONArray(getString(R.string.resultsRD));
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    list.add(((JSONObject) jsonArray.get(i)).getString(getString(R.string.short_nameRD)));
                                    hostnames.add(((JSONObject) jsonArray.get(i)).getString(getString(R.string.hostnameSP)));
                                }

                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getBaseContext(),
                                        android.R.layout.simple_spinner_item, list);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(dataAdapter);
                            } else {
                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(context);
                                }
                                builder.setTitle(R.string.login_failRD)
                                        .setMessage(jsonObject.getString(getString(R.string.errorRD)))
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
                tvHostname.setText("Error: " + error.getLocalizedMessage());
            }
        }) {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(getString(R.string.modeIETRD), "all");
                return params;
            }

            ;
        };
        queue.add(stringRequest);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) findViewById(R.id.spInstituciones);
                if (!spinner.getSelectedItem().toString().equals(getString(R.string.prompt_select_one))) {
                    String selected = spinner.getSelectedItem().toString(), hSelected = hostnames.get(position);
                    editor.putString(getString(R.string.schoolSP), selected);
                    editor.putString(getString(R.string.hostnameSP), hSelected);
                    editor.commit();
                    nextActivity();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private boolean restingMonitor() {
        //TODO: implement cache check
        return false;
    }

    private boolean schoolSelected() {
        return sharedPreferences.getString(getString(R.string.schoolSP), "") != "";
    }

    private boolean sessionStarted() {
        if (session == null) session = new Session(this);
        return session.started(this);
    }
}
