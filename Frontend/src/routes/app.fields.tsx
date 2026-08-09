import { createFileRoute } from "@tanstack/react-router";
import { Plus, Sprout } from "lucide-react";

import { PageHeader } from "@/components/common/page";
import { IconBubble } from "@/components/common/cards";
import { DataCard } from "@/components/app/list-primitives";
import { Button } from "@/components/ui/button";
import { demoFarms, demoFields } from "@/data/demo";

export const Route = createFileRoute("/app/fields")({
  head: () => ({
    meta: [
      { title: "Fields — Smart Farmer AI" },
      { name: "description", content: "All fields across your farms with current crops." },
      { name: "robots", content: "noindex" },
    ],
  }),
  component: FieldsPage,
});

function FieldsPage() {
  return (
    <div className="space-y-5">
      <PageHeader
        title="Fields"
        description="Plot-level view across every farm."
        actions={
          <Button className="min-h-11">
            <Plus className="size-4" aria-hidden="true" />
            Add field
          </Button>
        }
      />
      <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
        {demoFields.map((field) => {
          const farm = demoFarms.find((f) => f.id === field.farmId);
          return (
            <DataCard key={field.id}>
              <div className="flex items-center gap-3">
                <IconBubble icon={Sprout} tone="success" />
                <div className="min-w-0">
                  <h2 className="truncate font-semibold">{field.name}</h2>
                  <p className="truncate text-sm text-muted-foreground">{farm?.name}</p>
                </div>
              </div>
              <dl className="mt-4 grid grid-cols-2 gap-3 text-sm">
                <div>
                  <dt className="text-xs text-muted-foreground">Area</dt>
                  <dd className="font-medium">{field.area} {field.areaUnit.toLowerCase()}</dd>
                </div>
                <div>
                  <dt className="text-xs text-muted-foreground">Crop</dt>
                  <dd className="font-medium">{field.currentCrop ?? "—"}</dd>
                </div>
                <div className="col-span-2">
                  <dt className="text-xs text-muted-foreground">Soil</dt>
                  <dd className="font-medium">{field.soilType ?? "—"}</dd>
                </div>
              </dl>
            </DataCard>
          );
        })}
      </div>
    </div>
  );
}
