import { useEffect, useState } from "react";
import { Download, Smartphone } from "lucide-react";

import { Button } from "@/components/ui/button";
import { useI18n } from "@/i18n/i18n-provider";

interface BeforeInstallPromptEvent extends Event {
  prompt: () => Promise<void>;
  userChoice: Promise<{ outcome: "accepted" | "dismissed" }>;
}

export function useInstallPrompt() {
  const [deferred, setDeferred] = useState<BeforeInstallPromptEvent | null>(null);
  const [installed, setInstalled] = useState(false);

  useEffect(() => {
    const onPrompt = (event: Event) => {
      event.preventDefault();
      setDeferred(event as BeforeInstallPromptEvent);
    };
    const onInstalled = () => {
      setInstalled(true);
      setDeferred(null);
    };
    window.addEventListener("beforeinstallprompt", onPrompt);
    window.addEventListener("appinstalled", onInstalled);
    if (window.matchMedia("(display-mode: standalone)").matches) setInstalled(true);
    return () => {
      window.removeEventListener("beforeinstallprompt", onPrompt);
      window.removeEventListener("appinstalled", onInstalled);
    };
  }, []);

  const install = async () => {
    if (!deferred) return;
    await deferred.prompt();
    await deferred.userChoice;
    setDeferred(null);
  };

  return { canInstall: Boolean(deferred), installed, install };
}

export function InstallAppButton({ className }: { className?: string }) {
  const { canInstall, installed, install } = useInstallPrompt();
  const { t } = useI18n();

  if (installed) return null;

  return canInstall ? (
    <Button onClick={() => void install()} className={className}>
      <Download className="size-4" aria-hidden="true" />
      {t("home.installCta")}
    </Button>
  ) : (
    <p className={`text-sm text-muted-foreground ${className ?? ""}`}>
      <Smartphone className="mr-1 inline size-4" aria-hidden="true" />
      {t("home.installUnavailable")}
    </p>
  );
}
