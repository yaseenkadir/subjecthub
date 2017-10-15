import { Component, Input, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { Location } from '@angular/common';
import Promise from 'bluebird';

import { SubjectService } from '../services/subject.service';
import { Subject } from '../models/subject';

import { FacultyService } from '../services/faculty.service';
import { Faculty } from '../models/faculty';


import { UniversityService} from '../services/university.service';
import { University} from '../models/university';


import { ToastrService } from 'ngx-toastr';

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

  isCreate: boolean;
  selectedSubject: Subject;
  isEditingSubject: boolean;
  errors = {
    name: '',
    code: '',
    creditPoints: '',
    description: '',
    minRequirements: '',
    graduateType: '',
    session: '',
  }


  constructor(
    private subjectService: SubjectService,
    private universityService: UniversityService,
    private facultyService: FacultyService,
    private location: Location,
    private router: Router,
    private route: ActivatedRoute,
    protected toastr: ToastrService,
  ) {
    this.isEditingSubject = false;
  }

  setErrors() {
    return {
      name: '',
      code: '',
      creditPoints: '',
      description: '',
      minRequirements: '',
      graduateType: '',
      session: '',
    }
  }


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


  fetchSubjects(universityId: string): Promise<Subject[]> {
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

  goBack() {
    this.location.back();
  }

  submit() {
   if (!this.validateSubject()) return;
    if (this.isCreate) {
      this.createSubject();
    } else {
      this.editSubject();
    }
  }

  createSubject() {
    this.selectedSubject.faculty = this.faculty;
    this.subjectService.createSubject(this.university.id, this.selectedSubject)
    .then((response: Subject) => {
      this.toastr.success(`Created ${this.selectedSubject.name}`, null, {timeOut: 3000});
      this.fetchSubjects(this.university.id.toString()).then(subjects => {
        this.subjects = subjects.filter(subject => subject.faculty.id === this.faculty.id);
      } )
      this.hideSubjectForm();
    })
    .catch(error => {
      console.log(error);
      this.toastr.error(Utils.getApiErrorMessage(error), 'Unable to create Subject');
    });
  }


  editSubject() {
    delete this.selectedSubject.assessments;
    delete this.selectedSubject.comments;
    this.subjectService.editSubject(this.university.id, this.selectedSubject.id, this.selectedSubject)
    .then((response: Subject) => {
      this.toastr.success(`Edited ${this.selectedSubject.name}`, null, {timeOut: 3000});
      this.fetchSubjects(this.university.id.toString()).then(subjects => {
        this.subjects = subjects.filter(subject => subject.faculty.id === this.faculty.id);
      } )
      this.hideSubjectForm();
    })
    .catch(error => {
      console.log(error);
      this.toastr.error(Utils.getApiErrorMessage(error), 'Unable to create Subject');
    });
  }

  setGraduateType(isUndergrad: boolean) {
    if (isUndergrad) {
      this.selectedSubject.undergrad = true;
      this.selectedSubject.postgrad = false;
    }
    else {
      this.selectedSubject.postgrad = true;
      this.selectedSubject.undergrad = false;
    }
  }

  setSession(key) {
    this.selectedSubject[key] = !this.selectedSubject[key];
  }

  setErrorClass(key) {
    let className = 'form-group';
    if (this.errors[key]) className += ' has-error';
    return className;
  }

  validateSubject() {
    this.errors = this.setErrors();
    const name = this.selectedSubject.name.trim();
    const code = this.selectedSubject.code.trim();
    const creditPoints = this.selectedSubject.creditPoints.toString().trim();
    const description = this.selectedSubject.description.trim();
    const graduateType = {
      undergrad: this.selectedSubject.undergrad,
      postgrad: this.selectedSubject.postgrad
    }
    const session = {
      summer: this.selectedSubject.summer,
      autumn: this.selectedSubject.autumn,
      spring: this.selectedSubject.spring
    }

    if (!name) {
      this.errors.name = 'A Subject must have a name';
    }

    if (!code) {
      this.errors.code = 'A subject must have a code';
    }

    if (!creditPoints.match(/^\d+$/)) {
      this.errors.creditPoints = 'Credit Points must be a number';
    }

    if (!description) {
      this.errors.description = 'A subject must have a description';
    }

    if (!graduateType.postgrad && !graduateType.undergrad) {
      this.errors.graduateType = 'A subject must be given a graduate type';
    }

    if (!session.autumn && !session.spring && !session.summer) {
      this.errors.session = 'A subject must be given a session';
    }

    const errs = Object.keys(this.errors).filter(key => !!this.errors[key]);

    return !errs || errs.length < 1;

  }


  showSubjectForm(selectedSubject) {
    if (!selectedSubject) {
      selectedSubject = new Subject();
      this.isCreate = true;

    } else {
      this.isCreate = false;
    }

    this.selectedSubject = selectedSubject;
    console.log(this.selectedSubject);
    this.isEditingSubject = !this.isEditingSubject;

  }

  hideSubjectForm() {
    this.selectedSubject = undefined;
    this.isEditingSubject = !this.isEditingSubject;
  }
}
