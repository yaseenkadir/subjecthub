import {Injectable} from '@angular/core';
import {Http} from "@angular/http";
import {Tag} from '../models/Tag';

import {environment} from '../../environments/environment';
import {SubjectHubApiResponse} from "../models/subject-hub-api-response";

@Injectable()
export class AddTagService {
    constructor(private http: Http) {
    }

    createTag(universityID: number, subjectId: number, tag: Tag): Promise<SubjectHubApiResponse<Tag>> {
        return this.http.post(`${environment.API_URL}/universities/university/${universityID}/subjects/subject/${subjectId}/addTag`, tag)
            .toPromise()
            .then(res => res.json() as SubjectHubApiResponse<Tag>);
    }
}
