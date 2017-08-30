import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/share';
import { SubjectResult } from './subject-result';

@Injectable()
export class SubjectSearchService {
  constructor(private http: Http) {}

  search(term: string, selectedRoute: string): Observable<SubjectResult[]> {
    if (!selectedRoute) {
      selectedRoute = 'title';
    }
    return this.http
      .get(`http://localhost:8080/api/subjects?${selectedRoute}=${term}`)
      .map(response => {
        return response.json() as SubjectResult[];
      })
      .share();
  }

  fetch(): Promise<SubjectResult[]> {
    return this.http
      .get('http://localhost:8080/api/subjects')
      .toPromise()
      .then(response => {
        return response.json() as SubjectResult[];
      });
  }
}
