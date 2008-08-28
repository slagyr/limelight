package limelight;

import limelight.task.RecurringTask;
import limelight.ui.Panel;
import limelight.ui.model.RootPanel;

public abstract class AnimationTask extends RecurringTask
{
  private Panel panel;

  public AnimationTask(String name, int updatesPerSecond, Panel panel)
  {
    super(name, updatesPerSecond);
    this.panel = panel;
    setStrict(true);
  }

  public boolean isReady()
  {
    RootPanel activeRoot = Context.getActiveFrame().getRoot();
    return super.isReady() && panel.getRoot() == activeRoot;
  }

  public void start()
  {
    Context.instance().taskEngine.add(this);
  }

  public void stop()
  {
    Context.instance().taskEngine.remove(this);
  }
}
