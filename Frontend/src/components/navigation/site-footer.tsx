import { Link } from "@tanstack/react-router";
import { Facebook, Instagram, Linkedin, Mail, MapPin, Phone, Youtube } from "lucide-react";

import { BrandMark } from "@/components/navigation/site-header";
import { PageContainer } from "@/components/common/page";
import { LANGUAGES } from "@/i18n/languages";
import { useI18n } from "@/i18n/i18n-provider";

const quickLinks = [
  { to: "/", label: "Home" },
  { to: "/about", label: "About Us" },
  { to: "/how-it-works", label: "How It Works" },
  { to: "/contact", label: "Contact" },
];

const featureLinks = [
  { to: "/features", label: "All Features" },
  { to: "/ai-modules", label: "AI Modules" },
  { to: "/app/market", label: "Market Prices" },
  { to: "/app/schemes", label: "Government Schemes" },
];

const resourceLinks = [
  { to: "/faq", label: "FAQ" },
  { to: "/app/assistant", label: "AI Assistant" },
  { to: "/app/weather", label: "Weather" },
  { to: "/login", label: "Log in" },
];

const socials = [
  { label: "Facebook", icon: Facebook },
  { label: "Instagram", icon: Instagram },
  { label: "YouTube", icon: Youtube },
  { label: "LinkedIn", icon: Linkedin },
];

export function SiteFooter() {
  const { t } = useI18n();
  const year = new Date().getFullYear();

  return (
    <footer className="border-t border-border bg-muted/30">
      <PageContainer className="py-12">
        <div className="grid gap-10 md:grid-cols-2 lg:grid-cols-5">
          <div className="lg:col-span-2">
            <BrandMark />
            <p className="mt-3 max-w-sm text-sm text-muted-foreground">{t("brand.tagline")}</p>
            <ul className="mt-4 space-y-2 text-sm text-muted-foreground">
              <li className="flex items-center gap-2">
                <Mail className="size-4 shrink-0" aria-hidden="true" />
                <a href="mailto:support@smartfarmerai.in" className="hover:text-foreground">
                  support@smartfarmerai.in
                </a>
              </li>
              <li className="flex items-center gap-2">
                <Phone className="size-4 shrink-0" aria-hidden="true" />
                <a href="tel:+911800000000" className="hover:text-foreground">
                  1800 000 000
                </a>
              </li>
              <li className="flex items-center gap-2">
                <MapPin className="size-4 shrink-0" aria-hidden="true" />
                Hyderabad, Telangana, India
              </li>
            </ul>
            <div className="mt-4 flex gap-2">
              {socials.map((s) => (
                <a
                  key={s.label}
                  href="#"
                  aria-label={s.label}
                  className="grid size-11 place-items-center rounded-xl border border-border bg-background text-muted-foreground transition-colors hover:text-foreground"
                >
                  <s.icon className="size-4" aria-hidden="true" />
                </a>
              ))}
            </div>
          </div>

          <FooterColumn title={t("footer.quickLinks")} links={quickLinks} />
          <FooterColumn title={t("footer.features")} links={featureLinks} />
          <FooterColumn title={t("footer.resources")} links={resourceLinks} />
        </div>

        <div className="mt-10 border-t border-border pt-6">
          <p className="text-xs font-semibold uppercase tracking-wide text-muted-foreground">
            {t("footer.languages")}
          </p>
          <p className="mt-2 text-sm text-muted-foreground">
            {LANGUAGES.map((l) => l.nativeLabel).join(" · ")}
          </p>
        </div>

        <div className="mt-6 flex flex-col gap-2 border-t border-border pt-6 text-xs text-muted-foreground sm:flex-row sm:items-center sm:justify-between">
          <p>
            © {year} {t("brand.name")}. {t("footer.rights")}
          </p>
          <p>{t("footer.disclaimer")}</p>
        </div>
      </PageContainer>
    </footer>
  );
}

function FooterColumn({ title, links }: { title: string; links: { to: string; label: string }[] }) {
  return (
    <nav aria-label={title}>
      <h2 className="text-sm font-semibold">{title}</h2>
      <ul className="mt-3 space-y-2">
        {links.map((link) => (
          <li key={link.label}>
            <Link to={link.to} className="text-sm text-muted-foreground hover:text-foreground">
              {link.label}
            </Link>
          </li>
        ))}
      </ul>
    </nav>
  );
}
