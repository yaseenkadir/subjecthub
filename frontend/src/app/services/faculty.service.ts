import { Injectable } from "@angular/core";
import { HttpClient } from '@angular/common/http';
import 'rxjs/add/operator/toPromise';
import { Faculty } from "../models/faculty";
import { ApiUrlUtils } from '../utils/utils';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class FacultyService {
  constructor(private http: HttpClient) {
  }

  fetch(universityId: number | string): Promise<Faculty[]> {
    return this.handleFacultiesResponse(
      this.http.get(ApiUrlUtils.buildFacultiesUrl(universityId))
    );
  }

  getFaculty(universityId: number | string, facultyId: number | string): Promise<Faculty> {
    return this.handleFacultyResponse(
      this.http.get(ApiUrlUtils.buildFacultyUrl(universityId, facultyId))
    );
  }

  editFaculty(universityId: number | string, facultyId: number | string, faculty: Faculty): Promise<Faculty> {
    return this.handleFacultyResponse(
      this.http.put(ApiUrlUtils.buildFacultyUrl(universityId, facultyId), faculty)
    );
  }

  createFaculty(universityId: number | string, faculty: Faculty): Promise<Faculty> {
    return this.handleFacultyResponse(
      this.http.post(ApiUrlUtils.buildFacultiesUrl(universityId) + '/faculty', faculty)
    );
  }

  deleteFaculty(universityId: number | string, facultyId: number | string): Promise<void> {
    return this.http
      .delete(ApiUrlUtils.buildFacultyUrl(universityId, facultyId))
      .toPromise()
      .then(() => {});
  }

  private handleFacultyResponse(response: Observable<any>): Promise<Faculty> {
    return response.toPromise().then(r => {
      return r as Faculty;
    });
  }

  private handleFacultiesResponse(response: Observable<any>): Promise<Faculty[]> {
    return response.toPromise().then(r => {
      return r as Faculty[];
    });
  }
}
