import { Injectable } from '@angular/core';
import 'rxjs/add/operator/toPromise';
import { Subject } from '../models/subject';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { ApiUrlUtils } from '../utils/utils';


@Injectable()
export class SubjectService {

  constructor(private http: HttpClient) {
  }

  getSubject(universityId: number | string, subjectId: number | string): Promise<Subject> {
    return this.handleSubjectResponse(
      this.http.get(ApiUrlUtils.buildSubjectUrl(universityId, subjectId))
    );
  }

  getSubjects(universityId: number | string): Promise<Subject[]> {
    return this.handleSubjectsResponse(
      this.http.get(ApiUrlUtils.buildSubjectsUrl(universityId))
    );
  }

  deleteSubject(universityId: number | string, subjectId: number | string): Promise<void> {
    return this.http
      .delete(ApiUrlUtils.buildSubjectUrl(universityId, subjectId))
      .toPromise()
      .then(() => {
      });
  }

  editSubject(universityId: number | string, subjectId: number | string, subject: Subject): Promise<Subject> {
    return this.handleSubjectResponse(
      this.http.put(ApiUrlUtils.buildSubjectUrl(universityId, subjectId), subject)
    );
  }

  createSubject(universityId: number | string, subject: Subject): Promise<Subject> {
    return this.handleSubjectResponse(
      this.http.post(ApiUrlUtils.buildSubjectsUrl(universityId) + '/subject', subject)
    );
  }

  private handleSubjectResponse(response: Observable<any>): Promise<Subject> {
    return response.toPromise().then(r => {
      return r as Subject;
    });
  }

  private handleSubjectsResponse(response: Observable<any>): Promise<Subject[]> {
    return response.toPromise().then(r => {
      return r as Subject[];
    });
  }
}
