package net.sramanovich.fitnessday.utils;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class PairSet extends Pair<Integer, Double> implements Parcelable{

    private final static String REPS_PARAM_NAME="Reps";

    private final static String WEIGHT_PARAM_NAME="Weight";

    public PairSet(int reps, double weight) {
        super(reps, weight);
    }

    private PairSet(Parcel parcel) {
        Bundle bundle = parcel.readBundle();
        x = bundle.getInt(REPS_PARAM_NAME);
        y = bundle.getDouble(WEIGHT_PARAM_NAME);
    }

    public Integer getReps() {
        return x;
    }

    public Double getWeight() {
        return y;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();

        bundle.putInt("Reps", getReps());
        bundle.putDouble("Weight", getWeight());

        dest.writeBundle(bundle);
    }

    public static final Parcelable.Creator<PairSet> CREATOR = new Parcelable.Creator<PairSet>() {
        // unpack object from Parcel
        public PairSet createFromParcel(Parcel in) {
            return new PairSet(in);
        }

        public PairSet[] newArray(int size) {
            return new PairSet[size];
        }
    };
}
