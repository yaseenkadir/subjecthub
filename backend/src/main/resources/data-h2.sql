INSERT INTO users (user_id, username, password, email, admin)
VALUES(
    1,
    'UserOne',
    '$2a$10$nZ4rwkQfzd51jBmorrZDceaJq2BJ2LI9Ap58VuoHNeTBDPHCLxvba', /* password is "password" */
    'UserOne@email.com',
    FALSE
);

INSERT INTO users (user_id, username, password, email, admin)
VALUES(
    2,
    'UserTwo',
    '$2a$10$nZ4rwkQfzd51jBmorrZDceaJq2BJ2LI9Ap58VuoHNeTBDPHCLxvba', /* password is "password" */
    'UserTwo@email.com',
    FALSE
);

INSERT INTO users (user_id, username, password, email, admin)
VALUES(
    3,
    'admin',
    '$2a$10$nZ4rwkQfzd51jBmorrZDceaJq2BJ2LI9Ap58VuoHNeTBDPHCLxvba', /* password is "password" */
    'admin@example.com',
    TRUE
);

/*
  _   _   _____   ____
 | | | | |_   _| / ___|
 | | | |   | |   \___ \
 | |_| |   | |    ___) |
  \___/    |_|   |____/

 */


INSERT INTO universities (university_id, name, abbreviation, image_url)
VALUES (1, 'University of Technology Sydney', 'UTS',
        'https://s3-ap-southeast-2.amazonaws.com/wordpress.multisite.prod.uploads/wp-content/uploads/sites/4/2017/07/10125839/UTSLogo_Horizontal_BLK.png');

/* Initialise faculties */
INSERT INTO faculties (faculty_id, name, code, university_id)
VALUES (1, 'Faculty of Engineering and Information Technology', 'FEIT', 1);

INSERT INTO faculties (faculty_id, name, code, university_id)
VALUES (2, 'Faculty of Health', 'FOH', 1);

INSERT INTO faculties (faculty_id, name, code, university_id)
VALUES (3, 'Faculty of Law', 'FOL', 1);

/* Initialise subjects */
/* SOFTWARE ENGINEERING PRACTICE */
INSERT INTO subjects (subject_id, code, name, credit_points, description, min_requirements,
                      undergrad, postgrad, autumn, spring, summer, faculty_id)
VALUES (
    1,
    '48440',
    'Software Engineering Practice',
    6,
    'This subject introduces students to the fundamentals of contemporary software engineering. An overview of the agile and non-agile software engineering principles, methods, tools and techniques is presented. Current trends and challenges in the practice of software engineering are explored. Students apply contemporary agile requirements analysis, planning, architecture, design, implementation and testing practices to software engineering project work in small teams.',
    'In order to pass the subject, you must\nearn an overall total of 50 marks or more for the subject; and \nhave submitted two project assessment tasks (Assessment Items 1-2) containing all the required items; and\nhave submitted an Individual Contribution Logbook with the project assessment tasks (Assessment Items 1-2) containing all the required items.\n\nNote: The Individual Contribution Logbook is mandatory for students to receive any individual project marks. If a student does not submit this assessment item, then he/she will receive zero for the project assessment tasks (Assessment Items 1-2).',
    TRUE,
    FALSE,
    FALSE,
    TRUE,
    FALSE,
    1
);

INSERT INTO assessments (assessment_id, name, description, weighting, group_work, length, type, subject_id)
VALUES (
    1,
    'Assessment task 1',
    'Project - Agile Planning, Analysis and Architecture',
    30,
    TRUE,
    'There is no word limit.',
    3,
    1
);

INSERT INTO assessments (assessment_id, name, description, weighting, group_work, length, type, subject_id)
VALUES (
    2,
    'Assessment task 2',
    'Project - Agile Design, Implementation and Testing',
    60,
    TRUE,
    'There is no word limit.',
    3,
    1
);

INSERT INTO assessments (assessment_id, name, description, weighting, group_work, length, type, subject_id)
VALUES (
    3,
    'Assessment task 3',
    'Short Quiz',
    10,
    FALSE ,
    'There is no word limit.',
    1,
    1
);

INSERT INTO comments (comment_id, user_id, subject_id, post, post_time)
VALUES (
    1,
    1,
    1,
    'I like this subject!',
    TIMESTAMP '2017-09-19 20:43:37'
);

INSERT INTO comments (comment_id, user_id, subject_id, post, post_time)
VALUES (
    2,
    2,
    1,
    'I learnt alot o.O',
    TIMESTAMP '2017-09-19 20:47:28'
);

