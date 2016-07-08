package com.zhy.vdh;

public class MenuItem {
    boolean isSelected;
    String text;
    int icon;
    int iconSelected;

    public MenuItem(String text, boolean isSelected, int icon, int iconSelected) {
        this.text = text;
        this.isSelected = isSelected;
        this.icon = icon;
        this.iconSelected = iconSelected;
    }

}