import { Component, OnInit } from '@angular/core';

import { Observable } from 'rxjs/Observable';
import { Subject } from 'rxjs/Subject';

import 'rxjs/add/observable/of';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/distinctUntilChanged';

import { SubjectSearchService } from '../app/subject-search.service';
import { SubjectResult } from '../app/subject-result';

class Option {
  name: string;
  value: string;
  constructor(name: string, value: string) {
    this.name = name;
    this.value = value;
  }
}

@Component({
  selector: 'app-search-bar',
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.css'],
  providers: [SubjectSearchService],
})
export class SearchBarComponent implements OnInit {
  subjects: Observable<SubjectResult[]>;
  count: number;

  private searchTerms = new Subject<string>();
  selectedOption: Option;

  options = [
    new Option('Title', 'title'),
    new Option('Course Number', 'courseNumber'),
    new Option('Pre-requisites', 'requisite'),
    new Option('Availability', 'availability'),
    new Option('studentType', 'Course Type'),
  ];

  constructor(private subjectSearchService: SubjectSearchService) {
    this.selectedOption = this.options[0];
  }

  search(term: string): void {
    this.subjects.subscribe(t => (this.count = t.length));
    this.searchTerms.next(term);
  }

  ngOnInit(): void {
    this.subjects = this.searchTerms
      .debounceTime(300)
      .distinctUntilChanged()
      .switchMap(
        term =>
          term
            ? this.subjectSearchService.search(term, this.selectedOption.value)
            : Observable.of<SubjectResult[]>([])
      )
      .catch(error => {
        console.log(error);
        return Observable.of<SubjectResult[]>([]);
      });
  }
}
