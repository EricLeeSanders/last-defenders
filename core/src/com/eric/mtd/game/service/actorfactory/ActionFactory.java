package com.eric.mtd.game.service.actorfactory;


import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Pool;
import com.eric.mtd.util.Logger;


public class ActionFactory {
	private static ActionPool<MoveToAction> moveActionPool = new ActionPool<MoveToAction>(MoveToAction.class);
	private static ActionPool<DelayAction> delayActionPool = new ActionPool<DelayAction>(DelayAction.class);
	public static Action loadAction(String type){
		Action action = null;
		if(type.equals("MoveToAction")){
			action =  moveActionPool.obtain();
		}
		else if(type.equals("DelayAction")){
			action =  delayActionPool.obtain();
		}
      return action;
	}
	protected static Action createAction(Class<? extends Action> type){
      Action actionType = null;
      if (type.equals(MoveToAction.class)) {
  		actionType = new MoveToAction();
  		actionType.setPool(moveActionPool);
      } 
      else if (type.equals(DelayAction.class)) {
  		actionType = new DelayAction();
  		actionType.setPool(delayActionPool);
      } 
      else {
          throw new NullPointerException("Action factory couldn't create: " + type.getSimpleName());
      }
      
      if(Logger.DEBUG)System.out.println("Created new " + actionType);
      return actionType;
		
	}
	public static class ActionPool<T extends Action> extends Pool<Action> {
		private final Class<? extends Action> type;
		public ActionPool(Class<? extends Action>type){
			this.type = type;
		}

		@Override
		protected Action newObject() {
			return createAction(type);
		}

	}	
}
