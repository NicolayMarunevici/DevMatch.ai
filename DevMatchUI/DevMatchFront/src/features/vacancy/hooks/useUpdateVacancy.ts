import { useMutation, useQueryClient } from "@tanstack/react-query";
import { updateVacancy, type Vacancy, type VacancyId } from "../../../api/vacancy";

// Update a vacancy and refresh caches
export function useUpdateVacancy(id: VacancyId) {
  const qc = useQueryClient();
  return useMutation<Vacancy, any, Partial<Vacancy>>({
    mutationFn: (dto) => updateVacancy(id, dto),
    onSuccess: (updated) => {
      qc.setQueryData(["vacancy", id], updated);
      qc.invalidateQueries({ queryKey: ["vacancies", "mine"] });
    },
  });
}
