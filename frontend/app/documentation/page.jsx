export default function Documentation() {
  return (
    <main className="flex flex-col items-center justify-center pt-10 gap-5">
      <h1 className="text-4xl font-bold text-blue">Documentation</h1>
      <p>Here, you can find the rules and guidelines for your document.</p>
      <div className="flex flex-col items-center justify-center">
        <h2 className="text-lg font-semibold text-blue">
          Rules and Guidelines
        </h2>
        <ul className="list-disc pl-5 items-center justify-center">
          <li>
            Question descriptions nor answers can't start with the words
            "question", "feedback", nor "\t".
          </li>
          <li>Feedback doesn't support pictures.</li>
          <li>
            Answers and question descriptions can have pictures, which are
            always added to the end of the text.
          </li>
          <li>Asterisks (*) mark the correct answers.</li>
          <li>
            Supports only single choice and multiple choice/checkbox questions.
          </li>
          <li>Doesn't support multi-paragraph question descriptions.</li>
          <li>
            Coursera doesn't support default feedback for multi nor single
            choice questions.
          </li>
          <li>
            By default, there is no shuffle in the order of questions or
            answers.
          </li>
          <li>
            For multiple choice questions in Moodle, every correct answer gives
            100/nrOfCorrectAnswers percentage points, and every incorrect answer
            gives -100/nrOfCorrectAnswers percentage points. In Coursera, it
            behaves as "no partial credit".
          </li>
        </ul>
      </div>
      <button className="px-4 py-2 bg-blue-500 text-blue border border-3 rounded-md hover:bg-gray-100">
        Download .DOCX Template
      </button>
    </main>
  );
}
