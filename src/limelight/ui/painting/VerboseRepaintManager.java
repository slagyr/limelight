//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.painting;

import javax.swing.*;
import java.awt.*;

public class VerboseRepaintManager extends RepaintManager
{
  private VerboseRepaintManager()
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
    System.err.println("limelight.ui.painting.VerboseRepaintManager.addInvalidComponent");
    super.addInvalidComponent(invalidComponent);
  }

  public synchronized void removeInvalidComponent(JComponent component)
  {
    System.err.println("limelight.ui.painting.VerboseRepaintManager.removeInvalidComponent");
    super.removeInvalidComponent(component);
  }

  public void addDirtyRegion(JComponent c, int x, int y, int w, int h)
  {
    System.err.println("limelight.ui.painting.VerboseRepaintManager.addDirtyRegion");
    super.addDirtyRegion(c, x, y, w, h);
  }

  public Rectangle getDirtyRegion(JComponent aComponent)
  {
    System.err.println("limelight.ui.painting.VerboseRepaintManager.getDirtyRegion");
    return super.getDirtyRegion(aComponent);
  }

  public void markCompletelyDirty(JComponent aComponent)
  {
    System.err.println("limelight.ui.painting.VerboseRepaintManager.markCompletelyDirty");
    super.markCompletelyDirty(aComponent);
  }

  public void markCompletelyClean(JComponent aComponent)
  {
    System.err.println("limelight.ui.painting.VerboseRepaintManager.markCompletelyClean");
    super.markCompletelyClean(aComponent);
  }

  public boolean isCompletelyDirty(JComponent aComponent)
  {
    System.err.println("limelight.ui.painting.VerboseRepaintManager.isCompletelyDirty");
    return super.isCompletelyDirty(aComponent);
  }

  public void validateInvalidComponents()
  {
    System.err.println("limelight.ui.painting.VerboseRepaintManager.validateInvalidComponents");
    super.validateInvalidComponents();
  }

  public void paintDirtyRegions()
  {
    System.err.println("limelight.ui.painting.VerboseRepaintManager.paintDirtyRegions");
    super.paintDirtyRegions();
  }

  public synchronized String toString()
  {
    System.err.println("limelight.ui.painting.VerboseRepaintManager.toString");
    return super.toString();
  }

  public Image getOffscreenBuffer(Component c, int proposedWidth, int proposedHeight)
  {
    System.err.println("limelight.ui.painting.VerboseRepaintManager.getOffscreenBuffer");
    return super.getOffscreenBuffer(c, proposedWidth, proposedHeight);
  }

  public Image getVolatileOffscreenBuffer(Component c, int proposedWidth, int proposedHeight)
  {
    System.err.println("limelight.ui.painting.VerboseRepaintManager.getVolatileOffscreenBuffer");
    return super.getVolatileOffscreenBuffer(c, proposedWidth, proposedHeight);
  }

  public void setDoubleBufferMaximumSize(Dimension d)
  {
    System.err.println("limelight.ui.painting.VerboseRepaintManager.setDoubleBufferMaximumSize");
    super.setDoubleBufferMaximumSize(d);
  }

  public Dimension getDoubleBufferMaximumSize()
  {
    System.err.println("limelight.ui.painting.VerboseRepaintManager.getDoubleBufferMaximumSize");
    return super.getDoubleBufferMaximumSize();
  }
         
  public void setDoubleBufferingEnabled(boolean aFlag)
  {
    System.err.println("limelight.ui.painting.VerboseRepaintManager.setDoubleBufferingEnabled");
    super.setDoubleBufferingEnabled(aFlag);
  }

  public boolean isDoubleBufferingEnabled()
  {
//    System.err.println("limelight.ui.painting.VerboseRepaintManager.isDoubleBufferingEnabled");
    return super.isDoubleBufferingEnabled();
  }
}
