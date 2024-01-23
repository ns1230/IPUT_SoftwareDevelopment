package client_system;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class ReservationControl {
	Connection 	sqlCon;
	Statement	sqlStmt;
	String		sqlUserID = "puser";
	String		sqlPassword = "1234";
	
	String		reservationUserID;
	
	private boolean flagLogin;
	
	private List<String> reservations;
	
	ReservationControl(){
		flagLogin = false;
		reservations = new ArrayList<>();
	}
	
	private void connectDB() {
		try {
			Class.forName("org.gjt.mm.mysql.Driver");
			String url = "jdbc:mysql://localhost?useUnicode=true&characterEncoding=SJIS";
			sqlCon = DriverManager.getConnection(url, sqlUserID, sqlPassword);
			sqlStmt = (Statement)sqlCon.createStatement();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void closeDB() {
		try {
			sqlStmt.close();
			sqlCon.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String loginLogout (MainFrame frame) {
		String res = "";
		if(flagLogin) {
			flagLogin = false;
			frame.buttonLog.setLabel(" Log in ");
			frame.tfLoginID.setText("Not Logged in");
		} else {
			LoginDialog ld = new LoginDialog(frame);
			ld.setBounds(100,100,350,150);
			ld.setResizable(false);
			ld.setVisible(true);
			ld.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
			
			if(ld.canceled) {
				return "";
			}
			
			reservationUserID = ld.tfUserID.getText();
			String password = ld.tfPassword.getText();
			
			connectDB();
			
			try {
				String sql = "SELECT * FROM db_reservation.user WHERE user_id = '" + reservationUserID + "';";
				ResultSet rs = sqlStmt.executeQuery(sql);
				
				if(rs.next()) {
					String password_form_db = rs.getString("password");
					if(password_form_db.equals(password)) {
						flagLogin = true;
						frame.buttonLog.setLabel("Log out");
						frame.tfLoginID.setText(reservationUserID);
						res = "";
					} else {
						res = "Wrong ID or password";
					}		
				}else {
					res = "Wrong ID";
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
			closeDB();
		}
		return res;
	}
	
	public String getFacilityExplanation(String facility_id) {
		String res = "";
		String exp = "";
		String openTime = "";
		String closeTime = "";
		connectDB();
		try {
			String sql = "SELECT * from db_reservation.facility WHERE facility_id ='" + facility_id +"';";
			ResultSet rs = sqlStmt.executeQuery(sql);
			if(rs.next()) {
				exp = rs.getString("explanation");
				openTime = rs.getString("open_time");
				closeTime = rs.getString("close_time");
				//
				res = exp + "   Available Time: " + openTime.substring(0,5)+"~"+closeTime.substring(0,5);
			} else {
				res = "Wrong Classroom Number";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeDB();
		return res;
	}
	
	public List getFacilityId() {
		List<String> facilityId= new ArrayList<String>();
		connectDB();
		try {
			String sql = "SELECT * FROM db_reservation.facility;";
			ResultSet rs = sqlStmt.executeQuery(sql);
			while (rs.next()) {
				facilityId.add(rs.getString("facility_id"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return facilityId;
	}
	
	private boolean isTimeOverlap(String startTime1, String endTime1, String startTime2, String endTime2) {
        // Implement logic to check if two time ranges overlap
        // Compare the start and end times of the two ranges
        // Return true if there is an overlap, false otherwise
        // Using the java.time.LocalTime class for time comparisons
		Boolean res = true;
		LocalTime start1 = LocalTime.parse(startTime1);
        LocalTime end1 = LocalTime.parse(endTime1);
        LocalTime start2 = LocalTime.parse(startTime2);
        LocalTime end2 = LocalTime.parse(endTime2);
        if(end1.equals(start2) || end2.equals(start1)) {
        	res = false;
        }else if(end1.isBefore(start2) || start1.isAfter(end2)) {
        	res = false;
        }
        return res;
     }
	
	public boolean isTimeSlotAvailable(String facilityId, String rdate, String startTime, String endTime) {
	    connectDB();
	    
    	String sql = "SELECT * FROM db_reservation.reservation WHERE facility_id = '"+facilityId+"' AND day = '"+rdate+"';";
        ResultSet rSet = null;
		try {
			rSet = sqlStmt.executeQuery(sql);
			while (rSet.next()) {
			    if (rSet.getString("facility_id").equals(facilityId) && isTimeOverlap( rSet.getString("start_time"),  rSet.getString("end_time"), startTime, endTime)) {
			        return false; // Time slot is not available, conflict found
			    }
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return true; // Time slot is available
    }
	
	///
	public String makeReservation( MainFrame frame) {
		String res = "";
		
		if(flagLogin) {
			//
			ReservationDialog rd = new ReservationDialog(frame,this);
			rd.setVisible(true);
			
			//if the window was closed with the cancel button
			if(rd.canceled) {
				return res;
			}
			
			//Retrieve the date
			String ryear_str 	= rd.tfYear.getText();
			String rmonth_str 	= rd.tfMonth.getText();
			String rday_str 	= rd.tfDay.getText();
			if(rmonth_str.length() == 1) {
				rmonth_str = "0" + rmonth_str;
			}
			if(rday_str.length() == 1) {
				rday_str = "0" + rday_str;
			}
			String rdate = ryear_str + "-" + rmonth_str + "-" + rday_str;
			
			//Checking the date format
			try {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				df.setLenient(false);
				String convData = df.format(df.parse(rdate));
				if((! rdate.equals(convData)) || (ryear_str.length() != 4)) {
					res = "The format of the date of reservation needs to be fixed.(Y：4digits,M：1~12,D:1~31(or EOM))";
					return res;
				}
			}catch(ParseException p) {
				res = "The date of reservation needs to be fixed";
				return res;
			}
			
			//retrieve facility and time
			String facility = rd.choiceFacility.getSelectedItem();
			String st = rd.startHour.getSelectedItem() + ":" + rd.startMinute.getSelectedItem() + ":00";
			String et = rd.endHour.getSelectedItem() + ":" + rd.endMinute.getSelectedItem() + ":00";
			String sth = rd.startHour.getSelectedItem();
			String stm = rd.startMinute.getSelectedItem();
			
			//check time
			if(st.compareTo(et) >= 0) {
				res = "Starting time and Finishing time are either the same, or the Finishing time seems to be earlier than the Starting time";
			}else {
				try {
						Calendar justNow	= Calendar.getInstance();
						SimpleDateFormat resDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String now = resDate.format(justNow.getTime());
						//LocalDate reservedate = LocalDate.of(Integer.valueOf(ryear_str), Integer.valueOf(rmonth_str), Integer.valueOf(rday_str));
						//LocalDate currdate = LocalDate.now();
						LocalDateTime reservedate = LocalDateTime.of(Integer.valueOf(ryear_str), Integer.valueOf(rmonth_str), Integer.valueOf(rday_str),Integer.valueOf(sth),Integer.valueOf(stm),0);
						LocalDateTime currdate = LocalDateTime.now();
					
						connectDB();
						//check if reservation is in the past
						if(reservedate.isBefore(currdate)){
							res = "Reservation date is in the past. Review the date.";
						//check is there is any time conflict
						}else if (isTimeSlotAvailable(facility, rdate, st, et) == false) {
							res = "Time conflicting reservation found";
						}else{
							//Reserve
							String sql = "INSERT INTO db_reservation.reservation(facility_id, user_id, date, day, start_time, end_time) VALUES("
						            + facility + ", '" + reservationUserID + "', '" + now + "', '" + rdate + "', '" + st + "', '" + et + "');";
							sqlStmt.executeUpdate(sql);
							res = "Reservation successful";
						}
							
				}catch(Exception e) {
					e.printStackTrace();
				}
				closeDB();
			}
		}else {
			res = "Please log in";
		}
		return res;
	}
	
	//Enables searching up reservation details using facility number and date
	public String getConfirmation(MainFrame frame) {
	    String res = "";
	    BookingConfirmation		rd = new BookingConfirmation(frame,this);
	    
	    rd.setVisible(true);
	    if(rd.canceled) {
	    	return res;
	    }
	    String ryear_str 	= rd.tfYear.getText();
		String rmonth_str 	= rd.tfMonth.getText();
		String rday_str 	= rd.tfDay.getText();
		//
		if(rmonth_str.length() == 1) {
			rmonth_str = "0" + rmonth_str;
		}
		if(rday_str.length() == 1) {
			rday_str = "0" + rday_str;
		}
		String rdate = ryear_str + "-" + rmonth_str + "-" + rday_str;
		
		//
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			df.setLenient(false);
			String convData = df.format(df.parse(rdate));
			if((! rdate.equals(convData)) || (ryear_str.length() != 4)) {
				res = "The format of the date of reservation needs to be fixed.(Y：4digits,M：1~12,D:1~31(or EOM))";
				return res;
			}
		}catch(ParseException p) {
			res = "The date of reservation needs to be fixed";
			return res;
		}
		
		String facility = rd.choiceFacility.getSelectedItem();
	    connectDB();
	    try {
	    	String sql = "SELECT * FROM db_reservation.reservation WHERE facility_id = '"+facility+"' AND day = '"+rdate+"' ORDER BY day, start_time;";
	    	ResultSet rSet = sqlStmt.executeQuery(sql);
	        reservations.clear();
	        while (rSet.next()) {
	            String reser_num = rSet.getString("reservation_id");
	            String cls = rSet.getString("facility_id");
	            String user = rSet.getString("user_id");
	            String day = rSet.getString("day");
	            String openTime = rSet.getString("start_time");
	            String closeTime = rSet.getString("end_time");
	            String reserve = reser_num + "　class:" + cls + " person:" + user + " day：" + day + "  Booked Time: " + openTime.substring(0, 5) + "～" + closeTime.substring(0, 5);
	            reservations.add(reserve);
	        }
	        if (reservations.isEmpty()) {
	            res = "No reservation made";
	            return res;
	        }else {
	        	// Accumulate sorted reservations
	        	for (String reservation : reservations) {
	        	    res += reservation + "\n";
	        	}
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    closeDB();
	    return res;
	}
	
	
	
	
	//Enables listing all the reservations made by the user
	public String getUserReservations(MainFrame frame) {
		String res = "";
		if(flagLogin) {
			//
		    UserReservationDialog	rd = new UserReservationDialog(frame,this);
		    rd.setVisible(true);
		    
		    if(rd.canceled) {
		    	return res;
		    }
		    
		    String facility 	= rd.choiceFacility.getSelectedItem();
		    String syear_str 	= rd.stfYear.getText();
			String smonth_str 	= rd.stfMonth.getText();
			String sday_str 	= rd.stfDay.getText();
		    String eyear_str 	= rd.etfYear.getText();
			String emonth_str 	= rd.etfMonth.getText();
			String eday_str 	= rd.etfDay.getText();
			String command = "user_id = '" + reservationUserID + "'";
			if(facility == "All Classrooms") {
				facility = "";
			}
			
			//fixing the dates
			if(smonth_str.length() == 1) {
				smonth_str = "0" + smonth_str;
			}
			if(sday_str.length() == 1) {
				sday_str = "0" + sday_str;
			}
			if(emonth_str.length() == 1) {
				emonth_str = "0" + emonth_str;
			}
			if(eday_str.length() == 1) {
				eday_str = "0" + eday_str;
			}
			
			String edate = "";
			String sdate = "";
			
			//checking if there are any blanks (when some are entered)
			if((!syear_str.isEmpty() && smonth_str.isEmpty() && sday_str.isEmpty()) ||
				(syear_str.isEmpty() && !smonth_str.isEmpty() && sday_str.isEmpty()) ||
				(syear_str.isEmpty() && smonth_str.isEmpty() && !sday_str.isEmpty()) ||
				(!syear_str.isEmpty() && !smonth_str.isEmpty() && sday_str.isEmpty()) ||
				(syear_str.isEmpty() && !smonth_str.isEmpty() && !sday_str.isEmpty()) ||
				(!syear_str.isEmpty() && smonth_str.isEmpty() && !sday_str.isEmpty())){
				res = "Filtering with dates cannot be done with blank years, months, or dates";
				return res;
			}else if((!eyear_str.isEmpty() && emonth_str.isEmpty() && eday_str.isEmpty()) ||
					(eyear_str.isEmpty() && !emonth_str.isEmpty() && eday_str.isEmpty()) ||
					(eyear_str.isEmpty() && emonth_str.isEmpty() && !eday_str.isEmpty()) ||
					(!eyear_str.isEmpty() && !emonth_str.isEmpty() && eday_str.isEmpty()) ||
					(eyear_str.isEmpty() && !emonth_str.isEmpty() && !eday_str.isEmpty()) ||
					(!eyear_str.isEmpty() && emonth_str.isEmpty() && !eday_str.isEmpty())){
					res = "Filtering with dates cannot be done with blank years, months, or dates";
					return res;
			}else { //dates are either all empty or at least 1 set is all entered
				//checking the dates
				//ending dates are filled in
				if(syear_str.isEmpty() && !eyear_str.isEmpty()){
					edate = eyear_str + "-" + emonth_str + "-" + eday_str;
					try {
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						df.setLenient(false);
						String convData = df.format(df.parse(edate));
						if((! edate.equals(convData)) || (eyear_str.length() != 4)) {
							res = "The format of the date of reservation needs to be fixed.(Y：4digits,M：1~12,D:1~31(or EOM))";
							return res;
						}
					}catch(ParseException p) {
						res = "The date of reservation needs to be fixed";
						return res;
					}
					//starting dates are filled in
				}else if(eyear_str.isEmpty() && !syear_str.isEmpty()){
					sdate = syear_str + "-" + smonth_str + "-" + sday_str;
					try {
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						df.setLenient(false);
						String convData = df.format(df.parse(sdate));
						if((! sdate.equals(convData)) || (syear_str.length() != 4)) {
							res = "The format of the date of reservation needs to be fixed.(Y：4digits,M：1~12,D:1~31(or EOM))";
							return res;
						}
					}catch(ParseException p) {
						res = "The date of reservation needs to be fixed";
						return res;
					}
					//both starting and ending dates are filled in
				}else if(!syear_str.isEmpty() && !eyear_str.isEmpty()) {
					sdate = syear_str + "-" + smonth_str + "-" + sday_str;
					edate = eyear_str + "-" + emonth_str + "-" + eday_str;
					try {
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						df.setLenient(false);
						String convData = df.format(df.parse(sdate));
						if((! sdate.equals(convData)) || (syear_str.length() != 4)) {
							res = "The format of the date of reservation needs to be fixed.(Y：4digits,M：1~12,D:1~31(or EOM))";
							return res;
						}
						convData = df.format(df.parse(edate));
						if((! edate.equals(convData)) || (eyear_str.length() != 4)) {
							res = "The format of the date of reservation needs to be fixed.(Y：4digits,M：1~12,D:1~31(or EOM))";
							return res;
						}
					}catch(ParseException p) {
						res = "The date of reservation needs to be fixed";
						return res;
					}
				}else if (syear_str.isEmpty() && eyear_str.isEmpty()) {
					edate = "";
					sdate = "";
				}
			}
			
			connectDB();
			
			
			//Making SQL commands
			if(facility.isEmpty() && (syear_str.isEmpty() && eyear_str.isEmpty())) {
		    	//command = command;
			}else if (!facility.isEmpty() && (syear_str.isEmpty() && eyear_str.isEmpty())) {
				command += " AND facility_id = '" + facility + "'";
			}else if(!facility.isEmpty() && syear_str.isEmpty() && !eyear_str.isEmpty()) {
				command += " AND facility_id = '"+facility+"' AND day >= '"+edate+"'";
			}else if(!facility.isEmpty() && !syear_str.isEmpty() && eyear_str.isEmpty()){
				command += " AND facility_id = '"+facility+"' AND day >= '"+sdate+"'";
			}else if(!facility.isEmpty() && syear_str.isEmpty() && !eyear_str.isEmpty()) {
				command += " AND facility_id = '"+facility+"' AND day <= '"+edate+"'";
			}else if(facility.isEmpty() && !syear_str.isEmpty() && eyear_str.isEmpty()){
				command += " AND day >= '"+sdate+"'";
			}else if(facility.isEmpty() && syear_str.isEmpty() && !eyear_str.isEmpty()){
				command += " AND day <= '"+edate+"'";
			}else if(facility.isEmpty() && !syear_str.isEmpty() && !eyear_str.isEmpty()){
				command += " AND day >= '" + sdate + "' AND day <= '" + edate + "'";
			}else if(!facility.isEmpty() && !syear_str.isEmpty() && !eyear_str.isEmpty()){
				command += " AND facility_id = '"+facility+"' AND day >= '"+sdate+"' AND day <= '"+edate+"'";
			}
			
			//SQL
			try {
			    reservations.clear();
			    String sql = "SELECT * FROM db_reservation.reservation WHERE " + command + " ORDER BY day;";
			    ResultSet rSet = sqlStmt.executeQuery(sql);
			    while (rSet.next()) {
			        String reser_id = rSet.getString("reservation_id");
			        String cls = rSet.getString("facility_id");
			        String day = rSet.getString("day");
			        String openTime = rSet.getString("start_time");
			        String closeTime = rSet.getString("end_time");
			        String reserve = "Reservation ID: " + reser_id + " Class: " + cls + " Day: " + day + " Booked Time: " + openTime.substring(0, 5) + "～" + closeTime.substring(0, 5);
			        reservations.add(reserve);
			    }

			    if (reservations.isEmpty()) {
			        res = "No reservation made";
			    } else {
			        // Accumulate sorted reservations
			        for (String reservation : reservations) {
			            res += reservation + "\n";
			        }
			    }
			} catch (Exception e) {
			    e.printStackTrace();
			}

			closeDB();
		}else {
			res = "Please log in";
		}
	    return res;
    }
	
	
	
	
	
	
	//cancel a future reservation made by the user
	public String getCancelReservation(MainFrame frame) {
		String res = "";
		if(flagLogin) {
			//
		    CancelReservationDialog	rd = new CancelReservationDialog(frame,this);
		    rd.setVisible(true);
		    
		    if(rd.canceled) {
		    	return res;
		    }
		    
			String reser_id 	= rd.reser_id.getText();
			connectDB();
			
			//SQL
			try {
				String sql = "SELECT * FROM db_reservation.reservation WHERE user_id = '" + reservationUserID + "'AND reservation_id = " + reser_id + ";";
		        ResultSet rSet = sqlStmt.executeQuery(sql);
		        while (rSet.next()) {
		        	String cls = rSet.getString("facility_id");
		            String day = rSet.getString("day");
		            String openTime = rSet.getString("start_time");
		            String closeTime = rSet.getString("end_time");
		            
		            String[] dateParts = day.split("-");
		    		int ryear = Integer.parseInt(dateParts[0]);
		    		int rmonth = Integer.parseInt(dateParts[1]);
		    		int rday = Integer.parseInt(dateParts[2]);

		    		String[] timeParts = closeTime.split(":");
		    		int rhour = Integer.parseInt(timeParts[0]);
		    		int rminute = Integer.parseInt(timeParts[1]);
		    		int rsecond = Integer.parseInt(timeParts[2]);

		    		LocalDateTime reservedate = LocalDateTime.of(ryear, rmonth, rday, rhour, rminute, rsecond);
		    		LocalDateTime currdate = LocalDateTime.now();
		    		
		    		if(reservedate.isBefore(currdate)){
		    			res = "Reservation date is in the past. Review the date.";
		            }else {
						//execute delete
						sql = "DELETE FROM db_reservation.reservation WHERE user_id = '" + reservationUserID + "'AND reservation_id = " + reser_id + ";";
				        sqlStmt.executeUpdate(sql);
				        String reserve ="Following reservation was canceled:   " + " Class:" + cls + " Day：" + day + " Booked Time:" + openTime + "～" + closeTime;
			            res += reserve +"\n";
					}
		        }
		        if (res.isEmpty()) {
		            res = "No matching reservation found";
		            return res;
		        }else {
		        	/*
		        	// Accumulate sorted reservations
		        	for (String reservation : reservations) {
		        	    res += reservation + "\n";
		        	}*/
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		    closeDB();
		}else {
			res = "Please log in";
			return res;
		}
	    return res;
    }
	
	

	

	

	//Gets available time for the facility
	public int[] getAvailableTime(String facility) {
		int[] availableTime = {0,0};
		connectDB();
		try {
			String sql = "SELECT * FROM db_reservation.facility WHERE facility_id =" + facility + ";";
			ResultSet rs = sqlStmt.executeQuery(sql);
			while(rs.next()) {
				String timeData = rs.getString("open_time");
				timeData = timeData.substring(0,2);
				availableTime[0]= Integer.parseInt(timeData);
				timeData = rs.getString("close_time");
				timeData = timeData.substring(0, 2);
				availableTime[1] = Integer.parseInt(timeData);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return availableTime;
	}
}