import type { LucideIcon } from "lucide-react";
import {
  Bot,
  Bug,
  CloudSun,
  Coins,
  Droplets,
  FlaskConical,
  Globe2,
  Landmark,
  LineChart,
  PieChart,
  ShieldCheck,
  Smartphone,
  Sprout,
  Users,
  Zap,
} from "lucide-react";

import type { Tone } from "@/components/common/cards";

export interface FeatureItem {
  id: string;
  title: string;
  description: string;
  benefit: string;
  icon: LucideIcon;
  tone: Tone;
}

export const CORE_FEATURES: FeatureItem[] = [
  {
    id: "disease",
    title: "AI Disease Detection",
    description: "Photograph an affected leaf and get the likely disease with treatment steps.",
    benefit: "Act before an outbreak spreads across the field.",
    icon: Bug,
    tone: "danger",
  },
  {
    id: "crop",
    title: "Crop Recommendation",
    description: "Soil nutrients, pH, rainfall and weather turned into a shortlist of suitable crops.",
    benefit: "Plant what your soil and season actually support.",
    icon: Sprout,
    tone: "primary",
  },
  {
    id: "fertilizer",
    title: "Fertilizer Advice",
    description: "Balanced NPK dosage guidance for your crop, soil and plot size.",
    benefit: "Spend less on inputs and avoid over-application.",
    icon: FlaskConical,
    tone: "warning",
  },
  {
    id: "weather",
    title: "Weather Forecast",
    description: "Local conditions, rainfall outlook and severe-weather alerts for your farm.",
    benefit: "Plan sowing, spraying and harvest around the sky.",
    icon: CloudSun,
    tone: "weather",
  },
  {
    id: "market",
    title: "Market Prices",
    description: "Daily mandi rates with day-on-day change and price trends.",
    benefit: "Sell at the right market on the right day.",
    icon: LineChart,
    tone: "market",
  },
  {
    id: "schemes",
    title: "Government Schemes",
    description: "Central and state schemes filtered by state, category and eligibility.",
    benefit: "Never miss support you already qualify for.",
    icon: Landmark,
    tone: "success",
  },
];

export const EXTRA_FEATURES: FeatureItem[] = [
  {
    id: "irrigation",
    title: "Irrigation Planning",
    description: "Water requirement and schedule based on soil moisture, crop stage and rainfall.",
    benefit: "Save water without stressing the crop.",
    icon: Droplets,
    tone: "weather",
  },
  {
    id: "profit",
    title: "Profit Prediction",
    description: "Estimate cost, yield and margin before you commit to a season.",
    benefit: "Choose the crop plan with the better economics.",
    icon: Coins,
    tone: "market",
  },
  {
    id: "analytics",
    title: "Farm Analytics",
    description: "Track farms, fields, crops and past recommendations in one place.",
    benefit: "See what worked last season.",
    icon: PieChart,
    tone: "primary",
  },
  {
    id: "assistant",
    title: "Voice & AI Assistant",
    description: "Ask farming questions in plain language and get short, practical answers.",
    benefit: "Help at any hour, without typing long messages.",
    icon: Bot,
    tone: "primary",
  },
  {
    id: "multilingual",
    title: "Multi-language",
    description: "Interface architecture ready for 11 Indian languages.",
    benefit: "Use the app in the language you think in.",
    icon: Globe2,
    tone: "success",
  },
  {
    id: "pwa",
    title: "Installable App",
    description: "Install on Android or desktop straight from the browser.",
    benefit: "Opens like a normal app, no store needed.",
    icon: Smartphone,
    tone: "weather",
  },
];

export interface AiModule {
  id: string;
  name: string;
  icon: LucideIcon;
  tone: Tone;
  summary: string;
  input: string[];
  processing: string[];
  output: string[];
  benefits: string[];
}

