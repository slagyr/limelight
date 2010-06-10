//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.MockPanel;
import limelight.ui.TextLayoutImpl;
import limelight.ui.model.TextPanel;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static junit.framework.Assert.assertEquals;

public class TextBoxModelTest
{
  TextModel boxModel;
  TextInputPanel panel;

  @Before
  public void setUp()
  {
    panel = new TextBox2Panel();
    MockPanel parent = new MockPanel();
    parent.add(panel);
    parent.setSize(150, 28);
    panel.doLayout();
    boxModel = panel.getModelInfo();
    boxModel.setText("Bob Dole likes to hear Bob Dole say 'Bob Dole'  ");
  }

  @Test
  public void canCalcTheXPosForCursorFromStringWithOffset()
  {
    int expectedX = boxModel.getWidthDimension(new TextLayoutImpl("ABC", boxModel.getFont(), TextPanel.getRenderContext()));
    boxModel.xOffset = 10;

    int x = boxModel.getXPosFromText("ABC");

    assertEquals(expectedX - 10, x);
  }

  @Test
  public void canCalculateTheXPositionForTheCursorFromAString()
  {
    int expectedX = boxModel.getWidthDimension(new TextLayoutImpl("ABC", boxModel.getFont(), TextPanel.getRenderContext()));

    int x = boxModel.getXPosFromText("ABC");

    assertEquals(expectedX, x);
  }

  @Test
  public void canTellIfTheTextPanelIsFull()
  {
    assertEquals(true, boxModel.isBoxFull());
  }

  @Test
  public void canTellIfTheCursorIsAtACriticalEdge()
  {
    boxModel.calculateLeftShiftingOffset();

    assertEquals(true, boxModel.isCursorAtCriticalEdge(boxModel.getXPosFromIndex(boxModel.getCursorIndex())));

    boxModel.setCursorIndex(0);

    assertEquals(true, boxModel.isCursorAtCriticalEdge(boxModel.getXPosFromIndex(boxModel.getCursorIndex())));

    boxModel.xOffset = 0;
    boxModel.setCursorIndex(boxModel.getText().length() - 5);

    assertEquals(true, boxModel.isCursorAtCriticalEdge(boxModel.getXPosFromIndex(boxModel.getCursorIndex())));
  }

  @Test
  public void canShiftTheCursorAndTextRightAboutHalfTheDistanceOfTheBoxWidthIfCriticallyLeft()
  {
    boxModel.setCursorIndex(10);
    int offset = boxModel.calculateTextDimensions().width - panel.getWidth();
    boxModel.xOffset = offset;

    assertEquals(true, boxModel.isCursorAtCriticalEdge(boxModel.getXPosFromIndex(boxModel.getCursorIndex())));

    boxModel.shiftOffset(boxModel.getCursorIndex());
    assertEquals(true, boxModel.xOffset <= offset / 2 + 5 && boxModel.xOffset != 0);
  }

  @Test
  public void canShiftTheCursorAndTextLeftIfCriticallyRight()
  {
    boxModel.setCursorIndex(0);
    boxModel.xOffset = 0;

    boxModel.setCursorIndex(30);

    assertEquals(true, boxModel.isCursorAtCriticalEdge(boxModel.getXPosFromIndex(boxModel.getCursorIndex())));

    boxModel.shiftOffset(boxModel.getCursorIndex());

    assertEquals(true, boxModel.xOffset > 0);

  }

  @Test
  public void canCutTheXOffsetInHalfWhenTheCursorIsOnTheLeftEdge()
  {
    boxModel.shiftOffset(boxModel.getCursorIndex());
    int offset = boxModel.xOffset;
    boxModel.setCursorIndex(0);

    boxModel.shiftOffset(boxModel.getCursorIndex());

    assertEquals(true, boxModel.xOffset <= offset / 2 + 2);

    boxModel.setText("hi");
    boxModel.shiftOffset(boxModel.getCursorIndex());
    assertEquals(true, boxModel.xOffset == 0);

  }

  @Test
  public void canCalculateTheTextModelsDimensions()
  {
    boxModel.setText("");
    Dimension dim = boxModel.calculateTextDimensions();
    assertEquals(null, dim);

    boxModel.setText("X");
    dim = boxModel.calculateTextDimensions();
    assertEquals(8, dim.width);
    assertEquals(14, dim.height);
  }

  @Test
  public void canGetTheSelectedRegion()
  {
    boxModel.setSelectionIndex(0);
    boxModel.setSelectionOn(true);

    Rectangle region = boxModel.getSelectionRegions().get(0);

    assertEquals(0, region.x);
    assertEquals(0, region.y);
    assertEquals(true, region.width > 0);
    assertEquals(true, region.height > 0);
  }

  @Test
  public void willRecalculateXOffsetIfTextIsFullWhenGettingSelection()
  {
    boxModel.setSelectionIndex(boxModel.getText().length());
    boxModel.setCursorIndex(10);
    boxModel.setSelectionOn(true);
    boxModel.xOffset = 100;

    boxModel.getSelectionRegions();

    assertEquals(true, boxModel.xOffset > 0 && boxModel.xOffset < 100);
  }

  @Test
  public void willAlwaysReturnZeroForTheLineNumber()
  {
    assertEquals(0, boxModel.getLineNumberOfIndex(boxModel.getCursorIndex()));
  }

}
