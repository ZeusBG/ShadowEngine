/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components.UI.menu;

import java.util.ArrayList;
import render.Renderer;

/**
 *
 * @author Zeus
 */
public class MenuContainer {
    private AbstractButton button;
    private ArrayList<MenuContainer> subMenus;
    private ArrayList<AbstractButton> buttons;

    public MenuContainer(AbstractButton button, ArrayList<MenuContainer> subMenus, ArrayList<AbstractButton> buttons) {
        this.button = button;
        this.subMenus = subMenus;
        this.buttons = buttons;
    }

    public MenuContainer() {
        button = null;
        subMenus = new ArrayList();
        buttons = new ArrayList();
    }

    
    public void addButton(AbstractButton b){
        if(b!=null)
            buttons.add(b);
    }
    
    
    public void render(Renderer r){
        if(button!=null)
            button.render(r);
    }
    
    public void renderMenu(Renderer r){
        for(MenuContainer mc : subMenus){
            mc.render(r);
        }
        for(AbstractButton b : buttons){
            b.render(r);
        }
    }
    
    public AbstractButton getButton() {
        return button;
    }

    public void setButton(AbstractButton button) {
        this.button = button;
    }

    public ArrayList<MenuContainer> getSubMenus() {
        return subMenus;
    }

    public void setSubMenus(ArrayList<MenuContainer> subMenus) {
        this.subMenus = subMenus;
    }

    public ArrayList<AbstractButton> getButtons() {
        return buttons;
    }

    public void setButtons(ArrayList<AbstractButton> buttons) {
        this.buttons = buttons;
    }
    
    
    
   
    
    
}
