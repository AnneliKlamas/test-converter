export default async function sendFilesToBackend(files, isMoodle, isCoursera) {
  const formData = new FormData();

  files.forEach((file) => {
    formData.append("files", file);
  });

  formData.append("isMoodle", isMoodle);
  formData.append("isCoursera", isCoursera);

  for (let [key, value] of formData.entries()) {
  }
  try {
    const response = await fetch(process.env.NEXT_PUBLIC_BACKEND_URL, {
      method: "POST",
      body: formData,
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const result = await response.json();
    return result;
  } catch (error) {
    throw error;
  }
}
