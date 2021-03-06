package net.sramanovich.fitnessday;

/**
 * Created by Administrator on 14.02.2017.
 */

public class Constants {

    public static final String INTENT_PARAM_ID = "ID";
    public static final String INTENT_PARAM_NAME = "NAME";
    public static final String INTENT_PARAM_TYPE = "TYPE";
    public static final String INTENT_PARAM_NOTE = "NOTE";
    public static final String INTENT_PARAM_IS_TEMPLATE = "IS_TEMPLATE";
    public static final String INTENT_PARAM_VIEW_MODE = "VIEW_MODE";
    public static final String INTENT_PARAM_POSITION = "POSITION";
    public static final String INTENT_PARAM_SET_LIST = "SET_LIST";

    public static final int    DB_VERSION = 1;
    public static final String DB_NAME = "Fitness";

    public static final String USER_PROGRAM_NAME_SEPARATOR = " - ";

    public static final int TT_ALL_PROGRAMS = -1;
    public static final int TT_USER_PROGRAM_TEMPLATE = 0;
    public static final int TT_PROGRAM_TEMPLATE = 1;
    public static final int TT_USER_PROGRAM = 2;

    public static final int TAB_MAIN = 0;
    public static final int TAB_EXERCISES = 1;
    public static final int TAB_PROGRAMS = 2;
    public static final int TAB_USER_PROGRAMS = 3;
}
