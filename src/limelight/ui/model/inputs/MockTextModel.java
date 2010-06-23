package limelight.ui.model.inputs;

import limelight.ui.MockTypedLayoutFactory;
import limelight.ui.TypedLayout;
import limelight.util.Box;

import java.awt.*;
import java.util.ArrayList;

public class MockTextModel extends TextModel
{
  public boolean clearLayoutsCalled;

  public MockTextModel(TextInputPanel inputPanel)
  {
    super(inputPanel);
    inputPanel.setModel(this);
    setTypedLayoutFactory(MockTypedLayoutFactory.instance);
  }

  @Override
  public void clearLayouts()
  {
    clearLayoutsCalled = true;
  }

  @Override
  public int calculateYOffset()
  {
    return 0;
  }

  @Override
  public int getXOffset()
  {
    return 0;
  }

  @Override
  public int getYOffset()
  {
    return 0;
  }

  @Override
  protected int getXPosFromText(String toIndexString)
  {
    return 0;
  }

  @Override
  public Dimension getTextDimensions()
  {
    return null;
  }

  @Override
  public ArrayList<TypedLayout> getTypedLayouts()
  {
    if(textLayouts == null)
      addLayout(getText());
    return textLayouts;
  }

  @Override
  protected void recalculateOffset()
  {
  }

  @Override
  public TypedLayout getActiveLayout()
  {
    return textLayouts.get(0);
  }

  @Override
  public Box getCaretShape()
  {
    return textLayouts.get(0).getCaretShape(getCaretIndex());
  }

  @Override
  public ArrayList<Rectangle> getSelectionRegions()
  {
    return null;
  }

  @Override
  public boolean isBoxFull()
  {
    return false;
  }

  @Override
  public boolean isMoveUpEvent(int keyCode)
  {
    return false;
  }

  @Override
  public boolean isMoveDownEvent(int keyCode)
  {
    return false;
  }

  @Override
  public int getTopOfStartPositionForCursor()
  {
    return 0;
  }

  @Override
  public int getBottomPositionForCursor()
  {
    return 0;
  }

  @Override
  public int getIndexOfLastCharInLine(int line)
  {
    return 0;
  }
  
  @Override
  public boolean isCursorAtCriticalEdge(int cursorX)
  {
    return false;
  }

  public void addLayout(String value)
  {
    if(textLayouts == null)
      textLayouts = new ArrayList<TypedLayout>();
    textLayouts.add(createLayout(value));
  }
}
