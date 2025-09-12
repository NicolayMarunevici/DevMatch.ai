import {useMutation} from "@tanstack/react-query";
import {getCurrentUser, login as loginRequest, type LoginRequest} from "../../../api/auth";
import {useAuthStore} from "../store/useAuthStore";
import {useNavigate} from "react-router-dom";


export function useLogin(){
    const loginStore = useAuthStore((state) => state.login);
    const navigate = useNavigate();

    return useMutation({
        mutationFn: async (data: LoginRequest) => {
            await loginRequest(data);
    
            const user = await getCurrentUser();
            
            loginStore(user);
            
            return user;
        },
            onSuccess: () => {
            navigate("/dashboard")
        },
        onError: (error) => {
            console.error("Login error:", error);
        }
    });
}