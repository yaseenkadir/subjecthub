import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import Promise from 'bluebird';

import { UniversityService} from '../services/university.service';
import { University} from '../models/university';

import { FacultyService } from '../services/faculty.service';
import { Faculty } from '../models/faculty';

import { SubjectService } from '../services/subject.service';
import { Subject } from '../models/subject';

import { Utils } from '../utils/utils';
import { AuthService } from '../services/auth.service';
import { ToastrService } from 'ngx-toastr';
import { BsModalService } from 'ngx-bootstrap/modal';
import { BsModalRef } from 'ngx-bootstrap/modal/modal-options.class';
import { CreateFacultyComponent } from '../create-faculty/create-faculty.component';


@Component({
  selector: 'app-university-edit',
  templateUrl: './university-edit.component.html',
  styleUrls: ['./university-edit.component.css'],
  providers: [UniversityService, AuthService, FacultyService, SubjectService],
})
export class UniversityEditComponent implements OnInit {

  university: University;
  subjects: Subject[];
  faculties: Faculty[];
  errorMessage: string = null;
  filterFaculties: string;
  filterSubjects: string
  isLoading: boolean;

  private editFacultyModal: BsModalRef;
  private isEditingUni: boolean;
  private isCreatingFaculty: boolean;
  private editingFaculty: Faculty;

  constructor(
    protected router: Router, 
    protected universityService: UniversityService,
    protected authService: AuthService,
    protected toastr: ToastrService, 
    private route: ActivatedRoute,
    private subjectService: SubjectService,
    private facultyService: FacultyService,
    private modalService: BsModalService
    ) {
      this.subjects  = [];
      this.faculties = [];
}

cleanMessages(){
  this.errorMessage = null;
}

  ngOnInit() {
    this.isLoading = true;
    this.route.paramMap
    .switchMap((params: ParamMap) => {
      const universityId = params.get('university');
      return Promise.all([
        this.fetchFaculties(universityId),
        this.fetchSubjects(universityId),
        this.fetchUni(universityId)
      ])
    }).subscribe(() => {
      this.isLoading = false;
    })
    this.modalService.onHidden.subscribe((reason: string) => {
      // We're not unsubscribing from these events. Could cause memory issues.
      this.onEditFacultyModelClose();
    });
  
  }

  fetchFaculties(universityId: string) {
    this.facultyService.fetch(universityId)
    .then(faculties => {
      this.faculties = faculties;
    })
    .catch(e => {
        this.errorMessage = Utils.getApiErrorMessage(e);
    });
  }

  fetchSubjects(universityId: string) {
    this.subjectService.getSubjects(universityId)
    .then(subjects => {
         this.subjects = subjects;
    })
    .catch(e => {   
        this.errorMessage = Utils.getApiErrorMessage(e);
    });
  }


  fetchUni(universityId: string) {
    return this.universityService.getUniversity(universityId)
        .then(university => {
          this.university = university;
        })
        .catch(e => {
            this.errorMessage = Utils.getApiErrorMessage(e);
        });
}


goToCreateSubjectPage(faculty: Faculty) {
  this.router.navigate([`university/${this.university.id}/faculty/${faculty.id}/subjects/edit`])
}

openFacultyModal(faculty: Faculty) {
  this.editFacultyModal = this.modalService.show(CreateFacultyComponent);

  if (faculty != null) {
    this.editFacultyModal.content.setFaculty(faculty);
    this.editingFaculty = faculty;
    console.log(this.editingFaculty);
    console.log(typeof this.editingFaculty.id);
    this.editFacultyModal.content.title = 'Edit Faculty';
    this.isEditingUni = true;
  } else {
    this.editFacultyModal.content.title = 'Create Faculty';
    this.isCreatingFaculty = true;
  }
}

private onEditFacultyModelClose(): void {
  console.log('Modal closed');
  let saved = this.editFacultyModal.content.saved;
  if (saved) {
    let faculty = this.editFacultyModal.content.getFaculty();
    if (this.isCreatingFaculty) {
      this.createFaculty(faculty);
    } else if (this.isEditingUni) {
      faculty.id = this.editingFaculty.id;
      if (faculty.name == this.editingFaculty.name && faculty.code == this.editingFaculty.code) {
        this.toastr.info('No changes made.')
      } else {
        this.editFaculty(faculty);
      }
    } else {
      throw new Error("Modal closed but without knowing if edit or create action.");
    }
  }
  this.editFacultyModal = null;
  this.isEditingUni = false;
  this.isCreatingFaculty = false;
  this.editingFaculty = null;
}

private createFaculty(faculty: Faculty): void {
  this.facultyService.createFaculty(this.university.id.toString(), faculty)
    .then((response: Faculty) => {
      this.toastr.success(`Created ${faculty.name}`, null, {timeOut: 3000});
      this.fetchFaculties(this.university.id.toString());
      
    })
    .catch(error => {
      console.log(error);
      this.toastr.error(Utils.getApiErrorMessage(error), 'Unable to create Faculty');
    });
}


private editFaculty(faculty: Faculty): void {
  this.facultyService.editFaculty(this.university.id, faculty.id, faculty)
    .then((response: Faculty) => {
      this.toastr.success(`Edited ${response.name}`, null, {timeOut: 3000});
      this.fetchFaculties(this.university.id.toString());
      this.fetchSubjects(this.university.id.toString());
    })
    .catch(error => {
      console.log(error);
      this.toastr.error(Utils.getApiErrorMessage(error), 'Unable to edit university');
    });
}
}
