import { createFileRoute, Link } from "@tanstack/react-router";

import { SiteLayout } from "@/layouts/site-layout";
import { Section } from "@/components/common/page";
import { Button } from "@/components/ui/button";

export const Route = createFileRoute("/404")({
  head: () => ({
    meta: [
      { title: "Page not found — Smart Farmer AI" },
      { name: "description", content: "This Smart Farmer AI page does not exist or has moved." },
      { name: "robots", content: "noindex" },
      { property: "og:title", content: "Page not found — Smart Farmer AI" },
      { property: "og:description", content: "This page does not exist or has moved." },
    ],
  }),
  component: NotFoundPage,
});

function NotFoundPage() {
  return (
    <SiteLayout>
      <Section className="text-center">
        <p className="text-6xl font-extrabold text-primary">404</p>
        <h1 className="mt-4 text-2xl font-bold">We couldn't find that field</h1>
        <p className="mx-auto mt-2 max-w-md text-sm text-muted-foreground">
          The page you're looking for doesn't exist or has been moved.
        </p>
        <div className="mt-6 flex justify-center gap-3">
          <Button asChild className="min-h-11">
            <Link to="/">Go home</Link>
          </Button>
          <Button asChild variant="outline" className="min-h-11">
            <Link to="/contact">Contact support</Link>
          </Button>
        </div>
      </Section>
    </SiteLayout>
  );
}
