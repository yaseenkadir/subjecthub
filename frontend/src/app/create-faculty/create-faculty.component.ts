import { Component, Input, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Faculty } from '../models/faculty';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Consts } from '../config/consts';
import { Utils } from '../utils/utils';

@Component({
  selector: 'app-create-faculty',
  templateUrl: './create-faculty.component.html',
  styleUrls: ['./create-faculty.component.css']
})
export class CreateFacultyComponent implements OnInit {

  public title: string;
  public saved: boolean = false;

  editForm: FormGroup;
  displayNameError: boolean = false;
  displayAbbreviationError: boolean = false;
  attemptedSave: boolean = false;


  constructor(public bsModalRef: BsModalRef, private fb: FormBuilder) {
    this.editForm = fb.group({
      'facultyName': [
        null,
        Validators.compose([
          Validators.required,
          Validators.minLength(Consts.MIN_UNIVERSITIES_NAME_LENGTH),
          Validators.maxLength(Consts.MAX_UNIVERSITIES_NAME_LENGTH)
        ])
      ],
      'facultyCode': [
        null,
        Validators.compose([
          Validators.required,
          Validators.minLength(Consts.MIN_UNIVERSITIES_ABBREVIATION_LENGTH),
          Validators.maxLength(Consts.MAX_UNIVERSITIES_ABBREVIATION_LENGTH)
        ])
      ]
    });

    this.facultyName.valueChanges.subscribe(name => {
      // Display error only if the user has already once exceeded the max length
      // This is to avoid the user seeing an error message as they're inputting valid data.
      if (!this.displayNameError && Utils.exceededMinLength(name, Consts.MIN_UNIVERSITIES_NAME_LENGTH)) {
        this.displayNameError = true;
      }
    });

    this.facultyCode.valueChanges.subscribe(name => {
      if (!this.displayAbbreviationError && Utils.exceededMinLength(name, Consts.MIN_UNIVERSITIES_ABBREVIATION_LENGTH)) {
        this.displayAbbreviationError = true;
      }
    });
  }

  get facultyName() {
    return this.editForm.get('facultyName');
  }

  get facultyCode() {
    return this.editForm.get('facultyCode');
  }

  ngOnInit() {
  }

  save() {
    // Called when the user tries to save the university.
    this.attemptedSave = true;

    if (this.editForm.valid) {
      // Set saved to true, so that caller knows that the user wishes to make changes
      this.saved = true;
      console.log('hding faculty modal');
      this.bsModalRef.hide();
    } else {
      Utils.markFormAsModified(this.editForm);
      this.displayNameError = true;
      this.displayAbbreviationError = true;
    }
  }

  setFaculty(faculty: Faculty) {
    this.facultyName.setValue(faculty.name);
    this.facultyCode.setValue(faculty.code);
  }

  /**
   * Returns a university object of the name and value only! Does not include the id of the edited
   * university. The caller should keep a copy. I know this is bad!
   *
   * TODO: Change this. This is bad.
   * @returns {Faculty}
   */
  getFaculty() {
    return new Faculty(null, this.facultyName.value, this.facultyCode.value);
  }

}
