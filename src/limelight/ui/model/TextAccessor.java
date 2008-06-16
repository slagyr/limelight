//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.LimelightException;

public interface TextAccessor
{
  void setText(String text) throws LimelightException;
  String getText();
}
