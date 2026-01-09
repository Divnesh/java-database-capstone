/* ================================
   Dynamic Header Rendering
   ================================ */

function renderHeader() {
  const headerDiv = document.getElementById("header");
  if (!headerDiv) return;

  /* -------------------------------
     Root page check
  -------------------------------- */
  if (window.location.pathname.endsWith("/") || window.location.pathname.endsWith("/index.html")) {
    localStorage.removeItem("userRole");
    localStorage.removeItem("token");
    headerDiv.innerHTML = `
      <header class="header">
        <div class="logo-section">
          <img src="../assets/images/logo/logo.png"
               alt="Hospital CRM Logo"
               class="logo-img">
          <span class="logo-title">Hospital CMS</span>
        </div>
      </header>
    `;
    return;
  }

  /* -------------------------------
     Retrieve session data
  -------------------------------- */
  const role = localStorage.getItem("userRole");
  const token = localStorage.getItem("token");

  /* -------------------------------
     Session validation
  -------------------------------- */
  if (
    (role === "loggedPatient" || role === "admin" || role === "doctor") &&
    !token
  ) {
    localStorage.removeItem("userRole");
    alert("Session expired or invalid login. Please log in again.");
    window.location.href = "/";
    return;
  }

  /* -------------------------------
     Base header structure
  -------------------------------- */
  let headerContent = `
    <header class="header">
      <div class="logo-section">
        <img src="../assets/images/logo/logo.png"
             alt="Hospital CRM Logo"
             class="logo-img">
        <span class="logo-title">Hospital CMS</span>
      </div>
      <nav class="header-actions">
  `;

  /* -------------------------------
     Role-based actions
  -------------------------------- */
  if (role === "admin") {
    headerContent += `
      <button id="addDocBtn"
              class="adminBtn"
              onclick="openModal('addDoctor')">
        Add Doctor
      </button>
      <a href="#" onclick="logout()">Logout</a>
    `;
  } else if (role === "doctor") {
    headerContent += `
      <button class="adminBtn"
              onclick="selectRole('doctor')">
        Home
      </button>
      <a href="#" onclick="logout()">Logout</a>
    `;
  } else if (role === "patient") {
    headerContent += `
      <button id="patientLogin" class="adminBtn">Login</button>
      <button id="patientSignup" class="adminBtn">Sign Up</button>
    `;
  } else if (role === "loggedPatient") {
    headerContent += `
      <button class="adminBtn"
              onclick="window.location.href='/pages/loggedPatientDashboard.html'">
        Home
      </button>
      <button class="adminBtn"
              onclick="window.location.href='/pages/patientAppointments.html'">
        Appointments
      </button>
      <a href="#" onclick="logoutPatient()">Logout</a>
    `;
  }

  /* -------------------------------
     Close header
  -------------------------------- */
  headerContent += `
      </nav>
    </header>
  `;

  /* -------------------------------
     Render header
  -------------------------------- */
  headerDiv.innerHTML = headerContent;

  /* -------------------------------
     Attach dynamic listeners
  -------------------------------- */
  attachHeaderButtonListeners();
}

/* ================================
   Helper Functions
   ================================ */

function attachHeaderButtonListeners() {
  const patientLogin = document.getElementById("patientLogin");
  const patientSignup = document.getElementById("patientSignup");

  if (patientLogin) {
    patientLogin.addEventListener("click", () => {
      openModal("patientLogin");
    });
  }

  if (patientSignup) {
    patientSignup.addEventListener("click", () => {
      openModal("patientSignup");
    });
  }
}

function logout() {
  localStorage.removeItem("token");
  localStorage.removeItem("userRole");
  window.location.href = "/";
}

function logoutPatient() {
  localStorage.removeItem("token");
  window.location.href = "/";
}

/* ================================
   Initialize Header on Load
   ================================ */
document.addEventListener("DOMContentLoaded", renderHeader);
