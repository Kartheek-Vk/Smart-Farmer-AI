import { Monitor, Moon, Sun } from "lucide-react";

import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { useI18n } from "@/i18n/i18n-provider";
import { useTheme } from "@/providers/theme-provider";

export function ThemeToggle() {
  const { theme, setTheme } = useTheme();
  const { t } = useI18n();

  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <Button variant="ghost" size="icon" className="min-h-11 min-w-11" aria-label={t("theme.toggle")}>
          {theme === "dark" ? (
            <Moon className="size-4" aria-hidden="true" />
          ) : theme === "system" ? (
            <Monitor className="size-4" aria-hidden="true" />
          ) : (
            <Sun className="size-4" aria-hidden="true" />
          )}
        </Button>
      </DropdownMenuTrigger>
      <DropdownMenuContent align="end">
        <DropdownMenuItem onSelect={() => setTheme("light")}>
          <Sun className="size-4" aria-hidden="true" /> {t("theme.light")}
        </DropdownMenuItem>
        <DropdownMenuItem onSelect={() => setTheme("dark")}>
          <Moon className="size-4" aria-hidden="true" /> {t("theme.dark")}
        </DropdownMenuItem>
        <DropdownMenuItem onSelect={() => setTheme("system")}>
          <Monitor className="size-4" aria-hidden="true" /> {t("theme.system")}
        </DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  );
}
