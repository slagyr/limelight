//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

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
  protected int getLineNumber(int index)
  {
    return 0;
  }

  @Override
  protected void buildLines(ArrayList<TypedLayout> lines)
  {
    lines.add(createLayout(getText()));
  }

  public Dimension getTextDimensions()
  {
    //TODO MDM could cache here
    TypedLayout activeLayout = getActiveLayout();
    return new Dimension(activeLayout.getWidth(), activeLayout.getHeight());
  }

  @Override
  public TextLocation getLocationAt(Point point)
  {
    TypedLayout layout = getActiveLayout();
    int index = layout.getIndexAt(point.x - getXOffset());
    return TextLocation.at(0, index);
  }

  @Override
  public TypedLayout getActiveLayout()
  {
    return getLines().get(0);
  }

  @Override
  public Box getCaretShape()
  {
    return getActiveLayout().getCaretShape(getCaretIndex()).translated(getOffset());
  }

  public ArrayList<Box> getSelectionRegions()
  {
    int x1 = getCaretX();
    int x2 = getSelectionX();
    int start = Math.min(x1, x2);
    int end = Math.max(x1, x2);

    ArrayList<Box> regions = new ArrayList<Box>();
    regions.add(new Box(start, 0, end - start, getActiveLayout().getHeight() + 1).translated(getOffset()));
    return regions;
  }

  public boolean isMoveUpEvent(int keyCode)
  {
    return false;
  }

  public boolean isMoveDownEvent(int keyCode)
  {
    return false;
  }

}
