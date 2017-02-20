package com.e_medicalRecord.kinit.e_medicalrecord.General.BusStation;

import com.squareup.otto.Bus;

public class BusStation {
    private static Bus bus = new Bus();

    public static Bus getBus() {
        return bus;
    }
}
