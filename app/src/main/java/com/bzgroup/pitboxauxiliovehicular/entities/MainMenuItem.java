package com.bzgroup.pitboxauxiliovehicular.entities;

import android.graphics.drawable.Drawable;

public class MainMenuItem {
    private Drawable image;
    private String text;

    public MainMenuItem(Drawable image, String text) {
        this.image = image;
        this.text = text;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
