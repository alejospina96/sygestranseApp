package com.sistemasivhorsnet.tutransporte;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sistemasivhorsnet.tutransporte.classes.AsistenciaBeneficiarios;
import com.sistemasivhorsnet.tutransporte.classes.Session;
import com.sistemasivhorsnet.tutransporte.classes.BeneficiariosListaController;
import com.sistemasivhorsnet.tutransporte.dialogs.ConfirmLogoutDialog;
import com.sistemasivhorsnet.tutransporte.entities.Beneficiario;

import java.util.ArrayList;

public class AsistenciaActivity extends AppCompatActivity {

    private BeneficiariosListaController beneficiariosListaController;
    private ArrayList<Beneficiario> beneficiarios;
    private Session session;
    private BeneficiarioListFragment fragment;

    public void setBeneficiarios(ArrayList<Beneficiario> beneficiarios) {
        this.beneficiarios = beneficiarios;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        session = new Session(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asistencia);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.archiveSP), Context.MODE_PRIVATE);

        beneficiariosListaController = new BeneficiariosListaController(this);
        beneficiarios = beneficiariosListaController.getBeneficiariosLoaded();
        if (beneficiarios.isEmpty()) {
            beneficiarios = beneficiariosListaController.loadBeneficiarios();
        }
        fragment = new BeneficiarioListFragment();
        fragment.setBeneficiarios(beneficiarios);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.listContainer, fragment)
                .commit();
        /*AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("SHARED PREFF")
                .setMessage(sharedPreferences.getString(getString(R.string.beneficiariosSP), "Vacio"))
                .setPositiveButton(android.R.string.yes, null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_asistencia, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutA:
                initLogoutFragment();
                return true;
            case R.id.refresh:
                actualizarInterfaz();
                return true;
        }
        return true;
    }

    private void actualizarInterfaz() {
        beneficiarios = beneficiariosListaController.loadBeneficiarios();
        fragment.updateBeneficiarios(beneficiarios);
        fragment.lvBeneficiarios.invalidateViews();
        fragment.lvBeneficiarios.refreshDrawableState();
    }

    private void initLogoutFragment() {
        ConfirmLogoutDialog dialog = new ConfirmLogoutDialog();
        dialog.setSession(this.session);
        dialog.show(getSupportFragmentManager(), "logout_dialog");
    }
    public static class ListAdapter extends ArrayAdapter<Item> {

        public ListAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public ListAdapter(Context context, int resource, List<Item> items) {
            super(context, resource, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.itemlistrow, null);
            }

            Item p = getItem(position);

            if (p != null) {
                TextView tt1 = (TextView) v.findViewById(R.id.id);
                TextView tt2 = (TextView) v.findViewById(R.id.categoryId);
                TextView tt3 = (TextView) v.findViewById(R.id.description);

                if (tt1 != null) {
                    tt1.setText(p.getId());
                }

                if (tt2 != null) {
                    tt2.setText(p.getCategory().getId());
                }

                if (tt3 != null) {
                    tt3.setText(p.getDescription());
                }
            }

            return v;
        }

    }
    public static class BeneficiarioListFragment extends Fragment {
        private AsistenciaBeneficiarios asistenciaBeneficiarios;
        private ArrayList<Beneficiario> beneficiarios;
        private ArrayAdapter<String> adapter;
        private ListView lvBeneficiarios;
        private ArrayList<String> beneficiariosStrings;


        public BeneficiarioListFragment() {
        }

        public void setBeneficiarios(ArrayList<Beneficiario> beneficiarios) {
            this.beneficiarios = beneficiarios;
        }

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_list, container, false);
            asistenciaBeneficiarios = new AsistenciaBeneficiarios();
            beneficiariosStrings = new ArrayList<>();
            for (int i = 0; i < beneficiarios.size(); i++) {
                beneficiariosStrings.add(beneficiarios.get(i).toString());
            }
            adapter = new ArrayAdapter<>(
                    getActivity(),
                    R.layout.estudiante_record,
                    R.id.tvBeneficiarioItem,
                    beneficiariosStrings
            );
            lvBeneficiarios = (ListView) rootView.findViewById(R.id.lvBeneficiarios);
            lvBeneficiarios.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            lvBeneficiarios.setAdapter(adapter);
            lvBeneficiarios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    View v = view.findViewById(R.id.listContainer);
                    changeState(view);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            return rootView;
        }

        public void changeState(View view) {
            TextView tvBeneficiarioItem = (TextView) view.findViewById(R.id.tvBeneficiarioItem);
            Beneficiario b = buscarBeneficiario(tvBeneficiarioItem.getText().toString());
            if (!asistenciaBeneficiarios.alreadyAdded(b)) {
                asistenciaBeneficiarios.add(b);
            } else {
                asistenciaBeneficiarios.remove(b);
            }
        }

        public Beneficiario buscarBeneficiario(String nombre) {
            for (int i = 0; i < beneficiarios.size(); i++) {
                if (beneficiarios.get(i).equals(nombre))
                    return beneficiarios.get(i);
            }
            return null;
        }

        public void updateBeneficiarios(ArrayList<Beneficiario> beneficiarios) {
            adapter.clear();
            setBeneficiarios(beneficiarios);
            beneficiariosStrings.clear();
            for (int i = 0; i < beneficiarios.size(); i++) {
                beneficiariosStrings.add(beneficiarios.get(i).toString());
            }
            adapter.notifyDataSetChanged();
        }
    }
}
