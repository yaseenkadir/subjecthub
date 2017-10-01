import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/share';
import {environment} from '../../environments/environment';
import { SubjectComment } from '../models/subject-comment';
import {SubjectHubApiResponse} from "../models/subject-hub-api-response";

@Injectable()
export class SubjectCommentsService {
    constructor(private http: Http) {}

    fetch(university: number, subject: number): Promise<SubjectHubApiResponse>{
        return this.commentsAction(`/universities/university/${university}/subjects/subject/${subject}/comments`,
            `No comments found for subject id: ${subject}`,`Comments request failed`);
    }

    add(university: number, subject: number, message: string): Promise<SubjectHubApiResponse>{
        let requestBody = {"message": message};
        return this.commentActionPost(`/universities/university/${university}/subjects/subject/${subject}/comments/comment/add`,
            requestBody,`Unable to add comment for subject id: ${subject}`,`Add comment request failed`)
    }

    addThumbUp(university: number, subject: number, comment: number): Promise<SubjectHubApiResponse>{
        return this.commentAction(`/universities/university/${university}/subjects/subject/${subject}/comments/comment/${comment}/addThumbUp`,
            `Unable to add thumb up to comment (id:${comment}) for subject id: ${subject}`,
            `Add thumb up to comment (id:${comment}) request failed`)
    }

    addThumbDown(university: number, subject: number, comment: number): Promise<SubjectHubApiResponse>{
        return this.commentAction(`/universities/university/${university}/subjects/subject/${subject}/comments/comment/${comment}/addThumbDown`,
            `Unable to add thumb down to comment (id:${comment}) for subject id: ${subject}`,
            `Add thumb down to comment (id:${comment}) request failed.`)
    }

    flag(university: number, subject: number, comment: number): Promise<SubjectHubApiResponse>{
        return this.commentAction(`/universities/university/${university}/subjects/subject/${subject}/comments/comment/${comment}/flag`,
            `Unable to flag comment (id:${comment}) for subject id: ${subject}`, `Flag comment (id:${comment}) request failed.`)
    }

    unFlag(university: number, subject: number, comment: number): Promise<SubjectHubApiResponse>{
        return this.commentAction(`/universities/university/${university}/subjects/subject/${subject}/comments/comment/${comment}/unflag`,
            `Unable to unflag comment (id:${comment}) for subject id: ${subject}`,`Unflag comment (id:${comment}) request failed.`)
    }

    /**
     * UTIL method - returns comment list
     * @param {string} url
     * @param {string} errorMessageApi
     * @param {string} errorMessageRequest
     * @returns {Promise<SubjectComment[]>}
     */
    commentsAction(url: string, errorMessageApi: string, errorMessageRequest: string): Promise<SubjectHubApiResponse>{
        return this.http
            .get(environment.API_URL + url)
            .toPromise()
            .then(response => {
                if(response.status == 200){
                    return new SubjectHubApiResponse(true, response.json() as SubjectComment[]);
                } else {
                    console.log(errorMessageApi);
                    return new SubjectHubApiResponse(false, response.json());
                }
            })
            .catch(e => {
                console.log(`${errorMessageRequest} Caught exception: ${e}`);
                return new SubjectHubApiResponse(false, e.json());
            });
    }

    /**
     * UTIL method - returns single comment
     * @param {string} url
     * @param {string} errorMessageApi
     * @param {string} errorMessageRequest
     * @returns {Promise<SubjectComment>}
     */
    commentAction(url: string, errorMessageApi: string, errorMessageRequest: string): Promise<SubjectHubApiResponse>{
        return this.http
            .get(environment.API_URL + url)
            .toPromise()
            .then(response => {
                if(response.status == 200){
                    return new SubjectHubApiResponse(true, response.json() as SubjectComment);
                } else {
                    console.log(errorMessageApi);
                    return new SubjectHubApiResponse(false, response.json());
                }
            })
            .catch(e => {
                console.log(`${errorMessageRequest} Caught exception: ${e}`);
                return new SubjectHubApiResponse(false, e.json());
            });
    }

    /**
     * UTIL method - returns single comment, isPost
     * @param {string} url
     * @param {Object} requestBody
     * @param {string} errorMessageApi
     * @param {string} errorMessageRequest
     * @returns {Promise<SubjectComment>}
     */
    commentActionPost(url: string, requestBody: object, errorMessageApi: string, errorMessageRequest: string): Promise<SubjectHubApiResponse>{
        return this.http
            .post(environment.API_URL + url, requestBody)
            .toPromise()
            .then(response => {
                if(response.status == 200){
                    return new SubjectHubApiResponse(true, response.json() as SubjectComment);
                } else {
                    console.log(errorMessageApi);
                    return new SubjectHubApiResponse(false, response.json());
                }
            })
            .catch(e => {
                console.log(`${errorMessageRequest} Caught exception: ${e}`);
                return new SubjectHubApiResponse(false, e.json());
            });
    }
}
