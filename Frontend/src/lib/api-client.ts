import axios from "axios";
import type { AxiosError, AxiosInstance, InternalAxiosRequestConfig } from "axios";

export const API_BASE_URL: string =
  (import.meta.env["VITE_API_BASE_URL"] as string | undefined) ?? "/api/v1";

export const TOKEN_STORAGE_KEY = "sfa.token";

export interface ApiError {
  status: number;
  message: string;
  code?: string;
  fields?: Record<string, string>;
}

export function getToken(): string | null {
  if (typeof window === "undefined") return null;
  return window.localStorage.getItem(TOKEN_STORAGE_KEY);
}

export function setToken(token: string | null): void {
  if (typeof window === "undefined") return;
  if (token) window.localStorage.setItem(TOKEN_STORAGE_KEY, token);
  else window.localStorage.removeItem(TOKEN_STORAGE_KEY);
}

export const apiClient: AxiosInstance = axios.create({
  baseURL: API_BASE_URL,
  timeout: 20000,
  headers: { "Content-Type": "application/json" },
});

apiClient.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  const token = getToken();
  if (token) config.headers.set("Authorization", `Bearer ${token}`);
  return config;
});

export function normalizeError(error: unknown): ApiError {
  const axiosError = error as AxiosError<{ message?: string; code?: string; errors?: Record<string, string> }>;
  if (axiosError?.isAxiosError) {
    if (!axiosError.response) {
      return {
        status: 0,
        message: "The Smart Farmer AI service is unreachable. Please check your connection.",
        code: "NETWORK_ERROR",
      };
    }
    const data = axiosError.response.data;
    return {
      status: axiosError.response.status,
      message: data?.message ?? axiosError.message ?? "Request failed.",
      ...(data?.code ? { code: data.code } : {}),
      ...(data?.errors ? { fields: data.errors } : {}),
    };
  }
  return { status: 0, message: error instanceof Error ? error.message : "Unexpected error." };
}

apiClient.interceptors.response.use(
  (response) => response,
  (error: unknown) => {
    const normalized = normalizeError(error);
    if (normalized.status === 401) setToken(null);
    return Promise.reject(normalized);
  },
);

export async function get<T>(url: string, params?: Record<string, unknown>): Promise<T> {
  const res = await apiClient.get<T>(url, params ? { params } : undefined);
  return res.data;
}

export async function post<T>(url: string, body?: unknown): Promise<T> {
  const res = await apiClient.post<T>(url, body);
  return res.data;
}

export async function put<T>(url: string, body?: unknown): Promise<T> {
  const res = await apiClient.put<T>(url, body);
  return res.data;
}

export async function del<T>(url: string): Promise<T> {
  const res = await apiClient.delete<T>(url);
  return res.data;
}
