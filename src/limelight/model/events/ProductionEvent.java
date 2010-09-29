package limelight.model.events;

import limelight.events.Event;
import limelight.model.Production;

public class ProductionEvent extends Event
{
  private Production production;

  public void dispatch(Production production)
  {
    subject = this.production = production;
    production.getEventHandler().dispatch(this);
  }

  public Production getProduction()
  {
    return production;
  }
}
