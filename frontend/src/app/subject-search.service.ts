import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';
import { SubjectResult } from './subject-result';

@Injectable()
export class SubjectSearchService {
  constructor(private http: Http) {}

  search(term: string, selectedRoute: string): Observable<SubjectResult[]> {
    console.log(term, selectedRoute);
    if (!selectedRoute) {
      selectedRoute = 'title';
    }
    return this.http
      .get(`api/subjects/?${selectedRoute}=${term}`)
      .map(response => response.json().data as SubjectResult[]);
  }

  fetch(): Promise<SubjectResult[]> {
    return this.http
      .get('api/subjects')
      .toPromise()
      .then(response => response.json().data as SubjectResult[]);
  }
}
