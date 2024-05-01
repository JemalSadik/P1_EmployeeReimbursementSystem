export const ModalReimbursement: React.FC<any> = ({reimbursement, key, isOpen, onClose}) => {


    return (
        <div className="modal" id={`reimbursementModal${key}`} tabIndex={-1} aria-labelledby={`reimbursementModal${key}Label`} aria-hidden="true">
            <div className="modal-dialog">
                <div className="modal-content">
                    <div className="modal-header">
                        <h1 className="modal-title fs-5" id={`reimbursementModal${key}Label`}>Reimbursement</h1>
                        <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div className="modal-body">
                        // TODO: Add onChange event to update description
                        <div className="form-floating">
                            {reimbursement.status == "Pending" ? <textarea className="form-control" value={reimbursement.description} id={`reimbDescription${key}`}></textarea> : <textarea className="form-control" value={reimbursement.description} disabled></textarea>}
                            <label htmlFor={`reimbDescription${key}`}>Description</label>
                        </div>
                        <div className="form-floating">
                            <input type="text" id={`reimbAmount${key}`} value={`$${reimbursement.amount}`} disabled/>
                            <label htmlFor={`reimbAmount${key}`}>Amount</label>
                        </div>
                        <div className="form-floating">
                            // TODO: Update ternary to check if user role is manager
                            {reimbursement.status == "Pending" ? <select className="form-select" id={`reimbStatus${key}`}>
                                <option value="Pending" selected>Pending</option>
                                <option value="Approved">Approved</option>
                                <option value="Denied">Denied</option>
                            </select> : <select className="form-select" id={`reimbStatus${key}`}>
                                <option value={reimbursement.status} selected disabled>{reimbursement.status}</option>
                            </select>}
                            <label htmlFor={`reimbStatus${key}`}>Status</label>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}