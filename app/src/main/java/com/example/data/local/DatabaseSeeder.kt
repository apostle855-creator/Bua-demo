package com.example.data.local

import com.example.data.model.AcademicResource
import com.example.data.model.Course
import com.example.data.model.DepartmentAnnouncement
import com.example.data.model.ExcosMember
import com.example.data.model.Level
import com.example.data.model.ModerationStatus
import com.example.data.model.ResourceType
import com.example.data.model.SuggestionCategory
import com.example.data.model.SuggestionComplaint
import com.example.data.model.SuggestionStatus

object DatabaseSeeder {
    suspend fun seedInitialData(database: AppDatabase) {
        val courseDao = database.courseDao()
        val resourceDao = database.resourceDao()
        val suggestionDao = database.suggestionDao()
        val departmentDao = database.departmentDao()

        // 1. Seed Departmental Courses (100L - 400L)
        val initialCourses = listOf(
            // 100 Level
            Course(code = "BUS 101", title = "Introduction to Business I", level = Level.LEVEL_100.name, semester = "First Semester", creditUnits = 3, description = "Basic concepts of business, forms of business ownership, and economic environment.", lecturerInCharge = "Dr. M. S. Kyari"),
            Course(code = "ACC 101", title = "Principles of Accounting I", level = Level.LEVEL_100.name, semester = "First Semester", creditUnits = 3, description = "Double entry system, journalizing, ledger accounts, and trial balance.", lecturerInCharge = "Mal. Usman Ali"),
            Course(code = "ECN 101", title = "Principles of Economics I", level = Level.LEVEL_100.name, semester = "First Semester", creditUnits = 3, description = "Microeconomic theory, consumer behaviour, supply and demand equilibrium.", lecturerInCharge = "Prof. Z. B. Goni"),
            Course(code = "SMS 101", title = "Business Mathematics I", level = Level.LEVEL_100.name, semester = "First Semester", creditUnits = 2, description = "Algebra, equations, sets theory and calculus applications in business.", lecturerInCharge = "Dr. H. Lawan"),
            Course(code = "BUS 102", title = "Introduction to Business II", level = Level.LEVEL_100.name, semester = "Second Semester", creditUnits = 3, description = "Functional areas of business: management, marketing, production, finance and personnel.", lecturerInCharge = "Dr. M. S. Kyari"),
            
            // 200 Level
            Course(code = "BUS 201", title = "Principles of Management I", level = Level.LEVEL_200.name, semester = "First Semester", creditUnits = 3, description = "Evolution of management thought, classical theories, planning and organizing.", lecturerInCharge = "Prof. B. K. Mustapha"),
            Course(code = "MKT 201", title = "Principles of Marketing I", level = Level.LEVEL_200.name, semester = "First Semester", creditUnits = 3, description = "The 4Ps marketing mix, market segmentation, targeting, and consumer behaviour.", lecturerInCharge = "Dr. Fatima Bulama"),
            Course(code = "BUS 203", title = "Business Communication", level = Level.LEVEL_200.name, semester = "First Semester", creditUnits = 2, description = "Written and oral communication, technical report writing, and meeting procedures.", lecturerInCharge = "Mrs. R. Garba"),
            Course(code = "FIN 201", title = "Elements of Finance", level = Level.LEVEL_200.name, semester = "Second Semester", creditUnits = 3, description = "Financial markets, money and capital markets, financial statements analysis.", lecturerInCharge = "Dr. I. D. Ndahi"),
            Course(code = "BUS 202", title = "Principles of Management II", level = Level.LEVEL_200.name, semester = "Second Semester", creditUnits = 3, description = "Leading, motivating, controlling, conflict resolution, and organizational dynamics.", lecturerInCharge = "Prof. B. K. Mustapha"),

            // 300 Level
            Course(code = "BUS 301", title = "Organizational Behaviour", level = Level.LEVEL_300.name, semester = "First Semester", creditUnits = 3, description = "Individual differences, group dynamics, motivation theories, leadership and culture.", lecturerInCharge = "Dr. A. B. Shettima"),
            Course(code = "BUS 303", title = "Business Law", level = Level.LEVEL_300.name, semester = "First Semester", creditUnits = 3, description = "Nigerian commercial law, contract law, sale of goods act, agency and partnership.", lecturerInCharge = "Barr. T. A. Alkali"),
            Course(code = "FIN 301", title = "Financial Management", level = Level.LEVEL_300.name, semester = "First Semester", creditUnits = 3, description = "Capital budgeting, cost of capital, dividend policy, and working capital management.", lecturerInCharge = "Prof. S. O. Ogundele"),
            Course(code = "BUS 305", title = "Research Methodology", level = Level.LEVEL_300.name, semester = "Second Semester", creditUnits = 3, description = "Scientific research design, sampling methods, questionnaire design and data analysis.", lecturerInCharge = "Dr. M. S. Kyari"),
            Course(code = "MKT 301", title = "Marketing Management", level = Level.LEVEL_300.name, semester = "Second Semester", creditUnits = 3, description = "Strategic marketing planning, branding, channel management and pricing strategies.", lecturerInCharge = "Dr. Fatima Bulama"),

            // 400 Level
            Course(code = "BUS 401", title = "Business Policy & Strategy I", level = Level.LEVEL_400.name, semester = "First Semester", creditUnits = 3, description = "Capstone strategy course: environmental scanning, SWOT, Porter's Five Forces, formulation.", lecturerInCharge = "Prof. M. B. Yunusa"),
            Course(code = "BUS 403", title = "International Business", level = Level.LEVEL_400.name, semester = "First Semester", creditUnits = 3, description = "Multinational corporations, international trade theories, foreign exchange and tariffs.", lecturerInCharge = "Dr. A. B. Shettima"),
            Course(code = "MGT 405", title = "Operations Research", level = Level.LEVEL_400.name, semester = "First Semester", creditUnits = 3, description = "Linear programming, simplex method, transportation problem, and network analysis.", lecturerInCharge = "Dr. H. Lawan"),
            Course(code = "MGT 407", title = "Small Business Management & Entrepreneurship", level = Level.LEVEL_400.name, semester = "Second Semester", creditUnits = 3, description = "Feasibility studies, business plan development, venture capital, and SME operations.", lecturerInCharge = "Prof. B. K. Mustapha"),
            Course(code = "BUS 402", title = "Business Policy & Strategy II", level = Level.LEVEL_400.name, semester = "Second Semester", creditUnits = 3, description = "Strategy implementation, corporate governance, organizational restructuring, and evaluation.", lecturerInCharge = "Prof. M. B. Yunusa")
        )
        courseDao.insertAllCourses(initialCourses)

        // 2. Seed Academic Resources
        val initialResources = listOf(
            // 100 Level
            AcademicResource(
                title = "BUS 101 Past Questions (2020 - 2024 Compiled)",
                description = "Complete compiled first semester examination questions for Introduction to Business I with answer hints and exam review guide.",
                courseCode = "BUS 101",
                courseTitle = "Introduction to Business I",
                level = Level.LEVEL_100.name,
                resourceType = ResourceType.PAST_QUESTION.name,
                fileFormat = "PDF",
                fileSizeMb = 3.4,
                uploadDate = "15 Jan 2025",
                contributorName = "Academic Committee",
                contributorMatric = "EXCOS/2024/01",
                moderationStatus = ModerationStatus.APPROVED.name,
                downloadCount = 342,
                previewSummary = "Sections covering: Forms of business ownership (Sole Proprietorship, Partnership, LLC), Business environmental factors (Internal and External PESTLE), and the social responsibilities of business in Northern Nigeria."
            ),
            AcademicResource(
                title = "ACC 101 Comprehensive Lecture Notes & Solved Drills",
                description = "Full lecture notes covering journal entries, ledger posting, 3-column cash book, bank reconciliation and final accounts preparation.",
                courseCode = "ACC 101",
                courseTitle = "Principles of Accounting I",
                level = Level.LEVEL_100.name,
                resourceType = ResourceType.LECTURE_NOTE.name,
                fileFormat = "PDF",
                fileSizeMb = 5.2,
                uploadDate = "18 Jan 2025",
                contributorName = "Mal. Usman Ali (Course Rep)",
                moderationStatus = ModerationStatus.APPROVED.name,
                downloadCount = 289,
                previewSummary = "Key concepts: The accounting equation (Assets = Liabilities + Equity), Trial balance extraction techniques, error correction via Suspense Account, and adjustments for depreciation and accruals."
            ),
            AcademicResource(
                title = "ECN 101 Microeconomics Revision Handout",
                description = "Concise study summary on price elasticity, indifference curve analysis, market structures (Perfect competition vs Monopoly).",
                courseCode = "ECN 101",
                courseTitle = "Principles of Economics I",
                level = Level.LEVEL_100.name,
                resourceType = ResourceType.STUDY_GUIDE.name,
                fileFormat = "PDF",
                fileSizeMb = 2.1,
                uploadDate = "22 Jan 2025",
                contributorName = "Abba Kyari (100L Rep)",
                moderationStatus = ModerationStatus.APPROVED.name,
                downloadCount = 215,
                previewSummary = "Includes formula cheat sheets for price elasticity of demand (PED), cross elasticity (XED), consumer surplus calculations, and graphical analysis of diminishing returns."
            ),

            // 200 Level
            AcademicResource(
                title = "BUS 201 Principles of Management I Lecture Slides",
                description = "Professor Mustapha's approved lecture slides on Management Thought: Taylor's Scientific Management, Fayol's 14 Principles, and Weber's Bureaucracy.",
                courseCode = "BUS 201",
                courseTitle = "Principles of Management I",
                level = Level.LEVEL_200.name,
                resourceType = ResourceType.LECTURE_NOTE.name,
                fileFormat = "PPTX",
                fileSizeMb = 8.6,
                uploadDate = "05 Feb 2025",
                contributorName = "EXCOS Academic Bureau",
                moderationStatus = ModerationStatus.APPROVED.name,
                downloadCount = 412,
                previewSummary = "Detailed breakdown of planning types (Strategic, Tactical, Operational), MBO (Management by Objectives), Delegation of authority principles, and departmentalization frameworks."
            ),
            AcademicResource(
                title = "MKT 201 Past Questions & Marketing Case Studies",
                description = "Past questions from 2019 to 2023 with Nigerian market case studies (FMCG and telecommunications sector in Maiduguri).",
                courseCode = "MKT 201",
                courseTitle = "Principles of Marketing I",
                level = Level.LEVEL_200.name,
                resourceType = ResourceType.PAST_QUESTION.name,
                fileFormat = "PDF",
                fileSizeMb = 4.1,
                uploadDate = "10 Feb 2025",
                contributorName = "Fatima Mohammed",
                moderationStatus = ModerationStatus.APPROVED.name,
                downloadCount = 378,
                previewSummary = "Focuses on Product Life Cycle (PLC) stages, Ansoff's Growth Matrix, Market Segmentation variables (Geographic, Demographic, Psychographic, Behavioural), and Marketing Communication mix."
            ),
            AcademicResource(
                title = "FIN 201 Elements of Finance Approved Textbook Excerpts",
                description = "Chapters covering Nigerian financial system, capital markets, stock exchange operations, and time value of money calculations.",
                courseCode = "FIN 201",
                courseTitle = "Elements of Finance",
                level = Level.LEVEL_200.name,
                resourceType = ResourceType.TEXTBOOK.name,
                fileFormat = "PDF",
                fileSizeMb = 12.8,
                uploadDate = "14 Feb 2025",
                contributorName = "Departmental Library Committee",
                moderationStatus = ModerationStatus.APPROVED.name,
                downloadCount = 195,
                previewSummary = "Compound interest vs simple interest formulas, present value of annuities, role of the Central Bank of Nigeria (CBN) and Securities & Exchange Commission (SEC)."
            ),

            // 300 Level
            AcademicResource(
                title = "BUS 301 Organizational Behaviour Comprehensive Study Pack",
                description = "In-depth review of motivation theories (Maslow, Herzberg Two-Factor, Vroom's Expectancy), leadership styles, and organizational change.",
                courseCode = "BUS 301",
                courseTitle = "Organizational Behaviour",
                level = Level.LEVEL_300.name,
                resourceType = ResourceType.STUDY_GUIDE.name,
                fileFormat = "DOCX",
                fileSizeMb = 3.9,
                uploadDate = "12 Feb 2025",
                contributorName = "Chukwudi Obi",
                moderationStatus = ModerationStatus.APPROVED.name,
                downloadCount = 310,
                previewSummary = "Detailed comparative tables on transactional vs transformational leadership, conflict resolution models (Thomas-Kilmann), and Lewin's 3-stage organizational change model."
            ),
            AcademicResource(
                title = "FIN 301 Capital Budgeting & Valuation Past Questions",
                description = "Solved past exam computations: Net Present Value (NPV), Internal Rate of Return (IRR), Payback Period, and Weighted Average Cost of Capital (WACC).",
                courseCode = "FIN 301",
                courseTitle = "Financial Management",
                level = Level.LEVEL_300.name,
                resourceType = ResourceType.PAST_QUESTION.name,
                fileFormat = "PDF",
                fileSizeMb = 4.8,
                uploadDate = "20 Feb 2025",
                contributorName = "Ibrahim Bukar",
                moderationStatus = ModerationStatus.APPROVED.name,
                downloadCount = 520,
                previewSummary = "Step-by-step mathematical solutions to past UNIMAID questions on Mutually Exclusive project selection, sensitivity analysis, and dividend irrelevance vs relevance theories (Modigliani-Miller, Walter, Gordon)."
            ),
            AcademicResource(
                title = "BUS 305 Research Proposal Writing Guide & Templates",
                description = "Standard UNIMAID Faculty of Management Sciences formatting template for Chapter 1 to 3 proposals, APA 7th referencing guide, and questionnaire design sample.",
                courseCode = "BUS 305",
                courseTitle = "Research Methodology",
                level = Level.LEVEL_300.name,
                resourceType = ResourceType.OTHER.name,
                fileFormat = "DOCX",
                fileSizeMb = 2.4,
                uploadDate = "25 Feb 2025",
                contributorName = "Dr. M. S. Kyari",
                moderationStatus = ModerationStatus.APPROVED.name,
                downloadCount = 445,
                previewSummary = "Sample problem statements, research questions, research hypotheses, operationalization of variables table, and Taro Yamane sample size determination formula."
            ),

            // 400 Level
            AcademicResource(
                title = "BUS 401 Business Policy & Strategy I Master Revision Note",
                description = "The ultimate 400L final year strategy manual: Strategic intent, Vision/Mission formulation, PESTLE, SWOT, Porter's Five Forces, BCG Matrix, and GE-McKinsey Matrix.",
                courseCode = "BUS 401",
                courseTitle = "Business Policy & Strategy I",
                level = Level.LEVEL_400.name,
                resourceType = ResourceType.LECTURE_NOTE.name,
                fileFormat = "PDF",
                fileSizeMb = 7.5,
                uploadDate = "01 Mar 2025",
                contributorName = "Prof. M. B. Yunusa",
                moderationStatus = ModerationStatus.APPROVED.name,
                downloadCount = 680,
                previewSummary = "Complete framework summaries: Porter's generic strategies (Cost leadership, Differentiation, Focus), Value Chain Analysis, Core Competencies (Prahalad & Hamel), and balanced scorecard implementation."
            ),
            AcademicResource(
                title = "MGT 405 Operations Research Model Questions & Simplex Guides",
                description = "Step-by-step guides for solving Linear Programming using Graphical and Simplex methods, Vogel's Approximation Method (VAM) for transportation, and Critical Path Method (CPM/PERT).",
                courseCode = "MGT 405",
                courseTitle = "Operations Research",
                level = Level.LEVEL_400.name,
                resourceType = ResourceType.PAST_QUESTION.name,
                fileFormat = "PDF",
                fileSizeMb = 6.3,
                uploadDate = "03 Mar 2025",
                contributorName = "Musa Dahiru (Final Year Exco)",
                moderationStatus = ModerationStatus.APPROVED.name,
                downloadCount = 590,
                previewSummary = "Simplex tableau calculations, sensitivity analysis, dual problem formulation, assignment problem (Hungarian method), and network diagram float calculations."
            ),
            AcademicResource(
                title = "MGT 407 Feasibility Report & Business Plan Sample",
                description = "Approved model business plan and feasibility report for a modern agribusiness venture in Borno State, showcasing financial projections, cash flow, and market analysis.",
                courseCode = "MGT 407",
                courseTitle = "Small Business Management & Entrepreneurship",
                level = Level.LEVEL_400.name,
                resourceType = ResourceType.ASSIGNMENT.name,
                fileFormat = "DOCX",
                fileSizeMb = 3.1,
                uploadDate = "08 Mar 2025",
                contributorName = "Amina Sani",
                moderationStatus = ModerationStatus.APPROVED.name,
                downloadCount = 330,
                previewSummary = "Comprehensive financial models: 3-year income statement, balance sheet projection, break-even analysis, marketing strategy, SWOT matrix, and executive summary format."
            ),

            // Pending Submissions for EXCOS Moderation demonstration
            AcademicResource(
                title = "BUS 203 Business Communication Sample Technical Report",
                description = "Student contribution containing sample departmental report on cafeteria facilities and formal memorandum formats.",
                courseCode = "BUS 203",
                courseTitle = "Business Communication",
                level = Level.LEVEL_200.name,
                resourceType = ResourceType.STUDY_GUIDE.name,
                fileFormat = "PDF",
                fileSizeMb = 1.8,
                uploadDate = "Yesterday, 4:15 PM",
                contributorName = "Zainab Alkali",
                contributorMatric = "19/08/04/082",
                moderationStatus = ModerationStatus.PENDING.name,
                moderationNote = "Awaiting review for formatting standards and relevance.",
                previewSummary = "Sample committee report format, executive memo templates, and minutes of meeting sample."
            ),
            AcademicResource(
                title = "ACC 201 Special Accounts Lecture Handout 2024",
                description = "Partnership dissolution, joint venture accounts, and consignment accounts notes from semester lecture.",
                courseCode = "ACC 201",
                courseTitle = "Principles of Accounting II",
                level = Level.LEVEL_200.name,
                resourceType = ResourceType.LECTURE_NOTE.name,
                fileFormat = "PDF",
                fileSizeMb = 4.2,
                uploadDate = "Today, 9:30 AM",
                contributorName = "Sadiq Gana",
                contributorMatric = "20/08/04/115",
                moderationStatus = ModerationStatus.PENDING.name,
                moderationNote = "Uploaded by student. Ready for EXCOS Academic Secretary vetting.",
                previewSummary = "Garner vs Murray rule explanation, revaluation of assets on admission of new partner, realization account steps."
            )
        )
        resourceDao.insertAll(initialResources)

        // 3. Seed Suggestions & Complaints
        val initialSuggestions = listOf(
            SuggestionComplaint(
                ticketNumber = "UNM-BA-2025-001",
                subject = "Request for Weekend Tutorial Sessions for BUS 101 and ACC 101",
                category = SuggestionCategory.ACADEMIC_ISSUES.name,
                message = "Many 100 level students are struggling with the double entry ledger concepts in ACC 101 and the vast syllabus of BUS 101. Could the EXCOS organize free Saturday tutorials at the Departmental Lecture Theatre?",
                studentName = "Hauwa Mohammed",
                studentContact = "hauwa.m@unimaid.edu.ng",
                isAnonymous = false,
                status = SuggestionStatus.RESOLVED.name,
                excosResponse = "Approved! The Academic Committee has scheduled free Saturday tutorial sessions starting this Saturday, 9:00 AM at LT 2. 300L and 400L top-performing scholars have been assigned as peer tutors.",
                respondedBy = "Aisha Mohammed (Academic Secretary)",
                submissionDate = "10 Jan 2025",
                resolutionDate = "12 Jan 2025"
            ),
            SuggestionComplaint(
                ticketNumber = "UNM-BA-2025-002",
                subject = "Poor Ventilation and Lighting in Management Hall 3",
                category = SuggestionCategory.STUDENT_WELFARE.name,
                message = "The ceiling fans in Hall 3 are non-functional and three fluorescent bulbs are dead. With afternoon temperatures reaching 38 degrees, lectures are extremely uncomfortable for students.",
                studentName = "",
                studentContact = "",
                isAnonymous = true,
                status = SuggestionStatus.UNDER_REVIEW.name,
                excosResponse = "The Welfare Director inspected Hall 3 and submitted a formal requisition to the Faculty Maintenance Unit. Electrical repairs scheduled for Friday afternoon.",
                respondedBy = "Emmanuel Danladi (Welfare Director)",
                submissionDate = "18 Jan 2025",
                resolutionDate = ""
            ),
            SuggestionComplaint(
                ticketNumber = "UNM-BA-2025-003",
                subject = "Introduction of Digital Business & FinTech Seminar",
                category = SuggestionCategory.ACADEMIC_PROGRAMMES.name,
                message = "As Business Administration scholars, practical exposure to electronic banking, digital payments, and e-commerce platforms in Nigeria is crucial. Can we host an industry workshop during Management Week?",
                studentName = "Babagana Kalli",
                studentContact = "08034567890",
                isAnonymous = false,
                status = SuggestionStatus.PENDING.name,
                excosResponse = "",
                respondedBy = "",
                submissionDate = "28 Jan 2025",
                resolutionDate = ""
            ),
            SuggestionComplaint(
                ticketNumber = "UNM-BA-2025-004",
                subject = "Need for Central Digital PDF Archive for 300L & 400L Past Questions",
                category = SuggestionCategory.DEPARTMENTAL_IMPROVEMENT.name,
                message = "Past question booklets at commercial photocopy centres are often expensive and missing key pages. Having this portal with free verified downloads is the best initiative so far. Please add more 2023 questions.",
                studentName = "",
                studentContact = "",
                isAnonymous = true,
                status = SuggestionStatus.RESOLVED.name,
                excosResponse = "Thank you! We have scanned and uploaded all 2023 questions across all 4 levels onto this Academic Resource Platform.",
                respondedBy = "Comrade Ibrahim Bukar (President)",
                submissionDate = "02 Feb 2025",
                resolutionDate = "05 Feb 2025"
            )
        )
        for (item in initialSuggestions) {
            suggestionDao.insert(item)
        }

        // 4. Seed Announcements
        val initialAnnouncements = listOf(
            DepartmentAnnouncement(
                title = "2024/2025 First Semester Examination Timetable Released",
                content = "The Department of Business Administration examination schedule has been officially published by the Faculty Examination Officer. 100L to 400L students must bring their ID cards, exam clearance slips, and adhere strictly to dress code regulations.",
                date = "28 Feb 2025",
                author = "Head of Department (Prof. M. B. Yunusa)",
                priority = "EXAM"
            ),
            DepartmentAnnouncement(
                title = "Free Academic Resource Bank Portal Officially Launched",
                content = "Welcome to the Departmental Academic Resource Platform for UNIMAID Business Administration students! Access past questions, lecture notes, submit materials, and get instant study guidance from our AI Assistant.",
                date = "01 Feb 2025",
                author = "Departmental EXCOS",
                priority = "URGENT"
            ),
            DepartmentAnnouncement(
                title = "Continuous Assessment (CA) Submission Deadline",
                content = "All 200L and 300L students are reminded that term papers for BUS 201 and BUS 301 must be submitted to course representatives by 4:00 PM this Friday. No late submissions will be entertained.",
                date = "15 Feb 2025",
                author = "Academic Committee",
                priority = "NORMAL"
            )
        )
        departmentDao.insertAllAnnouncements(initialAnnouncements)

        // 5. Seed EXCOS Members Directory
        val initialExcos = listOf(
            ExcosMember(name = "Comrade Ibrahim Bukar", roleTitle = "President (Executive Council)", session = "2024/2025 Session", phone = "+234 802 123 4567", email = "president.busadmin@unimaid.edu.ng", matricNumber = "18/08/04/001"),
            ExcosMember(name = "Aisha Mohammed", roleTitle = "Academic Secretary", session = "2024/2025 Session", phone = "+234 803 234 5678", email = "academics.busadmin@unimaid.edu.ng", matricNumber = "19/08/04/012"),
            ExcosMember(name = "Emmanuel Danladi", roleTitle = "Welfare Director", session = "2024/2025 Session", phone = "+234 805 345 6789", email = "welfare.busadmin@unimaid.edu.ng", matricNumber = "19/08/04/035"),
            ExcosMember(name = "Khadija Umar", roleTitle = "Public Relations Officer (PRO)", session = "2024/2025 Session", phone = "+234 808 456 7890", email = "pro.busadmin@unimaid.edu.ng", matricNumber = "19/08/04/048"),
            ExcosMember(name = "Musa Dahiru", roleTitle = "General Secretary & Admin Lead", session = "2024/2025 Session", phone = "+234 809 567 8901", email = "gensec.busadmin@unimaid.edu.ng", matricNumber = "18/08/04/089")
        )
        departmentDao.insertAllExcosMembers(initialExcos)
    }
}
