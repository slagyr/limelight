//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package utilities;

import limelight.About;
import limelight.java.JavaPlayer;
import limelight.java.JavaProp;
import limelight.java.JavaScene;
import limelight.model.api.Player;
import limelight.ui.events.panel.PanelEvent;

public class IncompatibleVersion
{
  public void populateTextLabels(PanelEvent event)
  {
    final JavaProp prop = (JavaProp) event.getProp();
    JavaScene scene = prop.getScene();
    Utilities utilities = getUtilities(prop);

    scene.findProp("productionNameLabel").setText(utilities.incompatibleVersionProductionName);
    scene.findProp("requiredVersionLabel").setText(utilities.incompatibleVersionRequiredVersion);
    scene.findProp("currentVersionLabel").setText(About.version.toString());
  }

  public static Utilities getUtilities(JavaProp prop)
  {
    final JavaPlayer player = (JavaPlayer)prop.getScene().getProduction().getPlayer();
    return (Utilities) player.getPlayer();
  }

  public static class CancelButton
  {
    public void pushed(PanelEvent event)
    {
      JavaProp prop = (JavaProp) event.getProp();
      getUtilities(prop).processIncompatibleVersionResponse(false);
    }
  }

  public static class ProceedButton
  {
    public void pushed(PanelEvent event)
    {
      JavaProp prop = (JavaProp) event.getProp();
      getUtilities(prop).processIncompatibleVersionResponse(true);
    }
  }
}
