import { useState } from "react";
import { createFileRoute } from "@tanstack/react-router";
import { Bell } from "lucide-react";

import { PageHeader } from "@/components/common/page";
import { IconBubble, StatusBadge } from "@/components/common/cards";
import { DataCard } from "@/components/app/list-primitives";
import { Button } from "@/components/ui/button";
import { demoNotifications } from "@/data/demo";

export const Route = createFileRoute("/app/notifications")({
  head: () => ({
    meta: [
      { title: "Notifications — Smart Farmer AI" },
      { name: "description", content: "Weather, market, scheme and AI alerts for your farm." },
      { name: "robots", content: "noindex" },
    ],
  }),
  component: NotificationsPage,
});

function NotificationsPage() {
  const [items, setItems] = useState(demoNotifications);
  const unread = items.filter((n) => !n.read).length;

  return (
    <div className="space-y-5">
      <PageHeader
        title="Notifications"
        description={`${unread} unread`}
        actions={
          <Button
            variant="outline"
            className="min-h-11"
            onClick={() => setItems((prev) => prev.map((n) => ({ ...n, read: true })))}
          >
            Mark all read
          </Button>
        }
      />
      <DataCard>
        <ul className="space-y-3">
          {items.map((item) => (
            <li
              key={item.id}
              className={
                "flex items-start gap-3 rounded-xl border border-border p-3 " + (item.read ? "opacity-70" : "")
              }
            >
              <IconBubble icon={Bell} tone={item.read ? "primary" : "warning"} />
              <div className="min-w-0 flex-1">
                <div className="flex items-center justify-between gap-2">
                  <p className="truncate font-medium">{item.title}</p>
                  <StatusBadge status={item.type} tone="primary" />
                </div>
                <p className="mt-1 text-sm text-muted-foreground">{item.body}</p>
                <p className="mt-1 text-xs text-muted-foreground">
                  {new Date(item.createdAt).toLocaleString()}
                </p>
              </div>
            </li>
          ))}
        </ul>
      </DataCard>
    </div>
  );
}
