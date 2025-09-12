import { useState } from "react";
import { useLogin } from "../hooks/useLogin";
import { Link } from "react-router-dom";

export function LoginForm() {
  const { mutate: login, isPending, error } = useLogin();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    login({ email, password });
  };

  return (
    /* ВАЖНО: этот блок сам центрирует форму по вертикали и горизонтали */
    <div className="min-h-[70vh] w-full flex items-center justify-center px-4">
      <div className="w-full max-w-lg">
        <div className="rounded-3xl bg-white/95 backdrop-blur border border-black/5 shadow-2xl">
          <div className="p-8 sm:p-12">
            <div className="mb-10 text-center">
              <h1 className="text-5xl font-extrabold tracking-tight">Welcome Back</h1>
              <p className="mt-3 text-sm text-gray-600">
                Sign in to continue to your dashboard.
              </p>
            </div>

            <form onSubmit={handleSubmit} className="space-y-8">
              {/* EMAIL */}
              <div className="space-y-2">
                <label htmlFor="email" className="block text-sm font-semibold text-gray-700">
                  Email
                </label>
                <input
                  id="email"
                  type="email"
                  required
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  className="block w-full h-14 rounded-2xl border-2 border-gray-300 bg-white px-5 text-lg
                             outline-none ring-0 transition shadow-sm
                             focus:border-indigo-500 focus:ring-2 focus:ring-indigo-100"
                  placeholder="you@example.com"
                  autoComplete="email"
                />
              </div>

              {/* PASSWORD */}
              <div className="space-y-2">
                <div className="flex items-center justify-between">
                  <label htmlFor="password" className="block text-sm font-semibold text-gray-700">
                    Password
                  </label>
                  <button
                    type="button"
                    onClick={() => setShowPassword((v) => !v)}
                    className="text-xs font-semibold text-indigo-600 hover:underline"
                  >
                    {showPassword ? "Hide" : "Show"}
                  </button>
                </div>

                <input
                  id="password"
                  type={showPassword ? "text" : "password"}
                  required
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  className="block w-full h-14 rounded-2xl border-2 border-gray-300 bg-white px-5 text-lg
                             outline-none ring-0 transition shadow-sm
                             focus:border-indigo-500 focus:ring-2 focus:ring-indigo-100"
                  placeholder="••••••••"
                  autoComplete="current-password"
                />
              </div>

              {error && (
                <div className="rounded-2xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
                  Login failed. Check your credentials and try again.
                </div>
              )}

              <button
                type="submit"
                disabled={isPending}
                className="inline-flex w-full h-14 items-center justify-center rounded-2xl
                           bg-gradient-to-tr from-indigo-600 to-fuchsia-600 px-6 text-base font-semibold text-white
                           shadow-lg transition hover:from-indigo-500 hover:to-fuchsia-500 active:scale-[.99]
                           disabled:opacity-60"
              >
                {isPending ? "Signing in..." : "Sign in"}
              </button>

              <div className="text-center text-sm text-gray-600">
                Don’t have an account?{" "}
                <Link to="/register" className="font-semibold text-indigo-600 hover:underline">
                  Create one
                </Link>
              </div>
            </form>
          </div>
        </div>

        <p className="mt-8 text-center text-xs text-gray-500">
          Protected by reCAPTCHA and subject to the{" "}
          <a className="underline decoration-dotted underline-offset-2 hover:text-gray-700" href="#">
            Terms of Service
          </a>{" "}
          and{" "}
          <a className="underline decoration-dotted underline-offset-2 hover:text-gray-700" href="#">
            Privacy Policy
          </a>
          .
        </p>
      </div>
    </div>
  );
}
