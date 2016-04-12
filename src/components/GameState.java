/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

/**
 *
 * @author Nick
 */
public enum GameState {
    RUNNING(0),
    PAUSED(1),
    MENU(2);
    
    private final int state;
    GameState(int state){
        this.state = state;
    }
}
