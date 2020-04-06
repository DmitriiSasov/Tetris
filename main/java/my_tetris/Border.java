/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my_tetris;

import java.awt.Point;

/**
 *
 * @author bopoh
 */
public class Border {
    
    private int minRowIndex;
    
    private int minColIndex;
    
    private int maxColIndex;

    public Border(int minRowIndex, int minColIndex,
            int maxColIndex) {
        
        if (maxColIndex - minColIndex <= 0) {
            
            throw new BorderParametersException("Границы заданы неверно");
        }
        
        this.minRowIndex = minRowIndex;
        this.minColIndex = minColIndex;
        this.maxColIndex = maxColIndex;
    }
  
    public boolean isPositionOutsideBorder(Point position) {
        
        return position.getX() > maxColIndex || position.getX() < minColIndex || position.getY() < minRowIndex;
    }

    public class BorderParametersException extends IllegalArgumentException {

        public BorderParametersException(String s) { super(s); }
    }

}
