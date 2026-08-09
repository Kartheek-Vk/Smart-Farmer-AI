import { createFileRoute } from "@tanstack/react-router";
import { BarChart3, Bug, MapPin, Users } from "lucide-react";

import { PageHeader } from "@/components/common/page";
import { StatCard } from "@/components/common/cards";
import { DataCard } from "@/components/app/list-primitives";
import { demoAdminStats, demoAdminUsers, demoAuditLogs } from "@/data/demo";

export const Route = createFileRoute("/admin/")({
  head: () => ({
    meta: [
      { title: "Admin dashboard — Smart Farmer AI" },
      { name: "description", content: "Platform overview: users, farms, scans and recommendations." },
      { name: "robots", content: "noindex" },
    ],
  }),
  component: AdminDashboard,
});

const ICONS = [Users, MapPin, Bug, BarChart3];

function AdminDashboard() {
  return (
    <div className="space-y-6">
      <PageHeader title="Admin dashboard" description="Platform health at a glance." />
      <div className="grid grid-cols-2 gap-4 lg:grid-cols-4">
        {demoAdminStats.map((stat, i) => (
          <StatCard
            key={stat.id}
            label={stat.label}
            value={stat.value}
            hint={stat.hint}
            icon={ICONS[i] ?? Users}
            tone={i % 2 === 0 ? "primary" : "market"}
          />
        ))}
      </div>
      <div className="grid gap-4 lg:grid-cols-2">
        <DataCard>
          <h2 className="font-semibold">Newest users</h2>
          <ul className="mt-3 space-y-3">
            {demoAdminUsers.slice(0, 5).map((user) => (
              <li key={user.id} className="flex items-center justify-between gap-3 rounded-xl border border-border p-3">
                <div className="min-w-0">
                  <p className="truncate font-medium">{user.name}</p>
                  <p className="truncate text-xs text-muted-foreground">{user.email}</p>
                </div>
                <span className="shrink-0 text-xs font-semibold text-muted-foreground">{user.role}</span>
              </li>
            ))}
          </ul>
        </DataCard>
        <DataCard>
          <h2 className="font-semibold">Recent activity</h2>
          <ul className="mt-3 space-y-3">
            {demoAuditLogs.map((log) => (
              <li key={log.id} className="rounded-xl border border-border p-3">
                <p className="text-sm font-medium">{log.action}</p>
                <p className="text-xs text-muted-foreground">{log.actor} · {log.at}</p>
              </li>
            ))}
          </ul>
        </DataCard>
      </div>
    </div>
  );
}
