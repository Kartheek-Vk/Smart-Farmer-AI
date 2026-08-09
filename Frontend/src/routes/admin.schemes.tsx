import { createFileRoute } from "@tanstack/react-router";

import { AdminTablePage } from "@/components/app/admin-table";
import { demoSchemes } from "@/data/demo";

export const Route = createFileRoute("/admin/schemes")({
  head: () => ({
    meta: [
      { title: "Schemes — Smart Farmer AI admin" },
      { name: "description", content: "Published government schemes." },
      { name: "robots", content: "noindex" },
    ],
  }),
  component: Page,
});

function Page() {
  return (
    <AdminTablePage
      title="Schemes"
      description="Published government schemes."
      columns={['Scheme','Category','State']}
      rows={demoSchemes.map((s) => [s.name, s.category, s.state])}
    />
  );
}
