import axios from "axios";
import { useState } from "react";
import { Button, Form, Modal } from "react-bootstrap";

export const ModalReimbursement: React.FC<any> = ({reimbursement, key}) => {

    const [show, setShow] = useState(false);

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    let descInput: string = reimbursement.description;
    let statusInput: string = reimbursement.status;

    const getDesc = (input: React.ChangeEvent<HTMLTextAreaElement>) => {
        descInput = input.target.value;
    }

    const getStatus = (input: React.ChangeEvent<HTMLSelectElement>) => {
        statusInput = input.target.value;
    }

    const updateReimbursement = async () => {
        let checkClose: boolean = false;
        // TODO: add {withCredentials: true} to axios request for session support
        if (descInput !== reimbursement.description) {
            const resp = await axios.patch(`http://localhost:8080/${reimbursement.reimbId}/description`, descInput)
                .then((resp) => checkClose = true)
                .catch((error) => {
                    // change checkClose to false to ensure error is displayed
                    checkClose = false;

                    const errorContainer = document.getElementById(`reimbErrorContainer${key}`);
                    
                    if (errorContainer) {
                        let errorEl = document.createElement('p');
                        errorEl.innerHTML = error;
                        errorContainer.appendChild(errorEl);
                    } else {
                        alert(error);
                    }
                });
        }
        // TODO: check if user role is manager first before sending request to update status
        if (statusInput !== reimbursement.status) {
            const resp = await axios.patch(`http://localhost:8080/${reimbursement.reimbId}/status`, statusInput)
                .then((resp) => checkClose = true)
                .catch((error) => {
                    // change checkClose to false to ensure error is displayed
                    checkClose = false;

                    const errorContainer = document.getElementById(`reimbErrorContainer${key}`);
                    
                    if (errorContainer) {
                        let errorEl = document.createElement('p');
                        errorEl.innerHTML = error;
                        errorContainer.appendChild(errorEl);
                    } else {
                        alert(error);
                    }
                });
        }
        setShow(checkClose);
    }

    return (
        <Modal show={show} onHide={handleClose}>
            <Modal.Dialog>
                <Modal.Header closeButton>
                    <Modal.Title >
                        <h1 id={`reimbursementModal${key}Label`}>Reimbursement</h1>
                    </Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <div id={`reimbErrorContainer${key}`} className="border border-danger bg-danger-subtle"></div>
                    <Form>
                        <Form.FloatingLabel label="Description" controlId={`reimbDescription${key}`}>
                            {reimbursement.status == "Pending" ? <Form.Control as="textarea" className="textbox" value={reimbursement.description} onChange={getDesc} /> : <Form.Control as="textarea" className="textbox" value={reimbursement.description} disabled />}
                        </Form.FloatingLabel>
                        <Form.FloatingLabel label="Amount" controlId={`reimbAmount${key}`}>
                            <Form.Control type="text" value={`$${reimbursement.amount}`} disabled />
                        </Form.FloatingLabel>
                        <Form.FloatingLabel label="Status" controlId={`reimbStatus${key}`}>
                            {reimbursement.status == "Pending" ? <Form.Select aria-label="Reimbursement Status">
                                <option value="Pending" selected>Pending</option>
                                <option value="Approved">Approved</option>
                                <option value="Denied">Denied</option>
                            </Form.Select> : <Form.Select aria-label="Reimbursement Status">
                                <option value={reimbursement.status} selected disabled>{reimbursement.status}</option>
                            </Form.Select>}
                        </Form.FloatingLabel>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>Close</Button>
                    <Button variant="primary" onClick={updateReimbursement}>Save</Button>
                </Modal.Footer>
            </Modal.Dialog>
        </Modal>
        
    )
}