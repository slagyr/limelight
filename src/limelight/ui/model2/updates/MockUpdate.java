package limelight.ui.model2.updates;

import limelight.ui.model2.Update;
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
