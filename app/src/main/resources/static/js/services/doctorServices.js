// ================= IMPORTS =================
import { API_BASE_URL } from "../config/config.js";

// ================= API ENDPOINT =================
const DOCTOR_API = `${API_BASE_URL }/doctor`;

// ================= GET ALL DOCTORS =================
export const getDoctors = async () => {
  try {
    const response = await fetch(DOCTOR_API);
    const data = await response.json();
    return data.doctors;
  } catch (error) {
    console.error("Error fetching doctors:", error);
    return [];
  }
};

// ================= DELETE DOCTOR =================
export const deleteDoctor = async (doctorId, token) => {
  try {
    const response = await fetch(
      `${DOCTOR_API}/${doctorId}/${token}`,
      {
        method: "DELETE"
      }
    );

    const data = await response.json();

    const result = await response.json();
    return { success: response.ok, message: result.message };
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
      `${DOCTOR_API}/${token}`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(doctor)
      }
    );

    const result = await response.json();
    return {success : response.ok , message : result.message}
  } catch (error) {
    console.error("Error saving doctor:", error);
    return {
      success: false,
      message: "Failed to save doctor"
    };
  }
};

// ================= FILTER DOCTORS =================
export const filterDoctors = async (name, time, speciality) => {
  try {
    const response = await fetch(
      `${DOCTOR_API}/filter/${name}/${time}/${speciality}`
    );

    if (!response.ok) {
      console.error("Failed to filter doctors:", response.statusText);
      return { doctors: [] };
    }

    return await response.json();
  } catch (error) {
    console.error("Error filtering doctors:", error);
    alert("Unable to fetch doctors. Please try again.");
    return { doctors: [] };
  }
};
