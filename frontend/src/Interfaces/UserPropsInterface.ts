import { UserInterface } from "./UserInterface";

export interface UserProps {
    user: UserInterface;
    show: boolean;
    onHide: CallableFunction;
}