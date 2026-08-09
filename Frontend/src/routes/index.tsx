import { createFileRoute, Link } from "@tanstack/react-router";
import { ArrowRight, CheckCircle2, Quote, Star } from "lucide-react";

import heroImage from "@/assets/hero-agri-ai.png";
import { SiteLayout } from "@/layouts/site-layout";
import { FadeIn, PageContainer, ResponsiveGrid, Section, SectionHeading } from "@/components/common/page";
import { FeatureCard, IconBubble, LinkCard } from "@/components/common/cards";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import {
  AI_MODULES,
  CORE_FEATURES,
  HOW_IT_WORKS,
  SHOWCASE_METRICS,
  TESTIMONIALS,
  WHY_US,
} from "@/config/content";

export const Route = createFileRoute("/")({
  head: () => ({
    meta: [
      { title: "Smart Farmer AI — AI crop, disease & market advice" },
      {
        name: "description",
        content:
          "Scan crop disease from a photo, get crop, fertilizer and irrigation advice, and track weather and mandi prices — built for Indian farmers in 11 languages.",
      },
      { property: "og:title", content: "Smart Farmer AI — AI crop, disease & market advice" },
      {
        property: "og:description",
        content:
          "AI-powered farming decisions: disease detection, crop and fertilizer advice, irrigation planning, weather alerts and daily mandi prices.",
      },
    ],
  }),
  component: HomePage,
});

