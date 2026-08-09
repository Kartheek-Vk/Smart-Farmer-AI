import { createFileRoute } from "@tanstack/react-router";

import { AdminTablePage } from "@/components/app/admin-table";
import { demoMarketPrices } from "@/data/demo";

export const Route = createFileRoute("/admin/markets")({
  head: () => ({
    meta: [
      { title: "Markets — Smart Farmer AI admin" },
      { name: "description", content: "Mandi price feeds and today's rates." },
      { name: "robots", content: "noindex" },
    ],
  }),
  component: Page,
});

function Page() {
  return (
    <AdminTablePage
      title="Markets"
      description="Mandi price feeds and today's rates."
      columns={['Crop','Market','State','Today','Change']}
      rows={demoMarketPrices.map((m) => [m.crop, m.market, m.state, `₹${m.todayPrice}`, `${m.changePercent}%`])}
    />
  );
}
