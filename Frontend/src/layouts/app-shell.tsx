import { useState, type ReactNode } from "react";
import { Link, useRouterState } from "@tanstack/react-router";
import { Bell, LogOut, Menu, Search } from "lucide-react";

import { BrandMark } from "@/components/navigation/site-header";
import { LanguageSelector } from "@/components/common/language-selector";
import { ThemeToggle } from "@/components/common/theme-toggle";
import { Button } from "@/components/ui/button";
import { Sheet, SheetContent, SheetHeader, SheetTitle, SheetTrigger } from "@/components/ui/sheet";
import { APP_NAV, MOBILE_TAB_NAV } from "@/config/navigation";
import { useI18n } from "@/i18n/i18n-provider";
import { cn } from "@/lib/utils";

function isActive(pathname: string, to: string, exact?: boolean) {
  return exact ? pathname === to : pathname === to || pathname.startsWith(`${to}/`);
}

function SidebarNav({ onNavigate }: { onNavigate?: () => void }) {
  const { t } = useI18n();
  const pathname = useRouterState({ select: (s) => s.location.pathname });

  return (
    <nav aria-label="Application" className="flex flex-col gap-1 p-3">
      {APP_NAV.map((item) => (
        <Link
          key={item.to}
          to={item.to}
          onClick={onNavigate}
          aria-current={isActive(pathname, item.to, item.exact) ? "page" : undefined}
          className={cn(
            "flex min-h-11 items-center gap-3 rounded-xl px-3 py-2 text-sm font-medium text-sidebar-foreground transition-colors hover:bg-sidebar-accent",
            isActive(pathname, item.to, item.exact) &&
              "bg-sidebar-accent text-sidebar-accent-foreground",
          )}
        >
          <item.icon className="size-4 shrink-0" aria-hidden="true" />
          <span className="truncate">{t(item.labelKey)}</span>
        </Link>
      ))}
      <Link
        to="/"
        onClick={onNavigate}
        className="mt-2 flex min-h-11 items-center gap-3 rounded-xl px-3 py-2 text-sm font-medium text-muted-foreground hover:bg-sidebar-accent"
      >
        <LogOut className="size-4 shrink-0" aria-hidden="true" />
        <span>{t("app.logout")}</span>
      </Link>
    </nav>
  );
}

function BottomNav() {
  const { t } = useI18n();
  const pathname = useRouterState({ select: (s) => s.location.pathname });

  return (
    <nav
      aria-label="Primary mobile"
      className="fixed inset-x-0 bottom-0 z-40 border-t border-border bg-background/95 pb-[env(safe-area-inset-bottom)] backdrop-blur lg:hidden"
    >
      <ul className="mx-auto flex max-w-lg items-stretch">
        {MOBILE_TAB_NAV.map((item) => (
          <li key={item.to + item.labelKey} className="flex-1">
            <Link
              to={item.to}
              aria-current={isActive(pathname, item.to, item.exact) ? "page" : undefined}
              className={cn(
                "flex min-h-14 flex-col items-center justify-center gap-1 px-1 text-[11px] font-medium text-muted-foreground",
                isActive(pathname, item.to, item.exact) && "text-primary",
              )}
            >
              <item.icon className="size-5" aria-hidden="true" />
              <span className="truncate">{t(item.labelKey)}</span>
            </Link>
          </li>
        ))}
      </ul>
    </nav>
  );
}

export function AppShell({ children }: { children: ReactNode }) {
  const { t } = useI18n();
  const [open, setOpen] = useState(false);

  return (
    <div className="min-h-dvh bg-muted/30">
      <div className="flex">
        <aside className="sticky top-0 hidden h-dvh w-64 shrink-0 flex-col border-r border-sidebar-border bg-sidebar lg:flex xl:w-72">
          <div className="flex h-16 items-center border-b border-sidebar-border px-4">
            <BrandMark />
          </div>
          <div className="flex-1 overflow-y-auto">
            <SidebarNav />
          </div>
        </aside>

        <div className="flex min-w-0 flex-1 flex-col">
          <header className="sticky top-0 z-30 border-b border-border bg-background/95 backdrop-blur">
            <div className="flex h-16 items-center gap-2 px-4 sm:px-6">
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
                <SheetContent side="left" className="w-[86vw] max-w-xs p-0">
                  <SheetHeader className="border-b border-border px-4 py-3">
                    <SheetTitle className="text-left">{t("brand.name")}</SheetTitle>
                  </SheetHeader>
                  <div className="overflow-y-auto">
                    <SidebarNav onNavigate={() => setOpen(false)} />
                  </div>
                </SheetContent>
              </Sheet>

              <div className="lg:hidden">
                <BrandMark />
              </div>

              <div className="ml-auto flex items-center gap-1">
                <Button variant="ghost" size="icon" className="hidden min-h-11 min-w-11 sm:inline-flex" aria-label={t("common.search")}>
                  <Search className="size-4" aria-hidden="true" />
                </Button>
                <Button asChild variant="ghost" size="icon" className="min-h-11 min-w-11" aria-label={t("app.notifications")}>
                  <Link to="/app/notifications">
                    <Bell className="size-4" aria-hidden="true" />
                  </Link>
                </Button>
                <div className="hidden sm:block">
                  <LanguageSelector compact />
                </div>
                <ThemeToggle />
                <Button asChild variant="outline" className="hidden min-h-11 md:inline-flex">
                  <Link to="/app/profile">{t("app.profile")}</Link>
                </Button>
              </div>
            </div>
          </header>

          <main id="main-content" className="min-w-0 flex-1 px-4 pb-24 pt-5 sm:px-6 lg:pb-10">
            <div className="mx-auto w-full max-w-6xl">{children}</div>
          </main>
        </div>
      </div>
      <BottomNav />
    </div>
  );
}
