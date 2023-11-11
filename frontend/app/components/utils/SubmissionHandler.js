import sendFilesToBackend from "@/app/components/utils/SendFileToBackend";

const handleSubmit = async (files, isMoodle, isCoursera) => {
  try {
    const result = await sendFilesToBackend(files, isMoodle, isCoursera);
    return result;
  } catch (error) {
    throw error;
  }
};

export default handleSubmit;
