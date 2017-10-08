import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {Observable} from "rxjs/Observable";
import {Faculty} from "../models/faculty";
import {SubjectHubApiResponse} from "../models/subject-hub-api-response";
import {environment} from "../../environments/environment";

import 'rxjs/add/operator/toPromise';

@Injectable()
export class FacultySearchService {
    constructor(private http: Http) {}
    // Replace the action with basic fetchFaculties and fetchSubjectsByFaculty

    // ignore subjecthubapi response for now -> just return a Promise<Faculty[]>

    fetchFaculties(universityId): Promise<Faculty[]> {
        return this.http.get(`${environment.API_URL}/universities/university/${universityId}/faculties`)
            .toPromise()
            .then(response => response.json() as Faculty[])
            .catch(err => err);
    }


    // fetch(): Promise<SubjectHubApiResponse<Faculty[]>> {
    //     return this.facultiesAction('/faculties', 'No faculties found', 'Faculty request failed')
    // }
    //
    // searchByCode(code: string): Promise<SubjectHubApiResponse<Faculty[]>> {
    //     return this.facultiesAction(`/faculties?code=${code}`,
    //         `No faculties found for code: ${code}`, `Faculties request failed`)
    // }
    //
    // facultiesAction(
    //     url: string,
    //     errorMessageApi: string,
    //     errorMessageRequest: string
    // ): Promise<SubjectHubApiResponse<Faculty[]>> {
    //     return this.http
    //         .get(environment.API_URL + url)
    //         .toPromise()
    //         .then(response => {
    //             const wasSuccessful = response.status == 200;
    //             if(wasSuccessful){
    //                 console.log("Response was successful!");
    //                 console.log(response.json());
    //                 return new SubjectHubApiResponse(wasSuccessful, response.json() as Faculty[]);
    //             } else {
    //                 console.log(errorMessageApi);
    //                 return new SubjectHubApiResponse(!wasSuccessful, null, response.json()['message']);
    //             }
    //         })
    //         .catch(e => {
    //             console.log(`${errorMessageRequest} Caught exception: ${e}`);
    //             return new SubjectHubApiResponse(false, null, e.json()['message']);
    //         });
    // }
}
