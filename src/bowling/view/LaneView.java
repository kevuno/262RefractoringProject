package bowling.view;

/**
 *  constructs a prototype bowling.Lane View
 *
 */

import bowling.*;
import bowling.Event;
import bowling.model.Bowler;
import bowling.model.Party;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

public class LaneView implements Observer, ActionListener {

  JFrame frame;
  Container cpanel;
  Vector bowlers;
  JPanel[][] balls;
  JLabel[][] ballLabel;
  JPanel[][] scores;
  JLabel[][] scoreLabel;
  JPanel[][] ballGrid;
  JPanel[] pins;
  JButton maintenance;
  Lane lane;
  private boolean initDone = true;

  /**
   * Constructs the LaneView
   * @param lane THe lane itself
   * @param laneNum Number of the lane in the alley
     */
  public LaneView(Lane lane, int laneNum) {

    this.lane = lane;

    initDone = true;
    frame = new JFrame("Lane " + laneNum + ":");
    cpanel = frame.getContentPane();
    cpanel.setLayout(new BorderLayout());

    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        frame.setVisible(false);
      }
    });

    cpanel.add(new JPanel());
  }

  /**
   * Reveals the view
   */
  public void show() {
    frame.setVisible(true);
  }

  /**
   * Hides the view
   */
  public void hide() {
    frame.setVisible(false);
  }

  /**
   * Creates the frames for the game
   * @param party The party playing the game
   * @return A JPanel illustrating the frames
     */
  private JPanel makeFrame(Party party) {

    initDone = false;
    bowlers = party.getMembers();
    int numBowlers = bowlers.size();

    JPanel panel = new JPanel();

    panel.setLayout(new GridLayout(0, 1));

    balls = new JPanel[numBowlers][23];
    ballLabel = new JLabel[numBowlers][23];
    scores = new JPanel[numBowlers][10];
    scoreLabel = new JLabel[numBowlers][10];
    ballGrid = new JPanel[numBowlers][10];
    pins = new JPanel[numBowlers];

    for (int i = 0; i != numBowlers; i++) {
      for (int j = 0; j != 23; j++) {
        ballLabel[i][j] = new JLabel(" ");
        balls[i][j] = new JPanel();
        balls[i][j].setBorder(
            BorderFactory.createLineBorder(Color.BLACK));
        balls[i][j].add(ballLabel[i][j]);
      }
    }

    for (int i = 0; i != numBowlers; i++) {
      for (int j = 0; j != 9; j++) {
        ballGrid[i][j] = new JPanel();
        ballGrid[i][j].setLayout(new GridLayout(0, 3));
        ballGrid[i][j].add(new JLabel("  "), BorderLayout.EAST);
        ballGrid[i][j].add(balls[i][2 * j], BorderLayout.EAST);
        ballGrid[i][j].add(balls[i][2 * j + 1], BorderLayout.EAST);
      }
      int j = 9;
      ballGrid[i][j] = new JPanel();
      ballGrid[i][j].setLayout(new GridLayout(0, 3));
      ballGrid[i][j].add(balls[i][2 * j]);
      ballGrid[i][j].add(balls[i][2 * j + 1]);
      ballGrid[i][j].add(balls[i][2 * j + 2]);
    }

    for (int i = 0; i != numBowlers; i++) {
      pins[i] = new JPanel();
      pins[i].setBorder(
          BorderFactory.createTitledBorder(
              ((Bowler) bowlers.get(i)).getNickName()));
      pins[i].setLayout(new GridLayout(0, 10));
      for (int k = 0; k != 10; k++) {
        scores[i][k] = new JPanel();
        scoreLabel[i][k] = new JLabel("  ", SwingConstants.CENTER);
        scores[i][k].setBorder(
            BorderFactory.createLineBorder(Color.BLACK));
        scores[i][k].setLayout(new GridLayout(0, 1));
        scores[i][k].add(ballGrid[i][k], BorderLayout.EAST);
        scores[i][k].add(scoreLabel[i][k], BorderLayout.SOUTH);
        pins[i].add(scores[i][k], BorderLayout.EAST);
      }
      panel.add(pins[i]);
    }

    initDone = true;
    return panel;
  }


  @Override
  public void receiveEvent(Event e) {
    LaneEvent le = (LaneEvent) e;
    if (lane.isPartyAssigned()) {
      int numBowlers = le.getParty().getMembers().size();
      while (!initDone) {
        try {
          Thread.sleep(1);
        }
        catch (Exception ex) {
          ex.printStackTrace();
        }
      }

      if (le.getFrameNum() == 1
              && le.getBall() == 0
              && le.getIndex() == 0) {
        System.out.println("Making the frame.");
        cpanel.removeAll();
        cpanel.add(makeFrame(le.getParty()), "Center");

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        maintenance = new JButton("Maintenance Call");
        JPanel maintenancePanel = new JPanel();
        maintenancePanel.setLayout(new FlowLayout());
        maintenance.addActionListener(this);
        maintenancePanel.add(maintenance);

        buttonPanel.add(maintenancePanel);

        cpanel.add(buttonPanel, "South");

        frame.pack();

      }

      int[][] lescores = le.getCumulScore();
      for (int k = 0; k < numBowlers; k++) {
        for (int i = 0; i <= le.getFrameNum() - 1; i++) {
          if (lescores[k][i] != 0) {
            scoreLabel[k][i].setText((new Integer(lescores[k][i])).toString());
          }
        }

        for (int i = 0; i < 21; i++) {
          if (((int[]) (le.getScore()).get(bowlers.get(k)))[i] != -1) {
            if (((int[]) (le.getScore()).get(bowlers.get(k)))[i] == 10 && (i % 2 == 0 || i == 19)) {
              ballLabel[k][i].setText("X");
            } else if (i > 0 && ((int[]) (le.getScore()).get(bowlers.get(k)))[i] +
                    ((int[]) (le.getScore()).get(bowlers.get(k)))[i - 1] == 10 && i % 2 == 1) {
              ballLabel[k][i].setText("/");
            } else if (((int[]) (le.getScore()).get(bowlers.get(k)))[i] == -2) {
              ballLabel[k][i].setText("F");
            } else {
              ballLabel[k][i].setText((new Integer(((int[]) (le.getScore()).get(bowlers.get(k)))[i])).toString());
            }
          }
        }
      }
    }
  }

  /**
   * Takes in an ActionEvent such as clicking on a button
   * @param e The ActionEvent
     */
  public void actionPerformed(ActionEvent e) {
    if (e.getSource().equals(maintenance)) {
      lane.pauseGame();
    }
  }
}
