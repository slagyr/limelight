package limelight;

import java.awt.event.*;

public class BlockMouseListener implements MouseListener
{
	private Block block;

	public BlockMouseListener(Block block)
	{
		this.block = block;
	}

	public void mouseClicked(MouseEvent mouseEvent)
	{
		block.mouseClicked();
	}

	public void mousePressed(MouseEvent mouseEvent)
	{
	}

	public void mouseReleased(MouseEvent mouseEvent)
	{
	}

	public void mouseEntered(MouseEvent mouseEvent)
	{
		block.hoverOn();
		block.mouseEntered();	
	}

	public void mouseExited(MouseEvent mouseEvent)
	{
		block.mouseExited();
		block.hoverOff();
	}
}
