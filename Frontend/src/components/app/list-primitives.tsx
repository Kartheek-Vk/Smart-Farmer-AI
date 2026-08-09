import type { ReactNode } from "react";
import { Card, CardContent } from "@/components/ui/card";

export function DataCard({ children, className }: { children: ReactNode; className?: string }) {
  return (
    <Card className={"surface-card border-0 shadow-none " + (className ?? "")}>
      <CardContent className="p-5">{children}</CardContent>
    </Card>
  );
}

export function DetailRow({ label, value }: { label: string; value: ReactNode }) {
  return (
    <div className="flex items-center justify-between gap-3 border-b border-border py-2 last:border-0">
      <span className="text-sm text-muted-foreground">{label}</span>
      <span className="min-w-0 truncate text-sm font-medium">{value}</span>
    </div>
  );
}
