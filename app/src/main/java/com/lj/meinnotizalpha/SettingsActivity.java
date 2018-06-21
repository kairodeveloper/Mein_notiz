package com.lj.meinnotizalpha;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.gildaswise.horizontalcounter.HorizontalCounter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity implements RefreshListener {
    @BindView(R.id.settings_hc) HorizontalCounter settings;
    @BindView(R.id.fab_save_setting) FloatingActionButton saveSetting;
    @BindView(R.id.settings_button_clear) Button clearButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        setTitle("Configurações");
        Intent receive = getIntent();
    }

    @OnClick(R.id.settings_button_clear)
    public void clearAllDisciplines(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Tem certeza disso?")
                .setMessage("Deseja mesmp excluir todos os dados?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<Discipline> disciplines = Discipline.listAll(Discipline.class);
                        List<Note> notas = Note.listAll(Note.class);

                        Discipline.deleteAll(Discipline.class);
                        Note.deleteAll(Note.class);
                    }
                })
                .setNegativeButton("NÃO",null);
        builder.show();


    }

    @OnClick(R.id.fab_save_setting)
    public void storeSettings(){
        SharedPreferences sharedPreferences = getSharedPreferences("settingsMN",MODE_PRIVATE);
        SharedPreferences.Editor sharedEditor = sharedPreferences.edit();
        sharedEditor.putFloat("mediaValueDefault",7);
        sharedEditor.apply();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Feito", Toast.LENGTH_SHORT).show();
    }

    public double applySettings(){
        SharedPreferences sharedPreferences = getSharedPreferences("settingsMN",MODE_PRIVATE);
        return sharedPreferences.getFloat("mediaValueDefault", 7);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public Context getContext() {
        return this;
    }
}
