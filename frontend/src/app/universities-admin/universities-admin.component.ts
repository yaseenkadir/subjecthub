import { Component, OnInit } from '@angular/core';
import { University } from '../models/university';
import { AuthService } from '../services/auth.service';
import { UniversityService } from '../services/university.service';
import { UniversitiesComponent } from '../universities/universities.component';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Utils } from '../utils/utils';
import { BsModalService } from 'ngx-bootstrap/modal';
import { BsModalRef } from 'ngx-bootstrap/modal/modal-options.class';

@Component({
  selector: 'app-universities-admin',
  templateUrl: './universities-admin.component.html',
  styleUrls: ['./universities-admin.component.css', '../universities/universities.component.css'],
  providers: [AuthService, UniversityService, ToastrService],
})
export class UniversitiesAdminComponent extends UniversitiesComponent implements OnInit {

  universities: University[];
  errorMessage: string = null;
  isLoading: boolean;

  constructor(router: Router,
              universityService: UniversityService,
              authService: AuthService,
              toastr: ToastrService) {
    super(router, universityService, authService, toastr);
  }

  ngOnInit() {
    super.ngOnInit();
  }

  deleteUniversity(universityId: number) {
    this.universityService.deleteUniversity(universityId)
      .then(response => {
        this.toastr.success('University Deleted', null, {timeOut: 3000});
        this.fetch();
      })
      .catch(error => {
        this.errorMessage = Utils.getApiErrorMessage(error);
        this.toastr.error(this.errorMessage, 'Unable to delete', {timeOut: 5000});
      });
  }

  goToFacultyEdit(universityId: number) {
    this.router.navigate([], {})
    this.router.navigate([`university/${universityId}/faculties/edit`])
  }

  goToEditUniversity(universityId: number) {
    this.router.navigate([`university/${universityId}/edit`])
  }

  goToCreateUniversity() {
    this.router.navigate([`university/create`]);
  }
}
