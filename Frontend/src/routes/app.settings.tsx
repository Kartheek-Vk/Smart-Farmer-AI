import { createFileRoute } from "@tanstack/react-router";

import { PageHeader } from "@/components/common/page";
import { DataCard } from "@/components/app/list-primitives";
import { LanguageSelector } from "@/components/common/language-selector";
import { ThemeToggle } from "@/components/common/theme-toggle";
import { InstallAppButton } from "@/components/common/install-prompt";
import { Switch } from "@/components/ui/switch";
import { Label } from "@/components/ui/label";

export const Route = createFileRoute("/app/settings")({
  head: () => ({
    meta: [
      { title: "Settings — Smart Farmer AI" },
      { name: "description", content: "Language, appearance and notification preferences." },
      { name: "robots", content: "noindex" },
    ],
  }),
  component: SettingsPage,
});

const TOGGLES = [
  { id: "weather-alerts", label: "Weather alerts", defaultOn: true },
  { id: "market-alerts", label: "Market price alerts", defaultOn: true },
  { id: "scheme-alerts", label: "New scheme announcements", defaultOn: false },
  { id: "ai-summary", label: "Weekly AI summary", defaultOn: true },
];

function SettingsPage() {
  return (
    <div className="space-y-5">
      <PageHeader title="Settings" description="Make the app work the way you do." />
      <div className="grid gap-4 lg:grid-cols-2">
        <DataCard>
          <h2 className="font-semibold">Language &amp; appearance</h2>
          <div className="mt-4 flex flex-wrap items-center gap-3">
            <LanguageSelector />
            <ThemeToggle />
          </div>
        </DataCard>
        <DataCard>
          <h2 className="font-semibold">Notifications</h2>
          <ul className="mt-4 space-y-3">
            {TOGGLES.map((toggle) => (
              <li key={toggle.id} className="flex items-center justify-between gap-3">
                <Label htmlFor={toggle.id} className="text-sm font-normal">
                  {toggle.label}
                </Label>
                <Switch id={toggle.id} defaultChecked={toggle.defaultOn} />
              </li>
            ))}
          </ul>
        </DataCard>
        <DataCard className="lg:col-span-2">
          <h2 className="font-semibold">Install the app</h2>
          <p className="mt-1 text-sm text-muted-foreground">
            Add Smart Farmer AI to your home screen for faster access.
          </p>
          <div className="mt-4">
            <InstallAppButton />
          </div>
        </DataCard>
      </div>
    </div>
  );
}
