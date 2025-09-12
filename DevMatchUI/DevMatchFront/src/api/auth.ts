import axios from "axios";
import type { User } from "../types/User";
import axiosInstance from "./axiosInstance.ts";

export interface LoginRequest {
    email: string;
    password: string;
}

export interface RegisterRequest {
    email: string;
    password: string;
    firstName: string;
    lastName: string;
    roles?: string[];
}

export interface AuthResponse {
    user: {
        id: number;
        email: string;
        firstName: string;
        lastName: string;
        roles: string[]
    };
    accessToken: string;
}

export interface UpdateProfileRequest {
    fullName?: string;
    email?: string;
}

export async function login(data: LoginRequest): Promise<AuthResponse> {
    const response = await axiosInstance.post(`/auth/login`, data)
    return response.data;
}

export async function register(data: RegisterRequest): Promise<AuthResponse> {
    const response = await axiosInstance.post("/auth/register", data);
    return response.data;
}

export async function updateProfile(data: UpdateProfileRequest): Promise<User> {
    const response = await axiosInstance.put("/auth/profile", data);
    return response.data;
}

export async function getCurrentUser(): Promise<User> {
    const response = await axiosInstance("/api/users/me");
    return response.data;
}
