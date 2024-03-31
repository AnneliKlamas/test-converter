export const sendFileToBackend = async (file, outputFormat) => {
  const formData = new FormData();
  formData.append("file", file[0]);

  try {
    const response = await fetch(
      `${process.env.NEXT_PUBLIC_BACKEND_URL}/file/convert/moodleXML`,
      {
        method: "POST",
        body: formData,
      },
    );

    return await response.json();
  } catch (error) {
    throw error;
  }
};
