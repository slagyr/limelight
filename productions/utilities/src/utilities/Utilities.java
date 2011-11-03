//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package utilities;

import limelight.model.Production;
import limelight.model.Stage;
import limelight.model.Theater;
import limelight.model.events.ProductionEvent;
import limelight.util.Opts;
import limelight.util.Util;

import java.util.HashMap;
import java.util.Map;

public class Utilities
{
  private static Opts dialogStageOptions = Opts.with(
    "location", "center, center",
    "size", "400, auto",
    "backgroundColor", "white",
    "alwaysOnTop", "true",
    "framed", "false",
    "vital", "false");

  private static Opts riggerStageOptions = Opts.with(
    "title", "Limelight Rigger",
    "location", "left, top",
    "size", "200, auto",
    "backgroundColor", "white");

  private Production production;

  final private Object alertMonitor = new Object();
  private Object alertResponse;
  public String alertMessage;

  final private Object incompatibleVersionMonitor = new Object();
  private Object incompatibleVersionResponse;
  public String incompatibleVersionProductionName;
  public String incompatibleVersionRequiredVersion;

  public void init(ProductionEvent event)
  {
    production = event.getProduction();
    production.setAllowClose(false);
  }

  public Object canProceedWithIncompatibleVersion(String productionName, String requiredVersion)
  {
    incompatibleVersionResponse = null;
    incompatibleVersionProductionName = productionName;
    incompatibleVersionRequiredVersion = requiredVersion;
    Stage incompatibleVersionStage = establishStage("Incompatible Version", dialogStageOptions);
    production.openScene("incompatibleVersion", incompatibleVersionStage, Util.toMap());

    waitForResponse(incompatibleVersionMonitor);

    incompatibleVersionStage.close();
    return incompatibleVersionResponse;
  }

  public void processIncompatibleVersionResponse(boolean response)
  {
    incompatibleVersionResponse = response;
    synchronized(incompatibleVersionMonitor)
    {
      incompatibleVersionMonitor.notify();
    }
  }

  public Object alert(String message)
  {
    alertMessage = message;
    alertResponse = null;
    Stage alertStage = establishStage("Alert", dialogStageOptions);
    production.openScene("alert", alertStage, Util.toMap());

    waitForResponse(alertMonitor);

    alertStage.close();
    return alertResponse;
  }

  public void processAlertResponse(boolean response)
  {
    alertResponse = response;
    synchronized(alertMonitor)
    {
      alertMonitor.notify();
    }
  }

  private void waitForResponse(Object monitor)
  {
    try
    {
      synchronized(monitor)
      {
        monitor.wait();
      }
    }
    catch(InterruptedException e)
    {
    }
  }

  private Stage establishStage(String stageName, Map<String, Object> stageOptions)
  {
    final Theater theater = production.getTheater();
    Stage alertStage = theater.get(stageName);
    if(alertStage == null)
    {
      alertStage = theater.getProxy().buildStage(stageName, new HashMap<String, Object>(stageOptions));
      theater.add(alertStage);
    }
    return alertStage;
  }

  public void openRigger()
  {
    Stage riggerStage = establishStage("rigger", riggerStageOptions);
    production.openScene("rigger", riggerStage, new Opts());
  }
}
