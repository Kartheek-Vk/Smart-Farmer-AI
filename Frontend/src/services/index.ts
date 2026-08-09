import { del, get, post, put } from "@/lib/api-client";
import type {
  AppNotification,
  ChatMessage,
  Conversation,
  Crop,
  DiseaseScan,
  Farm,
  Field,
  MarketPrice,
  Paginated,
  Recommendation,
  Scheme,
  User,
  WeatherSnapshot,
} from "@/types";

export interface Credentials {
  email: string;
  password: string;
}

export interface RegisterPayload {
  name: string;
  email: string;
  phone: string;
  password: string;
  role: string;
}

export interface AuthResponse {
  token: string;
  user: User;
}

export const authService = {
  login: (payload: Credentials) => post<AuthResponse>("/auth/login", payload),
  register: (payload: RegisterPayload) => post<AuthResponse>("/auth/register", payload),
  forgotPassword: (email: string) => post<{ message: string }>("/auth/forgot-password", { email }),
  resetPassword: (token: string, password: string) =>
    post<{ message: string }>("/auth/reset-password", { token, password }),
  me: () => get<User>("/auth/me"),
};

export const userService = {
  profile: () => get<User>("/users/me"),
  updateProfile: (payload: Partial<User>) => put<User>("/users/me", payload),
  changePassword: (currentPassword: string, newPassword: string) =>
    post<{ message: string }>("/users/me/password", { currentPassword, newPassword }),
};

export const farmService = {
  list: () => get<Farm[]>("/farms"),
  detail: (id: string) => get<Farm>(`/farms/${id}`),
  create: (payload: Omit<Farm, "id">) => post<Farm>("/farms", payload),
  update: (id: string, payload: Partial<Farm>) => put<Farm>(`/farms/${id}`, payload),
  remove: (id: string) => del<void>(`/farms/${id}`),
  fields: (farmId?: string) => get<Field[]>("/fields", farmId ? { farmId } : undefined),
  createField: (payload: Omit<Field, "id">) => post<Field>("/fields", payload),
};

export const cropService = {
  list: (params?: Record<string, unknown>) => get<Crop[]>("/crops", params),
  detail: (id: string) => get<Crop>(`/crops/${id}`),
  current: () => get<Crop[]>("/crops/current"),
};

export interface DiseaseAnalysisResult extends DiseaseScan {}

export const diseaseService = {
  analyze: (file: File, cropName?: string) => {
    const form = new FormData();
    form.append("image", file);
    if (cropName) form.append("cropName", cropName);
    return post<DiseaseAnalysisResult>("/disease/analyze", form);
  },
  history: (page = 1) => get<Paginated<DiseaseScan>>("/disease/scans", { page }),
};

export interface CropRecommendationInput {
  nitrogen: number;
  phosphorus: number;
  potassium: number;
  temperature: number;
  humidity: number;
  ph: number;
  rainfall: number;
  location: string;
}

export interface FertilizerInput {
  cropName: string;
  soilType: string;
  nitrogen: number;
  phosphorus: number;
  potassium: number;
  area: number;
}

export interface IrrigationInput {
  cropName: string;
  fieldId?: string;
  soilMoisture: number;
  temperature: number;
  humidity: number;
  rainfall: number;
}

export const recommendationService = {
  list: (params?: Record<string, unknown>) => get<Recommendation[]>("/recommendations", params),
  crop: (payload: CropRecommendationInput) => post<Recommendation[]>("/recommendations/crop", payload),
  fertilizer: (payload: FertilizerInput) => post<Recommendation>("/recommendations/fertilizer", payload),
  irrigation: (payload: IrrigationInput) => post<Recommendation>("/recommendations/irrigation", payload),
  profit: (payload: Record<string, unknown>) => post<Recommendation>("/recommendations/profit", payload),
};

export const weatherService = {
  current: (location?: string) => get<WeatherSnapshot>("/weather", location ? { location } : undefined),
};

export const marketService = {
  prices: (params?: Record<string, unknown>) => get<MarketPrice[]>("/market/prices", params),
  markets: () => get<{ id: string; name: string; state: string }[]>("/market/markets"),
  trend: (crop: string, market: string) =>
    get<{ date: string; price: number }[]>("/market/trend", { crop, market }),
};

export const schemeService = {
  list: (params?: Record<string, unknown>) => get<Scheme[]>("/schemes", params),
  detail: (id: string) => get<Scheme>(`/schemes/${id}`),
};

export const notificationService = {
  list: () => get<AppNotification[]>("/notifications"),
  markRead: (id: string) => post<void>(`/notifications/${id}/read`),
  markAllRead: () => post<void>("/notifications/read-all"),
  remove: (id: string) => del<void>(`/notifications/${id}`),
};

export const assistantService = {
  conversations: () => get<Conversation[]>("/assistant/conversations"),
  messages: (conversationId: string) =>
    get<ChatMessage[]>(`/assistant/conversations/${conversationId}/messages`),
  send: (conversationId: string | null, content: string) =>
    post<ChatMessage>("/assistant/messages", { conversationId, content }),
  clear: (conversationId: string) => del<void>(`/assistant/conversations/${conversationId}`),
};

export const historyService = {
  list: (params?: Record<string, unknown>) => get<Paginated<Recommendation>>("/history", params),
};

export const adminService = {
  stats: () => get<Record<string, number>>("/admin/stats"),
  userGrowth: () => get<{ month: string; users: number }[]>("/admin/reports/user-growth"),
  diseaseDistribution: () => get<{ name: string; value: number }[]>("/admin/reports/disease-distribution"),
  recommendationTrends: () => get<{ month: string; count: number }[]>("/admin/reports/recommendations"),
  users: (params?: Record<string, unknown>) => get<Paginated<User>>("/admin/users", params),
  farms: (params?: Record<string, unknown>) => get<Paginated<Farm>>("/admin/farms", params),
  crops: () => get<Crop[]>("/admin/crops"),
  diseaseScans: (params?: Record<string, unknown>) => get<Paginated<DiseaseScan>>("/admin/disease-scans", params),
  recommendations: (params?: Record<string, unknown>) =>
    get<Paginated<Recommendation>>("/admin/recommendations", params),
  markets: () => get<MarketPrice[]>("/admin/market-prices"),
  schemes: () => get<Scheme[]>("/admin/schemes"),
  notifications: () => get<AppNotification[]>("/admin/notifications"),
  auditLogs: (params?: Record<string, unknown>) =>
    get<Paginated<{ id: string; actor: string; action: string; target: string; createdAt: string }>>(
      "/admin/audit-logs",
      params,
    ),
  settings: () => get<Record<string, string>>("/admin/settings"),
};
