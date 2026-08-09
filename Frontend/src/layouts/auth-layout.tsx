import type { ReactNode } from "react";
import { Link } from "@tanstack/react-router";

import { BrandMark } from "@/components/navigation/site-header";
import { LanguageSelector } from "@/components/common/language-selector";
import { Card, CardContent } from "@/components/ui/card";

export function AuthLayout({
  title,
  description,
  children,
  footer,
}: {
  title: string;
  description: string;
  children: ReactNode;
  footer?: ReactNode;
}) {
  return (
    <div className="field-pattern flex min-h-dvh flex-col bg-background">
      <header className="flex items-center justify-between px-4 py-4 sm:px-6">
        <Link to="/" aria-label="Smart Farmer AI home">
          <BrandMark />
        </Link>
        <LanguageSelector compact />
      </header>
      <main id="main-content" className="flex flex-1 items-center justify-center px-4 py-8">
        <div className="w-full max-w-md">
          <Card className="surface-card border-0 shadow-[var(--shadow-soft)]">
            <CardContent className="p-6 sm:p-8">
              <h1 className="text-2xl font-bold tracking-tight">{title}</h1>
              <p className="mt-2 text-sm text-muted-foreground">{description}</p>
              <div className="mt-6">{children}</div>
            </CardContent>
          </Card>
          {footer ? <div className="mt-5 text-center text-sm text-muted-foreground">{footer}</div> : null}
        </div>
      </main>
    </div>
  );
}
