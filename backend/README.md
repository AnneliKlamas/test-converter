## To run:

Requires Java 21

Run QuizConverterApplication

Runs on port 8000

## About the project:

This project is a simple quiz converter that takes a DOCX file and converts it to a Moodle XML format or to the Coursera DOCX depending on which endpoint is used.

Currently, question description nor answer can't start with the words "question", "feedback" nor "\t".

Feedback doesn't support pictures.

Answers and question description can have pictures.

Pictures are always added to the end of the text.

\* marks right answers.

Supports only single choice and multiple choice/checkbox questions.

Doesn't support multi paragraph question descriptions. 

Coursera doesn't support default feedback for multi nor single choice questions.

