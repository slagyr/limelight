package limelight.ui.model;

import limelight.events.EventHandler;

import java.awt.*;

public abstract class Stage
{
  protected RootMouseListener mouseListener;
  protected RootKeyListener keyListener;
  protected RootPanel root;
  private EventHandler eventHandler = new EventHandler();
  private boolean vital = true;

  protected Stage()
  {
    mouseListener = new RootMouseListener(this);
    keyListener = new RootKeyListener(this);
  }

  public abstract void close();
  public abstract boolean isVisible();
  public abstract int getWidth();
  public abstract int getHeight();
  public abstract Graphics getGraphics();
  public abstract Cursor getCursor();
  public abstract void setCursor(Cursor cursor);
  public abstract Insets getInsets();

  public EventHandler getEventHandler()
  {
    return eventHandler;
  }

  public RootKeyListener getKeyListener()
  {
    return keyListener;
  }

  public RootMouseListener getMouseListener()
  {
    return mouseListener;
  }

  public RootPanel getRoot()
  {
    return root;
  }

  public void setRoot(RootPanel root)
  {
    this.root = root;
  }

  public boolean shouldAllowClose()
  {
    return root == null || root.shouldAllowClose();
  }

  public boolean isVital()
  {
    return vital;
  }

  public void setVital(boolean value)
  {
    vital = value;
  }
}
