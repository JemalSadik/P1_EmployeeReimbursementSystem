import axios, { AxiosError, AxiosResponse } from "axios";
import { useState } from "react";
import { Alert, Button, Modal, FloatingLabel, Form, InputGroup } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { UserInterface } from "../../Interfaces/UserInterface";

export const UserModal: React.FC<{usr: UserInterface, show: boolean, onHide: () => void}> = ({usr, show, onHide}) => {

    const baseUrl = localStorage.getItem("baseUrl");

    const navigate = useNavigate();

    const [user, setUser] = useState({
        firstname: "",
        lastname: "",
        role: ""
    });
    
    const [showFailedMessage, setShowFailedMessage] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");

    const storeValues = (input: React.ChangeEvent<HTMLInputElement>) => {
         if (input.target.name === "firstname") {
            setUser((user) => ({...user, firstname: input.target.value}));
        } else if (input.target.name === "lastname") {
            setUser((user) => ({...user, lastname: input.target.value}));
        } else {
            setUser((user) => ({...user, role: input.target.value}));
        }
    };

    let roleInput: string = "";

    const getRole = (input: React.ChangeEvent<HTMLSelectElement>) => {
        console.log(input.target.value);
        
        roleInput = input.target.value;
    }

    const updateuser = async () => {
        console.log(roleInput);
        
        const resp = await axios.patch(baseUrl + `/users/${usr.userId}`, roleInput, {withCredentials: true, headers: {
            "Content-Type": "text/plain"
        }})
        .then((resp: AxiosResponse) => {
            localStorage.setItem("hasUpdated", "true");
            onHide();
        })
        .catch((error: AxiosError) => {
            console.log(error);
            
            setShowFailedMessage(true);
            setErrorMessage(error.message);
        })
    };

    return (
        <Modal show={show} onHide={onHide}>
            <Modal.Header closeButton>
                <Modal.Title className="text-center">
                    <h1 className="fs-2">User Account Update Form</h1>
                </Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <p className="fs-5"> You can modify the user role and save to update the account</p>
                <Form>
                    <Form.Group className="mb-3">
                        <FloatingLabel label="Firstname">
                            <Form.Control type="text" name="firstname" className="border border-2" value={usr.firstname} onChange={storeValues} />
                        </FloatingLabel>
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <FloatingLabel label="Lastname">
                            <Form.Control type="text" name="lastname" className="border border-2" value={usr.lastname} onChange={storeValues} />
                        </FloatingLabel>
                    </Form.Group>
                    <Form.Group>
                        <Form.FloatingLabel label="Role">
                            <Form.Select defaultValue={usr.role} onChange={getRole}>
                                <option value="Manager">Manager</option>
                                <option value="Employee">Employee</option>
                            </Form.Select>
                        </Form.FloatingLabel>
                    </Form.Group>
                </Form>
                {showFailedMessage && (
                    <Alert variant="danger">
                        <h3>Failed to update the account!</h3>
                        <p>{errorMessage}</p>
                    </Alert>
                )}
            </Modal.Body>
            <Modal.Footer>
                <Button type="button" variant="primary" className="mx-3" onClick={updateuser}>Save</Button>
                <Button type="button" variant="secondary" onClick={onHide}>Back</Button>
            </Modal.Footer>
        </Modal>
    )
}