import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/share';
import { University } from '../models/university';
import { Observable } from 'rxjs/Observable';
import { ApiUrlUtils } from '../utils/utils';

@Injectable()
export class UniversityService {

  constructor(private http: HttpClient) {
  }

  getUniversities(name?: string, abbreviation?: string): Promise<University[]> {
    let httpParams = new HttpParams();
    let paramPairs = {'name': name, 'abbreviation': abbreviation};
    for (let key in paramPairs) {
      if (paramPairs[key] != null) {
        httpParams = httpParams.append(key, paramPairs[key]);
      }
    }

    return this.handleUniversitiesResponse(
      this.http.get(`${ApiUrlUtils.universitiesUrl()}?${httpParams}`)
    );
  }

  getUniversity(universityId: string): Promise<University> {
    return this.handleUniversityResponse(
      this.http.get(ApiUrlUtils.buildUniversityUrl(universityId))
    );
  }

  editUniveristy(universityId: number | string, university: University): Promise<University> {
    return this.handleUniversityResponse(
      this.http.put(ApiUrlUtils.buildUniversityUrl(universityId), university)
    );
  }

  createUniversity(university: University): Promise<University> {
    return this.handleUniversityResponse(
      this.http.post(ApiUrlUtils.universitiesUrl() + '/university', university)
    );
  }

  deleteUniversity(universityId: number | string): Promise<void> {
    return this.http.delete(ApiUrlUtils.buildUniversityUrl(universityId))
      .toPromise()
      .then(() => {});
  }

  private handleUniversitiesResponse(response: Observable<any>): Promise<University[]> {
    return response.toPromise().then(r => {return r as University[]});
  }

  private handleUniversityResponse(response: Observable<any>): Promise<University> {
    return response.toPromise().then(r => {return r as University});
  }
}
