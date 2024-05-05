import { Button } from "react-bootstrap"
import { UserInterface } from "../../Interfaces/UserInterface"
import axios, { AxiosError, AxiosResponse } from "axios";

export const User: React.FC<{user: UserInterface, showModal: () => void, deleteUser: () => void}> = ({user, showModal, deleteUser}) => {

    const baseUrl: string|null = localStorage.getItem("baseUrl");

    console.log("user", showModal);
    

    return (
        <tr onDoubleClick={showModal}>
            <th>{user.userId}</th>
            <td>{user.firstname}</td>
            <td>{user.lastname}</td>
            <td>{user.role}</td>
            <td>
                <Button variant="info" onClick={deleteUser}>Delete</Button>
            </td>
        </tr>
    )
}