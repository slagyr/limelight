//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model.text.offsetting;

public interface XOffsetStrategy
{
  XOffsetStrategy STATIONARY = new StationaryXOffsetStrategy();
  XOffsetStrategy CENTERED = new CenteredXOffsetStrategy();
  XOffsetStrategy FITTING = new FittingXOffsetStrategy();

  int calculateXOffset(limelight.ui.model.text.TextModel model);
}
