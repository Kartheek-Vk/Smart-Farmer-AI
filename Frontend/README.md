# Smart Farm Assist

You are the Principal Frontend Architect, Senior React Engineer, Product Designer, UI/UX Engineer, Accessibility Engineer, PWA Engineer, and QA Engineer for Smart Farmer AI.

BUILD THE ENTIRE FRONTEND IN THIS SINGLE TASK.

Do not give me a plan.
Do not stop after the foundation.
Do not stop after the Home page.
Do not stop after public pages.
Do not ask for confirmation.
Do not create the backend.
Do not create FastAPI.
Do not leave major pages as placeholders.

Actually implement the complete, working frontend.

==================================================
PROJECT
==================================================

Project: Smart Farmer AI

An AI-powered agriculture platform for:

• Farmers
• Agriculture Experts
• Dealers
• NGOs
• Government Organizations
• Administrators

The primary user is an Indian farmer who may have limited technical knowledge.

The application should feel familiar and simple like:

• Google Pay
• PhonePe
• WhatsApp
• Facebook
• Google Maps

It must NOT feel like:

• an AI SaaS dashboard
• a futuristic AI interface
• a developer dashboard
• a corporate enterprise dashboard
• a cryptocurrency application

==================================================
VISUAL REFERENCE
==================================================

Use the attached Smart Farmer AI UI reference image as the PRIMARY visual direction.

Match its overall design language:

• clean white/light background
• fresh agricultural green accents
• premium but friendly
• clean cards
• subtle borders
• soft shadows
• rounded corners
• strong spacing
• readable typography
• simple icons
• clear navigation
• responsive desktop dashboard
• polished mobile application
• professional admin dashboard

IMPORTANT:

DO NOT put a farmer/person photograph on the Home/Landing page hero.

The Home hero should use an agriculture/product visual such as:

• plant/crop
• crop field illustration
• AI agriculture visualization
• product UI visualization
• weather/data icons
• crop/disease/irrigation visual

No human/farmer photograph in the first hero section.

==================================================
TECHNOLOGY
==================================================

Use:

React
TypeScript
Vite
Tailwind CSS
Shadcn UI
React Router
React Hook Form
Zod
Axios
Framer Motion
Recharts
Lucide React
PWA
i18n

Use strict TypeScript.

Do not use `any` unless absolutely unavoidable.

==================================================
EXISTING PROJECT
==================================================

First inspect the existing project.

If frontend code already exists:

• preserve good code
• reuse existing components
• reuse existing configuration
• repair broken code
• extend it
• do not create duplicate projects
• do not unnecessarily replace working code

If frontend is missing:

create the complete frontend in the existing project.

Do not create a second application.

==================================================
DESIGN SYSTEM
==================================================

Default theme:

LIGHT

Also support:

Dark
System

Use a fresh agriculture-inspired color palette.

Primary color:

natural green

Supporting colors:

weather blue
market purple/indigo
warning amber
danger red
success green

Use:

• white/light surfaces
• subtle shadows
• clean borders
• moderate radius
• professional spacing
• readable typography
• high contrast
• large touch targets

Avoid:

• excessive dark green
• neon
• excessive gradients
• excessive glassmorphism
• glowing effects
• overly rounded childish UI
• giant AI graphics
• dense dashboards

==================================================
RESPONSIVE DESIGN
==================================================

Perfectly support:

320px
375px
425px
768px
1024px
1280px
1440px
ultra-wide

Mobile/Android is the highest priority.

No horizontal scrolling.

No overlapping elements.

No clipped text.

Buttons must be touch friendly.

Desktop must use available space properly.

Tablet must have its own appropriate layout.

==================================================
ARCHITECTURE
==================================================

Use a clean feature-based architecture.

Recommended:

