package com.example.kinit.e_medicalrecord.Classes.Past_Medical_History;

public class Past_Medical_Histor {

    private String[] keys = {"adhd", "adhd_yr", "anesthesia_problem", "anesthesia_problem_yr", "anxiety_panic_attacks", "anxiety_panic_attacks_yr",
            "arthritis", "arthritis_yr", "asthma", "asthma_yr", "birth_defects", "birth_defects_yr" , "blood_problem", "blood_problem_yr",
            "bone", "bone_yr", "breast_disease", "breast_disease_yr", "cancer_breast", "cancer_breast_yr", "cancer_colon", "cancer_colon_yr",
            "cancer_malenoma", "cancer_malenoma_yr", "cancer_ovary", "cancer_ovary_yr", "cancer_prostate", "cancer_prostate_yr",
            "chicken_pox", "chicken_pox_yr", "colon_problems", "colon_problems_yr", "depression", "depression_yr", "diabetes_1", "diabetes_1_yr",
            "diabetes_2", "diabetes_2_yr", "digestive_tract_problem", "digestive_tract_problem_yr", "eating_disorder", "eating_disorder_yr",
            "eczema", "eczema_yr", "ent", "ent_yr", "epilepsy", "epilepsy_yr", "fertility_problems", "fertility_problems_yr",
            "gallbladder", "gallbladder_yr", "gynecology_problems", "gynecology_problems_yr", "hay_fever", "hay_fever_yr", "migrane", "migrane_yr",
            "hearing_problem", "hearing_problem_yr", "heart_attack", "heart_attack_yr", "heart_murmur", "heart_murmur_yr",
            "heart_problem", "heart_problem_yr", "hepatitis", "hepatitis_yr", "hypertension", "hypertension_yr", "hyperlipidemia", "hyperlipidemia_yr"};
    private String[] medicalCondition_header = { "Attention Deficit Disorders", "Anesthesia Problem", "Anxiety / Panic Attacks", "Arthritis",
            "Asthma", "Birth Defects", "Blood Problem / Clotting Disorder", "Bone / Joint Problems", "Breast Disease / Lumps (Benign)",
            "Cancer, Breast", "Cancer, Colon", "Cancer, Malenoma", "Cancer, Ovary", "Cancer, Prostate", "Chicken Pox",
            "Colitis or Colon Problems", "Depression (of a Serious State)", "Diabetes, Type 1 (Childhood Onset)",
            "Diabetes, Type 2 (Adult Onset)", "Digestive Tract Problem", "Eating Disorders", "Eczema", "Ear / Nose / Throat Problems",
            "Epilepsy (Convultions or Seizures)", "Fertility (Conception) Problems", "Gallbladder or Gallstones",
            "Gynecology Problems", "Hay Fever", "Headaches: Migranes or Frequent", "Hearing Problems: Loss", "Heart Attack", "Heart Murmur",
            "Heart Problem", "Hepatitis A, B, or C", "High Blood Pressure (Hypertension)", "High Cholesterol (Hyperlipidemia)"};
    private boolean[] medicalCondition_data;
    private int[] year;
    public Past_Medical_Histor() {
        this.medicalCondition_data = new boolean[keys.length/2];
        this.year = new int[keys.length/2];
    }

    public String getKey(int position){
        return this.keys[position];
    }

    public Boolean getMedicalCondition_data(int position) {
        return medicalCondition_data[position];
    }

    public void setMedicalCondition_data(int position, boolean header) {
        this.medicalCondition_data[position] = header;
    }

    public String getMedicalCondition_header(int position) {
        return medicalCondition_header[position];
    }

    public int getYear(int position) {
        return year[position];
    }

    public void setYear(int position, int year) {
        this.year[position] = year;
    }

    public int length(){
        return medicalCondition_header.length;
    }
}
