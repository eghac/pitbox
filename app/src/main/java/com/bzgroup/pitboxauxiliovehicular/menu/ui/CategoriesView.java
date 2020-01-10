package com.bzgroup.pitboxauxiliovehicular.menu.ui;

import com.bzgroup.pitboxauxiliovehicular.entities.Categorie;

import java.util.List;

public interface CategoriesView {
    void showProgress();

    void hideProgress();

    void providerCategories(List<Categorie> categories);

    void providerCategoriesEmpty();

    void showCategoriesErrorMessage(String errorMessage);
}
