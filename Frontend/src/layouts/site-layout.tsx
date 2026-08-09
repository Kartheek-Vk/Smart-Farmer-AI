import type { ReactNode } from "react";

import { SiteFooter } from "@/components/navigation/site-footer";
import { SiteHeader } from "@/components/navigation/site-header";

export function SiteLayout({ children }: { children: ReactNode }) {
  return (
    <div className="flex min-h-dvh flex-col bg-background">
      <SiteHeader />
      <main id="main-content" className="flex-1">
        {children}
      </main>
      <SiteFooter />
    </div>
  );
}
