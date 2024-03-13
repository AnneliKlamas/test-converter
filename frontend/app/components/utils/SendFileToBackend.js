export const sendFilesToBackend = async (files, outputFormat) => {
  const formData = new FormData();

  formData.append("files", files);
  formData.append("outputFormat", outputFormat);
  
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
};
