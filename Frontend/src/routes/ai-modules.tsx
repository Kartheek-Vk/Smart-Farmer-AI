import { createFileRoute, Link } from "@tanstack/react-router";

import { SiteLayout } from "@/layouts/site-layout";
import { Section, SectionHeading } from "@/components/common/page";
import { IconBubble } from "@/components/common/cards";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { AI_MODULES } from "@/config/content";

export const Route = createFileRoute("/ai-modules")({
  head: () => ({
    meta: [
      { title: "AI Modules — Smart Farmer AI" },
      {
        name: "description",
        content:
          "See the inputs, processing and outputs behind each Smart Farmer AI module: disease detection, crop, fertilizer, irrigation, profit, market and assistant.",
      },
      { property: "og:title", content: "AI Modules — Smart Farmer AI" },
      {
        property: "og:description",
        content: "Explainable AI modules for disease, crop, fertilizer, irrigation, profit and market decisions.",
      },
    ],
  }),
  component: AiModulesPage,
});

function ColumnList({ title, items }: { title: string; items: string[] }) {
  return (
    <div className="min-w-0">
      <h4 className="text-xs font-semibold uppercase tracking-wide text-muted-foreground">{title}</h4>
      <ul className="mt-2 space-y-1 text-sm">
        {items.map((item) => (
          <li key={item} className="flex gap-2">
            <span className="mt-2 size-1.5 shrink-0 rounded-full bg-primary" aria-hidden="true" />
            <span className="min-w-0">{item}</span>
          </li>
        ))}
      </ul>
    </div>
  );
}

function AiModulesPage() {
  return (
    <SiteLayout>
      <Section>
        <SectionHeading
          level={1}
          eyebrow="AI modules"
          title="How each model reaches its answer"
          body="No black boxes — every module shows what it takes in and what it gives back."
        />
        <div className="space-y-5">
          {AI_MODULES.map((module) => (
            <Card key={module.id} className="surface-card border-0 shadow-none">
              <CardContent className="p-5 sm:p-6">
                <div className="flex min-w-0 items-start gap-3">
                  <IconBubble icon={module.icon} tone={module.tone} />
                  <div className="min-w-0">
                    <h2 className="text-lg font-semibold">{module.name}</h2>
                    <p className="mt-1 text-sm text-muted-foreground">{module.summary}</p>
                  </div>
                </div>
                <div className="mt-5 grid gap-5 sm:grid-cols-2 lg:grid-cols-4">
                  <ColumnList title="Input" items={module.input} />
                  <ColumnList title="Processing" items={module.processing} />
                  <ColumnList title="Output" items={module.output} />
                  <ColumnList title="Benefits" items={module.benefits} />
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
        <div className="mt-10 text-center">
          <Button asChild size="lg" className="min-h-12">
            <Link to="/register">Run a module on your field</Link>
          </Button>
        </div>
      </Section>
    </SiteLayout>
  );
}
