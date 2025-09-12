import { useMutation } from "@tanstack/react-query";
import { createVacancy, type CreateVacancyRequest, type Vacancy } from "@/api/vacancy";
import { useNavigate } from "react-router-dom";

export function useCreateVacancy() {
  const navigate = useNavigate();

  return useMutation<Vacancy, any, CreateVacancyRequest>({
    mutationFn: (dto) => createVacancy(dto),
    onSuccess: () => {
      // NOTE: navigate wherever you want after create
      navigate("/dashboard");
    },
    onError: (err) => {
      // NOTE: do not redirect on error; axios interceptor handles 401 only
      console.error("Create vacancy failed", err?.response?.status, err?.response?.data);
    },
  });
}
