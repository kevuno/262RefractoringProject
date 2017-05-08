package bowling;

import bowling.model.Alley;
import bowling.view.ControlDeskView;

/**
 * Main class that the program centers on
 */
public class drive {

  public static void main(String[] args) {

    int numLanes = 3;
    int maxPatronsPerParty = 5;

    Alley a = new Alley(numLanes);
    ControlDesk controlDesk = a.getControlDesk();

    ControlDeskView cdv = new ControlDeskView(controlDesk, maxPatronsPerParty);
    controlDesk.subscribe(cdv);
  }
}
