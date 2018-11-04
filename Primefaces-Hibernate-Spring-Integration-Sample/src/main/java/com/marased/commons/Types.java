/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marased.commons;

/**
 *
 * @author mahmoudyahia
 */
public class Types {

    private int TypeId;
    private String TypeName;

    /**
     * @return the TypeId
     */
    public int getTypeId() {
        return TypeId;
    }

    public Types() {
    }

    public Types(int meetingTypeIdParam, String meetingTypeNameParam) {
        this.TypeId = meetingTypeIdParam;
        this.TypeName = meetingTypeNameParam;
    }

    /**
     * @param TypeId the TypeId to set
     */
    public void setTypeId(int TypeId) {
        this.TypeId = TypeId;
    }

    /**
     * @return the TypeName
     */
    public String getTypeName() {
        return TypeName;
    }

    /**
     * @param TypeName the TypeName to set
     */
    public void setTypeName(String TypeName) {
        this.TypeName = TypeName;
    }
}
