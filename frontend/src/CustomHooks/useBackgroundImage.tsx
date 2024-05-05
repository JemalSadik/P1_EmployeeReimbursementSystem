import { useEffect } from "react";
import { useLocation } from "react-router-dom"

export const useBackgroundImage = () => {
    const location = useLocation();

    const style = (image: string) => {
        document.body.style.backgroundImage = image;
        document.body.style.backgroundSize = "cover";
        document.body.style.backgroundRepeat = "no-repeat";
    }

    useEffect(() => {
        switch (location.pathname) {
            case "/":
                style("url('/images/login-background1.png')");
                break;
            case "/register":
                style("url('/images/register-background1.png')");
                break;
            case "/dashboard":
                style("url('/images/dashboard-background1.png')");
                break;
            default:
                document.body.style.backgroundImage = "none";
                break;
        };
    }, [location.pathname]);
}