package com.rescribe.doctor.model.my_appointments;

/**
 * Created by jeetal on 14/2/18.
 */

public class FilterSortByHighLowList {

    private String amountHighOrLow;

    private boolean selected;


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getAmountHighOrLow() {
        return amountHighOrLow;
    }

    public void setAmountHighOrLow(String amountHighOrLow) {
        this.amountHighOrLow = amountHighOrLow;
    }

}
