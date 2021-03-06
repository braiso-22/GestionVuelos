package com.example.gestionvuelos.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.DialogFragment;

import com.example.gestionvuelos.R;

public class ListaDialogFragment extends DialogFragment {
    ListenerDialogFragment listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ListenerDialogFragment) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setTitle(R.string.select_city);

        ArrayAdapter<CharSequence> adaptador = ArrayAdapter.createFromResource(getActivity(), R.array.ciudades, android.R.layout.simple_list_item_1);
        builder.setAdapter(adaptador, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CharSequence strName = adaptador.getItem(i);
                if (i != 0) {
                    listener.onListaDialogClick(strName.toString());
                } else {

                }
            }
        });

        return builder.create();
    }
}