CREATE TABLE universities (
    university_id BIGINT IDENTITY PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    abbreviation  VARCHAR(5)   NOT NULL
);

CREATE TABLE faculties (
    faculty_id    BIGINT IDENTITY PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    code          VARCHAR(5)   DEFAULT NULL,
    university_id BIGINT       NOT NULL,
    FOREIGN KEY (university_id) REFERENCES universities (university_id)
);

CREATE TABLE subjects (
    subject_id       BIGINT IDENTITY PRIMARY KEY,
    code             VARCHAR(10)    NOT NULL,
    name             VARCHAR(100)   NOT NULL,
    credit_points    INT            NOT NULL,
    description      VARCHAR(10000) NOT NULL,
    min_requirements VARCHAR(10000) DEFAULT NULL,
    undergrad        BOOLEAN        NOT NULL,
    postgrad         BOOLEAN        NOT NULL,
    autumn           BOOLEAN        NOT NULL,
    spring           BOOLEAN        NOT NULL,
    summer           BOOLEAN        NOT NULL,
    rating           FLOAT  DEFAULT 0.0,
    num_ratings      INT    DEFAULT 0,
    faculty_id       BIGINT         NOT NULL,
    FOREIGN KEY (faculty_id) REFERENCES faculties (faculty_id)
);

CREATE TABLE assessments (
    assessment_id BIGINT IDENTITY PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    description   VARCHAR(100) NOT NULL,
    weighting     INT          NOT NULL,
    group_work    BOOLEAN      NOT NULL,
    length        VARCHAR(100) NOT NULL,
    type          INT          NOT NULL,
    subject_id    BIGINT       NOT NULL,
    FOREIGN KEY (subject_id) REFERENCES subjects (subject_id)
);

/*
  _   _   _____   ____
 | | | | |_   _| / ___|
 | | | |   | |   \___ \
 | |_| |   | |    ___) |
  \___/    |_|   |____/

 */


INSERT INTO universities (university_id, name, abbreviation)
VALUES (1, 'University of Technology Sydney', 'UTS');

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


/*
  _   _    ___   __        __
 | | | |  / _ \  \ \      / /
 | | | | | | | |  \ \ /\ / /
 | |_| | | |_| |   \ V  V /
  \___/   \___/     \_/\_/

 */
INSERT INTO universities (university_id, name, abbreviation)
VALUES (2, 'University of Wollongong', 'UOW');

INSERT INTO faculties (faculty_id, name, code, university_id)
VALUES (4, 'Business', NULL, 2);

INSERT INTO faculties (faculty_id, name, code, university_id)
VALUES (5, 'Engineering and Information Sciences', NULL, 2);

/* Subjects */
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
