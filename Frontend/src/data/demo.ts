import type {
  AppNotification,
  Crop,
  DiseaseScan,
  Farm,
  Field,
  MarketPrice,
  Recommendation,
  Scheme,
  User,
  WeatherSnapshot,
} from "@/types";

export const demoUser: User = {
  id: "u-1",
  name: "Ramesh Patil",
  email: "ramesh@example.com",
  phone: "+91 98765 43210",
  role: "FARMER",
  language: "en",
  createdAt: "2025-06-12",
};

export const demoFarms: Farm[] = [
  {
    id: "f-1",
    name: "Ganga Farm",
    location: "Nashik, Maharashtra",
    area: 6.5,
    areaUnit: "ACRE",
    soilType: "Black soil",
    irrigationType: "Drip",
    ownership: "OWNED",
    createdAt: "2025-06-14",
  },
  {
    id: "f-2",
    name: "Riverside Plot",
    location: "Sangamner, Maharashtra",
    area: 3.2,
    areaUnit: "ACRE",
    soilType: "Loamy",
    irrigationType: "Canal",
    ownership: "LEASED",
    createdAt: "2025-08-02",
  },
  {
    id: "f-3",
    name: "Hill Block",
    location: "Igatpuri, Maharashtra",
    area: 2.0,
    areaUnit: "ACRE",
    soilType: "Red soil",
    irrigationType: "Rainfed",
    ownership: "SHARED",
    createdAt: "2025-09-21",
  },
];

export const demoFields: Field[] = [
  { id: "fl-1", farmId: "f-1", name: "North Field", area: 2.5, areaUnit: "ACRE", currentCrop: "Tomato", soilType: "Black soil" },
  { id: "fl-2", farmId: "f-1", name: "South Field", area: 4.0, areaUnit: "ACRE", currentCrop: "Onion", soilType: "Black soil" },
  { id: "fl-3", farmId: "f-2", name: "Canal Strip", area: 3.2, areaUnit: "ACRE", currentCrop: "Sugarcane", soilType: "Loamy" },
  { id: "fl-4", farmId: "f-3", name: "Upper Terrace", area: 2.0, areaUnit: "ACRE", currentCrop: "Soybean", soilType: "Red soil" },
];

export const demoCrops: Crop[] = [
  { id: "c-1", name: "Tomato", season: "Rabi", durationDays: 110, description: "Warm-season vegetable, sensitive to blight." },
  { id: "c-2", name: "Onion", season: "Rabi", durationDays: 140, description: "Bulb crop, needs well-drained soil." },
  { id: "c-3", name: "Sugarcane", season: "Annual", durationDays: 330, description: "High water demand, long duration." },
  { id: "c-4", name: "Soybean", season: "Kharif", durationDays: 100, description: "Nitrogen-fixing oilseed crop." },
  { id: "c-5", name: "Wheat", season: "Rabi", durationDays: 125, description: "Cool-season cereal grain." },
  { id: "c-6", name: "Cotton", season: "Kharif", durationDays: 180, description: "Fibre crop, pest monitoring critical." },
];

export const demoScans: DiseaseScan[] = [
  {
    id: "s-1",
    cropName: "Tomato",
    diseaseName: "Early Blight",
    confidence: 0.94,
    symptoms: ["Dark concentric spots on lower leaves", "Yellow halo around lesions", "Leaf drop"],
    actions: ["Remove affected leaves", "Spray Mancozeb 75% WP at 2g/litre", "Repeat after 10 days if spread continues"],
    prevention: ["Rotate with non-solanaceous crops", "Avoid overhead irrigation", "Maintain plant spacing"],
    createdAt: "2026-02-11T08:20:00Z",
  },
  {
    id: "s-2",
    cropName: "Onion",
    diseaseName: "Purple Blotch",
    confidence: 0.87,
    symptoms: ["Purple lesions on leaves", "Tip dieback"],
    actions: ["Spray recommended fungicide", "Improve field drainage"],
    prevention: ["Use disease-free sets", "Avoid dense planting"],
    createdAt: "2026-01-28T10:05:00Z",
  },
  {
    id: "s-3",
    cropName: "Soybean",
    diseaseName: "Healthy",
    confidence: 0.98,
    symptoms: ["No visible disease symptoms"],
    actions: ["Continue current practice"],
    prevention: ["Weekly field scouting"],
    createdAt: "2026-01-09T06:45:00Z",
  },
];

