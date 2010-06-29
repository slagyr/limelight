package limelight.ui.model.inputs;

import limelight.ui.MockTypedLayoutFactory;
import limelight.ui.model.inputs.offsetting.XOffsetStrategy;
import limelight.ui.model.inputs.offsetting.YOffsetStrategy;
import limelight.ui.text.TypedLayout;
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
  protected int getCaretLine()
  {
    return 0;
  }

  @Override
  protected TypedLayout getLineWithCaret()
  {
    return getActiveLayout();
  }

  @Override
  protected int getXPosFromText(String toIndexString)
  {
    return 0;
  }

  @Override
  public Dimension getTextDimensions()
  {
    TypedLayout activeLayout = getActiveLayout();
    return new Dimension(activeLayout.getWidth(), activeLayout.getHeight());
  }

  @Override
  public int getIndexAt(int x, int y)
  {
    return 0;
  }

  @Override
  public ArrayList<TypedLayout> getLines()
  {
    if(typedLayouts == null)
      addLayout(getText());
    return typedLayouts;
  }

  @Override
  public TypedLayout getActiveLayout()
  {
    return typedLayouts.get(0);
  }

  @Override
  public Box getCaretShape()
  {
    return typedLayouts.get(0).getCaretShape(getCaretIndex());
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
    if(typedLayouts == null)
      typedLayouts = new ArrayList<TypedLayout>();
    typedLayouts.add(createLayout(value));
  }
}
