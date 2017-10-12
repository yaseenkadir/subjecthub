import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { UniversityService } from '../services/university.service';
import { University } from '../models/university';

@Component({
  selector: 'app-search-page',
  templateUrl: './search-page.component.html',
  styleUrls: ['./search-page.component.css'],
  providers: [UniversityService]
})
export class SearchPageComponent implements OnInit {

  university: University;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private universitySearchService: UniversityService) {
  }


  ngOnInit() {
    this.route.paramMap
      .switchMap((params: ParamMap) => {
        const universityId = params.get('university');
        return this.universitySearchService.getUniversity(universityId);
      })
      .subscribe(universityApiResponse => {
        this.university = universityApiResponse;
      });
  }

}
