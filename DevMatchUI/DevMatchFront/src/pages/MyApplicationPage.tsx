import {useAuthStore} from "../features/auth/store/useAuthStore.ts";
import {useQuery} from "@tanstack/react-query";
import axiosInstance from "../api/axiosInstance.ts";


interface Application {
    id: number;
    vacancyTitle: string;
    companyName: string;
    status: string; // PENDING, ACCEPTED, REJECTED
}


export default function MyApplicationPage(){
    const { user } = useAuthStore();

    const { data, isLoading, error } = useQuery<Application>({
        queryKey: ["applications", user?.id],
        queryFn: async () => {
            const res = await axiosInstance.get('/api/applications/my');
            return res.data;
        },
        enabled: !!user,
    });

    if(isLoading){
        return <p className="p-4">Loading application...</p>;
    }

    if(error){
        return <p className="p-4 text-red-500">Error loading applications</p>;
    }

    if(!data || data.length === 0){
        return <p className="p-4">You have not applied to any jobs yet.</p>;
    }

    return (
        <div className="max-w-4xl mx-auto p-6">
            <h1 className="text-2xl font-bold mb-4">My Applications</h1>
            <ul className="space-y-3">
                {data.map((app) => (
                    <li
                        key={app.id}
                        className="border rounded p-4 flex justify-between items-center"
                    >
                        <div>
                            <p className="font-semibold">{app.vacancyTitle}</p>
                            <p className="text-sm text-gray-600">{app.companyName}</p>
                        </div>
                        <span
                            className={`px-2 py-1 rounded text-sm ${
                                app.status === "ACCEPTED"
                                    ? "bg-green-200 text-green-800"
                                    : app.status === "REJECTED"
                                        ? "bg-red-200 text-red-800"
                                        : "bg-yellow-200 text-yellow-800"
                            }`}
                        >
              {app.status}
            </span>
                    </li>
                ))}
            </ul>
        </div>
    );
}