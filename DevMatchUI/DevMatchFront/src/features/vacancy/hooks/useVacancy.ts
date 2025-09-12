import { useQuery } from "@tanstack/react-query";
import { getVacancy, type VacancyId, type Vacancy } from "@/api/vacancy";

// NOTE: fetch one vacancy by id
export function useVacancy(id: VacancyId) {
  return useQuery<Vacancy>({
    queryKey: ["vacancy", id],
    queryFn: () => getVacancy(id),
    enabled: Boolean(id),
  });
}
