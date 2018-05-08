package com.scorg.dms.bottom_menus;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class BottomMenu implements Parcelable {

    private String menuName = "";
    private Drawable menuIcon;
    private boolean isSelected;
    private boolean isAppIcon;
    private int notificationCount;
    private String iconImageUrl = "";

    public final static Creator<BottomMenu> CREATOR = new Creator<BottomMenu>() {


        @SuppressWarnings({
                "unchecked"
        })
        public BottomMenu createFromParcel(Parcel in) {
            return new BottomMenu(in);
        }

        public BottomMenu[] newArray(int size) {
            return (new BottomMenu[size]);
        }

    }
            ;

    protected BottomMenu(Parcel in) {
        this.menuName = ((String) in.readValue((String.class.getClassLoader())));
        this.menuIcon = ((Drawable) in.readValue((String.class.getClassLoader())));
        this.isSelected = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.isAppIcon = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.notificationCount = ((int) in.readValue((boolean.class.getClassLoader())));
        this.iconImageUrl = (String) in.readValue(String.class.getClassLoader());
    }

    public BottomMenu() {
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Drawable getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(Drawable menuIcon) {
        this.menuIcon = menuIcon;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isAppIcon() {
        return isAppIcon;
    }

    public void setAppIcon(boolean appIcon) {
        isAppIcon = appIcon;
    }

    public int getNotificationCount() {
        return notificationCount;
    }

    public void setNotificationCount(int notificationCount) {
        this.notificationCount = notificationCount;
    }
    public String getIconImageUrl() {
        return iconImageUrl;
    }

    public void setIconImageUrl(String iconImageUrl) {
        this.iconImageUrl = iconImageUrl;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(menuName);
        dest.writeValue(menuIcon);
        dest.writeValue(isSelected);
        dest.writeValue(isAppIcon);
        dest.writeValue(notificationCount);
        dest.writeValue(iconImageUrl);
    }

    public int describeContents() {
        return 0;
    }

}