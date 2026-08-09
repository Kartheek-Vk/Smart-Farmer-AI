import { createContext, useCallback, useContext, useEffect, useMemo, useState } from "react";
import type { ReactNode } from "react";

import { DEFAULT_LANGUAGE, LANGUAGES } from "./languages";
import { en } from "./locales/en";
import { hi } from "./locales/hi";
import type { TranslationShape } from "./locales/en";

type DeepPartial<T> = { [K in keyof T]?: Partial<T[K]> };

const dictionaries: Record<string, DeepPartial<TranslationShape>> = { en, hi };

const STORAGE_KEY = "sfa.language";

interface I18nValue {
  language: string;
  setLanguage: (code: string) => void;
  t: (key: string) => string;
  languages: typeof LANGUAGES;
}

const I18nContext = createContext<I18nValue | null>(null);

function lookup(dict: Record<string, unknown> | undefined, key: string): string | undefined {
  if (!dict) return undefined;
  const parts = key.split(".");
  const group = parts[0] ?? "";
  const entry = parts[1] ?? "";
  const section = dict[group] as Record<string, string> | undefined;
  const value = section?.[entry];
  return typeof value === "string" ? value : undefined;
}

export function I18nProvider({ children }: { children: ReactNode }) {
  const [language, setLanguageState] = useState<string>(DEFAULT_LANGUAGE);

  useEffect(() => {
    const stored = window.localStorage.getItem(STORAGE_KEY);
    if (stored && LANGUAGES.some((l) => l.code === stored)) setLanguageState(stored);
  }, []);

  const setLanguage = useCallback((code: string) => {
    setLanguageState(code);
    window.localStorage.setItem(STORAGE_KEY, code);
    document.documentElement.lang = code;
  }, []);

  const t = useCallback(
    (key: string) =>
      lookup(dictionaries[language] as Record<string, unknown>, key) ??
      lookup(en as unknown as Record<string, unknown>, key) ??
      key,
    [language],
  );

  const value = useMemo<I18nValue>(
    () => ({ language, setLanguage, t, languages: LANGUAGES }),
    [language, setLanguage, t],
  );

  return <I18nContext.Provider value={value}>{children}</I18nContext.Provider>;
}

export function useI18n(): I18nValue {
  const ctx = useContext(I18nContext);
  if (!ctx) throw new Error("useI18n must be used inside I18nProvider");
  return ctx;
}

export function useTranslation() {
  return useI18n();
}
