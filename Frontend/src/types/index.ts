export type UserRole = "FARMER" | "EXPERT" | "DEALER" | "NGO" | "GOVERNMENT" | "ADMIN";

export interface User {
  id: string;
  name: string;
  email: string;
  phone?: string;
  role: UserRole;
  language?: string;
  avatarUrl?: string;
  createdAt?: string;
}

export interface Farm {
  id: string;
  name: string;
  location: string;
  area: number;
  areaUnit: "ACRE" | "HECTARE" | "SQ_M";
  soilType: string;
  irrigationType: string;
  ownership: "OWNED" | "LEASED" | "SHARED";
  createdAt?: string;
}

export interface Field {
  id: string;
  farmId: string;
  name: string;
  area: number;
  areaUnit: Farm["areaUnit"];
  currentCrop?: string;
  soilType?: string;
}

export interface Crop {
  id: string;
  name: string;
  season: string;
  durationDays?: number;
  imageUrl?: string;
  description?: string;
}

export interface DiseaseScan {
  id: string;
  cropName: string;
  diseaseName: string;
  confidence: number;
  symptoms: string[];
  actions: string[];
  prevention: string[];
  imageUrl?: string;
  createdAt: string;
}

export interface Recommendation {
  id: string;
  type: "CROP" | "FERTILIZER" | "IRRIGATION" | "PROFIT";
  title: string;
  summary: string;
  createdAt: string;
  details?: Record<string, string | number>;
}

export interface WeatherSnapshot {
  location: string;
  temperatureC: number;
  humidity: number;
  windKph: number;
  rainMm: number;
  condition: string;
  advice?: string;
  forecast: { date: string; minC: number; maxC: number; condition: string; rainMm: number }[];
  alerts: { id: string; title: string; severity: "LOW" | "MEDIUM" | "HIGH"; body: string }[];
}

export interface MarketPrice {
  id: string;
  crop: string;
  market: string;
  state: string;
  todayPrice: number;
  yesterdayPrice: number;
  unit: string;
  changePercent: number;
  history?: { date: string; price: number }[];
}

export interface Scheme {
  id: string;
  name: string;
  category: string;
  state: string;
  summary: string;
  eligibility: string[];
  startDate?: string;
  endDate?: string;
  applicationUrl?: string;
}

export interface AppNotification {
  id: string;
  title: string;
  body: string;
  read: boolean;
  type: "WEATHER" | "MARKET" | "SCHEME" | "SYSTEM" | "AI";
  createdAt: string;
}

export interface ChatMessage {
  id: string;
  role: "user" | "assistant";
  content: string;
  createdAt: string;
}

export interface Conversation {
  id: string;
  title: string;
  updatedAt: string;
}

export interface Paginated<T> {
  items: T[];
  page: number;
  pageSize: number;
  total: number;
}