export const AI_MODULES: AiModule[] = [
  {
    id: "disease-detection",
    name: "Disease Detection",
    icon: Bug,
    tone: "danger",
    summary: "Image-based identification of common crop diseases with treatment guidance.",
    input: ["Leaf or plant photo", "Crop name", "Optional field context"],
    processing: ["Image quality check", "Vision model inference", "Confidence scoring"],
    output: ["Disease name", "Confidence score", "Symptoms", "Treatment and prevention"],
    benefits: ["Early detection", "Lower crop loss", "Targeted pesticide use"],
  },
  {
    id: "crop-recommendation",
    name: "Crop Recommendation",
    icon: Sprout,
    tone: "primary",
    summary: "Suggests crops suited to your soil chemistry, climate and location.",
    input: ["N, P, K values", "Soil pH", "Temperature and humidity", "Rainfall", "Location"],
    processing: ["Soil-climate matching", "Suitability scoring", "Season filter"],
    output: ["Ranked crop list", "Suitability score", "Reasoning", "Expected benefits"],
    benefits: ["Better yield fit", "Reduced input waste", "Season-aware planning"],
  },
  {
    id: "fertilizer",
    name: "Fertilizer Recommendation",
    icon: FlaskConical,
    tone: "warning",
    summary: "Balances soil nutrient gaps against crop demand.",
    input: ["Crop", "Soil type", "Current NPK", "Plot area"],
    processing: ["Nutrient gap analysis", "Dosage calculation", "Safety checks"],
    output: ["Fertilizer type", "Dosage per acre", "Application schedule", "Safety notes"],
    benefits: ["Lower input cost", "Healthier soil", "Avoids over-fertilising"],
  },
  {
    id: "irrigation",
    name: "Irrigation Recommendation",
    icon: Droplets,
    tone: "weather",
    summary: "Tells you how much water to apply and when.",
    input: ["Soil moisture", "Temperature and humidity", "Rainfall", "Crop and field"],
    processing: ["Evapotranspiration estimate", "Rainfall offset", "Schedule generation"],
    output: ["Water requirement", "Next irrigation window", "Water-saving tips"],
    benefits: ["Water savings", "Less root stress", "Predictable scheduling"],
  },
  {
    id: "profit",
    name: "Profit Prediction",
    icon: Coins,
    tone: "market",
    summary: "Projects season economics from cost, yield and price signals.",
    input: ["Crop and area", "Input costs", "Expected yield", "Market prices"],
    processing: ["Cost modelling", "Yield estimation", "Price trend blend"],
    output: ["Projected revenue", "Estimated margin", "Break-even price"],
    benefits: ["Financial clarity", "Better crop mix", "Lower risk"],
  },
  {
    id: "market-intelligence",
    name: "Market Intelligence",
    icon: LineChart,
    tone: "market",
    summary: "Daily mandi prices with trends and change indicators.",
    input: ["Crop", "Market", "State"],
    processing: ["Price normalisation", "Trend computation", "Change detection"],
    output: ["Today vs yesterday", "Percent change", "Trend chart"],
    benefits: ["Sell at better rates", "Spot demand shifts", "Compare markets"],
  },
  {
    id: "assistant",
    name: "AI Assistant",
    icon: Bot,
    tone: "primary",
    summary: "Conversational help for everyday farming questions.",
    input: ["Typed or spoken question", "Farm context"],
    processing: ["Intent understanding", "Farm data lookup", "Answer generation"],
    output: ["Short practical answer", "Follow-up suggestions", "Links to modules"],
    benefits: ["Always available", "Plain language", "Guided next steps"],
  },
];

export const WHY_US: { title: string; description: string; icon: LucideIcon; tone: Tone }[] = [
  {
    title: "Easy to Use",
    description: "Big buttons, simple words and a layout that works like the apps you already use.",
    icon: Smartphone,
    tone: "primary",
  },
  {
    title: "Real-Time Insights",
    description: "Weather, market and field data refreshed so decisions are based on today.",
    icon: Zap,
    tone: "weather",
  },
  {
    title: "Multi-Language",
    description: "Built from the start for Indian languages, not translated as an afterthought.",
    icon: Globe2,
    tone: "success",
  },
  {
    title: "Secure & Reliable",
    description: "Your farm data stays yours, protected by role-based access on the server.",
    icon: ShieldCheck,
    tone: "market",
  },
  {
    title: "Expert Support",
    description: "Agriculture experts, dealers, NGOs and officials work in the same platform.",
    icon: Users,
    tone: "warning",
  },
];

export interface MetricItem {
  id: string;
  value: string;
  label: string;
}

