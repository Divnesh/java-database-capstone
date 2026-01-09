// ================= IMPORTS =================
import { getAllAppointments } from "./services/appointmentRecordService.js";
import { createPatientRow } from "./components/patientRows.js";

let selectedDate;
// ================= DOM ELEMENTS =================
document.addEventListener("DOMContentLoaded", () => {
  const searchInput = document.getElementById("searchPatient");
  const todayBtn = document.getElementById("todayBtn");
  const datePicker = document.getElementById("datePicker");

  // ================= SEARCH FILTER =================
  if (searchInput) {
      searchInput.addEventListener("input", () => {
        const value = searchInput.value.trim();
        patientName = value !== "" ? value : null;
        loadAppointments();
      });
  }

  // ================= TODAY BUTTON =================
  if (todayBtn) {
    todayBtn.addEventListener("click", () => {
      selectedDate = new Date().toISOString().split("T")[0];
      if (datePicker) datePicker.value = selectedDate;
      loadAppointments();
    });
  }

  // ================= DATE PICKER =================
  if (datePicker) {
    selectedDate = new Date().toISOString().split("T")[0]; // 'YYYY-MM-DD'
    datePicker.addEventListener("change", () => {
      selectedDate = datePicker.value;
      loadAppointments();
    });
  }
});



// ================= LOAD APPOINTMENTS =================
const loadAppointments = async () => {
  const tableBody = document.getElementById("appointmentsTableBody");
  try {
    let token = localStorage.getItem("token");
    let patientName = null;

    const appointments = await getAllAppointments(selectedDate, patientName, token);

    tableBody.innerHTML = ""; // Clear existing rows

    if (!appointments || appointments.appointments.length === 0) {
      const noDataRow = document.createElement("tr");
      noDataRow.innerHTML = `<td colspan="4" class="text-center">No Appointments found for today.</td>`;
      tableBody.appendChild(noDataRow);
      return;
    }

    appointments.appointments.forEach((appt) => {
      const patient = {
        id: appt.id,
        name: appt.patientName,
        phone: appt.patientPhone,
        email: appt.patientEmail
      };
      const row = createPatientRow(patient, appt);
      tableBody.appendChild(row);
    });
  } catch (error) {
    console.error("Error loading appointments:", error);
    tableBody.innerHTML = `<tr><td colspan="4" class="text-center">Error loading appointments. Try again later.</td></tr>`;
  }
};

// ================= INITIAL LOAD =================
document.addEventListener("DOMContentLoaded", () => {
  if (typeof renderContent === "function") {
    renderContent(); // setup UI layout
  }
  loadAppointments();
});
