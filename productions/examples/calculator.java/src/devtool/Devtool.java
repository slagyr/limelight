package devtool;

import limelight.events.Event;
import limelight.model.Production;
import limelight.model.Stage;
import limelight.ui.events.panel.PanelEvent;
import limelight.util.OptionsMap;

import java.util.List;

public class Devtool
{

  public void refresh(Event e)
  {
    PanelEvent event = (PanelEvent)e;
    final Production production = event.getRecipient().getRoot().getProduction();
    final List<Stage> stages = production.getTheater().getStages();
    for(Stage stage : stages)
    {
      production.openScene(stage.getScene().getResourceLoader().getRoot(), stage, new OptionsMap());      
    }

//      (let [scene (.getRoot (.getRecipient e))
//        stage (.getStage scene)
//        production (.getProduction scene)
//        stages (.getStages (.getTheater production))]
//    (println stages)
//    (doall (map
//      (fn [stage]
//        (println "stage: " stage)
//        (.openScene production (.getName (.getScene stage)) stage (limelight.util.OptionsMap. {})))
//      stages))
  }
}
