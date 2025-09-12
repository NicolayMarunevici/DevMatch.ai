import React, { useState, useEffect } from "react";
import { useUpdateProfile } from "../features/auth/hooks/useUpdateProfile";
import { useAuthStore } from "../features/auth/store/useAuthStore";

const ProfileEditPage = () => {
    const { user } = useAuthStore();
    const mutation = useUpdateProfile();

    const [firstName, setFirstName] = useState(user?.firstName || "");
    const [lastName, setLastName] = useState(user?.lastName || "");
    const [email, setEmail] = useState(user?.email || "");

    useEffect(() => {
        if (user) {
            setFirstName(user.firstName);
            setLastName(user.lastName);
            setEmail(user.email);
        }
    }, [user]);

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        mutation.mutate({ firstName, lastName, email });
    };

    const isSaving = mutation.isPending;
    const errStatus = (mutation.error as any)?.response?.status;

return (
    <div style={{ maxWidth: 500, margin: "50px auto" }}>
      <h2>Edit Profile</h2>

      <form onSubmit={handleSubmit}>
        <div style={{ marginBottom: 10 }}>
          <label>First Name:</label>
          <input
            type="text"
            value={firstName}
            onChange={(e) => setFirstName(e.target.value)}
            style={{ width: "100%", padding: 8 }}
          />
        </div>

        <div style={{ marginBottom: 10 }}>
          <label>Last Name:</label>
          <input
            type="text"
            value={lastName}
            onChange={(e) => setLastName(e.target.value)}
            style={{ width: "100%", padding: 8 }}
          />
        </div>

        <div style={{ marginBottom: 10 }}>
          <label>Email:</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            style={{ width: "100%", padding: 8 }}
          />
        </div>

        <button type="submit" disabled={isSaving} style={{ padding: "10px 20px" }}>
          {isSaving ? "Saving..." : "Save"}
        </button>
      </form>

      {mutation.isSuccess && <p style={{ color: "green" }}>Profile updated!</p>}

      {mutation.isError && (
        <p style={{ color: "red" }}>
          {errStatus === 403
            ? "You don't have permission to update this profile (403)."
            : errStatus === 400
            ? "Validation error. Please check your inputs."
            : "Error updating profile."}
        </p>
      )}
    </div>
  );
};

export default ProfileEditPage;