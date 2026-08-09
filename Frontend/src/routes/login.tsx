import { useState } from "react";
import { createFileRoute, Link, useNavigate } from "@tanstack/react-router";
import { Eye, EyeOff } from "lucide-react";
import { toast } from "sonner";
import { z } from "zod";

import { AuthLayout } from "@/layouts/auth-layout";
import { Button } from "@/components/ui/button";
import { Checkbox } from "@/components/ui/checkbox";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";

export const Route = createFileRoute("/login")({
  head: () => ({
    meta: [
      { title: "Log in — Smart Farmer AI" },
      { name: "description", content: "Log in to your Smart Farmer AI account to reach your farms, scans and recommendations." },
      { property: "og:title", content: "Log in — Smart Farmer AI" },
      { property: "og:description", content: "Access your Smart Farmer AI dashboard." },
      { name: "robots", content: "noindex" },
    ],
  }),
  component: LoginPage,
});

const schema = z.object({
  email: z.string().trim().email("Enter a valid email").max(255),
  password: z.string().min(8, "Password must be at least 8 characters").max(128),
});

function LoginPage() {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [show, setShow] = useState(false);
  const [errors, setErrors] = useState<{ email?: string; password?: string }>({});
  const [loading, setLoading] = useState(false);

  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const parsed = schema.safeParse({ email, password });
    if (!parsed.success) {
      const next: { email?: string; password?: string } = {};
      for (const issue of parsed.error.issues) {
        const key = issue.path[0] as "email" | "password";
        if (!next[key]) next[key] = issue.message;
      }
      setErrors(next);
      return;
    }
    setErrors({});
    setLoading(true);
    await new Promise((r) => setTimeout(r, 600));
    setLoading(false);
    toast.success("Welcome back");
    void navigate({ to: "/app" });
  };

  return (
    <AuthLayout
      title="Welcome back"
      description="Log in to see your farms, scans and today's recommendations."
      footer={
        <>
          New here?{" "}
          <Link to="/register" className="font-semibold text-primary hover:underline">
            Create an account
          </Link>
        </>
      }
    >
      <form onSubmit={onSubmit} noValidate className="space-y-4">
        <div className="space-y-2">
          <Label htmlFor="login-email">Email</Label>
          <Input
            id="login-email"
            type="email"
            autoComplete="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            aria-invalid={!!errors.email}
            className="h-11"
          />
          {errors.email ? <p className="text-xs text-danger">{errors.email}</p> : null}
        </div>
        <div className="space-y-2">
          <Label htmlFor="login-password">Password</Label>
          <div className="relative">
            <Input
              id="login-password"
              type={show ? "text" : "password"}
              autoComplete="current-password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              aria-invalid={!!errors.password}
              className="h-11 pr-12"
            />
            <Button
              type="button"
              variant="ghost"
              size="icon"
              onClick={() => setShow((s) => !s)}
              aria-label={show ? "Hide password" : "Show password"}
              className="absolute right-1 top-1/2 min-h-9 min-w-9 -translate-y-1/2"
            >
              {show ? <EyeOff className="size-4" aria-hidden="true" /> : <Eye className="size-4" aria-hidden="true" />}
            </Button>
          </div>
          {errors.password ? <p className="text-xs text-danger">{errors.password}</p> : null}
        </div>
        <div className="flex items-center justify-between gap-3">
          <label className="flex items-center gap-2 text-sm text-muted-foreground">
            <Checkbox id="remember" />
            <span>Remember me</span>
          </label>
          <Link to="/forgot-password" className="text-sm font-medium text-primary hover:underline">
            Forgot password?
          </Link>
        </div>
        <Button type="submit" size="lg" className="min-h-12 w-full" disabled={loading}>
          {loading ? "Logging in…" : "Log in"}
        </Button>
        <Button asChild variant="outline" size="lg" className="min-h-12 w-full">
          <Link to="/admin">Continue as admin</Link>
        </Button>
      </form>
    </AuthLayout>
  );
}
