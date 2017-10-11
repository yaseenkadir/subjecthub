import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/share';
import { University } from '../models/university';
import { environment } from '../../environments/environment';

@Injectable()
export class UniversitySearchService {

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
    return this.universitiesAction(`/universities?${httpParams}`,
      `No universities found`, `Universities request failed`);
  }

  searchById(id: string): Promise<University> {
    return this.universityAction(`/universities/university/${id}`,
      `No university found for id: ${id}`, `University request failed`);
  }

  /**
   * UTIL method - returns university list
   * @param {string} url
   * @param {string} errorMessageApi
   * @param {string} errorMessageRequest
   * @returns {Promise<University[]>}
   */
  universitiesAction(url: string,
                     errorMessageApi: string,
                     errorMessageRequest: string): Promise<University[]> {
    return this.http
      .get(environment.API_URL + url)
      .toPromise()
      .then(response => {
        return response as University[];
      });
  }

  /**
   * UTIL method - returns single university
   * @param {string} url
   * @param {string} errorMessageApi
   * @param {string} errorMessageRequest
   * @returns {Promise<University>}
   */
  universityAction(url: string,
                   errorMessageApi: string,
                   errorMessageRequest: string): Promise<University> {
    return this.http
      .get(environment.API_URL + url)
      .toPromise()
      .then(response => {
        return response as University;
      });
  }
}
