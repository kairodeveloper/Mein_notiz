package com.lj.meinnotizalpha;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.gildaswise.horizontalcounter.HorizontalCounter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements RefreshListener {

    public List<Discipline> disciplines;
    @BindView(R.id.recyclerViewDisciplines) RecyclerView disciplinesMain;
    @BindView(R.id.buttomAddDisc) FloatingActionButton buttonAddDiscipline;
    private DisciplineAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setTitle(R.string.disciplina);
        disciplines = Discipline.listAll(Discipline.class);
        loadDisciplines();
    }

    //Adapta e mostra a lista de disciplinas
    public void loadDisciplines(){
        if(disciplines != null) {
            boobleDiscipline();
            disciplinesMain.setHasFixedSize(true);
            disciplinesMain.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            if(adapter == null) adapter = new DisciplineAdapter(disciplines, MainActivity.this);
            disciplinesMain.setAdapter(adapter);
        }
    }

    //Infla a dialog de nova disciplina
    @OnClick(R.id.buttomAddDisc)
    public void newDiscipline(){
        AlertDialog.Builder alertDialogBuilder =  new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.add_discipline_dialog,null);
        final EditText nomeDisc = (EditText) view.findViewById(R.id.newNameInDialog);
        final EditText profDisc = (EditText) view.findViewById(R.id.newTeacherInDialog);
        final HorizontalCounter horizontalConter = (HorizontalCounter) view.findViewById(R.id.new_horizontal_counter);


        alertDialogBuilder.setTitle(R.string.inserir_disciplina)
                .setPositiveButton(R.string.Insert,new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        String name = nomeDisc.getText().toString();
                        String teacher = profDisc.getText().toString();
                        Integer period = horizontalConter.getCurrentValue().intValue();
                        if(name.isEmpty() || teacher.isEmpty() || period.equals(0)) {
                            Toast.makeText(MainActivity.this, R.string.refresh, Toast.LENGTH_SHORT).show();
                        }else{

                            Discipline newDisc = new Discipline(name,teacher,period,name);
                            disciplines.add(newDisc);
                            newDisc.save();

                            loadDisciplines();
                            Toast.makeText(MainActivity.this, R.string.hasInsert, Toast.LENGTH_SHORT).show();

                        }


                    }
                })

                .setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){

                    }
                });

        alertDialogBuilder.setView(view);
        alertDialogBuilder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    public void onRefresh() {
        loadDisciplines();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.action_bar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.actionbar_settings_button){
            Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(intent);
        }



        return super.onOptionsItemSelected(item);
    }

    public void boobleDiscipline(){
        if(disciplines.size()>1) {
            for (int i = disciplines.size()-1; i >= 0; i--) {
                for (int j = 0; j < i; j++) {
                    if (disciplines.get(j).getAverage() < disciplines.get(j + 1).getAverage()) {
                        Discipline temp = disciplines.get(j);
                        Discipline temp2 = disciplines.get(j + 1);
                        disciplines.set(j, temp2);
                        disciplines.set(j + 1, temp);
                    }
                }
            }
        }
    }
}