// ================= IMPORTS =================
import { API_BASE_URL } from "./config.js";

// ================= API ENDPOINT =================
const DOCTOR_API = `${API_BASE_URL }/doctor`;

// ================= GET ALL DOCTORS =================
export const getDoctors = async () => {
  try {
    const response = await fetch(`${DOCTOR_API}/all`);
    const data = await response.json();
    return data.doctors || [];
  } catch (error) {
    console.error("Error fetching doctors:", error);
    return [];
  }
};

// ================= DELETE DOCTOR =================
export const deleteDoctor = async (doctorId, token) => {
  try {
    const response = await fetch(
      `${DOCTOR_API}/delete/${doctorId}/${token}`,
      {
        method: "DELETE"
      }
    );

    const data = await response.json();

    return {
      success: response.ok,
      message: data.message || "Doctor deleted successfully"
    };
  } catch (error) {
    console.error("Error deleting doctor:", error);
    return {
      success: false,
      message: "Failed to delete doctor"
    };
  }
};

// ================= SAVE (CREATE) DOCTOR =================
export const saveDoctor = async (doctor, token) => {
  try {
    const response = await fetch(
      `${DOCTOR_API}/save/${token}`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(doctor)
      }
    );

    const data = await response.json();

    return {
      success: response.ok,
      message: data.message || "Doctor saved successfully"
    };
  } catch (error) {
    console.error("Error saving doctor:", error);
    return {
      success: false,
      message: "Failed to save doctor"
    };
  }
};

// ================= FILTER DOCTORS =================
export const filterDoctors = async (name, time, specialty) => {
  try {
    const response = await fetch(
      `${DOCTOR_API}/filter/${name}/${time}/${specialty}`
    );

    if (!response.ok) {
      console.error("Failed to filter doctors");
      return { doctors: [] };
    }

    return await response.json();
  } catch (error) {
    console.error("Error filtering doctors:", error);
    alert("Unable to fetch doctors. Please try again.");
    return { doctors: [] };
  }
};
