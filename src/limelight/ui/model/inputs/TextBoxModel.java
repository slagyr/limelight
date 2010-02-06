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
    cursorX = SIDE_TEXT_MARGIN;
    selectionOn = false;
    font = new Font("Arial", Font.PLAIN, 12);
    selectionStartX = 0;
    cursorIndex = 0;
    selectionIndex = 0;
    xOffset = 0;
  }

  protected int getXPosFromText(String toIndexString)
  {
    TypedLayout layout = new TextLayoutImpl(toIndexString, font, TextPanel.getRenderContext());
    int x = getWidthDimension(layout) + SIDE_TEXT_MARGIN - xOffset;
    if (x < SIDE_TEXT_MARGIN)
      x = SIDE_TEXT_MARGIN;
    return x;
  }
  
  public void shiftOffset()
  {
    String rightShiftingText = getText().substring(0, cursorIndex);
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
    if (getText() != null && getText().length() > 0)
    {
      int height = 0;
      int width = 0;
      for (TypedLayout layout : getTextLayouts())
      {
        height += (int) (getHeightDimension(layout) + layout.getLeading() + .5);
        width += getWidthDimension(layout);
      }
      return new Dimension(width, height);
    }
    return null;
  }

  public ArrayList<TypedLayout> getTextLayouts()
  {
    if (getText().length() == 0)
      return null;
    else
    {

      if (textLayouts == null || isThereSomeDifferentText())
      {
        setLastLayedOutText(getText());
        textLayouts = new ArrayList<TypedLayout>();
        textLayouts.add(new TextLayoutImpl(getText(), font, TextPanel.getRenderContext()));
      }
      return textLayouts;
    }
  }

  public ArrayList<Rectangle> getSelectionRegions()
  {
    if (getText().length() > 0)
      calculateTextXOffset(myPanel.getWidth(), calculateTextDimensions().width);
    int x1 = getXPosFromIndex(cursorIndex);
    int x2 = getXPosFromIndex(selectionIndex);
    int edgeSelectionExtension = 0;

    if (x1 <= SIDE_TEXT_MARGIN || x2 <= SIDE_TEXT_MARGIN)
      edgeSelectionExtension = SIDE_TEXT_MARGIN;
    ArrayList<Rectangle> regions = new ArrayList<Rectangle>();
    if (x1 > x2)
      regions.add(new Box(x2 - edgeSelectionExtension, TOP_MARGIN, x1 - x2 + edgeSelectionExtension, getPanelHeight() - TOP_MARGIN * 2));
    else
      regions.add(new Box(x1 - edgeSelectionExtension, TOP_MARGIN, x2 - x1 + edgeSelectionExtension, getPanelHeight() - TOP_MARGIN * 2));
    return regions;
  }

  public boolean isBoxFull()
  {
    if (getText().length() > 0)
      return (myPanel.getWidth() - TextModel.SIDE_DETECTION_MARGIN * 2 <= calculateTextDimensions().width);
    return false;
  }

  @Override
  public int getTopOfStartPositionForCursor()
  {
    return TOP_MARGIN;
  }

  @Override
  public int getBottomPositionForCursor()
  {
    return myPanel.getHeight() - TOP_MARGIN * 2;
  }

  public void lostOwnership(Clipboard clipboard, Transferable contents)
  {
    //this doesn't have to do anything...
  }
}
