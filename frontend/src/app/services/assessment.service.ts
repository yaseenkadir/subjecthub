
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Assessment, assessmentTypeFromString } from '../models/assessment';
import { ApiUrlUtils, Utils } from '../utils/utils';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class AssessmentService {

  constructor(private http: HttpClient) {

  }

  getAssessments(universityId: string, subjectId: string): Promise<Assessment[]> {
    return this.http.get(ApiUrlUtils.buildAssessmentsUrl(universityId, subjectId))
      .toPromise()
      .then((response: Array<any>) => {
        let assessments: Assessment[] = [];
        for (let assessment of response) {
          assessments.push(this.anyToAssessment(assessment));
        }
        return assessments;
      })
  }

  getAssessment(universityId: string, subjectId: string, assessmentId: string) {
    return this.handleAssessmentResponse(
      this.http.get(ApiUrlUtils.buildAssessmentUrl(universityId, subjectId, assessmentId))
    );
  }

  editAssessment(universityId: string, subjectId: string, assessment: Assessment): Promise<Assessment> {
    return this.handleAssessmentResponse(
      this.http.put(ApiUrlUtils.buildAssessmentUrl(universityId, subjectId, assessment.id), assessment)
    );
  }

  createAssessment(universityId: string, subjectId: string, assessment: Assessment): Promise<Assessment> {
    return this.handleAssessmentResponse(
      this.http.post(ApiUrlUtils.buildAssessmentsUrl(universityId, subjectId) + '/assessment',
        assessment)
    );
  }

  deleteAssessment(universityId: string, subjectId: string, assessmentId: string): Promise<void> {
    return this.http.delete(ApiUrlUtils.buildAssessmentUrl(universityId, subjectId, assessmentId))
      .toPromise()
      .then(() => {});
  }

  private handleAssessmentResponse(r: Observable<any>): Promise<Assessment> {
    return r.toPromise()
      .then(response => {
        return this.anyToAssessment(response);
      })
  }

  /**
   * Converts any type response to an Assessment object
   */
  private anyToAssessment(o: any) {
    // TODO: Upgrade typescript so we can use string enums
    let assessmentType = assessmentTypeFromString(o.type);
    return new Assessment(o.id, o.name, o.description, o.weighting, o.groupWork, o.length,
      assessmentType)
  }
}
