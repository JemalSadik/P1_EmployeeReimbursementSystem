import axios from "axios";
import { useState } from "react";
import { Button, Container, FloatingLabel, Form, InputGroup } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

export const Login: React.FC = () => {

    const baseUrl = localStorage.getItem("baseUrl");

    const navigate = useNavigate();

    const [user, setUser] = useState({
        username: "",
        password: ""
    });
    const [passwordIsVisible, setPasswordIsVisible] = useState(false);

    const storeValues = (input: React.ChangeEvent<HTMLInputElement>) => {
        if (input.target.name === "username") {
            setUser((user) => ({...user, username:input.target.value}));
        } else {
            setUser((user) => ({...user, password:input.target.value}));
        }
    };

    const togglePasswordVisibility: React.MouseEventHandler<HTMLButtonElement> = () => {
        setPasswordIsVisible(!passwordIsVisible);
    }

    const login = async () => {
        // TODO: validate username and password
        const resp = await axios.post(baseUrl + "/users/login", user, {withCredentials: true})
        .then((resp) => {
            localStorage.setItem("user", JSON.stringify(resp.data));
            navigate("/dashboard");
        })
        .catch((error) => alert(error.data));
    };

    return (
        <Container className="d-flex flex-column justify-content-center m-5 px-5">
            <Container className="w-75">
                <header className="text-center">
                    <h1 className="fs-2">Employee Reimbursement System</h1>
                    <p className="fs-5">Sign in to view/manage your reimbursements</p>
                </header>
                <Form>
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
                    <div>
                        <Button type="button" variant="primary" className="mx-3" onClick={login}>Login</Button>
                        <Button type="button" variant="secondary" onClick={() => navigate("/register")}>Register</Button>
                    </div>
                </Form>
            </Container>
        </Container>
    )
}