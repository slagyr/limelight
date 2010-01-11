package limelight.ui.model.inputs;

import limelight.ui.model.*;
import limelight.util.Box;
import limelight.styles.Style;
import limelight.styles.HorizontalAlignment;
import limelight.styles.VerticalAlignment;
import limelight.styles.styling.SimpleHorizontalAlignmentAttribute;
import limelight.styles.styling.SimpleVerticalAlignmentAttribute;
import limelight.Context;

import java.awt.datatransfer.*;
import java.awt.event.MouseEvent;
import java.awt.event.FocusEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.*;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;
import java.io.IOException;

public class TextBox2Panel extends BasePanel implements TextAccessor, InputPanel, ClipboardOwner
{
  private boolean focused;
  private StringBuffer text;
  private static SimpleHorizontalAlignmentAttribute horizontalTextAlignment = new SimpleHorizontalAlignmentAttribute(HorizontalAlignment.LEFT);
  private static SimpleVerticalAlignmentAttribute verticalTextAlignment = new SimpleVerticalAlignmentAttribute(VerticalAlignment.CENTER);
  private Dimension textDimensions;
  private TextLayout textLayout;
  private static Font font = new Font("Arial", Font.PLAIN, 12);
  private boolean cursorOn;
  private int cursorX = 0;
  private Thread cursorThread;
  private int cursorIndex = 0;
  private boolean selectingTextOn;
  private int selectStartX;
  private int textX = 3;
  private int xOffset;
  private int selectStartIndex;
  private int mouseClickIndex;
  private int mouseReleaseIndex;
  private boolean mouseSelectingTextOn;
  private boolean clickWasInBox;

  public TextBox2Panel()
  {
    setSize(150, 25);
  }

  public void setParent(limelight.ui.Panel panel)
  {
    if (panel == null)
      Context.instance().keyboardFocusManager.focusFrame((StageFrame) getRoot().getStageFrame());
    super.setParent(panel);
    if (panel instanceof PropPanel)
    {
      PropPanel propPanel = (PropPanel) panel;
      propPanel.sterilize();
      propPanel.setTextAccessor(this);
    }
  }

  public Box getBoxInsidePadding()
  {
    return getBoundingBox();
  }

  public Box getChildConsumableArea()
  {
    return getBoundingBox();
  }

  public void paintOn(Graphics2D graphics)
  {
    graphics.setColor(Color.lightGray);
    graphics.fillRect(0, 0, width, height);

    if (focused)
      graphics.setColor(Color.green);
    else
      graphics.setColor(Color.gray);

    graphics.drawRect(0, 0, width - 1, height - 1);

    if (text != null && text.length() > 0)
    {
      graphics.setColor(Color.BLACK);
      calculateTextDimensions();

      if (textDimensions.width >= width)
      {
        int newXOffset = textDimensions.width - width + 7;
        if (!(cursorX < width - 4 && newXOffset > xOffset))
          xOffset = newXOffset;
        if (cursorX < 4)
          shiftCursorAndTextRight();
      }
      else
        xOffset = 0;

      textX = horizontalTextAlignment.getX(textDimensions.width, getBoundingBox()) + 3 - xOffset;

      float textY = verticalTextAlignment.getY(textDimensions.height, getBoundingBox()) + textLayout.getAscent();
      setCursorAndStartSelectPosition();
      if (selectingTextOn)
      {
        graphics.setColor(Color.cyan);
        if (cursorX > selectStartX)
          graphics.fillRect(selectStartX, 4, cursorX - selectStartX, height - 8);
        else
          graphics.fillRect(cursorX, 4, selectStartX - cursorX, height - 8);

      }
      graphics.setColor(Color.black);
      textLayout.draw(graphics, textX, textY + 1);
    }

    else
      cursorX = 2;

    if (cursorOn)

    {
      graphics.setColor(Color.black);
      graphics.drawLine(cursorX, 4, cursorX, height - 4);
    }

  }

  private void shiftCursorAndTextRight()
  {
    String rightShiftingText = text.substring(cursorIndex / 2, cursorIndex);
    if (rightShiftingText.length() == 0)
    {
      cursorX = 2;
      xOffset = 0;
    }
    else
    {
      TextLayout textLayout = this.textLayout;
      this.textLayout = new TextLayout(rightShiftingText, font, TextPanel.staticFontRenderingContext);
      int x = getWidthDimension();
      xOffset -= x;
      if (xOffset < 0)
        xOffset = 0;
      cursorX += x;

      this.textLayout = textLayout;
    }
  }

  private void setCursorAndStartSelectPosition()
  {
    cursorX = getXPositionFromIndex(cursorIndex);
    selectStartX = getXPositionFromIndex(selectStartIndex);
  }

  private int getXPositionFromIndex(int index)
  {
    String toIndexString = text.substring(0, index);
    if (toIndexString.length() == 0)
      return 2;
    else
      return getCursorXFromTextLayout(toIndexString) + getTerminatingSpaceWidth(toIndexString);

  }

