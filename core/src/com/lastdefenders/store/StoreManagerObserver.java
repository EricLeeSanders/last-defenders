package com.lastdefenders.store;

import com.badlogic.gdx.pay.Transaction;
import com.lastdefenders.store.StoreManager.PurchasableItem;

public interface StoreManagerObserver {
    void handlePurchase(PurchasableItem purchasableItem);
    void purchaseManagerInstalled(boolean installed);
}
