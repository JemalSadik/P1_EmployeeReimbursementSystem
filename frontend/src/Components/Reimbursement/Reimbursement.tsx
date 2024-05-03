import { ReimbursementInterface } from "../../Interfaces/ReimbursementInterface"

export const Reimbursement: React.FC<{reimbursement: ReimbursementInterface, showModal: () => void}> = ({reimbursement, showModal}) => {

    console.log(showModal);

    return (
        <tr onClick={showModal}>
            <th>{reimbursement.reimbId}</th>
            <td>{reimbursement.description}</td>
            <td>{reimbursement.amount}</td>
            <td>{reimbursement.status}</td>
        </tr>
    )
}