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
import { EditUniversityComponent } from './edit-university.component';

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
  private editUniModal: BsModalRef;
  private isEditingUni: boolean;
  private isCreatingUni: boolean;
  private editingUni: University;

  constructor(router: Router, universityService: UniversityService, authService: AuthService,
              toastr: ToastrService, private modalService: BsModalService) {
    super(router, universityService, authService, toastr);
  }

  ngOnInit() {
    super.ngOnInit();
    this.modalService.onHidden.subscribe((reason: string) => {
      // We're not unsubscribing from these events. Could cause memory issues.
      this.onEditModelClose();
    });
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

  openModal(university: University) {
    this.editUniModal = this.modalService.show(EditUniversityComponent);

    if (university != null) {
      this.editUniModal.content.setUniversity(university);
      this.editingUni = university;
      console.log(this.editingUni);
      console.log(typeof this.editingUni.id);
      this.editUniModal.content.title = 'Edit University';
      this.isEditingUni = true;
    } else {
      this.editUniModal.content.title = 'Create University';
      this.isCreatingUni = true;
    }
  }

  private onEditModelClose(): void {
    console.log('Modal closed');
    let saved = this.editUniModal.content.saved;
    if (saved) {
      let uni = this.editUniModal.content.getUniversity();
      if (this.isCreatingUni) {
        this.createUniversity(uni);
      } else if (this.isEditingUni) {
        uni.id = this.editingUni.id;
        if (uni.name == this.editingUni.name && uni.abbreviation == this.editingUni.abbreviation) {
          this.toastr.info('No changes made.')
        } else {
          this.editUniversity(uni);
        }
      } else {
        throw new Error("Modal closed but without knowing if edit or create action.");
      }
    }
    this.editUniModal = null;
    this.isEditingUni = false;
    this.isCreatingUni = false;
    this.editingUni = null;
  }

  private createUniversity(university: University): void {
    this.universityService.createUniversity(university)
      .then((response: University) => {
        this.toastr.success(`Created ${university.name}`, null, {timeOut: 3000});
        this.fetch();
      })
      .catch(error => {
        console.log(error);
        this.toastr.error(Utils.getApiErrorMessage(error), 'Unable to create university');
      });
  }

  private editUniversity(university: University): void {
    this.universityService.editUniveristy(university.id, university)
      .then((response: University) => {
        this.toastr.success(`Edited ${response.name}`, null, {timeOut: 3000});
        this.fetch();
      })
      .catch(error => {
        console.log(error);
        this.toastr.error(Utils.getApiErrorMessage(error), 'Unable to edit university');
      });
  }
}
