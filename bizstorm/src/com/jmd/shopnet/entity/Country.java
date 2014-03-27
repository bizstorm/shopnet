package com.jmd.shopnet.entity;

public enum Country {

    India("IN"),
    Singapore("SG"),
    USA("US"),
    UK("UK");

    private final String alpha2;
    private Country(String s) {
    	alpha2 = s;
    }
    public boolean equalsAlpha2(String otherAlpha2){
        return (otherAlpha2 == null)? false:alpha2.equals(otherAlpha2);
    }
    public String toString(){
       return alpha2;
    }
}
