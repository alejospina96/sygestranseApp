package com.sistemasivhorsnet.tutransporte.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.sistemasivhorsnet.tutransporte.MainActivity;
import com.sistemasivhorsnet.tutransporte.MonitorActivity;
import com.sistemasivhorsnet.tutransporte.R;
import com.sistemasivhorsnet.tutransporte.classes.Session;

/**
 * Created by Daniel on 20/05/2017.
 */

public class ConfirmLogoutDialog extends DialogFragment {
    private Session session;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.confirm_logout)
                .setPositiveButton(R.string.logout, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        session.logout();
                        startActivity(new Intent(getActivity(),MainActivity.class));
                    }
                })
                .setMessage(R.string.confirm_logout_message)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
    public void setSession(Session session){
        this.session = session;
    }
}
