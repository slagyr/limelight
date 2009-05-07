package limelight.os.darwin;

import com.apple.eawt.ApplicationAdapter;
import com.apple.eawt.ApplicationEvent;
import com.apple.eawt.Application;

public class LimelightApplicationAdapter extends ApplicationAdapter
{
  public void register()
  {
    Application.getApplication().addApplicationListener(this);
  }

  public void handleAbout(ApplicationEvent applicationEvent)
  {
    super.handleAbout(applicationEvent);
  }

  public void handleOpenApplication(ApplicationEvent applicationEvent)
  {
    super.handleOpenApplication(applicationEvent);
  }

  public void handleOpenFile(ApplicationEvent applicationEvent)
  {
    super.handleOpenFile(applicationEvent);
  }

  public void handlePreferences(ApplicationEvent applicationEvent)
  {
    super.handlePreferences(applicationEvent);
  }

  public void handlePrintFile(ApplicationEvent applicationEvent)
  {
    super.handlePrintFile(applicationEvent);
  }

  public void handleQuit(ApplicationEvent applicationEvent)
  {
    super.handleQuit(applicationEvent);
  }

  public void handleReOpenApplication(ApplicationEvent applicationEvent)
  {
    super.handleReOpenApplication(applicationEvent);
  }
}
