package com.eric.mtd.game.model.ai;
//QUESTION: Bad to do this?
public enum TowerTargetPriority {
    FIRST(0),
    LAST(1),
    WEAKEST(2),
    STRONGEST(3);
	
    private int position;
    TowerTargetPriority(int pos){
    	position = pos;
    }
    public int getPosition(){
    	return position;
    }
}