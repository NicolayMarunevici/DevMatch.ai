import { useEffect, useMemo, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useVacancy } from "../features/vacancy/hooks/useVacancy";
import { useUpdateVacancy } from "../features/vacancy/hooks/useUpdateVacancy";
import { isOwner } from "../api/vacancy";
import { useAuthStore } from "../features/auth/store/useAuthStore";

export default function EditVacancyPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const user = useAuthStore((s) => s.user);

  const { data: vacancy, isLoading, isError } = useVacancy(id!);
  const mutation = useUpdateVacancy(id!);

  const [form, setForm] = useState({
    title: "",
    description: "",
    location: "",
    companyName: "",
    employmentType: "FULL_TIME",
    skills: [] as string[],
  });

  // Hydrate form from loaded vacancy
  useEffect(() => {
    if (!vacancy) return;
    setForm({
      title: vacancy.title ?? "",
      description: vacancy.description ?? "",
      location: vacancy.location ?? "",
      companyName: vacancy.companyName ?? "",
      employmentType: (vacancy.employmentType as any) ?? "FULL_TIME",
      skills: vacancy.skills ?? [],
    });
  }, [vacancy]);

  const canEdit = useMemo(
    () => (vacancy && user ? isOwner(vacancy, user) : false),
    [vacancy, user]
  );

  const isValid =
    form.title.trim() &&
    form.description.trim() &&
    form.location.trim() &&
    form.companyName.trim();

  const update = (patch: Partial<typeof form>) => setForm((s) => ({ ...s, ...patch }));

  const onSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!isValid || mutation.isPending || !id) return;

    mutation.mutate(
      {
        title: form.title.trim(),
        description: form.description.trim(),
        location: form.location.trim(),
        companyName: form.companyName.trim(),
        employmentType: form.employmentType as any,
        skills: form.skills,
      },
      {
        onSuccess: () => navigate(`/profile/vacancies`), // go back to My Vacancies
      }
    );
  };

  if (isLoading) return <div className="min-h-[40vh] grid place-items-center">Loading...</div>;
  if (isError || !vacancy)
    return <div className="min-h-[40vh] grid place-items-center text-red-600">Failed to load vacancy.</div>;
  if (!canEdit)
    return (
      <div className="min-h-[40vh] grid place-items-center text-amber-600">
        You do not have permission to edit this vacancy (owner only).
      </div>
    );

  return (
    <div className="min-h-screen bg-slate-50 px-4">
      <div className="max-w-3xl mx-auto py-10">
        <div className="mb-8 text-center">
          <h1 className="text-4xl font-bold">Edit Vacancy</h1>
          <p className="mt-2 text-slate-600">Only the creator can modify this vacancy.</p>
        </div>

        <div className="rounded-3xl bg-white border border-black/5 shadow-xl">
          <form onSubmit={onSubmit} className="p-6 sm:p-10 space-y-6">
            <div className="space-y-2">
              <label className="block text-sm font-semibold text-slate-700">Job Title *</label>
              <input
                value={form.title}
                onChange={(e) => update({ title: e.target.value })}
                className="block w-full h-12 rounded-xl border border-slate-200 px-4 outline-none
                           focus:border-indigo-400 focus:ring-2 focus:ring-indigo-100"
                placeholder="Senior Java Developer"
              />
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-5">
              <div className="space-y-2">
                <label className="block text-sm font-semibold text-slate-700">Company *</label>
                <input
                  value={form.companyName}
                  onChange={(e) => update({ companyName: e.target.value })}
                  className="block w-full h-12 rounded-xl border border-slate-200 px-4 outline-none
                             focus:border-indigo-400 focus:ring-2 focus:ring-indigo-100"
                />
              </div>

              <div className="space-y-2">
                <label className="block text-sm font-semibold text-slate-700">Location *</label>
                <input
                  value={form.location}
                  onChange={(e) => update({ location: e.target.value })}
                  className="block w-full h-12 rounded-xl border border-slate-200 px-4 outline-none
                             focus:border-indigo-400 focus:ring-2 focus:ring-indigo-100"
                />
              </div>
            </div>

            <div className="space-y-2">
              <label className="block text-sm font-semibold text-slate-700">Employment Type</label>
              <select
                value={form.employmentType}
                onChange={(e) => update({ employmentType: e.target.value })}
                className="block w-full h-12 rounded-xl border border-slate-200 px-4 outline-none
                           focus:border-indigo-400 focus:ring-2 focus:ring-indigo-100"
              >
                <option value="FULL_TIME">Full-time</option>
                <option value="PART_TIME">Part-time</option>
                <option value="CONTRACT">Contract</option>
              </select>
            </div>

            <SkillEditor skills={form.skills} onChange={(skills) => update({ skills })} />

            <div className="space-y-2">
              <label className="block text-sm font-semibold text-slate-700">Description *</label>
              <textarea
                value={form.description}
                onChange={(e) => update({ description: e.target.value })}
                rows={6}
                className="block w-full rounded-xl border border-slate-200 px-4 py-3 outline-none
                           focus:border-indigo-400 focus:ring-2 focus:ring-indigo-100"
              />
            </div>

            <div className="flex items-center justify-end gap-3">
              <button
                type="button"
                onClick={() => navigate(-1)}
                className="h-12 px-5 rounded-xl border border-slate-200 bg-white text-slate-700 hover:bg-slate-50 transition"
              >
                Cancel
              </button>
              <button
                type="submit"
                disabled={!isValid || mutation.isPending}
                className="h-12 px-6 rounded-xl bg-indigo-600 text-white font-semibold hover:bg-indigo-500
                           disabled:opacity-60 transition"
              >
                {mutation.isPending ? "Saving..." : "Save changes"}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

// Inline skills editor for the form
function SkillEditor({
  skills,
  onChange,
}: {
  skills: string[];
  onChange: (s: string[]) => void;
}) {
  const [input, setInput] = useState("");

  const add = () => {
    const v = input.trim();
    if (!v) return;
    if (skills.includes(v)) return;
    onChange([...skills, v]);
    setInput("");
  };

  const remove = (v: string) => onChange(skills.filter((x) => x !== v));

  return (
    <div className="space-y-2">
      <label className="block text-sm font-semibold text-slate-700">Skills</label>
      <div className="flex gap-2">
        <input
          value={input}
          onChange={(e) => setInput(e.target.value)}
          onKeyDown={(e) => {
            if (e.key === "Enter") {
              e.preventDefault();
              add();
            }
          }}
          className="flex-1 h-12 rounded-xl border border-slate-200 px-4 outline-none
                     focus:border-indigo-400 focus:ring-2 focus:ring-indigo-100"
          placeholder="e.g. Java, Spring Boot, Kafka"
        />
        <button
          type="button"
          onClick={add}
          className="h-12 px-4 rounded-xl bg-indigo-600 text-white font-semibold hover:bg-indigo-500 transition"
        >
          Add
        </button>
      </div>

      {skills.length > 0 && (
        <div className="mt-2 flex flex-wrap gap-2">
          {skills.map((s) => (
            <span
              key={s}
              className="inline-flex items-center gap-2 rounded-full border bg-slate-100 text-slate-700
                         text-xs font-medium px-3 py-1"
            >
              {s}
              <button
                type="button"
                onClick={() => remove(s)}
                className="hover:text-rose-600"
                aria-label={`remove ${s}`}
                title="Remove"
              >
                ✕
              </button>
            </span>
          ))}
        </div>
      )}
    </div>
  );
}
