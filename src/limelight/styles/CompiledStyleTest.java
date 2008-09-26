package limelight.styles;

import junit.framework.TestCase;
import limelight.util.Colors;
import limelight.ui.painting.RepeatingImageFillStrategy;

public class CompiledStyleTest extends TestCase implements StyleObserver
{
  private FlatStyle target;
  private CompiledStyle style;

  public void setUp() throws Exception
  {
    target = new FlatStyle();
    style = new CompiledStyle(target);
  }

  public void testDecoratesAnotherStyle() throws Exception
  {
    target.setWidth("100");
    assertEquals("100", style.getWidth());

    style.setHeight("123");
    assertEquals("123", target.getHeight());

    target.setDefault(Style.MAX_HEIGHT, "123");
    assertEquals("123", style.getDefaultValue(Style.MAX_HEIGHT));

    style.setDefault(Style.MAX_WIDTH, "321");
    assertEquals("321", target.getDefaultValue(Style.MAX_WIDTH));

    style.flushChanges();
    assertEquals(0, target.getChangedCount());
    assertEquals(0, style.getChangedCount());

    style.setMinHeight("456");
    assertEquals(true, target.changed());
    assertEquals(true, style.changed());
    assertEquals(true, target.changed(Style.MIN_HEIGHT));
    assertEquals(true, style.changed(Style.MIN_HEIGHT));
    assertEquals(1, target.getChangedCount());
    assertEquals(1, style.getChangedCount());

    style.addObserver(this);
    assertEquals(true, target.hasObserver(this));
    assertEquals(true, style.hasObserver(this));
    style.removeObserver(this);
    assertEquals(false, target.hasObserver(this));
  }

  public void styleChanged(StyleDescriptor descriptor, String value)
  {
  }
  
  public void testIsAnObserverOfItsTarget() throws Exception
  {
    assertEquals(true, target.hasObserver(style));
  }
//
//  public void testCompileDefaults() throws Exception
//  {
//    assertEquals(null, style.width);
//    assertEquals(null, style.height);
//    assertEquals(null, style.minWidth);
//    assertEquals(null, style.minHeight);
//    assertEquals(null, style.maxWidth);
//    assertEquals(null, style.maxHeight);
//    assertEquals(null, style.verticalScrollbar);
//    assertEquals(null, style.horizontalScrollbar);
//    assertEquals(Colors.resolve("black"), style.topBorderColor);
//    assertEquals(Colors.resolve("black"), style.rightBorderColor);
//    assertEquals(Colors.resolve("black"), style.bottomBorderColor);
//    assertEquals(Colors.resolve("black"), style.leftBorderColor);
//    assertEquals(0, style.topBorderWidth);
//    assertEquals(0, style.rightBorderWidth);
//    assertEquals(0, style.bottomBorderWidth);
//    assertEquals(0, style.leftBorderWidth);
//    assertEquals(0, style.topMargin);
//    assertEquals(0, style.rightMargin);
//    assertEquals(0, style.bottomMargin);
//    assertEquals(0, style.leftMargin);
//    assertEquals(0, style.topPadding);
//    assertEquals(0, style.rightPadding);
//    assertEquals(0, style.bottomPadding);
//    assertEquals(0, style.leftPadding);
//    assertEquals(Colors.resolve("transparent"), style.backgroundColor);
//    assertEquals(Colors.resolve("transparent"), style.secondaryBackgroundColor);
//    assertEquals(null, style.backgroundImage);
//    assertEquals(RepeatingImageFillStrategy.class, style.backgroundImageFillStrategy);
//    assertEquals(null, style.gradient);
//    assertEquals(90, style.gradientAngle);
//    assertEquals(100, style.gradientPenetration);
//    assertEquals(null, style.cyclicGradient);
//    assertEquals(null, style.horizontalAlignment);
//    assertEquals(null, style.verticalAlignment);
//    assertEquals(Colors.resolve("black"), style.textColor);
//    assertEquals("Arial", style.fontFace);
//    assertEquals(12, style.fontSize);
//    assertEquals("plain", style.fontStyle);
//    assertEquals(0, style.transparency);
//    assertEquals(0, style.topRightRoundedCornerRadius);
//    assertEquals(0, style.bottomRightRoundedCornerRadius);
//    assertEquals(0, style.bottomLeftRoundedCornerRadius);
//    assertEquals(0, style.topLeftRoundedCornerRadius);
//    assertEquals(0, style.topRightBorderWidth);
//    assertEquals(0, style.bottomRightBorderWidth);
//    assertEquals(0, style.bottomLeftBorderWidth);
//    assertEquals(0, style.topLeftBorderWidth);
//    assertEquals(Colors.resolve("black"), style.topRightBorderColor);
//    assertEquals(Colors.resolve("black"), style.bottomRightBorderColor);
//    assertEquals(Colors.resolve("black"), style.bottomLeftBorderColor);
//    assertEquals(Colors.resolve("black"), style.topLeftBorderColor);
//    assertEquals(null, style.floating);
//    assertEquals(0, style.x);
//    assertEquals(0, style.y);
//  }
}
