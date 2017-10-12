import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { UniversitySearchService } from '../services/university-search.service';
import { University } from '../models/university';

@Component({
  selector: 'app-search-page',
  templateUrl: './search-page.component.html',
  styleUrls: ['./search-page.component.css'],
  providers: [UniversitySearchService]
})
export class SearchPageComponent implements OnInit {

  university: University;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private universitySearchService: UniversitySearchService) {
  }


  ngOnInit() {
    this.route.paramMap
      .switchMap((params: ParamMap) => {
        const universityId = params.get('university');
        return this.universitySearchService.searchById(universityId);
      })
      .subscribe(universityApiResponse => {
        this.university = universityApiResponse;
      });
  }

}
