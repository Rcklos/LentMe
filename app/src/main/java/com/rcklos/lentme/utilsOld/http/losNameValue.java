package com.rcklos.lentme.utilsOld.http;

import java.util.ArrayList;

public class losNameValue {
    private String name , value;

    public losNameValue(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String get(){
        return name + "=" + value;
    }

    public static String getPara(ArrayList<losNameValue> losNameValues){
        StringBuffer sb = new StringBuffer();
        for(int i = 0 ; i < losNameValues.size() ; i++ ){
            if ( i == 0 ){
                sb.append("?");
            } else {
                sb.append("&");
            }
            sb.append(losNameValues.get(i).get());
        }
        return sb.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
