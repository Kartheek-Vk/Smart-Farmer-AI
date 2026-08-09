import { createFileRoute } from "@tanstack/react-router";

import { AdminTablePage } from "@/components/app/admin-table";
import { demoNotifications } from "@/data/demo";

export const Route = createFileRoute("/admin/notifications")({
  head: () => ({
    meta: [
      { title: "Notifications — Smart Farmer AI admin" },
      { name: "description", content: "Broadcasts sent to farmers." },
      { name: "robots", content: "noindex" },
    ],
  }),
  component: Page,
});

function Page() {
  return (
    <AdminTablePage
      title="Notifications"
      description="Broadcasts sent to farmers."
      columns={['Title','Type','Sent']}
      rows={demoNotifications.map((n) => [n.title, n.type, new Date(n.createdAt).toLocaleDateString()])}
    />
  );
}
