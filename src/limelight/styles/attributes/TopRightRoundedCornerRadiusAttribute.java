package limelight.styles.attributes;

import limelight.styles.StyleAttribute;
import limelight.styles.abstrstyling.StyleValue;
import limelight.ui.model.ChangeablePanel;

public class TopRightRoundedCornerRadiusAttribute extends StyleAttribute
{
  public TopRightRoundedCornerRadiusAttribute()
  {
    super("Top Right Rounded Corner Radius", "pixels", "0");
  }

  @Override
  public void applyChange(ChangeablePanel panel, StyleValue value)
  {
    handleBorderChange(panel);
  }
}
