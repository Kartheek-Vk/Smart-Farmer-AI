import { createFileRoute } from "@tanstack/react-router";

import { PageHeader } from "@/components/common/page";
import { DataCard } from "@/components/app/list-primitives";
import { Label } from "@/components/ui/label";
import { Switch } from "@/components/ui/switch";

export const Route = createFileRoute("/admin/settings")({
  head: () => ({
    meta: [
      { title: "Platform settings — Smart Farmer AI admin" },
      { name: "description", content: "Feature flags and platform configuration." },
      { name: "robots", content: "noindex" },
    ],
  }),
  component: AdminSettingsPage,
});

const FLAGS = [
  { id: "flag-disease", label: "Disease detection module", on: true },
  { id: "flag-profit", label: "Profit prediction module", on: true },
  { id: "flag-assistant", label: "AI assistant", on: true },
  { id: "flag-registration", label: "Open registration", on: true },
  { id: "flag-maintenance", label: "Maintenance mode", on: false },
];

function AdminSettingsPage() {
  return (
    <div className="space-y-5">
      <PageHeader title="Platform settings" description="Control which modules farmers can reach." />
      <DataCard>
        <ul className="space-y-3">
          {FLAGS.map((flag) => (
            <li key={flag.id} className="flex items-center justify-between gap-3">
              <Label htmlFor={flag.id} className="text-sm font-normal">{flag.label}</Label>
              <Switch id={flag.id} defaultChecked={flag.on} />
            </li>
          ))}
        </ul>
      </DataCard>
    </div>
  );
}