/* Applications Programming */
INSERT INTO subjects (subject_id, code, name, credit_points, description, min_requirements,
                      undergrad, postgrad, autumn, spring, summer, faculty_id)
VALUES (
    2,
    '48024',
    'Applications Programming',
    6,
    'This subject teaches students how to design, develop and evaluate software systems to meet predefined quality characteristics of functionality (suitability) and usability (understandability, learnability, operability, compliance). Software solutions are implemented using Java. Concepts, theories and technologies underlying the methods and techniques are introduced and explained as required. Students apply all that they have learned to develop and implement the architecture of a business system.',
    'A student must have a total mark for the subject >= 50% and an exam mark >= 50% to pass the subject.\nIf you have a mark of >= 50% for the subject, but have failed the exam, you will be awarded a Fail (X) grade for the subject.Under the University''s rules, no supplementary exam will be given.',
    TRUE,
    FALSE,
    TRUE,
    TRUE,
    FALSE,
    1
);

/* .NET Application Development */
INSERT INTO subjects (subject_id, code, name, credit_points, description, min_requirements,
                      undergrad, postgrad, autumn, spring, summer, faculty_id)
VALUES (
    3,
    '32998',
    '.NET Application Development',
    6,
    'This subject introduces C#, Visual Studio and the .NET development environment. The emphasis is on examining the .NET framework and the practicalities of developing software in this setting using the C# language.',
    'In order to pass the subject you must attain all of the following minimum requirements /n 1.Minimum of 50% in the final exam /n 2.Minimum of 50% in the overall mark /n If you fail to attain requirement 1 but attain requirement 2 then your final grade will be set to X (fail). Otherwise, your final grade will be determined by your overall mark.',
    FALSE,
    TRUE,
    FALSE,
    TRUE,
    FALSE,
    1
);

/* .NET Enterprise Development */
INSERT INTO subjects (subject_id, code, name, credit_points, description, min_requirements,
                      undergrad, postgrad, autumn, spring, summer, faculty_id)
VALUES (
    4,
    '32013',
    '.NET Enterprise Development',
    6,
    'In this subject, students design, implement and then test web-based applications suitable for deployment in a modern enterprise. Students work in groups and gain experiences in the tools and practices used by small software development teams working in industry and embedded in larger organisations. The subject spans technical knowledge as well as the professional practices required by working software developers./n The subject is primarily focused on developing web-based, database-driven enterprise applications using the .NET framework. Technologies covered include LINQ, ASP.NET Web Forms, ASP.NET MVC, ADO.NET, Entity Framework and Identity. Practices include test-driven development, architectural layering and agile development. The language used is C#.',
    'Students must achieve a mark of 50% overall to pass the subject. In addition, students must pass the final examination to pass the subject. That is, a student who obtains a result in the final exam that is less than 50%, will receive their overall mark with a failing grade (''X'')./nAssignments in this subject are done in groups. The contribution of each individual in a group will be monitored throughout the semester. Should the need arise, the mark allocated for individuals within groups can reflect an agreed level of contribution by members of the group. If no agreement can be reached or if the mark allocation does not properly reflect individual contribution or understanding, the tutor or subject coordinator may vary the individual marks to ensure an accurate measure of student learning./nA late penalty may be applied to submitted work unless prior arrangements have been made with the subject coordinator. Details of the late penalties will be included with the descriptions of the assessment items./nAssignments are due in labs on the date specified and should be submitted (and/or demonstrated) to your tutor, who will give you a receipt.',
    FALSE,
    TRUE,
    TRUE,
    FALSE,
    FALSE,
    1
);

/* Fundamentals of Interaction Design */
INSERT INTO subjects (subject_id, code, name, credit_points, description, min_requirements,
                      undergrad, postgrad, autumn, spring, summer, faculty_id)
VALUES (
    5,
    '31260',
    'Fundamentals of Interaction Design',
    6,
    'This subject focuses on the design, evaluation and implementation of interactive computing systems for human use within actual situations. Students gain an understanding of human–computer interaction (HCI) and interaction design principles, including the main concepts, tools and techniques available to build human-centred systems. The subject considers the effects on use of the different metaphors for human activity that designers use in their systems and how human-centred design and evaluation methods can improve the usability of computer systems.',
    'A student must have a total mark for the subject >= 50%',
    TRUE,
    FALSE,
    TRUE,
    TRUE,
    FALSE,
    1
);

