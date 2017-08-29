import { Component, OnInit } from '@angular/core';

import { Observable } from 'rxjs/Observable';
import { Subject } from 'rxjs/Subject';
import * as Fuse from 'fuse.js';

import 'rxjs/add/observable/of';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/toPromise';

import { SubjectSearchService } from '../app/subject-search.service';
import { SubjectResult } from '../app/subject-result';

@Component({
  selector: 'fuse-search-bar',
  templateUrl: './fuse-search-bar.component.html',
  styleUrls: ['./search-bar.component.css'],
  providers: [SubjectSearchService],
})
export class FuseSearchBarComponent implements OnInit {
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
      'title',
      'courseNumber',
      'availability',
      'studentType',
      'description',
    ],
  };
  constructor(private subjectSearchService: SubjectSearchService) {}

  search(term: string): void {
    this.subjects = this.fuse.search(term);
  }

  ngOnInit(): void {
    this.subjectSearchService
      .fetch()
      .then(subjects => {
        return subjects.map(subject => {
          const formatted = {};
          Object.keys(subject).forEach(key => {
            formatted[key] = subject[key];
            if (key === 'courseNumber')
              formatted[key] = formatted[key].toString();
          });
          return formatted;
        });
      })
      .then(subjects => {
        this.fuse = new Fuse(subjects, this.options);
      });
  }
}
