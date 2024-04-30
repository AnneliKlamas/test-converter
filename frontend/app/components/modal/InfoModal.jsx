import React, { useMemo } from "react";
import Link from "next/link";

import KeyValueList from "./KeyValueList";
import Section from "./Section";
import Modal from "./Modal";

import CloseIcon from "@/app/components/assets/CloseIcon";
import data from "../../data/en.json";

const InfoModal = ({ show, onClose, details }) => {
  const questionTypes = useMemo(() => {
    if (
      !details?.questionConfigDetails ||
      Object.keys(details.questionConfigDetails).length === 0
    ) {
      return null;
    }
    return (
      <KeyValueList
        data={details.questionConfigDetails}
        renderValue={(question, config) => (
          <>
            <div className="flex">
              <div>{question.trim()}:</div>
              <div className="pl-1">
                <div>{config}</div>
              </div>
            </div>
          </>
        )}
      />
    );
  }, [details.questionConfigDetails]);

  const questionErrors = useMemo(() => {
    if (
      !details?.questionErrors ||
      Object.keys(details.questionErrors).length === 0
    ) {
      return null;
    }

    return (
      <KeyValueList
        data={details.questionErrors}
        renderValue={(question, errors) => (
          <div className="flex">
            <div>{question.trim()}</div>
            <div className="pl-1">
              <KeyValueList data={errors} />
            </div>
          </div>
        )}
      />
    );
  }, [details.questionErrors]);

  const questionWarnings = useMemo(() => {
    if (
      !details?.questionWarnings ||
      Object.keys(details.questionWarnings).length === 0
    ) {
      return null;
    }

    return (
      <KeyValueList
        data={details.questionWarnings}
        renderValue={(question, errors) => (
          <>
            <div>{question.trim()}</div>
            {errors.map((error, index) => (
              <div key={index} className="flex">
                <div>{error}</div>
              </div>
            ))}
          </>
        )}
      />
    );
  }, [details.questionWarnings]);

  return (
    <Modal show={show} onClose={onClose}>
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold">{data.modal.title}</h2>
        <button onClick={onClose}>
          <CloseIcon />
        </button>
      </div>
      <div className="pb-4 text-sm">
        {data.modal.documentation}
        <Link href="/documentation" className="pl-1 text-blue">
          {data.modal.click}
        </Link>
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
        <div>
          <Section title={data.modal.questionCount}>
            <span className="pl-1">{details?.questionCount || "0"}</span>
          </Section>
          <Section title={data.modal.answersCount}>
            <span className="pl-1">{details?.answersCount || "0"}</span>
          </Section>
          <Section title={data.modal.questionPicCount}>
            <span className="pl-1">
              {details?.questionPicturesCount || "0"}
            </span>
          </Section>
          <Section title={data.modal.answerPicCount}>
            <span className="pl-1">{details?.answerPictureCount || "0"}</span>
          </Section>
          <Section title={data.modal.skippedQuestions}>
            <span className="pl-1">{details?.skippedQuestions || "0"}</span>
          </Section>
          <Section title={data.modal.questionTypes}>
            {questionTypes || <span className="pl-1">{data.modal.none}</span>}
          </Section>
          <Section title={data.modal.questionErrors}>
            {questionErrors || <span className="pl-1">{data.modal.none}</span>}
          </Section>
          <Section title={data.modal.questionWarnings}>
            {questionWarnings || (
              <span className="pl-1">{data.modal.none}</span>
            )}
          </Section>
        </div>
      </div>
    </Modal>
  );
};

export default InfoModal;