export const demoRecommendations: Recommendation[] = [
  {
    id: "r-1",
    type: "CROP",
    title: "Tomato is the best fit for North Field",
    summary: "Soil NPK and rainfall outlook match tomato requirements with a suitability score of 92%.",
    createdAt: "2026-02-10T09:00:00Z",
    details: { Suitability: "92%", Season: "Rabi", "Expected yield": "18 t/acre" },
  },
  {
    id: "r-2",
    type: "FERTILIZER",
    title: "Apply 45 kg urea per acre in two splits",
    summary: "Nitrogen gap detected against tomato demand at the flowering stage.",
    createdAt: "2026-02-08T09:00:00Z",
    details: { Nitrogen: "45 kg/acre", Phosphorus: "20 kg/acre", Potassium: "25 kg/acre" },
  },
  {
    id: "r-3",
    type: "IRRIGATION",
    title: "Next irrigation window: 3 days",
    summary: "Soil moisture at 38% with only 4 mm rain forecast this week.",
    createdAt: "2026-02-07T09:00:00Z",
    details: { "Water needed": "22 mm", Method: "Drip", Duration: "70 min" },
  },
  {
    id: "r-4",
    type: "PROFIT",
    title: "Projected margin ₹58,400 for Rabi tomato",
    summary: "Based on current input costs and mandi price trends for Nashik.",
    createdAt: "2026-02-02T09:00:00Z",
    details: { Revenue: "₹1,42,000", Cost: "₹83,600", "Break-even": "₹9.10/kg" },
  },
];

export const demoWeather: WeatherSnapshot = {
  location: "Nashik, Maharashtra",
  temperatureC: 29,
  humidity: 58,
  windKph: 12,
  rainMm: 0,
  condition: "Partly cloudy",
  advice: "Good conditions for spraying before noon. Avoid irrigation on Thursday — rain expected.",
  forecast: [
    { date: "Mon", minC: 18, maxC: 30, condition: "Sunny", rainMm: 0 },
    { date: "Tue", minC: 19, maxC: 31, condition: "Partly cloudy", rainMm: 0 },
    { date: "Wed", minC: 20, maxC: 29, condition: "Cloudy", rainMm: 2 },
    { date: "Thu", minC: 21, maxC: 27, condition: "Rain", rainMm: 14 },
    { date: "Fri", minC: 20, maxC: 28, condition: "Showers", rainMm: 6 },
    { date: "Sat", minC: 19, maxC: 30, condition: "Sunny", rainMm: 0 },
    { date: "Sun", minC: 18, maxC: 31, condition: "Sunny", rainMm: 0 },
  ],
  alerts: [
    {
      id: "a-1",
      title: "Heavy rain likely on Thursday",
      severity: "MEDIUM",
      body: "14 mm rainfall expected. Postpone fertilizer application and spraying.",
    },
  ],
};

export const demoMarketPrices: MarketPrice[] = [
  {
    id: "m-1",
    crop: "Tomato",
    market: "Nashik",
    state: "Maharashtra",
    todayPrice: 1450,
    yesterdayPrice: 1320,
    unit: "₹/quintal",
    changePercent: 9.8,
    history: [
      { date: "Mon", price: 1210 },
      { date: "Tue", price: 1280 },
      { date: "Wed", price: 1240 },
      { date: "Thu", price: 1300 },
      { date: "Fri", price: 1320 },
      { date: "Sat", price: 1450 },
    ],
  },
  {
    id: "m-2",
    crop: "Onion",
    market: "Lasalgaon",
    state: "Maharashtra",
    todayPrice: 1830,
    yesterdayPrice: 1905,
    unit: "₹/quintal",
    changePercent: -3.9,
    history: [
      { date: "Mon", price: 2010 },
      { date: "Tue", price: 1980 },
      { date: "Wed", price: 1940 },
      { date: "Thu", price: 1920 },
      { date: "Fri", price: 1905 },
      { date: "Sat", price: 1830 },
    ],
  },
  {
    id: "m-3",
    crop: "Soybean",
    market: "Latur",
    state: "Maharashtra",
    todayPrice: 4720,
    yesterdayPrice: 4680,
    unit: "₹/quintal",
    changePercent: 0.9,
    history: [
      { date: "Mon", price: 4600 },
      { date: "Tue", price: 4640 },
      { date: "Wed", price: 4650 },
      { date: "Thu", price: 4670 },
      { date: "Fri", price: 4680 },
      { date: "Sat", price: 4720 },
    ],
  },
  {
    id: "m-4",
    crop: "Wheat",
    market: "Indore",
    state: "Madhya Pradesh",
    todayPrice: 2510,
    yesterdayPrice: 2510,
    unit: "₹/quintal",
    changePercent: 0,
    history: [
      { date: "Mon", price: 2480 },
      { date: "Tue", price: 2495 },
      { date: "Wed", price: 2500 },
      { date: "Thu", price: 2505 },
      { date: "Fri", price: 2510 },
      { date: "Sat", price: 2510 },
    ],
  },
  {
    id: "m-5",
    crop: "Cotton",
    market: "Akola",
    state: "Maharashtra",
    todayPrice: 7240,
    yesterdayPrice: 7390,
    unit: "₹/quintal",
    changePercent: -2,
    history: [
      { date: "Mon", price: 7500 },
      { date: "Tue", price: 7460 },
      { date: "Wed", price: 7420 },
      { date: "Thu", price: 7400 },
      { date: "Fri", price: 7390 },
      { date: "Sat", price: 7240 },
    ],
  },
];

