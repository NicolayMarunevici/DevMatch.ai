// src/features/auth/components/RegisterForm.tsx
import { useState } from "react";
import { useRegister } from "../hooks/useRegister";

type FormRole = "CANDIDATE" | "RECRUITER";

const roleToBackend = (r: FormRole): "ROLE_CANDIDATE" | "ROLE_RECRUITER" => {
  return r === "RECRUITER" ? "ROLE_RECRUITER" : "ROLE_CANDIDATE";
};

export function RegisterForm() {
  const { mutate: register, isPending, error } = useRegister();

  const [email, setEmail] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState<FormRole>("CANDIDATE");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const roles = [roleToBackend(role)]; // backend expects exact RoleEnum values
    register({ email, password, firstName, lastName, roles } as any);
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4 max-w-sm mx-auto">
      <h2 className="text-xl font-semibold text-center">Create account</h2>

      <input
        type="text"
        placeholder="First Name"
        value={firstName}
        onChange={(e) => setFirstName(e.target.value)}
        className="w-full border px-3 py-2 rounded"
        required
      />

      <input
        type="text"
        placeholder="Last Name"
        value={lastName}
        onChange={(e) => setLastName(e.target.value)}
        className="w-full border px-3 py-2 rounded"
        required
      />

      <input
        type="email"
        placeholder="Email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        className="w-full border px-3 py-2 rounded"
        required
      />

      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        className="w-full border px-3 py-2 rounded"
        required
      />

      {/* Role selector */}
      <select
        value={role}
        onChange={(e) => setRole(e.target.value as FormRole)}
        className="w-full border px-3 py-2 rounded"
      >
        <option value="CANDIDATE">Candidate</option>
        <option value="RECRUITER">Recruiter</option>
      </select>

      {error && (
        <p className="text-sm text-red-600">
          Registration failed. Please try again.
        </p>
      )}

      <button
        type="submit"
        disabled={isPending}
        className="w-full bg-blue-600 text-white py-2 rounded"
      >
        {isPending ? "Registering..." : "Create account"}
      </button>
    </form>
  );
}
