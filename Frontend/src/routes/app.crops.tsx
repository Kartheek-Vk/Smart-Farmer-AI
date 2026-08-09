import { useMemo, useState } from "react";
import { createFileRoute } from "@tanstack/react-router";
import { Leaf } from "lucide-react";

import { PageHeader } from "@/components/common/page";
import { IconBubble, StatusBadge } from "@/components/common/cards";
import { DataCard } from "@/components/app/list-primitives";
import { SearchBar } from "@/components/common/controls";
import { EmptyState } from "@/components/common/states";
import { demoCrops } from "@/data/demo";

export const Route = createFileRoute("/app/crops")({
  head: () => ({
    meta: [
      { title: "Crops — Smart Farmer AI" },
      { name: "description", content: "Crop library with season and duration details." },
      { name: "robots", content: "noindex" },
    ],
  }),
  component: CropsPage,
});

function CropsPage() {
  const [query, setQuery] = useState("");
  const crops = useMemo(
    () => demoCrops.filter((c) => c.name.toLowerCase().includes(query.trim().toLowerCase())),
    [query],
  );

  return (
    <div className="space-y-5">
      <PageHeader title="Crops" description="Crops you grow and crops you could grow." />
      <SearchBar value={query} onChange={setQuery} label="crops" placeholder="Search crops" className="max-w-sm" />
      {crops.length === 0 ? (
        <EmptyState title="No crops found" body="Try a different crop name." />
      ) : (
        <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
          {crops.map((crop) => (
            <DataCard key={crop.id}>
              <div className="flex items-start gap-3">
                <IconBubble icon={Leaf} tone="success" />
                <div className="min-w-0 flex-1">
                  <div className="flex items-center justify-between gap-2">
                    <h2 className="truncate font-semibold">{crop.name}</h2>
                    <StatusBadge status={crop.season} tone="primary" />
                  </div>
                  <p className="mt-1 text-sm text-muted-foreground">{crop.description}</p>
                  <p className="mt-2 text-xs text-muted-foreground">Duration: {crop.durationDays} days</p>
                </div>
              </div>
            </DataCard>
          ))}
        </div>
      )}
    </div>
  );
}
