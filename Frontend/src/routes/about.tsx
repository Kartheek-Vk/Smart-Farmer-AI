import { createFileRoute, Link } from "@tanstack/react-router";
import { Globe2, HeartHandshake, Target } from "lucide-react";

import { SiteLayout } from "@/layouts/site-layout";
import { ResponsiveGrid, Section, SectionHeading } from "@/components/common/page";
import { FeatureCard } from "@/components/common/cards";
import { Button } from "@/components/ui/button";
import { LANGUAGES } from "@/i18n/languages";

export const Route = createFileRoute("/about")({
  head: () => ({
    meta: [
      { title: "About — Smart Farmer AI" },
      {
        name: "description",
        content:
          "Smart Farmer AI brings AI advisory to Indian farmers in their own language, connecting farmers, experts, dealers, NGOs and government.",
      },
      { property: "og:title", content: "About — Smart Farmer AI" },
      {
        property: "og:description",
        content: "Our mission: practical, explainable AI advice for every Indian farm.",
      },
    ],
  }),
  component: AboutPage,
});

function AboutPage() {
  return (
    <SiteLayout>
      <Section>
        <SectionHeading
          level={1}
          eyebrow="About us"
          title="Technology that speaks the farmer's language"
          body="Smart Farmer AI exists to make good agronomic decisions available to every farm, no matter its size."
        />
        <ResponsiveGrid cols="3">
          <FeatureCard
            icon={Target}
            title="Our mission"
            description="Put explainable AI advice in the hands of farmers so fewer seasons are lost to guesswork."
          />
          <FeatureCard
            icon={Globe2}
            title="Our approach"
            description="Design for low bandwidth, small screens and many languages first, then add depth."
            tone="weather"
          />
          <FeatureCard
            icon={HeartHandshake}
            title="Our network"
            description="Farmers, agri experts, dealers, NGOs and government officers working in one platform."
            tone="market"
          />
        </ResponsiveGrid>
      </Section>

      <Section muted>
        <SectionHeading
          eyebrow="Languages"
          title="Built for multilingual India"
          body="The interface architecture supports these languages, with more added over time."
        />
        <ul className="mx-auto flex max-w-3xl flex-wrap justify-center gap-2">
          {LANGUAGES.map((lang) => (
            <li
              key={lang.code}
              className="rounded-full border border-border bg-card px-4 py-2 text-sm font-medium"
            >
              {lang.nativeLabel}
            </li>
          ))}
        </ul>
        <div className="mt-10 text-center">
          <Button asChild size="lg" className="min-h-12">
            <Link to="/contact">Work with us</Link>
          </Button>
        </div>
      </Section>
    </SiteLayout>
  );
}
