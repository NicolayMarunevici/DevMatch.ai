import { useQuery } from "@tanstack/react-query";
import { getMyVacancies, type Vacancy } from "../../../api/vacancy";
import type { Page } from "../../../types/page";

// Fetch vacancies created by current user (paged)
export function useMyVacancies(page = 0, size = 20, sort = "createdAt,desc") {
  return useQuery<Page<Vacancy>>({
    queryKey: ["vacancies", "mine", page, size, sort],
    queryFn: () => getMyVacancies(page, size, sort),
  });
}
