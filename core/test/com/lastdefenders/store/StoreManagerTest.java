package com.lastdefenders.store;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.pay.PurchaseManager;
import com.badlogic.gdx.pay.Transaction;
import com.lastdefenders.store.StoreManager.PurchasableItem;
import com.lastdefenders.util.UserPreferences;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class StoreManagerTest {

    @Mock private PurchaseManager gdxPurchaseManager;
    @Mock private UserPreferences userPreferences;

    @InjectMocks private StoreManager storeManager;

    private AutoCloseable closeable;
    @BeforeAll
    public static void init() {

        Gdx.app = mock(Application.class);
    }

    @BeforeEach
    public void startMocks() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    public void hanldePurchaseNoAdsTest(){

        Transaction noAdsTransaction = new Transaction();
        noAdsTransaction.setIdentifier(PurchasableItem.NO_ADS.getSku());

        storeManager.handlePurchase(noAdsTransaction);

        verify(userPreferences, times(1)).setAdRemovalPurchased(true);
    }

    @Test
    public void hanldePurchaseOtherTest(){

        Transaction noAdsTransaction = new Transaction();
        noAdsTransaction.setIdentifier("SomethingElse");

        assertThrows(IllegalArgumentException.class,
            () -> storeManager.handlePurchase(noAdsTransaction));

        verify(userPreferences, never()).setAdRemovalPurchased(true);
    }

}
