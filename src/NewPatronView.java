/* AddPartyView.java
 *
 *  Version
 *  $Id$
 * 
 *  Revisions:
 * 		$Log: NewPatronView.java,v $
 * 		Revision 1.3  2003/02/02 16:29:52  ???
 * 		Added ControlDeskEvent and ControlDeskObserver. Updated Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of ControlDesk.
 * 		
 * 
 */

/**
 * Class for GUI components need to add a patron
 */

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewPatronView implements ActionListener {

  private JFrame win;
  private JButton abort, finished;
  private JTextField nickField, fullField, emailField;
  private String nick, full, email;

  private AddPartyView addParty;

  public NewPatronView(AddPartyView v) {

    addParty = v;

    win = new JFrame("Add Patron");
    win.getContentPane().setLayout(new BorderLayout());
    ((JPanel) win.getContentPane()).setOpaque(false);

    JPanel colPanel = new JPanel();
    colPanel.setLayout(new BorderLayout());

    // Patron Panel
    JPanel patronPanel = new JPanel();
    patronPanel.setLayout(new GridLayout(3, 1));
    patronPanel.setBorder(new TitledBorder("Your Info"));

    JPanel nickPanel = new JPanel();
    nickPanel.setLayout(new FlowLayout());
    JLabel nickLabel = new JLabel("Nick Name");
    nickField = new JTextField("", 15);
    nickPanel.add(nickLabel);
    nickPanel.add(nickField);

    JPanel fullPanel = new JPanel();
    fullPanel.setLayout(new FlowLayout());
    JLabel fullLabel = new JLabel("Full Name");
    fullField = new JTextField("", 15);
    fullPanel.add(fullLabel);
    fullPanel.add(fullField);

    JPanel emailPanel = new JPanel();
    emailPanel.setLayout(new FlowLayout());
    JLabel emailLabel = new JLabel("E-Mail");
    emailField = new JTextField("", 15);
    emailPanel.add(emailLabel);
    emailPanel.add(emailField);

    patronPanel.add(nickPanel);
    patronPanel.add(fullPanel);
    patronPanel.add(emailPanel);

    // Button Panel
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(4, 1));

    Insets buttonMargin = new Insets(4, 4, 4, 4);

    finished = new JButton("Add Patron");
    JPanel finishedPanel = new JPanel();
    finishedPanel.setLayout(new FlowLayout());
    finished.addActionListener(this);
    finishedPanel.add(finished);

    abort = new JButton("Abort");
    JPanel abortPanel = new JPanel();
    abortPanel.setLayout(new FlowLayout());
    abort.addActionListener(this);
    abortPanel.add(abort);

    buttonPanel.add(abortPanel);
    buttonPanel.add(finishedPanel);

    // Clean up main panel
    colPanel.add(patronPanel, "Center");
    colPanel.add(buttonPanel, "East");

    win.getContentPane().add("Center", colPanel);

    win.pack();

    // Center Window on Screen
    Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
    win.setLocation(
        ((screenSize.width) / 2) - ((win.getSize().width) / 2),
        ((screenSize.height) / 2) - ((win.getSize().height) / 2));
    win.setVisible(true);
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource().equals(abort)) {
      win.setVisible(false);
    }

    if (e.getSource().equals(finished)) {
      nick = nickField.getText();
      full = fullField.getText();
      email = emailField.getText();
      addParty.updateNewPatron(this);
      win.setVisible(false);
    }
  }

  public String getNick() {
    return nick;
  }

  public String getFull() {
    return full;
  }

  public String getEmail() {
    return email;
  }
}
