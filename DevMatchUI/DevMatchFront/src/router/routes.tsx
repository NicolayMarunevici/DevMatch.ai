import {Route, Routes} from "react-router-dom";
import LoginPage from "../pages/LoginPage.tsx";
import RegisterPage from "../pages/RegisterPage.tsx";
import RequireAuth from "./RequireAuth.tsx";
import Dashboard from "../pages/Dashboard.tsx";
import ProfilePage from "../pages/ProfilePage.tsx";
import ProfileEditPage from "../pages/ProfileEditPage.tsx";
import CreateVacancyPage from "../pages/CreateVacancyPage.tsx";
import MyApplicationPage from "../pages/MyApplicationPage.tsx";


export function AppRoutes(){
    return (
        <Routes>
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="/dashboard" element={<RequireAuth> <Dashboard /> </RequireAuth>} />
            <Route path="/profile" element={<RequireAuth> <ProfilePage /> </RequireAuth>} />
            <Route path="/profile/edit" element={<RequireAuth><ProfileEditPage /></RequireAuth>} />
            <Route path="/vacancy/create" element={<RequireAuth><CreateVacancyPage/></RequireAuth>} />
            <Route path="/vacancy/create" element={<RequireAuth><MyApplicationPage/></RequireAuth>} />
            <Route path="/vacancy/create" element={ <RequireAuth><CreateVacancyPage/></RequireAuth>}/>

        </Routes>
    )
}