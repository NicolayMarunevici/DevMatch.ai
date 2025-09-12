import { useMyVacancies } from "../features/vacancy/hooks/useMyVacancies";
import { Link } from "react-router-dom";

// Card used here allows "Edit" link; on Dashboard you should not show Edit.
function VacancyCard({ v }: { v: any }) {
  return (
    <div className="rounded-xl border bg-white shadow p-5">
      <h3 className="text-xl font-semibold">{v.title}</h3>
      <p className="text-gray-600">
        {v.companyName} • {v.location}
      </p>

      {Array.isArray(v.skills) && v.skills.length > 0 && (
        <div className="mt-3 flex flex-wrap gap-2">
          {v.skills.map((s: string) => (
            <span key={s} className="px-3 py-1 rounded-full bg-gray-100 text-gray-700 text-xs">
              {s}
            </span>
          ))}
        </div>
      )}

      <div className="mt-4 flex gap-3">
        <Link to={`/vacancy/${v.id}`} className="px-3 py-1.5 rounded-lg border text-sm hover:bg-gray-50">
          View
        </Link>
        <Link
          to={`/vacancy/${v.id}/edit`}
          className="px-3 py-1.5 rounded-lg bg-indigo-600 text-white text-sm hover:bg-indigo-500"
        >
          Edit
        </Link>
      </div>
    </div>
  );
}

export default function MyVacanciesPage() {
  const { data, isLoading, isError } = useMyVacancies();

  if (isLoading) return <div className="p-6">Loading...</div>;
  if (isError) return <div className="p-6 text-red-600">Failed to load your vacancies.</div>;

  return (
    <div className="max-w-5xl mx-auto p-6">
      <div className="mb-6">
        <h1 className="text-3xl font-bold">My Vacancies</h1>
        <p className="text-gray-600">You can edit your own vacancies here.</p>
      </div>

{!data?.content?.length ? (
  <p className="text-gray-600">No vacancies yet.</p>
) : (
  <div className="grid gap-5 sm:grid-cols-2 lg:grid-cols-3">
    {data.content.map((v) => (
      <VacancyCard key={v.id} v={v} />
    ))}
  </div>
)}
    </div>
  );
}
