export const sendFileToBackend = async (file, outputFormat) => {
  const formData = new FormData();
  formData.append("file", file[0]);

  try {
    const response = await fetch(
      `${process.env.NEXT_PUBLIC_BACKEND_URL}/file/convert`,
      {
        method: "POST",
        body: formData,
      },
    );

    if (!response.ok) {
      throw new Error(`HTTP error ${response.status}`);
    }

    const fileName = file[0].name.split(".")[0];
    const blob = await response.blob();
    const url = URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.href = url;

    const fileExtension =
      outputFormat === "moodle"
        ? "xml"
        : outputFormat === "coursera"
        ? "docx"
        : "";

    if (fileExtension) {
      link.download = `${fileName}.${fileExtension}`;
    } else {
      throw new Error("Invalid output format");
    }

    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    URL.revokeObjectURL(url);

    return { success: true };
  } catch (error) {
    throw error;
  }
};
