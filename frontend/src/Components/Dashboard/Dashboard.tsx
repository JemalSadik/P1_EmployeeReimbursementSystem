import { useEffect, useState } from "react"
import { Button, Col, Container, Form, Row, Table } from "react-bootstrap"
import { Reimbursement } from "../Reimbursement/Reimbursement";
import { ReimbursementInterface } from "../../Interfaces/ReimbursementInterface";
import axios from "axios";
import { UserInterface } from "../../Interfaces/UserInterface";
import { ModalCreateReimbursement } from "../Reimbursement/ModalCreateReimbursement";
import { ModalReimbursement } from "../Reimbursement/ModalReimbursement";
import {User} from "../User/User";

export const Dashboard: React.FC = () => {

    const user: UserInterface = JSON.parse(localStorage.getItem("user")||"");
    const baseUrl: string|null = localStorage.getItem("baseUrl");

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
    const [selectedUser, setSelectedUser] = useState<UserInterface|null>(null);

    const getAllReimbursementsByUserId = async () => {
        const resp = await axios.get(baseUrl + `/reimbursements/user`, {withCredentials: true});
        setReimbursements(resp.data);
    };
    const getAllPendingReimbursementsByUserId = async () => {
        const resp = await axios.get(baseUrl + `/reimbursements/user/pending`, {withCredentials: true});
        setReimbursements(resp.data);
    };

    // The following functions are used for User Account user stories
    const getAllUsers = async () => {
        const resp = await axios.get(baseUrl + `/users`, {withCredentials: true});
        setUsers(resp.data);
    }

    const getUserByUserId = async () => {
        const resp = await axios.get(baseUrl + `/users/userid`, {withCredentials: true});
        setUsers(resp.data);
    }

    useEffect(() => {
        // TODO: get all reimbursements
        getAllReimbursementsByUserId();
    }, []);

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
    }

    return (
        <Container className="d-flex flex-column justify-content-center m-5 px-5">
            <Container className="w-75">
                <Form id="table-controls">
                    <Row>
                        <Col md="6">
                            <Form.Group>
                                <Form.Label>Select View</Form.Label>
                                <Form.Select className="border border-2">
                                    <option value="myReimbursements">My Reimbursements</option>
                                    <option value="myPending">My Pending Reimbursements</option>
                                    {user.role.toLowerCase() === "manager" && (
                                        <>
                                            <option value="allReimbursements">All Reimbursements</option>
                                            <option value="allPending">All Pending Reimbursements</option>
                                        </>
                                    )}
                                </Form.Select>
                            </Form.Group>
                        </Col>
                        <Col md="3">
                            <Form.Group>
                                <Form.Label>Select Type</Form.Label>
                                <div>
                                    <Form.Check inline label="Reimbursements" name="selectTypeGroup" type="radio" id="selectReimbursement" checked/>
                                    {user.role.toLowerCase() === "manager" ? <Form.Check inline label="Users" name="selectTypeGroup" type="radio" id="selectUsers" /> : <Form.Check inline label="Users" name="selectTypeGroup" type="radio" id="selectUsers" disabled/>}
                                </div>
                            </Form.Group>
                        </Col>
                        <Col md="3" className="d-flex align-items-center">
                            <Button type="button" variant="primary" onClick={handleShowCreateReimbursementModal}>
                                <i className="bi bi-plus-lg me-2"></i>
                                Create
                            </Button>
                        </Col>
                    </Row>
                </Form>
                <Table id="reimbursement-table" className="mt-3" striped hover>
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
                </Table>
                <Table id="user-table">
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Fistname</th>
                            <th>Lastname</th>
                            <th>Role</th>
                        </tr>
                    </thead>
                    <tbody>
                        {users.map((user) => 
                            <User key={user.userId} {...user} showModal={handleUserShowModal} onHide={handleUserCloseModal} />
                        )}
                    </tbody>
                </Table> 
                <ModalCreateReimbursement show={showCreateReimbursementModal} onHide={handleCloseCreateReimbursementModal}/>
                <ModalReimbursement reimbursement={selectedReimbursement} show={showReimbursementModal} onHide={handleCloseReimbursementModal}/>
            </Container>
        </Container>
    )
}