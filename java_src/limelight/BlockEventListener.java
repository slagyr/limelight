package limelight;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.*;

public class BlockEventListener implements MouseListener, KeyListener, MouseMotionListener, FocusListener, ChangeListener, ActionListener, ItemListener
{
	private Block block;

	public BlockEventListener(Block block)
	{
		this.block = block;
	}

	public void mouseClicked(MouseEvent e)
	{
		block.mouse_clicked(e);
	}

	public void mousePressed(MouseEvent e)
	{
    block.mouse_pressed(e);
  }

	public void mouseReleased(MouseEvent e)
	{
    block.mouse_released(e);
  }

	public void mouseEntered(MouseEvent e)
	{
		block.hover_on();
		block.mouse_entered(e);
	}

	public void mouseExited(MouseEvent e)
	{
		block.mouse_exited(e);
		block.hover_off();
	}

  public void keyTyped(KeyEvent e)
  {
    block.key_typed(e);
  }

  public void keyPressed(KeyEvent e)
  {
    block.key_pressed(e);
  }

  public void keyReleased(KeyEvent e)
  {
    block.key_released(e);
  }

  public void mouseDragged(MouseEvent e)
  {
    block.mouse_dragged(e);
  }

  public void mouseMoved(MouseEvent e)
  {
    block.mouse_moved(e);
  }

  public void focusGained(FocusEvent e)
  {
    block.focus_gained(e);
  }

  public void focusLost(FocusEvent e)
  {
    block.focus_lost(e);
  }

  public void stateChanged(ChangeEvent e)
  {    
    block.state_changed(e);
  }

  public void actionPerformed(ActionEvent e)
  {
    block.button_pressed(e);
  }

  public void itemStateChanged(ItemEvent e)
  {
    block.item_state_changed(e);
  }
}
