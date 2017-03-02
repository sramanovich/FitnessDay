package net.sramanovich.fitnessday.db;

import net.sramanovich.fitnessday.utils.PairSet;

import java.util.ArrayList;

public class TrainingSet {
    public int split_nr;
    public int exercise_id;
    public String exercise_name;
    public ArrayList<PairSet> setList = new ArrayList<>();

    public TrainingSet() {
        this.split_nr = 0;
        this.exercise_id = 0;
        this.exercise_name = "";
        setList.clear();
    }

    public TrainingSet(int split_nr, int exer_id, String exercise_name) {
        this.split_nr = split_nr;
        this.exercise_id = exer_id;
        this.exercise_name = exercise_name;
        setList.clear();
    }

    public void add(int reps, double weight) {
        PairSet set = new PairSet(new Integer(reps), new Double(weight));
        setList.add(set);
    }

    @Override
    public boolean equals(Object o) {
        TrainingSet trSet = (TrainingSet)o;
        return exercise_id==trSet.exercise_id;
    }
}
