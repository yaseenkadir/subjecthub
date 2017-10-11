import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/share';
import { environment } from '../../environments/environment';
import { Subject } from '../models/subject';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class SubjectSearchService {
  constructor(private http: HttpClient) {
  }

  search(term: string, selectedRoute: string): Observable<Subject[]> {
    if (!selectedRoute) {
      selectedRoute = 'title';
    }

    return this.http
      .get(`http://localhost:8080/api/subjects?${selectedRoute}=${term}`)
      .map(response => {
        return response as Subject[];
      })
      .share();
  }

  fetch(universityId: string): Promise<Subject[]> {
    return this.http
      .get(`${environment.API_URL}/universities/university/${universityId}/subjects`)
      .toPromise()
      .then(response => {
        return response as Subject[];
      });
  }
}
