/* ================================
   Imports
   ================================ */

// Overlay UI for booking appointments
import { showBookingOverlay } from "../loggedPatient.js";

// Admin API – delete doctor
import { deleteDoctor } from "../services/doctorServices.js";

// Patient API – fetch patient details
import { getPatientData } from "../services/patientServices.js";

/* ================================
   Create Doctor Card
   ================================ */

export function createDoctorCard(doctor) {
  /* -------------------------------
     Main card container
  -------------------------------- */
  const card = document.createElement("div");
  card.className = "doctor-card";

  /* -------------------------------
     User role
  -------------------------------- */
  const role = localStorage.getItem("userRole");

  /* -------------------------------
     Doctor information container
  -------------------------------- */
  const info = document.createElement("div");
  info.className = "doctor-info";

  const name = document.createElement("h3");
  name.textContent = doctor.name;

  const specialization = document.createElement("p");
  specialization.textContent = `Specialization: ${doctor.specialization}`;

  const email = document.createElement("p");
  email.textContent = `Email: ${doctor.email}`;

  /* -------------------------------
     Available appointment times
  -------------------------------- */
  const times = document.createElement("p");
  times.textContent = `Available Slots: ${
    doctor.availableSlots?.join(", ") || "N/A"
  }`;

  info.append(name, specialization, email, times);

  /* -------------------------------
     Actions container
  -------------------------------- */
  const actions = document.createElement("div");
  actions.className = "actions";

  /* ===============================
     ADMIN ROLE ACTIONS
     =============================== */
  if (role === "admin") {
    const deleteBtn = document.createElement("button");
    deleteBtn.className = "adminBtn";
    deleteBtn.textContent = "Delete";

    deleteBtn.addEventListener("click", async () => {
      const token = localStorage.getItem("token");
      if (!token) {
        alert("Admin session expired.");
        return;
      }

      if (!confirm("Are you sure you want to delete this doctor?")) return;

      try {
        await deleteDoctor(doctor.id, token);
        alert("Doctor deleted successfully.");
        card.remove();
      } catch (err) {
        alert("Failed to delete doctor.");
        console.error(err);
      }
    });

    actions.appendChild(deleteBtn);
  }

  /* ===============================
     PATIENT (NOT LOGGED IN)
     =============================== */
  else if (role === "patient" || !role) {
    const bookBtn = document.createElement("button");
    bookBtn.className = "button";
    bookBtn.textContent = "Book Now";

    bookBtn.addEventListener("click", () => {
      alert("Please log in to book an appointment.");
    });

    actions.appendChild(bookBtn);
  }

  /* ===============================
     LOGGED-IN PATIENT
     =============================== */
  else if (role === "loggedPatient") {
    const bookBtn = document.createElement("button");
    bookBtn.className = "button";
    bookBtn.textContent = "Book Now";

    bookBtn.addEventListener("click", async () => {
      const token = localStorage.getItem("token");
      if (!token) {
        alert("Session expired. Please log in again.");
        window.location.href = "/";
        return;
      }

      try {
        const patient = await getPatientData(token);

        showBookingOverlay({
          doctor,
          patient
        });
      } catch (err) {
        alert("Unable to start booking.");
        console.error(err);
      }
    });

    actions.appendChild(bookBtn);
  }

  /* -------------------------------
     Assemble card
  -------------------------------- */
  card.append(info, actions);

  return card;
}
