package client_system;

import 	java.awt.*;
import	java.awt.event.*;
import	java.util.ArrayList;
import	java.util.List;

public class ReservationDialog extends Dialog implements ActionListener, WindowListener, ItemListener {

	boolean	canceled;
	ReservationControl	rc;
	
	//panel
	Panel	panelNorth;
	Panel	panelCenter;
	Panel	panelSouth;
	
	//input component
	ChoiceFacility	choiceFacility;
	TextField		tfYear,tfMonth,tfDay;
	ChoiceHour		startHour;
	ChoiceMinute	startMinute;
	ChoiceHour		endHour;
	ChoiceMinute	endMinute;
	
	//button
	Button	buttonOK;
	Button	buttonCancel;
	
	//constructor
	public	ReservationDialog( Frame owner,ReservationControl rc) {
		//
		super(owner,"New Reservation",true);
		
		this.rc = rc;
		
		//
		canceled = true;
		
		//
		List<String> facilityId = new ArrayList<String>();
		facilityId = rc.getFacilityId();
		choiceFacility = new ChoiceFacility(facilityId);
		//text field
		tfYear		= new TextField("",4);
		tfMonth		= new TextField("",2);
		tfDay		= new TextField("",2);
		//start time
		startHour	= new ChoiceHour();
		startMinute	= new ChoiceMinute();
		//end time
		endHour		= new ChoiceHour();
		endMinute	= new ChoiceMinute();
		
		//button
		buttonOK	= new Button("Make Reservation");
		buttonCancel= new Button("Cancel");
		
		//panel
		panelNorth	= new Panel();
		panelCenter	= new Panel();
		panelSouth	= new Panel();
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
		panelCenter.add(new Label("Reservation Time"));
		panelCenter.add(new Label("Hour"));
		panelCenter.add(startHour);
		panelCenter.add(new Label("  Minute"));
		panelCenter.add(startMinute);
		panelCenter.add(new Label(" ~   Hour"));
		panelCenter.add(endHour);
		panelCenter.add(new Label("  Minute"));
		panelCenter.add(endMinute);
		
		//
		panelSouth.add(buttonCancel);
		panelSouth.add(new Label(" "));
		panelSouth.add(buttonOK);
		
		//
		setLayout(new BorderLayout());
		add(panelNorth,BorderLayout.NORTH);
		add(panelCenter,BorderLayout.CENTER);
		add(panelSouth,BorderLayout.SOUTH);
		
		
		//
		addWindowListener(this);
		//
		buttonOK.addActionListener(this);
		buttonCancel.addActionListener(this);
		//
		choiceFacility.addItemListener(this);
		startHour.addItemListener(this);
		endHour.addItemListener(this);
		
		//
		resetTimeRange(choiceFacility.getSelectedItem());
		
		//
		this.setBounds(100, 100, 600, 150);
		setResizable(false);
	}
	
	///
	private void resetTimeRange(String facility) {
		int[]	availableTime;
		//
		availableTime = rc.getAvailableTime(facility);
		//
		startHour.resetRange(availableTime[0],availableTime[1]);
		endHour.resetRange(availableTime[0],availableTime[1]);
	}
	
	//
	@Override
	public void itemStateChanged(ItemEvent e) {
	    if (e.getSource() == choiceFacility) {
	        String startTime = startHour.getSelectedItem();
	        String endTime = endHour.getSelectedItem();
	        resetTimeRange(choiceFacility.getSelectedItem());
	        if (Integer.parseInt(startTime) < Integer.parseInt(startHour.getFirst())) {
	            startTime = startHour.getFirst();
	        }
	        if (Integer.parseInt(endTime) > Integer.parseInt(endHour.getLast())) {
	            endTime = endHour.getLast();
	        }
	        startHour.select(startTime);
	        endHour.select(endTime);
	        updateStartMinuteChoices(Integer.parseInt(startTime));  // Update startMinute choices based on startHour
	    } else if (e.getSource() == startHour) {
	    	 int start = Integer.parseInt(startHour.getSelectedItem());
	         String endTime = endHour.getSelectedItem();
	         endHour.resetRange(start, Integer.parseInt(endHour.getLast()));
	         if (Integer.parseInt(endTime) >= start) {
	             endHour.select(endTime);
	         }
	         updateEndMinuteChoices(Integer.parseInt(endHour.getSelectedItem()));  // Update endMinute choices based on endHour
	         updateStartMinuteChoices(start);  // Update startMinute choices based on startHour
	     } else if (e.getSource() == endHour) {
	         int end = Integer.parseInt(endHour.getSelectedItem());
	         String startTime = startHour.getSelectedItem();
	         startHour.resetRange(Integer.parseInt(startHour.getFirst()), end);
	         if (Integer.parseInt(startTime) <= end) {
	             startHour.select(startTime);
	         }
	         updateEndMinuteChoices(end);  // Update endMinute choices based on endHour
	         updateStartMinuteChoices(Integer.parseInt(startHour.getSelectedItem()));  // Update startMinute choices based on startHour
	     }
	}


	// Method to update the choices for endMinute based on the selected endHour
	private void updateEndMinuteChoices(int endHour) {
	    // Clear existing choices in endMinute
	    endMinute.removeAll();

	    // Add available choices to endMinute based on endHour
	    if (endHour == 21) {
	        endMinute.add("00");
	        endMinute.add("15");
	        endMinute.add("30");
	    } else {
	        endMinute.add("00");
	        endMinute.add("15");
	        endMinute.add("30");
	        endMinute.add("45");
	    }

	    // Set the first choice as the selected choice
	    endMinute.select(0);

	    // Update the choices for startMinute based on startHour
	    updateStartMinuteChoices(Integer.parseInt(startHour.getSelectedItem()));
	}

	// Method to update the choices for startMinute based on the selected startHour
	private void updateStartMinuteChoices(int startHour) {
	    // Clear existing choices in startMinute
	    startMinute.removeAll();

	    // Add available choices to startMinute based on startHour
	    if (startHour == 21) {
	        startMinute.add("00");
	        startMinute.add("15");
	        startMinute.add("30");
	    } else {
	        startMinute.add("00");
	        startMinute.add("15");
	        startMinute.add("30");
	        startMinute.add("45");
	    }

	    // Set the first choice as the selected choice
	    startMinute.select(0);
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
		if(e.getSource() == buttonCancel) {
			setVisible(false);
			dispose();
		}else if(e.getSource() == buttonOK) {
			canceled = false;
			setVisible(false);
			dispose();
		}
	}
	
}

