import type {UpdateProfileRequest} from "./auth.ts";
import type {User} from "../types/User.ts";
import axios from "axios";
import axiosInstance from "./axiosInstance.ts";


interface UpdateProfileRequest {
    firstName: string;
    lastName: string;
    email: string;
}


export async function updateMyProfile(dto: UpdateProfileRequest) {
  const { data } = await axiosInstance.put("/api/users/me", dto);
  return data;
}