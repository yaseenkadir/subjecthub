import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/share';
import { University } from '../models/university';
import {environment} from '../../environments/environment';
import {SubjectHubApiResponse} from "../models/subject-hub-api-response";

@Injectable()
export class UniversitySearchService {
    constructor(private http: Http) {}

    fetch(): Promise<SubjectHubApiResponse<University[]>> {
        return this.universitiesAction(`/universities`,
            `No universities found`, `Universities request failed`);
    }

    searchByNameAndAbbreviation(name: string, abbreviation: string): Promise<SubjectHubApiResponse<University[]>> {
        return this.universitiesAction(`/universities?name=${name}&abbreviation=${abbreviation}`,
            `No universities found for name: ${name} and abbreviation: ${abbreviation}`, `Universities request failed`);
    }

    searchByName(name: string): Promise<SubjectHubApiResponse<University[]>> {
        return this.universitiesAction(`/universities?name=${name}`,
            `No universities found for name: ${name}`, `Universities request failed`);
    }

    searchByAbbreviation(abbreviation: string): Promise<SubjectHubApiResponse<University[]>> {
        return this.universitiesAction(`/universities?abbreviation=${abbreviation}`,
            `No universities found for abbreviation: ${abbreviation}`, `Universities request failed`);
    }

    searchById(id: number): Promise<SubjectHubApiResponse<University>> {
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
    universitiesAction(
        url: string,
        errorMessageApi: string,
        errorMessageRequest: string
    ): Promise<SubjectHubApiResponse<University[]>>{
        return this.http
            .get(environment.API_URL + url)
            .toPromise()
            .then(response => {
                const wasSuccessful = response.status == 200;
                if(wasSuccessful){
                    return new SubjectHubApiResponse(wasSuccessful, response.json() as University[]);
                } else {
                    console.log(errorMessageApi);
                    return new SubjectHubApiResponse(!wasSuccessful, null, response.json()['message']);
                }
            })
            .catch(e => {
                console.log(`${errorMessageRequest} Caught exception: ${e}`);
                return new SubjectHubApiResponse(false, null, e.json()['message']);
            });
    }

    /**
     * UTIL method - returns single university
     * @param {string} url
     * @param {string} errorMessageApi
     * @param {string} errorMessageRequest
     * @returns {Promise<University>}
     */
    universityAction(
        url: string,
        errorMessageApi: string,
        errorMessageRequest: string
    ): Promise<SubjectHubApiResponse<University>>{
        return this.http
            .get(environment.API_URL + url)
            .toPromise()
            .then(response => {
                const wasSuccessful = response.status == 200;
                if(wasSuccessful){
                    return new SubjectHubApiResponse(wasSuccessful, response.json() as University);
                } else {
                    console.log(errorMessageApi);
                    return new SubjectHubApiResponse(!wasSuccessful, null, response.json()['message']);
                }
            })
            .catch(e => {
                console.log(`${errorMessageRequest} Caught exception: ${e}`);
                return new SubjectHubApiResponse(false, e.json(), e.json()['message']);
            });
    }
}
