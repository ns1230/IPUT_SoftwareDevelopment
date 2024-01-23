package client_system;

//import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

public class UserReservationDialog extends Dialog implements ActionListener, WindowListener {

    //private JList<String> reservationsList;
    //private JScrollPane scrollPane;
    
	boolean	canceled;
	ReservationControl	rc;
	
	//panel
	Panel	panelNorth;
	Panel	panelCenter;
	Panel	panelSouth;
	
	//input component
	ChoiceFacility	choiceFacility;
	TextField		stfYear,stfMonth,stfDay,etfYear,etfMonth,etfDay;
	
	//button
	Button	buttonOK;
	Button	buttonCancel;
	
	//private int maxReservations;

    public UserReservationDialog(Frame owner, ReservationControl rc) {
        super(owner, "User Reservation List", true);
        this.rc = rc;
        //this.reservations = reservationControl.getUserReservations(mainFrame.getLoggedInUser());
        //sthis.maxReservations = 20;

        // Create and configure the reservations list component
        //reservationsList = new JList<>();
        //scrollPane = new JScrollPane(reservationsList);
        
		//
		canceled = true;
		
		List<String> facilityId = rc.getFacilityId();
		// Add "All Classrooms" option to the facilityId list
        facilityId.add(0, "All Classrooms");
        choiceFacility = new ChoiceFacility(facilityId);
        
		//button
		buttonOK	= new Button("Show");
		buttonCancel= new Button("Cancel");
		
		//text field
 		stfYear		= new TextField("",4);
 		stfMonth	= new TextField("",2);
 		stfDay		= new TextField("",2);
 		etfYear		= new TextField("",4);
 		etfMonth	= new TextField("",2);
 		etfDay		= new TextField("",2);
		
		//panel
		panelNorth		= new Panel();
		panelCenter	= new Panel();
		panelSouth		= new Panel();
		
		panelNorth.add(new Label("Classroom"));
		panelNorth.add(choiceFacility);
		panelCenter.add(new Label("Period"));
		panelCenter.add(new Label("   Year"));
		panelCenter.add(stfYear);
		panelCenter.add(new Label("  Month"));
		panelCenter.add(stfMonth);
		panelCenter.add(new Label("  Date"));
		panelCenter.add(stfDay);
		panelCenter.add(new Label("      ~       Year"));
		panelCenter.add(etfYear);
		panelCenter.add(new Label("  Month"));
		panelCenter.add(etfMonth);
		panelCenter.add(new Label("  Date"));
		panelCenter.add(etfDay);

        // Set layout and add components
        //add(scrollPane, BorderLayout.CENTER);
		
		//
		panelSouth.add(buttonCancel);
		panelSouth.add(new Label(" "));
		panelSouth.add(buttonOK);
		
		setLayout(new BorderLayout());
		add(panelNorth,BorderLayout.NORTH);
		add(panelCenter,BorderLayout.CENTER);
		add(panelSouth,BorderLayout.SOUTH);
        
		//
		addWindowListener(this);
        buttonOK.addActionListener(this);
        buttonCancel.addActionListener(this);

        // Set dialog size and location
		this.setBounds(100, 100, 900, 250);
		setResizable(false);
    }
    


	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
        setVisible(false);
        dispose();
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
        if (e.getSource() == buttonCancel) {
            setVisible(false);
            dispose();
        } else if (e.getSource() == buttonOK) {
            canceled = false;
            setVisible(false);
            dispose();
        }
	}
}
