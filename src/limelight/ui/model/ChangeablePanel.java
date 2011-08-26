//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.ui.Panel;

public interface ChangeablePanel extends Panel
{
  void setSizeChangePending(boolean value);

  void markAsNeedingLayout();

  void markAsNeedingLayout(Layout layout);

  void propagateSizeChangeUp();

  void propagateSizeChangeDown();

  void setBorderChanged(boolean b);

  TextAccessor getTextAccessor();
}
