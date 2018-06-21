package com.lj.meinnotizalpha;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by lapesi on 29/03/17.
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder>{
    private List<Note> notes;
    private RefreshListener activity;
    private Context context;

    public NoteAdapter(List<Note> notas, Context context) {
        this.context=context;
        this.notes=notas;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView note,weight,positionItem;
        private ImageButton detail;

        public ViewHolder(View itemView) {
            super(itemView);
            note = (TextView) itemView.findViewById(R.id.note_in_item_recycler);
            weight = (TextView) itemView.findViewById(R.id.weight_in_item_recycler);
            detail = (ImageButton) itemView.findViewById(R.id.detail_in_item_recycler);
            positionItem = (TextView) itemView.findViewById(R.id.itemPosition);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_note,parent,false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.note.setText("Valor da Nota: "+note.getValue());
        holder.weight.setText("Peso: "+note.getWeight());
        if(note.getValue()>=7){
            holder.detail.setImageResource(R.mipmap.ic_good_note);
        }else{
            holder.detail.setImageResource(R.mipmap.ic_bad_note);
        }
        holder.positionItem.setText(""+(position+1));

    }

    @Override
    public int getItemCount() {
        if(notes!=null)
            return notes.size();
        else
            return 0;

    }

}
