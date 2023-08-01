package com.lastdefenders.game.service.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.support.AirStrike;
import com.lastdefenders.game.model.actor.support.SupportActor;
import com.lastdefenders.game.model.actor.support.SupportActorCooldown;
import org.junit.jupiter.api.Test;

public class SupportActorValidatorTest {

    private Player player = mock(Player.class);


    private static SupportActorValidator createSupportActorValidator(Class<? extends SupportActor> clazz, Player player) {
        Integer cooldownTime = 0;
        try {
            cooldownTime = (Integer) clazz.getDeclaredField("COOLDOWN_TIME").get(null);
        } catch(Exception e){
            System.out.println(e);
        }

        SupportActorCooldown cooldown = new SupportActorCooldown(cooldownTime);

        return createSupportActorValidator(clazz, player, cooldown);
    }

    private static SupportActorValidator createSupportActorValidator(Class<? extends SupportActor> clazz, Player player, SupportActorCooldown cooldown) {
        Integer cost = 0;
        try {
            cost = (Integer) clazz.getDeclaredField("COST").get(null);
        } catch(Exception e){
            System.out.println(e);
        }

        SupportActorValidator validator = new SupportActorValidator(cost, cooldown, player);

        return validator;
    }

    @Test
    public void onCooldownTest(){
        // Player setup
        doReturn(true).when(player).hasEnoughMoney(any(Integer.class));

        // Cooldown setup
        SupportActorCooldown cooldown = mock(SupportActorCooldown.class);
        doReturn(true).when(cooldown).isOnCooldown();

        SupportActorValidator validator = createSupportActorValidator(AirStrike.class, player, cooldown);

        ValidationResponseEnum response = validator.canCreateSupportActor();

        assertEquals(ValidationResponseEnum.ON_COOLDOWN, response);

    }

    @Test
    public void insufficientMoneyTest(){
        // Player setup
        doReturn(false).when(player).hasEnoughMoney(any(Integer.class));

        // Cooldown setup
        SupportActorCooldown cooldown = mock(SupportActorCooldown.class);
        doReturn(false).when(cooldown).isOnCooldown();

        SupportActorValidator validator = createSupportActorValidator(AirStrike.class, player, cooldown);

        ValidationResponseEnum response = validator.canCreateSupportActor();

        assertEquals(ValidationResponseEnum.INSUFFICIENT_MONEY, response);

    }

    @Test
    public void okTest(){
        // Player setup
        doReturn(true).when(player).hasEnoughMoney(any(Integer.class));

        // Cooldown setup
        SupportActorCooldown cooldown = mock(SupportActorCooldown.class);
        doReturn(false).when(cooldown).isOnCooldown();

        SupportActorValidator validator = createSupportActorValidator(AirStrike.class, player, cooldown);

        ValidationResponseEnum response = validator.canCreateSupportActor();

        assertEquals(ValidationResponseEnum.OK, response);

    }
}
