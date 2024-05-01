import { ReimbursementInterface } from "./ReimbursementInterface";

export interface ReimbursementProps {
    reimbursement: ReimbursementInterface;
    show: boolean;
    onHide: CallableFunction;
}