src/
  app/
  assets/
  components/
    ui/
    common/
    navigation/
  config/
  constants/
  features/
    auth/
    farmer/
    farm/
    crop/
    disease/
    recommendation/
    weather/
    market/
    schemes/
    notifications/
    assistant/
    history/
    admin/
  hooks/
  i18n/
  layouts/
  lib/
  pages/
  providers/
  routes/
  services/
  types/

Use reusable components.

Use @/ aliases.

Avoid giant components.

==================================================
ROUTES
==================================================

Every route must actually work.

PUBLIC:

/
 /about
 /features
 /ai-modules
 /how-it-works
 /faq
 /contact

AUTH:

/login
/register
/forgot-password
/reset-password

APPLICATION:

/app
/app/farm
/app/farm/:farmId
/app/fields
/app/crops
/app/disease
/app/recommendations
/app/weather
/app/market
/app/schemes
/app/notifications
/app/assistant
/app/history
/app/profile
/app/settings

ADMIN:

/admin
/admin/users
/admin/farms
/admin/crops
/admin/disease
/admin/recommendations
/admin/markets
/admin/schemes
/admin/notifications
/admin/reports
/admin/audit-logs
/admin/settings

Also:

404

Every navigation link must work.

No accidental:

Not Found: /index.html

No broken React Router paths.

Use lazy loading/code splitting.

==================================================
PUBLIC WEBSITE
==================================================

HOME PAGE

Create a premium landing page matching the attached visual reference.

NAVBAR:

Smart Farmer AI logo

Home
Features
AI Modules
How It Works
About Us
Contact

Language selector

Get Started

Mobile menu.

HERO:

Headline:

AI-Powered Farming
Better Decisions, Higher Yields

Supporting text explaining Smart Farmer AI.

Buttons:

Get Started Free
Explore Features

Hero visual:

NO FARMER/PERSON PHOTO.

Use:

plant/crop visual
AI insights
weather
disease detection
crop recommendation
irrigation
market data

Make it elegant and product-focused.

FEATURE STRIP:

AI Disease Detection
Crop Recommendation
Fertilizer Advice
Weather Forecast
Market Prices
Government Schemes

Each with icon and short description.

AI MODULES:

Disease Detection
Crop Recommendation
Fertilizer Recommendation
Irrigation Recommendation
Profit Prediction
Market Intelligence
AI Assistant

Create beautiful cards.

WHY SMART FARMER AI:

Easy to Use
Real-Time Insights
Multi-Language
Secure & Reliable
Expert Support

STATISTICS SECTION:

Use editable/replaceable values such as:

10K+
Happy Farmers

50K+
Acres Covered

25+
Crops Supported

95%
Accuracy Rate

24/7
AI Support

Clearly structure these as configurable metrics, not verified claims.

HOW IT WORKS:

Register
Add Farm
Add Crop
Scan/Enter Information
AI Analysis
Recommendation
Take Action
Improve Yield

SUPPORTED LANGUAGES

Show language support.

TESTIMONIALS

Create polished testimonial cards with placeholder content clearly structured for later replacement.

FAQ PREVIEW

PWA INSTALL CTA

FINAL CTA

FOOTER

==================================================
ABOUT PAGE
==================================================

Include:

Mission
Vision
Our Story
Why We Built Smart Farmer AI
Core Values
Technology Overview

==================================================
FEATURES PAGE
==================================================

Show:

Disease Detection
Crop Recommendation
Fertilizer Recommendation
Irrigation
Weather
Market Prices
Profit Prediction
Government Schemes
Farm Analytics
Voice Assistant
Multi-language
PWA

Each feature:

icon
visual
description
benefit
CTA

==================================================
AI MODULES PAGE
==================================================

Detailed modules:

Disease Detection
Crop Recommendation
Fertilizer
Irrigation
Profit Prediction
Market Intelligence
AI Assistant

Show:

workflow
input
AI processing visualization
expected output
benefits

==================================================
HOW IT WORKS PAGE
==================================================

Interactive timeline:

