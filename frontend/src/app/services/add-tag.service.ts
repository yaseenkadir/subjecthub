import { Injectable } from '@angular/core';
import { Tag } from '../models/Tag';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class AddTagService {
  constructor(private http: HttpClient) {
  }

  createTag(universityID: number, subjectId: number, tag: Tag): Promise<Tag> {
    return this.http.post(`${environment.API_URL}/universities/university/${universityID}/subjects/subject/${subjectId}/addTag`, tag)
      .toPromise()
      .then(res => res as Tag);
  }
}
