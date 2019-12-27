package com.bzgroup.pitboxauxiliovehicular.menu;

import com.bzgroup.pitboxauxiliovehicular.entities.Categorie;
import com.bzgroup.pitboxauxiliovehicular.menu.event.CategorieEvent;

public interface ICategoriesPresenter {
    void onCreate();

    void onDestroy();

    void handleCategories();

    void onEventMainThread(CategorieEvent categorieEvent);
}
