package com.sistemasivhorsnet.tutransporte;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sistemasivhorsnet.tutransporte.classes.Session;
import com.sistemasivhorsnet.tutransporte.dialogs.ConfirmLogoutDialog;

import java.util.Calendar;

public class MonitorActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvHora;
    private int hora, minuto;
    private Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);

        session = new Session(this);

        tvHora = (TextView)findViewById(R.id.tvHora);

        Runnable r = new RefreshClock();

        Thread thClock = new Thread(r);
        thClock.start();

        Button btnCantGo = (Button) findViewById(R.id.btnCantGo);
        btnCantGo.setOnClickListener(this);

        Button btnInitTrip = (Button) findViewById(R.id.btnInitTrip);
        btnInitTrip.setOnClickListener(this);
    }

    private void initLogoutFragment() {
        ConfirmLogoutDialog dialog = new ConfirmLogoutDialog();
        dialog.setSession(this.session);
        dialog.show(getSupportFragmentManager(),"logout_dialog");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCantGo:
                startActivity(new Intent(this, NoPuedesIrActivity.class));
                break;
            case R.id.btnInitTrip:
                startActivity(new Intent(this, AsistenciaActivity.class));
                break;

        }
    }

    private class RefreshClock implements Runnable{
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()){
                try{
                    initClock();
                    Thread.sleep(500);
                }catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
    private void initClock(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateTime();
                String currentTime = hora+":"+minuto;
                tvHora.setText(currentTime);
            }
        });
    }
    private void updateTime() {
        Calendar c = Calendar.getInstance();
        hora = c.get(Calendar.HOUR_OF_DAY);
        minuto = c.get(Calendar.MINUTE);
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
}
