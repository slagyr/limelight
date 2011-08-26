//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package utilities;

import limelight.java.JavaProp;
import limelight.java.JavaScene;
import limelight.ui.events.panel.PanelEvent;

public class Alert
{
  public static Utilities getUtilities(JavaProp prop)
  {
    return (Utilities) prop.getScene().getProduction().getPlayer();
  }

  public static class OkButton
  {
    public void pressed(PanelEvent event)
    {
      getUtilities((JavaProp)event.getProp()).processAlertResponse(true);
    }
  }

  public static class Advice
  {
    public void loadMessage(PanelEvent event)
    {
      final JavaProp prop = (JavaProp) event.getProp();
      final Utilities utilities = getUtilities(prop);
      prop.setText(utilities.alertMessage);
    }
  }

  public void checkMessageHeight(PanelEvent event)
  {
    JavaScene scene = (JavaScene)event.getProp();
    JavaProp advice = scene.findProp("advice");
    final int lines = advice.getText().split("[\r|\r\n]").length;
    if(lines > 32)
      advice.getStyle().setVerticalScrollbar("on");
  }
}
