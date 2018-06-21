package com.lj.meinnotizalpha;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kairo on 22/03/17.
 */

public class Discipline extends SugarRecord{

    private String name;
    private String teacher;
    private String idDisc;
    private int semester;
    private boolean visible;

    public Discipline() { }

    public Discipline(String name, String teacher, int semester,String idDisc) {
        this.name = name;
        this.teacher = teacher;
        this.semester = semester;
        this.idDisc = idDisc;
    }

    public String getIdDisc() { return idDisc; }

    //isVisible
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    //semester
    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    //name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //teacher
    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public List<Note> getNotes() {
        return SugarRecord.find(Note.class, "discipline = ?", getId().toString());
    }
    
    //average
    public Double getAverage() {
        Double average = 0.0;
        List<Note> notes = getNotes();
        if(notes.size() > 0) {
            double somaNotas = 0;
            int somaPesos = 0;
            for (int i = 0; i < notes.size(); i++) {
                somaNotas+=(notes.get(i).getValue()*notes.get(i).getWeight());
                somaPesos+=notes.get(i).getWeight();
            }
            average += (somaNotas/somaPesos);
        }
        return average;
    }

    public String getSituation() {
        return (getAverage() >= 7) ? "Aprovado" : "Reprovado";
    }
}
