/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my_tetris.events;

import java.util.EventObject;

/**
 *
 * @author bopoh
 */
public class HorizontalRowEvent extends EventObject{
    
    public HorizontalRowEvent(Object o) {
        
        super(o);
    }
    
    private int score = 0;
    
    public void setScore(int score) {
        
        this.score = score;
    }

    public int getScore() {
        
        return score;
    }
}
