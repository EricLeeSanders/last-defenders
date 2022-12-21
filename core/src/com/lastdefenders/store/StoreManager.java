package com.lastdefenders.store;

import com.badlogic.gdx.pay.Offer;
import com.badlogic.gdx.pay.OfferType;
import com.badlogic.gdx.pay.PurchaseManager;
import com.badlogic.gdx.pay.PurchaseManagerConfig;
import com.badlogic.gdx.pay.PurchaseObserver;
import com.badlogic.gdx.pay.Transaction;
import com.badlogic.gdx.utils.SnapshotArray;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.UserPreferences;

public class StoreManager implements PurchaseObserver  {

    private PurchaseManager gdxPurchaseManager;
    private UserPreferences userPreferences;
    private SnapshotArray<StoreManagerObserver> observers = new SnapshotArray<>();

    public StoreManager(PurchaseManager gdxPurchaseManager, UserPreferences userPreferences){
        this.gdxPurchaseManager = gdxPurchaseManager;
        this.userPreferences = userPreferences;
        installPurchaseManager();
    }

    private void notifyObserversOfInstallationStatus(boolean installed){
        for(StoreManagerObserver observer : observers){
            observer.purchaseManagerInstalled(installed);
        }
    }

    private void notifyObserversOfPurchase(PurchasableItem purchasableItem){
        for(StoreManagerObserver observer : observers){
            observer.handlePurchase(purchasableItem);
        }
    }

    public void purchaseItem(PurchasableItem item){
        this.gdxPurchaseManager.purchase(item.getSku());
    }


    public void addObserver(StoreManagerObserver observer){
        this.observers.add(observer);
    }

    public boolean installed(){
        return gdxPurchaseManager.installed();
    }

    @Override
    public void handleInstall() {
        Logger.info("StoreManager: Purchase Manager Installation Successful");
        notifyObserversOfInstallationStatus(true);
    }

    @Override
    public void handleInstallError(Throwable e) {
        Logger.error("StoreManager: Purchase Manager Installation Unsuccessful", e);
        notifyObserversOfInstallationStatus(false);
    }

    @Override
    public void handleRestore(Transaction[] transactions) {
        Logger.info("StoreManager: handleRestore");
        for(Transaction transaction : transactions){
            PurchasableItem purchasableItem = PurchasableItem.getPurchasableItemFromSku(transaction.getIdentifier());
            notifyObserversOfPurchase(purchasableItem);
        }
    }

    @Override
    public void handleRestoreError(Throwable e) {
        Logger.error("StoreManager: handleRestoreError", e);
    }

    @Override
    public void handlePurchase(Transaction transaction) {
        Logger.info("StoreManager: handlePurchase - " + transaction);
        PurchasableItem purchasableItem = PurchasableItem.getPurchasableItemFromSku(transaction.getIdentifier());
        notifyObserversOfPurchase(purchasableItem);

        if(purchasableItem.equals(PurchasableItem.NO_ADS)){
            handleNoAdsPurchase();
        } else {
            Logger.error("Invalid Purchase: " + transaction);
        }

    }

    @Override
    public void handlePurchaseError(Throwable e) {
        Logger.error("StoreManager: handlePurchaseError", e);
    }

    @Override
    public void handlePurchaseCanceled() {
        Logger.info("StoreManager: handlePurchaseCanceled");
    }

    private void handleNoAdsPurchase(){
        userPreferences.setAdRemovalPurchased(true);
    }

    private void installPurchaseManager(){
        PurchaseManagerConfig pmc = new PurchaseManagerConfig();
        pmc.addOffer(new Offer().setType(OfferType.ENTITLEMENT).setIdentifier(PurchasableItem.NO_ADS.getSku()));

        gdxPurchaseManager.install(this, pmc, true);

    }

    public boolean isAdsRemovalPurchasable(){
        return this.installed() & !this.userPreferences.getAdRemovalPurchased();
    }


    public enum PurchasableItem {

        NO_ADS("no_ads");

        private final String sku;

        PurchasableItem(String sku){
            this.sku = sku;
        }

        public String getSku(){
            return sku;
        }

        public static PurchasableItem getPurchasableItemFromSku(String sku){
            for(PurchasableItem item : PurchasableItem.values()){
                if(item.getSku().equals(sku)){
                    return item;
                }
            }

            throw new IllegalArgumentException("No PurchasableItem for sku: " + sku);
        }


    }
}
