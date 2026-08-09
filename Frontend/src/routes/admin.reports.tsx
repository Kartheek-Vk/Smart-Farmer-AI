import { createFileRoute } from "@tanstack/react-router";

import { PageHeader } from "@/components/common/page";
import { DataCard } from "@/components/app/list-primitives";
import { demoMarketPrices } from "@/data/demo";

export const Route = createFileRoute("/admin/reports")({
  head: () => ({
    meta: [
      { title: "Reports — Smart Farmer AI admin" },
      { name: "description", content: "Usage and market trend reports." },
      { name: "robots", content: "noindex" },
    ],
  }),
  component: ReportsPage,
});

function ReportsPage() {
  const max = Math.max(...demoMarketPrices.map((p) => p.todayPrice));
  return (
    <div className="space-y-5">
      <PageHeader title="Reports" description="Platform usage and price movement." />
      <DataCard>
        <h2 className="font-semibold">Today's prices by crop</h2>
        <ul className="mt-4 space-y-3">
          {demoMarketPrices.map((price) => (
            <li key={price.id}>
              <div className="flex items-center justify-between text-sm">
                <span>{price.crop}</span>
                <span className="font-medium">₹{price.todayPrice.toLocaleString("en-IN")}</span>
              </div>
              <div className="mt-1 h-2 rounded-full bg-muted">
                <div className="h-2 rounded-full bg-primary" style={{ width: `${(price.todayPrice / max) * 100}%` }} />
              </div>
            </li>
          ))}
        </ul>
      </DataCard>
    </div>
  );
}
