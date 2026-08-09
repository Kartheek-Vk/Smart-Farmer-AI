import { createFileRoute, Link } from "@tanstack/react-router";

import { SiteLayout } from "@/layouts/site-layout";
import { Section, SectionHeading } from "@/components/common/page";
import { Button } from "@/components/ui/button";
import { HOW_IT_WORKS } from "@/config/content";

export const Route = createFileRoute("/how-it-works")({
  head: () => ({
    meta: [
      { title: "How It Works — Smart Farmer AI" },
      {
        name: "description",
        content:
          "Register, add your farm and crop, scan or enter field data, and get AI recommendations you can act on the same day.",
      },
      { property: "og:title", content: "How It Works — Smart Farmer AI" },
      {
        property: "og:description",
        content: "Eight simple steps from registration to a better yield with Smart Farmer AI.",
      },
    ],
  }),
  component: HowItWorksPage,
});

function HowItWorksPage() {
  return (
    <SiteLayout>
      <Section>
        <SectionHeading
          level={1}
          eyebrow="How it works"
          title="Eight steps, start to harvest"
          body="You only ever answer simple questions. The AI does the rest."
        />
        <ol className="relative mx-auto max-w-3xl space-y-4 border-l border-border pl-6">
          {HOW_IT_WORKS.map((step) => (
            <li key={step.step} className="relative surface-card p-5">
              <span
                className="absolute -left-[2.35rem] top-6 grid size-8 place-items-center rounded-full bg-primary text-xs font-bold text-primary-foreground"
                aria-hidden="true"
              >
                {step.step}
              </span>
              <h2 className="font-semibold">
                Step {step.step}: {step.title}
              </h2>
              <p className="mt-1 text-sm text-muted-foreground">{step.description}</p>
            </li>
          ))}
        </ol>
        <div className="mt-10 text-center">
          <Button asChild size="lg" className="min-h-12">
            <Link to="/register">Start at step one</Link>
          </Button>
        </div>
      </Section>
    </SiteLayout>
  );
}
