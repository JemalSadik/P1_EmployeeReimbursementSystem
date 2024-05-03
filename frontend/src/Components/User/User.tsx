import { Button } from "react-bootstrap"
import { UserInterface } from "../../Interfaces/UserInterface"
import axios, { AxiosError, AxiosResponse } from "axios";

export const User: React.FC<{user: UserInterface, showModal: () => void}> = ({user, showModal}) => {

    const baseUrl: string|null = localStorage.getItem("baseUrl");

    const deleteUser = () => {
        const resp = axios.delete(baseUrl + `/users/${user.userId}`, {withCredentials: true})
        .then((res: AxiosResponse) => alert("Successfully deleted user!"))
        .catch((error: AxiosError) => alert("Failed to delete user"))
    }

    console.log("user", showModal);
    

    return (
        <tr onClick={showModal}>
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