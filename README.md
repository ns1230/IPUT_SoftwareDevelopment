Overview
This repository contains a Java-based desktop application for a classroom reservation system. The application allows users to log in, view classroom availability, make new reservations, and cancel existing ones.

Components
BookingConfirmation.java: A dialog for confirming bookings. Users can select facilities and specify dates for their reservation.

CancelReservationDialog.java: A dialog for canceling existing reservations. Users can enter the reservation ID to cancel their booking.

ChoiceFacility.java: A custom UI component for selecting a facility from a list.

ChoiceHour.java and ChoiceMinute.java: UI components for selecting time in hours and minutes format.

LoginDialog.java: A dialog for user authentication. It handles user logins with ID and password.

MainFrame.java: The main application window. It integrates various components and serves as the primary interface for user interactions.

ReservationControl.java: The core control class. It handles database connections, reservation logic, and user authentication.

ReservationDialog.java: A dialog for making new reservations. It allows users to choose a facility, date, and time for their booking.

ReservationSystem.java: The main class that launches the application.

UserReservationDialog.java: A dialog for viewing and managing user-specific reservations.

How to Use
Compile and run ReservationSystem.java to start the application.
Log in using a valid user ID and password.
Navigate through the application to make or cancel reservations, and view classroom availability.
