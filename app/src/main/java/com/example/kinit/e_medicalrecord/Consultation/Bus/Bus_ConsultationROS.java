package com.example.kinit.e_medicalrecord.Consultation.Bus;

import com.example.kinit.e_medicalrecord.Consultation.Class.Consultation_ROS;
import com.example.kinit.e_medicalrecord.General.Enum.Query_Type;

public class Bus_ConsultationROS {
    public Query_Type queryType;
    public int position;
    public Consultation_ROS consultationRos;

    public Bus_ConsultationROS(Query_Type queryType, int position, Consultation_ROS consultationRos) {
        this.queryType = queryType;
        this.position = position;
        this.consultationRos = consultationRos;
    }
}
