import { ReimbursementInterface } from "../../Interfaces/ReimbursementInterface"

export const Reimbursement: React.FC<any> = (reimbursement: ReimbursementInterface, showModal: CallableFunction) => {


    return (
        <tr onClick={showModal(reimbursement)}>
            <th>{reimbursement.reimbId}</th>
            <td>{reimbursement.description}</td>
            <td>{reimbursement.amount}</td>
            <td>{reimbursement.status}</td>
        </tr>
    )
}