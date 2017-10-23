import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActionType, Utils } from '../utils/utils';
import { Consts } from '../config/consts';
import { Assessment, AssessmentType, assessmentTypeFromString } from '../models/assessment';
import { ActivatedRoute, Params } from '@angular/router';
import { AssessmentService } from '../services/assessment.service';
import { ToastrService } from 'ngx-toastr';
import { Location } from '@angular/common';


@Component({
  selector: 'app-create-assessment',
  templateUrl: './create-assessment.component.html',
  styleUrls: ['./create-assessment.component.css'],
  providers: [AssessmentService]
})
export class CreateAssessmentComponent implements OnInit {

  private isLoading: boolean;
  private title: String;
  private universityId;
  private subjectId;

  // Specifies whether assessment is being edited or created
  private actionType: ActionType;

  // Assessment that already exists. This is used to compare if changes have been made.
  private existingAssessment: Assessment;
  // Assessment being edited in form
  private editingAssessment: Assessment;

  private editForm: FormGroup;

  // These are manually enabled when user tries to save an assessment and form is invalid.
  // Used to prevent errors from appearing as users are typing valid entries.
  private nameError: boolean = false;
  private descriptionError: boolean = false;
  private lengthError: boolean = false;

  private MIN_ASSESSMENT_NAME_LENGTH = Consts.MIN_ASSESSMENT_NAME_LENGTH;
  private MAX_ASSESSMENT_NAME_LENGTH = Consts.MAX_ASSESSMENT_NAME_LENGTH;
  private MIN_ASSESSMENT_DESCRIPTION_LENGTH = Consts.MIN_ASSESSMENT_DESCRIPTION_LENGTH;
  private MAX_ASSESSMENT_DESCRIPTION_LENGTH = Consts.MAX_ASSESSMENT_DESCRIPTION_LENGTH;
  private MIN_ASSESSMENT_LENGTH_LENGTH = Consts.MIN_ASSESSMENT_LENGTH_LENGTH;
  private MAX_ASSESSMENT_LENGTH_LENGTH = Consts.MAX_ASSESSMENT_LENGTH_LENGTH;

  constructor(private route: ActivatedRoute,
              private assessmentService: AssessmentService,
              private toastr: ToastrService,
              private location: Location,
              fb: FormBuilder) {
    this.editForm = fb.group({
      // groupWork is handled manually in code and is not build with the form.
      // called assessmentName to avoid name collision with `name`.
      'assessmentName': [
        null,
        Validators.compose([
          Validators.required,
          Validators.minLength(this.MIN_ASSESSMENT_NAME_LENGTH),
          Validators.maxLength(this.MAX_ASSESSMENT_NAME_LENGTH)
        ])
      ],
      'description': [
        null,
        Validators.compose([
          Validators.required,
          Validators.minLength(this.MIN_ASSESSMENT_DESCRIPTION_LENGTH),
          Validators.maxLength(this.MAX_ASSESSMENT_DESCRIPTION_LENGTH),
        ])
      ],
      'weighting': [
        null,
        Validators.compose([
          Validators.required,
          // https://stackoverflow.com/a/13473595
          // ^ Thank you for number regex, checks number is 1-100.
          Validators.pattern(/^[1-9][0-9]?$|^100$/)
        ]),
      ],
      'assessmentLength': [
        null,
        Validators.compose([
          Validators.required,
          Validators.minLength(this.MIN_ASSESSMENT_LENGTH_LENGTH),
          Validators.maxLength(this.MAX_ASSESSMENT_LENGTH_LENGTH)
        ])
      ],
      'assessmentType': [
        null,
        Validators.required
      ]
    });

    this.assessmentName.valueChanges.subscribe(name => {
      if (!this.nameError && Utils.exceededMinLength(name, this.MIN_ASSESSMENT_NAME_LENGTH)) {
        this.nameError = true;
      }
    });

    this.description.valueChanges.subscribe(name => {
      if (!this.descriptionError && Utils.exceededMinLength(name, this.MIN_ASSESSMENT_DESCRIPTION_LENGTH)) {
        this.descriptionError = true;
      }
    });

    this.assessmentLength.valueChanges.subscribe(name => {
      if (!this.lengthError && Utils.exceededMinLength(name, this.MIN_ASSESSMENT_LENGTH_LENGTH)) {
        this.lengthError = true;
      }
    });
  }

