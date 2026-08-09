import { createFileRoute } from "@tanstack/react-router";
import { AlertTriangle, CloudRain, Droplets, Wind } from "lucide-react";

import { PageHeader } from "@/components/common/page";
import { StatCard } from "@/components/common/cards";
import { DataCard } from "@/components/app/list-primitives";
import { demoWeather } from "@/data/demo";

export const Route = createFileRoute("/app/weather")({
  head: () => ({
    meta: [
      { title: "Weather — Smart Farmer AI" },
      { name: "description", content: "Seven-day forecast, rainfall outlook and severe weather alerts for your farm." },
      { name: "robots", content: "noindex" },
    ],
  }),
  component: WeatherPage,
});

function WeatherPage() {
  return (
    <div className="space-y-5">
      <PageHeader title="Weather" description={demoWeather.location} />
      <div className="grid grid-cols-2 gap-4 lg:grid-cols-4">
        <StatCard label="Temperature" value={`${demoWeather.temperatureC}°C`} tone="weather" hint={demoWeather.condition} />
        <StatCard label="Humidity" value={`${demoWeather.humidity}%`} icon={Droplets} tone="weather" />
        <StatCard label="Wind" value={`${demoWeather.windKph} km/h`} icon={Wind} tone="weather" />
        <StatCard label="Rain today" value={`${demoWeather.rainMm} mm`} icon={CloudRain} tone="weather" />
      </div>

      {demoWeather.alerts.map((alert) => (
        <div key={alert.id} className="surface-card flex items-start gap-3 p-4" role="alert">
          <AlertTriangle className="mt-0.5 size-5 shrink-0 text-warning-foreground" aria-hidden="true" />
          <div className="min-w-0">
            <p className="font-semibold">{alert.title}</p>
            <p className="text-sm text-muted-foreground">{alert.body}</p>
          </div>
        </div>
      ))}

      <DataCard>
        <h2 className="font-semibold">7-day forecast</h2>
        <ul className="mt-3 grid gap-3 sm:grid-cols-3 lg:grid-cols-7">
          {demoWeather.forecast.map((day) => (
            <li key={day.date} className="rounded-xl border border-border p-3 text-center">
              <p className="text-sm font-semibold">{day.date}</p>
              <p className="mt-1 text-xs text-muted-foreground">{day.condition}</p>
              <p className="mt-2 text-sm font-medium">{day.maxC}° / {day.minC}°</p>
              <p className="text-xs text-weather">{day.rainMm} mm</p>
            </li>
          ))}
        </ul>
      </DataCard>

      {demoWeather.advice ? (
        <DataCard>
          <h2 className="font-semibold">Farm advice</h2>
          <p className="mt-2 text-sm text-muted-foreground">{demoWeather.advice}</p>
        </DataCard>
      ) : null}
    </div>
  );
}
