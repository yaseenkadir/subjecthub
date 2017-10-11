import { Injectable } from "@angular/core";
import { HttpClient } from '@angular/common/http';
import 'rxjs/add/operator/toPromise';
import { Faculty } from "../models/faculty";
import { environment } from "../../environments/environment";

@Injectable()
export class FacultySearchService {
  constructor(private http: HttpClient) {
  }

  fetch(universityId: string): Promise<Faculty[]> {
    return this.http
      .get(`${environment.API_URL}/universities/university/${universityId}/faculties`)
      .toPromise()
      .then(response => {
        return response as Faculty[];
      });

  }
}
