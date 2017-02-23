package com.e_medicalRecord.kinit.e_medicalrecord.Laboratory.Class;

public class Laboratory_Sqlite {
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `laboratory_result` (\n"+
            "  `lab_id` INTEGER NOT NULL,\n"+
            "  `patient_id` INTEGER NOT NULL,\n"+
            "  `user_data_id` INTEGER NOT NULL,\n"+
            "  `physician_name` TEXT NOT NULL,\n"+
            "  `pathologist` TEXT NOT NULL,\n"+
            "  `med_tech` TEXT NOT NULL,\n"+
            "  `lab_name` TEXT NOT NULL,\n"+
            "  `date_performed` datetime NOT NULL,\n"+
            "  `remarks` TEXT NOT NULL,\n"+
            "  `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n"+
            "  `status` INTEGER NOT NULL\n"+
            ")";

    public static final String TABLE_NAME = "laboratory_result";
    public static final String LAB_ID = "lab_id";
    public static final String PATIENT_ID = "patient_id";
    public static final String USER_DATA_ID = "user_data_id";
    public static final String PHYSICIAN_NAME = "physician_name";
    public static final String PATHOLOGIST = "pathologist";
    public static final String MED_TECH = "med_tech";
    public static final String LAB_NAME = "lab_name";
    public static final String DATE_PERFORMED = "date_performed";
    public static final String REMARKS = "remarks";
    public static final String DATE = "date";
    public static final String STATUS = "status";


}