1 Register
2 Add Farm
3 Add Crop
4 Scan/Enter Information
5 AI Analysis
6 Recommendation
7 Take Action
8 Improve Yield

==================================================
FAQ PAGE
==================================================

Accessible accordion.

==================================================
CONTACT PAGE
==================================================

Include:

Contact form
Email
Phone
Location
Social links
Map section

Use proper form validation.

==================================================
FOOTER
==================================================

Create professional footer.

Quick Links
Features
Resources
Support
Languages
Social Media
Copyright

==================================================
AUTHENTICATION UI
==================================================

Create:

Login
Register
Forgot Password
Reset Password

Use React Hook Form + Zod.

Include:

validation
loading
error
success
password visibility
accessible labels

Do not fake successful authentication.

==================================================
APPLICATION SHELL
==================================================

Desktop:

Left sidebar
Top header
Main content

Sidebar:

Dashboard
My Farms
Crops
AI Modules
Weather
Market Prices
Schemes
Notifications
AI Assistant
History
Profile
Settings

Mobile:

Top header
Bottom navigation

Bottom navigation:

Home
Farm
AI
Market
Profile

Additional modules via drawer/menu.

==================================================
FARMER DASHBOARD
==================================================

Route:

/app

Match the attached reference style.

Include:

Welcome back

Weather card

Farm statistics

My Farms

Current Crops

Today's Weather

Quick Actions

Recent AI Recommendations

Market Prices

Notifications

Quick actions:

Scan Disease
Crop Recommendation
Fertilizer Advice
Irrigation Advice
Market Prices
AI Assistant
Schemes
Weather

Keep it simple.

Do not make it a generic BI dashboard.

==================================================
FARM MANAGEMENT
==================================================

Routes:

/app/farm
/app/farm/:farmId
/app/fields

Create:

Farm list
Farm details
Add farm
Edit farm
Fields
Farm crops
Farm activity

Fields:

Farm name
Location
Area
Area unit
Soil type
Irrigation type
Ownership

Use React Hook Form + Zod.

==================================================
CROP MANAGEMENT
==================================================

Route:

/app/crops

Create:

Crop list
Crop details
Current crops
Crop season
Crop information
Crop history

Use clean image-based cards.

==================================================
DISEASE DETECTION
==================================================

Route:

/app/disease

Create mobile-first scanning UI.

Include:

Upload
Camera-friendly interface
Preview
Analyze
Loading
Result
Disease name
Confidence
Symptoms
Recommended action
Prevention
Save
Share
History

Do not fake successful AI predictions.

==================================================
CROP RECOMMENDATION
==================================================

Create:

soil inputs
NPK
temperature
humidity
pH
rainfall
location

Result:

recommended crops
suitability
reason
benefits

==================================================
FERTILIZER RECOMMENDATION
==================================================

Create:

Input
Analysis
Recommended fertilizer
Dosage
Application guidance
Safety information
History

==================================================
IRRIGATION
==================================================

Create:

Soil moisture
Temperature
Humidity
Rainfall
Crop
Field

Result:

Water requirement
Irrigation recommendation
Timing
Water-saving tips

==================================================
WEATHER
==================================================

Route:

/app/weather

Include:

Current weather
Forecast
Alerts
Humidity
Wind
Rain
Farming advice

==================================================
MARKET
==================================================

Route:

/app/market

Include:

Market overview
Market list
Crop selector
Market selector
Price cards
Today
Yesterday
Percentage change
Price trends
Search
Filters

Use Recharts where useful.

==================================================
GOVERNMENT SCHEMES
==================================================

Route:

/app/schemes

Include:

Scheme cards
Search
Filters
State
Category
Eligibility
Important dates
Application information

==================================================
AI ASSISTANT
==================================================

Route:

/app/assistant

Create a simple messaging experience.

Include:

Conversation list
Chat
User messages
Assistant messages
Typing state
Suggested questions
Voice button
Clear conversation

