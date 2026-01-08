## MySQL Database Design
### Table: users
- id: INT, Primary Key, Auto Increment
- username: VARCHAR, UNIQUE, Not Null
- password_hash: VARCHAR Not Null
- user_type: VARCHAR Not Null  ('ADMIN', 'DOCTOR', 'PATIENT')

### Table: patients
- id: INT, Primary Key, Auto Increment
- user_id: INT, Foreign Key → users(id)
- patient_code: VARCHAR, Unique, Not Null
- first_name: VARCHAR, Not Null
- last_name: VARCHAR, Not Null
- middle_name: VARCHAR
- date_of_birth: Date Not Null 
- gender: VARCHAR (male/female)
- email: VARCHAR, Not Null
- phone: VARCHAR, Not Null
- location: VARCHAR, Not Null
- emergency_contact_name: VARCHAR
- emergency_contact_phone: VARCHAR
- status: INT (0 = Scheduled, 1 = Completed, 2 = Cancelled)

### Table: doctors
- id: INT, Primary Key, Auto Increment
- user_id: INT, Foreign Key → users(id)
- doctor_code: VARCHAR, UNIQUE, Not Null
- first_name: VARCHAR, Not Null
- last_name: VARCHAR, NOT NULL
- specialty: VARCHAR (e.g. Cardiology, Pediatrics)
- qualification: VARCHAR (MD, MBBS, PhD)
- phone_number: VARCHAR, NOT NULL
- email: VARCHAR, UNIQUE, NOT NULL
- license_number: VARCHAR, UNIQUE
- years_experience: INT
- is_active: BOOLEAN, DEFAULT, TRUE

### Table: admins
- id: INT, Primary Key, Auto Increment
- user_id: INT, Foreign Key → users(id)
- first_name: VARCHAR, Not Null
- last_name: VARCHAR, NOT NULL
- is_active: BOOLEAN, DEFAULT, TRUE

### Table: appointments
- id: INT, Primary Key, Auto Increment
- doctor_id: INT, Foreign Key → doctors(id)
- patient_id: INT, Foreign Key → patients(id)
- appointment_time: DATETIME, Not Null
- status: INT (0 = Scheduled, 1 = Completed, 2 = Cancelled)

### Table: payments
- id: INT, Primary Key, Auto Increment
- patient_id: INT, Foreign Key → patients(id)
- amount: DECIMAL(12,2), NOT NULL
- transaction_reference VARCHAR,
- payment_status VARCHAR, DEFAULT 'PENDING' (PENDING, PAID, FAILED, REFUNDED)
- payment_date DATETIME, DEFAULT CURRENT_TIMESTAMP


### doctor_appointment_slots
- id: INT, Primary Key, Auto Increment
- doctor_id: INT, Foreign Key → doctors(id)
- slot_date DATE, NOT NULL,
- start_time TIME, NOT NULL,
- end_time TIME, NOT NULL,
- slot_status VARCHAR, DEFAULT 'AVAILABLE' (AVAILABLE, BOOKED)


## MongoDB Collection Design
### Collection: prescriptions
```json
{
  "_id": "ObjectId('64abc123456')",
  "patientName": "John Smith",
  "appointmentId": 51,
  "medication": "Paracetamol",
  "dosage": "500mg",
  "doctorNotes": "Take 1 tablet every 6 hours.",
  "refillCount": 2,
  "pharmacy": {
    "name": "Walgreens SF",
    "location": "Market Street"
  }
}
```

### Collection: feedback
```json
{
  "_id": "ObjectId('64abc123456')",
  "feedbackType": "DOCTOR", // DOCTOR | APPOINTMENT | SYSTEM
  "submittedBy": {
    "userId": 123,           // MySQL users.user_id
    "role": "PATIENT",
    "isAnonymous": false
  },
  "doctor": {
    "doctorId": 45,         
    "name": "Dr. Alice Smith"
  },
  "appointmentId": 9876,
  "rating": 4,              // 1–5
  "comment": "Doctor was very attentive and explained clearly.",
  "createdAt": "2026-01-08T10:15:00Z",
  "updatedAt": "2026-01-08T10:15:00Z"
}
```

### Collection: prescriptions
```json
{
  "_id": "ObjectId('64abc123456')",
  "user": {
    "userId": 123,          // MySQL users.user_id
    "role": "ADMIN"          // ADMIN | DOCTOR | PATIENT
  },
  "entity": {
    "type": "APPOINTMENT",   // PATIENT, DOCTOR, PAYMENT, PRESCRIPTION, FEEDBACK, etc.
    "id": 9876
  },
  "action": "UPDATE",        // CREATE, UPDATE, DELETE, LOGIN, LOGOUT, REFUND, DISPENSE, etc.
  "description": "Updated appointment status from PENDING to COMPLETED",
  "ipAddress": "192.168.1.10",
  "status": "SUCCESS",       // SUCCESS | FAILURE
  "metadata": {
    "previousData": { ... }, // optional, old values for updates
    "newData": { ... }       // optional, new values
  },
  "createdAt": "2026-01-08T10:30:00Z"
}
```

### Collection: clinical_notes
```json
{
  "_id": "ObjectId('64abc123456')",
  "patient": {
    "patientId": 12345,          // MySQL patients.patient_id
    "name": "John Doe"
  },
  "doctor": {
    "doctorId": 678,             // MySQL doctors.doctor_id
    "name": "Dr. Alice Smith",
  },
  "appointmentId": 98765,        // optional, MySQL appointments.appointment_id
  "noteType": "PROGRESS",        // PROGRESS, OBSERVATION, DISCHARGE, LAB_REVIEW, etc.
  "title": "Follow-up on Hypertension",
  "content": "Patient BP stable at 130/80. Continue Amlodipine 5mg daily.",
  "tags": ["hypertension", "blood pressure", "medication"],
  "status": "ACTIVE",             // ACTIVE, ARCHIVED, DELETED
  "createdAt": "2026-01-08T10:15:00Z",
  "updatedAt": "2026-01-08T10:15:00Z",
  "metadata": {
    "vitalSigns": {
      "bp": "130/80",
      "heartRate": 72,
      "temperature": 36.8
    },
  }
}

```
