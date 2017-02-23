package com.e_medicalRecord.kinit.e_medicalrecord.Admission.Class;

public class Admission_Sqlite {

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `admission` (\n" +
            "  `id` integer NOT NULL,\n" +
            "  `patient_id` integer NOT NULL,\n" +
            "  `user_data_id` integer NOT NULL,\n" +
            "  `physician_name` text NOT NULL,\n" +
            "  `hospital` text NOT NULL,\n" +
            "  `date_admitted` datetime NOT NULL,\n" +
            "  `date_discharged` datetime NOT NULL,\n" +
            "  `admitting_impression` text NOT NULL,\n" +
            "  `procedures` text NOT NULL,\n" +
            "  `future_plan` text NOT NULL,\n" +
            "  `final_diagnosis` text NOT NULL,\n" +
            "  `status` integer NOT NULL DEFAULT '1'\n" +
            ")";

    public static final String TABLE_NAME = "admission";
    public static final String ID = "id";
    public static final String PATIENT_ID = "patient_id";
    public static final String USER_DATA_ID = "user_data_id";
    public static final String PHYSICIAN_NAME = "physician_name";
    public static final String HOSPITAL = "hospital";
    public static final String DATE_ADMITTED = "date_admitted";
    public static final String DATE_DISCHARGED = "idate_dischargedd";
    public static final String ADMITTING_IMPRESSION = "iadmitting_impressiond";
    public static final String PROCEDURES = "procedures";
    public static final String FUTURE_PLAN = "future_plan";
    public static final String FINAL_DIAGNOSIS = "final_diagnosis";
    public static final String DATE_INSERTED = "date_inserted";
    public static final String STATUS = "status";
}
