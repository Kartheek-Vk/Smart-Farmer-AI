import { createFileRoute, Outlet } from "@tanstack/react-router";

import { AppShell } from "@/layouts/app-shell";

export const Route = createFileRoute("/app")({
  head: () => ({
    meta: [
      { title: "Farm workspace — Smart Farmer AI" },
      { name: "description", content: "Your farms, scans, recommendations, weather and market prices." },
      { name: "robots", content: "noindex" },
    ],
  }),
  component: AppLayout,
});

function AppLayout() {
  return (
    <AppShell>
      <Outlet />
    </AppShell>
  );
}
