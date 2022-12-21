package com.lastdefenders.store;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.pay.PurchaseManager;
import com.badlogic.gdx.pay.Transaction;
import com.lastdefenders.store.StoreManager.PurchasableItem;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.UserPreferences;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Logger.class})
public class StoreManagerTest {

    @Mock private PurchaseManager gdxPurchaseManager;
    @Mock private UserPreferences userPreferences;

    @InjectMocks private StoreManager storeManager;

    @Before
    public void initStoreManagerTest() {

        PowerMockito.mockStatic(Logger.class);
    }

    @Test
    public void hanldePurchaseNoAdsTest(){

        Transaction noAdsTransaction = new Transaction();
        noAdsTransaction.setIdentifier(PurchasableItem.NO_ADS.getSku());

        storeManager.handlePurchase(noAdsTransaction);

        verify(userPreferences, times(1)).setAdRemovalPurchased(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void hanldePurchaseOtherTest(){

        Transaction noAdsTransaction = new Transaction();
        noAdsTransaction.setIdentifier("SomethingElse");

        storeManager.handlePurchase(noAdsTransaction);

        verify(userPreferences, never()).setAdRemovalPurchased(true);
    }

}
