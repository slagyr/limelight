//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.ui.Panel;

import java.util.Map;

public interface Layout
{
  void doExpansion(Panel panel);
  void doContraction(Panel panel);
  void doFinalization(Panel panel);

  void doLayout(Panel panel, Map<Panel, Layout> panelsToLayout);

  boolean overides(Layout other);

  void doLayout(Panel thePanel, Map<Panel, Layout> panelsToLayout, boolean topLevel);
}
