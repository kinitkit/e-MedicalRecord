package com.example.kinit.e_medicalrecord.Consultation.Bus;

import com.example.kinit.e_medicalrecord.Consultation.Class.Consultation_ROS;

public class Bus_ConsultationRos_OnLongClick {
    public int position;
    public Consultation_ROS consultationRos;

    public Bus_ConsultationRos_OnLongClick(int position, Consultation_ROS consultationRos) {
        this.position = position;
        this.consultationRos = consultationRos;
    }
}
