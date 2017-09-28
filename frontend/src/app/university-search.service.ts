import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/share';
import { UniversityResult } from './university-result';

@Injectable()
export class UniversitySearchService {
    constructor(private http: Http) {}

    search(term1: string, selectedRoute1: string, term2: string, selectedRoute2: string): Observable<UniversityResult[]> {
        if (!selectedRoute1) { // probably needs to change
            selectedRoute1 = 'title';
        }
        return this.http
            .get(`http://localhost:8080/api/universities?${selectedRoute1}=${term1}&${selectedRoute2}=${term2}`)
            .map(response => {
                return response.json() as UniversityResult[];
            })
            .share();
    }

    fetch(): Promise<UniversityResult[]> {
        return this.http
            .get('http://localhost:8080/api/universities')
            .toPromise()
            .then(response => {
                return response.json() as UniversityResult[];
            });
    }
}
