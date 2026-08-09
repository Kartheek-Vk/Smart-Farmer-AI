import { createFileRoute } from "@tanstack/react-router";

import { AdminTablePage } from "@/components/app/admin-table";
import { demoCrops } from "@/data/demo";

export const Route = createFileRoute("/admin/crops")({
  head: () => ({
    meta: [
      { title: "Crops — Smart Farmer AI admin" },
      { name: "description", content: "Crop library available to farmers." },
      { name: "robots", content: "noindex" },
    ],
  }),
  component: Page,
});

function Page() {
  return (
    <AdminTablePage
      title="Crops"
      description="Crop library available to farmers."
      columns={['Crop','Season','Duration (days)']}
      rows={demoCrops.map((c) => [c.name, c.season, c.durationDays ?? '—'])}
    />
  );
}
