import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { Location } from '@angular/common';

import { AssessmentService } from '../services/assessment.service';
import { AuthService } from '../services/auth.service';
import { ToastrService } from 'ngx-toastr';
import { Assessment } from '../models/assessment';
import { Utils } from '../utils/utils';

@Component({
  selector: 'app-admin-assessments',
  templateUrl: './admin-assessments.component.html',
  styleUrls: ['./admin-assessments.component.css'],
  providers: [AssessmentService]
})
export class AdminAssessmentsComponent implements OnInit {

  assessments: Assessment[] = [];
  errorMessage: string = null;
  isLoading: boolean;

  private universityId: string;
  private subjectId: string;

  constructor(private router: Router,
              private assessmentService: AssessmentService,
              private authService: AuthService,
              private route: ActivatedRoute,
              private location: Location,
              private toastr: ToastrService) {
  }

  goBack() {
    this.location.back();
  }


  ngOnInit() {
    this.route.params.forEach((params: Params) => {
      this.universityId = params['universityId'];
      this.subjectId = params['subjectId'];
    });

    this.fetch();
  }

  fetch() {
    this.isLoading = true;
    this.assessmentService.getAssessments(this.universityId, this.subjectId)
      .then(assessments => {
        this.isLoading = false;
        this.assessments = assessments;
      })
      .catch(error => {
        this.isLoading = false;
        this.toastr.error(Utils.getApiErrorMessage(error), 'Unable to fetch assessments');
      })

  }

  deleteAssessment(assessmentId: string) {
    this.isLoading = true;
    this.assessmentService.deleteAssessment(this.universityId, this.subjectId, assessmentId)
      .then(response => {
        this.toastr.success(null, 'Assessment Deleted', {timeOut: 3000});
        this.fetch();
      })
      .catch(error => {
        this.errorMessage = Utils.getApiErrorMessage(error);
        this.isLoading = false;
        this.toastr.error(this.errorMessage, 'Unable to delete');
      });
  }


  editAssessment(assessmentId: number) {
    this.router.navigate([`/admin/university/${this.universityId}/subject/${this.subjectId}/assessment/${assessmentId}`]);
  }

  createAssessment() {
    this.router.navigate([`/admin/university/${this.universityId}/subject/${this.subjectId}/assessment/create`]);
  }
}
