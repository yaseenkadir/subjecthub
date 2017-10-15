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
  isLoading: boolean;
  isEditingFaculty: boolean;
  selectedFaculty: Faculty;
  errors = {
    name: '',
    code: ''
  }
  isCreate: boolean;

  constructor(
    protected router: Router,
    protected universityService: UniversityService,
    protected authService: AuthService,
    protected toastr: ToastrService,
    private route: ActivatedRoute,
    private subjectService: SubjectService,
    private facultyService: FacultyService,
    ) {
      this.subjects  = [];
      this.faculties = [];
      this.isEditingFaculty = false;
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
        this.fetchUni(universityId)
      ])
    }).subscribe(() => {
      this.isLoading = false;
    }) 
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


  showFacultyForm(selectedFaculty) {
    if (!selectedFaculty) {
      selectedFaculty = new Faculty(null, '', '')
      this.isCreate = true;

    } else {
      this.isCreate = false;
    }

    this.selectedFaculty = selectedFaculty;
    this.isEditingFaculty = !this.isEditingFaculty;
    console.log(this.selectedFaculty);
  }

  hideFacultyForm() {
    this.selectedFaculty = undefined;
    this.isEditingFaculty = !this.isEditingFaculty;
  }

  setNameErrorClass() {
    let className = 'form-group';
    if (this.errors.name) className += ' has-error';
    return className;
  }

  setCodeErrorClass() {
    let className = 'form-group';
    if (this.errors.code) className += ' has-error';
    return className;
  }

  validateFaculty() {
    const name = this.selectedFaculty.name.trim();
    const code = this.selectedFaculty.code.trim();

    if (!name) {
      this.errors.name = 'A Faculty Must have a name'
    }

    if (name.length > 50) {
      this.errors.name = 'A Faculty name must be less than 50 characters'
    }

    if (this.containsDigit(name)) {
      this.errors.name = "A Faculty name must not contain any numbers"
    }

    if (code && code.length > 10) {
      this.errors.code = 'A Faculty code must be less than 10 characters';
    }
    return !this.errors.code && !this.errors.name;
  }

  containsDigit(word) {
    return word.match(/\d+/);
  }

  submit() {
    if (!this.validateFaculty()) return;
    if (this.isCreate) {
      this.createFaculty(this.selectedFaculty)
    } else {
      this.editFaculty(this.selectedFaculty);
    }


  }

  private createFaculty(faculty: Faculty): void {

    this.facultyService.createFaculty(this.university.id.toString(), faculty)
      .then((response: Faculty) => {
        this.toastr.success(`Created ${faculty.name}`, null, {timeOut: 3000});
        this.fetchFaculties(this.university.id.toString());
        this.hideFacultyForm();
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
        this.hideFacultyForm();
      })
      .catch(error => {
        console.log(error);
        this.toastr.error(Utils.getApiErrorMessage(error), 'Unable to edit university');
      });
  }

}