  private int getCursorXFromTextLayout(String toCursorString)
  {
    TextLayout textLayout = this.textLayout;
    this.textLayout = new TextLayout(toCursorString, font, TextPanel.staticFontRenderingContext);
    int x = getWidthDimension();
    this.textLayout = textLayout;
    return x + 3 - xOffset;
  }

  private int getTerminatingSpaceWidth(String toCursorString)
  {
    int spaceWidth = 0;
    if (toCursorString.charAt(toCursorString.length() - 1) == ' ')
    {
      int i = toCursorString.length() - 1;

      while (toCursorString.charAt(i) == ' ' && i > 0)
      {
        spaceWidth += 3;
        i--;
      }
    }
    return spaceWidth;
  }

  private void calculateTextDimensions()
  {
    if (text != null && text.length() > 0)
    {
      textLayout = new TextLayout(text.toString(), font, TextPanel.staticFontRenderingContext);
      int height = getHeightDimension();
      int width = getWidthDimension();
      textDimensions = new Dimension(width, height);
    }
  }

  private int getHeightDimension()
  {
    return (int) ((textLayout.getAscent() + textLayout.getDescent() + textLayout.getLeading()) + 0.5);
  }

  private int getWidthDimension()
  {
    return (int) (textLayout.getBounds().getWidth() + textLayout.getBounds().getX() + 0.5);
  }

  public Style getStyle()
  {
    return getParent().getStyle();
  }

  public void setText(String text)
  {
    this.text = new StringBuffer(text);
    cursorIndex = text.length();
  }

  public String getText()
  {
    if (text == null)
      return null;
    return text.toString();
  }

  public void mouseDragged(MouseEvent e)
  {
    int myX = e.getX() - this.absoluteLocation.x;
    int myY = e.getY() - this.absoluteLocation.y;
    if (clickWasInBox)
    {
      cursorIndex = calculateMouseClickIndex(myX, myY);
      markAsDirty();
    }
  }

  public void mousePressed(MouseEvent e)
  {
    super.mousePressed(e);
    int myX = e.getX() - this.absoluteLocation.x;
    int myY = e.getY() - this.absoluteLocation.y;
    clickWasInBox = false;
    if (clickIsWithinBox(e, myX, myY))
    {
      clickWasInBox = true;
      selectingTextOn = true;
      selectStartIndex = calculateMouseClickIndex(myX, myY);
      if (selectStartIndex > 0)
        selectStartX = getCursorXFromTextLayout(text.substring(0, selectStartIndex));
      else
        selectStartX = 0;
      cursorIndex = selectStartIndex;
    }
    else
    {
      clickWasInBox = false;
    }
    markAsDirty();

  }

  private int calculateMouseClickIndex(int myX, int myY)
  {
    TextHitInfo hitInfo = textLayout.hitTestChar(myX, myY);
    int index = hitInfo.getCharIndex();
    if (index < hitInfo.getInsertionIndex() && index == text.length() - 1)
      index += 1;
    System.out.println("hitInfo.getInsertionIndex() = " + hitInfo.getInsertionIndex());
    System.out.println("hitInfo.isLeadingEdge = " + hitInfo.isLeadingEdge());
    System.out.println("index = " + index);
    return index;
  }


  private boolean clickIsWithinBox(MouseEvent e, int myX, int myY)
  {
    return isWithinMyXRange(e, myX) && isWithinMyYRange(e, myY);
  }

  private boolean isWithinMyYRange(MouseEvent e, int myY)
  {
    return myY > 0 && myY < height;
  }

  private boolean isWithinMyXRange(MouseEvent e, int myX)
  {
    return myX > 0 && myX < width;
  }

  public void mouseReleased(MouseEvent e)
  {
    super.mouseReleased(e);
    int myX = e.getX() - this.absoluteLocation.x;
    int myY = e.getY() - this.absoluteLocation.y;
    cursorIndex = calculateMouseClickIndex(myX, myY);
    if (cursorIndex == selectStartIndex)
      selectingTextOn = false;
    System.out.println("cursorIndex = " + cursorIndex);
    markAsDirty();
    Context.instance().keyboardFocusManager.focusPanel(this);
    focusGained(new FocusEvent(getRoot().getStageFrame().getWindow(), 0));
    super.buttonPressed(new ActionEvent(this, 0, "blah"));
  }

  public void focusGained(FocusEvent e)
  {
    focused = true;
    markAsDirty();
    startCursor();
    super.focusGained(e);
  }

  public void focusLost(FocusEvent e)
  {
    focused = false;
    stopCursor();
    markAsDirty();
    super.focusLost(e);

  }

