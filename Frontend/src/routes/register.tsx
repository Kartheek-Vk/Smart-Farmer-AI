import { useState } from "react";
import { createFileRoute, Link, useNavigate } from "@tanstack/react-router";
import { toast } from "sonner";
import { z } from "zod";

import { AuthLayout } from "@/layouts/auth-layout";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { LANGUAGES } from "@/i18n/languages";

export const Route = createFileRoute("/register")({
  head: () => ({
    meta: [
      { title: "Create account — Smart Farmer AI" },
      { name: "description", content: "Create a free Smart Farmer AI account and get your first crop recommendation in minutes." },
      { property: "og:title", content: "Create account — Smart Farmer AI" },
      { property: "og:description", content: "Free registration for farmers, experts, dealers and NGOs." },
      { name: "robots", content: "noindex" },
    ],
  }),
  component: RegisterPage,
});

const ROLES = ["FARMER", "EXPERT", "DEALER", "NGO", "GOVERNMENT"] as const;

const schema = z
  .object({
    name: z.string().trim().min(2, "Name is required").max(100),
    email: z.string().trim().email("Enter a valid email").max(255),
    phone: z
      .string()
      .trim()
      .regex(/^[0-9+\-\s]{8,15}$/, "Enter a valid phone number"),
    role: z.enum(ROLES),
    language: z.string().min(2),
    password: z.string().min(8, "Password must be at least 8 characters").max(128),
    confirm: z.string(),
  })
  .refine((v) => v.password === v.confirm, { path: ["confirm"], message: "Passwords do not match" });

type FormValues = z.infer<typeof schema>;
type FormErrors = Partial<Record<keyof FormValues, string>>;

function RegisterPage() {
  const navigate = useNavigate();
  const [values, setValues] = useState<FormValues>({
    name: "",
    email: "",
    phone: "",
    role: "FARMER",
    language: "en",
    password: "",
    confirm: "",
  });
  const [errors, setErrors] = useState<FormErrors>({});
  const [loading, setLoading] = useState(false);

  const set = <K extends keyof FormValues>(key: K, value: FormValues[K]) =>
    setValues((prev) => ({ ...prev, [key]: value }));

  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const parsed = schema.safeParse(values);
    if (!parsed.success) {
      const next: FormErrors = {};
      for (const issue of parsed.error.issues) {
        const key = issue.path[0] as keyof FormValues;
        if (key && !next[key]) next[key] = issue.message;
      }
      setErrors(next);
      return;
    }
    setErrors({});
    setLoading(true);
    await new Promise((r) => setTimeout(r, 700));
    setLoading(false);
    toast.success("Account created", { description: "Let's add your first farm." });
    void navigate({ to: "/app/farm" });
  };

  return (
    <AuthLayout
      title="Create your account"
      description="Free to start. Add your farm and get advice the same day."
      footer={
        <>
          Already registered?{" "}
          <Link to="/login" className="font-semibold text-primary hover:underline">
            Log in
          </Link>
        </>
      }
    >
      <form onSubmit={onSubmit} noValidate className="space-y-4">
        <div className="space-y-2">
          <Label htmlFor="reg-name">Full name</Label>
          <Input
            id="reg-name"
            value={values.name}
            onChange={(e) => set("name", e.target.value)}
            maxLength={100}
            aria-invalid={!!errors.name}
            className="h-11"
          />
          {errors.name ? <p className="text-xs text-danger">{errors.name}</p> : null}
        </div>
        <div className="grid gap-4 sm:grid-cols-2">
          <div className="space-y-2">
            <Label htmlFor="reg-email">Email</Label>
            <Input
              id="reg-email"
              type="email"
              value={values.email}
              onChange={(e) => set("email", e.target.value)}
              maxLength={255}
              aria-invalid={!!errors.email}
              className="h-11"
            />
            {errors.email ? <p className="text-xs text-danger">{errors.email}</p> : null}
          </div>
          <div className="space-y-2">
            <Label htmlFor="reg-phone">Phone</Label>
            <Input
              id="reg-phone"
              type="tel"
              value={values.phone}
              onChange={(e) => set("phone", e.target.value)}
              maxLength={15}
              aria-invalid={!!errors.phone}
              className="h-11"
            />
            {errors.phone ? <p className="text-xs text-danger">{errors.phone}</p> : null}
          </div>
        </div>
        <div className="grid gap-4 sm:grid-cols-2">
          <div className="space-y-2">
            <Label htmlFor="reg-role">I am a</Label>
            <Select value={values.role} onValueChange={(v) => set("role", v as FormValues["role"])}>
              <SelectTrigger id="reg-role" className="h-11 w-full">
                <SelectValue />
              </SelectTrigger>
              <SelectContent>
                {ROLES.map((role) => (
                  <SelectItem key={role} value={role}>
                    {role.charAt(0) + role.slice(1).toLowerCase()}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>
          <div className="space-y-2">
            <Label htmlFor="reg-language">Preferred language</Label>
            <Select value={values.language} onValueChange={(v) => set("language", v)}>
              <SelectTrigger id="reg-language" className="h-11 w-full">
                <SelectValue />
              </SelectTrigger>
              <SelectContent>
                {LANGUAGES.map((lang) => (
                  <SelectItem key={lang.code} value={lang.code}>
                    {lang.nativeLabel}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>
        </div>
        <div className="grid gap-4 sm:grid-cols-2">
          <div className="space-y-2">
            <Label htmlFor="reg-password">Password</Label>
            <Input
              id="reg-password"
              type="password"
              autoComplete="new-password"
              value={values.password}
              onChange={(e) => set("password", e.target.value)}
              aria-invalid={!!errors.password}
              className="h-11"
            />
            {errors.password ? <p className="text-xs text-danger">{errors.password}</p> : null}
          </div>
          <div className="space-y-2">
            <Label htmlFor="reg-confirm">Confirm password</Label>
            <Input
              id="reg-confirm"
              type="password"
              autoComplete="new-password"
              value={values.confirm}
              onChange={(e) => set("confirm", e.target.value)}
              aria-invalid={!!errors.confirm}
              className="h-11"
            />
            {errors.confirm ? <p className="text-xs text-danger">{errors.confirm}</p> : null}
          </div>
        </div>
        <Button type="submit" size="lg" className="min-h-12 w-full" disabled={loading}>
          {loading ? "Creating account…" : "Create account"}
        </Button>
      </form>
    </AuthLayout>
  );
}
