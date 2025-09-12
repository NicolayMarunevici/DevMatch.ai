import React from "react";
import {Routes, Route} from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import ProfilePage from "./pages/ProfilePage";
import Header from "./components/Header.tsx";
import RequireAuth from "./router/RequireAuth.tsx";
import ProfileEditPage from "./pages/ProfileEditPage.tsx";
import Dashboard from "./pages/Dashboard.tsx";
import { useAuthInit } from "./features/auth/hooks/useAuthInit.ts";
import { useAuthStore } from "./features/auth/store/useAuthStore.ts";
import { useLocation } from "react-router-dom"; 
import CreateVacancyPage from "./pages/CreateVacancyPage.tsx";
import EditVacancyPage from "./pages/EditVacancyPage.tsx";
import MyVacanciesPage from "./pages/MyVacanciesPage.tsx";

function App() {
    useAuthInit();
    const isAuthLoading = useAuthStore((state) => state.isAuthLoading);

    const { pathname } = useLocation();
    const hideHeader = pathname === "/login" || pathname === "/register";

    if(isAuthLoading){
        return (
            <div className="flex justify-center items-center h-screen text-xl">
                Loading...
            </div>
        );
    }

    return (
        <>
            {!hideHeader && <Header />}
            <Routes>
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />

                {/* Private Pages */}
                <Route
                    path="/profile"
                    element={
                        <RequireAuth>
                            <ProfilePage />
                        </RequireAuth>
                    }
                />
                <Route
                    path="/profile/edit"
                    element={
                        <RequireAuth>
                            <ProfileEditPage />
                        </RequireAuth>
                    }
                />
                <Route
                    path="/dashboard"
                    element={
                        <RequireAuth>
                            <Dashboard />
                        </RequireAuth>
                    }
                />

                <Route path="/vacancy/create" element={ <RequireAuth><CreateVacancyPage /></RequireAuth>
            }
/>
            <Route path="/vacancy/:id/edit" element={ <RequireAuth> <EditVacancyPage /> </RequireAuth>
        }
/>
 <Route
    path="/profile/vacancies"
    element={
      <RequireAuth>
        <MyVacanciesPage />
      </RequireAuth>
    }
  />
            </Routes>
        </>
    );
}

export default App;