export const demoSchemes: Scheme[] = [
  {
    id: "sc-1",
    name: "PM-KISAN Samman Nidhi",
    category: "Income support",
    state: "All India",
    summary: "₹6,000 per year paid in three equal instalments to eligible landholding farmer families.",
    eligibility: ["Landholding farmer family", "Valid Aadhaar", "Bank account linked to Aadhaar"],
    applicationUrl: "https://pmkisan.gov.in",
  },
  {
    id: "sc-2",
    name: "Pradhan Mantri Fasal Bima Yojana",
    category: "Insurance",
    state: "All India",
    summary: "Crop insurance against yield loss from natural calamities, pests and disease.",
    eligibility: ["Notified crop in notified area", "Loanee or non-loanee farmer"],
    applicationUrl: "https://pmfby.gov.in",
  },
  {
    id: "sc-3",
    name: "Soil Health Card Scheme",
    category: "Soil health",
    state: "All India",
    summary: "Free soil testing with nutrient status and fertilizer recommendations every cycle.",
    eligibility: ["All farmers with cultivable land"],
  },
  {
    id: "sc-4",
    name: "Micro Irrigation Fund",
    category: "Irrigation",
    state: "Maharashtra",
    summary: "Subsidy support for drip and sprinkler installation on eligible holdings.",
    eligibility: ["Farmer with irrigation source", "Land record in applicant's name"],
  },
];

export const demoNotifications: AppNotification[] = [
  {
    id: "n-1",
    title: "Rain expected Thursday",
    body: "14 mm rainfall forecast for Nashik. Hold off on spraying.",
    read: false,
    type: "WEATHER",
    createdAt: "2026-02-11T05:00:00Z",
  },
  {
    id: "n-2",
    title: "Tomato prices up 9.8%",
    body: "Nashik mandi is at ₹1,450/quintal today.",
    read: false,
    type: "MARKET",
    createdAt: "2026-02-11T04:10:00Z",
  },
  {
    id: "n-3",
    title: "New scheme available",
    body: "Micro Irrigation Fund applications open for Maharashtra.",
    read: true,
    type: "SCHEME",
    createdAt: "2026-02-09T11:30:00Z",
  },
  {
    id: "n-4",
    title: "Disease scan complete",
    body: "Early Blight detected on Tomato with 94% confidence.",
    read: true,
    type: "AI",
    createdAt: "2026-02-11T08:22:00Z",
  },
];

export const demoAdminStats = [
  { id: "users", label: "Total users", value: "12,480", hint: "+320 this month" },
  { id: "farms", label: "Farms registered", value: "9,132", hint: "+186 this month" },
  { id: "scans", label: "Disease scans", value: "48,905", hint: "1,204 last 7 days" },
  { id: "recs", label: "Recommendations", value: "76,331", hint: "2,940 last 7 days" },
];

export const demoAdminUsers: User[] = [
  { id: "au-1", name: "Ramesh Patil", email: "ramesh@example.com", role: "FARMER", createdAt: "2025-06-12" },
  { id: "au-2", name: "Dr. Anita Rao", email: "anita@example.com", role: "EXPERT", createdAt: "2025-04-02" },
  { id: "au-3", name: "Krishi Dealers", email: "sales@krishidealers.in", role: "DEALER", createdAt: "2025-07-19" },
  { id: "au-4", name: "GreenEarth NGO", email: "contact@greenearth.org", role: "NGO", createdAt: "2025-03-08" },
  { id: "au-5", name: "District Officer", email: "officer@gov.in", role: "GOVERNMENT", createdAt: "2025-01-24" },
  { id: "au-6", name: "Platform Admin", email: "admin@smartfarmer.ai", role: "ADMIN", createdAt: "2024-11-11" },
  { id: "au-7", name: "Sunita Deshmukh", email: "sunita@example.com", role: "FARMER", createdAt: "2025-09-30" },
  { id: "au-8", name: "Iqbal Khan", email: "iqbal@example.com", role: "FARMER", createdAt: "2025-10-14" },
];

export const demoAuditLogs = [
  { id: "l-1", actor: "admin@smartfarmer.ai", action: "Updated market price source", target: "markets", at: "2026-02-11 09:42" },
  { id: "l-2", actor: "anita@example.com", action: "Approved recommendation template", target: "recommendations", at: "2026-02-10 17:05" },
  { id: "l-3", actor: "admin@smartfarmer.ai", action: "Deactivated user", target: "users/au-19", at: "2026-02-10 12:20" },
  { id: "l-4", actor: "officer@gov.in", action: "Published scheme", target: "schemes/sc-4", at: "2026-02-09 15:12" },
  { id: "l-5", actor: "admin@smartfarmer.ai", action: "Rotated API key", target: "settings", at: "2026-02-08 08:00" },
];
