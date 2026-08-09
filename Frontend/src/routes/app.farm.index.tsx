import { createFileRoute, Link } from "@tanstack/react-router";
import { MapPin, Plus } from "lucide-react";

import { PageHeader } from "@/components/common/page";
import { IconBubble, StatusBadge } from "@/components/common/cards";
import { DataCard, DetailRow } from "@/components/app/list-primitives";
import { Button } from "@/components/ui/button";
import { demoFarms } from "@/data/demo";

export const Route = createFileRoute("/app/farm/")({
  head: () => ({
    meta: [
      { title: "My farms — Smart Farmer AI" },
      { name: "description", content: "All your registered farms with soil, area and irrigation details." },
      { name: "robots", content: "noindex" },
    ],
  }),
  component: FarmsPage,
});

function FarmsPage() {
  return (
    <div className="space-y-5">
      <PageHeader
        title="My farms"
        description="Every plot you manage, with its soil and irrigation profile."
        actions={
          <Button className="min-h-11">
            <Plus className="size-4" aria-hidden="true" />
            Add farm
          </Button>
        }
      />
      <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
        {demoFarms.map((farm) => (
          <DataCard key={farm.id}>
            <div className="flex items-start gap-3">
              <IconBubble icon={MapPin} />
              <div className="min-w-0 flex-1">
                <div className="flex items-center justify-between gap-2">
                  <h2 className="truncate font-semibold">{farm.name}</h2>
                  <StatusBadge status={farm.ownership} tone="success" />
                </div>
                <p className="truncate text-sm text-muted-foreground">{farm.location}</p>
              </div>
            </div>
            <div className="mt-4">
              <DetailRow label="Area" value={`${farm.area} ${farm.areaUnit.toLowerCase()}`} />
              <DetailRow label="Soil" value={farm.soilType} />
              <DetailRow label="Irrigation" value={farm.irrigationType} />
            </div>
            <Button asChild variant="outline" className="mt-4 min-h-11 w-full">
              <Link to="/app/farm/$farmId" params={{ farmId: farm.id }}>
                View farm
              </Link>
            </Button>
          </DataCard>
        ))}
      </div>
    </div>
  );
}
