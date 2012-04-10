//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.ui.Panel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FakeLayout implements Layout
{
  public static FakeLayout instance = new FakeLayout(false);
  public static FakeLayout alwaysOverides = new FakeLayout(true);

  public boolean overide;
  public Panel lastPanelExpanded;
  public Panel lastPanelContracted;
  public Panel lastPanelFinalized;
  public List<Panel> expansions = new ArrayList<Panel>();
  public List<Panel> contractions = new ArrayList<Panel>();
  public List<Panel> finalizations = new ArrayList<Panel>();

  public FakeLayout(boolean overide)
  {
    this.overide = overide;
  }

  public void doExpansion(Panel panel)
  {
    expansions.add(panel);
    lastPanelExpanded = panel;
  }

  public void doContraction(Panel panel)
  {
    contractions.add(panel);
    lastPanelContracted = panel;
  }

  public void doFinalization(Panel panel)
  {
    finalizations.add(panel);
    lastPanelFinalized = panel;
  }

  public boolean overides(Layout other)
  {
    return overide;
  }
}
