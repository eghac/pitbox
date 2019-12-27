package com.bzgroup.pitboxauxiliovehicular.menu.event;

import com.bzgroup.pitboxauxiliovehicular.entities.Categorie;

import java.util.List;

public class CategorieEvent {
    public final static int CATEGORIES_SUCCESS = 0;
    public final static int CATEGORIES_EMPTY = 1;
    public final static int CATEGORIES_ERROR = 2;

    private int eventType;
    private String errorMessage;
    private List<Categorie> categories;

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<Categorie> getCategories() {
        return categories;
    }

    public void setCategories(List<Categorie> categories) {
        this.categories = categories;
    }
}
