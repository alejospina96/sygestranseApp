package com.sistemasivhorsnet.tutransporte;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.sistemasivhorsnet.tutransporte.classes.Session;
import com.sistemasivhorsnet.tutransporte.dialogs.ConfirmLogoutDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SuplenteActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvHora;
    private int hora;
    private Session session;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suplente);
        context = this;
        tvHora = (TextView)findViewById(R.id.tvHora);
        session = new Session(this);
        FloatingActionButton fabNoContesta = (FloatingActionButton) findViewById(R.id.fabNoContesta);
        fabNoContesta.setOnClickListener(this);
        Runnable r = new RefreshClock();

        Thread thClock = new Thread(r);
        thClock.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fabNoContesta:
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(this);
                }
                builder.setTitle(getString(R.string.suplenteStartTitle))
                        .setMessage(getString(R.string.suplenteStartMessage))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(context,AsistenciaActivity.class));
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
                break;
        }
    }


    class RefreshClock implements Runnable {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    initClock();
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private void initClock() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateTime();
                String currentTime = hora + ":" + new SimpleDateFormat("mm").format(new Date());
                tvHora.setText(currentTime);
            }
        });
    }

    private void updateTime() {
        Calendar c = Calendar.getInstance();
        hora = c.get(Calendar.HOUR_OF_DAY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_estudiante, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                initLogoutFragment();
                return true;
        }
        return false;
    }

    private void initLogoutFragment() {
        ConfirmLogoutDialog dialog = new ConfirmLogoutDialog();
        dialog.setSession(this.session);
        dialog.show(getSupportFragmentManager(), "logout_dialog");
    }
}
