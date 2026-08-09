import { createFileRoute } from "@tanstack/react-router";

import { AdminTablePage } from "@/components/app/admin-table";
import { demoAuditLogs } from "@/data/demo";

export const Route = createFileRoute("/admin/audit-logs")({
  head: () => ({
    meta: [
      { title: "Audit logs — Smart Farmer AI admin" },
      { name: "description", content: "Administrative actions on the platform." },
      { name: "robots", content: "noindex" },
    ],
  }),
  component: Page,
});

function Page() {
  return (
    <AdminTablePage
      title="Audit logs"
      description="Administrative actions on the platform."
      columns={['Actor','Action','Target','Time']}
      rows={demoAuditLogs.map((l) => [l.actor, l.action, l.target, l.at])}
    />
  );
}
