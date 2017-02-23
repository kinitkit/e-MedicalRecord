package com.e_medicalRecord.kinit.e_medicalrecord.Past_Medical_History.Class;

public class Past_Medical_History_Sqlite {
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `history_past_medical` (\n" +
            "  `id` INTEGER NOT NULL,\n" +
            "  `patient_id` INTEGER NOT NULL,\n" +
            "  `user_data_id` INTEGER NOT NULL,\n" +
            "  `medical_condition` TEXT NOT NULL,\n" +
            "  `date_time` datetime NOT NULL\n" +
            ")";

    public static final String TABLE_NAME = "history_past_medical";
    public static final String ID = "id";
    public static final String PATIENT_ID = "patient_id";
    public static final String USER_DATA_ID = "user_data_id";
    public static final String MEDICAL_CONDITION = "medical_condition";
    public static final String DATE_TIME = "date_time";
}
