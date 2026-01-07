# User Story Template

**Title:**
_As a [user role], I want [feature/goal], so that [reason]._

**Acceptance Criteria:**
1. [Criteria 1]
2. [Criteria 2]
3. [Criteria 3]

**Priority:** [High/Medium/Low]
**Story Points:** [Estimated Effort in Points]
**Notes:**
- [Additional information or edge cases]


# Admin User Stories
Title:
_As an admin, I want login with my username and password, so that can manage platform securely._

Acceptance Criteria:
1. Admin can access login page with authenticated credentials.
2. Allow user to enter username and password and click Login.
3. System authenticates supplied credentials and grants access admin dashboard on success.
4. System displays Invalid credentials error message if authentication fails.
5. Maintain login session and expires after period of 20 mins of inactivity.

Priority: High
Story Points: 5
Notes:
- Passwords must be stored and compared using secure hashing.
- Login page should be accessible only over HTTPS.

Title:
_As an admin, I want be able to log out of the portal, so that system access is protected._

Acceptance Criteria:
1. Admin is able to click Logout.
2. Controller handles request and sends request to service layer.
3. Service layer clears session and sends state to controller.
4. Controller redirects admin to login page.

Priority: Medium
Story Points: 3
Notes:
- User should not be able to navigate back admin dashboard

Title:
_As an admin, I want be able to add doctors to the portal, so that doctors use the portal._

Acceptance Criteria:
1. Admin is able to click Add Doctor on the admin dashboard.
2. Admin can enter required doctor details (e.g., name, specialty, contact information, username/email).
3. Admin can submit to add new doctor.
4. System validates all required fields and prevents saving with invalid or incomplete data.
5. System adds the doctor to the database.
6. Admin gets redirected to doctors list and added doctor is visible in the list.
7. System shows success message of doctor added.

Priority: High
Story Points: 8
Notes:
- Handle addition of duplicates.

Title:
_As an admin, I want be able to delete doctors profile, so that inactive doctors are removed._

Acceptance Criteria:
1. Admin can view a list of all doctors in the portal.
2. Admin can select a specific doctor profile and choose the delete option.
3. System prompts the admin with a confirmation message before deletion.
4. Upon confirmation, the doctor’s profile is deactivated from the system.
5. Deactivated doctors no longer have access to the portal.
6. System displays a success message after the deactivation is completed.

Priority: Medium
Story Points: 5
Notes:
- Ensure deactivated doctors cant access doctor dashboard
- Ensure users cant view/schedule appointments with deactivated doctors.

Title:
_As an admin, I want be able to run stored procedure in MySQL CLI, so that admin can get no. of appointments per month for statistics._

Acceptance Criteria:
1. A stored procedure exists in the database.
2. Admin can successfully execute the stored procedure from the MySQL CLI.
3. The stored procedure returns the total number of appointments per month.
4. Results accurately reflect the data stored in the appointments table.
5. The procedure execution does not modify or corrupt existing data.

Priority: Medium
Story Points: 5
Notes:
- SP should handle edge cases such as months with zero appointments.

# Patient User Stories

Title:
_As a patient, I want view list of doctors without logging in, so that patients dont need to register to view doctors._

Acceptance Criteria:
1. Patients can access the doctors list page without registering.
2. The list displays basic, non-sensitive doctor information (e.g., name, specialty, location).
3. Patients cannot view or access restricted details or perform actions that require login.

Priority: Low
Story Points: 3
Notes:
- Ensure no personal or sensitive data is exposed to unauthenticated users.
- Consider adding search or filter options (e.g., by specialty or location).

Title:
_As a patient, I want sign up using email and password, so that I can book appointments._

Acceptance Criteria:
1. Patient can access the sign-up page and register using a valid email and password.
2. System validates the email format and enforces password strength requirements.
3. System prevents registration with an email that already exists.
4. Upon successful sign-up, the account is created and the patient can log in.

Priority: Medium
Story Points: 5
Notes:
- Passwords must be securely hashed and stored.
- Provide clear error messages for invalid input or failed registration.

Title:
_As a patient, I want to log out of the portal, so that my account remains secure when I am finished managing my bookings._

Acceptance Criteria:
1. Patient can see a logout option in the portal.
2. Patient can select the logout option.
3. System clears patient session.
4. After logout, the patient is redirected to the home page.
5. Patient cannot access protected pages after logging out without logging in again.

