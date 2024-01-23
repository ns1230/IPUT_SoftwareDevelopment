package client_system;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
public class MainFrame extends Frame implements ActionListener, WindowListener {
	ReservationControl reservationControl;
	
	Panel panelNorth;
	Panel panelNorthSub1;
	Panel panelNorthSub2;
	Panel panelCenter;
	Panel panelSouth;
	
	Button buttonLog;
	Button buttonExplanation;
	Button buttonReservation;
	Button buttonConfirmation;				//予約確認
	Button buttonUserReservation;
	Button buttonCancelReservation;
	
	ChoiceFacility choiceFacility;
	
	TextField tfLoginID;
	
	TextArea textMessage;
	
	
	public MainFrame(ReservationControl rc) {
		reservationControl = rc;
		
		buttonLog = new Button("  Log in  ");
		buttonExplanation = new Button("Classroom Overview");
		buttonReservation = new Button("New Reservation"); 
		buttonConfirmation = new Button("Booking Confirmation");  //予約確認
		buttonUserReservation = new Button("User Reservation List");  //予約確認
		buttonCancelReservation = new Button("Cancel Reservation");
		
		List <String>facilityId = new ArrayList<String>();
		facilityId = rc.getFacilityId();
		choiceFacility = new ChoiceFacility(facilityId);
		
		tfLoginID = new TextField("Not Logged in", 12);
		tfLoginID.setEditable(false);
		
		setLayout(new BorderLayout());
		
		panelNorthSub1 = new Panel();
		panelNorthSub1.add(new Label("Classroom Reservation System"));
		panelNorthSub1.add(buttonLog);
		panelNorthSub1.add(new Label("			    Login ID:"));
		panelNorthSub1.add(tfLoginID);
		
		panelNorthSub2 = new Panel();
		panelNorthSub2.add(new Label("Classroom  "));
		panelNorthSub2.add(choiceFacility);
		panelNorthSub2.add(new Label("  "));
		panelNorthSub2.add(buttonExplanation);
		panelNorthSub2.add(buttonConfirmation); //予約確認
		panelNorthSub2.add(buttonUserReservation); //予約確認
		panelNorthSub2.add(buttonCancelReservation);
		
		panelNorth = new Panel(new BorderLayout());
		panelNorth.add(panelNorthSub1, BorderLayout.NORTH);
		panelNorth.add(panelNorthSub2, BorderLayout.CENTER);
		
		add(panelNorth, BorderLayout.NORTH);
		
		panelCenter = new Panel();
		textMessage = new TextArea(20,80);
		textMessage.setEditable(false);
		panelCenter.add(textMessage);
		
		add(panelCenter, BorderLayout.CENTER);
		
		panelSouth = new Panel();
		panelSouth.add( buttonReservation);
		
		add(panelSouth, BorderLayout.SOUTH);
		
		buttonLog.addActionListener(this);
		buttonExplanation.addActionListener(this);
		buttonReservation.addActionListener(this);
		buttonConfirmation.addActionListener(this);
		buttonUserReservation.addActionListener(this);
		buttonCancelReservation.addActionListener(this);
		addWindowListener(this);
	}
	

	@Override
	public void windowOpened(WindowEvent e) {
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		System.exit(0);
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String result = new String();
		if (e.getSource() == buttonLog) {
			result = reservationControl.loginLogout(this);
		}else if(e.getSource() == buttonExplanation) {
			result = reservationControl.getFacilityExplanation(choiceFacility.getSelectedItem());
		}else if(e.getSource() == buttonReservation) {
			result = reservationControl.makeReservation(this);
		}else if(e.getSource() == buttonConfirmation){
			result =reservationControl.getConfirmation(this);
		}else if(e.getSource() == buttonUserReservation){
			result =reservationControl.getUserReservations(this);
		}else if(e.getSource() == buttonCancelReservation){
			result =reservationControl.getCancelReservation(this);
		}
		textMessage.setText(result);
	}
}