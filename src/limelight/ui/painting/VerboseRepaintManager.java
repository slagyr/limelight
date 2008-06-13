package limelight.ui.painting;

import javax.swing.*;
import java.awt.*;

public class VerboseRepaintManager extends RepaintManager
{
  public VerboseRepaintManager()
  {
    super();
  }

  public static void install()
  {
    VerboseRepaintManager instance = new VerboseRepaintManager();
    RepaintManager.setCurrentManager(instance);
  }

  public synchronized void addInvalidComponent(JComponent invalidComponent)
  {
    System.out.println("addInvalidComponent(" + invalidComponent + ")");
    super.addInvalidComponent(invalidComponent);
  }

  public synchronized void removeInvalidComponent(JComponent component)
  {
    System.out.println("removeInvalidComponent(" + component + ")");
    super.removeInvalidComponent(component);
  }

  public void addDirtyRegion(JComponent c, int x, int y, int w, int h)
  {
    System.out.println("addDirtyRegion(" + c + ")");
    new Exception().printStackTrace();
    super.addDirtyRegion(c, x, y, w, h);
  }

  public Rectangle getDirtyRegion(JComponent aComponent)
  {
    System.out.println("getDirtyRegion(" + aComponent + ")");
    return super.getDirtyRegion(aComponent);
  }

  public void markCompletelyDirty(JComponent aComponent)
  {
    System.out.println("markCompletelyDirty(" + aComponent + ")");
    super.markCompletelyDirty(aComponent);
  }

  public void markCompletelyClean(JComponent aComponent)
  {
    System.out.println("markCompletelyClean(" + aComponent + ")");
    super.markCompletelyClean(aComponent);
  }

  public boolean isCompletelyDirty(JComponent aComponent)
  {
    System.out.println("isCompletelyDirty(" + aComponent + ")");
    return super.isCompletelyDirty(aComponent);
  }

  public void validateInvalidComponents()
  {
    System.out.println("validateInvalidComponents()");
    super.validateInvalidComponents();
  }

  public void paintDirtyRegions()
  {
    System.out.println("paintDirtyRegions()");
    super.paintDirtyRegions();
  }

  public synchronized String toString()
  {
    return super.toString();
  }

  public Image getOffscreenBuffer(Component c, int proposedWidth, int proposedHeight)
  {
    return super.getOffscreenBuffer(c, proposedWidth, proposedHeight);
  }

  public Image getVolatileOffscreenBuffer(Component c, int proposedWidth, int proposedHeight)
  {
    return super.getVolatileOffscreenBuffer(c, proposedWidth, proposedHeight);
  }

  public void setDoubleBufferMaximumSize(Dimension d)
  {
    super.setDoubleBufferMaximumSize(d);
  }

  public Dimension getDoubleBufferMaximumSize()
  {
    return super.getDoubleBufferMaximumSize();
  }

  public void setDoubleBufferingEnabled(boolean aFlag)
  {
    super.setDoubleBufferingEnabled(aFlag);
  }

  public boolean isDoubleBufferingEnabled()
  {
    return super.isDoubleBufferingEnabled();
  }
}
