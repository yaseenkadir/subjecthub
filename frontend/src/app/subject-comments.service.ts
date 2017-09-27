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
                return response.json() as SubjectComment[];
            });
    }

    add(university: number, subject: number, message: string): void{
        //unsure if this post works
        this.http
            .post(`http://localhost:8080/api/universities/university/${university}/subjects/subject/${subject}/comments/comment/add`,{"message":`${message}`})
        //assuming no return is needed since fetch was promise
    }

    thumbUp(university: number, subject: number, comment: number): void{
        this.http
            .get(`http://localhost:8080/api/universities/university/${university}/subjects/subject/${subject}/comments/comment/${comment}/thumbUp`)
        //assuming no return is needed since fetch was promise
    }

    thumbDown(university: number, subject: number, comment: number): void{
        this.http
            .get(`http://localhost:8080/api/universities/university/${university}/subjects/subject/${subject}/comments/comment/${comment}/thumbDown`)
        //assuming no return is needed since fetch was promise
    }

    flag(university: number, subject: number, comment: number): void{
        this.http
            .get(`http://localhost:8080/api/universities/university/${university}/subjects/subject/${subject}/comments/comment/${comment}/flag`)
        //assuming no return is needed since fetch was promise
    }

    unFlag(university: number, subject: number, comment: number): void{
        this.http
            .get(`http://localhost:8080/api/universities/university/${university}/subjects/subject/${subject}/comments/comment/${comment}/unflag`)
        //assuming no return is needed since fetch was promise
    }
}
