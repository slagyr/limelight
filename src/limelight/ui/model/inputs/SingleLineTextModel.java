//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.model.inputs.offsetting.XOffsetStrategy;
import limelight.ui.model.inputs.offsetting.YOffsetStrategy;
import limelight.ui.text.TextLocation;
import limelight.ui.text.TypedLayout;
import limelight.util.Box;

import java.awt.*;
import java.util.ArrayList;

public class SingleLineTextModel extends TextModel
{
  public SingleLineTextModel(TextContainer myBox)
  {
    super(myBox);
  }

  @Override
  protected void buildLines(ArrayList<TypedLayout> lines)
  {
    lines.add(createLayout(getText()));
  }

  @Override
  protected XOffsetStrategy getDefaultXOffsetStrategy()
  {
    return XOffsetStrategy.CENTERED;
  }

  @Override
  protected YOffsetStrategy getDefaultYOffsetStrategy()
  {
    return YOffsetStrategy.STATIONARY;
  }

  @Override
  public int getXOffset(TypedLayout line)
  {
    return super.getXOffset(line) + getXOffset();
  }

  public Dimension getTextDimensions()
  {
    //TODO MDM could cache here
    return new Dimension(getLine().getWidth(), getLine().getHeight());
  }

  @Override
  public TextLocation getLocationAt(Point point)
  {
    TypedLayout layout = getLine();
    int index = layout.getIndexAt(point.x - getXOffset());
    return TextLocation.at(0, index);
  }

  @Override
  public Box getCaretShape()
  {
    return getLine().getCaretShape(getCaretLocation().index).translated(getOffset());
  }

  @Override
  public boolean isSingleLine()
  {
    return true;
  }

  private TypedLayout getLine()
  {
    return getLines().get(0);
  }

  public ArrayList<Box> getSelectionRegions()
  {
    int x1 = getAbsoluteX(getCaretLocation());
    int x2 = getAbsoluteX(getSelectionLocation());
    int start = Math.min(x1, x2);
    int end = Math.max(x1, x2);

    ArrayList<Box> regions = new ArrayList<Box>();
    regions.add(new Box(start, 0, end - start, getLine().getHeight() + 1).translated(getOffset()));
    return regions;
  }
}
