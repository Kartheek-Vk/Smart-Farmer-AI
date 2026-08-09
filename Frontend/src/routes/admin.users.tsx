import { createFileRoute } from "@tanstack/react-router";

import { AdminTablePage } from "@/components/app/admin-table";
import { demoAdminUsers } from "@/data/demo";

export const Route = createFileRoute("/admin/users")({
  head: () => ({
    meta: [
      { title: "Users — Smart Farmer AI admin" },
      { name: "description", content: "All registered accounts and their roles." },
      { name: "robots", content: "noindex" },
    ],
  }),
  component: Page,
});

function Page() {
  return (
    <AdminTablePage
      title="Users"
      description="All registered accounts and their roles."
      columns={['Name','Email','Role','Joined']}
      rows={demoAdminUsers.map((u) => [u.name, u.email, u.role, u.createdAt ?? '—'])}
    />
  );
}