  public void keyPressed(KeyEvent e)
  {
    int key = e.getKeyCode();

    if (key == KeyEvent.VK_BACK_SPACE)
    {

      if (!deletedTextInSelection())
      {
        if (cursorIndex >= 0)
        {
          text.deleteCharAt(cursorIndex - 1);
          cursorIndex--;
        }
      }
      selectStartIndex = 0;
    }
    else if (e.getModifiers() == 4)
    {
      if (key == KeyEvent.VK_C && selectingTextOn)
      {
        copySelectedText();
      }
      else if (key == KeyEvent.VK_V)
      {
        if (deletedTextInSelection())
          selectingTextOn = false;
        String clipboard = getClipboardContents();
        if (clipboard != null && clipboard.length() > 0)
        {
          text.insert(cursorIndex, clipboard);
          cursorIndex += clipboard.length();
        }
      }
      else if (key == KeyEvent.VK_X && selectingTextOn)
      {
        copySelectedText();
        if (deletedTextInSelection())
          selectingTextOn = false;
      }
      else if (key == KeyEvent.VK_A)
      {
        selectingTextOn = true;
        cursorIndex = 0;
        selectStartIndex = text.length();
      }
      else if (key == KeyEvent.VK_RIGHT)
        cursorIndex = text.length();
      else if (key == KeyEvent.VK_LEFT)
        cursorIndex = 0;
    }
    else if (e.getModifiers() == 5)
    {
      if (key == KeyEvent.VK_RIGHT)
      {
        if (!selectingTextOn)
          selectStartIndex = cursorIndex;
        System.out.println("selectStartIndex = " + selectStartIndex);
        selectingTextOn = true;
        cursorIndex = text.length();
      }
      else if (key == KeyEvent.VK_LEFT)
      {
        if (!selectingTextOn)
          selectStartIndex = cursorIndex;
        System.out.println("selectStartIndex = " + selectStartIndex);
        selectingTextOn = true;
        cursorIndex = 0;
      }
      System.out.println("cursorIndex + selectingTextOn = " + cursorIndex + selectingTextOn);
    }
    else if ((key > 40 && key < 100 || key == 222 || key == 32) && e.getModifiers() != 2)
    {
      if (deletedTextInSelection())
        selectingTextOn = false;
      text.insert(cursorIndex, e.getKeyChar());
      cursorIndex++;

    }
    else if (key == KeyEvent.VK_LEFT && cursorIndex > 0)
    {
      if (e.getModifiers() == 1 && !selectingTextOn)
      {
        initializeSelection();
      }
      cursorIndex--;
    }
    else if (key == KeyEvent.VK_RIGHT && cursorIndex < text.length())
    {
      if (e.getModifiers() == 1 && !selectingTextOn)
      {
        initializeSelection();
      }
      cursorIndex++;
    }

    if (e.getModifiers() % 2 != 1 && (key != KeyEvent.VK_META && key != KeyEvent.VK_ALT && key != KeyEvent.VK_CONTROL))
      selectingTextOn = false;
    System.out.println("key = " + key + " modifiers= " + e.getModifiers() + " selecting? = " + selectingTextOn);
    markAsDirty();
  }

  private String getClipboardContents()
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

  private void copySelectedText()
  {
    String clipboard;
    if (selectStartIndex < cursorIndex)
      clipboard = text.substring(selectStartIndex, cursorIndex);
    else
      clipboard = text.substring(cursorIndex, selectStartIndex);
    StringSelection stringSelection = new StringSelection(clipboard);
    Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    systemClipboard.setContents(stringSelection, this);
  }

  private boolean deletedTextInSelection()
  {
    if (selectingTextOn)
    {
      if (selectStartIndex < cursorIndex)
        deleteSelectedText(selectStartIndex, cursorIndex);
      else
        deleteSelectedText(cursorIndex, selectStartIndex);
      return true;
    }
    return false;
  }

  private void deleteSelectedText(int first, int second)
  {
    text.delete(first, second);
    System.out.println("first = " + first + " second = " + second);
    cursorIndex = first;
  }

  private void initializeSelection()
  {
    selectingTextOn = true;
    selectStartX = cursorX;
    selectStartIndex = cursorIndex;
  }

  public void keyReleased(KeyEvent e)
  {

  }


  private void startCursor()
  {
    if (cursorThread == null || !cursorThread.isAlive())
    {
      cursorThread = new Thread(new Runnable()
      {
        public void run()
        {
          while (true)
          {
            cursorOn = !cursorOn;
            markAsDirty();

            try
            {
              Thread.sleep(500);
            }
            catch (InterruptedException e)
            {
              e.printStackTrace();
            }
          }
        }
      });
      cursorThread.start();
    }
  }

  private void stopCursor()
  {
    cursorThread.interrupt();
    cursorOn = false;
    markAsDirty();
  }

  public void lostOwnership(Clipboard clipboard, Transferable contents)
  {
    //This doesn't have to do anything... how unsettling.
  }
}
