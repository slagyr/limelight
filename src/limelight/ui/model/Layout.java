package limelight.ui.model;

import limelight.ui.Panel;

public interface Layout
{
  void doLayout(Panel panel);
  boolean overides(Layout other);
}
