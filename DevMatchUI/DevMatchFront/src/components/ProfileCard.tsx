import { Badge } from "@/components/ui/badge";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { UserIcon } from "lucide-react";
import { useNavigate } from "react-router-dom";
import { Button } from "@/components/ui/button";

type ProfileCardProps = {
    user: {
        firstName: string;
        lastName: string;
        email: string;
        roles: string[];
    }
}

export function ProfileCard({ user }: ProfileCardProps) {
const navigate = useNavigate();
const initials = (
    (user.firstName?.[0] ?? "") + (user.lastName?.[0] ?? "")
  ).toUpperCase();

  // Гарантируем массив строк
  const roles = (user.roles ?? []).map((r) =>
    typeof r === "string" ? r : String(r ?? "")
  );



  return (
    <Card className="max-w-xl mx-auto mt-12 shadow-lg rounded-2xl border bg-white">
      <CardHeader className="flex flex-col items-center space-y-4">
        {/* Avatar */}
        <div className="w-20 h-20 bg-indigo-600 text-white rounded-full flex items-center justify-center text-3xl font-bold">
          {initials || "?"}
        </div>

        {/* Name */}
        <CardTitle className="text-2xl text-center">
          {(user.firstName ?? "") + " " + (user.lastName ?? "")}
        </CardTitle>

        {/* Email */}
        <p className="text-sm text-gray-500">{user.email ?? ""}</p>

        {/* Roles */}
        <div className="flex flex-wrap gap-2 mt-2 justify-center">
          {roles.length === 0 ? (
            <span className="text-xs text-gray-400">No roles</span>
          ) : (
            roles.map((role, index) => (
              <span
                key={index}
                className="uppercase tracking-wide text-xs px-2.5 py-0.5 rounded-full bg-gray-100 text-gray-700 border"
              >
                {role.replace(/^ROLE_/, "")}
              </span>
            ))
          )}
        </div>
      </CardHeader>
      <CardContent className="flex justify-center">
        <button
    onClick={() => navigate("/profile/edit")}
    className="px-4 py-2 rounded-lg bg-indigo-600 text-white font-semibold hover:bg-indigo-500 transition">
    Edit
  </button>
      </CardContent>
    </Card>
  );
}