import { createFileRoute, Link } from "@tanstack/react-router";
import { Bug, CloudSun, Leaf, MapPin, TrendingDown, TrendingUp } from "lucide-react";

import { PageHeader } from "@/components/common/page";
import { ActionCard, IconBubble, StatCard, StatusBadge } from "@/components/common/cards";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Progress } from "@/components/ui/progress";
import { QUICK_ACTIONS } from "@/config/navigation";
import {
  demoFarms,
  demoFields,
  demoMarketPrices,
  demoRecommendations,
  demoScans,
  demoUser,
  demoWeather,
} from "@/data/demo";

export const Route = createFileRoute("/app/")({
  head: () => ({
    meta: [
      { title: "Dashboard — Smart Farmer AI" },
      { name: "description", content: "Today's farm overview: weather, alerts, recommendations and market prices." },
      { name: "robots", content: "noindex" },
    ],
  }),
  component: DashboardPage,
});

function DashboardPage() {
  const latestScan = demoScans[0];
  const topPrices = demoMarketPrices.slice(0, 3);

  return (
    <div className="space-y-6">
      <PageHeader
        title={`Namaste, ${demoUser.name.split(" ")[0]}`}
        description="Here's what your fields need today."
        actions={
          <Button asChild className="min-h-11">
            <Link to="/app/disease">Scan a leaf</Link>
          </Button>
        }
      />

      <div className="grid grid-cols-2 gap-4 lg:grid-cols-4">
        <StatCard label="Farms" value={demoFarms.length} icon={MapPin} hint="2 active seasons" />
        <StatCard label="Fields" value={demoFields.length} icon={Leaf} tone="success" hint="11.7 acres total" />
        <StatCard label="Scans" value={demoScans.length} icon={Bug} tone="danger" hint="1 needs action" />
        <StatCard
          label="Advice"
          value={demoRecommendations.length}
          icon={CloudSun}
          tone="weather"
          hint="Updated today"
        />
      </div>

      <section aria-labelledby="quick-actions">
        <h2 id="quick-actions" className="mb-3 text-sm font-semibold uppercase tracking-wide text-muted-foreground">
          Quick actions
        </h2>
        <div className="grid grid-cols-2 gap-3 sm:grid-cols-4 lg:grid-cols-8">
          {QUICK_ACTIONS.map((action) => (
            <ActionCard
              key={action.label}
              to={action.to}
              label={action.label}
              icon={action.icon}
              tone={action.tone}
            />
          ))}
        </div>
      </section>

      <div className="grid gap-4 lg:grid-cols-3">
        <Card className="surface-card border-0 shadow-none lg:col-span-2">
          <CardContent className="p-5">
            <div className="flex items-start justify-between gap-3">
              <div className="min-w-0">
                <h2 className="font-semibold">Weather · {demoWeather.location}</h2>
                <p className="text-sm text-muted-foreground">{demoWeather.condition}</p>
              </div>
              <p className="text-3xl font-extrabold">{demoWeather.temperatureC}°C</p>
            </div>
            <div className="mt-4 grid grid-cols-2 gap-3 text-sm sm:grid-cols-4">
              {[
                { label: "Humidity", value: `${demoWeather.humidity}%` },
                { label: "Wind", value: `${demoWeather.windKph} km/h` },
                { label: "Rain today", value: `${demoWeather.rainMm} mm` },
                { label: "Alerts", value: `${demoWeather.alerts.length}` },
              ].map((item) => (
                <div key={item.label} className="rounded-xl bg-muted/60 p-3">
                  <p className="text-xs text-muted-foreground">{item.label}</p>
                  <p className="font-semibold">{item.value}</p>
                </div>
              ))}
            </div>
            {demoWeather.advice ? (
              <p className="mt-4 rounded-xl bg-weather-soft p-3 text-sm text-weather">{demoWeather.advice}</p>
            ) : null}
            <Button asChild variant="outline" className="mt-4 min-h-11">
              <Link to="/app/weather">Full forecast</Link>
            </Button>
          </CardContent>
        </Card>

        <Card className="surface-card border-0 shadow-none">
          <CardContent className="p-5">
            <h2 className="font-semibold">Latest scan</h2>
            {latestScan ? (
              <>
                <div className="mt-3 flex items-center gap-3">
                  <IconBubble icon={Bug} tone="danger" />
                  <div className="min-w-0">
                    <p className="truncate font-semibold">{latestScan.diseaseName}</p>
                    <p className="text-xs text-muted-foreground">{latestScan.cropName}</p>
                  </div>
                </div>
                <p className="mt-4 text-xs text-muted-foreground">
                  Confidence {Math.round(latestScan.confidence * 100)}%
                </p>
                <Progress value={latestScan.confidence * 100} className="mt-2" />
                <ul className="mt-4 space-y-1 text-sm text-muted-foreground">
                  {latestScan.actions.slice(0, 2).map((action) => (
                    <li key={action}>• {action}</li>
                  ))}
                </ul>
                <Button asChild variant="outline" className="mt-4 min-h-11 w-full">
                  <Link to="/app/disease">Open disease centre</Link>
                </Button>
              </>
            ) : null}
          </CardContent>
        </Card>
      </div>

      <div className="grid gap-4 lg:grid-cols-2">
        <Card className="surface-card border-0 shadow-none">
          <CardContent className="p-5">
            <div className="flex items-center justify-between gap-3">
              <h2 className="font-semibold">Latest recommendations</h2>
              <Link to="/app/recommendations" className="text-sm font-medium text-primary hover:underline">
                View all
              </Link>
            </div>
            <ul className="mt-4 space-y-3">
              {demoRecommendations.slice(0, 3).map((rec) => (
                <li key={rec.id} className="rounded-xl border border-border p-3">
                  <div className="flex items-center justify-between gap-2">
                    <p className="min-w-0 truncate font-medium">{rec.title}</p>
                    <StatusBadge status={rec.type} tone="primary" />
                  </div>
                  <p className="mt-1 text-sm text-muted-foreground">{rec.summary}</p>
                </li>
              ))}
            </ul>
          </CardContent>
        </Card>

        <Card className="surface-card border-0 shadow-none">
          <CardContent className="p-5">
            <div className="flex items-center justify-between gap-3">
              <h2 className="font-semibold">Market watch</h2>
              <Link to="/app/market" className="text-sm font-medium text-primary hover:underline">
                All prices
              </Link>
            </div>
            <ul className="mt-4 space-y-3">
              {topPrices.map((price) => {
                const up = price.changePercent >= 0;
                return (
                  <li key={price.id} className="flex items-center justify-between gap-3 rounded-xl border border-border p-3">
                    <div className="min-w-0">
                      <p className="truncate font-medium">{price.crop}</p>
                      <p className="text-xs text-muted-foreground">{price.market}</p>
                    </div>
                    <div className="shrink-0 text-right">
                      <p className="font-semibold">₹{price.todayPrice.toLocaleString("en-IN")}</p>
                      <p className={up ? "flex items-center gap-1 text-xs text-success" : "flex items-center gap-1 text-xs text-danger"}>
                        {up ? <TrendingUp className="size-3" aria-hidden="true" /> : <TrendingDown className="size-3" aria-hidden="true" />}
                        {price.changePercent}%
                      </p>
                    </div>
                  </li>
                );
              })}
            </ul>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
