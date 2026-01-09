// ================= IMPORTS =================
import { openModal } from "./components/modals.js";
import { getDoctors, filterDoctors, saveDoctor } from "./services/doctorServices.js";
import { createDoctorCard } from "./components/doctorCard.js";

// ================= ADD DOCTOR BUTTON =================
document.addEventListener("DOMContentLoaded", () => {
  const addDoctorBtn = document.getElementById("addDocBtn");

  if (addDoctorBtn) {
    addDoctorBtn.addEventListener("click", () => {
      openModal("addDoctor");
    });
  }
});

// ================= INITIAL LOAD =================
document.addEventListener("DOMContentLoaded", () => {
  loadDoctorCards();
});

// ================= LOAD ALL DOCTORS =================
export function loadDoctorCards() {
  getDoctors()
    .then(doctors => {
      const contentDiv = document.getElementById("content");
      contentDiv.innerHTML = ""; 

      doctors.forEach(doctor => {
        const card = createDoctorCard(doctor);
        contentDiv.appendChild(card);
      });
    })
    .catch(error => {
      console.error(" Failed to load doctors:", error);
    });
}

// ================= FILTER LISTENERS =================
document.getElementById("searchBar").addEventListener("input", filterDoctorsOnChange);
document.getElementById("filterTime").addEventListener("change", filterDoctorsOnChange);
document.getElementById("filterSpecialty").addEventListener("change", filterDoctorsOnChange);

// ================= FILTER DOCTORS =================
function filterDoctorsOnChange() {
  const searchBar = document.getElementById("searchBar").value.trim();
  const filterTime = document.getElementById("filterTime").value;
  const filterSpecialty = document.getElementById("filterSpecialty").value;

  const name = searchBar.length > 0 ? searchBar : null;
  const time = filterTime.length > 0 ? filterTime : null;
  const specialty = filterSpecialty.length > 0 ? filterSpecialty : null;

  filterDoctors(name , time ,specialty)
    .then(response => {
      const doctors = response.doctors;
      const contentDiv = document.getElementById("content");
      contentDiv.innerHTML = "";
      if (doctors.length > 0) {
        console.log(doctors);
        doctors.forEach(doctor => {
          const card = createDoctorCard(doctor);
          contentDiv.appendChild(card);
        });
      } else {
        contentDiv.innerHTML = "<p>No doctors found with the given filters.</p>";
        console.log("Nothing");
      }
    })
    .catch(error => {
      console.error(" Failed to filter doctors:", error);
      alert("❌ An error occurred while filtering doctors.");
    });
}

// ================= RENDER DOCTOR CARDS =================
export function renderDoctorCards(doctors) {
  const contentDiv = document.getElementById("content");
      contentDiv.innerHTML = ""; 

      doctors.forEach(doctor => {
        const card = createDoctorCard(doctor);
        contentDiv.appendChild(card);
      });
}

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
    const { success, message } = await saveDoctor(doctor, token);
    if (success) {
      alert(message);
      document.getElementById("modal").style.display = "none";
      window.location.reload();

    } else {
      alert("❌ Error: " + message);
    }
  } catch (error) {
    console.error("Error adding doctor:", error);
    alert("Something went wrong while adding the doctor.");
  }
};
