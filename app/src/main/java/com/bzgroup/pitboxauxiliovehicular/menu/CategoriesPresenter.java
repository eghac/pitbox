package com.bzgroup.pitboxauxiliovehicular.menu;

import android.content.Context;
import android.util.Log;

import com.bzgroup.pitboxauxiliovehicular.entities.Categorie;
import com.bzgroup.pitboxauxiliovehicular.lib.EventBus;
import com.bzgroup.pitboxauxiliovehicular.lib.GreenRobotEventBus;
import com.bzgroup.pitboxauxiliovehicular.menu.event.CategorieEvent;
import com.bzgroup.pitboxauxiliovehicular.menu.ui.CategoriesView;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class CategoriesPresenter implements ICategoriesPresenter {

    private CategoriesView mView;
    private ICategoriesRepository mRepository;
    private EventBus eventBus;

    public CategoriesPresenter(Object object, CategoriesView view) {
        this.mView = view;
        this.eventBus = GreenRobotEventBus.getInstance();
        this.mRepository = new CategoriesRepository((Context) object);
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        mView = null;
        eventBus.unregister(this);
    }

    @Override
    public void handleCategories() {
        if (mView != null)
            mView.showProgress();
        mRepository.handleCategories();
    }

    @Subscribe
    @Override
    public void onEventMainThread(CategorieEvent categorieEvent) {
        switch (categorieEvent.getEventType()) {
            case CategorieEvent.CATEGORIES_SUCCESS:
                categoriesSuccess(categorieEvent.getCategories());
                break;
            case CategorieEvent.CATEGORIES_ERROR:
                categoriesError(categorieEvent.getErrorMessage());
                break;
            case CategorieEvent.CATEGORIES_EMPTY:
                categoriesEmpty();
                break;
        }
    }

    private void categoriesError(String errorMessage) {
        if (mView != null) {
            mView.hideProgress();
            mView.showCategoriesErrorMessage(errorMessage);
        }
    }

    private void categoriesEmpty() {
        if (mView != null) {
            mView.hideProgress();
            mView.providerCategoriesEmpty();
        }
    }

    private void categoriesSuccess(List<Categorie> categories) {
        if (mView != null) {
            mView.hideProgress();
            mView.providerCategories(categories);
        }
    }
}
