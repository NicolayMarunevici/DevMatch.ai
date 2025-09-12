import { Link, useLocation, useNavigate } from "react-router-dom";
import { useAuthStore } from "@/features/auth/store/useAuthStore";

// NOTE: normalize a single role string (handles "ROLE_X", case, spaces)
function normalizeRole(r: unknown): string | null {
  if (r == null) return null;
  const s = String(
    // supports objects like { authority: 'ROLE_RECRUITER' } or { name: 'RECRUITER' }
    typeof r === "object" && "authority" in (r as any)
      ? (r as any).authority
      : typeof r === "object" && "name" in (r as any)
      ? (r as any).name
      : r
  )
    .trim()
    .toUpperCase()
    .replace(/^ROLE_/, "");
  return s || null;
}

// NOTE: extract all roles from user in a robust way
function extractRoles(user: any): string[] {
  const bag: unknown[] = [];
  if (Array.isArray(user?.roles)) bag.push(...user.roles);    // roles: string[]
  if (user?.role) bag.push(user.role);                         // role: string
  if (Array.isArray(user?.authorities)) bag.push(...user.authorities); // authorities: [{authority:...}]
  const norm = bag.map(normalizeRole).filter((x): x is string => Boolean(x));
  return Array.from(new Set(norm));
}

// NOTE: small helper to build link classes with active state
function navLinkClass(active: boolean) {
  return [
    "transition",
    active ? "text-white font-semibold" : "text-gray-300 hover:text-white",
  ].join(" ");
}

export default function Header() {
  const { user, logout } = useAuthStore();
  const navigate = useNavigate();
  const location = useLocation();

  // NOTE: hide header on auth pages
  const hiddenRoutes = ["/login", "/register"];
  if (hiddenRoutes.includes(location.pathname)) return null;

  // NOTE: derive roles robustly
  const roles = extractRoles(user);

  // NOTE: recruiter-only flag (do NOT include candidate/admin here)
  const isRecruiter = roles.includes("RECRUITER");

  // NOTE: logout handler
  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  const path = location.pathname;

  return (
    <header className="bg-gray-800 text-white shadow-md">
      <div className="container mx-auto px-4 py-3 flex items-center justify-between">
        {/* Logo */}
        <Link to="/" className="text-xl font-bold hover:text-gray-300">
          DevMatch
        </Link>

        {/* Navigation */}
        <nav className="flex items-center gap-6">
          <Link
            to="/dashboard"
            className={navLinkClass(path.startsWith("/dashboard"))}
          >
            Dashboard
          </Link>

          <Link
            to="/profile"
            className={navLinkClass(path === "/profile")}
          >
            Profile
          </Link>

          <Link
            to="/profile/edit"
            className={navLinkClass(path === "/profile/edit")}
          >
            Edit Profile
          </Link>

          {/* Recruiter-only links */}
          {isRecruiter && (
            <>
              <Link
                to="/vacancy/create"
                className={navLinkClass(path === "/vacancy/create")}
              >
                Create Vacancy
              </Link>

              <Link
                to="/profile/vacancies"
                className={navLinkClass(path.startsWith("/profile/vacancies"))}
              >
                My Vacancies
              </Link>
            </>
          )}

          {/* Candidate-only link */}
          {roles.includes("CANDIDATE") && (
            <Link
              to="/applications"
              className={navLinkClass(path.startsWith("/applications"))}
            >
              My Applications
            </Link>
          )}
        </nav>

        {/* User + Actions */}
        <div className="flex items-center gap-4">
          {user && (
            <span className="hidden sm:block text-gray-300">
              {/* NOTE: simple greeting */}
              👋 {user.firstName}
            </span>
          )}

          {user ? (
            <button
              onClick={handleLogout}
              className="bg-red-500 hover:bg-red-600 px-3 py-1 rounded text-sm transition"
            >
              Logout
            </button>
          ) : (
            <Link
              to="/login"
              className="bg-blue-500 hover:bg-blue-600 px-3 py-1 rounded text-sm transition"
            >
              Login
            </Link>
          )}
        </div>
      </div>
    </header>
  );
}
