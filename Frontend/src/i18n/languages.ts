export interface LanguageOption {
  code: string;
  label: string;
  nativeLabel: string;
}

export const LANGUAGES: LanguageOption[] = [
  { code: "en", label: "English", nativeLabel: "English" },
  { code: "hi", label: "Hindi", nativeLabel: "हिन्दी" },
  { code: "te", label: "Telugu", nativeLabel: "తెలుగు" },
  { code: "ta", label: "Tamil", nativeLabel: "தமிழ்" },
  { code: "kn", label: "Kannada", nativeLabel: "ಕನ್ನಡ" },
  { code: "ml", label: "Malayalam", nativeLabel: "മലയാളം" },
  { code: "mr", label: "Marathi", nativeLabel: "मराठी" },
  { code: "bn", label: "Bengali", nativeLabel: "বাংলা" },
  { code: "pa", label: "Punjabi", nativeLabel: "ਪੰਜਾਬੀ" },
  { code: "gu", label: "Gujarati", nativeLabel: "ગુજરાતી" },
  { code: "or", label: "Odia", nativeLabel: "ଓଡ଼ିଆ" },
];

export const DEFAULT_LANGUAGE = "en";
