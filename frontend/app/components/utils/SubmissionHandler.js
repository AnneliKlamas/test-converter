import sendFilesToBackend from "@/app/components/utils/SendFileToBackend";

const handleSubmit = async (files) => {
  try {
    const result = await sendFilesToBackend(files);
    return result;
  } catch (error) {
    throw error;
  }
};

export default handleSubmit;
