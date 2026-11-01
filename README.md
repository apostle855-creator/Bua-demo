# University of Maiduguri (UNIMAID)
## Department of Business Administration Academic Resource Platform & EXCOS Portal

Official central academic resource platform and governance management application for the Department of Business Administration, Faculty of Management Sciences, University of Maiduguri, Borno State, Nigeria.

---

### 🌟 Key Platform Features

1. **Academic Resource Bank (100L – 400L)**
   - Past examination questions, lecture notes, textbooks, study guides, and assignments.
   - Organized by Level (100 Level, 200 Level, 300 Level, 400 Level) and course codes (e.g., BUS 101, BUS 201, BUS 301, BUS 401).
   - Fast keyword searching and filtering by document format (PDF, DOCX, XLSX, PPTX).

2. **Student Upload System**
   - Direct material contributions up to 100MB per file.
   - Executive review pipeline ensures quality and relevance prior to public release.

3. **Suggestions & Complaints Centre**
   - Private and anonymous ticket submission for students.
   - Categorized by Academic Issues, Student Welfare, Departmental Improvement, Complaints, and General Suggestions.
   - Real-time ticket tracking with EXCOS status updates (Pending, Under Review, Resolved, Rejected).

4. **Integrated AI Student Assistant**
   - Dual-engine: Works both Online (Firebase AI / Gemini) and Offline (Local Departmental Knowledge Base).
   - Instant answers on UNIMAID 5.00 CGPA grading scale, Henri Fayol's 14 Principles of Management, syllabus navigation, and platform FAQs.

5. **EXCOS Administrative Portal**
   - Secure access passcode (Default PIN: `2026`).
   - PIN management: Authorized officers can set custom passcodes or restore via master recovery.
   - **One-Click Logout**: Dedicated red Logout button with confirmation dialog for secure session closure.
   - Content moderation queue for student uploads and report handling.
   - Executive handover log for smooth transition across academic sessions.

---

### 🌐 GitHub Pages Web Publishing (`index.html`)

The root `index.html` file contains a responsive web showcase of the platform that can be deployed to GitHub Pages with zero configuration:

1. Push this repository to GitHub.
2. In your repository on GitHub, navigate to **Settings** > **Pages**.
3. Under **Build and deployment** > **Branch**, select `main` (or `master`) and directory `/ (root)`.
4. Click **Save**. GitHub Pages will publish your site at `https://<username>.github.io/<repository-name>/`.

---

### 📱 Android Application Setup & Build

This project is built using modern Android Jetpack Compose and Material Design 3.

#### Prerequisites
- Android Studio (Koala, Ladybug, or newer)
- JDK 17 / 21
- Android SDK 34 / 35 / 36

#### Building from Command Line
```bash
# Clone the repository
git clone https://github.com/apostle855/unimaid-busadmin.git
cd unimaid-busadmin

# Build debug APK
gradle assembleDebug

# Build release APK
gradle assembleRelease

# Run unit tests
gradle :app:testDebugUnitTest
```

The compiled APK will be located in:
`app/build/outputs/apk/release/app-release.apk`
or
`app/build/outputs/apk/debug/app-debug.apk`

---

### 🏛️ Executive Council (EXCOS) 2025/2026
- **President**: Ibrahim Modu
- **Vice President**: Fatima Abubakar
- **Secretary General**: Musa Usman
- **Academic Director**: Zainab Mustapha
- **Financial Secretary**: Babagana Kyari

---

### 📄 License & Attribution
Department of Business Administration, University of Maiduguri (UNIMAID), Borno State, Nigeria.
All rights reserved.