/* Networking Essentials */
INSERT INTO subjects (subject_id, code, name, credit_points, description, min_requirements,
                      undergrad, postgrad, autumn, spring, summer, faculty_id)
VALUES (
    6,
    '31270',
    'Networking Essentials',
    6,
    'Computer networks are now business critical in all modern organisations and business enterprises. They are important in everyday life. This subject introduces students to the fundamental issues in modern data communications and computer networks. This is essential knowledge for all users of IT, IT professionals and those who wish to specialise in computer networking. Students learn about the layered networking model and are introduced to networking devices and protocols. They learn how these are used in computer networks and in net-based application programs. The primary focus of this subject is Local Area Networks (LAN). Student practical work includes designing and building simple peer-to-peer networks and LANs that are connected to the Internet. The core set of protocols employed on the global Internet, TCP/IP, is studied.',
    'Students must satisfactorily complete a minimum of eight (8) chapter tests. Students must also complete the online final exam, written exam, and skills based assessment. Failure to meet these requirements will result in an X grade for the subject.\nStudents must obtain a minimum of 50% from all marks available to pass this subject.',
    TRUE,
    FALSE,
    TRUE,
    TRUE,
    FALSE,
    1
);

/* Communications for IT Professionals */
INSERT INTO subjects (subject_id, code, name, credit_points, description, min_requirements,
                      undergrad, postgrad, autumn, spring, summer, faculty_id)
VALUES (
    7,
    '31265',
    'Communications for IT Professionals',
    6,
    'This subject focuses on developing the academic written and spoken language skills required for undergraduate study in information technology and for the professional workplace. Students take a critical and analytical approach to understanding and producing written and spoken texts appropriate for IT professionals in the Australian context. Students consider the ethical and social issues that IT raises within both society and the IT industry. Accordingly, students undertake a range of listening, speaking, reading and writing activities individually and in groups to maximise the development of their written and spoken communication and academic literacy.',
    'A student must achieve a mark of 50% or greater in Assessment Task 4.\nThey must also achieve an overall mark of 50% or greater in the subject.',
    TRUE,
    FALSE,
    TRUE,
    TRUE,
    FALSE,
    1
);

/* Programming Fundamentals */
INSERT INTO subjects (subject_id, code, name, credit_points, description, min_requirements,
                      undergrad, postgrad, autumn, spring, summer, faculty_id)
VALUES (
    8,
    '48023',
    'Programming Fundamentals',
    6,
    'This subject provides basic skills in Java programming and software design, with no assumed knowledge of programming. It covers the topics of object-oriented (OO) programming concepts, data flow, control flow, arrays, and the basics of sorting and searching algorithms. The subject teaches and illustrates a design process using a set of design notations and design rules, and shows how to develop a correct, readable and reusable solution from a problem specification.',
    'To pass, students must successfully complete all pass/fail lab tests, under exam conditions. If a student does not successfully complete all of the pass/fail lab tests, then all other assessment items will be ignored, even if those assessment items have been submitted and marked.',
    TRUE,
    FALSE,
    TRUE,
    TRUE,
    FALSE,
    1
);

/* Data Structures and Algorithms */
INSERT INTO subjects (subject_id, code, name, credit_points, description, min_requirements,
                      undergrad, postgrad, autumn, spring, summer, faculty_id)
VALUES (
    9,
    '31251',
    'Data Structures and Algorithms',
    6,
    'This subject teaches students how to design, develop and evaluate data structures and algorithms to meet predefined quality characteristics of functionality (suitability) and usability (understandability, learnability, operability, compliance). Software solutions are implemented using C++. Concepts, theories and technologies underlying the methods and techniques are introduced and explained as required.',
    'In order to pass the subject you must attain all of the following minimum requirements\n1. Minimum of 16 out of 40 (40%) in the final exam.\n2. Minimum of 50 out of 100 (50%) in the overall mark.\nIf you fail to achieve the first requirement but meet the second then your final grade will be set to X (fail)',
    TRUE,
    FALSE,
    TRUE,
    FALSE,
    FALSE,
    1
);

/* Introduction to Health Care Systems */
INSERT INTO subjects (subject_id, code, name, credit_points, description, min_requirements,
                      undergrad, postgrad, autumn, spring, summer, faculty_id)
