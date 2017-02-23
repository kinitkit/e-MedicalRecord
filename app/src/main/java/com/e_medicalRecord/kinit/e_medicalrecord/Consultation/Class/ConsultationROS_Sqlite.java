package com.e_medicalRecord.kinit.e_medicalrecord.Consultation.Class;

public class ConsultationROS_Sqlite {
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `consultation_ros` (\n"+
            "  `consultation_ros_id` INTEGER NOT NULL,\n"+
            "  `consultation_id` INTEGER NOT NULL,\n"+
            "  `ros` INTEGER NOT NULL,\n"+
            "  `remarks` TEXT NOT NULL\n"+
            ")";

    public static final String TABLE_NAME = "consultation_ros";
    public static final String CONSULTATION_ROS_ID = "consultation_ros_id";
    public static final String CONSULTATION_ID = "consultation_id";
    public static final String ROS = "ros";
    public static final String REMARKS = "remarks";
}
