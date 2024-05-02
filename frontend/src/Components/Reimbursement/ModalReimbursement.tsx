import axios, { AxiosError, AxiosResponse } from "axios";
import { Button, Form, Modal } from "react-bootstrap";
import { ReimbursementProps } from "../../Interfaces/ReimbursementPropsInterface";
import { useState } from "react";
import { UserInterface } from "../../Interfaces/UserInterface";

export const ModalReimbursement: React.FC<ReimbursementProps> = ({reimbursement, show, onHide}) => {

    const user: UserInterface = JSON.parse(localStorage.getItem("userId")||"");
    const baseUrl: string|null = localStorage.getItem("baseUrl");

    let descInput: string = reimbursement.description ? reimbursement.description : "";
    let statusInput: string = reimbursement.status ? reimbursement.status : "";

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
            const resp = await axios.patch(baseUrl + `/${reimbursement.reimbId}/description`, descInput, {withCredentials: true})
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
            const resp = await axios.patch(baseUrl + `/${reimbursement.reimbId}/status`, statusInput, {withCredentials: true})
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
        }
    }

    return (
        <Modal show={show} onHide={onHide()} >
            <Modal.Dialog>
                <Modal.Header closeButton>
                    <Modal.Title id={`reimbursementModal${reimbursement.reimbId}Label`}>
                        <h1>Reimbursement</h1>
                    </Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <div id={`reimbErrorContainer${reimbursement.reimbId}`} className="border border-danger bg-danger-subtle"></div>
                    <Form>
                        <Form.Group>
                            <Form.FloatingLabel label="Description" controlId={`reimbDescription${reimbursement.reimbId}`}>
                                {reimbursement.status == "Pending" ? <Form.Control as="textarea" className="textbox" value={reimbursement.description} onChange={getDesc} /> : <Form.Control as="textarea" className="textbox" value={reimbursement.description} disabled />}
                                <Form.Control.Feedback type="invalid">{descriptionError}</Form.Control.Feedback>
                            </Form.FloatingLabel>
                        </Form.Group>
                        <Form.Group>
                            <Form.FloatingLabel label="Amount" controlId={`reimbAmount${reimbursement.reimbId}`}>
                                <Form.Control type="text" value={`$${reimbursement.amount}`} disabled />
                            </Form.FloatingLabel>
                        </Form.Group>
                        <Form.Group>
                            <Form.FloatingLabel label="Status" controlId={`reimbStatus${reimbursement.reimbId}`}>
                                {reimbursement.status == "Pending" ? <Form.Select aria-label="Reimbursement Status" onChange={getStatus}>
                                    <option value="Pending" selected>Pending</option>
                                    <option value="Approved">Approved</option>
                                    <option value="Denied">Denied</option>
                                </Form.Select> : <Form.Select aria-label="Reimbursement Status">
                                    <option value={reimbursement.status} selected disabled>{reimbursement.status}</option>
                                </Form.Select>}
                                <Form.Control.Feedback type="invalid">{statusError}</Form.Control.Feedback>
                            </Form.FloatingLabel>
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={onHide()}>Close</Button>
                    <Button variant="primary" onClick={updateReimbursement}>Save</Button>
                </Modal.Footer>
            </Modal.Dialog>
        </Modal>
        
    )
}