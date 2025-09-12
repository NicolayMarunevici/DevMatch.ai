import React, { useMemo, useState } from "react";
import { useCreateVacancy } from "@/features/vacancy/hooks/useCreateVacancy";

// NOTE: form initial state
const initial = {
  title: "",
  description: "",
  location: "",
  employmentType: "FULL_TIME",
  companyName: "",
  salaryMin: "",
  salaryMax: "",
  currency: "EUR",
  isRemote: true,
  skills: [] as string[],
};

export default function CreateVacancyPage() {
  const [form, setForm] = useState({ ...initial });
  const [skillInput, setSkillInput] = useState("");
  const mutation = useCreateVacancy();

  // NOTE: simple validation rules
  const isValid = useMemo(() => {
    if (!form.title.trim()) return false;
    if (!form.description.trim()) return false;
    if (!form.location.trim()) return false;
    if (!form.companyName.trim()) return false;
    return true;
  }, [form]);

  // NOTE: helpers
  const update = (patch: Partial<typeof form>) => setForm((s) => ({ ...s, ...patch }));

  const addSkill = () => {
    const s = skillInput.trim();
    if (!s) return;
    if (form.skills.includes(s)) return;
    update({ skills: [...form.skills, s] });
    setSkillInput("");
  };

  const removeSkill = (s: string) => {
    update({ skills: form.skills.filter((x) => x !== s) });
  };

  const onSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!isValid || mutation.isPending) return;

    const payload = {
      title: form.title.trim(),
      description: form.description.trim(),
      location: form.location.trim(),
      employmentType: form.employmentType as any,
      companyName: form.companyName.trim(),
      salaryMin: form.salaryMin ? Number(form.salaryMin) : undefined,
      salaryMax: form.salaryMax ? Number(form.salaryMax) : undefined,
      currency: form.currency as any,
      isRemote: !!form.isRemote,
      skills: form.skills,
    };

    mutation.mutate(payload);
  };

  return (
    <div className="min-h-screen bg-slate-50 px-4">
      <div className="max-w-3xl mx-auto py-10">
        <div className="mb-8 text-center">
          <h1 className="text-4xl font-bold tracking-tight">Create Vacancy</h1>
          <p className="mt-2 text-slate-600">
            Provide clear details about the role to get better AI matches.
          </p>
        </div>

        <div className="rounded-3xl bg-white border border-black/5 shadow-xl">
          <form onSubmit={onSubmit} className="p-6 sm:p-10 space-y-8">
            {/* Job Title */}
            <div className="space-y-2">
              <label className="block text-sm font-semibold text-slate-700">Job Title *</label>
              <input
                value={form.title}
                onChange={(e) => update({ title: e.target.value })}
                className="block w-full h-12 rounded-xl border border-slate-200 bg-white px-4 text-base outline-none
                           focus:border-indigo-400 focus:ring-2 focus:ring-indigo-100"
                placeholder="Senior Java Developer"
              />
            </div>

            {/* Company & Location */}
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-5">
              <div className="space-y-2">
                <label className="block text-sm font-semibold text-slate-700">Company *</label>
                <input
                  value={form.companyName}
                  onChange={(e) => update({ companyName: e.target.value })}
                  className="block w-full h-12 rounded-xl border border-slate-200 bg-white px-4 text-base outline-none
                             focus:border-indigo-400 focus:ring-2 focus:ring-indigo-100"
                  placeholder="Acme Corp"
                />
              </div>

              <div className="space-y-2">
                <label className="block text-sm font-semibold text-slate-700">Location *</label>
                <input
                  value={form.location}
                  onChange={(e) => update({ location: e.target.value })}
                  className="block w-full h-12 rounded-xl border border-slate-200 bg-white px-4 text-base outline-none
                             focus:border-indigo-400 focus:ring-2 focus:ring-indigo-100"
                  placeholder="Bucharest (Hybrid)"
                />
              </div>
            </div>

            {/* Employment Type & Remote */}
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-5">
              <div className="space-y-2">
                <label className="block text-sm font-semibold text-slate-700">Employment Type</label>
                <select
                  value={form.employmentType}
                  onChange={(e) => update({ employmentType: e.target.value })}
                  className="block w-full h-12 rounded-xl border border-slate-200 bg-white px-4 text-base outline-none
                             focus:border-indigo-400 focus:ring-2 focus:ring-indigo-100"
                >
                  <option value="FULL_TIME">Full-time</option>
                  <option value="PART_TIME">Part-time</option>
                  <option value="CONTRACT">Contract</option>
                  <option value="INTERNSHIP">Internship</option>
                  <option value="OTHER">Other</option>
                </select>
              </div>

              <div className="flex items-center gap-3 pt-6">
                <input
                  id="isRemote"
                  type="checkbox"
                  checked={form.isRemote}
                  onChange={(e) => update({ isRemote: e.target.checked })}
                  className="h-4 w-4 accent-indigo-600"
                />
                <label htmlFor="isRemote" className="text-sm font-semibold text-slate-700">
                  Remote friendly
                </label>
              </div>
            </div>

            {/* Salary */}
            <div className="grid grid-cols-1 sm:grid-cols-3 gap-5">
              <div className="space-y-2">
                <label className="block text-sm font-semibold text-slate-700">Salary Min</label>
                <input
                  type="number"
                  min={0}
                  value={form.salaryMin}
                  onChange={(e) => update({ salaryMin: e.target.value })}
                  className="block w-full h-12 rounded-xl border border-slate-200 bg-white px-4 text-base outline-none
                             focus:border-indigo-400 focus:ring-2 focus:ring-indigo-100"
                  placeholder="3000"
                />
              </div>
              <div className="space-y-2">
                <label className="block text-sm font-semibold text-slate-700">Salary Max</label>
                <input
                  type="number"
                  min={0}
                  value={form.salaryMax}
                  onChange={(e) => update({ salaryMax: e.target.value })}
                  className="block w-full h-12 rounded-xl border border-slate-200 bg-white px-4 text-base outline-none
                             focus:border-indigo-400 focus:ring-2 focus:ring-indigo-100"
                  placeholder="6000"
                />
              </div>
              <div className="space-y-2">
                <label className="block text-sm font-semibold text-slate-700">Currency</label>
                <select
                  value={form.currency}
                  onChange={(e) => update({ currency: e.target.value })}
                  className="block w-full h-12 rounded-xl border border-slate-200 bg-white px-4 text-base outline-none
                             focus:border-indigo-400 focus:ring-2 focus:ring-indigo-100"
                >
                  <option value="EUR">EUR</option>
                  <option value="USD">USD</option>
                  <option value="GBP">GBP</option>
                  <option value="RON">RON</option>
                  <option value="MDL">MDL</option>
                  <option value="OTHER">Other</option>
                </select>
              </div>
            </div>

            {/* Skills */}
            <div className="space-y-2">
              <label className="block text-sm font-semibold text-slate-700">Skills</label>
              <div className="flex gap-2">
                <input
                  value={skillInput}
                  onChange={(e) => setSkillInput(e.target.value)}
                  onKeyDown={(e) => {
                    if (e.key === "Enter") {
                      e.preventDefault();
                      addSkill();
                    }
                  }}
                  className="flex-1 h-12 rounded-xl border border-slate-200 bg-white px-4 text-base outline-none
                             focus:border-indigo-400 focus:ring-2 focus:ring-indigo-100"
                  placeholder="e.g. Java, Spring Boot, React"
                />
                <button
                  type="button"
                  onClick={addSkill}
                  className="h-12 px-4 rounded-xl bg-indigo-600 text-white font-semibold hover:bg-indigo-500 transition"
                >
                  Add
                </button>
              </div>

              {form.skills.length > 0 && (
                <div className="mt-2 flex flex-wrap gap-2">
                  {form.skills.map((s) => (
                    <span
                      key={s}
                      className="inline-flex items-center gap-2 rounded-full border bg-slate-100 text-slate-700
                                 text-xs font-medium px-3 py-1"
                    >
                      {s}
                      <button
                        type="button"
                        onClick={() => removeSkill(s)}
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

            {/* Description */}
            <div className="space-y-2">
              <label className="block text-sm font-semibold text-slate-700">Description *</label>
              <textarea
                value={form.description}
                onChange={(e) => update({ description: e.target.value })}
                rows={6}
                className="block w-full rounded-xl border border-slate-200 bg-white px-4 py-3 text-base outline-none
                           focus:border-indigo-400 focus:ring-2 focus:ring-indigo-100"
                placeholder={`Responsibilities:
- ...
Requirements:
- ...
Nice to have:
- ...
`}
              />
              <p className="text-xs text-slate-500">
                Provide clear requirements and nice-to-haves. AI uses this text for better matching.
              </p>
            </div>

            {/* Actions & Status */}
            {mutation.isError && (
              <div className="rounded-xl border border-rose-200 bg-rose-50 px-3 py-2 text-sm text-rose-700">
                Failed to create vacancy. Please check fields and try again.
              </div>
            )}

            <div className="flex items-center justify-end gap-3">
              <button
                type="button"
                onClick={() => setForm({ ...initial })}
                className="h-12 px-5 rounded-xl border border-slate-200 bg-white text-slate-700 hover:bg-slate-50 transition"
              >
                Reset
              </button>
              <button
                type="submit"
                disabled={!isValid || mutation.isPending}
                className="h-12 px-6 rounded-xl bg-indigo-600 text-white font-semibold hover:bg-indigo-500
                           disabled:opacity-60 transition"
              >
                {mutation.isPending ? "Creating..." : "Create Vacancy"}
              </button>
            </div>
          </form>
        </div>

        {mutation.isSuccess && (
          <p className="mt-4 text-center text-sm text-emerald-600">
            Vacancy successfully created.
          </p>
        )}
      </div>
    </div>
  );
}