/** Configurable display metrics. Replace with verified values before publishing. */
export const SHOWCASE_METRICS: MetricItem[] = [
  { id: "farmers", value: "10K+", label: "Happy Farmers" },
  { id: "acres", value: "50K+", label: "Acres Covered" },
  { id: "crops", value: "25+", label: "Crops Supported" },
  { id: "accuracy", value: "95%", label: "Accuracy Rate" },
  { id: "support", value: "24/7", label: "AI Support" },
];

export const HOW_IT_WORKS: { step: number; title: string; description: string }[] = [
  { step: 1, title: "Register", description: "Create a free account with your mobile number or email." },
  { step: 2, title: "Add Farm", description: "Enter farm name, location, area, soil and irrigation type." },
  { step: 3, title: "Add Crop", description: "Tell us what you grow and the season you are planning for." },
  { step: 4, title: "Scan / Enter Information", description: "Upload a leaf photo or fill in soil and weather values." },
  { step: 5, title: "AI Analysis", description: "The relevant AI module processes your inputs." },
  { step: 6, title: "Recommendation", description: "You get a clear result with reasoning and next steps." },
  { step: 7, title: "Take Action", description: "Apply the advice in the field and record what you did." },
  { step: 8, title: "Improve Yield", description: "Compare seasons in history and refine your plan." },
];

export interface Testimonial {
  id: string;
  quote: string;
  name: string;
  role: string;
  location: string;
}

/** Placeholder testimonials — replace with consented, verified quotes. */
export const TESTIMONIALS: Testimonial[] = [
  {
    id: "t1",
    quote: "Placeholder quote about catching leaf blight early and saving the season's crop.",
    name: "Farmer name",
    role: "Paddy farmer",
    location: "State, India",
  },
  {
    id: "t2",
    quote: "Placeholder quote about choosing the right market day using price trends.",
    name: "Farmer name",
    role: "Cotton farmer",
    location: "State, India",
  },
  {
    id: "t3",
    quote: "Placeholder quote about reducing fertilizer spend with dosage guidance.",
    name: "Farmer name",
    role: "Groundnut farmer",
    location: "State, India",
  },
];

export const FAQS: { question: string; answer: string }[] = [
  {
    question: "Is Smart Farmer AI free to use?",
    answer:
      "Creating an account and using the core advisory modules is free. Some advanced services may require a subscription in the future.",
  },
  {
    question: "Do I need a fast internet connection?",
    answer:
      "The app is light and works on ordinary mobile data. Disease detection needs a connection to upload the photo for analysis.",
  },
  {
    question: "Which languages are supported?",
    answer:
      "English is available today. Hindi, Telugu, Tamil, Kannada, Malayalam, Marathi, Bengali, Punjabi, Gujarati and Odia are part of the translation architecture and roll out as translations are completed.",
  },
  {
    question: "How accurate is disease detection?",
    answer:
      "Every result includes a confidence score. Treat it as guidance and confirm serious cases with a local agriculture officer or expert.",
  },
  {
    question: "Can I install it on my phone?",
    answer:
      "Yes. Open the site in Chrome on Android or a desktop browser and choose Install, or use the install button on this site.",
  },
  {
    question: "Who can see my farm data?",
    answer:
      "Only you and the roles you allow. Access is enforced by the server, and the app never stores backend secrets in your browser.",
  },
  {
    question: "Does it work for small plots?",
    answer: "Yes. You can record area in acres, hectares or square metres, from a kitchen plot upwards.",
  },
  {
    question: "How do I get help?",
    answer: "Use the contact page or the in-app AI assistant, and our support team will follow up.",
  },
];

export const CORE_VALUES: { title: string; description: string }[] = [
  { title: "Farmer first", description: "Every screen is judged by whether a first-time smartphone user can finish the task." },
  { title: "Honest advice", description: "We show confidence, reasoning and limits instead of pretending certainty." },
  { title: "Local relevance", description: "Crops, markets, schemes and languages are built around Indian agriculture." },
  { title: "Privacy by default", description: "Farm data is private and never sold." },
  { title: "Accessible always", description: "Readable text, large tap targets and screen-reader support." },
  { title: "Practical AI", description: "Models exist to answer a question in the field, not to look impressive." },
];
