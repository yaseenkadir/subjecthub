import { Injectable } from '@angular/core';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/share';
import { environment } from '../../environments/environment';
import { SubjectComment } from '../models/subject-comment';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class SubjectCommentsService {
  constructor(private http: HttpClient) {
  }

  fetch(university: number, subject: number): Promise<SubjectComment[]> {
    return this.commentsAction(`/universities/university/${university}/subjects/subject/${subject}/comments`,
      `No comments found for subject id: ${subject}`, `Comments request failed`);
  }

  add(university: number, subject: number, comment: object): Promise<SubjectComment> {
    let requestBody = comment;
    return this.commentActionPost(`/universities/university/${university}/subjects/subject/${subject}/comments/comment/add`,
      requestBody, `Unable to add comment for subject id: ${subject}`, `Add comment request failed`);
  }

  addThumbUp(university: number, subject: number, comment: number): Promise<SubjectComment> {
    return this.commentAction(`/universities/university/${university}/subjects/subject/${subject}/comments/comment/${comment}/addThumbUp`,
      `Unable to add thumb up to comment (id:${comment}) for subject id: ${subject}`,
      `Add thumb up to comment (id:${comment}) request failed`);
  }

  addThumbDown(university: number, subject: number, comment: number): Promise<SubjectComment> {
    return this.commentAction(`/universities/university/${university}/subjects/subject/${subject}/comments/comment/${comment}/addThumbDown`,
      `Unable to add thumb down to comment (id:${comment}) for subject id: ${subject}`,
      `Add thumb down to comment (id:${comment}) request failed.`);
  }

  flag(university: number, subject: number, comment: number): Promise<SubjectComment> {
    return this.commentAction(`/universities/university/${university}/subjects/subject/${subject}/comments/comment/${comment}/flag`,
      `Unable to flag comment (id:${comment}) for subject id: ${subject}`, `Flag comment (id:${comment}) request failed.`);
  }

  unFlag(university: number, subject: number, comment: number): Promise<SubjectComment> {
    return this.commentAction(`/universities/university/${university}/subjects/subject/${subject}/comments/comment/${comment}/unflag`,
      `Unable to unflag comment (id:${comment}) for subject id: ${subject}`, `Unflag comment (id:${comment}) request failed.`);
  }

  /**
   * UTIL method - returns comment list
   * @param {string} url
   * @param {string} errorMessageApi
   * @param {string} errorMessageRequest
   * @returns {Promise<SubjectComment[]>}
   */
  commentsAction(url: string, errorMessageApi: string, errorMessageRequest: string): Promise<SubjectComment[]> {
    return this.http
      .get(environment.API_URL + url)
      .toPromise()
      .then(response => {
        return response as SubjectComment[];
      });
  }

  /**
   * UTIL method - returns single comment
   * @param {string} url
   * @param {string} errorMessageApi
   * @param {string} errorMessageRequest
   * @returns {Promise<SubjectComment>}
   */
  commentAction(url: string,
                errorMessageApi: string,
                errorMessageRequest: string): Promise<SubjectComment> {
    return this.http
      .get(environment.API_URL + url)
      .toPromise()
      .then(response => {
        return response as SubjectComment;
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
  commentActionPost(url: string,
                    requestBody: object,
                    errorMessageApi: string,
                    errorMessageRequest: string): Promise<SubjectComment> {
    return this.http
      .post(environment.API_URL + url, requestBody)
      .toPromise()
      .then(response => {
        return response as SubjectComment;
      });
  }
}
