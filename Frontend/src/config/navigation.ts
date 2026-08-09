import type { LucideIcon } from "lucide-react";
import {
  Bell,
  Bot,
  BarChart3,
  Bug,
  CloudSun,
  Droplets,
  FileText,
  FlaskConical,
  Gauge,
  Home,
  Landmark,
  Leaf,
  LineChart,
  ListChecks,
  MapPin,
  Settings,
  ShieldCheck,
  ShoppingBasket,
  Sprout,
  Store,
  User,
  Users,
} from "lucide-react";

export interface NavItem {
  to: string;
  labelKey: string;
  icon: LucideIcon;
  exact?: boolean;
}

export const PUBLIC_NAV: { to: string; labelKey: string }[] = [
  { to: "/", labelKey: "nav.home" },
  { to: "/features", labelKey: "nav.features" },
  { to: "/ai-modules", labelKey: "nav.aiModules" },
  { to: "/how-it-works", labelKey: "nav.howItWorks" },
  { to: "/about", labelKey: "nav.about" },
  { to: "/contact", labelKey: "nav.contact" },
];

export const APP_NAV: NavItem[] = [
  { to: "/app", labelKey: "app.dashboard", icon: Home, exact: true },
  { to: "/app/farm", labelKey: "app.myFarms", icon: MapPin },
  { to: "/app/fields", labelKey: "app.fields", icon: Sprout },
  { to: "/app/crops", labelKey: "app.crops", icon: Leaf },
  { to: "/app/disease", labelKey: "app.disease", icon: Bug },
  { to: "/app/recommendations", labelKey: "app.recommendations", icon: ListChecks },
  { to: "/app/weather", labelKey: "app.weather", icon: CloudSun },
  { to: "/app/market", labelKey: "app.market", icon: ShoppingBasket },
  { to: "/app/schemes", labelKey: "app.schemes", icon: Landmark },
  { to: "/app/notifications", labelKey: "app.notifications", icon: Bell },
  { to: "/app/assistant", labelKey: "app.assistant", icon: Bot },
  { to: "/app/history", labelKey: "app.history", icon: FileText },
  { to: "/app/profile", labelKey: "app.profile", icon: User },
  { to: "/app/settings", labelKey: "app.settings", icon: Settings },
];

export const MOBILE_TAB_NAV: NavItem[] = [
  { to: "/app", labelKey: "app.dashboard", icon: Home, exact: true },
  { to: "/app/farm", labelKey: "app.myFarms", icon: MapPin },
  { to: "/app/disease", labelKey: "nav.aiModules", icon: Bug },
  { to: "/app/market", labelKey: "app.market", icon: ShoppingBasket },
  { to: "/app/profile", labelKey: "app.profile", icon: User },
];

export const ADMIN_NAV: NavItem[] = [
  { to: "/admin", labelKey: "admin.dashboard", icon: Gauge, exact: true },
  { to: "/admin/users", labelKey: "admin.users", icon: Users },
  { to: "/admin/farms", labelKey: "admin.farms", icon: MapPin },
  { to: "/admin/crops", labelKey: "admin.crops", icon: Leaf },
  { to: "/admin/disease", labelKey: "admin.disease", icon: Bug },
  { to: "/admin/recommendations", labelKey: "admin.recommendations", icon: ListChecks },
  { to: "/admin/markets", labelKey: "admin.markets", icon: Store },
  { to: "/admin/schemes", labelKey: "admin.schemes", icon: Landmark },
  { to: "/admin/notifications", labelKey: "admin.notifications", icon: Bell },
  { to: "/admin/reports", labelKey: "admin.reports", icon: BarChart3 },
  { to: "/admin/audit-logs", labelKey: "admin.auditLogs", icon: ShieldCheck },
  { to: "/admin/settings", labelKey: "admin.settings", icon: Settings },
];

export type QuickActionTone = "primary" | "weather" | "market" | "warning" | "danger" | "success";

export const QUICK_ACTIONS: { to: string; label: string; icon: LucideIcon; tone: QuickActionTone }[] = [
  { to: "/app/disease", label: "Scan Disease", icon: Bug, tone: "danger" },
  { to: "/app/recommendations", label: "Crop Advice", icon: Sprout, tone: "primary" },
  { to: "/app/recommendations", label: "Fertilizer", icon: FlaskConical, tone: "warning" },
  { to: "/app/recommendations", label: "Irrigation", icon: Droplets, tone: "weather" },
  { to: "/app/market", label: "Market Prices", icon: LineChart, tone: "market" },
  { to: "/app/assistant", label: "AI Assistant", icon: Bot, tone: "primary" },
  { to: "/app/schemes", label: "Schemes", icon: Landmark, tone: "market" },
  { to: "/app/weather", label: "Weather", icon: CloudSun, tone: "weather" },
];
