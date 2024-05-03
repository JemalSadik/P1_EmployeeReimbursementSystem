import axios, { AxiosError, AxiosResponse } from "axios";
import { useState } from "react";
import { Alert, Button, Container, FloatingLabel, Form, InputGroup } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

export const Register: React.FC = () => {

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

    const updateuser = async () => {
        const resp = await axios.post(baseUrl + "/userid", user, {withCredentials: true})
        .then((resp: AxiosResponse) => {
            localStorage.setItem("hasUpdated", "true");
            navigate("/dashboard");
        })
        .catch((error: AxiosError) => {
            setShowFailedMessage(true);
            setErrorMessage(error.message);
        })
    };

    return (
        <Container className="d-flex flex-column justify-content-center m-5 px-5">
            <Container className="w-75">
                <header className="text-center">
                    <h1 className="fs-2">User Account Update Form</h1>
                    <p className="fs-5"> You can modify the user role and save to update the account</p>
                </header>
                <Form>
                    <Form.Group className="mb-3">
                        <FloatingLabel label="Firstname">
                            <Form.Control type="text" name="firstname" className="border border-2" onChange={storeValues} />
                        </FloatingLabel>
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <FloatingLabel label="Lastname">
                            <Form.Control type="text" name="lastname" className="border border-2" onChange={storeValues} />
                        </FloatingLabel>
                    </Form.Group>
                  </Form>
                <div>
                    <Button type="button" variant="primary" className="mx-3" onClick={updateuser}>Save</Button>
                    <Button type="button" variant="secondary" onClick={() => navigate("/dashboard")}>Back</Button>
                </div>
                {showFailedMessage && (
                    <Alert variant="danger">
                        <h3>Failed to update the account!</h3>
                        <p>{errorMessage}</p>
                    </Alert>
                )}
            </Container>
        </Container>
    )
}
