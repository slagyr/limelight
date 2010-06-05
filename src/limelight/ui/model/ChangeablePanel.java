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