  ngOnInit() {
    this.route.params.forEach((params: Params) => {
      this.universityId = params['universityId'];
      this.subjectId = params['subjectId'];

      if (params['assessmentId'] === 'create') {
        this.actionType = ActionType.Create;
        this.title = 'Creating Assessment';
        // We set the values to empty strings so that angular can bind them to form inputs
        this.editingAssessment = new Assessment(null, '', '', 10, true, '');
      } else {
        this.actionType = ActionType.Edit;
        this.fetchAssessment(params['assessmentId']);
      }
    });
    // Set it to the first option in the list
    this.assessmentType.setValue(this.enumAssessmentTypes()[0])
  }

  get assessmentName() {
    return this.editForm.get('assessmentName');
  }

  get description() {
    return this.editForm.get('description');
  }

  get weighting() {
    return this.editForm.get('weighting');
  }

  get assessmentLength() {
    return this.editForm.get('assessmentLength');
  }

  get assessmentType() {
    return this.editForm.get('assessmentType');
  }

  // Util to return all the assessment types as a list of strings.
  enumAssessmentTypes() {
    return [
      AssessmentType[AssessmentType.PROJECT],
      AssessmentType[AssessmentType.REPORT],
      AssessmentType[AssessmentType.TEST],
      AssessmentType[AssessmentType.FINAL],
    ]
  }

  save() {
    this.nameError = false;
    this.descriptionError = false;
    this.lengthError= false;
    if (this.editForm.valid) {
      this.editingAssessment.type = assessmentTypeFromString(this.assessmentType.value);
      if (this.actionType == ActionType.Edit) {
        if (this.existingAssessment.equals(this.editingAssessment)) {
          console.log('No changes made. Returning to previous page.');
          this.toastr.info('No changes made.');
          this.location.back();
          return;
        }
        this.editAssessment();
      } else {
        this.createAssessment();
      }
    } else {
      Utils.markFormAsModified(this.editForm);
      console.log('Edit form is invalid.');
      console.log(this.editForm.controls.assessmentName.errors);
      console.log(this.editForm.controls.assessmentLength.errors);
      console.log(this.editForm.controls.description.errors);
      console.log(this.editForm.controls.groupWork.errors);
      console.log(this.editForm.controls.weighting.errors);
      console.log(this.editForm.controls.assessmentType.errors);
      // this.editForm.
      this.nameError = true;
      this.descriptionError = true;
      this.lengthError = true;
    }
  }

  fetchAssessment(assessmentId: string) {
    this.isLoading = true;
    this.assessmentService.getAssessment(this.universityId, this.subjectId, assessmentId)
      .then(assessment => {
        this.existingAssessment = assessment;
        this.title = `Editing ${assessment.name}`;

        // Create a new copy so we can modify without changing existing assessment.
        this.editingAssessment = new Assessment(assessment.id, assessment.name,
          assessment.description, assessment.weighting, assessment.groupWork, assessment.length,
          assessment.type);

        // FormGroup elements are binded using ngModel except for assessment type which is set
        this.assessmentType.setValue(AssessmentType[assessment.type]);

        this.isLoading = false;
      })
  }

  editAssessment() {
    this.assessmentService.editAssessment(this.universityId, this.subjectId, this.editingAssessment)
      .then(assessment => {
        this.toastr.success(null, 'Edited Assessment', {timeOut: 3000});
        this.location.back();
      })
      .catch(error => {
        console.log(error);
        this.toastr.error(Utils.getApiErrorMessage(error), 'Unable to edit assessment');
      })
  }

  createAssessment() {
    this.assessmentService.createAssessment(this.universityId, this.subjectId, this.editingAssessment)
      .then(assessment => {
        this.toastr.success(`Created ${assessment.name}.`, 'Created Assessment', {timeOut: 3000});
        this.location.back();
      })
      .catch(error => {
        console.log(error);
        this.toastr.error(Utils.getApiErrorMessage(error), 'Unable to create assessment');
      })
  }

  setGroupWork(groupWork: boolean) {
    this.editingAssessment.groupWork = groupWork;
  }
}
