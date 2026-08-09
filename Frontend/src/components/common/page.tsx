import type { ElementType, ReactNode } from "react";
import { motion, useReducedMotion } from "motion/react";

import { cn } from "@/lib/utils";

export function PageContainer({
  children,
  className,
  as: Tag = "div",
}: {
  children: ReactNode;
  className?: string;
  as?: ElementType;
}) {
  return <Tag className={cn("mx-auto w-full max-w-7xl px-4 sm:px-6 lg:px-8", className)}>{children}</Tag>;
}

export function Section({
  children,
  className,
  id,
  muted = false,
}: {
  children: ReactNode;
  className?: string;
  id?: string;
  muted?: boolean;
}) {
  return (
    <section id={id} className={cn("py-14 sm:py-20", muted && "bg-muted/40", className)}>
      <PageContainer>{children}</PageContainer>
    </section>
  );
}

export function SectionHeading({
  eyebrow,
  title,
  body,
  align = "center",
  level = 2,
}: {
  eyebrow?: string;
  title: string;
  body?: string;
  align?: "center" | "left";
  level?: 1 | 2 | 3;
}) {
  const Heading = (`h${level}` as ElementType) satisfies ElementType;
  return (
    <div className={cn("mb-10 max-w-2xl", align === "center" && "mx-auto text-center")}>
      {eyebrow ? (
        <p className="mb-2 text-xs font-semibold uppercase tracking-widest text-primary">{eyebrow}</p>
      ) : null}
      <Heading className="text-balance text-2xl font-bold tracking-tight sm:text-3xl lg:text-4xl">
        {title}
      </Heading>
      {body ? <p className="mt-3 text-pretty text-sm text-muted-foreground sm:text-base">{body}</p> : null}
    </div>
  );
}

export function ResponsiveGrid({
  children,
  className,
  cols = "3",
}: {
  children: ReactNode;
  className?: string;
  cols?: "2" | "3" | "4";
}) {
  const map: Record<string, string> = {
    "2": "sm:grid-cols-2",
    "3": "sm:grid-cols-2 lg:grid-cols-3",
    "4": "sm:grid-cols-2 lg:grid-cols-4",
  };
  return <div className={cn("grid grid-cols-1 gap-4", map[cols], className)}>{children}</div>;
}

export function FadeIn({
  children,
  delay = 0,
  className,
}: {
  children: ReactNode;
  delay?: number;
  className?: string;
}) {
  const reduce = useReducedMotion();
  if (reduce) return <div className={className}>{children}</div>;
  return (
    <motion.div
      initial={{ opacity: 0, y: 12 }}
      whileInView={{ opacity: 1, y: 0 }}
      viewport={{ once: true, margin: "-60px" }}
      transition={{ duration: 0.35, delay, ease: "easeOut" }}
      className={className}
    >
      {children}
    </motion.div>
  );
}

export function PageHeader({
  title,
  description,
  actions,
}: {
  title: string;
  description?: string;
  actions?: ReactNode;
}) {
  return (
    <header className="mb-6 grid grid-cols-[minmax(0,1fr)_auto] items-center gap-4 sm:flex sm:flex-wrap sm:justify-between">
      <div className="min-w-0">
        <h1 className="truncate text-xl font-bold tracking-tight sm:text-2xl">{title}</h1>
        {description ? <p className="mt-1 text-sm text-muted-foreground">{description}</p> : null}
      </div>
      {actions ? <div className="flex shrink-0 items-center gap-2">{actions}</div> : null}
    </header>
  );
}
