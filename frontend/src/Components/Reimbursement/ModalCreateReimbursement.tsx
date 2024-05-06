import { Alert, Button, FloatingLabel, Form, InputGroup, Modal } from "react-bootstrap"
import { CreateReimbursementProps } from "../../Interfaces/CreateReimbursementModalInterface"
import { useState } from "react";
import axios, { AxiosError, AxiosResponse } from "axios";

export const ModalCreateReimbursement: React.FC<{show: boolean, onHide: () => void, setShowAlertSuccess: () => void, setAlertSuccessMessage: () => void, refreshReimbursements: () => void}> = ({show, onHide, setShowAlertSuccess, setAlertSuccessMessage, refreshReimbursements}) => {

    const baseUrl = localStorage.getItem("baseUrl");

    let descInput: string = "";
    let amountInput: number = 0;

    const [validated, setValidated] = useState(true);
    const [errorMessage, setErrorMessage] = useState("");
    const [showErrorAlert, setShowErrorAlert] = useState(false);

    const getDesc = (input: React.ChangeEvent<HTMLTextAreaElement>) => {
        descInput = input.target.value;
    };
    const getAmount = (input: React.ChangeEvent<HTMLTextAreaElement>) => {
        amountInput = Number.parseFloat(input.target.value);
    };

    const createReimbursement = async () => {
        if (amountInput <= 0 && descInput === "") {
            setErrorMessage("Amount must be greater than 0. Description cannot be empty.");
            setValidated(false);
            setShowErrorAlert(true);
            return;
        } else if (amountInput <= 0) {
            setErrorMessage("Amount must be greater than 0.");
            setValidated(false);
            setShowErrorAlert(true);
            return;
        } else if (descInput === "") {
            setErrorMessage("Description cannot be empty.");
            setValidated(false);
            setShowErrorAlert(true);
            return;
        }
        const resp = await axios.post(baseUrl + "/reimbursements", {
            description: descInput,
            amount: amountInput
        }, {withCredentials: true}).then((resp: AxiosResponse) => {
            setAlertSuccessMessage();
            setShowAlertSuccess();
            setShowErrorAlert(false);
            onHide();
            // Refresh reimbursement list based on view
            refreshReimbursements();
        }).catch((error: AxiosError) => {
            setErrorMessage(error.message);
        });
    }

    const onClickClose = () => {
        setShowErrorAlert(false);
        onHide();
    }

    return (
        <Modal show={show} onHide={onClickClose}>
            <Modal.Header closeButton>
                <Modal.Title id="createReimbursementModal">
                    <h2>Create Reimbursement</h2>
                </Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form noValidate id="createReimbursentForm">
                    <Form.Group className="mb-3">
                        <FloatingLabel label="Description">
                            <Form.Control type="textarea" className="textbox" onChange={getDesc}/>
                        </FloatingLabel>
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <InputGroup>
                            <InputGroup.Text id="usd-sign">$</InputGroup.Text>
                            <FloatingLabel label="Amount">
                                <Form.Control type="number" step="0.01" aria-describedby="usd-sign" onChange={getAmount}/>
                            </FloatingLabel>
                        </InputGroup>
                    </Form.Group>
                </Form>
                {showErrorAlert && (
                    <Alert variant="danger" onClose={() => setShowErrorAlert(false)} dismissible>
                        <h3>Failed to create reimbursement</h3>
                        <p>{errorMessage}</p>
                    </Alert>
                )}
            </Modal.Body>
            <Modal.Footer>
                <Button variant="primary" onClick={createReimbursement}>Save</Button>
                <Button variant="secondary" onClick={onClickClose}>Close</Button>
            </Modal.Footer>
        </Modal>
    )
}