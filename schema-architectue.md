This document details the architecture of the Smart Clinic Management System. The application is built using Sprint Boot and contains both MVC and REST controllers. The Admin and Doctor dashboards are developed using Thymeleaf templates. Other modules are developed using REST APIs. Both interact with a common Service Layer which handles all the application logic and processing required.

The system is connected to two databases, MySQL and MongoDB. MySQL handles all relational tables such as patient, doctor, appointment, and admin data while MongoDB handles unstructured data like prescriptions.

1. User clicks to access Appointment page.
2. Action is rotued to Thymeleaf controller.
3. The controller calls the Appointment Service Layer.
4. The service layer calls the MySQL repository layer.
5. Repository layer fetches all appointments for the MySQL db and returns to list of appointments to service layer.
6. Service layer process the list (eg. places it into Upcomming Appointments, Cancelled Appointments, etc). and returns data to controller.
7. Controller returns the view to the user for Appointment page.