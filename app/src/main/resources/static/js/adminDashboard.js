// ================= IMPORTS =================
import { openModal, closeModal } from "./modal.js";
import { getDoctors, filterDoctors, saveDoctor } from "./doctorService.js";
import { createDoctorCard } from "./doctorCard.js";

// ================= DOM ELEMENTS =================
const contentDiv = document.getElementById("content");
const addDoctorBtn = document.getElementById("addDoctorBtn");
const searchInput = document.getElementById("searchDoctor");
const timeFilter = document.getElementById("timeFilter");
const specialtyFilter = document.getElementById("specialtyFilter");

// ================= ADD DOCTOR BUTTON =================
if (addDoctorBtn) {
  addDoctorBtn.addEventListener("click", () => {
    openModal("addDoctor");
  });
}

// ================= INITIAL LOAD =================
document.addEventListener("DOMContentLoaded", () => {
  loadDoctorCards();
});

// ================= LOAD ALL DOCTORS =================
const loadDoctorCards = async () => {
  try {
    const doctors = await getDoctors();
    renderDoctorCards(doctors);
  } catch (error) {
    console.error("Error loading doctors:", error);
  }
};

// ================= FILTER LISTENERS =================
if (searchInput) {
  searchInput.addEventListener("input", filterDoctorsOnChange);
}

if (timeFilter) {
  timeFilter.addEventListener("change", filterDoctorsOnChange);
}

if (specialtyFilter) {
  specialtyFilter.addEventListener("change", filterDoctorsOnChange);
}

// ================= FILTER DOCTORS =================
const filterDoctorsOnChange = async () => {
  try {
    const name = searchInput.value.trim() || null;
    const time = timeFilter.value || null;
    const specialty = specialtyFilter.value || null;

    const response = await filterDoctors(name, time, specialty);
    const doctors = response.doctors || [];

    if (doctors.length === 0) {
      contentDiv.innerHTML =
        "<p>No doctors found with the given filters.</p>";
      return;
    }

    renderDoctorCards(doctors);
  } catch (error) {
    console.error("Error filtering doctors:", error);
    alert("Failed to filter doctors. Please try again.");
  }
};

// ================= RENDER DOCTOR CARDS =================
const renderDoctorCards = (doctors) => {
  contentDiv.innerHTML = "";
  doctors.forEach((doctor) => {
    const card = createDoctorCard(doctor);
    contentDiv.appendChild(card);
  });
};

// ================= ADD DOCTOR =================
window.adminAddDoctor = async () => {
  const name = document.getElementById("doctorName").value;
  const email = document.getElementById("doctorEmail").value;
  const phone = document.getElementById("doctorPhone").value;
  const password = document.getElementById("doctorPassword").value;
  const specialty = document.getElementById("doctorSpecialty").value;
  const availableTimes = document.getElementById("doctorTimes").value;

  const token = localStorage.getItem("token");
  if (!token) {
    alert("Authentication required. Please log in again.");
    return;
  }

  const doctor = {
    name,
    email,
    phone,
    password,
    specialty,
    availableTimes
  };

  try {
    const result = await saveDoctor(doctor, token);

    if (result.success) {
      alert("Doctor added successfully");
      closeModal("addDoctor");
      window.location.reload();
    } else {
      alert(result.message || "Failed to add doctor");
    }
  } catch (error) {
    console.error("Error adding doctor:", error);
    alert("Something went wrong while adding the doctor.");
  }
};