VALUES (
    10,
    '92566',
    'Introduction to Health Care Systems',
    6,
    'This subject introduces students to the major structural and functional components of healthcare systems, both in Australia and overseas. Key arrangements, stakeholders, processes and performance and trends in health system reform are actively explored. Students develop the ability to critically evaluate health systems, skills to identify and prioritise health system needs and identify and plan strategies that address major challenges. This subject provides a foundation for students to influence health system improvement, from an informed position, in health organisations.',
    'A student must achieve an overall mark of 50% or greater in the subject.',
    TRUE,
    FALSE,
    TRUE,
    TRUE,
    FALSE,
    2
);

/* Health Systems and Change*/
INSERT INTO subjects (subject_id, code, name, credit_points, description, min_requirements,
                      undergrad, postgrad, autumn, spring, summer, faculty_id)
VALUES (
    11,
    '92297',
    'Health Systems and Change',
    6,
    'Health systems worldwide are facing increasing pressure to improve their efficiency and effectiveness while delivering safe, high quality and patient-centred care. Most countries accept that existing models of health care delivery are not sustainable for future decades, resulting in modern health services engaging in large system changes. This subject assists students to develop knowledge and skills in understanding and adapting health systems to achieve efficiency and effectiveness. The subject initially examines a range of change theories that can be applied to the healthcare environment as well as relevant leadership theories within the context of change. Finally the subject focuses on national health reform and a range of service improvement approaches and tools such as lean thinking, clinical process mapping and patient flow analysis. A number of recent initiatives are presented that explain how to implement change at national, corporate, statewide and health care organisation levels.',
    'A student must achieve an overall mark of 50% or greater in the subject.',
    FALSE,
    TRUE,
    TRUE,
    TRUE,
    FALSE,
    2
);

/* Fundamentals of Mental Health Nursing */
INSERT INTO subjects (subject_id, code, name, credit_points, description, min_requirements,
                      undergrad, postgrad, autumn, spring, summer, faculty_id)
VALUES (
    12,
    '92436',
    'Fundamentals of Mental Health Nursing',
    6,
    'In this subject students learn concepts about mental health presentations and care generally and within the specialised context of mental health care settings, both acute and community-based. Students consider recovery as a context for mental health practice as they begin to explore different mental health issues and people who have experience of mental health challenges. As this is a foundational mental health clinical subject, students explore a range of skills in assessment such as mental state examination, risk assessment and attending to family and carers'' needs. Students also begin to develop the knowledge, attitudes and skills required for mental health nursing practice, therapeutic communication and reflective practice. This subject explores personal and professional resilience to better enable students to work effectively with complex presentations in mental health and generalist care settings.',
    'A student must achieve an overall mark of 50% or greater in the subject.',
    TRUE,
    FALSE,
    TRUE,
    TRUE,
    FALSE,
    2
);

/* Foundations of Law */
INSERT INTO subjects (subject_id, code, name, credit_points, description, min_requirements,
                      undergrad, postgrad, autumn, spring, summer, faculty_id)
VALUES (
    13,
    '70102',
    'Foundations of Law',
    8,
    'This subject introduces students to the foundations of Australian law: its origins, institutions, cultural contexts and theoretical foundations. We examine the role of the legal doctrine of terra nullius in the British colonisation of Australian peoples and places, and its connection to the ‘reception’ of English law into Australia. We explore the key ideas that underpin Australia’s legal institutions including democracy, sovereignty, the rule of law and the separation of powers. We combine our understanding of the historical development of Australian law and legal institutions with a critical analysis of their conceptual underpinnings using critical legal theory. This theory scrutinises the assumptions, logic, language and practice of law. Using a critical analysis of law, for example, from the perspective of the colonised rather than the colonising, allows students to ask different questions about not only the abstract principles of law, but also the lived experience of law. The subject also introduces students to the nature of legal thinking and legal practice including research methods, and the techniques and principles involved in reading and interpreting case law and statute. Legal reasoning is one of the most important topics in the subject and students are given the opportunity to explore both traditional methods of legal reasoning and critical lenses through which to analyse and evaluate a legal question. For instance, using feminist legal theories and critical race theory we can arrive at different answers to the same legal question. The critical legal thinking and legal research skills that students develop in this subject are essential to the successful completion of later subjects in the law degree program.',
    'Class attendance is a subject requirement. Your seminar leader will take a roll during class. Although we encourage you to attend every seminar we understand that things happen and you can afford to miss up to four seminars without penalty provided that you send an email to your seminar leader to explain your absence. However, if you miss more than four classes you will not be permitted to receive a mark for the subject. In exceptional circumstances of illness and misadventure, you may be offered an opportunity to submit additional work for each class missed and permitted to receive a mark for the subject. Please note that work commitments and personal travel do not constitute an acceptable reason for absence from seminars. Please do not plan holidays that coincide with UTS teaching weeks.',
    TRUE,
    FALSE,
    TRUE,
    TRUE,
    FALSE,
    3
);

