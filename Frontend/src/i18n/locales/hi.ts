import type { TranslationShape } from "./en";

type DeepPartial<T> = { [K in keyof T]?: T[K] extends object ? DeepPartial<T[K]> : T[K] };

export const hi: DeepPartial<TranslationShape> = {
  brand: {
    name: "स्मार्ट फार्मर AI",
    tagline: "बेहतर निर्णय के लिए AI आधारित खेती",
  },
  nav: {
    home: "होम",
    features: "विशेषताएँ",
    aiModules: "AI मॉड्यूल",
    howItWorks: "यह कैसे काम करता है",
    about: "हमारे बारे में",
    faq: "सामान्य प्रश्न",
    contact: "संपर्क",
    getStarted: "शुरू करें",
    login: "लॉग इन",
    language: "भाषा",
  },
  home: {
    heroTitleLine1: "AI आधारित खेती",
    heroTitleLine2: "बेहतर निर्णय, अधिक उपज",
    ctaPrimary: "मुफ़्त शुरू करें",
    ctaSecondary: "विशेषताएँ देखें",
  },
  common: {
    loading: "लोड हो रहा है…",
    retry: "फिर कोशिश करें",
    search: "खोजें",
  },
};
