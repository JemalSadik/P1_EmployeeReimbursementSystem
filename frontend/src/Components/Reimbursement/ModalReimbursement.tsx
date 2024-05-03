import axios, { AxiosError, AxiosResponse } from "axios";
import { Alert, Button, Form, Modal } from "react-bootstrap";
import { ReimbursementProps } from "../../Interfaces/ReimbursementModalInterface";
import { useState } from "react";

export const ModalReimbursement: React.FC<ReimbursementProps> = ({reimbursement, show, onHide}) => {

    const baseUrl: string|null = localStorage.getItem("baseUrl");
    const user = JSON.parse(localStorage.getItem("user")||"{}");

    let descInput: string = reimbursement.description ? reimbursement.description : "";
    let statusInput: string = reimbursement.status ? reimbursement.status : "";

    const [validated, setValidated] = useState(false);
    const [descriptionError, setVDescriptionError] = useState("");
    const [statusError, setStatusError] = useState("");

    const getDesc = (input: React.ChangeEvent<HTMLTextAreaElement>) => {
        descInput = input.target.value;
    }
    const getStatus = (input: React.ChangeEvent<HTMLSelectElement>) => {
        statusInput = input.target.value;
    }

    const updateReimbursement = async () => {
        let checkClose: boolean = true;
        // TODO: add {withCredentials: true} to axios request for session support
        if (descInput !== reimbursement.description && descInput !== "") {
            const resp = await axios.patch(baseUrl + `/reimbursements/${reimbursement.reimbId}/description`, descInput, {withCredentials: true, headers: {
                "Content-Type": "text/plain"
            }})
                .then((resp: AxiosResponse) => {
                    console.log(resp.data);
                })
                .catch((error: AxiosError) => {
                    checkClose = false;
                    setVDescriptionError(error.message);
                });
        }
        // TODO: check if user role is manager first before sending request to update status
        if (statusInput !== reimbursement.status && statusInput !== "") {
            const resp = await axios.patch(baseUrl + `/reimbursements/${reimbursement.reimbId}/status`, statusInput, {withCredentials: true, headers: {
                "Content-Type": "text/plain"
            }})
                .then((resp: AxiosResponse) => {
                    console.log(resp.data);
                })
                .catch((error: AxiosError) => {
                    checkClose = false;
                    setStatusError(error.message);
                });
        }
        if (checkClose) {
            onHide();
        } else {
            setValidated(true);
        }
    }

    return (
        <Modal show={show} onHide={onHide} >
            <Modal.Header closeButton>
                <Modal.Title id={`reimbursementModal${reimbursement.reimbId}Label`}>
                    <h2>Reimbursement #{reimbursement.reimbId}</h2>
                </Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form noValidate validated={validated}>
                    <Form.Group className="mb-3">
                        <Form.FloatingLabel label="Description" controlId={`reimbDescription${reimbursement.reimbId}`}>
                            {reimbursement.status == "Pending" ? <Form.Control as="textarea" className="textbox" defaultValue={reimbursement.description} onChange={getDesc} /> : <Form.Control as="textarea" className="textbox" value={reimbursement.description} disabled />}
                            <Form.Control.Feedback type="invalid">{descriptionError}</Form.Control.Feedback>
                        </Form.FloatingLabel>
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Form.FloatingLabel label="Amount" controlId={`reimbAmount${reimbursement.reimbId}`}>
                            <Form.Control type="text" value={`$${reimbursement.amount}`} disabled />
                        </Form.FloatingLabel>
                    </Form.Group>
                    <Form.Group>
                        <Form.FloatingLabel label="Status" controlId={`reimbStatus${reimbursement.reimbId}`}>
                            {reimbursement.status == "Pending" && user.role.toLowerCase() === "manager" ? <Form.Select aria-label="Reimbursement Status" defaultValue="Pending" onChange={getStatus}>
                                <option value="Pending">Pending</option>
                                <option value="Approved">Approved</option>
                                <option value="Denied">Denied</option>
                            </Form.Select> : <Form.Select aria-label="Reimbursement Status" disabled>
                                <option value={reimbursement.status} selected>{reimbursement.status}</option>
                            </Form.Select>}
                            <Form.Control.Feedback type="invalid">{statusError}</Form.Control.Feedback>
                        </Form.FloatingLabel>
                    </Form.Group>
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onHide}>Close</Button>
                <Button variant="primary" onClick={updateReimbursement}>Save</Button>
            </Modal.Footer>
        </Modal>
        
    )
}