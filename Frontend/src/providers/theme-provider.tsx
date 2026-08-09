import { createContext, useCallback, useContext, useEffect, useMemo, useState } from "react";
import type { ReactNode } from "react";

export type ThemeMode = "light" | "dark" | "system";

const STORAGE_KEY = "sfa.theme";

interface ThemeValue {
  theme: ThemeMode;
  resolvedTheme: "light" | "dark";
  setTheme: (mode: ThemeMode) => void;
}

const ThemeContext = createContext<ThemeValue | null>(null);

function systemPrefersDark(): boolean {
  return window.matchMedia("(prefers-color-scheme: dark)").matches;
}

export function ThemeProvider({ children }: { children: ReactNode }) {
  const [theme, setThemeState] = useState<ThemeMode>("light");
  const [resolvedTheme, setResolvedTheme] = useState<"light" | "dark">("light");

  const apply = useCallback((mode: ThemeMode) => {
    const dark = mode === "dark" || (mode === "system" && systemPrefersDark());
    document.documentElement.classList.toggle("dark", dark);
    setResolvedTheme(dark ? "dark" : "light");
  }, []);

  useEffect(() => {
    const stored = window.localStorage.getItem(STORAGE_KEY) as ThemeMode | null;
    const initial: ThemeMode = stored ?? "light";
    setThemeState(initial);
    apply(initial);
  }, [apply]);

  useEffect(() => {
    if (theme !== "system") return;
    const media = window.matchMedia("(prefers-color-scheme: dark)");
    const handler = () => apply("system");
    media.addEventListener("change", handler);
    return () => media.removeEventListener("change", handler);
  }, [theme, apply]);

  const setTheme = useCallback(
    (mode: ThemeMode) => {
      setThemeState(mode);
      window.localStorage.setItem(STORAGE_KEY, mode);
      apply(mode);
    },
    [apply],
  );

  const value = useMemo(() => ({ theme, resolvedTheme, setTheme }), [theme, resolvedTheme, setTheme]);

  return <ThemeContext.Provider value={value}>{children}</ThemeContext.Provider>;
}

export function useTheme(): ThemeValue {
  const ctx = useContext(ThemeContext);
  if (!ctx) throw new Error("useTheme must be used inside ThemeProvider");
  return ctx;
}
