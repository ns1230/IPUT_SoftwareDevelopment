package client_system;

//import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class CancelReservationDialog extends Dialog implements ActionListener, WindowListener {
    
	boolean	canceled;
	ReservationControl	rc;
	
	//panel
	Panel	panelNorth;
	Panel	panelCenter;
	Panel	panelSouth;
	
	//input component
	TextField		reser_id;
	
	//button
	Button	buttonOK;
	Button	buttonCancel;
	

    public CancelReservationDialog(Frame owner, ReservationControl rc) {
        super(owner, "Cancel Reservation", true);
        this.rc = rc;

		canceled = true;
        
		//button
		buttonOK	= new Button("Cancel Reservation");
		buttonCancel= new Button("Cancel Operation");
		
		//text field
 		reser_id		= new TextField("",5);
		
		//panel
		panelNorth		= new Panel();
		panelCenter		= new Panel();
		panelSouth		= new Panel();
		
		panelNorth.add(new Label("Enter the Reservation ID of the reservation you would like to cancel"));
		
		panelCenter.add(new Label("Reservation ID"));
		panelCenter.add(reser_id);
		
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
		this.setBounds(100, 100, 600, 200);
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
