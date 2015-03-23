package com.tcsorcs.trailsapp.helpers;


public class OrcContact  {

    private String contactName;
    private String phoneNumber;



    private boolean isSelected;


    public OrcContact(String contactName, String phoneNumber) {
        this.contactName = contactName;
        this.phoneNumber = phoneNumber;
        this.isSelected=false;
    }


    public String getContactName() {
        return contactName;
    }

    public String getphoneNumber() {
        return phoneNumber;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

}