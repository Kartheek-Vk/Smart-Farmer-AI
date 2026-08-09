import { createFileRoute } from "@tanstack/react-router";

import { PageHeader } from "@/components/common/page";
import { DataCard, DetailRow } from "@/components/app/list-primitives";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { demoFarms, demoUser } from "@/data/demo";

export const Route = createFileRoute("/app/profile")({
  head: () => ({
    meta: [
      { title: "Profile — Smart Farmer AI" },
      { name: "description", content: "Your account details and farm summary." },
      { name: "robots", content: "noindex" },
    ],
  }),
  component: ProfilePage,
});

function ProfilePage() {
  return (
    <div className="space-y-5">
      <PageHeader title="Profile" description="Keep your contact details up to date." />
      <div className="grid gap-4 lg:grid-cols-2">
        <DataCard>
          <h2 className="font-semibold">Account details</h2>
          <form className="mt-4 space-y-4" onSubmit={(e) => e.preventDefault()}>
            <div className="space-y-2">
              <Label htmlFor="profile-name">Name</Label>
              <Input id="profile-name" defaultValue={demoUser.name} className="h-11" maxLength={100} />
            </div>
            <div className="space-y-2">
              <Label htmlFor="profile-email">Email</Label>
              <Input id="profile-email" type="email" defaultValue={demoUser.email} className="h-11" maxLength={255} />
            </div>
            <div className="space-y-2">
              <Label htmlFor="profile-phone">Phone</Label>
              <Input id="profile-phone" type="tel" defaultValue={demoUser.phone} className="h-11" maxLength={15} />
            </div>
            <Button type="submit" className="min-h-11">Save changes</Button>
          </form>
        </DataCard>
        <DataCard>
          <h2 className="font-semibold">Summary</h2>
          <div className="mt-3">
            <DetailRow label="Role" value={demoUser.role} />
            <DetailRow label="Member since" value={demoUser.createdAt ?? "—"} />
            <DetailRow label="Farms" value={demoFarms.length} />
            <DetailRow
              label="Total area"
              value={`${demoFarms.reduce((sum, f) => sum + f.area, 0).toFixed(1)} acres`}
            />
          </div>
        </DataCard>
      </div>
    </div>
  );
}