Do not fake AI responses.

==================================================
NOTIFICATIONS
==================================================

Route:

/app/notifications

Include:

Unread
Read
Details
Mark read
Mark all read
Delete
Empty state

==================================================
HISTORY
==================================================

Route:

/app/history

Show:

Disease scans
Crop recommendations
Fertilizer recommendations
Irrigation recommendations
AI conversations

Include filters and pagination UI.

==================================================
PROFILE
==================================================

Route:

/app/profile

Include:

Profile information
Avatar
Language
Preferences
Notifications
Theme
Password
Logout

==================================================
SETTINGS
==================================================

Route:

/app/settings

Include:

Theme
Language
Notifications
Privacy
About
App version
PWA installation

==================================================
ADMIN
==================================================

Create a complete admin interface matching the attached reference.

Routes:

/admin
/admin/users
/admin/farms
/admin/crops
/admin/disease
/admin/recommendations
/admin/markets
/admin/schemes
/admin/notifications
/admin/reports
/admin/audit-logs
/admin/settings

Dashboard:

Total Users
Total Farms
Disease Scans
Recommendations

Charts:

User Growth
Disease Distribution
Recommendation Trends

Tables:

Recent Users
Recent Disease Scans

Admin navigation:

Dashboard
Users
Farms
Crops
Disease Scans
Recommendations
Market Prices
Schemes
Notifications
Reports
Audit Logs
Settings

==================================================
API LAYER
==================================================

Create:

apiClient

Axios interceptors.

Environment variable:

VITE_API_BASE_URL

API prefix:

/api/v1

Services:

authService
userService
farmService
cropService
diseaseService
recommendationService
weatherService
marketService
schemeService
notificationService
assistantService
historyService
adminService

Normalize API errors.

Do not hardcode API URLs.

Do not fake successful production API responses.

If backend endpoint is unavailable, show:

Loading
Empty
Error

states.

==================================================
I18N
==================================================

Default:

English

Prepare for:

Telugu
Hindi
Tamil
Kannada
Malayalam
Marathi
Bengali
Punjabi
Gujarati
Odia

Do not hardcode user-facing text.

Use translation keys.

Create language selector.

==================================================
PWA
==================================================

Implement:

manifest
icons
service worker
install prompt
install banner
offline fallback
standalone mode
theme color

Must be installable on Android and desktop.

==================================================
ACCESSIBILITY
==================================================

Implement:

Semantic HTML
ARIA labels
Keyboard navigation
Focus states
Screen-reader support
Accessible forms
Accessible dialogs
Proper heading hierarchy
Reduced motion
Good color contrast

==================================================
ANIMATIONS
==================================================

Use Framer Motion.

Animations should be:

subtle
fast
professional

Use for:

page transitions
card entrance
hover
drawer
modal
feedback

Respect reduced-motion preferences.

==================================================
PERFORMANCE
==================================================

Use:

lazy routes
code splitting
optimized assets
reusable components
memoization where useful

Avoid unnecessary dependencies.

==================================================
COMMON COMPONENTS
==================================================

Create reusable:

Button
IconButton
Input
Textarea
Select
Checkbox
Radio
Switch
Card
Dialog
Modal
Sheet
Tabs
Badge
Avatar
Tooltip
Dropdown
Toast
Spinner
Loader
Skeleton
EmptyState
ErrorState
ConfirmDialog
PageContainer
Section
ResponsiveGrid
StatCard
FeatureCard
ImageCard
ActionCard
StatusBadge
SearchBar
FilterBar
Pagination
Breadcrumbs

==================================================
LOADING / ERROR / EMPTY
==================================================

Every data-driven page must have:

Loading state
Success state
Empty state
Error state

Never leave blank screens.

==================================================
SECURITY
==================================================

Never expose:

API keys
Gemini keys
database credentials
backend secrets

Frontend role guards are only UX protection.

Backend remains authoritative.

