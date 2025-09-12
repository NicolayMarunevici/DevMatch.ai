import {create} from "zustand"
import type { User } from "../../../types/User"
import { persist } from "zustand/middleware";


interface AuthState {
  user: User | null;
  isAuthLoading: boolean;
  login: (user: User) => void;
  logout: () => void;
  setAuthLogin: (loading: boolean) => void;
}

export const useAuthStore = create<AuthState>((set) => ({
  user: null,
  isAuthLoading:true,
  login: (user) => set({ user }),
  logout: () => set({ user: null }),
  setAuthLogin: (loading) => set({ isAuthLoading : loading})
}));
