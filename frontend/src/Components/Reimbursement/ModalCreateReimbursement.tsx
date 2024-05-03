import { Alert, Button, FloatingLabel, Form, InputGroup, Modal } from "react-bootstrap"
import { CreateReimbursementProps } from "../../Interfaces/CreateReimbursementModalInterface"
import { useState } from "react";
import axios, { AxiosError, AxiosResponse } from "axios";

export const ModalCreateReimbursement: React.FC<CreateReimbursementProps> = ({show, onHide}) => {

    const baseUrl = localStorage.getItem("baseUrl");

    let descInput: string = "";
    let amountInput: number = 0;

    const [validated, setValidated] = useState(true);
    const [errorMessage, setErrorMessage] = useState("");

    const getDesc = (input: React.ChangeEvent<HTMLTextAreaElement>) => {
        descInput = input.target.value;
    };
    const getAmount = (input: React.ChangeEvent<HTMLTextAreaElement>) => {
        amountInput = Number.parseInt(input.target.value);
    };

    const createReimbursement = async () => {
        if (amountInput <= 0) {
            setErrorMessage("Amount must be greater than 0");
            setValidated(false);
            return;
        }
        const resp = await axios.post(baseUrl + "/reimbursements", {
            description: descInput,
            amount: amountInput
        }, {withCredentials: true}).then((resp: AxiosResponse) => {
            onHide();
        }).catch((error: AxiosError) => {
            setErrorMessage(error.message);
        });
    }

    return (
        <Modal show={show} onHide={onHide}>
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
                {!validated && (
                    <Alert variant="danger">
                        <h3>Failed to create reimbursement</h3>
                        <p>{errorMessage}</p>
                    </Alert>
                )}
            </Modal.Body>
            <Modal.Footer>
                <Button variant="primary" onClick={createReimbursement}>Save</Button>
                <Button variant="secondary" onClick={onHide}>Close</Button>
            </Modal.Footer>
        </Modal>
    )
}