/* Law and Justice Studies*/
INSERT INTO subjects (subject_id, code, name, credit_points, description, min_requirements,
                      undergrad, postgrad, autumn, spring, summer, faculty_id)
VALUES (
    14,
    '76069',
    'Law and Justice Studies',
    6,
    'This subject introduces students to the dynamics of law reform and community engagement. Students experience the practice of community-based law through a placement with a social justice agency. In their placement, students gain experience and understanding of the ethics of practice, the legal needs addressed by the agency, and access to justice issues for disadvantaged individuals and communities. Depending on the requirements of participating organisations students may review and assess the impact of legislation and legislative reforms on vulnerable populations, undertake research and write reports on specific legal issues related to the practice of the agency, interview clients under the supervision of practicing lawyers, develop creative legal education materials and prepare case summaries.Students develop legal skills such as self-management, a practical understanding of the rules and practice of professional legal responsibility, the practice of ethical behaviour in a social justice context, and collaboration with colleagues, community groups and government agencies. Students develop communication and research skills through undertaking research on complex legal issues to generate appropriate theoretical and practical legal responses.',
    'This subject is taught at Masters level. All students will need to achieve the advanced subject learning outcomes of self-management, critical reflection and professional responsibility through their participation in the internship and pre-departure preparations, their critical reflection and their debriefing presentation.',
    TRUE,
    FALSE,
    TRUE,
    TRUE,
    FALSE,
    3
);

/* Introduction to Taxation Law */
INSERT INTO subjects (subject_id, code, name, credit_points, description, min_requirements,
                      undergrad, postgrad, autumn, spring, summer, faculty_id)
VALUES (
    15,
    '77938',
    'Introduction to Taxation Law',
    6,
    'This subject acquaints students with Australian taxation law in a practical business environment. The focus of the syllabus is on the application of tax law concepts in a professional accounting setting. It aims to develop students'' conceptual and analytical skills and an appreciation of the Australian tax system. It provides a general analysis of the current tax system and consideration of the many changes it is presently undergoing with an emphasis on the implications for the commercial world. The subject looks at the Income Tax Assessment Act, the Tax Law Reform Project and the New Tax System. Particular concepts to be considered include: taxable income, income, deductions, capital gains tax, trusts, partnerships, companies and shareholders, tax accounting, tax planning and anti-avoidance provisions, fringe benefits tax and goods and services tax.',
    'To obtain a pass in this subject, students must obtain a total of at least 50 marks from all components of the assessment.',
    FALSE,
    TRUE,
    TRUE,
    TRUE,
    FALSE,
    3
);

/* Security Fundamentals */
INSERT INTO subjects (subject_id, code, name, credit_points, description, min_requirements,
                      undergrad, postgrad, autumn, spring, summer, faculty_id)
VALUES (
    20,
    '41900',
    'Security Fundamentals',
    6,
    'Security is a major issue for enterprises, with breaches leaving them vulnerable to legal sanctions, financial loss or reduced customer confidence. This subject introduces students to modern security issues and technologies by considering various aspects, from security principles and policies, to network and system security, as well as intrusion detection and cyber security. Students learn and apply programming skills to implement secure communications while demonstrating professional practice in a group project.',
    'To pass this subject, you must achieve an overall mark of 50% or greater. In general, supplementary assessments are not offered in this subject. Students are referred to the University Policy and Procedures for the Assessment of Coursework Subjects for further details.',
    TRUE,
    FALSE,
    TRUE,
    TRUE,
    FALSE,
    1
);

/* Cyber Security */
INSERT INTO subjects (subject_id, code, name, credit_points, description, min_requirements,
                      undergrad, postgrad, autumn, spring, summer, faculty_id)
VALUES (
    21,
    '48730',
    'Cyber Security',
    6,
    'Network security is a major issue for enterprises, with breaches of security possibly being punished by legal sanctions, financial loss, or loss of customer confidence. This subject consolidates the student''s understanding of security by considering security principles from both a people management and a technical perspective, exploring security technologies commonly used in industry. Topics covered include network security theories, such as RSA algorithm, security appliances such as firewalls, and Intrusion Detection Systems; security services such as confidentiality, integrity and authentication; and technologies such as IPSec, SSL, etc. Students doing this subject are well placed to contribute to the security solution of a modern organisation using industry-based tools and solutions.',
    'A passing grade will be awarded if an aggregate mark of 50% or better is achieved overall. To pass the subject, students must achieve at least 45% in the written exam. Under the University’s assessment policy no supplementary examination is required in this subject and none shall be offered. ',
    TRUE,
    FALSE,
    TRUE,
    TRUE,
    FALSE,
    1
);

