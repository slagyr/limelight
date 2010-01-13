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
import java.io.IOException;

public class TextBox2Panel extends TextInputPanel
{

  private static SimpleHorizontalAlignmentAttribute horizontalTextAlignment = new SimpleHorizontalAlignmentAttribute(HorizontalAlignment.LEFT);
  private static SimpleVerticalAlignmentAttribute verticalTextAlignment = new SimpleVerticalAlignmentAttribute(VerticalAlignment.CENTER);


  public TextBox2Panel()
  {
    setSize(150, 25);
  }


  public void paintOn(Graphics2D graphics)
  {
    graphics.setColor(Color.lightGray);
    graphics.fillRect(0, 0, width, height);
    Dimension textDimensions;

    if (focused)
      graphics.setColor(Color.green);
    else
      graphics.setColor(Color.gray);

    graphics.drawRect(0, 0, width - 1, height - 1);

    if (boxState.text != null && boxState.text.length() > 0)
    {
      graphics.setColor(Color.BLACK);
      textDimensions = boxState.calculateTextDimensions();

      boxState.calculateTextXOffset(width, textDimensions.width);

      int textX = horizontalTextAlignment.getX(textDimensions.width, getBoundingBox()) + TextModel.LEFT_TEXT_MARGIN - boxState.xOffset;

      float textY = verticalTextAlignment.getY(textDimensions.height, getBoundingBox()) + boxState.textLayout.getAscent();
      boxState.setCursorAndSelectionStartX();
      if (boxState.selectionOn)
      {
        graphics.setColor(Color.cyan);
        if (boxState.cursorX > boxState.selectionStartX)
          graphics.fillRect(boxState.selectionStartX, 4, boxState.cursorX - boxState.selectionStartX, height - 8);
        else
          graphics.fillRect(boxState.cursorX, 4, boxState.selectionStartX - boxState.cursorX, height - 8);

      }
      graphics.setColor(Color.black);
      boxState.textLayout.draw(graphics, textX, textY + 1);
    }

    else
      boxState.cursorX = 2;

    if (cursorOn)

    {
      graphics.setColor(Color.black);
      graphics.drawLine(boxState.cursorX, 4, boxState.cursorX, height - 4);
    }

  }


  public void mouseDragged(MouseEvent e)
  {
    int myX = e.getX() - this.absoluteLocation.x;
    int myY = e.getY() - this.absoluteLocation.y;
    if (clickWasInBox)
    {
      boxState.cursorIndex = calculateMouseClickIndex(myX, myY);
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
      boxState.selectionOn = true;
      boxState.selectionIndex = calculateMouseClickIndex(myX, myY);
      if (boxState.selectionIndex > 0)
        boxState.selectionStartX = boxState.getXPosFromTextLayout(boxState.text.substring(0, boxState.selectionIndex));
      else
        boxState.selectionStartX = 0;
      boxState.cursorIndex = boxState.selectionIndex;
    }
    else
    {
      clickWasInBox = false;
    }
    markAsDirty();

  }

  private int calculateMouseClickIndex(int myX, int myY)
  {
    TextHitInfo hitInfo = boxState.textLayout.hitTestChar(myX, myY);
    int index = hitInfo.getCharIndex();
    if (index < hitInfo.getInsertionIndex() && index == boxState.text.length() - 1)
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
    boxState.cursorIndex = calculateMouseClickIndex(myX, myY);
    if (boxState.cursorIndex == boxState.selectionIndex)
      boxState.selectionOn = false;
    System.out.println("boxState.cursorIndex = " + boxState.cursorIndex);
    markAsDirty();
    Context.instance().keyboardFocusManager.focusPanel(this);
    focusGained(new FocusEvent(getRoot().getStageFrame().getWindow(), 0));
    super.buttonPressed(new ActionEvent(this, 0, "blah"));
  }



