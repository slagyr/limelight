//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.ui.Panel;
import limelight.util.Box;

import java.util.List;

public interface ParentPanel
{
  Box getChildConsumableBounds();
  void add(Panel child);
  List<Panel> getChildren();
  boolean remove(Panel child);
  void removeAll();
  void sterilize();
  boolean isSterilized();
  boolean hasChildren();
}
