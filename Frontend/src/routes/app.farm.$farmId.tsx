import { createFileRoute, Link, notFound } from "@tanstack/react-router";
import { ArrowLeft, Sprout } from "lucide-react";

import { PageHeader } from "@/components/common/page";
import { DataCard, DetailRow } from "@/components/app/list-primitives";
import { IconBubble } from "@/components/common/cards";
import { Button } from "@/components/ui/button";
import { demoFarms, demoFields } from "@/data/demo";

export const Route = createFileRoute("/app/farm/$farmId")({
  loader: ({ params }: { params: { farmId: string } }) => {
    const farm = demoFarms.find((f) => f.id === params.farmId);
    if (!farm) throw notFound();
    return { farm, fields: demoFields.filter((f) => f.farmId === farm.id) };
  },
  head: ({ loaderData }) => ({
    meta: [
      { title: loaderData ? `${loaderData.farm.name} — Smart Farmer AI` : "Farm — Smart Farmer AI" },
      { name: "description", content: "Farm profile, fields and soil details." },
      { name: "robots", content: "noindex" },
    ],
  }),
  component: FarmDetailPage,
});

function FarmDetailPage() {
  const { farm, fields } = Route.useLoaderData();

  return (
    <div className="space-y-5">
      <Button asChild variant="ghost" className="min-h-11 -ml-2">
        <Link to="/app/farm">
          <ArrowLeft className="size-4" aria-hidden="true" />
          All farms
        </Link>
      </Button>
      <PageHeader title={farm.name} description={farm.location} />
      <div className="grid gap-4 lg:grid-cols-3">
        <DataCard>
          <h2 className="font-semibold">Farm profile</h2>
          <div className="mt-3">
            <DetailRow label="Area" value={`${farm.area} ${farm.areaUnit.toLowerCase()}`} />
            <DetailRow label="Soil type" value={farm.soilType} />
            <DetailRow label="Irrigation" value={farm.irrigationType} />
            <DetailRow label="Ownership" value={farm.ownership} />
            <DetailRow label="Added" value={farm.createdAt ?? "—"} />
          </div>
        </DataCard>
        <DataCard className="lg:col-span-2">
          <h2 className="font-semibold">Fields ({fields.length})</h2>
          <ul className="mt-3 space-y-3">
            {fields.map((field: (typeof demoFields)[number]) => (
              <li key={field.id} className="flex items-center gap-3 rounded-xl border border-border p-3">
                <IconBubble icon={Sprout} tone="success" />
                <div className="min-w-0 flex-1">
                  <p className="truncate font-medium">{field.name}</p>
                  <p className="text-xs text-muted-foreground">
                    {field.area} {field.areaUnit.toLowerCase()} · {field.currentCrop ?? "No crop"}
                  </p>
                </div>
              </li>
            ))}
          </ul>
        </DataCard>
      </div>
    </div>
  );
}
