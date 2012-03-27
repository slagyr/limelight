//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.Log;
import limelight.ui.Panel;

import java.util.Map;

public class TextPanelLayout extends SimpleLayout
{
  public static TextPanelLayout instance = new TextPanelLayout();

  @Override
  public void doExpansion(Panel thePanel)
  {
    TextPanel panel = (TextPanel) thePanel;
    try
    {
      panel.compile();
    }
    catch(Exception e)
    {
      // Can fail if the graphics are not ready.
      Log.debug("TEXT failed to compile", e);
      panel.markAsNeedingLayout();
      panel.getParent().markAsNeedingLayout();
    }
  }

  public boolean overides(Layout other)
  {
    return true;
  }
}
