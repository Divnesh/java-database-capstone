// ================= IMPORTS =================
import { openModal } from "./modal.js";
import { API_BASE_URL } from "./config.js";

// ================= API ENDPOINTS =================
const ADMIN_API = `${API_BASE_URL}/admin/login`;
const DOCTOR_API = `${API_BASE_URL}/doctor/login`;

// ================= PAGE LOAD =================
window.onload = () => {
  const adminLoginBtn = document.getElementById("adminLogin");
  const doctorLoginBtn = document.getElementById("doctorLogin");

  if (adminLoginBtn) {
    adminLoginBtn.addEventListener("click", () => {
      openModal("adminLogin");
    });
  }

  if (doctorLoginBtn) {
    doctorLoginBtn.addEventListener("click", () => {
      openModal("doctorLogin");
    });
  }
};

// ================= ADMIN LOGIN HANDLER =================
window.adminLoginHandler = async () => {
  try {
    const username = document.getElementById("adminUsername").value;
    const password = document.getElementById("adminPassword").value;

    const admin = { username, password };

    const response = await fetch(ADMIN_API, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(admin)
    });

    if (!response.ok) {
      alert("Invalid admin credentials");
      return;
    }

    const data = await response.json();
    localStorage.setItem("token", data.token);

    selectRole("admin");
  } catch (error) {
    console.error(error);
    alert("Something went wrong. Please try again.");
  }
};

// ================= DOCTOR LOGIN HANDLER =================
window.doctorLoginHandler = async () => {
  try {
    const email = document.getElementById("doctorEmail").value;
    const password = document.getElementById("doctorPassword").value;

    const doctor = { email, password };

    const response = await fetch(DOCTOR_API, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(doctor)
    });

    if (!response.ok) {
      alert("Invalid doctor credentials");
      return;
    }

    const data = await response.json();
    localStorage.setItem("token", data.token);

    selectRole("doctor");
  } catch (error) {
    console.error(error);
    alert("Something went wrong. Please try again.");
  }
};