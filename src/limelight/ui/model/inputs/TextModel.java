package limelight.ui.model.inputs;

import limelight.styles.styling.SimpleHorizontalAlignmentAttribute;
import limelight.styles.styling.SimpleVerticalAlignmentAttribute;
import limelight.ui.TextLayoutImpl;
import limelight.ui.TypedLayout;
import limelight.ui.model.TextPanel;
import limelight.util.Box;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.util.ArrayList;

public abstract class TextModel implements ClipboardOwner
{

  public static final int LEFT_TEXT_MARGIN = 3;
  public static final int SIDE_DETECTION_MARGIN = 4;
  public static final int TOP_MARGIN = 4;

  public StringBuffer text = new StringBuffer();
  ArrayList<TypedLayout> textLayouts;
  int cursorX;
  public boolean selectionOn;
  int selectionStartX;
  protected int cursorIndex;
  protected int selectionIndex;
  public Font font;
  int xOffset;
  TextInputPanel myBox;

  public TextModel()
  {

  }

  public void calculateTextXOffset(int panelWidth, int textWidth)
  {
    if (textWidth >= panelWidth)
    {
      cursorX = getXPosFromIndex(cursorIndex);
      int newXOffset = textWidth - panelWidth + SIDE_DETECTION_MARGIN + LEFT_TEXT_MARGIN;
      if (!typingInCenterOfBox(panelWidth, newXOffset))
        xOffset = newXOffset;
      while (cursorX < SIDE_DETECTION_MARGIN)
        shiftTextRight();
    }
    else
      xOffset = 0;
  }

  private boolean typingInCenterOfBox(int panelWidth, int newXOffset)
  {
    return (isLeftOfTheRightMargin(panelWidth) && newXOffset > xOffset);
  }

  private boolean isLeftOfTheRightMargin(int panelWidth)
  {
    return cursorX < panelWidth - SIDE_DETECTION_MARGIN;
  }


  public void shiftTextRight()
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

  public void setCursorAndSelectionStartX()
  {
    cursorX = getXPosFromIndex(cursorIndex);
    selectionStartX = getXPosFromIndex(selectionIndex);
  }

  public int getXPosFromIndex(int index)
  {
    String toIndexString = text.substring(0, index);
    if (index <= 0)
      return LEFT_TEXT_MARGIN;
    else
      return getXPosFromTextLayout(toIndexString) + getTerminatingSpaceWidth(toIndexString);
  }


  public int getXPosFromTextLayout(String toIndexString)
  {
    TypedLayout layout = new TextLayoutImpl(toIndexString, font, TextPanel.getRenderContext());
    int x = getWidthDimension(layout) + LEFT_TEXT_MARGIN - xOffset;
    if (x < LEFT_TEXT_MARGIN)
      x = LEFT_TEXT_MARGIN;
    return x;
  }

  public int getTerminatingSpaceWidth(String string)
  {
    int totalSpaceWidth = 0;
    if (string.charAt(string.length() - 1) == ' ')
    {
      int i = string.length() - 1;

      while (i > 0 && string.charAt(i) == ' ')
      {
        totalSpaceWidth += 3;
        i--;
      }
    }
    return totalSpaceWidth;
  }

  public Dimension calculateTextDimensions()
  {
    if (text != null && text.length() > 0)
    {
      int height = 0;
      int width = 0;
      ArrayList<TypedLayout> textLayouts = getTextLayouts();
      for (int i = 0; i < textLayouts.size(); i++)
      {
        height += getHeightDimension(textLayouts.get(i));
        int dimWidth = getWidthDimension(textLayouts.get(i));
        if (width < dimWidth)
          width = dimWidth;
      }
      return new Dimension(width, height);
    }
    return null;
  }

  public int getHeightDimension(TypedLayout layout)
  {
    return (int) ((layout.getAscent() + layout.getDescent() + layout.getLeading()) + 0.5);

  }

  public int getWidthDimension(TypedLayout layout)
  {
    return (int) (layout.getBounds().getWidth() + layout.getBounds().getX() + 0.5);

  }

