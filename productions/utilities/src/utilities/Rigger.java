package utilities;

import limelight.Context;
import limelight.model.Production;
import limelight.model.Stage;
import limelight.ui.events.panel.PanelEvent;
import limelight.ui.model.Scene;
import limelight.util.Opts;

import java.util.List;

public class Rigger
{

  public static class ReloadAllButton
  {
    public void pressed(PanelEvent event)
    {
      final List<Production> productions = Context.instance().studio.getProductions();
      for(Production production : productions)
      {
        final List<Stage> stages = production.getTheater().getStages();
        for(Stage stage : stages)
        {
          final Scene scene = stage.getScene();
          if(scene != null)
            production.openScene(scene.getAbsoluteName(), stage.getName(), new Opts());
        }
      }
    }
  }
}
