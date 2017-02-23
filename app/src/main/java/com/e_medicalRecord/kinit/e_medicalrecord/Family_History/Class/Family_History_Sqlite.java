package com.e_medicalRecord.kinit.e_medicalrecord.Family_History.Class;

public class Family_History_Sqlite {
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `history_family` (\n" +
            "  `id` INTEGER NOT NULL,\n" +
            "  `patient_id` INTEGER NOT NULL,\n" +
            "  `user_data_id` INTEGER NOT NULL,\n" +
            "  `medical_condition` TEXT NOT NULL,\n" +
            "  `grandP` INTEGER NOT NULL DEFAULT '0',\n" +
            "  `parent` INTEGER NOT NULL DEFAULT '0',\n" +
            "  `sibling` INTEGER NOT NULL DEFAULT '0',\n" +
            "  `child` INTEGER NOT NULL DEFAULT '0'\n" +
            ")";

    public static final String TABLE_NAME = "history_family";
    public static final String ID = "id";
    public static final String PATIENT_ID = "patient_id";
    public static final String USER_DATA_ID = "user_data_id";
    public static final String MEDICAL_CONDITION = "medical_condition";
    public static final String GRANDP = "grandP";
    public static final String PARENT = "parent";
    public static final String SIBLING = "sibling";
    public static final String CHILD = "child";

}
