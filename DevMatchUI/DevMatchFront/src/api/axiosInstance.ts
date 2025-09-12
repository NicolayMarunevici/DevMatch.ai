import axios, {AxiosError} from "axios";
import {useAuthStore} from "../features/auth/store/useAuthStore.ts";


const axiosInstance = axios.create({
  baseURL: "http://localhost:8888",
  withCredentials: true,
});


axiosInstance.defaults.headers.common["Accept"] = "application/json";
axiosInstance.defaults.headers.post["Content-Type"] = "application/json";
axiosInstance.defaults.headers.put["Content-Type"] = "application/json";

axiosInstance.interceptors.response.use(
  (res) => res,
  (error) => {
    const status = error?.response?.status;

    // ТОЛЬКО 401: истёк/нет токена -> выходим и отправляем на /login
    if (status === 401) {
      try {
        useAuthStore.getState().logout?.();
      } catch {}
      const cur = window.location.pathname;
      if (cur !== "/login" && cur !== "/register") {
        window.location.href = "/login";
      }
      return Promise.reject(error);
    }

    // 403 И ДРУГОЕ — не редиректим! Отдаём ошибку в UI
    return Promise.reject(error);
  }
);

export default axiosInstance;