/* Digital Forensics */
INSERT INTO subjects (subject_id, code, name, credit_points, description, min_requirements,
                      undergrad, postgrad, autumn, spring, summer, faculty_id)
VALUES (
    22,
    '48436',
    'Digital Forensics',
    6,
    'This is a practice-based subject, using material based on the textbook. Learning is laboratory-based. Students assess if a crime has been committed, acquire digital evidence, analyse the evidence and prepare forensic reports. \nThe emphasis is on digital forensics applications, in particular: \n- forensic analysis of a digital storage device where evidence of visits to web sites is recovered to support or oppose a hypothesis before a criminal court \n- eDiscovery (a form of discovery related to civil litigation) where students acquire evidence of contact with a third party using email or social media \n- intrusion investigation into the nature and extent of an unauthorised network intrusion. Students look for evidence of malware being installed on the device that may use the network to exfiltrate data to an unauthorised person.',
    'A passing grade will be awarded if an aggregate mark of 50% or better is achieved overall.',
    TRUE,
    FALSE,
    TRUE,
    TRUE,
    FALSE,
    1
);

/* Network Servers */
INSERT INTO subjects (subject_id, code, name, credit_points, description, min_requirements,
                      undergrad, postgrad, autumn, spring, summer, faculty_id)
VALUES (
    23,
    '31388',
    'Network Servers',
    6,
    'Through this subject students gain an understanding of the design principles and implementation issues for the deployment of network servers based on Windows and Linux operating systems. Techniques and skills for system administration are developed through a comprehensive sequence of laboratory activities in combination with mini-lectures and design tasks.',
    'Students must have a total mark of at least 50% to be eligible for a pass of this subject AND must pass the final skills test. \nStudents who do not meet these minimum requirements but achieve an overall mark of 50% or greater will fail the subject and receive their overall mark with an "X" (fail) grade.',
    TRUE,
    FALSE,
    TRUE,
    TRUE,
    FALSE,
    1
);

/* Applying Network Security */
INSERT INTO subjects (subject_id, code, name, credit_points, description, min_requirements,
                      undergrad, postgrad, autumn, spring, summer, faculty_id)
VALUES (
    24,
    '41890',
    'Applying Network Security',
    6,
    'In the network security subject students learn about the theory underlying computer-security. This subject applies this theory to practice by using material based on the Cisco Network Security course. The emphasis is on network security appliances and networking infrastructure such as firewalls, access control, secure network design and Virtual Private Networks.'||chr(10)||'Students work collaboratively throughout the session. From week 1 students form groups of three and all lab work is a shared experience. All additional activities, such as researching concepts, exploring networking challenges and building the required networks, are achieved by students working cooperatively. \nGroups are also encouraged to share their findings with other groups, and while most assessments are individual, this shared knowledge contributes positively to the whole learning experience.',
    'A passing grade will be awarded if an aggregate mark of 50% or better is achieved overall. ',
    TRUE,
    FALSE,
    TRUE,
    TRUE,
    FALSE,
    1
);

/* Probability and Random Variables */
INSERT INTO subjects (subject_id, code, name, credit_points, description, min_requirements,
                      undergrad, postgrad, autumn, spring, summer, faculty_id)
VALUES (
    25,
    '37161',
    'Probability and Random Variables',
    6,
    'When modelling real-world problems we need to deal with uncertainty, and probability provides an effective way to quantify and model uncertainty. This subject introduces concepts in probability such as dependent and independent events as well as conditional probability. The idea of modelling random events with distributions is introduced, including probability calculation, expectation, variance, generating functions, and order statistics for independent events. The subject concludes by considering discrete Markov chains.',
    'In order to pass this subject, a student must achieve a final result of 50% or more. Any assessment task worth 40% or more requires the student to gain at least 40% of the mark for that task. If 40% is not reached, an X grade fail may be awarded for the subject, irrespective of an overall mark greater than 50.',
    TRUE,
    FALSE,
    TRUE,
    TRUE,
    FALSE,
    1
);

/*
  _   _    ___   __        __
 | | | |  / _ \  \ \      / /
 | | | | | | | |  \ \ /\ / /
 | |_| | | |_| |   \ V  V /
  \___/   \___/     \_/\_/

 */
