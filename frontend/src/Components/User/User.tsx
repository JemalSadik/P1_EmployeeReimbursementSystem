import { UserInterface } from "../../Interfaces/UserInterface"

export const User: React.FC<any> = (user: UserInterface, showModal: CallableFunction) => {


    return (
        <tr onClick={showModal(user)}>
            <th>{user.userId}</th>
            <td>{user.firstname}</td>
            <td>{user.lastname}</td>
            <td>{user.role}</td>
        </tr>
  
    )
}