import type { ReactNode } from "react";
import { AlertTriangle, Inbox, Loader2 } from "lucide-react";

import { Button } from "@/components/ui/button";
import { Skeleton } from "@/components/ui/skeleton";
import { cn } from "@/lib/utils";
import { useI18n } from "@/i18n/i18n-provider";
import type { ApiError } from "@/lib/api-client";

export function LoadingState({ label, rows = 3 }: { label?: string; rows?: number }) {
  const { t } = useI18n();
  return (
    <div className="space-y-3" role="status" aria-live="polite">
      <span className="sr-only">{label ?? t("common.loading")}</span>
      {Array.from({ length: rows }).map((_, i) => (
        <Skeleton key={i} className="h-24 w-full rounded-xl" />
      ))}
    </div>
  );
}

export function Spinner({ className }: { className?: string }) {
  return <Loader2 className={cn("size-4 animate-spin", className)} aria-hidden="true" />;
}

export function EmptyState({
  title,
  body,
  icon,
  action,
}: {
  title?: string;
  body?: string;
  icon?: ReactNode;
  action?: ReactNode;
}) {
  const { t } = useI18n();
  return (
    <div className="surface-card flex flex-col items-center gap-3 px-6 py-12 text-center">
      <div className="grid size-12 place-items-center rounded-full bg-muted text-muted-foreground">
        {icon ?? <Inbox className="size-5" aria-hidden="true" />}
      </div>
      <h3 className="text-base font-semibold">{title ?? t("states.emptyTitle")}</h3>
      <p className="max-w-sm text-sm text-muted-foreground">{body ?? t("states.emptyBody")}</p>
      {action}
    </div>
  );
}

export function ErrorState({
  title,
  body,
  onRetry,
}: {
  title?: string;
  body?: string;
  onRetry?: () => void;
}) {
  const { t } = useI18n();
  return (
    <div className="surface-card flex flex-col items-center gap-3 px-6 py-12 text-center" role="alert">
      <div className="grid size-12 place-items-center rounded-full bg-danger-soft text-danger">
        <AlertTriangle className="size-5" aria-hidden="true" />
      </div>
      <h3 className="text-base font-semibold">{title ?? t("states.errorTitle")}</h3>
      <p className="max-w-md text-sm text-muted-foreground">{body ?? t("states.errorBody")}</p>
      {onRetry ? (
        <Button variant="outline" onClick={onRetry} className="min-h-11">
          {t("common.retry")}
        </Button>
      ) : null}
    </div>
  );
}

interface QueryViewProps<T> {
  isLoading: boolean;
  isError: boolean;
  error?: unknown;
  data: T | undefined;
  isEmpty?: (data: T) => boolean;
  onRetry?: () => void;
  emptyTitle?: string;
  emptyBody?: string;
  skeletonRows?: number;
  children: (data: T) => ReactNode;
}

export function QueryView<T>({
  isLoading,
  isError,
  error,
  data,
  isEmpty,
  onRetry,
  emptyTitle,
  emptyBody,
  skeletonRows,
  children,
}: QueryViewProps<T>) {
  if (isLoading) return <LoadingState {...(skeletonRows ? { rows: skeletonRows } : {})} />;
  if (isError || !data) {
    const message = (error as ApiError | undefined)?.message;
    return <ErrorState {...(message ? { body: message } : {})} {...(onRetry ? { onRetry } : {})} />;
  }
  if (isEmpty?.(data)) {
    return (
      <EmptyState
        {...(emptyTitle ? { title: emptyTitle } : {})}
        {...(emptyBody ? { body: emptyBody } : {})}
      />
    );
  }
  return <>{children(data)}</>;
}
