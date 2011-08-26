//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

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
