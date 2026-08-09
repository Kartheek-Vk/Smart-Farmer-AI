import { Languages } from "lucide-react";

import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { useI18n } from "@/i18n/i18n-provider";

export function LanguageSelector({ compact = false }: { compact?: boolean }) {
  const { language, setLanguage, languages, t } = useI18n();
  const active = languages.find((l) => l.code === language);

  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <Button
          variant="ghost"
          className="min-h-11 gap-2 px-3"
          aria-label={t("nav.language")}
        >
          <Languages className="size-4" aria-hidden="true" />
          {!compact && <span className="text-sm font-medium">{active?.nativeLabel ?? "English"}</span>}
        </Button>
      </DropdownMenuTrigger>
      <DropdownMenuContent align="end" className="max-h-80 w-56 overflow-y-auto">
        <DropdownMenuLabel>{t("nav.language")}</DropdownMenuLabel>
        <DropdownMenuSeparator />
        {languages.map((l) => (
          <DropdownMenuItem
            key={l.code}
            onSelect={() => setLanguage(l.code)}
            className="flex items-center justify-between"
          >
            <span>{l.nativeLabel}</span>
            <span className="text-xs text-muted-foreground">{l.label}</span>
          </DropdownMenuItem>
        ))}
      </DropdownMenuContent>
    </DropdownMenu>
  );
}
