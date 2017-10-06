import { Injectable } from '@angular/core';
import { Http } from '@angular/http';


import {environment} from '../../environments/environment';

import 'rxjs/add/operator/toPromise';

import { Subject } from '../models/subject';

@Injectable()
export class SubjectService {
    constructor(private http: Http) {}

    fetch(universityId, subjectId): Promise<Subject> {
        return this.http
            .get(`${environment.API_URL}/universities/university/${universityId}/subjects/subject/${subjectId}`)
            .toPromise()
            .then(response => {
                return response.json() as Subject;
            });
    }
}
