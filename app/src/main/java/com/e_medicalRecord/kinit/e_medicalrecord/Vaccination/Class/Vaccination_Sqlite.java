package com.e_medicalRecord.kinit.e_medicalrecord.Vaccination.Class;

public class Vaccination_Sqlite {
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `history_vaccination` (\n" +
            "  `id` INTEGER NOT NULL,\n" +
            "  `patient_id` INTEGER NOT NULL,\n" +
            "  `user_data_id` INTEGER NOT NULL,\n" +
            "  `vaccine_id` INTEGER NOT NULL,\n" +
            "  `provider_name` TEXT NOT NULL,\n" +
            "  `date_taken` datetime NOT NULL,\n" +
            "  `place_taken` TEXT NOT NULL,\n" +
            "  `status` INTEGER NOT NULL DEFAULT '1'\n" +
            ")";

    public static final String TABLE_NAME = "history_vaccination";
    public static final String ID = "id";
    public static final String PATIENT_ID = "patient_id";
    public static final String USER_DATA_ID = "user_data_id";
    public static final String VACCINE_ID = "vaccine_id";
    public static final String PROVIDER_NAME = "provider_name";
    public static final String DATE_TAKEN = "date_taken";
    public static final String PLACE_TAKEN = "place_taken";
    public static final String STATUS = "status";
}
