import axios, { AxiosError, AxiosResponse } from "axios";
import { useState } from "react";
import { Alert, Button, Container, FloatingLabel, Form, InputGroup } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

export const Register: React.FC = () => {

    const baseUrl = localStorage.getItem("baseUrl");

    const navigate = useNavigate();

    const [user, setUser] = useState({
        username: "",
        password: "",
        firstname: "",
        lastname: ""
    });
    const [passwordIsVisible, setPasswordIsVisible] = useState(false);
    const [showFailedMessage, setShowFailedMessage] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");

    const storeValues = (input: React.ChangeEvent<HTMLInputElement>) => {
        if (input.target.name === "username") {
            setUser((user) => ({...user, username: input.target.value}));
        } else if (input.target.name === "password") {
            setUser((user) => ({...user, password: input.target.value}));
        } else if (input.target.name === "firstname") {
            setUser((user) => ({...user, firstname: input.target.value}));
        } else {
            setUser((user) => ({...user, lastname: input.target.value}));
        }
    };

    const togglePasswordVisibility: React.MouseEventHandler<HTMLButtonElement> = () => {
        setPasswordIsVisible(!passwordIsVisible);
    };

    const register = async () => {
        const resp = await axios.post(baseUrl + "/users", user, {withCredentials: true})
        .then((resp: AxiosResponse) => {
            localStorage.setItem("hasRegistered", "true");
            navigate("/");
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
                    <h1 className="fs-2">Employee Registration Form</h1>
                    <p className="fs-5">Fill out the form below to register for an account</p>
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
                    <Form.Group className="mb-3">
                        <FloatingLabel label="Username">
                            <Form.Control type="text" name="username" className="border border-2" onChange={storeValues} />
                        </FloatingLabel>
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <InputGroup>
                            <FloatingLabel label="Password">
                                <Form.Control type={passwordIsVisible ? "text": "password"} name="password" className="border border-2" onChange={storeValues} aria-describedby="passwordVisibility" />
                            </FloatingLabel>
                            <Button onClick={togglePasswordVisibility} id="passwordVisibility">
                                {passwordIsVisible ? <i className="bi bi-eye fs-3"></i> : <i className="bi bi-eye-slash fs-3"></i>}
                            </Button>
                        </InputGroup>
                    </Form.Group>
                </Form>
                <div>
                    <Button type="button" variant="primary" className="mx-3" onClick={register}>Register</Button>
                    <Button type="button" variant="secondary" onClick={() => navigate("/")}>Back</Button>
                </div>
                {showFailedMessage && (
                    <Alert variant="danger" className="mt-3" onClose={() => setShowFailedMessage(false)} dismissible>
                        <Alert.Heading>Failed to register account!</Alert.Heading>
                        <p>{errorMessage}</p>
                    </Alert>
                )}
            </Container>
        </Container>
    )
}