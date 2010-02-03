package limelight.ui.model.inputs;

import limelight.ui.TextLayoutImpl;
import limelight.ui.TypedLayout;
import limelight.ui.model.TextPanel;
import limelight.util.Box;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;


public class TextBoxModel extends TextModel
{

  public TextBoxModel(TextInputPanel myBox)
  {
    this.myPanel = myBox;
    cursorX = LEFT_TEXT_MARGIN;
    selectionOn = false;
    font = new Font("Arial", Font.PLAIN, 12);
    selectionStartX = 0;
    cursorIndex = 0;
    selectionIndex = 0;
    xOffset = 0;
  }

  public int getXPosFromText(String toIndexString)
  {
    TypedLayout layout = new TextLayoutImpl(toIndexString, font, TextPanel.getRenderContext());
    int x = getWidthDimension(layout) + LEFT_TEXT_MARGIN - xOffset;
    if (x < LEFT_TEXT_MARGIN)
      x = LEFT_TEXT_MARGIN;
    return x;
  }

  public void shiftOffset()
  {
    String rightShiftingText = text.substring(0, cursorIndex);
    if (rightShiftingText.length() == 0 || xOffset == 0)
    {
      cursorX = SIDE_DETECTION_MARGIN;
      xOffset = 0;
    }
    else
    {
      TypedLayout layout = new TextLayoutImpl(rightShiftingText, font, TextPanel.getRenderContext());
      int textWidth = getWidthDimension(layout);
      if (textWidth > getPanelWidth() / 2)
        xOffset -= getPanelWidth() / 2;
      else
        xOffset -= textWidth;
      if (xOffset < 0)
        xOffset = 0;
      cursorX = getXPosFromIndex(cursorIndex);
    }
  }

  public Dimension calculateTextDimensions()
  {
    if (text != null && text.length() > 0)
    {
      int height = 0;
      int width = 0;
      for (TypedLayout layout : getTextLayouts())
      {
        height += getHeightDimension(layout);
        width += getWidthDimension(layout);
      }
      return new Dimension(width, height);
    }
    return null;
  }

  public ArrayList<TypedLayout> getTextLayouts()
  {
    if (text.length() == 0)
      return null;
    else
    {
      if (textLayouts == null || !text.toString().equals(concatenateAllLayoutText()))
      {
        textLayouts = new ArrayList<TypedLayout>();
        textLayouts.add(new TextLayoutImpl(text.toString(), font, TextPanel.getRenderContext()));
      }
      return textLayouts;
    }
  }

  public Rectangle getSelectionRegion()
  {
    if (text.length() > 0)
      calculateTextXOffset(myPanel.getWidth(), calculateTextDimensions().width);
    int x1 = getXPosFromIndex(cursorIndex);
    int x2 = getXPosFromIndex(selectionIndex);
    int edgeSelectionExtension = 0;

    if (x1 <= LEFT_TEXT_MARGIN || x2 <= LEFT_TEXT_MARGIN)
      edgeSelectionExtension = LEFT_TEXT_MARGIN;
    if (x1 > x2)
      return new Box(x2 - edgeSelectionExtension, TOP_MARGIN, x1 - x2 + edgeSelectionExtension, getPanelHeight() - TOP_MARGIN * 2);
    else
      return new Box(x1 - edgeSelectionExtension, TOP_MARGIN, x2 - x1 + edgeSelectionExtension, getPanelHeight() - TOP_MARGIN * 2);

  }

  public boolean isBoxFull()
  {
    if (text.length() > 0)
      return (myPanel.getWidth() - TextModel.SIDE_DETECTION_MARGIN * 2 <= calculateTextDimensions().width);
    return false;
  }

  public void lostOwnership(Clipboard clipboard, Transferable contents)
  {
    //this doesn't have to do anything...
  }
}
