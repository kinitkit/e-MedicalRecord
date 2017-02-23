package com.e_medicalRecord.kinit.e_medicalrecord.Allergy.Class;

public class Allergy_Sqlite {
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `allergy` (\n"+
            "  `id` integer NOT NULL,\n"+
            "  `patient_id` integer NOT NULL,\n"+
            "  `fr` text NOT NULL,\n"+
            "  `reaction` text NOT NULL,\n"+
            "  `treatment` text NOT NULL\n"+
            ")";

    public static final String TABLE_NAME = "allergy";
    public static final String ID = "id";
    public static final String PATIENT_ID = "patient_id";
    public static final String FR = "fr";
    public static final String REACTION = "reaction";
    public static final String TREATMENT = "treatment";

}
