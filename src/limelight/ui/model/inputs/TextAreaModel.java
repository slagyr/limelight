package limelight.ui.model.inputs;

import limelight.ui.TextLayoutImpl;
import limelight.ui.TypedLayout;
import limelight.ui.model.TextPanel;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;

public class TextAreaModel extends TextModel
{
  public TextAreaModel(TextInputPanel myAreaPanel)
  {
    this.myPanel = myAreaPanel;
    cursorX = SIDE_TEXT_MARGIN;
    selectionOn = false;
    font = new Font("Arial", Font.PLAIN, 12);
    selectionStartX = 0;
    cursorIndex = 0;
    selectionIndex = 0;
    xOffset = 0;
  }

  @Override
  public void shiftOffset()
  {
  }

  @Override
  public int getXPosFromText(String toIndexString)
  {
    return 0;
  }

  @Override
  public Dimension calculateTextDimensions()
  {
    if (getText() != null && getText().length() > 0)
    {
      int height = 0;
      int width = 0;
      for (TypedLayout layout : getTextLayouts())
      {
        height += getHeightDimension(layout);
        int dimWidth = getWidthDimension(layout);
        if (dimWidth > width)
          width = dimWidth;
      }
      return new Dimension(width, height);
    }
    return null;
  }

  @Override
  public ArrayList<TypedLayout> getTextLayouts()
  {
    if (getText().length() == 0)
      return null;
    else
    {
      if (textLayouts == null || isThereSomeDifferentText())
      {
        setLastLayedOutText(getText());
        textLayouts = parseTextForMultipleLayouts(getText());
      }
      return textLayouts;
    }
  }

  @Override
  public Rectangle getSelectionRegion()
  {
    return null;
  }

  @Override
  public boolean isBoxFull()
  {
   if (getText().length() > 0)
      return (myPanel.getHeight() - TextModel.TOP_MARGIN * 2 <= calculateTextDimensions().height);
    return false;
  }

  public ArrayList<TypedLayout> parseTextForMultipleLayouts(String text)
  {
    AttributedString attrString = new AttributedString(text);
    attrString.addAttribute(TextAttribute.FONT, font);
    AttributedCharacterIterator iterator = attrString.getIterator();

    ArrayList<Integer> newLineIndices = findNewLineIndices(text);
    ArrayList<TypedLayout> textLayouts = new ArrayList<TypedLayout>();

    LineBreakMeasurer breaker = new LineBreakMeasurer(iterator, TextPanel.getRenderContext());
    int firstCharIndex = 0,lastCharIndex,newLineIndex = 0;
    String layoutText = "";
    while (breaker.getPosition() < iterator.getEndIndex())
    {
      TextLayout layout;
      if (newLineIndices != null && newLineIndex < newLineIndices.size())
      {
        layout = breaker.nextLayout(myPanel.getWidth(), newLineIndices.get(newLineIndex) + 1, false);
        newLineIndex++;
      }
      else
        layout = breaker.nextLayout(myPanel.getWidth());
      lastCharIndex = firstCharIndex + layout.getCharacterCount();
      layoutText = text.substring(firstCharIndex, lastCharIndex);
      System.out.println("layoutText = " + layoutText);
      textLayouts.add(new TextLayoutImpl(layoutText, font, TextPanel.getRenderContext()));
      firstCharIndex = lastCharIndex;

    }

    return textLayouts;
  }

  public ArrayList<Integer> findNewLineIndices(String text)
  {
    ArrayList<Integer> indices = new ArrayList<Integer>();
    for (int i = 0; i < text.length(); i++)
    {
      if (text.charAt(i) == '\n' || text.charAt(i) == '\r')
        indices.add(i);
    }
    return indices;
  }

  public void lostOwnership(Clipboard clipboard, Transferable contents)
  {
  }
}
