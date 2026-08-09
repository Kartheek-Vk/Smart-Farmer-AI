import { useState } from "react";
import { createFileRoute } from "@tanstack/react-router";
import { Bug, Upload } from "lucide-react";

import { PageHeader } from "@/components/common/page";
import { IconBubble } from "@/components/common/cards";
import { DataCard } from "@/components/app/list-primitives";
import { Button } from "@/components/ui/button";
import { Progress } from "@/components/ui/progress";
import { demoScans } from "@/data/demo";

export const Route = createFileRoute("/app/disease")({
  head: () => ({
    meta: [
      { title: "Disease detection — Smart Farmer AI" },
      { name: "description", content: "Scan a leaf photo and get disease, treatment and prevention guidance." },
      { name: "robots", content: "noindex" },
    ],
  }),
  component: DiseasePage,
});

function DiseasePage() {
  const [analysing, setAnalysing] = useState(false);
  const [result, setResult] = useState(demoScans[0]);

  const runScan = async () => {
    setAnalysing(true);
    await new Promise((r) => setTimeout(r, 1200));
    setResult(demoScans[0]);
    setAnalysing(false);
  };

  return (
    <div className="space-y-5">
      <PageHeader title="Disease detection" description="Upload a clear photo of the affected leaf." />
      <div className="grid gap-4 lg:grid-cols-2">
        <DataCard>
          <div className="flex flex-col items-center gap-3 rounded-2xl border-2 border-dashed border-border p-8 text-center">
            <IconBubble icon={Upload} tone="danger" />
            <p className="text-sm font-medium">Drop a leaf photo here</p>
            <p className="text-xs text-muted-foreground">JPG or PNG, under 5 MB, taken in daylight.</p>
            <Button onClick={runScan} className="min-h-11" disabled={analysing}>
              {analysing ? "Analysing…" : "Run AI scan"}
            </Button>
          </div>
        </DataCard>

        {result ? (
          <DataCard>
            <div className="flex items-center gap-3">
              <IconBubble icon={Bug} tone="danger" />
              <div className="min-w-0">
                <h2 className="truncate text-lg font-semibold">{result.diseaseName}</h2>
                <p className="text-sm text-muted-foreground">{result.cropName}</p>
              </div>
            </div>
            <p className="mt-4 text-xs text-muted-foreground">Confidence {Math.round(result.confidence * 100)}%</p>
            <Progress value={result.confidence * 100} className="mt-2" />
            {[
              { title: "Symptoms", items: result.symptoms },
              { title: "Treatment", items: result.actions },
              { title: "Prevention", items: result.prevention },
            ].map((block) => (
              <div key={block.title} className="mt-4">
                <h3 className="text-xs font-semibold uppercase tracking-wide text-muted-foreground">{block.title}</h3>
                <ul className="mt-1 space-y-1 text-sm">
                  {block.items.map((item) => (
                    <li key={item}>• {item}</li>
                  ))}
                </ul>
              </div>
            ))}
          </DataCard>
        ) : null}
      </div>

      <DataCard>
        <h2 className="font-semibold">Past scans</h2>
        <ul className="mt-3 space-y-3">
          {demoScans.map((scan) => (
            <li key={scan.id} className="flex items-center justify-between gap-3 rounded-xl border border-border p-3">
              <div className="min-w-0">
                <p className="truncate font-medium">{scan.diseaseName}</p>
                <p className="text-xs text-muted-foreground">
                  {scan.cropName} · {new Date(scan.createdAt).toLocaleDateString()}
                </p>
              </div>
              <span className="shrink-0 text-sm font-semibold">{Math.round(scan.confidence * 100)}%</span>
            </li>
          ))}
        </ul>
      </DataCard>
    </div>
  );
}
