package com.example.kinit.e_medicalrecord.Family_History.Bus;

public class Bus_Family_History_Item {
    boolean isGrandP, isParent, isSibling, isChild;
    String key_grandP, key_parent, key_sibling, key_child;
    int patient_id;

    public Bus_Family_History_Item(int patient_id, boolean isGrandP, boolean isParent, boolean isSibling, boolean isChild, String key_grandP,
                                   String key_parent, String key_sibling, String key_child) {
        this.patient_id = patient_id;
        this.isGrandP = isGrandP;
        this.isParent = isParent;
        this.isSibling = isSibling;
        this.isChild = isChild;
        this.key_grandP = key_grandP;
        this.key_parent = key_parent;
        this.key_sibling = key_sibling;
        this.key_child = key_child;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public boolean isGrandP() {
        return isGrandP;
    }

    public boolean isParent() {
        return isParent;
    }

    public boolean isSibling() {
        return isSibling;
    }

    public boolean isChild() {
        return isChild;
    }

    public String getKey_grandP() {
        return key_grandP;
    }

    public String getKey_parent() {
        return key_parent;
    }

    public String getKey_sibling() {
        return key_sibling;
    }

    public String getKey_child() {
        return key_child;
    }
}
