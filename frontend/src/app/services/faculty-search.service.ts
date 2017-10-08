import {Injectable} from "@angular/core";
import {Http} from "@angular/http";


import {SubjectHubApiResponse} from "../models/subject-hub-api-response";
import {environment} from "../../environments/environment";

import 'rxjs/add/operator/toPromise';

import {Faculty} from "../models/faculty";

@Injectable()
export class FacultySearchService {
    constructor(private http: Http) {}

    fetch(universityId: string): Promise<Faculty[]> {
        return this.http
            .get(`${environment.API_URL}/universities/university/${universityId}/faculties`)
            .toPromise()
            .then(response => {
                return response.json() as Faculty[];
            });

    }
}
