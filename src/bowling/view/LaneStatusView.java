package bowling.view;

/**
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

import bowling.*;
import bowling.Event;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LaneStatusView implements ActionListener, Observer {

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
    JLabel pdLabel = new JLabel("Pins Down: ");
    pinsDown = new JLabel("0");

    // Button Panel
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout());

    viewLane = new JButton("View Lane");
    JPanel viewLanePanel = new JPanel();
    viewLanePanel.setLayout(new FlowLayout());
    viewLane.addActionListener(this);
    viewLanePanel.add(viewLane);

    viewPinSetter = new JButton("Pinsetter");
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
    jp.add(pdLabel);
    jp.add(pinsDown);

    jp.add(buttonPanel);
  }

  public JPanel showLane() {
    return jp;
  }

  public void actionPerformed(ActionEvent e) {
    //New party is assigned
    if (lane.isPartyAssigned()) {
      if (e.getSource().equals(viewPinSetter)) {
        if (!psShowing) {
          psv.show();
          psShowing = true;
        } else {
          psv.hide();
          psShowing = false;
        }
      }
      //A new lane view is requested
      if (e.getSource().equals(viewLane)) {
        if (!laneShowing) {
          lv.show();
          laneShowing = true;
        } else {
          lv.hide();
          laneShowing = false;
        }
      }
    }
    //A maintenance request was made
    if (e.getSource().equals(maintenance)) {
      if (lane.isPartyAssigned()) {
        lane.unPauseGame();
        maintenance.setBackground(Color.GREEN);
      }
    }
  }
  public void receiveLaneEvent(LaneEvent le) {
    curBowler.setText((le.getBowler()).getNickName());
    //Mechanical problem from lane
    if (le.isMechanicalProblem()) {
      maintenance.setBackground(Color.RED);
    }
    //A lane has finished playing
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
  }

  @Override
  /**
   * Receives a generic event and it calls the designed Event type receiver method
   */
  public void receiveEvent(Event e) {
    if(e instanceof LaneEvent){
      receiveLaneEvent((LaneEvent) e);
    }else if(e instanceof PinsetterEvent){
      receivePinsetterEvent((PinsetterEvent) e);
    }
  }
}
