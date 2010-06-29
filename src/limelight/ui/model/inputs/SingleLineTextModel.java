//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.text.TextLayoutImpl;
import limelight.ui.text.TypedLayout;
import limelight.ui.model.TextPanel;
import limelight.util.Box;

import java.awt.*;
import java.util.ArrayList;

public class SingleLineTextModel extends TextModel
{
  public SingleLineTextModel(TextContainer myBox)
  {
    super(myBox);
  }

  protected int getXPosFromText(String toIndexString)
  {
    TypedLayout layout = createLayout(toIndexString);
    return getWidthDimension(layout);
  }

  @Override
  public int calculateYOffset()
  {
    return 0;
  }

  public boolean isCursorAtCriticalEdge(int xPos)
  {
    if(container.getWidth() > getTextDimensions().width)
      return false;
    return isCriticallyRight(xPos) || isCriticallyLeft(xPos);
  }

  private boolean isCriticallyLeft(int xPos)
  {
    return (xPos <= CARET_WIDTH && getOffset().x != 0);
  }

  private boolean isCriticallyRight(int xPos)
  {
    return (xPos >= container.getWidth() - CARET_WIDTH);
  }

  public int calculateRightShiftingOffset()
  {
    int xOffset = getXOffset();

    int textWidth = getCaretX();

    if(textWidth > getPanelWidth() / 2)
      xOffset -= getPanelWidth() / 2;
    else
      xOffset -= textWidth;

    return xOffset;
  }

  @Override
  protected int getLineNumber(int index)
  {
    return 0;
  }

  public int calculateLeftShiftingOffset()
  {
    int xOffset = getXOffset();

    if(getCaretIndex() == getText().length())
    {
      int textWidth = getTextDimensions().width;
      if(textWidth > getPanelWidth())
        xOffset = textWidth - getPanelWidth() + CARET_WIDTH;
    }
    else
    {
      int textWidth = widthOfTextAfterCaret();
      if(textWidth > getPanelWidth() / 2)
        xOffset += getPanelWidth() / 2;
      else
        xOffset += textWidth;
    }

    return xOffset;
  }

  private int widthOfTextAfterCaret()
  {
    if(getText().length() == 0 || getText().length() == getCaretIndex())
      return 0;

    String leftShiftingText;
    if(getCaretIndex() == getText().length() - 1)
      leftShiftingText = Character.toString(getText().charAt(getCaretIndex()));
    else
      leftShiftingText = getText().substring(getCaretIndex(), getText().length() - 1);
    TypedLayout layout = new TextLayoutImpl(leftShiftingText, getFont(), TextPanel.getRenderContext());
    int textWidth = getWidthDimension(layout) + getTerminatingSpaceWidth(leftShiftingText);
    return textWidth;
  }

  public Dimension getTextDimensions()
  {
    //TODO MDM could cache here
    TypedLayout activeLayout = getActiveLayout();
    return new Dimension(activeLayout.getWidth(), activeLayout.getHeight());
  }

  @Override
  public int getIndexAt(int x, int y)
  {
    TypedLayout layout = getActiveLayout();
    return layout.getIndexAt(x - getXOffset());
  }

  public ArrayList<TypedLayout> getLines()
  {
    if(getText() == null)
    {
      initNewTextLayouts("");
      return typedLayouts;
    }
    else
    {
      if(typedLayouts == null || isThereSomeDifferentText())
      {
        setLastLayedOutText(getText());
        initNewTextLayouts(getText());
      }
      return typedLayouts;
    }
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

  private void initNewTextLayouts(String text)
  {
    typedLayouts = new ArrayList<TypedLayout>();
    typedLayouts.add(createLayout(text));
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

  public boolean isBoxFull()
  {
    if(getText().length() > 0)
      return (container.getWidth() - (TextModel.CARET_WIDTH * 2) <= getTextDimensions().width);
    return false;
  }

  public boolean isMoveUpEvent(int keyCode)
  {
    return false;
  }

  public boolean isMoveDownEvent(int keyCode)
  {
    return false;
  }

  public int getIndexOfLastCharInLine(int line)
  {
    return getText().length();
  }
}
