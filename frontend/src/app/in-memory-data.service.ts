import { InMemoryDbService } from 'angular-in-memory-web-api';
export class InMemoryDataService implements InMemoryDbService {
  createDb() {
    const subjects = [
      {
        title: 'Real-time Operating Systems',
        availability: ['Autumn session'],
        description: `This subject addresses the purposes, design alternatives and uses of computer operating systems for programming in embedded systems, PC, and servers. After several weeks studying the areas of responsibility that an operating system possesses in the context of a conventional computing environment (PC and server), a treatment of operating systems in support of meeting real-time computing requirements, such as managing operating for programming on PC and server, is completed.
                      Topics include: process management, scheduling and inter-process communication, memory management and I/O device management. Comparisons of designs including monolithic and microkernel approaches. Embedded systems are explored as case studies for investigating operating systems modifications. The Linux kernel is studied as an example of a UNIX environment and programming exercises are completed in POSIX compliant C. Real-time systems are studied including real-time kernels and real-time CPU scheduling.`,
        courseNumber: 48450,
        requisite: 'Embedded Software ',
        studentType: 'undergraduate',
      },

      {
        title: 'Embedded Software',
        availability: ['Spring session'],
        description: `This subject develops the student's technical knowledge of the design, implementation and testing of software modules and application frameworks for embedded systems.
                      Students develop their ability to interpret and evaluate a set of software specifications and work in small groups to write software modules and applications for an embedded system. Students are introduced to abstracting hardware functionality into software modules and researching and implementing software data structures.
                      Students develop their ability to test and modify their software to ensure compliance with the application specifications and be introduced to reviewing and evaluating their own and others software.
                      The technical content is contextualised in a project in which students analyse the requirements of an embedded system and design the software to meet those requirements. Skills in debugging software are also developed through the practice-based nature of the subject.`,
        courseNumber: 48434,
        requisite: 'Fundamentals of C Programming  ',
        studentType: 'undergraduate',
      },

      {
        title: 'Fundamentals of C Programming',
        availability: ['Autumn session', 'Spring session'],
        description: `Data engineers use C programming language to collect, process and store data. This is an introductory subject to high-level procedural programming using C programming language. Students learn basic programming concepts such as conditional statements, iteration, functions, arrays, file processing, data structures and memory management using pointers. The subject also introduces how to compile C source code using the GNU toolchain, link binary object files and run executable files. As a data engineer would, students go through the complete development cycle, incorporating analysis of complex problems, programming solution design, implementation in C, debugging and testing.`,
        courseNumber: 48430,
        requisite: '',
        studentType: 'undergraduate',
      },

      {
        title: 'Networking Essentials',
        availability: ['Spring session'],
        description: `Computer networks are now business critical in all modern organisations and business enterprises. They are important in everyday life. This subject introduces students to the fundamental issues in modern data communications and computer networks. This is essential knowledge for all users of IT, IT professionals and those who wish to specialise in computer networking. Students learn about the layered networking model and are introduced to networking devices and protocols. They learn how these are used in computer networks and in net-based application programs. The primary focus of this subject is Local Area Networks (LAN). Student practical work includes designing and building simple peer-to-peer networks and LANs that are connected to the Internet. The core set of protocols employed on the global Internet, TCP/IP, is studied.`,
        courseNumber: 31270,
        requisite: '',
        studentType: 'undergraduate',
      },

      {
        title: 'Database Fundamentals',
        availability: ['Autumn session'],
        description: `This subject introduces students to the fundamentals of effective database systems. Students are taught how data is structured and managed in an organisation in a way that can be used effectively by applications and users. They also learn to use the language SQL for effective data retrieval and modification. This subject teaches students to appreciate the significance and challenges of good database design and management, which underpin the development of functional software applications.`,
        courseNumber: 31271,
        requisite: 'Programming Fundamentals',
        studentType: 'undergraduate',
      },

      {
        title: 'Programming Fundamentals',
        availability: ['Autumn session', 'Spring session'],
        description: `This subject provides basic skills in Java programming and software design, with no assumed knowledge of programming. It covers the topics of object-oriented (OO) programming concepts, data flow, control flow, arrays, and the basics of sorting and searching algorithms. The subject teaches and illustrates a design process using a set of design notations and design rules, and shows how to develop a correct, readable and reusable solution from a problem specification.`,
        courseNumber: 48023,
        requisite: '',
        studentType: 'undergraduate',
      },

      {
        title: 'Digital Forensics',
        availability: ['Spring session'],
        description: `This is a practice-based subject, using material based on the textbook. Learning is laboratory-based. Students assess if a crime has been committed, acquire digital evidence, analyse the evidence and prepare forensic reports.
                      The emphasis is on digital forensics applications, in particular:
                      forensic analysis of a digital storage device where evidence of visits to web sites is recovered to support or oppose a hypothesis before a criminal court
                      eDiscovery (a form of discovery related to civil litigation) where students acquire evidence of contact with a third party using email or social media
                      intrusion investigation into the nature and extent of an unauthorised network intrusion. Students look for evidence of malware being installed on the device that may use the network to exfiltrate data to an unauthorised person.`,
        courseNumber: 48436,
        requisite: 'Network Security',
        studentType: 'undergraduate',
      },
      {
        title: 'Parallel and Multicore Computing',
        availability: ['Autumn session'],
        description: `This subject adopts both a traditional coursework approach to teaching complex issues of developing parallel and multicore computing systems and a problem-based learning approach to the task of designing and implementing complex software intensive applications that use parallel and multicore computing paradigms, components and tools. The final product of the subject is a fine quality, well designed, implemented and documented software project (development plan, requirements specification, design of the system, application code and test).`,
        courseNumber: 42009,
        requisite: 'Fundamentals of Software Development ',
        studentType: 'postgraduate',
      },
      {
        title: 'Advanced and Distributed Operating Systems',
        availability: ['Autumn session'],
        description: `This subject provides an in-depth examination of the principles of distributed systems in general, and distributed operating systems in particular. Covered topics include processes and threads, concurrent programming, distributed process scheduling, distributed interprocess communication, virtualisation, distributed file systems, distributed middleware and applications such as peer-to-peer and web systems. Fundamentals of operating system for multiprocessor is also covered in this subject.If time permits, a brief overview of advanced topics such as green computing, grid computing, and mobile computing is also provided.`,
        courseNumber: 42010,
        requisite: '',
        studentType: 'postgraduate',
      },
      {
        title: 'Fundamentals of Security',
        availability: ['Spring session'],
        description: `Security is a major issue for enterprises, with breaches leaving them vulnerable to legal sanctions, financial loss or reduced customer confidence. This subject introduces students to modern security issues and technologies by considering various aspects, from security principles and policies, to network and system security, as well as intrusion detection and cyber security. Students learn and apply programming skills to implement secure communications while demonstrating professional practice in a group project.`,
        courseNumber: 41900,
        requisite:
          '(31268 Web Systems OR 48410 Introduction to ICT Engineering OR 41082 Introduction to Data Engineering) AND (48023 Programming Fundamentals OR 48430 Fundamentals of C Programming OR 31267 Programming Fundamentals OR 31465 Object-oriented Programming)',
        studentType: 'undergraduate',
      },

      {
        title: 'Quantum Computing',
        availability: ['Spring session'],
        description: `Quantum computing is a disruptive new technology since quantum computers promise dramatic advantages over current computers. Recent rapid physical experimental progress has made it possible that large-scalable and functional quantum computers will be built within 10 years. This subject exposes and demystifies quantum computing using a step-by-step approach. It introduces systematically the basic principles of quantum computing, quantum algorithms and programming methodologies and techniques so that the students can develop software to realise the superpower of quantum computers.`,
        courseNumber: 41076,
        requisite: '',
        studentType: 'postgraduate',
      },

      {
        title: 'Data Driven and Intelligent Robotics',
        availability: ['Spring session'],
        description: `Intelligent robots are a disruptive technology poised to transform business and society. A robot is a real-time distributed system that makes complex real-time decisions autonomously using data it collects from a wide range of sources such as sensors and the internet. Robots spend their entire lives gathering, analysing and integrating data for decisions that guide their behaviour. This subject builds on previous data analytics subjects to provide an understanding of the critical role data analytics plays in the design and development of intelligent robots. It focuses on machine learning and feedback mechanisms required to develop autonomous real-time decision making that enables intelligent robots to achieve specific missions and interact with people.`,
        courseNumber: 41077,
        requisite: '',
        studentType: 'postgraduate',
      },
    ];
    return {
      subjects,
    };
  }
}
