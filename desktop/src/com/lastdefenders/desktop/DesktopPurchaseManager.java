package com.lastdefenders.desktop;

import com.badlogic.gdx.pay.Information;
import com.badlogic.gdx.pay.PurchaseManager;
import com.badlogic.gdx.pay.PurchaseManagerConfig;
import com.badlogic.gdx.pay.PurchaseObserver;

public class DesktopPurchaseManager implements PurchaseManager {

    @Override
    public String storeName() {

        return null;
    }

    @Override
    public void install(PurchaseObserver observer, PurchaseManagerConfig config,
        boolean autoFetchInformation) {

    }

    @Override
    public boolean installed() {

        return false;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void purchase(String identifier) {

    }

    @Override
    public void purchaseRestore() {

    }

    @Override
    public Information getInformation(String identifier) {

        return null;
    }
}
