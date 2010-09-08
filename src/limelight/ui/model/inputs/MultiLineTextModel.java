//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.model.inputs.offsetting.XOffsetStrategy;
import limelight.ui.model.inputs.offsetting.YOffsetStrategy;
import limelight.ui.text.TextLocation;
import limelight.ui.text.TypedLayout;
import limelight.util.Box;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class MultiLineTextModel extends TextModel
{
  public MultiLineTextModel(TextContainer myAreaPanel)
  {
    super(myAreaPanel);
    setOffset(0, 0);
  }

  @Override
  public Dimension getTextDimensions()
  {
    if(getText() == null && getText().length() == 0)
      return new Dimension(0, 0);

    int height = 0;
    int width = 0;
    for(TypedLayout layout : getLines())
    {
      height += layout.getHeight();
      int lineWidth = layout.getWidth();
      if(lineWidth > width)
        width = lineWidth;
    }
    return new Dimension(width, height);
  }

  @Override
  public TextLocation getLocationAt(Point point)
  {
    int remainingY = point.y - getYOffset();
    ArrayList<TypedLayout> lines = getLines();
    for(int lineNumber = 0; lineNumber < lines.size(); lineNumber++)
    {
      TypedLayout line = lines.get(lineNumber);
      int lineHeight = line.getHeight();
      if(lineHeight > remainingY)
      {
        int lineIndex = line.getIndexAt(point.x - getXOffset(line));
        return TextLocation.at(lineNumber, lineIndex);
      }
      else
      {
        remainingY -= lineHeight;
      }
    }
    return TextLocation.fromIndex(lines, getText().length());
  }

  public TypedLayout getLineAt(int y)
  {
    int remainingY = y - getYOffset();
    ArrayList<TypedLayout> lines = getLines();
    for(TypedLayout line : lines)
    {
      int lineHeight = line.getHeight();
      if(lineHeight > remainingY)
        return line;
      else
        remainingY -= lineHeight;
    }
    return lines.get(lines.size() - 1);
  }

  protected void buildLines(ArrayList<TypedLayout> lines)
  {
    if(getText() == null || getText().length() == 0)
      lines.add(createLayout(""));
    else
      Lineator.parseTextForMultipleLayouts(this, lines);
  }

  @Override
  protected XOffsetStrategy getDefaultXOffsetStrategy()
  {
    return XOffsetStrategy.STATIONARY;
  }

  @Override
  protected YOffsetStrategy getDefaultYOffsetStrategy()
  {
    return YOffsetStrategy.FITTING;
  }

  @Override
  public Box getCaretShape()
  {
    TextLocation caretLocation = getCaretLocation();
    TypedLayout line = getLines().get(caretLocation.line);
    Box caretShape = line.getCaretShape(caretLocation.index);
    caretShape.translate(getXOffset(line), getY(caretLocation));
    return caretShape;
  }

  @Override
  public ArrayList<Box> getSelectionRegions()
  {
    ArrayList<Box> regions = new ArrayList<Box>();

    boolean startsAtCaret = getCaretLocation().before(getSelectionLocation());
    TextLocation start = startsAtCaret ? getCaretLocation() : getSelectionLocation();
    TextLocation end = startsAtCaret ?  getSelectionLocation() : getCaretLocation();

    ArrayList<TypedLayout> lines = getLines();
    int y = getY(start);
    for(int i = start.line; i <= end.line; i++)
    {
      TypedLayout line = lines.get(i);
      int startX = i == start.line ? line.getX(start.index) + getXOffset(line) : 0;
      int endX = i == end.line ? line.getX(end.index) + getXOffset(line) : getContainer().getWidth();

      regions.add(new Box(startX, y, endX - startX, line.getHeight()));

      y += line.getHeight() + line.getLeading();
    }

    return regions;
  }
}
