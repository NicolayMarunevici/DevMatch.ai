import { useMutation } from "@tanstack/react-query";
import { updateMyProfile, type UpdateProfileRequest } from "@/api/user";
import { useAuthStore } from "@/features/auth/store/useAuthStore";

export function useUpdateProfile() {
  const setUser = useAuthStore((s) => s.login); 
  
  return useMutation({
    mutationFn: (dto: UpdateProfileRequest) => updateMyProfile(dto),
    onSuccess: (updatedUser) => {
      if (setUser) setUser(updatedUser);
    },
    onError: (err) => {
      console.error("Update profile failed", err);
    },
  });
}