INSERT INTO universities (university_id, name, abbreviation, image_url)
VALUES (2, 'University of Wollongong', 'UOW',
        'https://www.uow.edu.au/content/groups/public/@web/documents/siteelement/uow171491.png');

INSERT INTO faculties (faculty_id, name, code, university_id)
VALUES (4, 'Business', NULL, 2);

INSERT INTO faculties (faculty_id, name, code, university_id)
VALUES (5, 'Engineering and Information Sciences', NULL, 2);

/* Subjects */
/* Programming Fundamentals */
INSERT INTO subjects (code, name, credit_points, description, undergrad, postgrad, autumn, spring,
                      summer, faculty_id)
VALUES (
    'CSIT111',
    'Programming Fundamentals',
    6,
    'The broad aim of this subject is to develop in students an understanding of the fundamental principles of programming. The subject focusses on the object oriented view of problem analysis and solving. It enables students to develop skills in the design and implementation of well structured programs in a range of domains.',
    TRUE,
    FALSE,
    TRUE,
    TRUE,
    FALSE,
    5
);

/* Introduction to Web Technology */
INSERT INTO subjects (code, name, credit_points, description, undergrad, postgrad, autumn, spring,
                      summer, faculty_id)
VALUES (
    'CSIT128',
    'Introduction to Web Technology',
    6,
    'This subject introduces students to fundamental web technologies that underlie the World Wide Web and its commercial applications. Topics include an overview of internet communications, an introduction to the web-browser/web-server client-server systems, HTML5/CSS/XHTML/XML markup languages, web forms and client side scripting. Students will build working web-sites with dynamic content. The subject explains the differences between client-side and server-side Web development, and demonstrates how to build simple applications using scripting and other tools. The subject also covers current Web “standards” and future W3C recommendations.',
    TRUE,
    FALSE,
    FALSE,
    TRUE,
    FALSE,
    5
);

/* Data Management & Security */
INSERT INTO subjects (code, name, credit_points, description, undergrad, postgrad, autumn, spring,
                      summer, faculty_id)
VALUES (
    'CSIT115',
    'Data Management & Security',
    6,
    'The subject investigates three major areas of modern data management systems: data modelling, data processing, and data security. The goal of the subject is to learn the fundamental concepts in data management including conceptual modelling, the relational data model, processing of relational data with Structured Query Language (SQL), enforcing the concepts of data confidentiality, integrity, and availability data management systems. The subject develops the skills in the design, implementation, processing, and security of data management systems. The subject covers the following topics in data security: discretionary access control, user management, enforcing data security and integrity. The subject also explains the important ethical issues associated with responsible disclosure, responsibility, liability, security weaknesses, and privacy in data management systems.',
    TRUE,
    FALSE,
    TRUE,
    FALSE,
    FALSE,
    5
);

/* Problem Solving */
INSERT INTO subjects (code, name, credit_points, description, undergrad, postgrad, autumn, spring,
                      summer, faculty_id)
VALUES (
    'CSIT113',
    'Problem Solving',
    6,
    'This subject introduces the analysis of problems and the strategies used to manage them, primarily in the context of computing. Problem classification is introduced, as are formal and informal approaches to problem solving. The importance of method and method classification for problem solving strategies is motivated, and the need to compare and analyse strategies is justified. Introductory tools for the analysis of strategies are covered. Appropriate representations for problem solving are explored.',
    TRUE,
    FALSE,
    TRUE,
    FALSE,
    FALSE,
    5
);

/* Algorithms and Data Structures */
INSERT INTO subjects (code, name, credit_points, description, undergrad, postgrad, autumn, spring,
                      summer, faculty_id)
VALUES (
    'CSCI203',
    'Algorithms and Data Structures',
    6,
    'Approaches to analysing algorithm complexity, introduced in first year subjects, will be reviewed. The use of abstract data types as a design technique, and their implementation in solutions to problems, will form a large part of the subject. The concept of efficient code and ways to measure efficiency (both empirically, by timings, and theoretically) will be studied.',
    TRUE,
    FALSE,
    FALSE,
    TRUE,
    FALSE,
    5
);

/* Human Computer Interaction */
INSERT INTO subjects (code, name, credit_points, description, undergrad, postgrad, autumn, spring,
                      summer, faculty_id)
