import axios from "./axiosInstance";
import type { Page } from "../types/page";

export type VacancyId = string | number;

export type Vacancy = {
  id: VacancyId;
  title: string;
  description: string;
  location: string;
  companyName: string;
  employmentType: string;
  skills?: string[];
  // Ownership fields (adjust to your backend)
  createdById?: number;
  createdByEmail?: string;
  createdBy?: { id?: number; email?: string };
  createdAt?: string;
  updatedAt?: string;
};

// Create
export async function createVacancy(dto: {
  title: string;
  description: string;
  location: string;
  companyName: string;
  employmentType: "FULL_TIME" | "PART_TIME" | "CONTRACT";
  skills?: string[];
}) {
  const { data } = await axios.post<Vacancy>("/api/vacancies", dto);
  return data;
}

// Read one
export async function getVacancy(id: VacancyId) {
  const { data } = await axios.get<Vacancy>(`/api/vacancies/${id}`);
  return data;
}

// Update
export async function updateVacancy(id: VacancyId, dto: Partial<Vacancy>) {
  const { data } = await axios.put<Vacancy>(`/api/vacancies/${id}`, dto);
  return data;
}

// List current user's vacancies (owner list)
export async function getMyVacancies(page = 0, size = 20, sort = "createdAt,desc") {
  const { data } = await axios.get<Page<Vacancy> | Vacancy[]>("/api/vacancies/mine", {
    params: { page, size, sort },
  });


  if (Array.isArray(data)) {
    return {
      content: data,
      totalElements: data.length,
      totalPages: 1,
      number: 0,
      size: data.length,
      first: true,
      last: true,
      numberOfElements: data.length,
    } satisfies Page<Vacancy>;
  }
  return data;
}

// FE helper: check ownership on client (best-effort; server must enforce)
export function isOwner(v: Vacancy, user: any): boolean {
  if (user?.id && (v?.createdById || v?.createdBy?.id)) {
    const ownerId = v.createdById ?? v.createdBy?.id;
    return String(ownerId) === String(user.id);
  }
  if (user?.email && (v?.createdByEmail || v?.createdBy?.email)) {
    const ownerEmail = v.createdByEmail ?? v.createdBy?.email;
    return String(ownerEmail).toLowerCase() === String(user.email).toLowerCase();
  }
  return false;
}
