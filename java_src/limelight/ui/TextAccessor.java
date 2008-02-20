package limelight.ui;

import limelight.LimelightException;

public interface TextAccessor
{
  void setText(String text) throws LimelightException;
  String getText();
}
