import { useState } from "react";
import { createFileRoute } from "@tanstack/react-router";
import { Mail, MapPin, Phone } from "lucide-react";
import { toast } from "sonner";
import { z } from "zod";

import { SiteLayout } from "@/layouts/site-layout";
import { Section, SectionHeading } from "@/components/common/page";
import { IconBubble } from "@/components/common/cards";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";

export const Route = createFileRoute("/contact")({
  head: () => ({
    meta: [
      { title: "Contact — Smart Farmer AI" },
      {
        name: "description",
        content:
          "Questions about Smart Farmer AI, partnerships or support? Send us a message and our team will get back to you.",
      },
      { property: "og:title", content: "Contact — Smart Farmer AI" },
      { property: "og:description", content: "Reach the Smart Farmer AI team for support or partnerships." },
    ],
  }),
  component: ContactPage,
});

const contactSchema = z.object({
  name: z.string().trim().min(1, "Name is required").max(100, "Name must be under 100 characters"),
  email: z.string().trim().email("Enter a valid email").max(255),
  subject: z.string().trim().min(1, "Subject is required").max(150),
  message: z.string().trim().min(10, "Tell us a little more").max(1000, "Message must be under 1000 characters"),
});

type ContactErrors = Partial<Record<keyof z.infer<typeof contactSchema>, string>>;

function ContactPage() {
  const [values, setValues] = useState({ name: "", email: "", subject: "", message: "" });
  const [errors, setErrors] = useState<ContactErrors>({});
  const [submitting, setSubmitting] = useState(false);

  const update = (key: keyof typeof values) => (value: string) =>
    setValues((prev) => ({ ...prev, [key]: value }));

  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const parsed = contactSchema.safeParse(values);
    if (!parsed.success) {
      const next: ContactErrors = {};
      for (const issue of parsed.error.issues) {
        const key = issue.path[0] as keyof ContactErrors;
        if (key && !next[key]) next[key] = issue.message;
      }
      setErrors(next);
      return;
    }
    setErrors({});
    setSubmitting(true);
    await new Promise((r) => setTimeout(r, 700));
    setSubmitting(false);
    setValues({ name: "", email: "", subject: "", message: "" });
    toast.success("Message sent", { description: "Our team will reply within two working days." });
  };

  return (
    <SiteLayout>
      <Section>
        <SectionHeading
          level={1}
          eyebrow="Contact"
          title="Talk to the Smart Farmer AI team"
          body="Support, partnerships, field pilots or feedback — we read every message."
        />
        <div className="grid gap-6 lg:grid-cols-[1fr_1.4fr]">
          <div className="space-y-3">
            {[
              { icon: Mail, title: "Email", value: "support@smartfarmer.ai" },
              { icon: Phone, title: "Phone", value: "+91 1800 000 000" },
              { icon: MapPin, title: "Office", value: "Pune, Maharashtra, India" },
            ].map((item) => (
              <Card key={item.title} className="surface-card border-0 shadow-none">
                <CardContent className="flex items-center gap-3 p-4">
                  <IconBubble icon={item.icon} />
                  <div className="min-w-0">
                    <p className="text-xs uppercase tracking-wide text-muted-foreground">{item.title}</p>
                    <p className="truncate font-medium">{item.value}</p>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>

          <Card className="surface-card border-0 shadow-none">
            <CardContent className="p-5 sm:p-6">
              <form onSubmit={onSubmit} noValidate className="space-y-4">
                <div className="grid gap-4 sm:grid-cols-2">
                  <div className="space-y-2">
                    <Label htmlFor="contact-name">Name</Label>
                    <Input
                      id="contact-name"
                      value={values.name}
                      onChange={(e) => update("name")(e.target.value)}
                      maxLength={100}
                      aria-invalid={!!errors.name}
                      className="h-11"
                    />
                    {errors.name ? <p className="text-xs text-danger">{errors.name}</p> : null}
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="contact-email">Email</Label>
                    <Input
                      id="contact-email"
                      type="email"
                      value={values.email}
                      onChange={(e) => update("email")(e.target.value)}
                      maxLength={255}
                      aria-invalid={!!errors.email}
                      className="h-11"
                    />
                    {errors.email ? <p className="text-xs text-danger">{errors.email}</p> : null}
                  </div>
                </div>
                <div className="space-y-2">
                  <Label htmlFor="contact-subject">Subject</Label>
                  <Input
                    id="contact-subject"
                    value={values.subject}
                    onChange={(e) => update("subject")(e.target.value)}
                    maxLength={150}
                    aria-invalid={!!errors.subject}
                    className="h-11"
                  />
                  {errors.subject ? <p className="text-xs text-danger">{errors.subject}</p> : null}
                </div>
                <div className="space-y-2">
                  <Label htmlFor="contact-message">Message</Label>
                  <Textarea
                    id="contact-message"
                    rows={6}
                    value={values.message}
                    onChange={(e) => update("message")(e.target.value)}
                    maxLength={1000}
                    aria-invalid={!!errors.message}
                  />
                  {errors.message ? <p className="text-xs text-danger">{errors.message}</p> : null}
                </div>
                <Button type="submit" size="lg" className="min-h-12 w-full sm:w-auto" disabled={submitting}>
                  {submitting ? "Sending…" : "Send message"}
                </Button>
              </form>
            </CardContent>
          </Card>
        </div>
      </Section>
    </SiteLayout>
  );
}