Priority: Low
Story Points: 3
Notes:
- Session data should be cleared on logout.

Title:
_As a patient, I want to be able to book an hour-long appointment with a doctor, so that I can consult with them at a scheduled time._

Acceptance Criteria:
1. Logged in patient can view available doctors and their available time slots.
2. Patient can select a doctor and select an appointment time with a fixed duration of one hour.
3. System prevents double-booking of the same doctor and time slot.
4. Upon successful booking, the appointment is saved and visible in the user’s bookings.
5. System displays a confirmation message with appointment details.

Priority: High
Story Points: 8
Notes:
- Time slots should be based on the doctor’s availability.
- Consider time zone handling if users and doctors are in different locations.

Title:
_As a patient, I want to view my upcoming appointments, so that I can prepare accordingly._

Acceptance Criteria:
1. Patient must be logged in to access upcoming appointments.
2. System displays a list of all future appointments for the user.
3. Each appointment shows key details (doctor name, date, time, and duration).
4. Appointments are ordered chronologically by date and time.
5. If there are no upcoming appointments, the system displays a clear message.

Priority: Medium
Story Points: 5
Notes:
- Ensure date and time are displayed in the user’s local time zone.
- Allow quick navigation to appointment details or management options.

Title:
_As a doctor, I want to log into the portal, so that I can manage my appointments._

Acceptance Criteria:
1. Doctor can access the login page and enter their credentials.
2. System authenticates the credentials and grants access to the doctor dashboard.
3. Upon login, the doctor can view, reschedule, or cancel existing appointments.
4. System displays an error message if login credentials are invalid.

Priority: Medium
Story Points: 5
Notes:
- Doctors should only be able to view their appointments.

Title:
_As a doctor, I want to be able to logout, so that I cam protect my data._

Acceptance Criteria:
1. Doctors can see and access a logout option when logged into the portal.
2. Selecting logout immediately ends the doctor's session.
3. After logging out, the doctor is redirected to the login page.
4. Doctor cannot access any protected pages or perform actions without logging in again.

Priority: Low
Story Points: 3
Notes:
- Doctor cant click back and go to doctors dashboard.

Title:
_As a doctor, I want to view my appointment calendar, so that I can stay organized and manage my schedule effectively._

Acceptance Criteria:
1. Doctor must be logged in to access the appointment calendar.
2. The calendar displays all upcoming appointments in a clear, chronological format.
3. Each appointment shows key details such as patient details, date, time, and duration.
4. Doctors can navigate between days, weeks, and months to view appointments.
5. The calendar updates in real-time if appointments are added, rescheduled, or canceled.
6. If there are no appointments, a clear message is displayed.

Priority: Medium
Story Points: 5
Notes:
- Ensure doctors cant view other doctors schedule.

Title:
_As a doctor, I want to mark my unavailability, so that patients only see my available appointment slots._

Acceptance Criteria:
1. Doctor can select specific dates and times to mark as unavailable.
2. System updates the appointment calendar to hide unavailable slots from patients.
3. Patients attempting to book appointments cannot select unavailable slots.
4. Doctor receives confirmation that unavailability has been successfully saved.

Priority: Medium
Story Points: 5
Notes:
- Ensure updates to unavailability do not affect already confirmed appointments.
- Display unavailable slots clearly in the doctor’s own calendar for reference.

Title:
_As a doctor, I want to update my profile with my specialization and contact information, so that patients have up-to-date information._

Acceptance Criteria:
1. Doctor can edit fields such as specialization, phone number, email, and other contact details.
2. System validates the input (e.g., correct email format, valid phone number).
3. Updated information is saved successfully and reflected on the doctor’s public profile.
4. System displays a confirmation message after the profile is updated.

Priority: Low
Story Points: 2
Notes:
- Profile updates should not affect existing appointments.

Title:
_As a doctor, I want to view patient details for my upcoming appointments, so that I can be prepared for each consultation._

Acceptance Criteria:
1. System displays a list of upcoming appointments with patient names and key details.
2. Doctor can click on a specific appointment to view more detailed patient information if available.
3. Patient details are only visible to the assigned doctor.

Priority: Medium
Story Points: 5
Notes:
- Ensure sensitive patient data is protected and complies with privacy regulations.