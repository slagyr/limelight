//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

public interface TextAccessor
{
  void setText(String text, Prop panel);
  String getText();
  void markAsDirty();
  void markAsNeedingLayout();
  boolean hasFocus();
}
