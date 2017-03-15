package net.sramanovich.fitnessday.utils;

import java.util.Date;

public class ProgramData {

    private String mName;

    private Date mDate;

    private String mNote;

    public ProgramData(String name, Date date, String note) {
        mName = name;
        mDate = date;
        mNote = note;
    }

    public String getmName() {
        return mName;
    }

    public String getmNote() {
        return mNote;
    }

    public Date getmDate() {
        return mDate;
    }
}
