import { useMemo, useState } from "react";
import { createFileRoute } from "@tanstack/react-router";
import { TrendingDown, TrendingUp } from "lucide-react";

import { PageHeader } from "@/components/common/page";
import { SearchBar } from "@/components/common/controls";
import { DataCard } from "@/components/app/list-primitives";
import { EmptyState } from "@/components/common/states";
import { demoMarketPrices } from "@/data/demo";

export const Route = createFileRoute("/app/market")({
  head: () => ({
    meta: [
      { title: "Market prices — Smart Farmer AI" },
      { name: "description", content: "Daily mandi prices with day-on-day change and weekly trend." },
      { name: "robots", content: "noindex" },
    ],
  }),
  component: MarketPage,
});

function MarketPage() {
  const [query, setQuery] = useState("");
  const prices = useMemo(() => {
    const q = query.trim().toLowerCase();
    return demoMarketPrices.filter(
      (p) => p.crop.toLowerCase().includes(q) || p.market.toLowerCase().includes(q),
    );
  }, [query]);

  return (
    <div className="space-y-5">
      <PageHeader title="Market prices" description="Today's mandi rates and how they moved." />
      <SearchBar value={query} onChange={setQuery} label="market" placeholder="Search crop or mandi" className="max-w-sm" />
      {prices.length === 0 ? (
        <EmptyState title="No prices found" body="Try another crop or market name." />
      ) : (
        <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
          {prices.map((price) => {
            const up = price.changePercent >= 0;
            const max = Math.max(...(price.history ?? []).map((h) => h.price), 1);
            return (
              <DataCard key={price.id}>
                <div className="flex items-start justify-between gap-3">
                  <div className="min-w-0">
                    <h2 className="truncate font-semibold">{price.crop}</h2>
                    <p className="truncate text-sm text-muted-foreground">
                      {price.market}, {price.state}
                    </p>
                  </div>
                  <div className="shrink-0 text-right">
                    <p className="text-lg font-bold">₹{price.todayPrice.toLocaleString("en-IN")}</p>
                    <p className={up ? "flex items-center justify-end gap-1 text-xs text-success" : "flex items-center justify-end gap-1 text-xs text-danger"}>
                      {up ? <TrendingUp className="size-3" aria-hidden="true" /> : <TrendingDown className="size-3" aria-hidden="true" />}
                      {price.changePercent}%
                    </p>
                  </div>
                </div>
                <div className="mt-4 flex h-20 items-end gap-1" aria-hidden="true">
                  {(price.history ?? []).map((point) => (
                    <span
                      key={point.date}
                      className="flex-1 rounded-t bg-market/70"
                      style={{ height: `${(point.price / max) * 100}%` }}
                    />
                  ))}
                </div>
                <p className="mt-2 text-xs text-muted-foreground">
                  Yesterday ₹{price.yesterdayPrice.toLocaleString("en-IN")} · {price.unit}
                </p>
              </DataCard>
            );
          })}
        </div>
      )}
    </div>
  );
}
