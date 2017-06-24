package com.foxholedefense.state;

import com.badlogic.gdx.utils.SnapshotArray;
import com.foxholedefense.util.Logger;

/**
 * Created by Eric on 5/24/2017.
 */

public abstract class ObservableStateManager<S, O extends StateObserver>
{
    private S state;
    private SnapshotArray<O> observers = new SnapshotArray<>();

    /**
     * Attach an observer and add it to observers list.
     *
     * @param observer
     */
    public void attach(O observer) {
        observers.add(observer);
    }

    /**
     * Notify all observers of state change
     */
    @SuppressWarnings("unchecked")
    private void notifyObservers() {
        Logger.info(getClass().getSimpleName() + ": Notify Observers");
        Object[] objects = observers.begin();
        for(int i = observers.size - 1; i >= 0; i--){
            O observer = (O) objects[i];
            Logger.info(getClass().getSimpleName()  + ": " + observer.getClass().getName());
            notifyObserver(observer, state);
        }
        observers.end();
    }

    /**
     * Abstract method to notify an observer. We can't add a changeState method to the
     * StateObserver interface with generics because some observers may observer
     * more than one observable. this would cause a compile error because you can't
     * implement an interface more than once, even with a  different type (with generics)
     * due to type erasure.
     * @param observer
     * @param state
     */
    protected abstract void notifyObserver(O observer, S state);

    /**
     * Set the state of the game
     *
     * @param state
     */
    public void setState(S state) {
        Logger.info("Changing " + getClass().getSimpleName() + ": " + this.getState() + " to state: " + state);
        this.state = state;
        notifyObservers();
    }

    public S getState() {
        return state;
    }
}
