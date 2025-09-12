import React, { useEffect, useState } from "react";
import axiosInstance from "../api/axiosInstance";
import { ProfileCard } from "../components/ProfileCard";
import type { AxiosResponse } from "axios";

function ProfilePage(){
    const [profile, setProfile] = useState<any>(null);
    const [error, setError] = useState("");

    useEffect(() => {
        axiosInstance.get<User>("/api/users/me")
            .then((res : AxiosResponse<User>) => setProfile(res.data))            
            .catch(() => 
                setError("You are not authorized"));
    }, []);

    if(error){
            return (
      <div className="flex justify-center items-center min-h-screen text-red-600 text-xl">
        {error}
      </div>
    );
    }

    if(!profile){
        return (
      <div className="flex justify-center items-center min-h-screen text-xl">
        Loading profile...
      </div>
    );
}

    const roles = Array.isArray(profile.roles) ? profile.roles : [profile.role].filter(Boolean);

    return (
        <div className="min-h-screen bg-gray-100 p-4">
            <ProfileCard user = {{ ...profile, roles}}/>
        </div>
    );
}

export default ProfilePage;