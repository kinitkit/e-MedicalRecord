package com.e_medicalRecord.kinit.e_medicalrecord.General.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.e_medicalRecord.kinit.e_medicalrecord.Admission.Class.Admission_Sqlite;
import com.e_medicalRecord.kinit.e_medicalrecord.Allergy.Class.Allergy_Sqlite;
import com.e_medicalRecord.kinit.e_medicalrecord.Consultation.Class.ConsultationROS_Sqlite;
import com.e_medicalRecord.kinit.e_medicalrecord.Consultation.Class.Consultation_Sqlite;
import com.e_medicalRecord.kinit.e_medicalrecord.Family_History.Class.Family_History_Sqlite;
import com.e_medicalRecord.kinit.e_medicalrecord.Laboratory.Class.Laboratory_Sqlite;
import com.e_medicalRecord.kinit.e_medicalrecord.Medical_Prescription.Class.Drug_List_Sqlite;
import com.e_medicalRecord.kinit.e_medicalrecord.Past_Medical_History.Class.Past_Medical_History_Sqlite;
import com.e_medicalRecord.kinit.e_medicalrecord.Social_History.Class.Social_History_Sqlite;
import com.e_medicalRecord.kinit.e_medicalrecord.Surgical_History.Class.Surgical_History_Sqlite;
import com.e_medicalRecord.kinit.e_medicalrecord.Vaccination.Class.Vaccination_Sqlite;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "bsitcaps_emrdb.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Admission_Sqlite.CREATE_TABLE);
        db.execSQL(Allergy_Sqlite.CREATE_TABLE);
        db.execSQL(Consultation_Sqlite.CREATE_TABLE);
        db.execSQL(ConsultationROS_Sqlite.CREATE_TABLE);
        db.execSQL(Drug_List_Sqlite.CREATE_TABLE);
        db.execSQL(Family_History_Sqlite.CREATE_TABLE);
        db.execSQL(Past_Medical_History_Sqlite.CREATE_TABLE);
        db.execSQL(Social_History_Sqlite.CREATE_TABLE);
        db.execSQL(Surgical_History_Sqlite.CREATE_TABLE);
        db.execSQL(Vaccination_Sqlite.CREATE_TABLE);
        db.execSQL(Laboratory_Sqlite.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Admission_Sqlite.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Allergy_Sqlite.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Consultation_Sqlite.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ConsultationROS_Sqlite.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Drug_List_Sqlite.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Family_History_Sqlite.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Past_Medical_History_Sqlite.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Social_History_Sqlite.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Surgical_History_Sqlite.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Laboratory_Sqlite.TABLE_NAME);
    }
}
