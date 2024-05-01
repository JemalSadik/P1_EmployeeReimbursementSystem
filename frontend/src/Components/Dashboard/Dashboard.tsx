import { useEffect, useState } from "react"
import { Table } from "react-bootstrap"
import { Reimbursement } from "../Reimbursement/Reimbursement";
import { ReimbursementInterface } from "../../Interfaces/ReimbursementInterface";
import axios from "axios";
import { UserInterface } from "../../Interfaces/UserInterface";

export const Dashboard: React.FC = () => {

    const user: UserInterface = JSON.parse(localStorage.getItem("userId")||"");
    const baseUrl: string|null = localStorage.getItem("baseUrl");

    const [reimbursements, setReimbursements] = useState<Array<ReimbursementInterface>>([]);
    const [showModal, setShowModal] = useState<boolean>(false);
    const [selectedReimbursement, setSelectedReimbursement] = useState<ReimbursementInterface|null>(null);

    const getAllReimbursementsByUserId = async () => {
        const resp = await axios.get(baseUrl + `/reimbursements/users/${user.userId||1}`||"");
        setReimbursements(resp.data);
    }

    const getAllPendingReimbursementsByUserId = async () => {
        const resp = await axios.get(baseUrl + `/reimbursements/users/${user.userId||1}/pending`||"");
        setReimbursements(resp.data);
    }

    useEffect(() => {
        // TODO: get all reimbursements

    }, []);

    const handleShowModal = (reimbursement: ReimbursementInterface) => {
        setSelectedReimbursement(reimbursement);
        setShowModal(true);
    };

    const handleCloseModal = () => {
        setShowModal(false);
    }

    return (
        <div>
            <Table>
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Description</th>
                        <th>Amount</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    {reimbursements.map((reimbursement) => 
                        <Reimbursement key={reimbursement.reimbId} {...reimbursement} showModal={handleShowModal} onHide={handleCloseModal} />
                    )}
                </tbody>
            </Table>
            {}
        </div>
    )
}