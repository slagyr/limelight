//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui;

import limelight.rapi.Prop;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.*;

public class PropEventListener implements MouseListener, KeyListener, MouseMotionListener, FocusListener, ChangeListener, ActionListener, ItemListener
{
	private Prop prop;

	public PropEventListener(Prop prop)
	{
		this.prop = prop;
	}

	public void mouseClicked(MouseEvent e)
	{
		prop.mouse_clicked(e);
	}

	public void mousePressed(MouseEvent e)
	{
    prop.mouse_pressed(e);
  }

	public void mouseReleased(MouseEvent e)
	{
    prop.mouse_released(e);
  }

	public void mouseEntered(MouseEvent e)
	{
		prop.hover_on();
		prop.mouse_entered(e);
	}

	public void mouseExited(MouseEvent e)
	{
		prop.mouse_exited(e);
		prop.hover_off();
	}

  public void keyTyped(KeyEvent e)
  {
    prop.key_typed(e);
  }

  public void keyPressed(KeyEvent e)
  {
    prop.key_pressed(e);
  }

  public void keyReleased(KeyEvent e)
  {
    prop.key_released(e);
  }

  public void mouseDragged(MouseEvent e)
  {
    prop.mouse_dragged(e);
  }

  public void mouseMoved(MouseEvent e)
  {
    prop.mouse_moved(e);
  }

  public void focusGained(FocusEvent e)
  {
    prop.focus_gained(e);
  }

  public void focusLost(FocusEvent e)
  {
    prop.focus_lost(e);
  }

  public void stateChanged(ChangeEvent e)
  {    
    prop.state_changed(e);
  }

  public void actionPerformed(ActionEvent e)
  {
    prop.button_pressed(e);
  }

  public void itemStateChanged(ItemEvent e)
  {
    prop.item_state_changed(e);
  }
}
