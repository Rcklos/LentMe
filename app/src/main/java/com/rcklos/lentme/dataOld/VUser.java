package com.rcklos.lentme.dataOld;

public class VUser {
    /**
     * 用户ID
     * Bmob:ObjectID
     */
    //private Integer UID;
    /**
     * 用户昵称
     * Bmob:Username
     */
    //private String UNAME;
    private String UGender;
    private String UAGE;
    private String UBirth;
    private String UEmail;
    private String UPNUM;
    private String UDistrict;

    public VUser(  String UGender, String UAGE, String UBirth, String UEmail, String UPNUM, String UDistrict) {
        this.UGender = UGender;
        this.UAGE = UAGE;
        this.UBirth = UBirth;
        this.UEmail = UEmail;
        this.UPNUM = UPNUM;
        this.UDistrict = UDistrict;
    }

    public String getUGender() {
        return UGender;
    }

    public void setUGender(String UGender) {
        this.UGender = UGender;
    }

    public String getUAGE() {
        return UAGE;
    }

    public void setUAGE(String UAGE) {
        this.UAGE = UAGE;
    }

    public String getUBirth() {
        return UBirth;
    }

    public void setUBirth(String UBirth) {
        this.UBirth = UBirth;
    }

    public String getUEmail() {
        return UEmail;
    }

    public void setUEmail(String UEmail) {
        this.UEmail = UEmail;
    }

    public String getUPNUM() {
        return UPNUM;
    }

    public void setUPNUM(String UPNUM) {
        this.UPNUM = UPNUM;
    }

    public String getUDistrict() {
        return UDistrict;
    }

    public void setUDistrict(String UDistrict) {
        this.UDistrict = UDistrict;
    }
}
