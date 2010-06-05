package limelight.ui.model.inputs;

import limelight.ui.TypedLayout;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;

public class MockTextModel extends TextModel
{
  public boolean clearLayoutsCalled;

  public MockTextModel(TextInputPanel inputPanel)
  {
    super();
  }

  @Override
  public void clearLayouts()
  {
    clearLayoutsCalled = true;
  }

  @Override
  public void shiftOffset(int index)
  {
  }

  @Override
  protected int getXPosFromText(String toIndexString)
  {
    return 0;
  }

  @Override
  public Dimension calculateTextDimensions()
  {
    return null;
  }

  @Override
  public ArrayList<TypedLayout> getTextLayouts()
  {
    return null;
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
  public void calculateLeftShiftingOffset()
  {
  }

  @Override
  public int calculateYOffset()
  {
    return 0;
  }

  @Override
  public boolean isCursorAtCriticalEdge(int cursorX)
  {
    return false;
  }

  public void lostOwnership(Clipboard clipboard, Transferable contents)
  {
  }
}
