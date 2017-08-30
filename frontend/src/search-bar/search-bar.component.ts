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
  subjects: SubjectResult[];
  count: number;

  private searchTerms = new Subject<string>();
  selectedOption: Option;

  options = [
    new Option('Name', 'name'),
    new Option('Subject Code', 'subjectCode'),
    new Option('Faculty', 'facultyName'),
    new Option('Credit Points', 'creditPoints'),
  ];

  constructor(private subjectSearchService: SubjectSearchService) {
    this.selectedOption = this.options[0];
  }

  search(term: string): void {
    this.searchTerms.next(term);
  }

  ngOnInit(): void {
    this.searchTerms
      .debounceTime(600)
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
      })
      .subscribe(subjects => {
        this.subjects = subjects;
        this.count = subjects.length;
      });
  }
}
