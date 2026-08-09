import { useState } from "react";
import { createFileRoute, Link, useNavigate } from "@tanstack/react-router";
import { toast } from "sonner";
import { z } from "zod";

import { AuthLayout } from "@/layouts/auth-layout";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";

export const Route = createFileRoute("/reset-password")({
  head: () => ({
    meta: [
      { title: "Set a new password — Smart Farmer AI" },
      { name: "description", content: "Choose a new password for your Smart Farmer AI account." },
      { property: "og:title", content: "Set a new password — Smart Farmer AI" },
      { property: "og:description", content: "Complete your Smart Farmer AI password reset." },
      { name: "robots", content: "noindex" },
    ],
  }),
  component: ResetPasswordPage,
});

const schema = z
  .object({
    password: z.string().min(8, "Password must be at least 8 characters").max(128),
    confirm: z.string(),
  })
  .refine((v) => v.password === v.confirm, { path: ["confirm"], message: "Passwords do not match" });

function ResetPasswordPage() {
  const navigate = useNavigate();
  const [password, setPassword] = useState("");
  const [confirm, setConfirm] = useState("");
  const [errors, setErrors] = useState<{ password?: string; confirm?: string }>({});
  const [loading, setLoading] = useState(false);

  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const parsed = schema.safeParse({ password, confirm });
    if (!parsed.success) {
      const next: { password?: string; confirm?: string } = {};
      for (const issue of parsed.error.issues) {
        const key = issue.path[0] as "password" | "confirm";
        if (!next[key]) next[key] = issue.message;
      }
      setErrors(next);
      return;
    }
    setErrors({});
    setLoading(true);
    await new Promise((r) => setTimeout(r, 600));
    setLoading(false);
    toast.success("Password updated", { description: "You can log in with your new password." });
    void navigate({ to: "/login" });
  };

  return (
    <AuthLayout
      title="Set a new password"
      description="Choose a password of at least 8 characters."
      footer={
        <Link to="/login" className="font-semibold text-primary hover:underline">
          Back to log in
        </Link>
      }
    >
      <form onSubmit={onSubmit} noValidate className="space-y-4">
        <div className="space-y-2">
          <Label htmlFor="reset-password">New password</Label>
          <Input
            id="reset-password"
            type="password"
            autoComplete="new-password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            aria-invalid={!!errors.password}
            className="h-11"
          />
          {errors.password ? <p className="text-xs text-danger">{errors.password}</p> : null}
        </div>
        <div className="space-y-2">
          <Label htmlFor="reset-confirm">Confirm new password</Label>
          <Input
            id="reset-confirm"
            type="password"
            autoComplete="new-password"
            value={confirm}
            onChange={(e) => setConfirm(e.target.value)}
            aria-invalid={!!errors.confirm}
            className="h-11"
          />
          {errors.confirm ? <p className="text-xs text-danger">{errors.confirm}</p> : null}
        </div>
        <Button type="submit" size="lg" className="min-h-12 w-full" disabled={loading}>
          {loading ? "Updating…" : "Update password"}
        </Button>
      </form>
    </AuthLayout>
  );
}
