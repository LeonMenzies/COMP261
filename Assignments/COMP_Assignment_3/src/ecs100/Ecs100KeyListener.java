package ecs100;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class Ecs100KeyListener extends KeyAdapter{
    private UIKeyListener controller;
    
    public Ecs100KeyListener(UIKeyListener controller) {
	this.controller = controller;
    }
    
    public void keyPressed(final KeyEvent e) {
	//	System.out.println("key pressed: "+ e); //DEBUG
	if (controller != null) {
	    final String key = KeyEvent.getKeyText(e.getKeyCode());
	    
	    if (System.getProperty("os.name").contains("Mac")) {
	    	switch(key) {
	    	case "→":
                    new Thread(() -> controller.keyPerformed("Right")).start();
                    return;
	    	case "←":
                    new Thread(() -> controller.keyPerformed("Left")).start();
                    return;
	    	case "↑":
                    new Thread(() -> controller.keyPerformed("Up")).start();
                    return;
	    	case "↓":
                    new Thread(() -> controller.keyPerformed("Down")).start();
                    return;
	    	}
	    	
	    }
	    
	    if (key.length()==1){
		new Thread(() -> controller.keyPerformed(Character.toString(e.getKeyChar()))).start();
	    }
	    else if (!key.equals("Shift")&&
		     !key.equals("Ctrl")&&
		     !key.equals("Alt"))
		new Thread(() -> controller.keyPerformed(key)).start();
	}
    }
}
