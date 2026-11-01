package com.example.data.ai

data class KnowledgeEntry(
    val title: String,
    val category: String,
    val keywords: List<String>,
    val answer: String,
    val relatedLinks: List<String> = emptyList()
)

object OfflineKnowledgeBase {

    private val entries = listOf(
        // Platform Usage & Instructions
        KnowledgeEntry(
            title = "How the Academic Resource Platform Works",
            category = "Platform Guide",
            keywords = listOf("how", "platform", "work", "website", "app", "features", "use", "overview", "help"),
            answer = "The UNIMAID Business Administration Academic Resource Platform connects students with verified academic materials:\n\n" +
                    "1. **Resource Bank**: Browse past questions, lecture notes, textbooks and study guides organized into 100L, 200L, 300L and 400L.\n" +
                    "2. **Student Uploads**: Submit your verified study materials (PDF, DOCX, PPTX up to 100MB) to help course mates.\n" +
                    "3. **Suggestions & Complaints**: Submit anonymous or identified feedback directly to departmental EXCOS.\n" +
                    "4. **AI Student Assistant**: Works both online and offline to answer departmental questions, explain business concepts and locate notes."
        ),
        KnowledgeEntry(
            title = "How to Upload Materials",
            category = "Platform Guide",
            keywords = listOf("upload", "contribute", "share", "materials", "file", "submit note", "add document"),
            answer = "To contribute academic resources:\n\n" +
                    "1. Tap **Upload** on the bottom navigation bar or the home screen button.\n" +
                    "2. Select your Level (100L, 200L, 300L, or 400L) and target Course.\n" +
                    "3. Choose the Category: Past Questions, Lecture Notes, Assignments, Textbooks, or Study Guides.\n" +
                    "4. Pick a file from your phone storage (PDF, DOC, DOCX, XLS, XLSX, PPT, PPTX up to 100MB).\n" +
                    "5. Fill in the title and description, then tap **Submit for Review**.\n\n" +
                    "⚠️ *Security Notice*: All uploads undergo EXCOS content review to prevent malware or inappropriate content before appearing in the public Resource Bank."
        ),
        KnowledgeEntry(
            title = "How to Download Past Questions & Lecture Notes",
            category = "Platform Guide",
            keywords = listOf("download", "past question", "lecture note", "save", "access", "view materials"),
            answer = "To download study materials:\n\n" +
                    "1. Open the **Resources** tab.\n" +
                    "2. Filter by your level (100L, 200L, 300L, 400L) or use the search bar to type a course code (e.g. `BUS 101`, `FIN 301`).\n" +
                    "3. Tap on any resource card to inspect its description, syllabus coverage, and file size.\n" +
                    "4. Click **Download / Open Document** to save the file locally to your device or view the built-in study notes."
        ),
        KnowledgeEntry(
            title = "Suggestions and Complaints Centre",
            category = "Platform Guide",
            keywords = listOf("complaint", "suggestion", "feedback", "welfare", "problem", "anonymous", "excos", "report issue"),
            answer = "The Suggestions & Complaints Centre empowers every student to communicate with the department:\n\n" +
                    "• **Categories**: Academic Issues, Departmental Improvement, Student Welfare, Academic Programmes, Complaints, General Suggestions.\n" +
                    "• **Privacy**: You can toggle **Submit Anonymously** to protect your identity. Your personal details are never shown publicly.\n" +
                    "• **Tracking**: You receive a unique Ticket Number (e.g., `UNM-BA-2025-001`) to check the status and read official EXCOS responses."
        ),
        KnowledgeEntry(
            title = "EXCOS Administrative Portal and Changing Security PIN",
            category = "EXCOS Governance",
            keywords = listOf("excos", "portal", "pin", "passcode", "admin", "change pin", "reset pin", "handover", "password"),
            answer = "The EXCOS Administrative Portal allows departmental executives to review uploaded study materials, respond to student complaints, and manage courses:\n\n" +
                    "• **Default PIN**: The initial departmental default PIN is `2026` (Master recovery code: `unimaid`).\n" +
                    "• **How to Change PIN**:\n" +
                    "  1. Tap the shield icon in the top header or navigate to the EXCOS Portal.\n" +
                    "  2. On the lock screen, tap **Change EXCOS Portal PIN**, or if already logged in, tap **Change PIN** in the dashboard top bar or under **Handover & Governance**.\n" +
                    "  3. Enter your current PIN (or master code `unimaid`), your new PIN (minimum 4 characters/digits), and confirm.\n" +
                    "  4. Tap **Save New PIN**. The new PIN is securely saved and takes effect immediately across all sessions.\n" +
                    "• **Resetting**: If needed, authenticated officers can also tap **Reset to 2026** under the Governance tab."
        ),


        // Academic Structure & Grading
        KnowledgeEntry(
            title = "UNIMAID Grading System and CGPA Calculation",
            category = "Academics",
            keywords = listOf("cgpa", "gpa", "grade", "grading", "calculate", "first class", "second class", "scale", "points"),
            answer = "The University of Maiduguri operates on a 5.00 Cumulative Grade Point Average (CGPA) system:\n\n" +
                    "• **A (70% - 100%)**: 5 Grade Points\n" +
                    "• **B (60% - 69%)**: 4 Grade Points\n" +
                    "• **C (50% - 59%)**: 3 Grade Points\n" +
                    "• **D (45% - 49%)**: 2 Grade Points\n" +
                    "• **E (40% - 44%)**: 1 Grade Point\n" +
                    "• **F (0% - 39%)**: 0 Grade Points (Fail)\n\n" +
                    "**Degree Classifications**:\n" +
                    "• First Class Honours: 4.50 – 5.00\n" +
                    "• Second Class Upper (2:1): 3.50 – 4.49\n" +
                    "• Second Class Lower (2:2): 2.40 – 3.49\n" +
                    "• Third Class: 1.50 – 2.39\n\n" +
                    "**CGPA Formula**: Total Quality Points (Sum of Grade Points × Course Units) ÷ Total Credit Units Registered."
        ),
        KnowledgeEntry(
            title = "Continuous Assessment (CA) and Examination Structure",
            category = "Academics",
            keywords = listOf("ca", "continuous assessment", "exam", "test", "marks", "score", "30%", "70%"),
            answer = "In the Department of Business Administration, course assessments follow the standard NUC and UNIMAID structure:\n\n" +
                    "• **Continuous Assessment (CA)**: 30% (Includes mid-semester tests, term papers, individual/group assignments, and lecture attendance).\n" +
                    "• **Semester Examination**: 70% (Written final examinations conducted at the Faculty of Management Sciences).\n" +
                    "• **Total**: 100%.\n\n" +
                    "A minimum of 75% lecture attendance is required to qualify to sit for the semester examination."
        ),

        // Level-by-Level Syllabus and Courses
        KnowledgeEntry(
            title = "100 Level Course Overview",
            category = "Curriculum",
            keywords = listOf("100 level", "100l", "first year", "bus 101", "acc 101", "ecn 101", "sms 101", "bus 102"),
            answer = "100 Level Business Administration core courses introduce management foundations:\n\n" +
                    "• **BUS 101**: Introduction to Business I (Forms of business, economic environment, entrepreneurship)\n" +
                    "• **ACC 101**: Principles of Accounting I (Double-entry, cash books, trial balance)\n" +
                    "• **ECN 101**: Principles of Economics I (Microeconomics, demand/supply, elasticity)\n" +
                    "• **SMS 101**: Business Mathematics I (Algebra, equations, sets, introductory calculus)\n" +
                    "• **BUS 102**: Introduction to Business II (Management functions, marketing, finance, personnel)"
        ),
        KnowledgeEntry(
            title = "200 Level Course Overview",
            category = "Curriculum",
            keywords = listOf("200 level", "200l", "second year", "bus 201", "mkt 201", "bus 203", "fin 201", "bus 202"),
            answer = "200 Level courses deepen managerial and functional business skills:\n\n" +
                    "• **BUS 201**: Principles of Management I (Management theories: Taylor, Fayol, Weber; planning, organizing)\n" +
                    "• **MKT 201**: Principles of Marketing I (4Ps marketing mix, consumer behaviour, STP)\n" +
                    "• **BUS 203**: Business Communication (Business letters, memos, technical reporting)\n" +
                    "• **FIN 201**: Elements of Finance (Nigerian financial institutions, capital markets, interest rates)\n" +
                    "• **BUS 202**: Principles of Management II (Staffing, leading, motivating, organizational control)"
        ),
        KnowledgeEntry(
            title = "300 Level Course Overview",
            category = "Curriculum",
            keywords = listOf("300 level", "300l", "third year", "bus 301", "bus 303", "fin 301", "bus 305", "mkt 301"),
            answer = "300 Level courses transition into analytical and supervisory disciplines:\n\n" +
                    "• **BUS 301**: Organizational Behaviour (Individual & group dynamics, leadership, organizational culture)\n" +
                    "• **BUS 303**: Business Law (Nigerian law of contract, sale of goods, agency, partnership)\n" +
                    "• **FIN 301**: Financial Management (Capital budgeting: NPV, IRR; cost of capital, dividend policy)\n" +
                    "• **BUS 305**: Research Methodology (Scientific inquiry, hypothesis testing, sampling, questionnaire design)\n" +
                    "• **MKT 301**: Marketing Management (Strategic brand management, pricing models, distribution channels)"
        ),
        KnowledgeEntry(
            title = "400 Level Course Overview",
            category = "Curriculum",
            keywords = listOf("400 level", "400l", "final year", "bus 401", "bus 403", "mgt 405", "mgt 407", "project"),
            answer = "400 Level focuses on capstone strategy, executive decision-making, and your final degree project:\n\n" +
                    "• **BUS 401**: Business Policy & Strategy I (Corporate strategy, SWOT, PESTLE, Porter's Five Forces, BCG Matrix)\n" +
                    "• **BUS 403**: International Business (Multinational management, exchange rates, foreign trade theories)\n" +
                    "• **MGT 405**: Operations Research (Linear programming, simplex method, transportation problem, CPM/PERT)\n" +
                    "• **MGT 407**: Small Business Management & Entrepreneurship (Feasibility studies, SME financing, business plans)\n" +
                    "• **BUS 499**: Final Year Research Project (Empirical dissertation guided by an assigned supervisor)"
        ),

        // Core Business Administration Concepts (Frequently Asked Academic Queries)
        KnowledgeEntry(
            title = "Henri Fayol's 14 Principles of Management (BUS 201)",
            category = "Business Concepts",
            keywords = listOf("fayol", "14 principles", "principles of management", "classical theory", "administrative management"),
            answer = "Henri Fayol (1841–1925), the father of modern operational management, formulated 14 key principles:\n\n" +
                    "1. **Division of Work**: Specialization increases output by making employees more efficient.\n" +
                    "2. **Authority & Responsibility**: Managers must give orders, accompanied by responsibility.\n" +
                    "3. **Discipline**: Obedience, application, energy, and outward marks of respect.\n" +
                    "4. **Unity of Command**: Each worker should receive orders from only one superior.\n" +
                    "5. **Unity of Direction**: One head and one plan for each group of activities with the same objective.\n" +
                    "6. **Subordination of Individual Interest**: General organizational interests supersede personal interests.\n" +
                    "7. **Remuneration**: Fair compensation for workers and the organization.\n" +
                    "8. **Centralization**: Proper balance between centralization and decentralization.\n" +
                    "9. **Scalar Chain**: Line of authority from highest executive to lowest rank (with 'gang plank' cross-communication).\n" +
                    "10. **Order**: Material and social order — a place for everything and everyone in their place.\n" +
                    "11. **Equity**: Kindness and justice toward employees.\n" +
                    "12. **Stability of Tenure**: Low employee turnover increases efficiency.\n" +
                    "13. **Initiative**: Allowing workers freedom to conceive and execute plans.\n" +
                    "14. **Esprit de Corps**: Promoting team spirit and unity."
        ),
        KnowledgeEntry(
            title = "The Marketing Mix: 4Ps and 7Ps (MKT 201 / MKT 301)",
            category = "Business Concepts",
            keywords = listOf("4ps", "7ps", "marketing mix", "product", "price", "place", "promotion"),
            answer = "The Marketing Mix represents the controllable tactical tools used to produce a desired market response:\n\n" +
                    "**Traditional 4Ps (for Goods)**:\n" +
                    "1. **Product**: Variety, quality, design, features, brand name, packaging, warranties.\n" +
                    "2. **Price**: List price, discounts, allowances, payment period, credit terms.\n" +
                    "3. **Place**: Distribution channels, coverage, locations, inventory, transport logistics.\n" +
                    "4. **Promotion**: Advertising, personal selling, sales promotion, public relations, direct marketing.\n\n" +
                    "**Extended 3Ps (for Services)**:\n" +
                    "5. **People**: Staff competence, customer service attitude, interpersonal training.\n" +
                    "6. **Process**: Operational workflow, service delivery steps, customer wait times.\n" +
                    "7. **Physical Evidence**: Environment, branding, receipts, certificates, ambience."
        ),
        KnowledgeEntry(
            title = "Strategic Analysis: SWOT and PESTLE Frameworks (BUS 401)",
            category = "Business Concepts",
            keywords = listOf("swot", "pestle", "strategy", "environmental scanning", "internal", "external"),
            answer = "Strategic environmental scanning tools evaluate internal capabilities and macro forces:\n\n" +
                    "**SWOT Analysis**:\n" +
                    "• **Strengths** (Internal): Distinctive capabilities, core competencies, brand reputation, patents.\n" +
                    "• **Weaknesses** (Internal): Resource limitations, obsolete facilities, high cost structure, poor brand awareness.\n" +
                    "• **Opportunities** (External): Emerging markets, tech advancements, favourable government subsidies.\n" +
                    "• **Threats** (External): Aggressive competitors, regulatory shifts, inflation, supply disruptions.\n\n" +
                    "**PESTLE Framework (Macro Environment)**:\n" +
                    "• **P**olitical: Stability, tax policies, trade tariffs.\n" +
                    "• **E**conomic: Inflation rate, GDP growth, exchange rates.\n" +
                    "• **S**ocial: Demographics, consumer lifestyles, cultural norms.\n" +
                    "• **T**echnological: Automation, R&D, digital communication.\n" +
                    "• **L**egal: Employment laws, consumer protection, health & safety.\n" +
                    "• **E**nvironmental: Climate change, sustainability, carbon footprint."
        ),
        KnowledgeEntry(
            title = "Porter's Five Forces Model (BUS 401)",
            category = "Business Concepts",
            keywords = listOf("porter", "five forces", "industry analysis", "competitive forces", "rivalry"),
            answer = "Developed by Michael Porter to determine industry attractiveness and profitability:\n\n" +
                    "1. **Threat of New Entrants**: Determined by barriers to entry (economies of scale, capital requirements, switching costs).\n" +
                    "2. **Bargaining Power of Buyers**: High when buyers are few, purchase in large volumes, or products are standardized.\n" +
                    "3. **Bargaining Power of Suppliers**: High when suppliers are concentrated, inputs are critical, or switching suppliers is costly.\n" +
                    "4. **Threat of Substitute Products**: Pressure from products in other industries that fulfill the same customer need.\n" +
                    "5. **Competitive Rivalry Among Existing Firms**: Intensity of price wars, advertising battles, and competitive maneuvers."
        ),
        KnowledgeEntry(
            title = "Capital Budgeting Techniques (FIN 301)",
            category = "Business Concepts",
            keywords = listOf("npv", "irr", "capital budgeting", "payback", "wacc", "investment appraisal"),
            answer = "Capital budgeting evaluates long-term investment projects:\n\n" +
                    "1. **Net Present Value (NPV)**: Present value of future cash inflows minus initial outlay. (Accept if NPV > 0). Considered the gold standard.\n" +
                    "2. **Internal Rate of Return (IRR)**: The discount rate at which NPV equals zero. (Accept if IRR > Cost of Capital).\n" +
                    "3. **Payback Period (PBP)**: Time required to recoup initial investment outlay. Simpler, but ignores cash flows after payback and time value of money.\n" +
                    "4. **Profitability Index (PI)**: Present value of cash inflows divided by initial cost. (Accept if PI > 1.0)."
        ),
        KnowledgeEntry(
            title = "Departmental EXCOS & Student Representation",
            category = "Department Info",
            keywords = listOf("excos", "executive", "president", "secretaries", "association", "representation", "who are the excos"),
            answer = "The Departmental Executive Council (EXCOS) for the current 2024/2025 session represents all Business Administration scholars:\n\n" +
                    "• **President**: Comrade Ibrahim Bukar (18/08/04/001)\n" +
                    "• **Academic Secretary**: Aisha Mohammed (19/08/04/012)\n" +
                    "• **Welfare Director**: Emmanuel Danladi (19/08/04/035)\n" +
                    "• **Public Relations Officer**: Khadija Umar (19/08/04/048)\n" +
                    "• **General Secretary & Admin**: Musa Dahiru (18/08/04/089)\n\n" +
                    "You can reach out through the Suggestions & Complaints Centre or visit the Departmental Secretariat, Faculty of Management Sciences."
        )
    )

