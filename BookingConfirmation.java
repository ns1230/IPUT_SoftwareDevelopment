package client_system;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class BookingConfirmation extends Dialog implements ActionListener, WindowListener, ItemListener {

    boolean canceled;
    ReservationControl rc;

    //panel
    Panel panelNorth;
    Panel panelSouth;

    //input component
    ChoiceFacility choiceFacility;
    TextField tfYear, tfMonth, tfDay;
    //button
    Button buttonOK;
    Button buttonCancel;

    public BookingConfirmation(Frame owner, ReservationControl rc) {

        super(owner, "Booking Confirmation", true);
        this.rc = rc;

        //
        canceled = true;

        //
        List<String> facilityId = rc.getFacilityId();
        choiceFacility = new ChoiceFacility(facilityId);

        //button
        buttonOK = new Button("Confirm Booking");
        buttonCancel = new Button("Cancel");
        
      //text field
  		tfYear		= new TextField("",4);
  		tfMonth		= new TextField("",2);
  		tfDay		= new TextField("",2);
        //panel
        panelNorth = new Panel();
        panelSouth = new Panel();
        //put at panelNorth for room box, year etc.
        panelNorth.add(new Label("Classroom"));
        panelNorth.add(choiceFacility);
        panelNorth.add(new Label("Reservation Data"));
        panelNorth.add(new Label("Year"));
        panelNorth.add(tfYear);
        panelNorth.add(new Label("  Month"));
        panelNorth.add(tfMonth);
        panelNorth.add(new Label("  Date"));
        panelNorth.add(tfDay);

        //
        panelSouth.add(buttonCancel);
        panelSouth.add(new Label(" "));
        panelSouth.add(buttonOK);

        //
        setLayout(new BorderLayout());
        add(panelNorth, BorderLayout.NORTH);
        add(panelSouth, BorderLayout.SOUTH);

        //
        addWindowListener(this);
        //
        buttonOK.addActionListener(this);
        buttonCancel.addActionListener(this);
        //
        choiceFacility.addItemListener(this);

        //
        this.setBounds(100, 100, 600, 150);
        setResizable(false);
    }

    @Override
    public void windowOpened(WindowEvent e) {
        //TODO

    }

    @Override
    public void windowClosing(WindowEvent e) {
        setVisible(false);
        dispose();

    }

    @Override
    public void windowClosed(WindowEvent e) {
        //TODO

    }

    @Override
    public void windowIconified(WindowEvent e) {
        //TODO

    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        //TODO

    }

    @Override
    public void windowActivated(WindowEvent e) {
        //TODO

    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        //TODO

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonCancel) {
            setVisible(false);
            dispose();
        } else if (e.getSource() == buttonOK) {
            canceled = false;
            setVisible(false);
            dispose();
        }
    }

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
}
