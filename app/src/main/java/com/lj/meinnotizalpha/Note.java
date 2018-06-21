package com.lj.meinnotizalpha;

import com.orm.SugarRecord;

/**
 * Created by kairo on 22/03/17.
 */

public class Note extends SugarRecord {

    private Discipline discipline;
    private double value;
    private int weight=1;


    public Note() {
    }

    public Note(double value, int weight, Discipline discipline) {
        this.value = value;
        this.weight = weight;
        this.discipline = discipline;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    //valor nota
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }


    //peso nota
    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