    fun search(query: String): String? {
        val cleanQuery = query.lowercase().trim()
        val queryTokens = cleanQuery.split(Regex("[\\s,?.!]+")).filter { it.isNotBlank() }

        var bestMatch: KnowledgeEntry? = null
        var maxScore = 0

        for (entry in entries) {
            var score = 0
            for (keyword in entry.keywords) {
                if (cleanQuery.contains(keyword)) {
                    score += 3
                }
                for (token in queryTokens) {
                    if (token == keyword || (token.length > 3 && keyword.contains(token))) {
                        score += 2
                    }
                }
            }
            if (cleanQuery.contains(entry.title.lowercase())) {
                score += 5
            }
            if (score > maxScore) {
                maxScore = score
                bestMatch = entry
            }
        }

        return if (maxScore >= 3) {
            bestMatch?.answer
        } else {
            null
        }
    }

    fun getAllQuickPrompts(): List<String> = listOf(
        "Where are BUS 101 and 201 past questions?",
        "How do I upload lecture notes?",
        "Explain Fayol's 14 Principles of Management",
        "How is CGPA calculated at UNIMAID?",
        "Explain the Marketing Mix 4Ps and 7Ps",
        "How to submit an anonymous complaint?",
        "What are Porter's Five Forces?",
        "Who are the current departmental EXCOS?"
    )
}
