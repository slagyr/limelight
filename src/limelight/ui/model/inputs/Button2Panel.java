package limelight.ui.model.inputs;

import limelight.ui.model.*;
import limelight.util.Box;
import limelight.util.Colors;
import limelight.styles.Style;
import limelight.styles.HorizontalAlignment;
import limelight.styles.VerticalAlignment;
import limelight.styles.styling.SimpleHorizontalAlignmentAttribute;
import limelight.styles.styling.SimpleVerticalAlignmentAttribute;
import limelight.Context;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.FocusEvent;
import java.awt.event.ActionEvent;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Button2Panel extends BasePanel implements TextAccessor, InputPanel
{
  private static Font font = new Font("Arial", Font.BOLD, 12);
  private static ButtonStyle normal = new ButtonStyle("button", "#e0e0e0");
  private static ButtonStyle selected = new ButtonStyle("button_selected", "#bbd453");
  private static ButtonStyle focusedStyle = new ButtonStyle("button_focus", "transparent");

  private static SimpleHorizontalAlignmentAttribute horizontalTextAlignment = new SimpleHorizontalAlignmentAttribute(HorizontalAlignment.CENTER);

  private static SimpleVerticalAlignmentAttribute verticalTextAlignment = new SimpleVerticalAlignmentAttribute(VerticalAlignment.CENTER);
  private String text;
  private ButtonStyle style;
  private Rectangle textBounds;
  private Dimension textDimensions;
  private TextLayout textLayout;
  private boolean focused;

  public Button2Panel()
  {
    style = normal;
    setSize(128, 29);
  }

  public void setParent(limelight.ui.Panel panel)
  {
    if(panel == null)
      Context.instance().keyboardFocusManager.focusFrame((StageFrame) getRoot().getStageFrame());
    super.setParent(panel);
    if(panel instanceof PropPanel)
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
    if(focused)
      focusedStyle.paintOn(graphics, this);
    style.paintOn(graphics, this);

    graphics.setColor(Color.BLACK);
    calculateTextDimentions();
    int textX = horizontalTextAlignment.getX(textDimensions.width, getBoundingBox());
    float textY = verticalTextAlignment.getY(textDimensions.height, getBoundingBox()) + textLayout.getAscent();
    textLayout.draw(graphics, textX, textY + 1);
  }

  private void calculateTextDimentions()
  {
    if(textBounds == null)
    {
      textLayout = new TextLayout(text, font, TextPanel.staticFontRenderingContext);
      int height = (int) ((textLayout.getAscent() + textLayout.getDescent() + textLayout.getLeading()) + 0.5);
      int width = (int) (textLayout.getBounds().getWidth() + textLayout.getBounds().getX() + 0.5);
      textDimensions = new Dimension(width, height);
    }
  }

  public Style getStyle()
  {
    return getParent().getStyle();
  }

  public void setText(String text)
  {
    this.text = text;
    textBounds = null;
  }

  public String getText()
  {
    return text;
  }

  public void mousePressed(MouseEvent e)
  {
System.err.println("e = " + e);
    style = selected;
    repaint();
  }

  public void mouseReleased(MouseEvent e)
  {
System.err.println("e = " + e);    
    style = normal;
    repaint();
    super.buttonPressed(new ActionEvent(this, 0, "blah"));
  }

  public void focusGained(FocusEvent e)
  {
    focused = true;
    repaint();
    super.focusGained(e);
  }

  public void focusLost(FocusEvent e)
  {
    focused = false;
    repaint();
    super.focusLost(e);
  }

  private static class ButtonStyle
  {
    private String prefix;
    private boolean loaded;

    private ButtonStyle(String prefix, String interiorColor)
    {
      this.prefix = prefix;
      this.interior = Colors.resolve(interiorColor);
    }

    public BufferedImage topLeftImage;
    public BufferedImage topImage;
    public BufferedImage topRightImage;
    public BufferedImage rightImage;
    public BufferedImage bottomRightImage;
    public BufferedImage bottomImage;
    public BufferedImage bottomLeftImage;
    public BufferedImage leftImage;
    public Color interior;


    public void load()
    {
      if(loaded)
        return;
      try
      {
        topLeftImage = ImageIO.read(getClass().getClassLoader().getResource("limelight/ui/images/" + prefix + "_tl.png"));
        topImage = ImageIO.read(getClass().getClassLoader().getResource("limelight/ui/images/" + prefix + "_t.png"));
        topRightImage = ImageIO.read(getClass().getClassLoader().getResource("limelight/ui/images/" + prefix + "_tr.png"));
        rightImage = ImageIO.read(getClass().getClassLoader().getResource("limelight/ui/images/" + prefix + "_r.png"));
        bottomRightImage = ImageIO.read(getClass().getClassLoader().getResource("limelight/ui/images/" + prefix + "_br.png"));
        bottomImage = ImageIO.read(getClass().getClassLoader().getResource("limelight/ui/images/" + prefix + "_b.png"));
        bottomLeftImage = ImageIO.read(getClass().getClassLoader().getResource("limelight/ui/images/" + prefix + "_bl.png"));
        leftImage = ImageIO.read(getClass().getClassLoader().getResource("limelight/ui/images/" + prefix + "_l.png"));
        loaded = true;
      }
      catch(IOException e)
      {
        throw new RuntimeException(e);
      }
    }

    public void paintOn(Graphics2D graphics, Button2Panel button)
    {
      load();
      int innerTopY = topImage.getHeight();
      int innerBottomY = button.getHeight() - bottomImage.getHeight();
      int innerLeftX = leftImage.getWidth();
      int innerRightX = button.getWidth() - rightImage.getWidth();

      graphics.drawImage(topLeftImage, 0, 0, null);
      graphics.drawImage(topRightImage, innerRightX, 0, null);
      graphics.drawImage(bottomLeftImage, 0, innerBottomY, null);
      graphics.drawImage(bottomRightImage, innerRightX, innerBottomY, null);
      for(int x = innerLeftX; x < innerRightX; x++)
      {
        graphics.drawImage(topImage, x, 0, null);
        graphics.drawImage(bottomImage, x, innerBottomY, null);
      }
      for(int y = innerTopY; y < innerBottomY; y++)
      {
        graphics.drawImage(leftImage, 0, y, null);
        graphics.drawImage(rightImage, innerRightX, y, null);
      }
      graphics.setColor(interior);
      Rectangle interiorArea = new Rectangle(innerLeftX, innerTopY, innerRightX - innerLeftX, innerBottomY - innerTopY);
      graphics.fill(interiorArea);
    }
  }
}
