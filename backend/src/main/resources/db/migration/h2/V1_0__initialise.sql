CREATE TABLE universities (
    university_id BIGINT IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    abbreviation VARCHAR(5) NOT NULL
);

CREATE TABLE faculties (
    faculty_id BIGINT IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(5) NOT NULL,
    university_id BIGINT NOT NULL,
    FOREIGN KEY (university_id) REFERENCES universities (university_id)
);

CREATE TABLE subjects (
    subject_id BIGINT IDENTITY PRIMARY KEY,
    code VARCHAR(10) NOT NULL,
    name VARCHAR(100) NOT NULL,
    credit_points INT NOT NULL,
    description VARCHAR(10000) NOT NULL,
    min_requirements VARCHAR(1000) NOT NULL,
    undergrad BOOLEAN NOT NULL,
    postgrad BOOLEAN NOT NULL,
    autumn BOOLEAN NOT NULL,
    spring BOOLEAN NOT NULL,
    summer BOOLEAN NOT NULL,
    rating FLOAT DEFAULT 0.0,
    num_ratings INT DEFAULT 0,
    faculty_id BIGINT NOT NULL,
    FOREIGN KEY (faculty_id) REFERENCES faculties (faculty_id)
);

CREATE TABLE assessments (
    assessment_id BIGINT IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(100) NOT NULL,
    weighting INT NOT NULL,
    group_work BOOLEAN NOT NULL,
    length VARCHAR(100) NOT NULL,
    type INT NOT NULL,
    subject_id BIGINT NOT NULL,
    FOREIGN KEY (subject_id) REFERENCES subjects (subject_id)
);


/* Initialise universities */
INSERT INTO universities (university_id, name, abbreviation)
VALUES (1, 'University of Technology Sydney', 'UTS');

INSERT INTO universities (university_id, name, abbreviation)
VALUES (2, 'University of Wollongong', 'UOW');

INSERT INTO universities (university_id, name, abbreviation)
VALUES (3, 'University of Sydney', 'USYD');

/* Initialise faculties */
INSERT INTO faculties (faculty_id, name, code, university_id)
VALUES (1, 'Faculty of Engineering and Information Technology', 'FEIT', 1);

INSERT INTO faculties (faculty_id, name, code, university_id)
VALUES (2, 'Faculty of Health', 'FOH', 1);

INSERT INTO faculties (faculty_id, name, code, university_id)
VALUES (3, 'Faculty of Law', 'FOL', 1);



/* Initialise subjects */

/* SOFTWARE ENGINEERING PRACTICE */
INSERT INTO subjects (subject_id, code, name, credit_points, description, min_requirements, undergrad,
                      postgrad, autumn, spring, summer, faculty_id)
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
INSERT INTO subjects (subject_id, code, name, credit_points, description, min_requirements, undergrad,
                      postgrad, autumn, spring, summer, faculty_id)
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

/* .NET Application Development*/
INSERT INTO subjects (subject_id, code, name, credit_points, description, min_requirements, undergrad,
                      postgrad, autumn, spring, summer, faculty_id)
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

/*  .NET Enterprise Development*/
INSERT INTO subjects (subject_id, code, name, credit_points, description, min_requirements, undergrad,
                      postgrad, autumn, spring, summer, faculty_id)
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
