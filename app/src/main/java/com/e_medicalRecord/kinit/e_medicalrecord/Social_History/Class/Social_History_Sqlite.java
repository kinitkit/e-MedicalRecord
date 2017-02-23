package com.e_medicalRecord.kinit.e_medicalrecord.Social_History.Class;

public class Social_History_Sqlite {
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `history_social` (\n" +
            "  `id` INTEGER NOT NULL,\n" +
            "  `patient_id` INTEGER NOT NULL,\n" +
            "  `user_data_id` INTEGER NOT NULL,\n" +
            "  `substance` TEXT NOT NULL,\n" +
            "  `currently_use` INTEGER NOT NULL,\n" +
            "  `previously_used` INTEGER NOT NULL,\n" +
            "  `frequency` TEXT NOT NULL,\n" +
            "  `length` INTEGER NOT NULL,\n" +
            "  `stopped_year` INTEGER NOT NULL DEFAULT '0'\n" +
            ")";

    public static final String TABLE_NAME = "history_social";
    public static final String ID = "id";
    public static final String PATIENT_ID = "patient_id";
    public static final String USER_DATA_ID = "user_data_id";
    public static final String SUBSTANCE = "substance";
    public static final String CURRENTLY_USE = "currently_use";
    public static final String PREVIOUSLY_USED = "previously_used";
    public static final String FREQUENCY = "frequency";
    public static final String LENGTH = "length";
    public static final String STOPPED_YEAR = "stopped_year";
}
