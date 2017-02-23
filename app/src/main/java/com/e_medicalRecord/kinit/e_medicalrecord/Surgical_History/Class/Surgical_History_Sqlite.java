package com.e_medicalRecord.kinit.e_medicalrecord.Surgical_History.Class;

public class Surgical_History_Sqlite {
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `history_surgery` (\n" +
            "  `id` INTEGER NOT NULL,\n" +
            "  `patient_id` INTEGER NOT NULL,\n" +
            "  `user_data_id` INTEGER NOT NULL,\n" +
            "  `surgery_title` TEXT NOT NULL,\n" +
            "  `date_performed` datetime NOT NULL\n" +
            ")";

    public static final String TABLE_NAME = "history_surgery";
    public static final String ID = "id";
    public static final String PATIENT_ID = "patient_id";
    public static final String USER_DATA_ID = "user_data_id";
    public static final String SURGERY_TITLE = "surgery_title";
    public static final String DATE_PERFORMED = "date_performed";
}
