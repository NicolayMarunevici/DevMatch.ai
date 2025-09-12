import { Navigate } from "react-router-dom";
import { useAuthStore } from "../features/auth/store/useAuthStore.ts"
import {ReactNode} from "react";

interface Props {
    children: ReactNode;
}

export default function RequireAuth({ children }: Props){
    const user = useAuthStore((state) => state.user);

    if(!user){
        return<Navigate to="/login" replace/>;
    }

    return children;
}