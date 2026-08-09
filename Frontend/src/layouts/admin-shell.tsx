import { useState, type ReactNode } from "react";
import { Link, useRouterState } from "@tanstack/react-router";
import { ArrowLeft, Menu } from "lucide-react";

import { BrandMark } from "@/components/navigation/site-header";
import { LanguageSelector } from "@/components/common/language-selector";
import { ThemeToggle } from "@/components/common/theme-toggle";
import { Button } from "@/components/ui/button";
import { Sheet, SheetContent, SheetHeader, SheetTitle, SheetTrigger } from "@/components/ui/sheet";
import { ADMIN_NAV } from "@/config/navigation";
import { useI18n } from "@/i18n/i18n-provider";
import { cn } from "@/lib/utils";

function AdminNav({ onNavigate }: { onNavigate?: () => void }) {
  const { t } = useI18n();
  const pathname = useRouterState({ select: (s) => s.location.pathname });

  return (
    <nav aria-label="Admin" className="flex flex-col gap-1 p-3">
      {ADMIN_NAV.map((item) => {
        const active = item.exact ? pathname === item.to : pathname.startsWith(item.to);
        return (
          <Link
            key={item.to}
            to={item.to}
            onClick={onNavigate}
            aria-current={active ? "page" : undefined}
            className={cn(
              "flex min-h-11 items-center gap-3 rounded-xl px-3 py-2 text-sm font-medium text-sidebar-foreground transition-colors hover:bg-sidebar-accent",
              active && "bg-sidebar-accent text-sidebar-accent-foreground",
            )}
          >
            <item.icon className="size-4 shrink-0" aria-hidden="true" />
            <span className="truncate">{t(item.labelKey)}</span>
          </Link>
        );
      })}
    </nav>
  );
}

export function AdminShell({ children }: { children: ReactNode }) {
  const { t } = useI18n();
  const [open, setOpen] = useState(false);

  return (
    <div className="min-h-dvh bg-muted/30">
      <div className="flex">
        <aside className="sticky top-0 hidden h-dvh w-64 shrink-0 flex-col border-r border-sidebar-border bg-sidebar lg:flex">
          <div className="flex h-16 items-center gap-2 border-b border-sidebar-border px-4">
            <BrandMark />
          </div>
          <p className="px-4 pt-3 text-xs font-semibold uppercase tracking-widest text-muted-foreground">
            {t("admin.title")}
          </p>
          <div className="flex-1 overflow-y-auto">
            <AdminNav />
          </div>
        </aside>

        <div className="flex min-w-0 flex-1 flex-col">
          <header className="sticky top-0 z-30 border-b border-border bg-background/95 backdrop-blur">
            <div className="flex h-16 items-center gap-2 px-4 sm:px-6">
              <Sheet open={open} onOpenChange={setOpen}>
                <SheetTrigger asChild>
                  <Button variant="ghost" size="icon" className="min-h-11 min-w-11 lg:hidden" aria-label={t("nav.openMenu")}>
                    <Menu className="size-5" aria-hidden="true" />
                  </Button>
                </SheetTrigger>
                <SheetContent side="left" className="w-[86vw] max-w-xs p-0">
                  <SheetHeader className="border-b border-border px-4 py-3">
                    <SheetTitle className="text-left">{t("admin.title")}</SheetTitle>
                  </SheetHeader>
                  <div className="overflow-y-auto">
                    <AdminNav onNavigate={() => setOpen(false)} />
                  </div>
                </SheetContent>
              </Sheet>
              <span className="truncate text-sm font-semibold lg:text-base">
                {t("brand.name")} · {t("admin.title")}
              </span>
              <div className="ml-auto flex items-center gap-1">
                <div className="hidden sm:block">
                  <LanguageSelector compact />
                </div>
                <ThemeToggle />
                <Button asChild variant="outline" className="min-h-11">
                  <Link to="/app">
                    <ArrowLeft className="size-4" aria-hidden="true" />
                    <span className="hidden sm:inline">{t("app.dashboard")}</span>
                  </Link>
                </Button>
              </div>
            </div>
          </header>

          <main id="main-content" className="min-w-0 flex-1 px-4 pb-10 pt-5 sm:px-6">
            <div className="mx-auto w-full max-w-7xl">{children}</div>
          </main>
        </div>
      </div>
    </div>
  );
}
