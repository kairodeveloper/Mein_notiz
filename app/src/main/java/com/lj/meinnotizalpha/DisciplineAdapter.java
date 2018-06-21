package com.lj.meinnotizalpha;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gildaswise.horizontalcounter.HorizontalCounter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kairo on 22/03/17.
 */

public class DisciplineAdapter extends RecyclerView.Adapter<DisciplineAdapter.ViewHolder> {

    private List<Discipline> disciplines = Discipline.listAll(Discipline.class);
    private RefreshListener activity;


    public DisciplineAdapter(List<Discipline> disciplinas, RefreshListener activity) {
        this.disciplines = disciplinas;
        this.activity = activity;
    }
    //edita a disciplina individualmente


    public class ViewHolder extends RecyclerView.ViewHolder{

        //cria o holder de disciplinas, e aponta cada variavel ao seu respectivo textview

        @BindView(R.id.titleDiscipline) TextView titulo;
        @BindView(R.id.img_status) ImageView status;
        @BindView(R.id.discipline_notes) TextView qtdNotas;
        @BindView(R.id.discipline_average) TextView media;

        public ViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    @Override
    public DisciplineAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //"aponta"para o xml q será usado
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_discipline, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DisciplineAdapter.ViewHolder holder, int position) {
        Discipline discipline = disciplines.get(holder.getAdapterPosition());
        holder.titulo.setText(discipline.getName());

        int qtdNotas = discipline.getNotes().size();
        Double media = discipline.getAverage();

        if (qtdNotas > 1) {
            holder.status.setImageResource((media >= 7.0) ? R.drawable.ic_good_note : R.drawable.ic_bad_note);
            holder.media.setVisibility(View.VISIBLE);
            holder.media.setText(String.format(activity.getContext().getString(R.string.media), media));
        }

        holder.qtdNotas.setText(String.format(activity.getContext().getString(R.string.discipline_qtd_notas), qtdNotas));
        holder.itemView.setOnClickListener(getClickListener(discipline));
        holder.itemView.setOnLongClickListener(getLongClickListener(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return disciplines.size();
    }

    private View.OnClickListener getClickListener(final Discipline discipline) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity.getContext(),DisciplineActivity.class);
                intent.putExtra("id",discipline.getId());
                activity.getContext().startActivity(intent);
            }
        };
    }

    private View.OnLongClickListener getLongClickListener(final int position) {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final View view = LayoutInflater.from(activity.getContext()).inflate(R.layout.menu_edt_rmv, null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(activity.getContext());
                final Button buttonRemove = (Button) view.findViewById(R.id.Remover);
                final Button buttonEdit = (Button) view.findViewById(R.id.Editar);
                builder.setCancelable(true);
                builder.setView(view);
                final AlertDialog dialog = builder.show();

                buttonRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        AlertDialog.Builder alert = new AlertDialog.Builder(activity.getContext());
                        alert.setTitle("Alerta")
                                .setMessage("Deseja mesmo excluir?")
                                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        remover(position);
                                    }
                                })
                                .setNegativeButton("Não",null);
                                alert.show();

                    }
                });

                buttonEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        editDiscipline(position);
                    }
                });

                return true;
            }
        };
    }
    private void editDiscipline(final int position){
        View view = LayoutInflater.from(activity.getContext()).inflate(R.layout.edit_discipline_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity.getContext());
        builder.setTitle(R.string.edit_title);

        final EditText name = (EditText) view.findViewById(R.id.editNameInDialog);
        final EditText teacher = (EditText) view.findViewById(R.id.editTeacherInDialog);
        final HorizontalCounter hcounter = (HorizontalCounter) view.findViewById(R.id.edit_horizontal_counter);

        final Discipline discipline = disciplines.get(position);

        name.setText(discipline.getName());
        teacher.setText(discipline.getTeacher());
        hcounter.setCurrentValue((double) discipline.getSemester());

        builder.setPositiveButton(R.string.salvar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                discipline.setName(name.getText().toString());
                discipline.setTeacher(teacher.getText().toString());
                discipline.setSemester(hcounter.getCurrentValue().intValue());
                discipline.save();
                disciplines.set(position, discipline); //guarda na posicao
                notifyItemChanged(position);  //modifica a posicao do objeto na lista
                activity.onRefresh();  //atualiza a lista do main
                Toast.makeText(activity.getContext(), "Atualizado", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar",null);

        //builder.setCancelable(true);
        builder.setView(view);
        builder.show();
    }

    private void remover(final int position){
        Long posi = disciplines.get(position).getId();
        Discipline disc = Discipline.findById(Discipline.class,posi);
        disc.delete();
        disciplines.remove(disciplines.get(position));
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, getItemCount());
        activity.onRefresh();

    }

}

