import { useEffect, useState } from "react"
import { Alert, Button, Col, Container, Form, Row, Table } from "react-bootstrap"
import { Reimbursement } from "../Reimbursement/Reimbursement";
import { ReimbursementInterface } from "../../Interfaces/ReimbursementInterface";
import axios, { AxiosError, AxiosResponse } from "axios";
import { UserInterface } from "../../Interfaces/UserInterface";
import { ModalCreateReimbursement } from "../Reimbursement/ModalCreateReimbursement";
import { ModalReimbursement } from "../Reimbursement/ModalReimbursement";
import {User} from "../User/User";
import { UserModal } from "../User/UserModal";
import { useNavigate } from "react-router-dom";

export const Dashboard: React.FC = () => {

    const user: UserInterface = JSON.parse(localStorage.getItem("user")||"");
    const baseUrl: string|null = localStorage.getItem("baseUrl");

    const [type, setType] = useState("Reimbursements");
    const [reimbursements, setReimbursements] = useState<Array<ReimbursementInterface>>([]);
    const [showReimbursementModal, setShowReimbursementModal] = useState<boolean>(false);
    const [selectedReimbursement, setSelectedReimbursement] = useState<ReimbursementInterface>({
        amount: 0,
        description: ""
    });
    const [showCreateReimbursementModal, setShowCreateReimbursementModal] = useState<boolean>(false);

    // The following state variables are used for User Account user stories
    const [users, setUsers] = useState<Array<UserInterface>>([]);
    const [showUserModal, setUserShowModal] = useState<boolean>(false);
    const [selectedUser, setSelectedUser] = useState<UserInterface>({
        role: "",
        userId: 0,
        username: ""
    });

    const [showSuccessAlert, setShowSuccessAlert] = useState(false);
    const [alertSuccessMessage, setAlertSuccessMessage] = useState("");
    const [showErrorAlert, setShowErrorAlert] = useState(false);
    const [alertErrorMessage, setAlertErrorMessage] = useState("");

    const [refreshReimbursements, setRefreshReimbursements] = useState(false);
    const [currentView, setCurrentView] = useState("myReimbursements");
    const [refreshUsers, setRefreshUsers] = useState(false);

    const getType = (input: React.ChangeEvent<HTMLInputElement>) => {
        setType(input.target.value);
    };

    const getAllReimbursementsByUserId = async () => {
        const resp = await axios.get(baseUrl + `/reimbursements/user`, {withCredentials: true});
        setReimbursements(resp.data);
    };
    const getAllPendingReimbursementsByUserId = async () => {
        const resp = await axios.get(baseUrl + `/reimbursements/user/pending`, {withCredentials: true});
        setReimbursements(resp.data);
    };
    const getAllReimbursements = async () => {
        const resp = await axios.get(baseUrl + "/reimbursements", {withCredentials: true});
        setReimbursements(resp.data);
    };
    const getAllPendingReimbursements = async () => {
        const resp = await axios.get(baseUrl + "/reimbursements/pending", {withCredentials: true})
        setReimbursements(resp.data);
    };

    // The following functions are used for User Account user stories
    const getAllUsers = async () => {
        const resp = await axios.get(baseUrl + `/users`, {withCredentials: true});
        setUsers(resp.data);
    }

    const deleteUser = async (userId: number) => {
        const resp = await axios.delete(baseUrl + `/users/${userId}`, {withCredentials: true})
        .then((res: AxiosResponse) => {
            setShowSuccessAlert(true);
            setAlertSuccessMessage("Successfully deleted user!");
            getAllUsers();
        })
        .catch((error: AxiosError) => {
            setShowErrorAlert(true);
            setAlertErrorMessage(`${error.response?.data}`);
        });
    }

    useEffect(() => {
        // TODO: get all reimbursements
        getAllReimbursementsByUserId();
        if (user.role.toLowerCase() === "manager") {
            getAllUsers();
        }
    }, []);

    const handleViewOnChange = (input: React.ChangeEvent<HTMLSelectElement>) => {
        const view: string = input.target.value;
        switch (view) {
            case "myReimbursements":
                getAllReimbursementsByUserId();
                setCurrentView("myReimbursements");
                return;
            case "myPending":
                getAllPendingReimbursementsByUserId();
                setCurrentView("myPending");
                return;
            case "allReimbursements":
                if (user.role.toLowerCase() !== "manager") {
                    // TODO: change to alert component
                    alert("You must be a MANAGER to view all reimbursements");
                    return;
                }
                getAllReimbursements();
                setCurrentView("allReimbursements");
                return;
            case "allPending":
                if (user.role.toLowerCase() !== "manager") {
                    // TODO: change to alert component
                    alert("You must be a MANAGER to view all pending reimbursements");
                    return;
                }
                getAllPendingReimbursements();
                setCurrentView("allPending");
                return;
            default:
                // TODO: change to alert component
                alert("Invalid view type!")
                return;
        }
    }
    const handleShowReimbursementModal = (reimbursement: ReimbursementInterface) => {
        setSelectedReimbursement(reimbursement);
        setShowReimbursementModal(true);
    };
    const handleCloseReimbursementModal = () => {
        setShowReimbursementModal(false);
    };

    const handleShowCreateReimbursementModal = () => {
        setShowCreateReimbursementModal(true);
    };
    const handleCloseCreateReimbursementModal = () => {
        setShowCreateReimbursementModal(false);
    };

    // For Uses
    const handleUserShowModal = (user: UserInterface) => {
        setSelectedUser(user);
        setUserShowModal(true);
    };

    const handleUserCloseModal = () => {
        setUserShowModal(false);
    };

    const navigate = useNavigate();

    const logout = async () => {        
        const resp = await axios.post(baseUrl + "/logout", {}, {withCredentials: true})
        .then((resp: AxiosResponse) => {            
            navigate("/");
        })
        .catch((error: AxiosError) => {
            setAlertErrorMessage(`${error.response?.data}`)
        })        
    };

    if (refreshReimbursements) {
        // then refresh view based on current view
        switch (currentView) {
            case "myReimbursements":
                getAllReimbursementsByUserId();
                break;
            case "myPending":
                getAllPendingReimbursementsByUserId();
                break;
            case "allReimbursements":
                getAllReimbursements();
                break;
            case "allPending":
                getAllPendingReimbursements();
                break;
            default:
                break;
        }
        setRefreshReimbursements(false);
    }

    if (refreshUsers) {
        getAllUsers();
        setRefreshUsers(false);
    }

    return (
        <Container className="d-flex flex-column justify-content-center m-5 px-5">
            <Container className="w-75">
                <Form id="table-controls">
                    <Row>
                        <Col md="6">
                            <Form.Group>
                                <Form.Label>Select View</Form.Label>
                                {type === "Reimbursements" ? <Form.Select id="selctReimbursementsView" className="border border-2" onChange={handleViewOnChange}>
                                    <option value="myReimbursements">My Reimbursements</option>
                                    <option value="myPending">My Pending Reimbursements</option>
                                    {user.role.toLowerCase() === "manager" && (
                                        <>
                                            <option value="allReimbursements">All Reimbursements</option>
                                            <option value="allPending">All Pending Reimbursements</option>
                                        </>
                                    )}
                                </Form.Select> : <Form.Select id="selectUsersView" className="border border-2" disabled>
                                    <option value="allUsers">All Users</option>
                                </Form.Select>}                                
                            </Form.Group>
                        </Col>
                        <Col md="3">
                            <Form.Group>
                                <Form.Label>Select Type</Form.Label>
                                <div>
                                    {type === "Reimbursements" ? <Form.Check inline label="Reimbursements" name="selectTypeGroup" type="radio" id="selectReimbursement" value="Reimbursements" onChange={getType} checked /> : <Form.Check inline label="Reimbursements" name="selectTypeGroup" type="radio" id="selectReimbursement" value="Reimbursements" onChange={getType} />}
                                    {user.role.toLowerCase() === "manager" ? <Form.Check inline label="Users" name="selectTypeGroup" type="radio" id="selectUsers" value="Users" onChange={getType} /> : <Form.Check inline label="Users" name="selectTypeGroup" type="radio" id="selectUsers" disabled/>}
                                </div>
                            </Form.Group>
                        </Col>
                        <Col md="3" className="d-flex align-items-center">
                            {type === "Reimbursements" ? <Button type="button" variant="primary" onClick={handleShowCreateReimbursementModal}>
                                <i className="bi bi-plus-lg me-2"></i>
                                Create
                            </Button> : <Button type="button" variant="secondary" onClick={handleShowCreateReimbursementModal} disabled>
                                <i className="bi bi-plus-lg me-2"></i>
                                Create
                            </Button>}
                        </Col>
                    </Row>
                </Form>
                <Button type="button" variant="info" onClick={logout} className="float-end">Logout</Button>
                {showSuccessAlert && (
                    <Alert variant="success" className="my-3" onClose={() => setShowSuccessAlert(false)} dismissible>
                        <Alert.Heading> Success!</Alert.Heading>
                        <p>{alertSuccessMessage}</p>
                    </Alert>
                )}
                {showErrorAlert && (
                    <Alert variant="danger" className="my-3" onClose={() => setShowErrorAlert(false)} dismissible>
                        <Alert.Heading>Failed!</Alert.Heading>
                        <p>{alertErrorMessage}</p>
                    </Alert>
                )}
                {type === "Reimbursements" ? <Table id="reimbursement-table" className="mt-3" striped hover>
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Description</th>
                            <th>Amount</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody className="table-group-divider">
                        {reimbursements.map((reimbursement) => 
                            <Reimbursement key={reimbursement.reimbId} reimbursement={reimbursement} showModal={() => handleShowReimbursementModal(reimbursement)} />
                        )}
                    </tbody>
                </Table> : <Table id="user-table" className="mt-3" striped hover>
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Fistname</th>
                            <th>Lastname</th>
                            <th>Role</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody className="table-group-divider">
                        {users.map((user) => 
                            <User key={user.userId} user={user} showModal={() => handleUserShowModal(user)} deleteUser={() => deleteUser(user.userId)} />
                        )}
                    </tbody>
                </Table>}
                <ModalCreateReimbursement show={showCreateReimbursementModal} onHide={handleCloseCreateReimbursementModal} setShowAlertSuccess={() => setShowSuccessAlert(true)} setAlertSuccessMessage={() => setAlertSuccessMessage("Created new reimbursement")} refreshReimbursements={() => setRefreshReimbursements(true)}/>
                <ModalReimbursement reimbursement={selectedReimbursement} show={showReimbursementModal} onHide={handleCloseReimbursementModal} setShowAlertSuccess={() => setShowSuccessAlert(true)} setAlertSuccessMessage={() => setAlertSuccessMessage("Updated reimbursement")} refreshReimbursements={() => setRefreshReimbursements(true)}/>
                <UserModal usr={selectedUser} show={showUserModal} onHide={handleUserCloseModal} setShowAlertSuccess={() => setShowSuccessAlert(true)} setAlertSuccessMessage={() => setAlertSuccessMessage("Updated user")} setRefreshUsers={() => setRefreshUsers(true)}/>
            </Container>
        </Container>
    )
}