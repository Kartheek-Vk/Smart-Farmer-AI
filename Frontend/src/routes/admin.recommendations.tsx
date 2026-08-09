import { createFileRoute } from "@tanstack/react-router";

import { AdminTablePage } from "@/components/app/admin-table";
import { demoRecommendations } from "@/data/demo";

export const Route = createFileRoute("/admin/recommendations")({
  head: () => ({
    meta: [
      { title: "Recommendations — Smart Farmer AI admin" },
      { name: "description", content: "Advice generated for farmers." },
      { name: "robots", content: "noindex" },
    ],
  }),
  component: Page,
});

function Page() {
  return (
    <AdminTablePage
      title="Recommendations"
      description="Advice generated for farmers."
      columns={['Title','Type','Date']}
      rows={demoRecommendations.map((r) => [r.title, r.type, new Date(r.createdAt).toLocaleDateString()])}
    />
  );
}
