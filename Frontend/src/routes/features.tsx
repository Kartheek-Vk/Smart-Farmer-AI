import { createFileRoute, Link } from "@tanstack/react-router";

import { SiteLayout } from "@/layouts/site-layout";
import { FadeIn, ResponsiveGrid, Section, SectionHeading } from "@/components/common/page";
import { FeatureCard } from "@/components/common/cards";
import { Button } from "@/components/ui/button";
import { CORE_FEATURES, EXTRA_FEATURES } from "@/config/content";

export const Route = createFileRoute("/features")({
  head: () => ({
    meta: [
      { title: "Features — Smart Farmer AI" },
      {
        name: "description",
        content:
          "Disease detection, crop and fertilizer advice, irrigation planning, weather alerts, mandi prices, schemes, analytics and a voice assistant.",
      },
      { property: "og:title", content: "Features — Smart Farmer AI" },
      {
        property: "og:description",
        content: "Every Smart Farmer AI feature, from disease scanning to profit prediction.",
      },
    ],
  }),
  component: FeaturesPage,
});

function FeaturesPage() {
  return (
    <SiteLayout>
      <Section>
        <SectionHeading
          level={1}
          eyebrow="Features"
          title="Tools that answer field questions"
          body="Each feature works on its own, and together they build a picture of your season."
        />
        <ResponsiveGrid cols="3">
          {CORE_FEATURES.map((feature, i) => (
            <FadeIn key={feature.id} delay={i * 0.04}>
              <FeatureCard
                icon={feature.icon}
                title={feature.title}
                description={feature.description}
                tone={feature.tone}
                footer={<p className="text-xs font-medium text-primary">{feature.benefit}</p>}
              />
            </FadeIn>
          ))}
        </ResponsiveGrid>
      </Section>

      <Section muted>
        <SectionHeading eyebrow="Also included" title="Beyond the core modules" />
        <ResponsiveGrid cols="3">
          {EXTRA_FEATURES.map((feature, i) => (
            <FadeIn key={feature.id} delay={i * 0.04}>
              <FeatureCard
                icon={feature.icon}
                title={feature.title}
                description={feature.description}
                tone={feature.tone}
                footer={<p className="text-xs font-medium text-primary">{feature.benefit}</p>}
              />
            </FadeIn>
          ))}
        </ResponsiveGrid>
        <div className="mt-10 text-center">
          <Button asChild size="lg" className="min-h-12">
            <Link to="/register">Try it on your farm</Link>
          </Button>
        </div>
      </Section>
    </SiteLayout>
  );
}
