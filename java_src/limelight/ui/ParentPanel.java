package limelight.ui;

import limelight.LimelightException;

import java.util.LinkedList;

public abstract class ParentPanel extends Panel
{
  protected LinkedList<Panel> children;
  private boolean sterilized;

  public ParentPanel()
  {
    super();
  }

  public void add(Panel panel) throws SterilePanelException
  {
    if (sterilized)
      throw new SterilePanelException(getBlock().getClassName());
    children.add(panel);
    panel.setParent(this);
  }

  public boolean hasChildren()
  {
    return children.size() > 0;
  }

  public LinkedList<Panel> getChildren()
  {
    return children;
  }

  public void sterilize()
  {
    sterilized = true;
  }

  public boolean isSterilized()
  {
    return sterilized;
  }

  public abstract Rectangle getChildConsumableArea();
}

class SterilePanelException extends LimelightException
{
  SterilePanelException(String name)
  {
    super("The panel for block named '" + name + "' has been sterilized. Child components may not be added.");
  }
}
