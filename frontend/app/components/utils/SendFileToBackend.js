export const sendFileToBackend = async (file, outputFormat) => {
  const formData = new FormData();
  formData.append("file", file[0]);

  try {
    const response = await fetch(
      `${process.env.NEXT_PUBLIC_BACKEND_URL}/file/convert/${outputFormat}`,
      {
        method: "POST",
        body: formData,
        headers: {
          Accept: "application/json",
        },
      },
    );

    if (!response.ok) {
      throw new Error(`Server responded with a status of ${response.status}`);
    }

    const data = await response.json();

    const mimeType =
      outputFormat === "moodleXML"
        ? "application/xml"
        : "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

    const fileData = data.file;
    const fileName = data.fileName;
    if (data.details.questionCount > 0) {
      const link = document.createElement("a");
      link.href = `data:${mimeType};base64,${fileData}`;
      link.download = fileName;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    }

    return { details: data.details };
  } catch (error) {
    throw error;
  }
};