function HomePage() {
  return (
    <SiteLayout>
      <section className="field-pattern relative overflow-hidden border-b border-border">
        <PageContainer className="grid items-center gap-10 py-14 lg:grid-cols-2 lg:py-24">
          <div className="min-w-0">
            <span className="inline-flex items-center gap-2 rounded-full bg-primary-soft px-3 py-1 text-xs font-semibold text-primary">
              <Star className="size-3.5" aria-hidden="true" />
              AI advisory for Indian farms
            </span>
            <h1 className="mt-4 text-balance text-3xl font-extrabold leading-tight tracking-tight sm:text-4xl lg:text-5xl">
              Smarter farming decisions, every single day
            </h1>
            <p className="mt-4 max-w-xl text-pretty text-base text-muted-foreground sm:text-lg">
              Detect crop disease from a photo, get crop, fertilizer and irrigation advice tuned to your
              soil, and follow weather and mandi prices — all in your own language.
            </p>
            <div className="mt-7 flex flex-col gap-3 sm:flex-row">
              <Button asChild size="lg" className="min-h-12 text-base">
                <Link to="/register">
                  Get started free
                  <ArrowRight className="size-4" aria-hidden="true" />
                </Link>
              </Button>
              <Button asChild size="lg" variant="outline" className="min-h-12 text-base">
                <Link to="/how-it-works">See how it works</Link>
              </Button>
            </div>
            <ul className="mt-7 flex flex-wrap gap-x-5 gap-y-2 text-sm text-muted-foreground">
              {["Free to start", "Works on any phone", "11 Indian languages"].map((item) => (
                <li key={item} className="flex items-center gap-2">
                  <CheckCircle2 className="size-4 text-primary" aria-hidden="true" />
                  {item}
                </li>
              ))}
            </ul>
          </div>

          <div className="relative mx-auto w-full max-w-lg">
            <img
              src={heroImage}
              alt="Illustration of a healthy crop seedling analysed by AI farming tools"
              width={1280}
              height={1024}
              className="w-full drop-shadow-xl"
            />
          </div>
        </PageContainer>
      </section>

      <section className="border-b border-border bg-primary text-primary-foreground">
        <PageContainer className="grid grid-cols-2 gap-6 py-8 sm:grid-cols-3 lg:grid-cols-5">
          {SHOWCASE_METRICS.map((metric) => (
            <div key={metric.id} className="min-w-0 text-center">
              <p className="text-2xl font-extrabold sm:text-3xl">{metric.value}</p>
              <p className="truncate text-xs opacity-90 sm:text-sm">{metric.label}</p>
            </div>
          ))}
        </PageContainer>
      </section>

      <Section>
        <SectionHeading
          eyebrow="Features"
          title="Everything a farm needs, in one app"
          body="Six core modules that turn field data into clear, practical next steps."
        />
        <ResponsiveGrid cols="3">
          {CORE_FEATURES.map((feature, i) => (
            <FadeIn key={feature.id} delay={i * 0.05}>
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
        <div className="mt-8 text-center">
          <Button asChild variant="outline" className="min-h-11">
            <Link to="/features">See all features</Link>
          </Button>
        </div>
      </Section>

      <Section muted>
        <SectionHeading
          eyebrow="AI modules"
          title="Seven models working for your field"
          body="Each module takes simple inputs and returns an explainable result."
        />
        <ResponsiveGrid cols="3">
          {AI_MODULES.slice(0, 6).map((module) => (
            <LinkCard
              key={module.id}
              to="/ai-modules"
              icon={module.icon}
              tone={module.tone}
              title={module.name}
              description={module.summary}
            />
          ))}
        </ResponsiveGrid>
      </Section>

      <Section>
        <SectionHeading eyebrow="How it works" title="From photo to plan in eight steps" />
        <ol className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
          {HOW_IT_WORKS.map((step) => (
            <li key={step.step} className="surface-card p-5">
              <span className="grid size-9 place-items-center rounded-full bg-primary text-sm font-bold text-primary-foreground">
                {step.step}
              </span>
              <h3 className="mt-3 font-semibold">{step.title}</h3>
              <p className="mt-1 text-sm text-muted-foreground">{step.description}</p>
            </li>
          ))}
        </ol>
      </Section>

      <Section muted>
        <SectionHeading eyebrow="Why Smart Farmer AI" title="Built for real fields, not demos" />
        <ResponsiveGrid cols="3">
          {WHY_US.map((item) => (
            <Card key={item.title} className="surface-card h-full border-0 shadow-none">
              <CardContent className="flex gap-3 p-5">
                <IconBubble icon={item.icon} tone={item.tone} />
                <div className="min-w-0">
                  <h3 className="font-semibold">{item.title}</h3>
                  <p className="mt-1 text-sm text-muted-foreground">{item.description}</p>
                </div>
              </CardContent>
            </Card>
          ))}
        </ResponsiveGrid>
      </Section>

      <Section>
        <SectionHeading eyebrow="Farmer stories" title="What farmers say" />
        <ResponsiveGrid cols="3">
          {TESTIMONIALS.map((item) => (
            <Card key={item.id} className="surface-card h-full border-0 shadow-none">
              <CardContent className="flex h-full flex-col gap-4 p-5">
                <Quote className="size-5 text-primary" aria-hidden="true" />
                <p className="text-sm leading-relaxed">{item.quote}</p>
                <div className="mt-auto">
                  <p className="text-sm font-semibold">{item.name}</p>
                  <p className="text-xs text-muted-foreground">
                    {item.role} · {item.location}
                  </p>
                </div>
              </CardContent>
            </Card>
          ))}
        </ResponsiveGrid>
      </Section>

      <Section muted className="pb-20">
        <div className="surface-card mx-auto max-w-3xl px-6 py-12 text-center">
          <h2 className="text-balance text-2xl font-bold sm:text-3xl">
            Start your first field analysis today
          </h2>
          <p className="mx-auto mt-3 max-w-xl text-sm text-muted-foreground sm:text-base">
            Create a free account, add your farm and get your first recommendation in minutes.
          </p>
          <div className="mt-6 flex flex-col justify-center gap-3 sm:flex-row">
            <Button asChild size="lg" className="min-h-12">
              <Link to="/register">Create free account</Link>
            </Button>
            <Button asChild size="lg" variant="outline" className="min-h-12">
              <Link to="/contact">Talk to us</Link>
            </Button>
          </div>
        </div>
      </Section>
    </SiteLayout>
  );
}
