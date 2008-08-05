package limelight.ui.model.updates;

import limelight.ui.model.Update;
import limelight.ui.Panel;

public class MockUpdate extends Update
{
  public boolean updatePerformed;
  public Panel updatedPanel;

  public MockUpdate()
  {
    super(1);
  }

  public void performUpdate(Panel panel)
  {
    updatePerformed = true;
    updatedPanel = panel;
  }
}
