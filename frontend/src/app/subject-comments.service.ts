import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/share';
import { SubjectComment } from './subject-comment';

@Injectable()
export class SubjectCommentsService {
    constructor(private http: Http) {}

    fetch(university: number, subject: number): Promise<SubjectComment[]>{
        return this.commentsAction(`http://localhost:8080/api/universities/university/${university}/subjects/subject/${subject}/comments`,
            `No comments found for subject id: ${subject}`,`Comments request failed`);
    }

    add(university: number, subject: number, message: string): Promise<SubjectComment>{
        let requestBody = {"message": message};
        return this.commentActionPost(`http://localhost:8080/api/universities/university/${university}/subjects/subject/${subject}/comments/comment/add`,
            requestBody,`Unable to add comment for subject id: ${subject}`,`Add comment request failed`)
    }

    addThumbUp(university: number, subject: number, comment: number): Promise<SubjectComment>{
        return this.commentAction(`http://localhost:8080/api/universities/university/${university}/subjects/subject/${subject}/comments/comment/${comment}/addThumbUp`,
            `Unable to add thumb up to comment (id:${comment}) for subject id: ${subject}`,
            `Add thumb up to comment (id:${comment}) request failed`)
    }

    addThumbDown(university: number, subject: number, comment: number): Promise<SubjectComment>{
        return this.commentAction(`http://localhost:8080/api/universities/university/${university}/subjects/subject/${subject}/comments/comment/${comment}/addThumbDown`,
            `Unable to add thumb down to comment (id:${comment}) for subject id: ${subject}`,
            `Add thumb down to comment (id:${comment}) request failed.`)
    }

    flag(university: number, subject: number, comment: number): Promise<SubjectComment>{
        return this.commentAction(`http://localhost:8080/api/universities/university/${university}/subjects/subject/${subject}/comments/comment/${comment}/flag`,
            `Unable to flag comment (id:${comment}) for subject id: ${subject}`, `Flag comment (id:${comment}) request failed.`)
    }

    unFlag(university: number, subject: number, comment: number): Promise<SubjectComment>{
        return this.commentAction(`http://localhost:8080/api/universities/university/${university}/subjects/subject/${subject}/comments/comment/${comment}/unflag`,
            `Unable to unflag comment (id:${comment}) for subject id: ${subject}`,`Unflag comment (id:${comment}) request failed.`)
    }

    /**
     * UTIL method - returns comment list
     * @param {string} url
     * @param {string} errorMessageApi
     * @param {string} errorMessageRequest
     * @returns {Promise<SubjectComment[]>}
     */
    commentsAction(url: string, errorMessageApi: string, errorMessageRequest: string): Promise<SubjectComment[]>{
        return this.http
            .get(url)
            .toPromise()
            .then(response => {
                if(response.status == 200){
                    return response.json() as SubjectComment[];
                } else {
                    console.log(errorMessageApi);
                    return null;
                }
            })
            .catch(e => {
                console.log(`${errorMessageRequest} Caught exception: ${e}`);
                return null;
            });
    }

    /**
     * UTIL method - returns single comment
     * @param {string} url
     * @param {string} errorMessageApi
     * @param {string} errorMessageRequest
     * @returns {Promise<SubjectComment>}
     */
    commentAction(url: string, errorMessageApi: string, errorMessageRequest: string): Promise<SubjectComment>{
        return this.http
            .get(url)
            .toPromise()
            .then(response => {
                if(response.status == 200){
                    return response.json() as SubjectComment;
                } else {
                    console.log(errorMessageApi);
                    return null;
                }
            })
            .catch(e => {
                console.log(`${errorMessageRequest} Caught exception: ${e}`);
                return null;
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
    commentActionPost(url: string, requestBody: object, errorMessageApi: string, errorMessageRequest: string): Promise<SubjectComment>{
        return this.http
            .post(url,requestBody)
            .toPromise()
            .then(response => {
                if(response.status == 200){
                    return response.json() as SubjectComment;
                } else {
                    console.log(errorMessageApi);
                    return null;
                }
            })
            .catch(e => {
                console.log(`${errorMessageRequest} Caught exception: ${e}`);
                return null;
            });
    }
}