==================================================
FINAL QUALITY CONTROL
==================================================

Actually run:

npm install
npm run lint
npm run build
npm run dev

Fix every error.

Do not stop after finding the first error.

Fix:

TypeScript errors
build errors
broken imports
broken routes
runtime errors
responsive problems
accessibility problems

==================================================
ROUTE VERIFICATION
==================================================

Verify every route:

/
 /about
 /features
 /ai-modules
 /how-it-works
 /faq
 /contact
 /login
 /register
 /forgot-password
 /reset-password
 /app
 /app/farm
 /app/farm/:farmId
 /app/fields
 /app/crops
 /app/disease
 /app/recommendations
 /app/weather
 /app/market
 /app/schemes
 /app/notifications
 /app/assistant
 /app/history
 /app/profile
 /app/settings
 /admin
 /admin/users
 /admin/farms
 /admin/crops
 /admin/disease
 /admin/recommendations
 /admin/markets
 /admin/schemes
 /admin/notifications
 /admin/reports
 /admin/audit-logs
 /admin/settings
 /404

==================================================
RESPONSIVE QA
==================================================

Check:

320px
375px
425px
768px
1024px
1280px
1440px

Verify:

No horizontal scrolling
No overlapping content
No clipped text
No broken navigation
No tiny touch controls
Mobile bottom navigation works
Desktop sidebar works
Tablet navigation works

==================================================
CRITICAL DEFINITION OF DONE
==================================================

DO NOT say the frontend is complete merely because files were created.

The frontend is complete only when:

✓ Public website is complete
✓ Home is polished
✓ No farmer/person image in Home hero
✓ About works
✓ Features works
✓ AI Modules works
✓ How It Works works
✓ FAQ works
✓ Contact works
✓ Login works
✓ Register works
✓ Password flows exist
✓ Farmer dashboard works
✓ Farm management works
✓ Field management works
✓ Crop management works
✓ Disease UI works
✓ Crop recommendation UI works
✓ Fertilizer UI works
✓ Irrigation UI works
✓ Weather works
✓ Market works
✓ Government schemes works
✓ Notifications works
✓ AI Assistant works
✓ History works
✓ Profile works
✓ Settings works
✓ Admin interface works
✓ Routing works
✓ Responsive design works
✓ i18n architecture works
✓ Theme switching works
✓ PWA works
✓ Accessibility is implemented
✓ Loading/error/empty states exist
✓ API service architecture exists
✓ npm run build succeeds
✓ npm run lint succeeds

IMPORTANT:

The Spring Boot backend may not currently provide every endpoint.

Do not block frontend completion because of this.

Build the complete frontend UI and API integration architecture.

Where an API is unavailable, show a proper error/empty state rather than fake successful production data.

DO NOT create backend code.

DO NOT create FastAPI.

DO NOT ask questions.

DO NOT return a plan.

IMPLEMENT THE COMPLETE FRONTEND NOW.

At the end, provide a concise summary of:

• files created/modified
• routes implemented
• major components
• API services
• i18n
• PWA
• build result
• lint result
• remaining backend-dependent integrations

The final application must visually follow the attached Smart Farmer AI reference and must be a polished, light, modern, responsive consumer application.

This project was built with [Lovable](https://lovable.dev).

## Build with Lovable

Continue developing this project in the [Lovable editor](https://lovable.dev/projects/0cf428b6-a2b8-40cb-8bb9-3b283670f95b).

- **Ship faster**: describe what you want to build and Lovable handles the code.
- **Stay in sync**: every change made in Lovable is committed straight to this repository.
- **Full ownership**: this code is yours. Push to `main` on GitHub and your changes sync back into Lovable, ready for your next prompt.

## Development

Prefer working locally? You need Node.js and npm — [install with nvm](https://github.com/nvm-sh/nvm#installing-and-updating).

```sh
git clone <this-repository-url>
cd <repository-name>
npm i
npm run dev
```