VALUES (
    'CSIT226',
    'Human Computer Interaction',
    6,
    'The subject provides students with an understanding of Human Computer Interaction (HCI) principles and practices, and how to apply them in the context of developing usable interactive computer applications and systems. The subject also emphasises the importance of taking into account contextual, organisational, and social factors in the design of computer systems. Students will be taken through the analysis, design, development, and evaluation of user interfaces. They will acquire hands-on design skills through an interaction design project. The subject will cover topics including user-centred design, the development process, prototyping, usability testing, measuring and evaluating the user experience and accessibility.',
    TRUE,
    FALSE,
    FALSE,
    TRUE,
    FALSE,
    5
);

/* Principles of Responsible Commerce */
INSERT INTO subjects (code, name, credit_points, description, undergrad, postgrad, autumn, spring,
                      summer, faculty_id)
VALUES (
    'COMM101',
    'Principles of Responsible Commerce',
    6,
    'The subject provides students with a conceptual tool kit for understanding and practising responsible and ethical Commerce. The topics covered will include the origins of contemporary systems of commerce, ethical and social responsibility in commerce and developments in ethical and responsible commerce. Areas addressed include the environment, globalization, technology, anti-corruption, labour and human rights. Students will examine these issues from a variety of theoretical and practical perspectives and apply them to contemporary commercial contexts.',
    TRUE,
    FALSE,
    TRUE,
    TRUE,
    FALSE,
    4
);

/* Business Oriented Information Systems */
INSERT INTO subjects (code, name, credit_points, description, undergrad, postgrad, autumn, spring,
                      summer, faculty_id)
VALUES (
    'COMM113',
    'Business Oriented Information Systems',
    6,
    'Information systems (IS) form an integral part of modern organisations and are used to support all aspects of an organisation’s daily functions and activities. This subject introduces the fundamental information system concepts that facilitate business processes. It explores how organisations use information, IS and their respective applications to increase profitability, gain market share, improve customer service and manage daily operations whilst understanding the social implications of their decisions. Students will learn about the role of IS in the modern organisation and how IS supports all of the functional areas of an organisation – Accounting, Finance, Marketing, Human Resources and Production/Operations Management.',
    TRUE,
    FALSE,
    FALSE,
    TRUE,
    FALSE,
    4
);

/* Marketing Principles */
INSERT INTO subjects (code, name, credit_points, description, undergrad, postgrad, autumn, spring,
                      summer, faculty_id)
VALUES (
    'MARK101',
    'Marketing Principles',
    6,
    'Marketing is a set of activities and processes for creating, communicating and delivering offerings and facilitating satisfying exchange relationships in a way that delivers value for consumers and society. Organisations need to know how to define and segment a market and how to position themselves strongly by identifying marketing opportunities and problems, and developing products, services, experiences and ideas for chosen target markets more effectively than their competitors. Marketing is essential for all organisations including manufacturers, wholesalers, retailers, professional services firms including lawyers, accountants and architects, and non-profit institutions including charities and museums. The subject examines the fundamental concepts underpinning the marketing process and theories relevant to the study and practice of marketing. It serves as a foundation for further studies in business by developing an overview of where marketing fits within organisations and what framework marketing provides for enhancing and enabling the conduct of a business.',
    TRUE,
    FALSE,
    TRUE,
    TRUE,
    FALSE,
    4
);

/* Accounting Fundamentals in Society */
INSERT INTO subjects (code, name, credit_points, description, undergrad, postgrad, autumn, spring,
                      summer, faculty_id)
VALUES (
    'ACCY111',
    'Accounting Fundamentals in Society',
    6,
    'This subject introduces the role of accounting information in society including its social and ethical aspects relating to both the individual and the organisation. The subject introduces basic accounting language, concepts and techniques to identify, classify, process, record and present accounting and financial information. The subject also considers accounting information that can be used for making decisions about past and future economic events in a variety of business and social settings.',
    TRUE,
    FALSE,
    TRUE,
    TRUE,
    FALSE,
    4
);

/* Macroeconomic Essentials for Business */
INSERT INTO subjects (code, name, credit_points, description, undergrad, postgrad, autumn, spring,
                      summer, faculty_id)
VALUES (
    'ECON101',
    'Macroeconomic Essentials for Business',
    6,
    'This subject analyses relevant macroeconomic concepts and principles in an integrated macroeconomic environment. Simple macroeconomic models will be developed to characterise the interdependencies of the more important components parts of a macro economy. This will allow students to analyse some real world problems and to start identifying and formulating appropriate macroeconomic policies.',
    TRUE,
    FALSE,
    TRUE,
    TRUE,
    FALSE,
    4
);