  public void keyPressed(KeyEvent e)
  {
    int key = e.getKeyCode();

    if (key == KeyEvent.VK_BACK_SPACE)
    {
                     
      if (!deletedTextInSelection())
      {
        if (boxState.cursorIndex >= 0)
        {
          boxState.text.deleteCharAt(boxState.cursorIndex - 1);
          boxState.cursorIndex--;
        }
      }
      boxState.selectionIndex = 0;
    }
    else if (e.getModifiers() == 4)
    {
      if (key == KeyEvent.VK_C && boxState.selectionOn)
      {
        copySelectedText();
      }
      else if (key == KeyEvent.VK_V)
      {
        if (deletedTextInSelection())
          boxState.selectionOn = false;
        String clipboard = getClipboardContents();
        if (clipboard != null && clipboard.length() > 0)
        {
          boxState.text.insert(boxState.cursorIndex, clipboard);
          boxState.cursorIndex += clipboard.length();
        }
      }
      else if (key == KeyEvent.VK_X && boxState.selectionOn)
      {
        copySelectedText();
        if (deletedTextInSelection())
          boxState.selectionOn = false;
      }
      else if (key == KeyEvent.VK_A)
      {
        boxState.selectionOn = true;
        boxState.cursorIndex = 0;
        boxState.selectionIndex = boxState.text.length();
      }
      else if (key == KeyEvent.VK_RIGHT)
        boxState.cursorIndex = boxState.text.length();
      else if (key == KeyEvent.VK_LEFT)
        boxState.cursorIndex = 0;
    }
    else if (e.getModifiers() == 5)
    {
      if (key == KeyEvent.VK_RIGHT)
      {
        if (!boxState.selectionOn)
          boxState.selectionIndex = boxState.cursorIndex;
        boxState.selectionOn = true;
        boxState.cursorIndex = boxState.text.length();
      }
      else if (key == KeyEvent.VK_LEFT)
      {
        if (!boxState.selectionOn)
          boxState.selectionIndex = boxState.cursorIndex;
        boxState.selectionOn = true;
        boxState.cursorIndex = 0;
      }
    }
    else if ((key > 40 && key < 100 || key == 222 || key == 32) && e.getModifiers() != 2)
    {
      if (deletedTextInSelection())
        boxState.selectionOn = false;
      boxState.text.insert(boxState.cursorIndex, e.getKeyChar());
      boxState.cursorIndex++;
      boxState.selectionIndex = 0;

    }
    else if (key == KeyEvent.VK_LEFT && boxState.cursorIndex > 0)
    {
      if (e.getModifiers() == 1 && !boxState.selectionOn)
      {
        initializeSelection();
      }
      boxState.cursorIndex--;
    }
    else if (key == KeyEvent.VK_RIGHT && boxState.cursorIndex < boxState.text.length())
    {
      if (e.getModifiers() == 1 && !boxState.selectionOn)
      {
        initializeSelection();
      }
      boxState.cursorIndex++;
    }

    if (e.getModifiers() % 2 != 1 && (key != KeyEvent.VK_META && key != KeyEvent.VK_ALT && key != KeyEvent.VK_CONTROL))
      boxState.selectionOn = false;
    System.out.println("key = " + key + " modifiers= " + e.getModifiers() + " selecting? = " + boxState.selectionOn);
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
    if (boxState.selectionIndex < boxState.cursorIndex)
      clipboard = boxState.text.substring(boxState.selectionIndex, boxState.cursorIndex);
    else
      clipboard = boxState.text.substring(boxState.cursorIndex, boxState.selectionIndex);
    StringSelection stringSelection = new StringSelection(clipboard);
    Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    systemClipboard.setContents(stringSelection, this);
  }

  private boolean deletedTextInSelection()
  {
    if (boxState.selectionOn)
    {
      if (boxState.selectionIndex < boxState.cursorIndex)
        deleteSelectedText(boxState.selectionIndex, boxState.cursorIndex);
      else
        deleteSelectedText(boxState.cursorIndex, boxState.selectionIndex);
      return true;
    }
    return false;
  }

  private void deleteSelectedText(int first, int second)
  {
    boxState.text.delete(first, second);
    System.out.println("first = " + first + " second = " + second);
    boxState.cursorIndex = first;
  }

  private void initializeSelection()
  {
    boxState.selectionOn = true;
    boxState.selectionStartX = boxState.cursorX;
    boxState.selectionIndex = boxState.cursorIndex;
  }

  public void keyReleased(KeyEvent e)
  {

  }




  public void lostOwnership(Clipboard clipboard, Transferable contents)
  {
    //This doesn't have to do anything... how unsettling.
  }
}
