import { Injectable } from '@angular/core';
import 'rxjs/add/operator/toPromise';
import { environment } from '../../environments/environment';
import { Subject } from '../models/subject';
import { HttpClient } from '@angular/common/http';


@Injectable()
export class SubjectService {

  constructor(private http: HttpClient) {
  }

  fetch(universityId, subjectId): Promise<Subject> {
    return this.http
      .get(this.buildSubjectUrl(universityId, subjectId))
      .toPromise()
      .then(response => {
        return response as Subject;
      });
  }

  deleteSubject(universityId, subjectId): Promise<void> {
    return this.http
      .delete(this.buildSubjectUrl(universityId, subjectId))
      .toPromise()
      .then(() => {});
  }

  buildSubjectUrl(universityId, subjectId): string {
    return `${environment.API_URL}/universities/university/${universityId}/subjects/subject/${subjectId}`
  }
}
