import {useQuery} from "@tanstack/react-query";
import type {User} from "../../../types/User.ts";
import {getCurrentUser} from "../../../api/auth.ts";


export function useCurrentUser(){
    return useQuery<User>({
        queryKey: ["currentUser"],
        queryFn: getCurrentUser,
        retry: false,
        staleTime: 5 * 60 * 1000
    })
}