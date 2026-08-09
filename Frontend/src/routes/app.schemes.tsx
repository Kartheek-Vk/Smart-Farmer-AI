import { createFileRoute } from "@tanstack/react-router";
import { Landmark } from "lucide-react";

import { PageHeader } from "@/components/common/page";
import { IconBubble, StatusBadge } from "@/components/common/cards";
import { DataCard } from "@/components/app/list-primitives";
import { Button } from "@/components/ui/button";
import { demoSchemes } from "@/data/demo";

export const Route = createFileRoute("/app/schemes")({
  head: () => ({
    meta: [
      { title: "Government schemes — Smart Farmer AI" },
      { name: "description", content: "Central and state farming schemes with eligibility and application links." },
      { name: "robots", content: "noindex" },
    ],
  }),
  component: SchemesPage,
});

function SchemesPage() {
  return (
    <div className="space-y-5">
      <PageHeader title="Government schemes" description="Support you may already qualify for." />
      <div className="grid gap-4 md:grid-cols-2">
        {demoSchemes.map((scheme) => (
          <DataCard key={scheme.id}>
            <div className="flex items-start gap-3">
              <IconBubble icon={Landmark} tone="market" />
              <div className="min-w-0 flex-1">
                <div className="flex items-center justify-between gap-2">
                  <h2 className="truncate font-semibold">{scheme.name}</h2>
                  <StatusBadge status={scheme.category} tone="market" />
                </div>
                <p className="text-xs text-muted-foreground">{scheme.state}</p>
                <p className="mt-2 text-sm text-muted-foreground">{scheme.summary}</p>
                <h3 className="mt-3 text-xs font-semibold uppercase tracking-wide text-muted-foreground">
                  Eligibility
                </h3>
                <ul className="mt-1 space-y-1 text-sm">
                  {scheme.eligibility.map((item) => (
                    <li key={item}>• {item}</li>
                  ))}
                </ul>
                {scheme.applicationUrl ? (
                  <Button asChild variant="outline" className="mt-4 min-h-11">
                    <a href={scheme.applicationUrl} target="_blank" rel="noreferrer noopener">
                      Apply on official site
                    </a>
                  </Button>
                ) : null}
              </div>
            </div>
          </DataCard>
        ))}
      </div>
    </div>
  );
}
