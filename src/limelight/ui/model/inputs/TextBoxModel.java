//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.text.TextLayoutImpl;
import limelight.ui.text.TypedLayout;
import limelight.ui.model.TextPanel;
import limelight.util.Box;

import java.awt.*;
import java.util.ArrayList;

public class TextBoxModel extends TextModel
{
  public TextBoxModel(TextInputPanel myBox)
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
    if(myPanel.getWidth() > getTextDimensions().width)
      return false;
    return isCriticallyRight(xPos) || isCriticallyLeft(xPos);
  }

  private boolean isCriticallyLeft(int xPos)
  {
    return (xPos <= CARET_WIDTH && getOffset().x != 0);
  }

  private boolean isCriticallyRight(int xPos)
  {
    return (xPos >= myPanel.getWidth() - CARET_WIDTH);
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

  public int getCaretX()
  {
    String textBeforeCaret = getText().substring(0, getCaretIndex());
    return getActiveLayout().getWidthOf(textBeforeCaret);
//    String textBeforeCaret = getText().substring(0, getCaretIndex());
//    TypedLayout layout = createLayout(textBeforeCaret);  //TODO Very inefficient creating a layout here.
//    int textWidth = getWidthDimension(layout) + getTerminatingSpaceWidth(textBeforeCaret);
//    return textWidth;
  }

  @Override
  public int getCaretY()
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

  public ArrayList<TypedLayout> getTypedLayouts()
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
    return getTypedLayouts().get(0);
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

  public ArrayList<Rectangle> getSelectionRegions()
  {
//    if(getText().length() > 0)
//      setOffset(calculateXOffset(), calculateYOffset());
    int x1 = getXPosFromIndex(getCaretIndex());
    int x2 = getXPosFromIndex(getSelectionIndex());
    int edgeSelectionExtension = 0;

    if(x1 <= 0 || x2 <= 0)
      edgeSelectionExtension = 0;
    ArrayList<Rectangle> regions = new ArrayList<Rectangle>();
    if(x1 > x2)
      regions.add(new Box(x2 - edgeSelectionExtension, 0, x1 - x2 + edgeSelectionExtension, getPanelHeight() * 2));
    else
      regions.add(new Box(x1 - edgeSelectionExtension, 0, x2 - x1 + edgeSelectionExtension, getPanelHeight() * 2));
    return regions;
  }

  public int getXOffset()
  {
    return offset == null ? 0 : offset.x;
  }

  public int getYOffset()
  {
    return offset.y;
  }

  public boolean isBoxFull()
  {
    if(getText().length() > 0)
      return (myPanel.getWidth() - (TextModel.CARET_WIDTH * 2) <= getTextDimensions().width);
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
