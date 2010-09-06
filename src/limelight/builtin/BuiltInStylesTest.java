package limelight.builtin;

import limelight.styles.RichStyle;
import limelight.styles.Style;
import limelight.util.Colors;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class BuiltInStylesTest
{
  private HashMap<String, RichStyle> styles;

  @Before
  public void setUp() throws Exception
  {
    styles = BuiltInStyles.all(); 
  }
  
  @Test
  public void hasCurtains() throws Exception
  {
    Style curtains = styles.get("limelight_builtin_curtains");

    assertNotNull(curtains);
    assertEquals("on", curtains.getFloat());
  }
  
  @Test
  public void hasComboBoxPopupList() throws Exception
  {
    Style popup = styles.get("limelight_builtin_combo_box_popup_list");

    assertNotNull(popup);
    assertEquals("on", popup.getFloat());
  }
  
  @Test
  public void hasComboBoxListItem() throws Exception
  {
    Style popup = styles.get("limelight_builtin_combo_box_popup_list_item");

    assertNotNull(popup);
    assertEquals("10", popup.getLeftPadding());

    Style hover = styles.get("limelight_builtin_combo_box_popup_list_item_selected");
    assertEquals(Colors.toString(Colors.resolve("white")), hover.getTextColor());
  }


}
