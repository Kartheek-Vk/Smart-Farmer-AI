import { Search } from "lucide-react";
import type { ReactNode } from "react";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { cn } from "@/lib/utils";
import { useI18n } from "@/i18n/i18n-provider";

export function SearchBar({
  value,
  onChange,
  placeholder,
  label,
  className,
}: {
  value: string;
  onChange: (value: string) => void;
  placeholder?: string;
  label?: string;
  className?: string;
}) {
  const { t } = useI18n();
  const id = `search-${label?.toLowerCase().replace(/\s+/g, "-") ?? "field"}`;
  return (
    <div className={cn("relative w-full", className)}>
      <label htmlFor={id} className="sr-only">
        {label ?? t("common.search")}
      </label>
      <Search
        className="pointer-events-none absolute left-3 top-1/2 size-4 -translate-y-1/2 text-muted-foreground"
        aria-hidden="true"
      />
      <Input
        id={id}
        type="search"
        value={value}
        onChange={(e) => onChange(e.target.value)}
        placeholder={placeholder ?? t("common.search")}
        className="h-11 pl-9"
      />
    </div>
  );
}

export function FilterBar({ children, className }: { children: ReactNode; className?: string }) {
  return (
    <div className={cn("flex flex-col gap-3 sm:flex-row sm:flex-wrap sm:items-center", className)}>
      {children}
    </div>
  );
}

export function Pagination({
  page,
  totalPages,
  onPageChange,
}: {
  page: number;
  totalPages: number;
  onPageChange: (page: number) => void;
}) {
  const { t } = useI18n();
  return (
    <nav className="flex items-center justify-between gap-3 pt-4" aria-label="Pagination">
      <Button
        variant="outline"
        className="min-h-11"
        disabled={page <= 1}
        onClick={() => onPageChange(page - 1)}
      >
        {t("common.previous")}
      </Button>
      <span className="text-sm text-muted-foreground">
        {t("common.page")} {page} / {Math.max(totalPages, 1)}
      </span>
      <Button
        variant="outline"
        className="min-h-11"
        disabled={page >= totalPages}
        onClick={() => onPageChange(page + 1)}
      >
        {t("common.next")}
      </Button>
    </nav>
  );
}
