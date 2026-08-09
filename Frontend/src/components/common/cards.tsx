import type { ComponentType, ReactNode } from "react";
import type { LucideIcon } from "lucide-react";
import { ArrowRight } from "lucide-react";
import { Link } from "@tanstack/react-router";

import { Badge } from "@/components/ui/badge";
import { Card, CardContent } from "@/components/ui/card";
import { cn } from "@/lib/utils";

export type Tone = "primary" | "weather" | "market" | "warning" | "danger" | "success";

const toneBg: Record<Tone, string> = {
  primary: "bg-primary-soft text-primary",
  weather: "bg-weather-soft text-weather",
  market: "bg-market-soft text-market",
  warning: "bg-warning-soft text-warning-foreground",
  danger: "bg-danger-soft text-danger",
  success: "bg-success-soft text-success",
};

export function IconBubble({
  icon: Icon,
  tone = "primary",
  className,
}: {
  icon: LucideIcon | ComponentType<{ className?: string }>;
  tone?: Tone;
  className?: string;
}) {
  return (
    <span className={cn("grid size-11 shrink-0 place-items-center rounded-xl", toneBg[tone], className)}>
      <Icon className="size-5" aria-hidden="true" />
    </span>
  );
}

export function StatCard({
  label,
  value,
  hint,
  icon,
  tone = "primary",
}: {
  label: string;
  value: string | number;
  hint?: string;
  icon?: LucideIcon;
  tone?: Tone;
}) {
  return (
    <Card className="surface-card border-0 shadow-none">
      <CardContent className="flex items-center gap-3 p-4 sm:p-5">
        {icon ? <IconBubble icon={icon} tone={tone} /> : null}
        <div className="min-w-0">
          <p className="truncate text-xs font-medium uppercase tracking-wide text-muted-foreground">
            {label}
          </p>
          <p className="text-xl font-bold sm:text-2xl">{value}</p>
          {hint ? <p className="truncate text-xs text-muted-foreground">{hint}</p> : null}
        </div>
      </CardContent>
    </Card>
  );
}

export function FeatureCard({
  icon,
  title,
  description,
  tone = "primary",
  footer,
}: {
  icon: LucideIcon;
  title: string;
  description: string;
  tone?: Tone;
  footer?: ReactNode;
}) {
  return (
    <Card className="surface-card h-full border-0 shadow-none transition-shadow hover:shadow-[var(--shadow-soft)]">
      <CardContent className="flex h-full flex-col gap-3 p-5">
        <IconBubble icon={icon} tone={tone} />
        <h3 className="text-base font-semibold">{title}</h3>
        <p className="text-sm leading-relaxed text-muted-foreground">{description}</p>
        {footer ? <div className="mt-auto pt-2">{footer}</div> : null}
      </CardContent>
    </Card>
  );
}

export function ActionCard({
  icon,
  label,
  to,
  tone = "primary",
}: {
  icon: LucideIcon;
  label: string;
  to: string;
  tone?: Tone;
}) {
  return (
    <Link
      to={to}
      className="surface-card flex min-h-24 flex-col items-center justify-center gap-2 p-3 text-center transition-colors hover:bg-accent/60"
    >
      <IconBubble icon={icon} tone={tone} />
      <span className="text-xs font-medium leading-tight sm:text-sm">{label}</span>
    </Link>
  );
}

export function StatusBadge({ status, tone = "primary" }: { status: string; tone?: Tone }) {
  return (
    <Badge variant="secondary" className={cn("rounded-full border-0 font-medium", toneBg[tone])}>
      {status}
    </Badge>
  );
}

export function LinkCard({
  title,
  description,
  to,
  icon,
  tone = "primary",
}: {
  title: string;
  description: string;
  to: string;
  icon: LucideIcon;
  tone?: Tone;
}) {
  return (
    <Link to={to} className="surface-card group flex items-start gap-3 p-4 transition-colors hover:bg-accent/50">
      <IconBubble icon={icon} tone={tone} />
      <span className="min-w-0 flex-1">
        <span className="flex items-center gap-1 font-semibold">
          {title}
          <ArrowRight className="size-4 opacity-0 transition-opacity group-hover:opacity-100" aria-hidden="true" />
        </span>
        <span className="mt-1 block text-sm text-muted-foreground">{description}</span>
      </span>
    </Link>
  );
}
