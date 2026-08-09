import { createFileRoute } from "@tanstack/react-router";

import { PageHeader } from "@/components/common/page";
import { StatusBadge } from "@/components/common/cards";
import { DataCard } from "@/components/app/list-primitives";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { demoRecommendations } from "@/data/demo";

export const Route = createFileRoute("/app/recommendations")({
  head: () => ({
    meta: [
      { title: "Recommendations — Smart Farmer AI" },
      { name: "description", content: "Crop, fertilizer, irrigation and profit recommendations for your fields." },
      { name: "robots", content: "noindex" },
    ],
  }),
  component: RecommendationsPage,
});

const TYPES = ["ALL", "CROP", "FERTILIZER", "IRRIGATION", "PROFIT"] as const;

function RecommendationsPage() {
  return (
    <div className="space-y-5">
      <PageHeader title="Recommendations" description="Generated from your soil, weather and market data." />
      <Tabs defaultValue="ALL">
        <TabsList className="flex w-full flex-wrap justify-start">
          {TYPES.map((type) => (
            <TabsTrigger key={type} value={type} className="min-h-10">
              {type.charAt(0) + type.slice(1).toLowerCase()}
            </TabsTrigger>
          ))}
        </TabsList>
        {TYPES.map((type) => {
          const items = demoRecommendations.filter((r) => type === "ALL" || r.type === type);
          return (
            <TabsContent key={type} value={type} className="mt-4 space-y-4">
              {items.map((rec) => (
                <DataCard key={rec.id}>
                  <div className="flex items-start justify-between gap-3">
                    <div className="min-w-0">
                      <h2 className="font-semibold">{rec.title}</h2>
                      <p className="mt-1 text-sm text-muted-foreground">{rec.summary}</p>
                    </div>
                    <StatusBadge status={rec.type} tone="primary" />
                  </div>
                  {rec.details ? (
                    <dl className="mt-4 grid grid-cols-2 gap-3 sm:grid-cols-3">
                      {Object.entries(rec.details).map(([key, value]) => (
                        <div key={key} className="rounded-xl bg-muted/60 p-3">
                          <dt className="text-xs text-muted-foreground">{key}</dt>
                          <dd className="font-semibold">{value}</dd>
                        </div>
                      ))}
                    </dl>
                  ) : null}
                </DataCard>
              ))}
            </TabsContent>
          );
        })}
      </Tabs>
    </div>
  );
}
