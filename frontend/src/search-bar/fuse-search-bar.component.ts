import { Input, Component, OnInit } from "@angular/core";
import { Router } from '@angular/router';

import * as Fuse from "fuse.js";

import "rxjs/add/observable/of";
import "rxjs/add/operator/switchMap";
import "rxjs/add/operator/toPromise";



import { SubjectSearchService } from "../app/services/subject-search.service";

import { University } from '../app/models/university';

@Component({
  selector: "fuse-search-bar",
  templateUrl: "./fuse-search-bar.component.html",
  styleUrls: ["./search-bar.component.css"],
  providers: [SubjectSearchService]
})
export class FuseSearchBarComponent implements OnInit {
  @Input ()
  university: University;

  subjects: {}[];
  fuse: Fuse;
  options = {
    shouldSort: true,
    // tokenize: true,
    // matchAllTokens: true,

    threshold: 0.4,
    location: 0,
    distance: 10000,
    maxPatternLength: 32,
    minMatchCharLength: 3,
    keys: [
      "code",
      "name",
      "availability",
      "university.name",
      "university.abbreviation",
      "creditPoints",
      "description"
    ]
  };
  constructor(private router: Router,private subjectSearchService: SubjectSearchService) {
    this.subjects = [];
  }

  search(term: string): void {
    if (!term) return;
    this.subjects = this.fuse.search(term);
  }

  ngOnInit(): void {
    this.subjectSearchService
      .fetch(this.university.id.toString())
      .then(subjects => {
        this.subjects = subjects;
        return subjects
      })
      .then(subjects => {

        this.fuse = new Fuse(subjects, this.options);
      })
      .catch(err => {
        console.log(err);
      });
  }

  goToSubjectDetails(subject) {
      console.log(subject);
      this.router.navigate([`/university/${this.university.id}/subject/${subject.id}`]);
  }
}
