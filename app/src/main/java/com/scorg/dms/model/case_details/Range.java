
package com.scorg.dms.model.case_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;


public class Range implements CustomResponse {

    @SerializedName("min")
    @Expose
    private String min;
    @SerializedName("max")
    @Expose
    private String max;
    @SerializedName("operator")
    @Expose
    private String operator;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("color")
    @Expose
    private String color;

    public String getNameOfVital() {
        return nameOfVital;
    }

    public void setNameOfVital(String nameOfVital) {
        this.nameOfVital = nameOfVital;
    }

    private String nameOfVital;

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
