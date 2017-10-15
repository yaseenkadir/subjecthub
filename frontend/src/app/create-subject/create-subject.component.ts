import { Component, Input, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import Promise from 'bluebird';

import { SubjectService } from '../services/subject.service';
import { Subject } from '../models/subject';

import { FacultyService } from '../services/faculty.service';
import { Faculty } from '../models/faculty';


import { UniversityService} from '../services/university.service';
import { University} from '../models/university';

import { Utils } from '../utils/utils';

@Component({
  selector: 'app-create-subject',
  templateUrl: './create-subject.component.html',
  styleUrls: ['./create-subject.component.css'],
  providers: [SubjectService, UniversityService, FacultyService]
})
export class CreateSubjectComponent implements OnInit {

  university: University;
  subjects: Subject[];
  faculty: Faculty;
  errorMessage: string = null;
  isLoading: boolean;
  filterSubjects: string;

  constructor(
    private subjectService: SubjectService,
    private universityService: UniversityService,
    private facultyService: FacultyService,
    private router: Router, 
    private route: ActivatedRoute,
  ) {}

  fetchFaculty(universityId: string, facultyId: string) {
    return this.facultyService.getFaculty(universityId, facultyId)
    .then(faculty => faculty)
    .catch(e => Utils.getApiErrorMessage(e));
  }

  fetchUni(universityId: string) {
    return this.universityService.getUniversity(universityId)
        .then(university =>  university)
        .catch(e => Utils.getApiErrorMessage(e));
}

  fetchSubjects(universityId: string) {
    return this.subjectService.getSubjects(universityId)
    .then(subjects => subjects)
    .catch(e => Utils.getApiErrorMessage(e));
  }

  ngOnInit() {
    this.isLoading = true;
    this.route.paramMap
    .switchMap((params: ParamMap) => {
      const universityId = params.get('university');
      const facultyId = params.get('faculty');
      return Promise.all([
        this.fetchUni(universityId),
        this.fetchFaculty(universityId, facultyId),
        this.fetchSubjects(universityId)       
      ]).spread((university, faculty, subjects) => {
        this.university = university;
        this.faculty = faculty;
        this.subjects = subjects.filter(subject => subject.faculty.id === faculty.id);
      })
    }).subscribe(() => {
      this.isLoading = false;
    })
  }
}
