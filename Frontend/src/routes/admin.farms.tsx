import { createFileRoute } from "@tanstack/react-router";

import { AdminTablePage } from "@/components/app/admin-table";
import { demoFarms } from "@/data/demo";

export const Route = createFileRoute("/admin/farms")({
  head: () => ({
    meta: [
      { title: "Farms — Smart Farmer AI admin" },
      { name: "description", content: "Registered farms across the platform." },
      { name: "robots", content: "noindex" },
    ],
  }),
  component: Page,
});

function Page() {
  return (
    <AdminTablePage
      title="Farms"
      description="Registered farms across the platform."
      columns={['Farm','Location','Area','Soil','Irrigation']}
      rows={demoFarms.map((f) => [f.name, f.location, `${f.area} ${f.areaUnit.toLowerCase()}`, f.soilType, f.irrigationType])}
    />
  );
}
