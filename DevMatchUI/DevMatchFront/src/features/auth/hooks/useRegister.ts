// src/features/auth/hooks/useRegister.ts
import { useNavigate } from "react-router-dom";
import { useMutation } from "@tanstack/react-query";
import { useAuthStore } from "../store/useAuthStore";
import { register as registerRequest, getCurrentUser } from "@/api/auth";
import type { RegisterRequest, CurrentUser } from "@/api/auth";
import type { AxiosError } from "axios";

export function useRegister() {
  const loginStore = useAuthStore((s) => s.login);
  const navigate = useNavigate();

  return useMutation<CurrentUser, AxiosError, RegisterRequest>({
    // NOTE: register then fetch fresh user so roles are up to date
    mutationFn: async (payload) => {
      // IMPORTANT: backend expects roles like ["ROLE_RECRUITER"] or ["ROLE_CANDIDATE"]
      if (!Array.isArray(payload.roles) || payload.roles.length === 0) {
        throw new Error("roles[] is required in RegisterRequest");
      }
      await registerRequest(payload);
      const user = await getCurrentUser();
      loginStore(user);
      return user;
    },
    onSuccess: () => {
      // NOTE: navigate after store is updated
      navigate("/dashboard");
    },
    onError: (err) => {
      // NOTE: do not redirect here; axios interceptor will handle 401 only
      // You can surface a toast/snackbar here
      console.error("Register error:", err.response?.status, err.response?.data ?? err.message);
    },
  });
}
