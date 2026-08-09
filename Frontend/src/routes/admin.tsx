import { createFileRoute, Outlet } from "@tanstack/react-router";

import { AdminShell } from "@/layouts/admin-shell";

export const Route = createFileRoute("/admin")({
  head: () => ({
    meta: [
      { title: "Admin console — Smart Farmer AI" },
      { name: "description", content: "Manage users, farms, crops, markets, schemes and platform settings." },
      { name: "robots", content: "noindex" },
    ],
  }),
  component: AdminLayout,
});

function AdminLayout() {
  return (
    <AdminShell>
      <Outlet />
    </AdminShell>
  );
}
