# UNIMAID Business Administration Academic Resource Platform - Project Documentation

## 1. Project Summary (For Showcase)
I have developed a centralized Academic Resource Platform for the Department of Business Administration, University of Maiduguri (UNIMAID). The platform serves as a digital bridge between students, academic syllabus resources, and departmental leadership.

### Key Features:
*   **Central Cloud Storage:** Direct integration with the Departmental Google Drive repository (`https://drive.google.com/drive/folders/1T7B6b0VosY2DmF30nDuIDk6zvjgBDbnO`), allowing students and lecturers to access notes, past questions, and slides across any device (Android, iPhone, PC) without complex logins or database limits.
*   **Academic Resource Bank:** A searchable repository for 100L–400L course materials (past questions, lecture notes, textbooks).
*   **Simplified Student Uploads:** A dual-mode upload system: 1-tap direct deposit into the Google Drive cloud folder without size limits, or structured submission with tracking codes (`UB-SUB-XXXXXX`) for EXCOS verification.
*   **Administrative Moderation:** A dedicated EXCOS dashboard allowing authorized officers to manage uploads, handle student grievances, and open the Google Drive cloud folder directly.
*   **Student Feedback & AI:** Includes an integrated suggestions/complaints ticketing system and a departmental AI assistant for academic queries (Fayol's 14 principles, UNIMAID 5.0 CGPA calculation).
*   **Mobile-First & Clean UI:** Material Design 3 styling, horizontal sliding quick-navigation bar, and PWA / Android APK support.

---

## 2. Master Build Prompt (For AI Agents / Future Development)
"Act as a senior software engineer. Build an intuitive, cross-device academic resource management platform for a university department using the following requirements:

1.  **Architecture:** Single-page web application (HTML5/Vanilla JS/PWA) hosted on GitHub Pages, with central cloud file storage linked to the official departmental Google Drive folder (`https://drive.google.com/drive/folders/1T7B6b0VosY2DmF30nDuIDk6zvjgBDbnO`).
2.  **Google Drive Cloud Integration:**
    *   Folder URL: `https://drive.google.com/drive/folders/1T7B6b0VosY2DmF30nDuIDk6zvjgBDbnO`.
    *   Prominent 'Open Google Drive Cloud Folder' action buttons in navigation, hero, resource cards, upload section, and EXCOS admin panel.
    *   Resource bank cards offer direct 'View in Drive' shortcuts alongside local previews and downloads.
3.  **Local Resilient Data Layer:**
    *   Use browser IndexedDB (with fallback) for instant, offline-capable caching of course notes, pending submissions, complaints tickets, leadership directory, and audit logs.
    *   No external paid databases or broken API key dependencies.
4.  **Features:**
    *   **Resource Bank:** Filterable strictly by 100L, 200L, 300L, and 400L with course code chips (BUS 101, 103, 201, 203, 301, 305, 401, 403).
    *   **Student Contribution:** 1-tap direct upload to Google Drive plus submission form generating unique tracking IDs.
    *   **EXCOS Dashboard:** Passcode-protected administrative console with moderation queue, active resources, recycle bin, feedback ticketing, and direct Google Drive cloud access.
    *   **AI Tutoring & Complaints:** Departmental knowledge engine and transparent ticket tracking.
5.  **UI/UX:**
    *   Material Design 3 with UNIMAID branding (#0B1E36 navy, #F59E0B gold).
    *   Horizontal sliding quick-navigation bar for seamless section jumping on mobile phones."
