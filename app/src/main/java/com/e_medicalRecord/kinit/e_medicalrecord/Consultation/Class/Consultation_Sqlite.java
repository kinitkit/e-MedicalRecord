package com.e_medicalRecord.kinit.e_medicalrecord.Consultation.Class;

public class Consultation_Sqlite {
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `consultation` (\n" +
            "  `id` INTEGER NOT NULL,\n" +
            "  `patient_id` INTEGER NOT NULL,\n" +
            "  `user_data_id` INTEGER NOT NULL,\n" +
            "  `physician_name` varchar(50) NOT NULL,\n" +
            "  `date_time` datetime NOT NULL,\n" +
            "  `chief_complaint` TEXT NOT NULL,\n" +
            "  `present_illness` TEXT NOT NULL,\n" +
            "  `height` TEXT DEFAULT '0.00',\n" +
            "  `weight` TEXT DEFAULT '0.00',\n" +
            "  `blood_pressure` TEXT DEFAULT NULL,\n" +
            "  `respiration_rate` INTEGER DEFAULT '0',\n" +
            "  `temperature` TEXT DEFAULT '0.0',\n" +
            "  `pulse_rate` INTEGER DEFAULT '0',\n" +
            "  `diagnosis` TEXT NOT NULL,\n" +
            "  `status` INTEGER NOT NULL DEFAULT '1'\n" +
            ")";

    public static final String TABLE_NAME = "consultation";
    public static final String ID = "id";
    public static final String PATIENT_ID = "patient_id";
    public static final String USER_DATA_ID = "user_data_id";
    public static final String PHYSICIAN_NAME = "physician_name";
    public static final String DATE_TIME = "date_time";
    public static final String CHIEF_COMPLAINT = "chief_complaint";
    public static final String PRESENT_ILLNESS = "present_illness";
    public static final String HEIGHT = "height";
    public static final String WEIGHT = "weight";
    public static final String BLOOD_PRESSURE = "blood_pressure";
    public static final String RESPIRATION_RATE = "respiration_rate";
    public static final String TEMPERATURE = "temperature";
    public static final String PULSE_RATE = "pulse_rate";
    public static final String DIAGNOSIS = "diagnosis";
    public static final String STATUS = "status";
}
