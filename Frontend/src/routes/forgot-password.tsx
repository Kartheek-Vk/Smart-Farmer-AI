import { useState } from "react";
import { createFileRoute, Link } from "@tanstack/react-router";
import { MailCheck } from "lucide-react";
import { z } from "zod";

import { AuthLayout } from "@/layouts/auth-layout";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";

export const Route = createFileRoute("/forgot-password")({
  head: () => ({
    meta: [
      { title: "Forgot password — Smart Farmer AI" },
      { name: "description", content: "Request a password reset link for your Smart Farmer AI account." },
      { property: "og:title", content: "Forgot password — Smart Farmer AI" },
      { property: "og:description", content: "Reset access to your Smart Farmer AI account." },
      { name: "robots", content: "noindex" },
    ],
  }),
  component: ForgotPasswordPage,
});

const schema = z.string().trim().email("Enter a valid email").max(255);

function ForgotPasswordPage() {
  const [email, setEmail] = useState("");
  const [error, setError] = useState("");
  const [sent, setSent] = useState(false);
  const [loading, setLoading] = useState(false);

  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const parsed = schema.safeParse(email);
    if (!parsed.success) {
      setError(parsed.error.issues[0]?.message ?? "Enter a valid email");
      return;
    }
    setError("");
    setLoading(true);
    await new Promise((r) => setTimeout(r, 600));
    setLoading(false);
    setSent(true);
  };

  return (
    <AuthLayout
      title="Reset your password"
      description="Enter the email on your account and we'll send a reset link."
      footer={
        <Link to="/login" className="font-semibold text-primary hover:underline">
          Back to log in
        </Link>
      }
    >
      {sent ? (
        <div className="flex flex-col items-center gap-3 text-center" role="status">
          <span className="grid size-12 place-items-center rounded-full bg-success-soft text-success">
            <MailCheck className="size-5" aria-hidden="true" />
          </span>
          <p className="text-sm text-muted-foreground">
            If an account exists for <span className="font-medium text-foreground">{email}</span>, a reset
            link is on its way.
          </p>
          <Button asChild variant="outline" className="min-h-11">
            <Link to="/reset-password">Open reset page</Link>
          </Button>
        </div>
      ) : (
        <form onSubmit={onSubmit} noValidate className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="forgot-email">Email</Label>
            <Input
              id="forgot-email"
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              maxLength={255}
              aria-invalid={!!error}
              className="h-11"
            />
            {error ? <p className="text-xs text-danger">{error}</p> : null}
          </div>
          <Button type="submit" size="lg" className="min-h-12 w-full" disabled={loading}>
            {loading ? "Sending…" : "Send reset link"}
          </Button>
        </form>
      )}
    </AuthLayout>
  );
}
