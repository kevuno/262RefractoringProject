package bowling; /**
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LaneStatusView implements ActionListener, LaneObserver, PinsetterObserver {

  int laneNum;
  boolean laneShowing;
  boolean psShowing;
  private JPanel jp;
  private JLabel curBowler, pinsDown;
  private JButton viewLane;
  private JButton viewPinSetter, maintenance;
  private PinSetterView psv;
  private LaneView lv;
  private Lane lane;

  public LaneStatusView(Lane lane, int laneNum) {

    this.lane = lane;
    this.laneNum = laneNum;

    laneShowing = false;
    psShowing = false;

    psv = new PinSetterView(laneNum);
    Pinsetter ps = lane.getPinsetter();
    ps.subscribe(psv);

    lv = new LaneView(lane, laneNum);
    lane.subscribe(lv);


    jp = new JPanel();
    jp.setLayout(new FlowLayout());
    JLabel cLabel = new JLabel("Now Bowling: ");
    curBowler = new JLabel("(no one)");
    JLabel fLabel = new JLabel("Foul: ");
    JLabel pdLabel = new JLabel("Pins Down: ");
    pinsDown = new JLabel("0");

    // Button Panel
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout());

    Insets buttonMargin = new Insets(4, 4, 4, 4);

    viewLane = new JButton("View bowling.Lane");
    JPanel viewLanePanel = new JPanel();
    viewLanePanel.setLayout(new FlowLayout());
    viewLane.addActionListener(this);
    viewLanePanel.add(viewLane);

    viewPinSetter = new JButton("bowling.Pinsetter");
    JPanel viewPinSetterPanel = new JPanel();
    viewPinSetterPanel.setLayout(new FlowLayout());
    viewPinSetter.addActionListener(this);
    viewPinSetterPanel.add(viewPinSetter);

    maintenance = new JButton("     ");
    maintenance.setBackground(Color.GREEN);
    JPanel maintenancePanel = new JPanel();
    maintenancePanel.setLayout(new FlowLayout());
    maintenance.addActionListener(this);
    maintenancePanel.add(maintenance);

    viewLane.setEnabled(false);
    viewPinSetter.setEnabled(false);


    buttonPanel.add(viewLanePanel);
    buttonPanel.add(viewPinSetterPanel);
    buttonPanel.add(maintenancePanel);

    jp.add(cLabel);
    jp.add(curBowler);
//		jp.add( fLabel );
//		jp.add( foul );
    jp.add(pdLabel);
    jp.add(pinsDown);

    jp.add(buttonPanel);
  }

  public JPanel showLane() {
    return jp;
  }

  public void actionPerformed(ActionEvent e) {
    if (lane.isPartyAssigned()) {
      if (e.getSource().equals(viewPinSetter)) {
        if (!psShowing) {
          psv.show();
          psShowing = true;
        } else if (psShowing) {
          psv.hide();
          psShowing = false;
        }
      }
    }
    if (e.getSource().equals(viewLane)) {
      if (lane.isPartyAssigned()) {
        if (!laneShowing) {
          lv.show();
          laneShowing = true;
        } else if (laneShowing) {
          lv.hide();
          laneShowing = false;
        }
      }
    }
    if (e.getSource().equals(maintenance)) {
      if (lane.isPartyAssigned()) {
        lane.unPauseGame();
        maintenance.setBackground(Color.GREEN);
      }
    }
  }

  public void receiveLaneEvent(LaneEvent le) {
    curBowler.setText((le.getBowler()).getNickName());
    if (le.isMechanicalProblem()) {
      maintenance.setBackground(Color.RED);
    }
    if (!lane.isPartyAssigned()) {
      viewLane.setEnabled(false);
      viewPinSetter.setEnabled(false);
    } else {
      viewLane.setEnabled(true);
      viewPinSetter.setEnabled(true);
    }
  }

  public void receivePinsetterEvent(PinsetterEvent pe) {
    pinsDown.setText((new Integer(pe.totalPinsDown())).toString());
//		foul.setText( ( new Boolean(pe.isFoulCommited()) ).toString() );

  }
}
