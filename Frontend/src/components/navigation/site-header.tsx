import { useState } from "react";
import { Link, useRouterState } from "@tanstack/react-router";
import { Leaf, Menu } from "lucide-react";

import { Button } from "@/components/ui/button";
import { Sheet, SheetContent, SheetHeader, SheetTitle, SheetTrigger } from "@/components/ui/sheet";
import { LanguageSelector } from "@/components/common/language-selector";
import { ThemeToggle } from "@/components/common/theme-toggle";
import { PUBLIC_NAV } from "@/config/navigation";
import { useI18n } from "@/i18n/i18n-provider";
import { cn } from "@/lib/utils";

export function BrandMark({ className }: { className?: string }) {
  const { t } = useI18n();
  return (
    <Link to="/" className={cn("flex min-w-0 items-center gap-2", className)} aria-label={t("brand.name")}>
      <span className="grid size-9 shrink-0 place-items-center rounded-xl bg-primary text-primary-foreground">
        <Leaf className="size-5" aria-hidden="true" />
      </span>
      <span className="truncate text-base font-bold tracking-tight">{t("brand.name")}</span>
    </Link>
  );
}

export function SiteHeader() {
  const { t } = useI18n();
  const [open, setOpen] = useState(false);
  const pathname = useRouterState({ select: (s) => s.location.pathname });

  return (
    <header className="sticky top-0 z-50 w-full border-b border-border bg-background/90 backdrop-blur">
      <div className="mx-auto flex h-16 w-full max-w-7xl items-center gap-3 px-4 sm:px-6 lg:px-8">
        <BrandMark />
        <nav aria-label="Primary" className="ml-6 hidden items-center gap-1 lg:flex">
          {PUBLIC_NAV.map((item) => (
            <Link
              key={item.to}
              to={item.to}
              className={cn(
                "rounded-lg px-3 py-2 text-sm font-medium text-muted-foreground transition-colors hover:bg-accent hover:text-accent-foreground",
                pathname === item.to && "bg-accent text-accent-foreground",
              )}
            >
              {t(item.labelKey)}
            </Link>
          ))}
        </nav>
        <div className="ml-auto flex items-center gap-1">
          <div className="hidden sm:block">
            <LanguageSelector />
          </div>
          <ThemeToggle />
          <Button asChild variant="ghost" className="hidden min-h-11 sm:inline-flex">
            <Link to="/login">{t("nav.login")}</Link>
          </Button>
          <Button asChild className="hidden min-h-11 md:inline-flex">
            <Link to="/register">{t("nav.getStarted")}</Link>
          </Button>
          <Sheet open={open} onOpenChange={setOpen}>
            <SheetTrigger asChild>
              <Button
                variant="ghost"
                size="icon"
                className="min-h-11 min-w-11 lg:hidden"
                aria-label={t("nav.openMenu")}
              >
                <Menu className="size-5" aria-hidden="true" />
              </Button>
            </SheetTrigger>
            <SheetContent side="right" className="w-[86vw] max-w-sm">
              <SheetHeader>
                <SheetTitle>{t("nav.menu")}</SheetTitle>
              </SheetHeader>
              <nav aria-label="Mobile" className="flex flex-col gap-1 p-4">
                {PUBLIC_NAV.map((item) => (
                  <Link
                    key={item.to}
                    to={item.to}
                    onClick={() => setOpen(false)}
                    className="rounded-lg px-3 py-3 text-base font-medium hover:bg-accent"
                  >
                    {t(item.labelKey)}
                  </Link>
                ))}
                <Link
                  to="/faq"
                  onClick={() => setOpen(false)}
                  className="rounded-lg px-3 py-3 text-base font-medium hover:bg-accent"
                >
                  {t("nav.faq")}
                </Link>
                <div className="mt-3 flex flex-col gap-2">
                  <Button asChild variant="outline" className="min-h-11">
                    <Link to="/login" onClick={() => setOpen(false)}>
                      {t("nav.login")}
                    </Link>
                  </Button>
                  <Button asChild className="min-h-11">
                    <Link to="/register" onClick={() => setOpen(false)}>
                      {t("nav.getStarted")}
                    </Link>
                  </Button>
                </div>
                <div className="mt-3">
                  <LanguageSelector />
                </div>
              </nav>
            </SheetContent>
          </Sheet>
        </div>
      </div>
    </header>
  );
}
