import { QueryClient, useMutation, useQuery } from "@tanstack/react-query";
import {useAuthStore} from "../features/auth/store/useAuthStore.ts";
import {useNavigate} from "react-router-dom";
import axiosInstance from "../api/axiosInstance.ts";


interface Vacancy {
    id: number;
    title: string;
    description: string;
    companyName: string;
    location: string;
    employmentType: string;
}


export default function Dashboard() {
    const user = useAuthStore((state) => state.user);
    const logout = useAuthStore((state) => state.logout);
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate("/login");
    };


// Load the list of Vacancy (paged, only OPEN)
const { data: vacancies, isLoading, error, refetch } = useQuery<Vacancy[]>({
  queryKey: ["vacancies", 0, 20, "createdAt,desc", "OPEN"],
  queryFn: async () => {
    try {
      const res = await axiosInstance.get("/api/vacancies", {
        params: { page: 0, size: 20, sort: "createdAt,desc", status: "OPEN" },
      });
      const data = res.data;
      // поддерживаем оба формата бэка: массив или Spring Page
      return Array.isArray(data) ? data : (data?.content ?? []);
    } catch (e: any) {
      // покажем полезное сообщение с бэка, чтобы не гадать
      const msg =
        e?.response?.data?.message ??
        e?.response?.data?.error ??
        e?.response?.data ??
        e?.message;
      console.error("GET /api/vacancies failed:", msg);
      throw e;
    }
  },
});


  const applyMutation = useMutation({
    mutationFn: (vacancyId: number) =>
      axiosInstance.post(`/api/applications/${vacancyId}`),
    onSuccess: (_data, vacancyId) => {
      // Локально обновляем кэш, чтобы кнопка сразу менялась
      queryClient.setQueryData<Vacancy[]>(["vacancies"], (old) => {
        if (!old) return old;
        return old.map((v) =>
          v.id === vacancyId ? { ...v, applied: true } : v
        );
      });
    },
  });

      if (isLoading) return <p className="p-4">Loading vacancies...</p>;
  if (error) return <p className="p-4 text-red-500">Error loading vacancies</p>;

  return (
    <div className="p-6">
      <h1 className="text-2xl font-semibold mb-4">Dashboard</h1>

      {user ? (
        <div className="mb-8">
          <p>👤 <strong>Email:</strong> {user.email}</p>
          <p>🧾 <strong>Role:</strong> 
          {user?.roles?.length ? user.roles.join(", ") : "No roles"}</p>

          <button
            className="mt-4 px-4 py-2 bg-red-600 text-white rounded"
            onClick={handleLogout}
          >
            Logout
          </button>
        </div>
      ) : (
        <p>The user does not exist</p>
      )}

      <h2 className="text-xl font-semibold mb-3">Available Vacancies</h2>

      <ul className="space-y-4">
        {vacancies?.map((vac) => (
          <li key={vac.id} className="border rounded p-4">
            <h3 className="font-bold">{vac.title}</h3>
            <p>{vac.companyName}</p>
            <p className="text-sm text-gray-500">{vac.location}</p>
            <p className="text-sm">{vac.description}</p>
            <p className="text-xs text-gray-400">{vac.employmentType}</p>

            {Array.isArray(user?.roles) && user?.roles.includes("CANDIDATE") && (
              <button
                onClick={() => applyMutation.mutate(vac.id)}
                disabled={vac.applied || applyMutation.isPending}
                className={`mt-3 px-3 py-1 rounded text-white ${
                  vac.applied
                    ? "bg-gray-400 cursor-not-allowed"
                    : "bg-green-500 hover:bg-green-600"
                }`}
              >
                {vac.applied ? "Applied" : applyMutation.isPending ? "Applying..." : "Apply"}
              </button>
            )}
          </li>
        ))}
      </ul>
    </div>
  );
}