  public int getXOffset()
  {
    return xOffset;
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

  public String concatenateAllLayoutText()
  {
   StringBuffer string = new StringBuffer();

    for(int i = 0; i < textLayouts.size(); i++)
    {
      string.append(textLayouts.get(i).getText());
    }
    return string.toString();
  }

  public void setText(String text)
  {
    this.text = new StringBuffer(text);
    setCursorIndex(text.length());
  }

  public String getText()
  {
    if (text == null)
      return null;
    return text.toString();
  }

  public Point getPanelAbsoluteLocation()
  {
    return myBox.getAbsoluteLocation();
  }

  public int getPanelWidth()
  {
    return myBox.getWidth();
  }

  public int getPanelHeight()
  {
    return myBox.getHeight();
  }

  public Shape getPaintableRegion()
  {
    return myBox.getPaintableRegion();
  }

  public SimpleHorizontalAlignmentAttribute getHorizontalAlignment()
  {
    return myBox.horizontalTextAlignment;
  }

  public SimpleVerticalAlignmentAttribute getVerticalAlignment()
  {
    return myBox.verticalTextAlignment;
  }

  public boolean isCursorOn()
  {
    return myBox.isCursorOn();
  }

  public boolean isFocused()
  {
    return myBox.isFocused();
  }

  public void copyText(String clipboard)
  {
    StringSelection stringSelection = new StringSelection(clipboard);
    Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    systemClipboard.setContents(stringSelection, this);
  }

  public String getClipboardContents()
  {
    String clipboardString = "";
    Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    Transferable contents = systemClipboard.getContents(null);
    boolean hasText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
    if (hasText)
    {
      try
      {
        clipboardString = (String) contents.getTransferData(DataFlavor.stringFlavor);
      }
      catch (UnsupportedFlavorException e)
      {
        e.printStackTrace();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    return clipboardString;
  }

  public void copySelection()
  {
    String clipboard;
    if (selectionIndex < cursorIndex)
      clipboard = text.substring(selectionIndex, cursorIndex);
    else
      clipboard = text.substring(cursorIndex, selectionIndex);
    copyText(clipboard);
  }

  public void pasteClipboard()
  {
    String clipboard = getClipboardContents();
    if (clipboard != null && clipboard.length() > 0)
    {
      text.insert(cursorIndex, clipboard);
      setCursorIndex(cursorIndex + clipboard.length());
    }
  }

  public void cutSelection()
  {
    copySelection();
    deleteSelection();
  }

  public void deleteSelection()
  {
    if (selectionIndex < cursorIndex)
      deleteEnclosedText(selectionIndex, cursorIndex);
    else
      deleteEnclosedText(cursorIndex, selectionIndex);
  }

  public void deleteEnclosedText(int first, int second)
  {
    text.delete(first, second);
    setCursorIndex(first);
    setSelectionIndex(0);
  }

  public Rectangle getSelectionRegion()
  {
    if (text.length() > 0)
      calculateTextXOffset(myBox.getWidth(), calculateTextDimensions().width);
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

  public int findWordsRightEdge(int index)
  {
    for (int i = index; i <= text.length() - 1; i++)
    {
      if (i == 0)
        i = 1;
      if (text.charAt(i - 1) != ' ' && text.charAt(i) == ' ')
        return i;
    }
    return text.length();
  }

  public int findWordsLeftEdge(int index)
  {
    for (int i = index; i > 1; i--)
    {
      if (text.charAt(i - 1) == ' ' && text.charAt(i) != ' ')
        return i;
    }
    return 0;
  }

  public int getCursorIndex()
  {
    return cursorIndex;
  }

  public void setCursorIndex(int cursorIndex)
  {
    this.cursorIndex = cursorIndex;
    myBox.setPaintableRegion(cursorIndex);
  }

  public int getSelectionIndex()
  {
    return selectionIndex;
  }

  public void setSelectionIndex(int selectionIndex)
  {
    this.selectionIndex = selectionIndex;
    myBox.setPaintableRegion(selectionIndex);
  }

  public boolean isBoxFull()
  {
    if (text.length() > 0)
      return (myBox.getWidth() - TextModel.SIDE_DETECTION_MARGIN * 2 <= calculateTextDimensions().width);
    return false;
  }
}
