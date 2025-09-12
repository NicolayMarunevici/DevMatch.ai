import { isOwner, type Vacancy } from "@/api/vacancy";
import { useAuthStore } from "@/features/auth/store/useAuthStore";
import { Link } from "react-router-dom";

export default function VacancyDetails({ vacancy }: { vacancy: Vacancy }) {
  const user = useAuthStore((s) => s.user);
  const canEdit = isOwner(vacancy, user);

  return (
    <div>
      {/* ...vacancy info... */}

      {canEdit && (
        <div className="mt-4">
          <Link
            to={`/vacancy/${vacancy.id}/edit`}
            className="inline-block px-4 py-2 rounded-lg bg-indigo-600 text-white font-semibold hover:bg-indigo-500 transition"
          >
            Edit Vacancy
          </Link>
        </div>
      )}
    </div>
  );
}
