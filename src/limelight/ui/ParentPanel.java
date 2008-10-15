package limelight.ui;

import java.util.List;

public interface ParentPanel
{
  void add(Panel child);
  List<Panel> getChildren();

  boolean remove(Panel child);
  void removeAll();
  void sterilize();
  boolean isSterilized();
  boolean hasChildren();
}
