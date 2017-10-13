import { Component, Input, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { University } from '../models/university';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Consts } from '../config/consts';
import { Utils } from '../utils/utils';

@Component({
  selector: 'app-edit-university',
  templateUrl: './edit-university.component.html',
  styleUrls: ['./universities-admin.component.css', '../universities/universities.component.css']
})

export class EditUniversityComponent implements OnInit {

  // These are public because the caller (UniversitiesAdminComponent) needs to access/modify these
  public title: string;
  public saved: boolean = false;

  private editForm: FormGroup;
  private displayNameError: boolean = false;
  private displayAbbreviationError: boolean = false;
  private attemptedSave: boolean = false;

  constructor(public bsModalRef: BsModalRef, private fb: FormBuilder) {
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

  get universityName() {
    return this.editForm.get('universityName');
  }

  get universityAbbreviation() {
    return this.editForm.get('universityAbbreviation');
  }

  ngOnInit(): void {
  }

  save() {
    // Called when the user tries to save the university.
    this.attemptedSave = true;

    if (this.editForm.valid) {
      // Set saved to true, so that caller knows that the user wishes to make changes
      this.saved = true;
      this.bsModalRef.hide();
    } else {
      Utils.markFormAsModified(this.editForm);
      this.displayNameError = true;
      this.displayAbbreviationError = true;
    }
  }

  setUniversity(university: University) {
    this.universityName.setValue(university.name);
    this.universityAbbreviation.setValue(university.abbreviation);
  }

  /**
   * Returns a university object of the name and value only! Does not include the id of the edited
   * university. The caller should keep a copy. I know this is bad!
   *
   * TODO: Change this. This is bad.
   * @returns {University}
   */
  getUniversity() {
    return new University(null, this.universityName.value, this.universityAbbreviation.value);
  }
}
