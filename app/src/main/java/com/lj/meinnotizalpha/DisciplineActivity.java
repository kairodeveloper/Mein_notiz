package com.lj.meinnotizalpha;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gildaswise.horizontalcounter.HorizontalCounter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DisciplineActivity extends AppCompatActivity implements RefreshListener {


    @BindView(R.id.textview_media) TextView media;
    @BindView(R.id.textview_status) TextView status;
    @BindView(R.id.recycler_notas) RecyclerView listNotes;
    @BindView(R.id.fab_add_nota) FloatingActionButton addNota;
    private List<Note> notas;
    private NoteAdapter adapter;
    private Discipline discipline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discipline);
        ButterKnife.bind(this);
        recebeIntent();
        onRefresh();
        addNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder =  new AlertDialog.Builder(DisciplineActivity.this);
                View view = getLayoutInflater().inflate(R.layout.add_nota_in_discipline, null);
                final EditText value = ButterKnife.findById(view, R.id.add_new_nota);
                final HorizontalCounter weight = ButterKnife.findById(view, R.id.note_horizontal_counter);
                alertDialogBuilder.setTitle("Adicionar nota");
                alertDialogBuilder.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        double valor = Double.parseDouble(value.getText().toString());
                        int peso = weight.getCurrentValue().intValue();
                        Note novaNota = new Note(valor, peso, discipline);
                        notas.add(novaNota);
                        novaNota.save();
                        onRefresh();
                        dialog.dismiss();
                    }
                });
                alertDialogBuilder.setNegativeButton("Cancelar",null);
                alertDialogBuilder.setView(view);
                alertDialogBuilder.show();
            }
        });


    }

    @Override
    public void onRefresh() {
        if(notas != null) {
            listNotes.setHasFixedSize(true);
            if(adapter == null) adapter = new NoteAdapter(notas, DisciplineActivity.this);
            listNotes.setAdapter(adapter);
            listNotes.setLayoutManager(new LinearLayoutManager(this));

            if(discipline.getNotes().size() > 1) {
                media.setVisibility(View.VISIBLE);
                media.setText(String.format(getString(R.string.media), discipline.getAverage()));
                status.setVisibility(View.VISIBLE);
                status.setText(discipline.getSituation());

            }
        }
    }


    @Override
    public Context getContext() {
        return DisciplineActivity.this;
    }

    private void recebeIntent(){
        Intent intent = getIntent();
        long id = 0;
        id = intent.getLongExtra("id", id);
        discipline = Discipline.findById(Discipline.class, id);
        setTitle(discipline.getName());
        notas = discipline.getNotes();
    }

}
