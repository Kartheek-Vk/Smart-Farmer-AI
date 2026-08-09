import { useState } from "react";
import { createFileRoute } from "@tanstack/react-router";
import { Send } from "lucide-react";

import { PageHeader } from "@/components/common/page";
import { DataCard } from "@/components/app/list-primitives";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import type { ChatMessage } from "@/types";

export const Route = createFileRoute("/app/assistant")({
  head: () => ({
    meta: [
      { title: "AI assistant — Smart Farmer AI" },
      { name: "description", content: "Ask farming questions in plain language and get practical answers." },
      { name: "robots", content: "noindex" },
    ],
  }),
  component: AssistantPage,
});

const SUGGESTIONS = [
  "When should I irrigate my tomato field?",
  "How do I treat early blight?",
  "Is this a good week to sell onion?",
];

function AssistantPage() {
  const [messages, setMessages] = useState<ChatMessage[]>([
    {
      id: "m-0",
      role: "assistant",
      content: "Namaste! Ask me about your crops, soil, weather or market prices.",
      createdAt: new Date().toISOString(),
    },
  ]);
  const [input, setInput] = useState("");

  const send = (text: string) => {
    const value = text.trim().slice(0, 500);
    if (!value) return;
    const now = new Date().toISOString();
    setMessages((prev) => [
      ...prev,
      { id: `u-${prev.length}`, role: "user", content: value, createdAt: now },
      {
        id: `a-${prev.length}`,
        role: "assistant",
        content:
          "Based on your Nashik farm: soil moisture is 38% and 14 mm rain is forecast on Thursday, so hold irrigation until Friday and keep spraying to the morning window.",
        createdAt: now,
      },
    ]);
    setInput("");
  };

  return (
    <div className="space-y-5">
      <PageHeader title="AI assistant" description="Short, practical answers for everyday decisions." />
      <DataCard>
        <ul className="space-y-3">
          {messages.map((message) => (
            <li
              key={message.id}
              className={message.role === "user" ? "flex justify-end" : "flex justify-start"}
            >
              <p
                className={
                  "max-w-[85%] rounded-2xl px-4 py-2 text-sm " +
                  (message.role === "user"
                    ? "bg-primary text-primary-foreground"
                    : "bg-muted text-foreground")
                }
              >
                {message.content}
              </p>
            </li>
          ))}
        </ul>
        <div className="mt-4 flex flex-wrap gap-2">
          {SUGGESTIONS.map((s) => (
            <Button key={s} variant="outline" className="min-h-10 text-xs" onClick={() => send(s)}>
              {s}
            </Button>
          ))}
        </div>
        <form
          className="mt-4 flex gap-2"
          onSubmit={(e) => {
            e.preventDefault();
            send(input);
          }}
        >
          <label htmlFor="assistant-input" className="sr-only">
            Your question
          </label>
          <Input
            id="assistant-input"
            value={input}
            onChange={(e) => setInput(e.target.value)}
            maxLength={500}
            placeholder="Ask a question…"
            className="h-11"
          />
          <Button type="submit" className="min-h-11 min-w-11" aria-label="Send question">
            <Send className="size-4" aria-hidden="true" />
          </Button>
        </form>
      </DataCard>
    </div>
  );
}
