import { createFileRoute } from "@tanstack/react-router";

import { PageHeader } from "@/components/common/page";
import { DataCard } from "@/components/app/list-primitives";
import { StatusBadge } from "@/components/common/cards";
import { demoRecommendations, demoScans } from "@/data/demo";

export const Route = createFileRoute("/app/history")({
  head: () => ({
    meta: [
      { title: "History — Smart Farmer AI" },
      { name: "description", content: "Every scan and recommendation, newest first." },
      { name: "robots", content: "noindex" },
    ],
  }),
  component: HistoryPage,
});

function HistoryPage() {
  const entries = [
    ...demoScans.map((s) => ({ id: s.id, title: s.diseaseName, type: "SCAN", body: s.cropName, at: s.createdAt })),
    ...demoRecommendations.map((r) => ({ id: r.id, title: r.title, type: r.type, body: r.summary, at: r.createdAt })),
  ].sort((a, b) => (a.at < b.at ? 1 : -1));

  return (
    <div className="space-y-5">
      <PageHeader title="History" description="A record of what the AI told you, and when." />
      <DataCard>
        <ol className="space-y-3">
          {entries.map((entry) => (
            <li key={`${entry.type}-${entry.id}`} className="rounded-xl border border-border p-3">
              <div className="flex items-center justify-between gap-2">
                <p className="min-w-0 truncate font-medium">{entry.title}</p>
                <StatusBadge status={entry.type} tone="primary" />
              </div>
              <p className="mt-1 text-sm text-muted-foreground">{entry.body}</p>
              <p className="mt-1 text-xs text-muted-foreground">{new Date(entry.at).toLocaleString()}</p>
            </li>
          ))}
        </ol>
      </DataCard>
    </div>
  );
}
