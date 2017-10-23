import { Component, OnInit } from '@angular/core';
import { University } from '../models/university';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Consts } from '../config/consts';
import { Utils } from '../utils/utils';
import { ActivatedRoute, Params } from '@angular/router';
import { UniversityService } from '../services/university.service';
import { ToastrService } from 'ngx-toastr';
import { Location } from '@angular/common';

enum ActionType {
  Edit,
  Create
}

@Component({
  selector: 'app-edit-university',
  templateUrl: './edit-university.component.html',
  styleUrls: ['./edit-university.component.css'],
  providers: [UniversityService],
})
export class EditUniversityComponent implements OnInit {

  university: University;
  isLoading: boolean;
  actionType: ActionType;
  title: String;

  private editForm: FormGroup;
  private displayNameError: boolean = false;
  private displayAbbreviationError: boolean = false;
  private attemptedSave: boolean = false;

  constructor(private route: ActivatedRoute,
              private universityService: UniversityService,
              private toastr: ToastrService,
              private location: Location,
              fb: FormBuilder) {

    this.editForm = fb.group({
      'universityName': [
        null,
        Validators.compose([
          Validators.required,
          Validators.minLength(Consts.MIN_UNIVERSITIES_NAME_LENGTH),
          Validators.maxLength(Consts.MAX_UNIVERSITIES_NAME_LENGTH)
        ])
      ],
      'universityAbbreviation': [
        null,
        Validators.compose([
          Validators.required,
          Validators.minLength(Consts.MIN_UNIVERSITIES_ABBREVIATION_LENGTH),
          Validators.maxLength(Consts.MAX_UNIVERSITIES_ABBREVIATION_LENGTH)
        ])
      ],
      'universityImageUrl': [
        null,
        Validators.compose([
          Validators.required,
        ])
      ]
    });

    this.universityName.valueChanges.subscribe(name => {
      // Display error only if the user has already once exceeded the max length
      // This is to avoid the user seeing an error message as they're inputting valid data.
      if (!this.displayNameError && Utils.exceededMinLength(name, Consts.MIN_UNIVERSITIES_NAME_LENGTH)) {
        this.displayNameError = true;
      }
    });

    this.universityAbbreviation.valueChanges.subscribe(name => {
      if (!this.displayAbbreviationError && Utils.exceededMinLength(name, Consts.MIN_UNIVERSITIES_ABBREVIATION_LENGTH)) {
        this.displayAbbreviationError = true;
      }
    });
  }

  goBack() {
    this.location.back();
  }

  get universityName() {
    return this.editForm.get('universityName');
  }

  get universityAbbreviation() {
    return this.editForm.get('universityAbbreviation');
  }

  get universityImageUrl() {
    return this.editForm.get('universityImageUrl');
  }

  ngOnInit() {
    this.route.params.forEach((params: Params) => {
      console.log(params);
      if (params['universityId'] !== undefined) {
        this.actionType = ActionType.Edit;
        this.fetchUniversity(params['universityId']);
      } else {
        this.actionType = ActionType.Create;
        this.title = 'Creating University';
      }
    });
  }

  private fetchUniversity(universityId: string) {
    this.isLoading = true;
    this.universityService.getUniversity(universityId)
      .then((university: University) => {
        this.isLoading = false;
        this.university = university;
        this.title = `Editing ${university.name}`;
        console.log(university);
        this.universityName.setValue(university.name);
        this.universityAbbreviation.setValue(university.abbreviation);
        this.universityImageUrl.setValue(university.imageUrl);
      })
      .catch((error) => {
        // TODO: Display big error message;
        this.isLoading = false;
      });
  }

  save() {
    // Called when the user tries to save the university.
    this.attemptedSave = true;

    if (this.editForm.valid) {
      switch (this.actionType) {
        case ActionType.Edit:
          this.editUniversity();
          break;
        case ActionType.Create:
          this.createUniversity();
          break;
        default:
          throw new Error(`Unexpected action type ${this.actionType}`);
      }
    } else {
      Utils.markFormAsModified(this.editForm);
      this.displayNameError = true;
      this.displayAbbreviationError = true;
    }
  }

  private editUniversity() {
    let newUni = new University(this.university.id, this.universityName.value,
      this.universityAbbreviation.value, this.universityImageUrl.value);

    if (this.university.equals(newUni)) {
      this.toastr.info('No changes made.');
      this.location.back();
      return;
    }

    this.isLoading = true;
    this.universityService.editUniveristy(newUni.id, newUni)
      .then((response: University) => {
        this.isLoading = false;
        this.toastr.success(`Edited ${response.name}`, null, {timeOut: 3000});
        this.location.back();
      })
      .catch(error => {
        console.log(error);
        this.isLoading = false;
        this.toastr.error(Utils.getApiErrorMessage(error), 'Unable to edit university');
      });
  }

  private createUniversity() {
    let newUni = new University(null, this.universityName.value, this.universityAbbreviation.value,
      this.universityImageUrl.value);

    this.isLoading = true;
    this.universityService.createUniversity(newUni)
      .then((response: University) => {
        this.isLoading = false;
        this.toastr.success(`Created ${response.name}`, null, {timeOut: 3000});
        this.location.back();
      })
      .catch(error => {
        console.log(error);
        this.isLoading = false;
        this.toastr.error(Utils.getApiErrorMessage(error), 'Unable to create university');
      });
  }
}
