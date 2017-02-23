package com.e_medicalRecord.kinit.e_medicalrecord.Medical_Prescription.Class;

public class Drug_List_Sqlite {
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `drug_list` (\n" +
            "  `id` INTEGER NOT NULL,\n" +
            "  `medical_prescription_id` INTEGER NOT NULL,\n" +
            "  `drug` TEXT NOT NULL,\n" +
            "  `strength` TEXT NOT NULL,\n" +
            "  `dosage` TEXT NOT NULL,\n" +
            "  `route` TEXT NOT NULL,\n" +
            "  `frequency` TEXT NOT NULL,\n" +
            "  `indication` TEXT DEFAULT NULL,\n" +
            "  `how_many` INTEGER NOT NULL DEFAULT '0'\n" +
            ")";

    public static final String TABLE_NAME = "drug_list";
    public static final String ID = "id";
    public static final String MEDICAL_PRESCRIPTION_ID = "medical_prescription_id";
    public static final String DRUG = "drug";
    public static final String STRENGTH = "strength";
    public static final String DOSAGE = "dosage";
    public static final String ROUTE = "route";
    public static final String FREQUENCY = "frequency";
    public static final String INDICATION = "indication";
    public static final String HOW_MANY = "how_many";

}
