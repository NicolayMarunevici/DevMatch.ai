import { useEffect } from "react";
import { useAuthStore } from "../store/useAuthStore";
import { getCurrentUser } from "../../../api/auth";
import axios from "axios";

export function useAuthInit() {
  const login = useAuthStore((state) => state.login);
  const logout = useAuthStore((state) => state.logout);
  const setAuthLoading = useAuthStore((state) => state.setAuthLogin);

  useEffect(() => {
    const initAuth = async () => {
      setAuthLoading(true);
      try {
        const user = await getCurrentUser();
        login(user);
      } catch (err) {
        if (axios.isAxiosError(err) && err.response?.status === 403) {
          logout();
        } else {
          console.error("Auth check failed", err);
        }
      } finally {
        setAuthLoading(false);
      }
    };

    initAuth();
  }, [login, logout, setAuthLoading]);
}
