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
        return this.http
            .get(`http://localhost:8080/api/universities/university/${university}/subjects/subject/${subject}/comments`)
            .toPromise()
            .then(response => {
                if(response.status == 200){
                    return response.json() as SubjectComment[];
                } else {
                    console.log(`No comments found for subject id: ${subject}`);
                    return null;
                }
            })
            .catch(e => {
                console.log(`Comments request failed. Caught exception: ${e}`);
                return null;
            });
    }

    add(university: number, subject: number, message: string): Promise<SubjectComment>{
        let requestBody = {"message": message};
        return this.http
            .post(`http://localhost:8080/api/universities/university/${university}/subjects/subject/${subject}/comments/comment/add`,requestBody)
            .toPromise()
            .then(response => {
                if(response.status == 200){
                    return response.json() as SubjectComment;
                } else {
                    console.log(`Unable to add comment for subject id: ${subject}`);
                    return null;
                }
            })
            .catch(e => {
                console.log(`Add comment request failed. Caught exception: ${e}`);
                return null;
            });
    }

    addThumbUp(university: number, subject: number, comment: number): Promise<SubjectComment>{
        return this.http
            .get(`http://localhost:8080/api/universities/university/${university}/subjects/subject/${subject}/comments/comment/${comment}/addThumbUp`)
            .toPromise()
            .then(response => {
                if(response.status == 200){
                    return response.json() as SubjectComment;
                } else {
                    console.log(`Unable to add thumb up to comment (id:${comment}) for subject id: ${subject}`);
                    return null;
                }
            })
            .catch(e => {
                console.log(`Add thumb up to comment (id:${comment}) request failed. Caught exception: ${e}`);
                return null;
            });
    }

    addThumbDown(university: number, subject: number, comment: number): Promise<SubjectComment>{
        return this.http
            .get(`http://localhost:8080/api/universities/university/${university}/subjects/subject/${subject}/comments/comment/${comment}/addThumbDown`)
            .toPromise()
            .then(response => {
                if(response.status == 200){
                    return response.json() as SubjectComment;
                } else {
                    console.log(`Unable to add thumb down to comment (id:${comment}) for subject id: ${subject}`);
                    return null;
                }
            })
            .catch(e => {
                console.log(`Add thumb down to comment (id:${comment}) request failed. Caught exception: ${e}`);
                return null;
            });
    }

    remThumbUp(university: number, subject: number, comment: number): Promise<SubjectComment>{
        return this.http
            .get(`http://localhost:8080/api/universities/university/${university}/subjects/subject/${subject}/comments/comment/${comment}/remThumbUp`)
            .toPromise()
            .then(response => {
                if(response.status == 200){
                    return response.json() as SubjectComment;
                } else {
                    console.log(`Unable to remove thumb up from comment (id:${comment}) for subject id: ${subject}`);
                    return null;
                }
            })
            .catch(e => {
                console.log(`Remove thumb up from comment (id:${comment}) request failed. Caught exception: ${e}`);
                return null;
            });
    }

    remThumbDown(university: number, subject: number, comment: number): Promise<SubjectComment>{
        return this.http
            .get(`http://localhost:8080/api/universities/university/${university}/subjects/subject/${subject}/comments/comment/${comment}/remThumbDown`)
            .toPromise()
            .then(response => {
                if(response.status == 200){
                    return response.json() as SubjectComment;
                } else {
                    console.log(`Unable to remove thumb down from comment (id:${comment}) for subject id: ${subject}`);
                    return null;
                }
            })
            .catch(e => {
                console.log(`Remove thumb down from comment (id:${comment}) request failed. Caught exception: ${e}`);
                return null;
            });
    }

    remThumbs(university: number, subject: number, comment: number): Promise<SubjectComment>{
        return this.http
            .get(`http://localhost:8080/api/universities/university/${university}/subjects/subject/${subject}/comments/comment/${comment}/remThumbs`)
            .toPromise()
            .then(response => {
                if(response.status == 200){
                    return response.json() as SubjectComment;
                } else {
                    console.log(`Unable to remove all thumbs from comment (id:${comment}) for subject id: ${subject}`);
                    return null;
                }
            })
            .catch(e => {
                console.log(`Remove all thumbs from comment (id:${comment}) request failed. Caught exception: ${e}`);
                return null;
            });
    }

    flag(university: number, subject: number, comment: number): Promise<SubjectComment>{
        return this.http
            .get(`http://localhost:8080/api/universities/university/${university}/subjects/subject/${subject}/comments/comment/${comment}/flag`)
            .toPromise()
            .then(response => {
                if(response.status == 200){
                    return response.json() as SubjectComment;
                } else {
                    console.log(`Unable to flag comment (id:${comment}) for subject id: ${subject}`);
                    return null;
                }
            })
            .catch(e => {
                console.log(`Flag comment (id:${comment}) request failed. Caught exception: ${e}`);
                return null;
            });
    }

    unFlag(university: number, subject: number, comment: number): Promise<SubjectComment>{
        return this.http
            .get(`http://localhost:8080/api/universities/university/${university}/subjects/subject/${subject}/comments/comment/${comment}/unflag`)
            .toPromise()
            .then(response => {
                if(response.status == 200){
                    return response.json() as SubjectComment;
                } else {
                    console.log(`Unable to unflag comment (id:${comment}) for subject id: ${subject}`);
                    return null;
                }
            })
            .catch(e => {
                console.log(`Unflag comment (id:${comment}) request failed. Caught exception: ${e}`);
                return null;
            });
    }
}
