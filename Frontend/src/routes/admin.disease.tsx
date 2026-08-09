import { createFileRoute } from "@tanstack/react-router";

import { AdminTablePage } from "@/components/app/admin-table";
import { demoScans } from "@/data/demo";

export const Route = createFileRoute("/admin/disease")({
  head: () => ({
    meta: [
      { title: "Disease scans — Smart Farmer AI admin" },
      { name: "description", content: "Recent AI disease detections." },
      { name: "robots", content: "noindex" },
    ],
  }),
  component: Page,
});

function Page() {
  return (
    <AdminTablePage
      title="Disease scans"
      description="Recent AI disease detections."
      columns={['Disease','Crop','Confidence','Date']}
      rows={demoScans.map((s) => [s.diseaseName, s.cropName, `${Math.round(s.confidence * 100)}%`, new Date(s.createdAt).toLocaleDateString()])}
    />
  );
}
