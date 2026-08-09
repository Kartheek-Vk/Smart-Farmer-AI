import { createFileRoute, Link } from "@tanstack/react-router";

import { SiteLayout } from "@/layouts/site-layout";
import { Section, SectionHeading } from "@/components/common/page";
import { Accordion, AccordionContent, AccordionItem, AccordionTrigger } from "@/components/ui/accordion";
import { Button } from "@/components/ui/button";
import { FAQS } from "@/config/content";

export const Route = createFileRoute("/faq")({
  head: () => ({
    meta: [
      { title: "FAQ — Smart Farmer AI" },
      {
        name: "description",
        content:
          "Answers about pricing, accuracy, languages, data privacy and offline use of the Smart Farmer AI advisory app.",
      },
      { property: "og:title", content: "FAQ — Smart Farmer AI" },
      { property: "og:description", content: "Common questions about using Smart Farmer AI on your farm." },
    ],
    scripts: [
      {
        type: "application/ld+json",
        children: JSON.stringify({
          "@context": "https://schema.org",
          "@type": "FAQPage",
          mainEntity: FAQS.map((faq) => ({
            "@type": "Question",
            name: faq.question,
            acceptedAnswer: { "@type": "Answer", text: faq.answer },
          })),
        }),
      },
    ],
  }),
  component: FaqPage,
});

function FaqPage() {
  return (
    <SiteLayout>
      <Section>
        <SectionHeading level={1} eyebrow="FAQ" title="Questions farmers ask us" />
        <div className="mx-auto max-w-3xl">
          <Accordion type="single" collapsible className="surface-card divide-y divide-border px-5">
            {FAQS.map((faq, i) => (
              <AccordionItem key={faq.question} value={`item-${i}`} className="border-0">
                <AccordionTrigger className="text-left text-base font-semibold">
                  {faq.question}
                </AccordionTrigger>
                <AccordionContent className="text-sm text-muted-foreground">{faq.answer}</AccordionContent>
              </AccordionItem>
            ))}
          </Accordion>
          <div className="mt-8 text-center">
            <p className="text-sm text-muted-foreground">Still stuck?</p>
            <Button asChild variant="outline" className="mt-3 min-h-11">
              <Link to="/contact">Contact support</Link>
            </Button>
          </div>
        </div>
      </Section>
    </SiteLayout>
  );
}
